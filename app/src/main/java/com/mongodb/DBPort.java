package com.mongodb;

import com.mongodb.DB;
import com.mongodb.util.ThreadUtil;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: classes.dex */
public class DBPort {
    static final long CONN_RETRY_TIME_MS = 15000;
    public static final int PORT = 27017;
    static final boolean USE_NAGLE = false;
    private static Logger _rootLogger = Logger.getLogger("com.mongodb.port");
    private volatile ActiveState _activeState;
    final InetSocketAddress _addr;
    private Map<DB, Boolean> _authed;
    long _calls;
    final DBDecoder _decoder;
    final int _hashCode;
    private InputStream _in;
    int _lastThread;
    final Logger _logger;
    final MongoOptions _options;
    private OutputStream _out;
    final DBPortPool _pool;
    private boolean _processingResponse;
    final ServerAddress _sa;
    private Socket _socket;

    public DBPort(ServerAddress addr) {
        this(addr, null, new MongoOptions());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DBPort(ServerAddress addr, DBPortPool pool, MongoOptions options) {
        this._authed = new ConcurrentHashMap();
        this._calls = 0L;
        this._options = options;
        this._sa = addr;
        this._addr = addr.getSocketAddress();
        this._pool = pool;
        this._hashCode = this._addr.hashCode();
        this._logger = Logger.getLogger(_rootLogger.getName() + "." + addr.toString());
        this._decoder = this._options.dbDecoderFactory.create();
    }

    Response call(OutMessage msg, DBCollection coll) throws IOException {
        return go(msg, coll);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Response call(OutMessage msg, DBCollection coll, DBDecoder decoder) throws IOException {
        return go(msg, coll, false, decoder);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void say(OutMessage msg) throws IOException {
        go(msg, null);
    }

    private synchronized Response go(OutMessage msg, DBCollection coll) throws IOException {
        return go(msg, coll, false, null);
    }

    private synchronized Response go(OutMessage msg, DBCollection coll, DBDecoder decoder) throws IOException {
        return go(msg, coll, false, decoder);
    }

    /* JADX WARN: Finally extract failed */
    private synchronized Response go(OutMessage msg, DBCollection coll, boolean forceResponse, DBDecoder decoder) throws IOException {
        Response response = null;
        synchronized (this) {
            if (this._processingResponse && coll != null) {
                throw new IllegalStateException("DBPort.go called and expecting a response while processing another response");
            }
            this._calls++;
            if (this._socket == null) {
                _open();
            }
            if (this._out == null) {
                throw new IllegalStateException("_out shouldn't be null");
            }
            try {
                try {
                    msg.prepare();
                    this._activeState = new ActiveState(msg);
                    msg.pipe(this._out);
                    if (this._pool != null) {
                        this._pool._everWorked = true;
                    }
                    if (coll != null || forceResponse) {
                        this._processingResponse = true;
                        ServerAddress serverAddress = this._sa;
                        InputStream inputStream = this._in;
                        if (decoder == null) {
                            decoder = this._decoder;
                        }
                        response = new Response(serverAddress, coll, inputStream, decoder);
                        this._activeState = null;
                        this._processingResponse = false;
                    } else {
                        this._activeState = null;
                        this._processingResponse = false;
                    }
                } catch (IOException ioe) {
                    close();
                    throw ioe;
                }
            } catch (Throwable th) {
                this._activeState = null;
                this._processingResponse = false;
                throw th;
            }
        }
        return response;
    }

    synchronized CommandResult getLastError(DB db, WriteConcern concern) throws IOException {
        DBApiLayer dbAL;
        dbAL = (DBApiLayer) db;
        return runCommand(dbAL, concern.getCommand());
    }

    private synchronized Response findOne(DB db, String coll, DBObject q) throws IOException {
        Response res;
        OutMessage msg = OutMessage.query(db.getCollection(coll), 0, 0, -1, q, null);
        res = go(msg, db.getCollection(coll), null);
        return res;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CommandResult runCommand(DB db, DBObject cmd) throws IOException {
        Response res;
        res = findOne(db, "$cmd", cmd);
        return convertToCommandResult(cmd, res);
    }

    private CommandResult convertToCommandResult(DBObject cmd, Response res) {
        if (res.size() == 0) {
            return null;
        }
        if (res.size() > 1) {
            throw new MongoInternalException("something is wrong.  size:" + res.size());
        }
        DBObject data = res.get(0);
        if (data == null) {
            throw new MongoInternalException("something is wrong, no command result");
        }
        CommandResult cr = new CommandResult(cmd, res.serverUsed());
        cr.putAll(data);
        return cr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CommandResult tryGetLastError(DB db, long last, WriteConcern concern) throws IOException {
        return last != this._calls ? null : getLastError(db, concern);
    }

    public synchronized void ensureOpen() throws IOException {
        if (this._socket == null) {
            _open();
        }
    }

    boolean _open() throws IOException {
        long sleepTime = 100;
        long maxAutoConnectRetryTime = CONN_RETRY_TIME_MS;
        if (this._options.maxAutoConnectRetryTime > 0) {
            maxAutoConnectRetryTime = this._options.maxAutoConnectRetryTime;
        }
        long start = System.currentTimeMillis();
        while (true) {
            try {
                this._socket = this._options.socketFactory.createSocket();
                this._socket.connect(this._addr, this._options.connectTimeout);
                this._socket.setTcpNoDelay(true);
                this._socket.setKeepAlive(this._options.socketKeepAlive);
                this._socket.setSoTimeout(this._options.socketTimeout);
                this._in = new BufferedInputStream(this._socket.getInputStream());
                this._out = this._socket.getOutputStream();
                return true;
            } catch (IOException ioe) {
                IOException lastError = new IOException("couldn't connect to [" + this._addr + "] bc:" + ioe);
                this._logger.log(Level.INFO, "connect fail to : " + this._addr, (Throwable) ioe);
                close();
                if (!this._options.autoConnectRetry) {
                    throw lastError;
                }
                if (this._pool != null && !this._pool._everWorked) {
                    throw lastError;
                }
                long sleptSoFar = System.currentTimeMillis() - start;
                if (sleptSoFar >= maxAutoConnectRetryTime) {
                    throw lastError;
                }
                if (sleepTime + sleptSoFar > maxAutoConnectRetryTime) {
                    sleepTime = maxAutoConnectRetryTime - sleptSoFar;
                }
                this._logger.severe("going to sleep and retry.  total sleep time after = " + (sleptSoFar + sleptSoFar) + "ms  this time:" + sleepTime + "ms");
                ThreadUtil.sleep(sleepTime);
                sleepTime *= 2;
            }
        }
    }

    public int hashCode() {
        return this._hashCode;
    }

    public String host() {
        return this._addr.toString();
    }

    public ServerAddress serverAddress() {
        return this._sa;
    }

    public String toString() {
        return "{DBPort  " + host() + "}";
    }

    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActiveState getActiveState() {
        return this._activeState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLocalPort() {
        if (this._socket != null) {
            return this._socket.getLocalPort();
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void close() {
        this._authed.clear();
        if (this._socket != null) {
            try {
                this._socket.close();
            } catch (Exception e) {
            }
        }
        this._in = null;
        this._out = null;
        this._socket = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkAuth(DB db) throws IOException {
        DB.AuthenticationCredentials credentials = db.getAuthenticationCredentials();
        if (credentials == null) {
            if (!db._name.equals("admin")) {
                checkAuth(db._mongo.getDB("admin"));
            }
        } else if (!this._authed.containsKey(db)) {
            CommandResult res = runCommand(db, credentials.getNonceCommand());
            res.throwOnError();
            runCommand(db, credentials.getAuthCommand(res.getString("nonce"))).throwOnError();
            this._authed.put(db, true);
        }
    }

    public DBPortPool getPool() {
        return this._pool;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ActiveState {
        final OutMessage outMessage;
        final long startTime = System.nanoTime();
        final String threadName = Thread.currentThread().getName();

        ActiveState(OutMessage outMessage) {
            this.outMessage = outMessage;
        }
    }
}
