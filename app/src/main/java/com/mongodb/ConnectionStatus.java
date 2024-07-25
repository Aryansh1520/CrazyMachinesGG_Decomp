package com.mongodb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class ConnectionStatus {
    protected static final float latencySmoothFactor;
    protected volatile boolean _closed;
    protected final Mongo _mongo;
    protected final MongoOptions _mongoOptions = mongoOptionsDefaults.copy();
    protected final List<ServerAddress> _mongosAddresses;
    protected BackgroundUpdater _updater;
    protected static final MongoOptions mongoOptionsDefaults = new MongoOptions();
    protected static final DBObject isMasterCmd = new BasicDBObject("ismaster", 1);
    protected static int updaterIntervalMS = Integer.parseInt(System.getProperty("com.mongodb.updaterIntervalMS", "5000"));
    protected static int updaterIntervalNoMasterMS = Integer.parseInt(System.getProperty("com.mongodb.updaterIntervalNoMasterMS", "10"));

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Node ensureMaster();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract List<ServerAddress> getServerAddressList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean hasServerUp();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConnectionStatus(List<ServerAddress> mongosAddresses, Mongo mongo) {
        this._mongoOptions.socketFactory = mongo._options.socketFactory;
        this._mongosAddresses = new ArrayList(mongosAddresses);
        this._mongo = mongo;
    }

    static {
        mongoOptionsDefaults.connectTimeout = Integer.parseInt(System.getProperty("com.mongodb.updaterConnectTimeoutMS", "20000"));
        mongoOptionsDefaults.socketTimeout = Integer.parseInt(System.getProperty("com.mongodb.updaterSocketTimeoutMS", "20000"));
        latencySmoothFactor = Float.parseFloat(System.getProperty("com.mongodb.latencySmoothFactor", "4"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start() {
        if (this._updater != null) {
            this._updater.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        this._closed = true;
        if (this._updater != null) {
            this._updater.interrupt();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void checkClosed() {
        if (this._closed) {
            throw new IllegalStateException("ReplicaSetStatus closed");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Node {
        protected final ServerAddress _addr;
        protected final int _maxBsonObjectSize;
        protected final boolean _ok;
        protected final float _pingTime;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Node(float pingTime, ServerAddress addr, int maxBsonObjectSize, boolean ok) {
            this._pingTime = pingTime;
            this._addr = addr;
            this._maxBsonObjectSize = maxBsonObjectSize;
            this._ok = ok;
        }

        public boolean isOk() {
            return this._ok;
        }

        public int getMaxBsonObjectSize() {
            return this._maxBsonObjectSize;
        }

        public ServerAddress getServerAddress() {
            return this._addr;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return this._maxBsonObjectSize == node._maxBsonObjectSize && this._ok == node._ok && Float.compare(node._pingTime, this._pingTime) == 0 && this._addr.equals(node._addr);
        }

        public int hashCode() {
            int result = this._addr.hashCode();
            return (((((result * 31) + (this._pingTime != 0.0f ? Float.floatToIntBits(this._pingTime) : 0)) * 31) + (this._ok ? 1 : 0)) * 31) + this._maxBsonObjectSize;
        }

        public String toJSON() {
            StringBuilder buf = new StringBuilder();
            buf.append("{");
            buf.append("address:'").append(this._addr).append("', ");
            buf.append("ok:").append(this._ok).append(", ");
            buf.append("ping:").append(this._pingTime).append(", ");
            buf.append("maxBsonObjectSize:").append(this._maxBsonObjectSize).append(", ");
            buf.append("}");
            return buf.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class BackgroundUpdater extends Thread {
        public BackgroundUpdater(String name) {
            super(name);
            setDaemon(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class UpdatableNode {
        final ServerAddress _addr;
        int _maxBsonObjectSize;
        final Mongo _mongo;
        final MongoOptions _mongoOptions;
        DBPort _port;
        boolean successfullyContacted = false;
        boolean _ok = false;
        float _pingTimeMS = 0.0f;

        protected abstract Logger getLogger();

        /* JADX INFO: Access modifiers changed from: package-private */
        public UpdatableNode(ServerAddress addr, Mongo mongo, MongoOptions mongoOptions) {
            this._addr = addr;
            this._mongo = mongo;
            this._mongoOptions = mongoOptions;
            this._port = new DBPort(addr, null, mongoOptions);
        }

        public CommandResult update() {
            CommandResult res = null;
            try {
                long start = System.nanoTime();
                res = this._port.runCommand(this._mongo.getDB("admin"), ConnectionStatus.isMasterCmd);
                long end = System.nanoTime();
                float newPingMS = ((float) (end - start)) / 1000000.0f;
                if (!this.successfullyContacted) {
                    this._pingTimeMS = newPingMS;
                } else {
                    this._pingTimeMS += (newPingMS - this._pingTimeMS) / ConnectionStatus.latencySmoothFactor;
                }
                getLogger().log(Level.FINE, "Latency to " + this._addr + " actual=" + newPingMS + " smoothed=" + this._pingTimeMS);
                this.successfullyContacted = true;
            } catch (Exception e) {
                if (!this._ok && Math.random() <= 0.1d) {
                    return null;
                }
                StringBuilder logError = new StringBuilder("Server seen down: ").append(this._addr);
                if (e instanceof IOException) {
                    logError.append(" - ").append(IOException.class.getName());
                    if (e.getMessage() != null) {
                        logError.append(" - message: ").append(e.getMessage());
                    }
                    getLogger().log(Level.WARNING, logError.toString());
                } else {
                    getLogger().log(Level.WARNING, logError.toString(), (Throwable) e);
                }
                this._ok = false;
            }
            if (res == null) {
                throw new MongoInternalException("Invalid null value returned from isMaster");
            }
            if (!this._ok) {
                getLogger().log(Level.INFO, "Server seen up: " + this._addr);
            }
            this._ok = true;
            if (res.containsField("maxBsonObjectSize")) {
                this._maxBsonObjectSize = ((Integer) res.get("maxBsonObjectSize")).intValue();
            } else {
                this._maxBsonObjectSize = 4194304;
            }
            return res;
        }
    }
}
