package com.mongodb;

import com.mappn.sdk.pay.chargement.ChargeActivity;
import com.mappn.sdk.pay.util.Constants;
import com.mongodb.Bytes;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bson.io.PoolOutputBuffer;
import org.bson.util.SimplePool;

/* loaded from: classes.dex */
public class Mongo {
    private static final String FULL_VERSION = "2.9.1";

    @Deprecated
    public static final int MAJOR_VERSION = 2;

    @Deprecated
    public static final int MINOR_VERSION = 9;
    static int cleanerIntervalMS = Integer.parseInt(System.getProperty("com.mongodb.cleanerIntervalMS", "1000"));
    final ServerAddress _addr;
    final List<ServerAddress> _addrs;
    SimplePool<PoolOutputBuffer> _bufferPool;
    final CursorCleanerThread _cleaner;
    private WriteConcern _concern;
    final DBTCPConnector _connector;
    final ConcurrentMap<String, DB> _dbs;
    final Bytes.OptionHolder _netOptions;
    final MongoOptions _options;
    private ReadPreference _readPref;

    public static int getMajorVersion() {
        return 2;
    }

    public static int getMinorVersion() {
        return 9;
    }

    public static DB connect(DBAddress addr) {
        return new Mongo(addr).getDB(addr.getDBName());
    }

    public Mongo() throws UnknownHostException {
        this(new ServerAddress());
    }

    public Mongo(String host) throws UnknownHostException {
        this(new ServerAddress(host));
    }

    public Mongo(String host, MongoOptions options) throws UnknownHostException {
        this(new ServerAddress(host), options);
    }

    public Mongo(String host, int port) throws UnknownHostException {
        this(new ServerAddress(host, port));
    }

    public Mongo(ServerAddress addr) {
        this(addr, new MongoOptions());
    }

    public Mongo(ServerAddress addr, MongoOptions options) {
        this._dbs = new ConcurrentHashMap();
        this._concern = WriteConcern.NORMAL;
        this._readPref = ReadPreference.primary();
        this._netOptions = new Bytes.OptionHolder(null);
        this._bufferPool = new SimplePool<PoolOutputBuffer>(Constants.PAYMENT_MAX) { // from class: com.mongodb.Mongo.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.bson.util.SimplePool
            public PoolOutputBuffer createNew() {
                return new PoolOutputBuffer();
            }
        };
        this._addr = addr;
        this._addrs = null;
        this._options = options;
        _applyMongoOptions();
        this._connector = new DBTCPConnector(this, this._addr);
        this._connector.start();
        if (options.cursorFinalizerEnabled) {
            this._cleaner = new CursorCleanerThread();
            this._cleaner.start();
        } else {
            this._cleaner = null;
        }
    }

    @Deprecated
    public Mongo(ServerAddress left, ServerAddress right) {
        this(left, right, new MongoOptions());
    }

    @Deprecated
    public Mongo(ServerAddress left, ServerAddress right, MongoOptions options) {
        this._dbs = new ConcurrentHashMap();
        this._concern = WriteConcern.NORMAL;
        this._readPref = ReadPreference.primary();
        this._netOptions = new Bytes.OptionHolder(null);
        this._bufferPool = new SimplePool<PoolOutputBuffer>(Constants.PAYMENT_MAX) { // from class: com.mongodb.Mongo.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.bson.util.SimplePool
            public PoolOutputBuffer createNew() {
                return new PoolOutputBuffer();
            }
        };
        this._addr = null;
        this._addrs = Arrays.asList(left, right);
        this._options = options;
        _applyMongoOptions();
        this._connector = new DBTCPConnector(this, this._addrs);
        this._connector.start();
        if (options.cursorFinalizerEnabled) {
            this._cleaner = new CursorCleanerThread();
            this._cleaner.start();
        } else {
            this._cleaner = null;
        }
    }

    public Mongo(List<ServerAddress> seeds) {
        this(seeds, new MongoOptions());
    }

