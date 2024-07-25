package com.mongodb;

import com.mongodb.ConnectionStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class MongosStatus extends ConnectionStatus {
    private static final Logger logger = Logger.getLogger("com.mongodb.MongosStatus");
    private volatile ConnectionStatus.Node preferred;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MongosStatus(Mongo mongo, List<ServerAddress> mongosAddresses) {
        super(mongosAddresses, mongo);
        this._updater = new MongosUpdater();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.mongodb.ConnectionStatus
    public boolean hasServerUp() {
        return this.preferred != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.mongodb.ConnectionStatus
    public ConnectionStatus.Node ensureMaster() {
        checkClosed();
        return getPreferred();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.mongodb.ConnectionStatus
    public List<ServerAddress> getServerAddressList() {
        return new ArrayList(this._mongosAddresses);
    }

    /* loaded from: classes.dex */
    class MongosUpdater extends ConnectionStatus.BackgroundUpdater {
        MongosUpdater() {
            super("MongosStatus:MongosUpdater");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            List<MongosNode> mongosNodes = getMongosNodes();
            while (!Thread.interrupted()) {
                try {
                    MongosNode bestThisPass = null;
                    try {
                        for (MongosNode cur : mongosNodes) {
                            cur.update();
                            if (cur._ok && (bestThisPass == null || cur._pingTimeMS < bestThisPass._pingTimeMS)) {
                                bestThisPass = cur;
                            }
                        }
                        MongosStatus.this.setPreferred(bestThisPass);
                    } catch (Exception e) {
                        MongosStatus.logger.log(Level.WARNING, "couldn't do update pass", (Throwable) e);
                    }
                    int sleepTime = MongosStatus.this.preferred == null ? ConnectionStatus.updaterIntervalNoMasterMS : ConnectionStatus.updaterIntervalMS;
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e2) {
                    MongosStatus.logger.log(Level.INFO, "Exiting background thread");
                    return;
                }
            }
        }

        private List<MongosNode> getMongosNodes() {
            List<MongosNode> mongosNodes = new ArrayList<>(MongosStatus.this._mongosAddresses.size());
            for (ServerAddress serverAddress : MongosStatus.this._mongosAddresses) {
                mongosNodes.add(new MongosNode(serverAddress, MongosStatus.this._mongo, MongosStatus.this._mongoOptions));
            }
            return mongosNodes;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class MongosNode extends ConnectionStatus.UpdatableNode {
        MongosNode(ServerAddress addr, Mongo mongo, MongoOptions mongoOptions) {
            super(addr, mongo, mongoOptions);
        }

        @Override // com.mongodb.ConnectionStatus.UpdatableNode
        protected Logger getLogger() {
            return MongosStatus.logger;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setPreferred(MongosNode bestThisPass) {
        if (bestThisPass == null) {
            this.preferred = null;
        } else {
            this.preferred = new ConnectionStatus.Node(bestThisPass._pingTimeMS, bestThisPass._addr, bestThisPass._maxBsonObjectSize, bestThisPass._ok);
        }
        notifyAll();
    }

    private synchronized ConnectionStatus.Node getPreferred() {
        if (this.preferred == null) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                throw new MongoInterruptedException("Interrupted while waiting for next update to mongos status", e);
            }
        }
        return this.preferred;
    }
}
