package com.mongodb;

import com.mongodb.ConnectionStatus;
import com.mongodb.DBPortPool;
import com.mongodb.MongoException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: classes.dex */
public class DBTCPConnector implements DBConnector {
    private final List<ServerAddress> _allHosts;
    private final AtomicBoolean _closed;
    private DynamicConnectionStatus _connectionStatus;
    private volatile Boolean _isMongosDirectConnection;
    private volatile DBPortPool _masterPortPool;
    private volatile int _maxBsonObjectSize;
    private final Mongo _mongo;
    private ThreadLocal<MyPort> _myPort;
    private DBPortPool.Holder _portHolder;
    static Logger _logger = Logger.getLogger(Bytes.LOGGER.getName() + ".tcp");
    static Logger _createLogger = Logger.getLogger(_logger.getName() + ".connect");

    public DBTCPConnector(Mongo m, ServerAddress addr) {
        this._closed = new AtomicBoolean(false);
        this._myPort = new ThreadLocal<MyPort>() { // from class: com.mongodb.DBTCPConnector.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public MyPort initialValue() {
                return new MyPort();
            }
        };
        this._mongo = m;
        this._portHolder = new DBPortPool.Holder(m._options);
        _checkAddress(addr);
        _createLogger.info(addr.toString());
        setMasterAddress(addr);
        this._allHosts = null;
    }

    public DBTCPConnector(Mongo m, ServerAddress... all) {
        this(m, (List<ServerAddress>) Arrays.asList(all));
    }

    public DBTCPConnector(Mongo m, List<ServerAddress> all) {
        this._closed = new AtomicBoolean(false);
        this._myPort = new ThreadLocal<MyPort>() { // from class: com.mongodb.DBTCPConnector.1
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public MyPort initialValue() {
                return new MyPort();
            }
        };
        this._mongo = m;
        this._portHolder = new DBPortPool.Holder(m._options);
        _checkAddress(all);
        this._allHosts = new ArrayList(all);
        _createLogger.info(all + " -> " + getAddress());
        this._connectionStatus = new DynamicConnectionStatus(m, this._allHosts);
    }

    public void start() {
        if (this._connectionStatus != null) {
            this._connectionStatus.start();
        }
    }

    private static ServerAddress _checkAddress(ServerAddress addr) {
        if (addr == null) {
            throw new NullPointerException("address can't be null");
        }
        return addr;
    }

    private static ServerAddress _checkAddress(List<ServerAddress> addrs) {
        if (addrs == null) {
            throw new NullPointerException("addresses can't be null");
        }
        if (addrs.size() == 0) {
            throw new IllegalArgumentException("need to specify at least 1 address");
        }
        return addrs.get(0);
    }

    @Override // com.mongodb.DBConnector
    public void requestStart() {
        this._myPort.get().requestStart();
    }

    @Override // com.mongodb.DBConnector
    public void requestDone() {
        this._myPort.get().requestDone();
    }

    @Override // com.mongodb.DBConnector
    public void requestEnsureConnection() {
        checkMaster(false, true);
        this._myPort.get().requestEnsureConnection();
    }

    void _checkClosed() {
        if (this._closed.get()) {
            throw new IllegalStateException("this Mongo has been closed");
        }
    }

    WriteResult _checkWriteError(DB db, DBPort port, WriteConcern concern) throws IOException {
        CommandResult e = port.runCommand(db, concern.getCommand());
        e.throwOnError();
        return new WriteResult(e, concern);
    }

    @Override // com.mongodb.DBConnector
    public WriteResult say(DB db, OutMessage m, WriteConcern concern) {
        return say(db, m, concern, null);
    }

    @Override // com.mongodb.DBConnector
    public WriteResult say(DB db, OutMessage m, WriteConcern concern, ServerAddress hostNeeded) {
        WriteResult writeResult;
        if (concern == null) {
            throw new IllegalArgumentException("Write concern is null");
        }
        _checkClosed();
        checkMaster(false, true);
        MyPort mp = this._myPort.get();
        DBPort port = mp.get(true, ReadPreference.primary(), hostNeeded);
        try {
            try {
                try {
                    port.checkAuth(db);
                    port.say(m);
                    if (concern.callGetLastError()) {
                        writeResult = _checkWriteError(db, port, concern);
                        mp.done(port);
                        m.doneWithMessage();
                    } else {
                        writeResult = new WriteResult(db, port, concern);
                        mp.done(port);
                        m.doneWithMessage();
                    }
                } catch (RuntimeException re) {
                    mp.error(port, re);
                    throw re;
                }
            } catch (MongoException me) {
                throw me;
            } catch (IOException ioe) {
                mp.error(port, ioe);
                _error(ioe, false);
                if (concern.raiseNetworkErrors()) {
                    throw new MongoException.Network("can't say something", ioe);
                }
                CommandResult res = new CommandResult(port.serverAddress());
                res.put("ok", (Object) false);
                res.put("$err", (Object) "NETWORK ERROR");
                writeResult = new WriteResult(res, concern);
                mp.done(port);
                m.doneWithMessage();
            }
            return writeResult;
        } catch (Throwable th) {
            mp.done(port);
            m.doneWithMessage();
            throw th;
        }
    }

    @Override // com.mongodb.DBConnector
    public Response call(DB db, DBCollection coll, OutMessage m, ServerAddress hostNeeded, DBDecoder decoder) {
        return call(db, coll, m, hostNeeded, 2, null, decoder);
    }

    @Override // com.mongodb.DBConnector
    public Response call(DB db, DBCollection coll, OutMessage m, ServerAddress hostNeeded, int retries) {
        return call(db, coll, m, hostNeeded, retries, null, null);
    }

    @Override // com.mongodb.DBConnector
    public Response call(DB db, DBCollection coll, OutMessage m, ServerAddress hostNeeded, int retries, ReadPreference readPref, DBDecoder decoder) {
        if (readPref == null) {
            try {
                readPref = ReadPreference.primary();
            } finally {
                m.doneWithMessage();
            }
        }
        if (readPref == ReadPreference.primary() && m.hasOption(4)) {
            readPref = ReadPreference.secondaryPreferred();
        }
        boolean secondaryOk = readPref != ReadPreference.primary();
        _checkClosed();
        checkMaster(false, !secondaryOk);
        MyPort mp = this._myPort.get();
        DBPort port = mp.get(false, readPref, hostNeeded);
        Response res = null;
        boolean retry = false;
        try {
            try {
                port.checkAuth(db);
                res = port.call(m, coll, decoder);
            } catch (Throwable th) {
                mp.done(port);
                throw th;
            }
        } catch (IOException ioe) {
            mp.error(port, ioe);
            retry = retries > 0 && !coll._name.equals("$cmd") && !(ioe instanceof SocketTimeoutException) && _error(ioe, secondaryOk);
            if (!retry) {
                throw new MongoException.Network("can't call something : " + port.host() + "/" + db, ioe);
            }
            mp.done(port);
        } catch (RuntimeException re) {
            mp.error(port, re);
            throw re;
        }
        if (res._responseTo != m.getId()) {
            throw new MongoException("ids don't match");
        }
        mp.done(port);
        if (retry) {
            res = call(db, coll, m, hostNeeded, retries - 1, readPref, decoder);
        } else {
            ServerError err = res.getError();
            if (err != null && err.isNotMasterError()) {
                checkMaster(true, true);
                if (retries <= 0) {
                    throw new MongoException("not talking to master and retries used up");
                }
                res = call(db, coll, m, hostNeeded, retries - 1, readPref, decoder);
            }
        }
        return res;
    }

    public ServerAddress getAddress() {
        DBPortPool pool = this._masterPortPool;
        if (pool != null) {
            return pool.getServerAddress();
        }
        return null;
    }

    public List<ServerAddress> getAllAddress() {
        return this._allHosts;
    }

    public List<ServerAddress> getServerAddressList() {
        if (this._connectionStatus != null) {
            return this._connectionStatus.getServerAddressList();
        }
        ServerAddress master = getAddress();
        if (master != null) {
            List<ServerAddress> list = new ArrayList<>();
            list.add(master);
            return list;
        }
        return null;
    }

    public ReplicaSetStatus getReplicaSetStatus() {
        if (this._connectionStatus == null) {
            return null;
        }
        return this._connectionStatus.asReplicaSetStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isMongosConnection() {
        if (this._connectionStatus != null) {
            return this._connectionStatus.asMongosStatus() != null;
        }
        if (this._isMongosDirectConnection == null) {
            initDirectConnection();
        }
        if (this._isMongosDirectConnection != null) {
            return this._isMongosDirectConnection.booleanValue();
        }
        return false;
    }

    public String getConnectPoint() {
        ServerAddress master = getAddress();
        if (master != null) {
            return master.toString();
        }
        return null;
    }

    boolean _error(Throwable t, boolean secondaryOk) {
        if (this._connectionStatus == null) {
            return false;
        }
        if (this._connectionStatus.hasServerUp()) {
            checkMaster(true, secondaryOk ? false : true);
        }
        return this._connectionStatus.hasServerUp();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class MyPort {
        boolean _inRequest;
        DBPort _requestPort;

        MyPort() {
        }

        DBPort get(boolean keep, ReadPreference readPref, ServerAddress hostNeeded) {
            DBPort p;
            if (hostNeeded != null) {
                if (this._requestPort == null || !this._requestPort.serverAddress().equals(hostNeeded)) {
                    return DBTCPConnector.this._portHolder.get(hostNeeded).get();
                }
                return this._requestPort;
            }
            if (this._requestPort != null) {
                if (this._requestPort.getPool() == DBTCPConnector.this._masterPortPool || !keep) {
                    return this._requestPort;
                }
                this._requestPort.getPool().done(this._requestPort);
                this._requestPort = null;
            }
            if (DBTCPConnector.this.getReplicaSetStatus() == null) {
                if (DBTCPConnector.this._masterPortPool != null) {
                    p = DBTCPConnector.this._masterPortPool.get();
                } else {
                    throw new MongoException("Rare case where master=null, probably all servers are down");
                }
            } else {
                ConnectionStatus.Node node = readPref.getNode(DBTCPConnector.this.getReplicaSetStatus()._replicaSetHolder.get());
                if (node != null) {
                    p = DBTCPConnector.this._portHolder.get(node.getServerAddress()).get();
                } else {
                    throw new MongoException("No replica set members available for query with " + readPref.toDBObject().toString());
                }
            }
            if (this._inRequest) {
                this._requestPort = p;
                return p;
            }
            return p;
        }

        void done(DBPort p) {
            if (p != this._requestPort) {
                p.getPool().done(p);
            }
        }

        void error(DBPort p, Exception e) {
            ConnectionStatus.Node newMaster;
            p.close();
            this._requestPort = null;
            boolean recoverable = p.getPool().gotError(e);
            if (!recoverable && DBTCPConnector.this._connectionStatus != null && DBTCPConnector.this._masterPortPool._addr.equals(p.serverAddress()) && (newMaster = DBTCPConnector.this._connectionStatus.ensureMaster()) != null) {
                DBTCPConnector.this.setMaster(newMaster);
            }
        }

        void requestEnsureConnection() {
            if (this._inRequest && this._requestPort == null) {
                this._requestPort = DBTCPConnector.this._masterPortPool.get();
            }
        }

        void requestStart() {
            this._inRequest = true;
        }

        void requestDone() {
            if (this._requestPort != null) {
                this._requestPort.getPool().done(this._requestPort);
            }
            this._requestPort = null;
            this._inRequest = false;
        }
    }

    void checkMaster(boolean force, boolean failIfNoMaster) {
        if (this._connectionStatus != null) {
            if (this._masterPortPool == null || force) {
                ConnectionStatus.Node master = this._connectionStatus.ensureMaster();
                if (master == null) {
                    if (failIfNoMaster) {
                        throw new MongoException("can't find a master");
                    }
                    return;
                } else {
                    setMaster(master);
                    return;
                }
            }
            return;
        }
        if (this._maxBsonObjectSize == 0) {
            initDirectConnection();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setMaster(ConnectionStatus.Node master) {
        if (!this._closed.get()) {
            setMasterAddress(master.getServerAddress());
            this._maxBsonObjectSize = master.getMaxBsonObjectSize();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void initDirectConnection() {
        if (this._masterPortPool != null) {
            DBPort port = this._masterPortPool.get();
            try {
                CommandResult res = port.runCommand(this._mongo.getDB("admin"), new BasicDBObject("isMaster", 1));
                if (res.containsField("maxBsonObjectSize")) {
                    this._maxBsonObjectSize = ((Integer) res.get("maxBsonObjectSize")).intValue();
                } else {
                    this._maxBsonObjectSize = 4194304;
                }
                String msg = res.getString("msg");
                this._isMongosDirectConnection = Boolean.valueOf(msg != null && msg.equals("isdbgrid"));
            } catch (Exception e) {
                _logger.log(Level.WARNING, "Exception executing isMaster command on " + port.serverAddress(), (Throwable) e);
            } finally {
                port.getPool().done(port);
            }
        }
    }

    private synchronized boolean setMasterAddress(ServerAddress addr) {
        boolean z;
        DBPortPool newPool = this._portHolder.get(addr);
        if (newPool == this._masterPortPool) {
            z = false;
        } else {
            if (this._masterPortPool != null) {
                _logger.log(Level.WARNING, "Master switching from " + this._masterPortPool.getServerAddress() + " to " + addr);
            }
            this._masterPortPool = newPool;
            z = true;
        }
        return z;
    }

    public String debugString() {
        StringBuilder buf = new StringBuilder("DBTCPConnector: ");
        if (this._connectionStatus != null) {
            buf.append("set : ").append(this._allHosts);
        } else {
            ServerAddress master = getAddress();
            buf.append(master).append(" ").append(master != null ? master.getSocketAddress() : null);
        }
        return buf.toString();
    }

    public void close() {
        this._closed.set(true);
        if (this._portHolder != null) {
            try {
                this._portHolder.close();
                this._portHolder = null;
            } catch (Throwable th) {
            }
        }
        if (this._connectionStatus != null) {
            try {
                this._connectionStatus.close();
                this._connectionStatus = null;
            } catch (Throwable th2) {
            }
        }
        this._myPort.remove();
    }

    public void updatePortPool(ServerAddress addr) {
        this._portHolder._pools.remove(addr);
    }

    public DBPortPool getDBPortPool(ServerAddress addr) {
        return this._portHolder.get(addr);
    }

    @Override // com.mongodb.DBConnector
    public boolean isOpen() {
        return !this._closed.get();
    }

    public int getMaxBsonObjectSize() {
        return this._maxBsonObjectSize;
    }

    MyPort getMyPort() {
        return this._myPort.get();
    }
}