    public Mongo(List<ServerAddress> seeds, MongoOptions options) {
        this._dbs = new ConcurrentHashMap();
        this._concern = WriteConcern.NORMAL;
        this._readPref = ReadPreference.primary();
        this._netOptions = new Bytes.OptionHolder(null);
        this._bufferPool = new SimplePool<PoolOutputBuffer>(Constants.PAYMENT_MAX) { // from class: com.mongodb.Mongo.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.bson.util.SimplePool
            public PoolOutputBuffer createNew() {
                return new PoolOutputBuffer();
            }
        };
        this._addr = null;
        this._addrs = seeds;
        this._options = options;
        _applyMongoOptions();
        this._connector = new DBTCPConnector(this, this._addrs);
        this._connector.start();
        if (options.cursorFinalizerEnabled) {
            this._cleaner = new CursorCleanerThread();
            this._cleaner.start();
        } else {
            this._cleaner = null;
        }
    }

    public Mongo(MongoURI uri) throws UnknownHostException {
        this._dbs = new ConcurrentHashMap();
        this._concern = WriteConcern.NORMAL;
        this._readPref = ReadPreference.primary();
        this._netOptions = new Bytes.OptionHolder(null);
        this._bufferPool = new SimplePool<PoolOutputBuffer>(Constants.PAYMENT_MAX) { // from class: com.mongodb.Mongo.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // org.bson.util.SimplePool
            public PoolOutputBuffer createNew() {
                return new PoolOutputBuffer();
            }
        };
        this._options = uri.getOptions();
        _applyMongoOptions();
        if (uri.getHosts().size() == 1) {
            this._addr = new ServerAddress(uri.getHosts().get(0));
            this._addrs = null;
            this._connector = new DBTCPConnector(this, this._addr);
        } else {
            List<ServerAddress> replicaSetSeeds = new ArrayList<>(uri.getHosts().size());
            for (String host : uri.getHosts()) {
                replicaSetSeeds.add(new ServerAddress(host));
            }
            this._addr = null;
            this._addrs = replicaSetSeeds;
            this._connector = new DBTCPConnector(this, replicaSetSeeds);
        }
        this._connector.start();
        if (this._options.cursorFinalizerEnabled) {
            this._cleaner = new CursorCleanerThread();
            this._cleaner.start();
        } else {
            this._cleaner = null;
        }
    }

    public DB getDB(String dbname) {
        DB db = this._dbs.get(dbname);
        if (db != null) {
            return db;
        }
        DB db2 = new DBApiLayer(this, dbname, this._connector);
        DB temp = this._dbs.putIfAbsent(dbname, db2);
        return temp == null ? db2 : temp;
    }

    public Collection<DB> getUsedDatabases() {
        return this._dbs.values();
    }

    public List<String> getDatabaseNames() {
        BasicDBObject cmd = new BasicDBObject();
        cmd.put("listDatabases", (Object) 1);
        CommandResult res = getDB("admin").command(cmd, getOptions());
        res.throwOnError();
        List l = (List) res.get("databases");
        List<String> list = new ArrayList<>();
        for (Object o : l) {
            list.add(((BasicDBObject) o).getString("name"));
        }
        return list;
    }

    public void dropDatabase(String dbName) {
        getDB(dbName).dropDatabase();
    }

    public String getVersion() {
        return FULL_VERSION;
    }

    public String debugString() {
        return this._connector.debugString();
    }

    public String getConnectPoint() {
        return this._connector.getConnectPoint();
    }

    public DBTCPConnector getConnector() {
        return this._connector;
    }

    public ReplicaSetStatus getReplicaSetStatus() {
        return this._connector.getReplicaSetStatus();
    }

    public ServerAddress getAddress() {
        return this._connector.getAddress();
    }

    public List<ServerAddress> getAllAddress() {
        List<ServerAddress> result = this._connector.getAllAddress();
        return result == null ? Arrays.asList(getAddress()) : result;
    }

    public List<ServerAddress> getServerAddressList() {
        return this._connector.getServerAddressList();
    }

