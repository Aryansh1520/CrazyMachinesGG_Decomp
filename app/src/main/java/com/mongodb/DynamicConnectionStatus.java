package com.mongodb;

import com.mongodb.ConnectionStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class DynamicConnectionStatus extends ConnectionStatus {
    private static final Logger logger = Logger.getLogger("com.mongodb.DynamicConnectionStatus");
    private volatile ConnectionStatus connectionStatus;
    private ExecutorService executorService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DynamicConnectionStatus(Mongo mongo, List<ServerAddress> mongosAddresses) {
        super(mongosAddresses, mongo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.mongodb.ConnectionStatus
    public void start() {
        super.start();
        this.executorService = Executors.newFixedThreadPool(this._mongosAddresses.size());
        initExecutorService();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.mongodb.ConnectionStatus
    public void close() {
        if (this.connectionStatus != null) {
            this.connectionStatus.close();
        }
        if (this.executorService != null) {
            this.executorService.shutdownNow();
        }
        super.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReplicaSetStatus asReplicaSetStatus() {
        ConnectionStatus connectionStatus = getConnectionStatus();
        if (connectionStatus instanceof ReplicaSetStatus) {
            return (ReplicaSetStatus) connectionStatus;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MongosStatus asMongosStatus() {
        ConnectionStatus connectionStatus = getConnectionStatus();
        if (connectionStatus instanceof MongosStatus) {
            return (MongosStatus) connectionStatus;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.mongodb.ConnectionStatus
    public List<ServerAddress> getServerAddressList() {
        return this.connectionStatus != null ? this.connectionStatus.getServerAddressList() : new ArrayList(this._mongosAddresses);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.mongodb.ConnectionStatus
    public boolean hasServerUp() {
        ConnectionStatus connectionStatus = getConnectionStatus();
        if (connectionStatus != null) {
            return connectionStatus.hasServerUp();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.mongodb.ConnectionStatus
    public ConnectionStatus.Node ensureMaster() {
        ConnectionStatus connectionStatus = getConnectionStatus();
        if (connectionStatus != null) {
            return connectionStatus.ensureMaster();
        }
        return null;
    }

    void initExecutorService() {
        try {
            for (final ServerAddress cur : this._mongosAddresses) {
                this.executorService.submit(new Runnable() { // from class: com.mongodb.DynamicConnectionStatus.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DynamicNode node = new DynamicNode(cur, DynamicConnectionStatus.this._mongo, DynamicConnectionStatus.this._mongoOptions);
                        while (!Thread.interrupted()) {
                            try {
                                try {
                                    node.update();
                                } catch (Exception e) {
                                    DynamicConnectionStatus.logger.log(Level.WARNING, "couldn't reach " + node._addr, (Throwable) e);
                                }
                                if (node._ok) {
                                    DynamicConnectionStatus.this.notifyOfOkNode(node);
                                    return;
                                } else {
                                    continue;
                                    int sleepTime = ConnectionStatus.updaterIntervalNoMasterMS;
                                    Thread.sleep(sleepTime);
                                }
                            } catch (InterruptedException e2) {
                                return;
                            }
                        }
                    }
                });
            }
        } catch (RejectedExecutionException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyOfOkNode(DynamicNode node) {
        synchronized (this) {
            if (this.connectionStatus != null) {
                return;
            }
            if (node.isMongos) {
                this.connectionStatus = new MongosStatus(this._mongo, this._mongosAddresses);
            } else {
                this.connectionStatus = new ReplicaSetStatus(this._mongo, this._mongosAddresses);
            }
            notifyAll();
            this.connectionStatus.start();
            this.executorService.shutdownNow();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class DynamicNode extends ConnectionStatus.UpdatableNode {
        private boolean isMongos;

        DynamicNode(ServerAddress addr, Mongo mongo, MongoOptions mongoOptions) {
            super(addr, mongo, mongoOptions);
        }

        @Override // com.mongodb.ConnectionStatus.UpdatableNode
        protected Logger getLogger() {
            return DynamicConnectionStatus.logger;
        }

        @Override // com.mongodb.ConnectionStatus.UpdatableNode
        public CommandResult update() {
            String msg;
            CommandResult res = super.update();
            if (res != null && (msg = res.getString("msg")) != null && msg.equals("isdbgrid")) {
                this.isMongos = true;
            }
            return res;
        }
    }

    private synchronized ConnectionStatus getConnectionStatus() {
        if (this.connectionStatus == null) {
            try {
                wait(this._mongoOptions.connectTimeout);
            } catch (InterruptedException e) {
                throw new MongoInterruptedException("Interrupted while waiting for next update to dynamic status", e);
            }
        }
        return this.connectionStatus;
    }
}
