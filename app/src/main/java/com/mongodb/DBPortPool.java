package com.mongodb;

import com.mongodb.util.ConnectionPoolStatisticsBean;
import com.mongodb.util.SimplePool;
import com.mongodb.util.management.JMException;
import com.mongodb.util.management.MBeanServerFactory;
import java.io.InterruptedIOException;
import java.nio.channels.ClosedByInterruptException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/* loaded from: classes.dex */
public class DBPortPool extends SimplePool<DBPort> implements MongoConnectionPoolMXBean {
    final ServerAddress _addr;
    boolean _everWorked;
    final MongoOptions _options;
    private final Semaphore _waitingSem;

    @Override // com.mongodb.MongoConnectionPoolMXBean
    public String getHost() {
        return this._addr.getHost();
    }

    @Override // com.mongodb.MongoConnectionPoolMXBean
    public int getPort() {
        return this._addr.getPort();
    }

    @Override // com.mongodb.MongoConnectionPoolMXBean
    public synchronized ConnectionPoolStatisticsBean getStatistics() {
        return new ConnectionPoolStatisticsBean(getTotal(), getInUse(), getInUseConnections());
    }

    private InUseConnectionBean[] getInUseConnections() {
        List<InUseConnectionBean> inUseConnectionInfoList = new ArrayList<>();
        long currentNanoTime = System.nanoTime();
        for (T port : this._out) {
            inUseConnectionInfoList.add(new InUseConnectionBean(port, currentNanoTime));
        }
        return (InUseConnectionBean[]) inUseConnectionInfoList.toArray(new InUseConnectionBean[inUseConnectionInfoList.size()]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Holder {
        static AtomicInteger nextSerial = new AtomicInteger(0);
        final MongoOptions _options;
        final Map<ServerAddress, DBPortPool> _pools = Collections.synchronizedMap(new HashMap());
        final int _serial = nextSerial.incrementAndGet();

        /* JADX INFO: Access modifiers changed from: package-private */
        public Holder(MongoOptions options) {
            this._options = options;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public DBPortPool get(ServerAddress addr) {
            DBPortPool p = this._pools.get(addr);
            if (p != null) {
                return p;
            }
            synchronized (this._pools) {
                try {
                    DBPortPool p2 = this._pools.get(addr);
                    if (p2 != null) {
                        return p2;
                    }
                    DBPortPool p3 = new DBPortPool(addr, this._options);
                    try {
                        this._pools.put(addr, p3);
                        try {
                            try {
                                String on = createObjectName(addr);
                                if (MBeanServerFactory.getMBeanServer().isRegistered(on)) {
                                    MBeanServerFactory.getMBeanServer().unregisterMBean(on);
                                    Bytes.LOGGER.log(Level.INFO, "multiple Mongo instances for same host, jmx numbers might be off");
                                }
                                MBeanServerFactory.getMBeanServer().registerMBean(p3, on);
                            } catch (AccessControlException e) {
                                Bytes.LOGGER.log(Level.WARNING, "jmx registration error: " + e + " continuing...");
                            }
                        } catch (JMException e2) {
                            Bytes.LOGGER.log(Level.WARNING, "jmx registration error: " + e2 + " continuing...");
                        }
                        return p3;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void close() {
            synchronized (this._pools) {
                for (DBPortPool p : this._pools.values()) {
                    p.close();
                    try {
                        String on = createObjectName(p._addr);
                        if (MBeanServerFactory.getMBeanServer().isRegistered(on)) {
                            MBeanServerFactory.getMBeanServer().unregisterMBean(on);
                        }
                    } catch (JMException e) {
                        Bytes.LOGGER.log(Level.WARNING, "jmx de-registration error, continuing", (Throwable) e);
                    }
                }
            }
        }

        private String createObjectName(ServerAddress addr) {
            String name = "com.mongodb:type=ConnectionPool,host=" + addr.toString().replace(":", ",port=") + ",instance=" + this._serial;
            if (this._options.description != null) {
                return name + ",description=" + this._options.description;
            }
            return name;
        }
    }

    /* loaded from: classes.dex */
    public static class NoMoreConnection extends MongoInternalException {
        private static final long serialVersionUID = -4415279469780082174L;

        NoMoreConnection(String msg) {
            super(msg);
        }
    }

    /* loaded from: classes.dex */
    public static class SemaphoresOut extends NoMoreConnection {
        private static final long serialVersionUID = -4415279469780082174L;

        SemaphoresOut() {
            super("Out of semaphores to get db connection");
        }
    }

    /* loaded from: classes.dex */
    public static class ConnectionWaitTimeOut extends NoMoreConnection {
        private static final long serialVersionUID = -4415279469780082174L;

        ConnectionWaitTimeOut(int timeout) {
            super("Connection wait timeout after " + timeout + " ms");
        }
    }

    DBPortPool(ServerAddress addr, MongoOptions options) {
        super("DBPortPool-" + addr.toString() + ", options = " + options.toString(), options.connectionsPerHost);
        this._everWorked = false;
        this._options = options;
        this._addr = addr;
        this._waitingSem = new Semaphore(this._options.connectionsPerHost * this._options.threadsAllowedToBlockForConnectionMultiplier);
    }

    protected long memSize(DBPort p) {
        return 0L;
    }

    @Override // com.mongodb.util.SimplePool
    protected int pick(int recommended, boolean couldCreate) {
        int id = System.identityHashCode(Thread.currentThread());
        for (int i = this._avail.size() - 1; i >= 0; i--) {
            if (((DBPort) this._avail.get(i))._lastThread == id) {
                return i;
            }
        }
        if (couldCreate) {
            recommended = -1;
        }
        int i2 = recommended;
        return i2;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.mongodb.util.SimplePool
    public DBPort get() {
        try {
            if (!this._waitingSem.tryAcquire()) {
                throw new SemaphoresOut();
            }
            try {
                DBPort port = get(this._options.maxWaitTime);
                if (port == null) {
                    throw new ConnectionWaitTimeOut(this._options.maxWaitTime);
                }
                port._lastThread = System.identityHashCode(Thread.currentThread());
                return port;
            } catch (InterruptedException e) {
                throw new MongoInterruptedException(e);
            }
        } finally {
            this._waitingSem.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean gotError(Exception e) {
        if ((e instanceof ClosedByInterruptException) || (e instanceof InterruptedIOException)) {
            return true;
        }
        Bytes.LOGGER.log(Level.WARNING, "emptying DBPortPool to " + getServerAddress() + " b/c of error", (Throwable) e);
        List<DBPort> all = new ArrayList<>();
        while (true) {
            try {
                DBPort temp = get(0L);
                if (temp == null) {
                    break;
                }
                all.add(temp);
            } catch (InterruptedException interruptedException) {
                throw new MongoInterruptedException(interruptedException);
            }
        }
        for (DBPort p : all) {
            p.close();
            done(p);
        }
        return false;
    }

    @Override // com.mongodb.util.SimplePool
    public void cleanup(DBPort p) {
        p.close();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.mongodb.util.SimplePool
    public DBPort createNew() {
        return new DBPort(this._addr, this, this._options);
    }

    public ServerAddress getServerAddress() {
        return this._addr;
    }
}