    public void close() {
        try {
            this._connector.close();
        } catch (Throwable th) {
        }
        if (this._cleaner != null) {
            this._cleaner.interrupt();
            try {
                this._cleaner.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public void setWriteConcern(WriteConcern concern) {
        this._concern = concern;
    }

    public WriteConcern getWriteConcern() {
        return this._concern;
    }

    public void setReadPreference(ReadPreference preference) {
        this._readPref = preference;
    }

    public ReadPreference getReadPreference() {
        return this._readPref;
    }

    @Deprecated
    public void slaveOk() {
        addOption(4);
    }

    public void addOption(int option) {
        this._netOptions.add(option);
    }

    public void setOptions(int options) {
        this._netOptions.set(options);
    }

    public void resetOptions() {
        this._netOptions.reset();
    }

    public int getOptions() {
        return this._netOptions.get();
    }

    void _applyMongoOptions() {
        if (this._options.slaveOk) {
            slaveOk();
        }
        if (this._options.getReadPreference() != null) {
            setReadPreference(this._options.getReadPreference());
        }
        setWriteConcern(this._options.getWriteConcern());
    }

    public MongoOptions getMongoOptions() {
        return this._options;
    }

    public int getMaxBsonObjectSize() {
        if (this._connector.getMaxBsonObjectSize() == 0) {
            this._connector.initDirectConnection();
        }
        int maxsize = this._connector.getMaxBsonObjectSize();
        if (maxsize > 0) {
            return maxsize;
        }
        return 4194304;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isMongosConnection() {
        return this._connector.isMongosConnection();
    }

    public CommandResult fsync(boolean async) {
        DBObject cmd = new BasicDBObject("fsync", 1);
        if (async) {
            cmd.put("async", 1);
        }
        return getDB("admin").command(cmd);
    }

    public CommandResult fsyncAndLock() {
        DBObject cmd = new BasicDBObject("fsync", 1);
        cmd.put(Constants.RES_LOCK, 1);
        return getDB("admin").command(cmd);
    }

    public DBObject unlock() {
        DB db = getDB("admin");
        DBCollection col = db.getCollection("$cmd.sys.unlock");
        return col.findOne();
    }

    public boolean isLocked() {
        DB db = getDB("admin");
        DBCollection col = db.getCollection("$cmd.sys.inprog");
        BasicDBObject res = (BasicDBObject) col.findOne();
        return res.containsField("fsyncLock") && res.getInt("fsyncLock") == 1;
    }

    /* loaded from: classes.dex */
    public static class Holder {
        private static Holder _default = new Holder();
        private final ConcurrentMap<String, Mongo> _mongos = new ConcurrentHashMap();

        public Mongo connect(MongoURI uri) throws UnknownHostException {
            String key = _toKey(uri);
            Mongo m = this._mongos.get(key);
            if (m != null) {
                return m;
            }
            Mongo m2 = new Mongo(uri);
            Mongo temp = this._mongos.putIfAbsent(key, m2);
            if (temp == null) {
                return m2;
            }
            m2.close();
            return temp;
        }

        String _toKey(MongoURI uri) {
            StringBuilder buf = new StringBuilder();
            for (String h : uri.getHosts()) {
                buf.append(h).append(Constants.TERM);
            }
            buf.append(uri.getOptions());
            buf.append(uri.getUsername());
            return buf.toString();
        }

        public static Holder singleton() {
            return _default;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class CursorCleanerThread extends Thread {
        CursorCleanerThread() {
            setDaemon(true);
            setName("MongoCleaner" + hashCode());
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (Mongo.this._connector.isOpen()) {
                try {
                    try {
                        Thread.sleep(Mongo.cleanerIntervalMS);
                    } catch (Throwable th) {
                    }
                } catch (InterruptedException e) {
                }
                for (DB db : Mongo.this._dbs.values()) {
                    db.cleanCursors(true);
                }
            }
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder("Mongo: ");
        List<ServerAddress> list = getServerAddressList();
        if (list == null || list.size() == 0) {
            str.append(ChargeActivity.TYPE_CHARGE_TYPE_LIST);
        } else {
            for (ServerAddress addr : list) {
                str.append(addr.toString()).append(',');
            }
            str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }
}
