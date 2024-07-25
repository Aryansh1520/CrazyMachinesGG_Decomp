package com.mongodb;

import javax.net.SocketFactory;

/* loaded from: classes.dex */
public class MongoOptions {
    public boolean autoConnectRetry;
    public int connectTimeout;
    public int connectionsPerHost;
    public boolean cursorFinalizerEnabled;
    public DBDecoderFactory dbDecoderFactory;
    public DBEncoderFactory dbEncoderFactory;
    public String description;
    public boolean fsync;
    public boolean j;
    public long maxAutoConnectRetryTime;
    public int maxWaitTime;
    public ReadPreference readPreference;
    public boolean safe;

    @Deprecated
    public boolean slaveOk;
    public SocketFactory socketFactory;
    public boolean socketKeepAlive;
    public int socketTimeout;
    public int threadsAllowedToBlockForConnectionMultiplier;
    public int w;
    public int wtimeout;

    public MongoOptions() {
        reset();
    }

    public void reset() {
        this.connectionsPerHost = Bytes.CONNECTIONS_PER_HOST;
        this.threadsAllowedToBlockForConnectionMultiplier = 5;
        this.maxWaitTime = 120000;
        this.connectTimeout = 10000;
        this.socketTimeout = 0;
        this.socketKeepAlive = false;
        this.autoConnectRetry = false;
        this.maxAutoConnectRetryTime = 0L;
        this.slaveOk = false;
        this.readPreference = null;
        this.safe = false;
        this.w = 0;
        this.wtimeout = 0;
        this.fsync = false;
        this.j = false;
        this.dbDecoderFactory = DefaultDBDecoder.FACTORY;
        this.dbEncoderFactory = DefaultDBEncoder.FACTORY;
        this.socketFactory = SocketFactory.getDefault();
        this.description = null;
        this.cursorFinalizerEnabled = true;
    }

    public MongoOptions copy() {
        MongoOptions m = new MongoOptions();
        m.connectionsPerHost = this.connectionsPerHost;
        m.threadsAllowedToBlockForConnectionMultiplier = this.threadsAllowedToBlockForConnectionMultiplier;
        m.maxWaitTime = this.maxWaitTime;
        m.connectTimeout = this.connectTimeout;
        m.socketTimeout = this.socketTimeout;
        m.socketKeepAlive = this.socketKeepAlive;
        m.autoConnectRetry = this.autoConnectRetry;
        m.maxAutoConnectRetryTime = this.maxAutoConnectRetryTime;
        m.slaveOk = this.slaveOk;
        m.readPreference = this.readPreference;
        m.safe = this.safe;
        m.w = this.w;
        m.wtimeout = this.wtimeout;
        m.fsync = this.fsync;
        m.j = this.j;
        m.dbDecoderFactory = this.dbDecoderFactory;
        m.dbEncoderFactory = this.dbEncoderFactory;
        m.socketFactory = this.socketFactory;
        m.description = this.description;
        m.cursorFinalizerEnabled = this.cursorFinalizerEnabled;
        return m;
    }

    public WriteConcern getWriteConcern() {
        if (this.w != 0 || this.wtimeout != 0 || this.fsync) {
            return new WriteConcern(this.w, this.wtimeout, this.fsync);
        }
        if (this.safe) {
            return WriteConcern.SAFE;
        }
        return WriteConcern.NORMAL;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("description=").append(this.description).append(", ");
        buf.append("connectionsPerHost=").append(this.connectionsPerHost).append(", ");
        buf.append("threadsAllowedToBlockForConnectionMultiplier=").append(this.threadsAllowedToBlockForConnectionMultiplier).append(", ");
        buf.append("maxWaitTime=").append(this.maxWaitTime).append(", ");
        buf.append("connectTimeout=").append(this.connectTimeout).append(", ");
        buf.append("socketTimeout=").append(this.socketTimeout).append(", ");
        buf.append("socketKeepAlive=").append(this.socketKeepAlive).append(", ");
        buf.append("autoConnectRetry=").append(this.autoConnectRetry).append(", ");
        buf.append("maxAutoConnectRetryTime=").append(this.maxAutoConnectRetryTime).append(", ");
        buf.append("slaveOk=").append(this.slaveOk).append(", ");
        if (this.readPreference != null) {
            buf.append("readPreference").append(this.readPreference);
        }
        buf.append("safe=").append(this.safe).append(", ");
        buf.append("w=").append(this.w).append(", ");
        buf.append("wtimeout=").append(this.wtimeout).append(", ");
        buf.append("fsync=").append(this.fsync).append(", ");
        buf.append("j=").append(this.j).append(", ");
        buf.append("cursorFinalizerEnabled=").append(this.cursorFinalizerEnabled);
        return buf.toString();
    }

    public synchronized String getDescription() {
        return this.description;
    }

    public synchronized void setDescription(String desc) {
        this.description = desc;
    }

    public synchronized int getConnectionsPerHost() {
        return this.connectionsPerHost;
    }

    public synchronized void setConnectionsPerHost(int connections) {
        this.connectionsPerHost = connections;
    }

    public synchronized int getThreadsAllowedToBlockForConnectionMultiplier() {
        return this.threadsAllowedToBlockForConnectionMultiplier;
    }

    public synchronized void setThreadsAllowedToBlockForConnectionMultiplier(int threads) {
        this.threadsAllowedToBlockForConnectionMultiplier = threads;
    }

    public synchronized int getMaxWaitTime() {
        return this.maxWaitTime;
    }

    public synchronized void setMaxWaitTime(int timeMS) {
        this.maxWaitTime = timeMS;
    }

    public synchronized int getConnectTimeout() {
        return this.connectTimeout;
    }

    public synchronized void setConnectTimeout(int timeoutMS) {
        this.connectTimeout = timeoutMS;
    }

    public synchronized int getSocketTimeout() {
        return this.socketTimeout;
    }

    public synchronized void setSocketTimeout(int timeoutMS) {
        this.socketTimeout = timeoutMS;
    }

    public synchronized boolean isSocketKeepAlive() {
        return this.socketKeepAlive;
    }

    public synchronized void setSocketKeepAlive(boolean keepAlive) {
        this.socketKeepAlive = keepAlive;
    }

    public synchronized boolean isAutoConnectRetry() {
        return this.autoConnectRetry;
    }

    public synchronized void setAutoConnectRetry(boolean retry) {
        this.autoConnectRetry = retry;
    }

    public synchronized long getMaxAutoConnectRetryTime() {
        return this.maxAutoConnectRetryTime;
    }

    public synchronized void setMaxAutoConnectRetryTime(long retryTimeMS) {
        this.maxAutoConnectRetryTime = retryTimeMS;
    }

    public synchronized DBDecoderFactory getDbDecoderFactory() {
        return this.dbDecoderFactory;
    }

    public synchronized void setDbDecoderFactory(DBDecoderFactory factory) {
        this.dbDecoderFactory = factory;
    }

    public synchronized DBEncoderFactory getDbEncoderFactory() {
        return this.dbEncoderFactory;
    }

    public synchronized void setDbEncoderFactory(DBEncoderFactory factory) {
        this.dbEncoderFactory = factory;
    }

    public synchronized boolean isSafe() {
        return this.safe;
    }

    public synchronized void setSafe(boolean isSafe) {
        this.safe = isSafe;
    }

    public synchronized int getW() {
        return this.w;
    }

    public synchronized void setW(int val) {
        this.w = val;
    }

    public synchronized int getWtimeout() {
        return this.wtimeout;
    }

    public synchronized void setWtimeout(int timeoutMS) {
        this.wtimeout = timeoutMS;
    }

    public synchronized boolean isFsync() {
        return this.fsync;
    }

    public synchronized void setFsync(boolean sync) {
        this.fsync = sync;
    }

    public synchronized boolean isJ() {
        return this.j;
    }

    public synchronized void setJ(boolean safe) {
        this.j = safe;
    }

    public synchronized SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public synchronized void setSocketFactory(SocketFactory factory) {
        this.socketFactory = factory;
    }

    public ReadPreference getReadPreference() {
        return this.readPreference;
    }

    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    public boolean isCursorFinalizerEnabled() {
        return this.cursorFinalizerEnabled;
    }

    public void setCursorFinalizerEnabled(boolean cursorFinalizerEnabled) {
        this.cursorFinalizerEnabled = cursorFinalizerEnabled;
    }
}
