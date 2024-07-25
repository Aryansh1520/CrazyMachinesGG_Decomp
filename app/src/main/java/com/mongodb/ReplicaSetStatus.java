package com.mongodb;

import com.mappn.sdk.pay.util.Constants;
import com.mokredit.payment.StringUtils;
import com.mongodb.ConnectionStatus;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.util.annotations.Immutable;
import org.bson.util.annotations.ThreadSafe;

@ThreadSafe
/* loaded from: classes.dex */
public class ReplicaSetStatus extends ConnectionStatus {
    private final AtomicReference<String> _lastPrimarySignal;
    private final AtomicReference<Logger> _logger;
    final ReplicaSetHolder _replicaSetHolder;
    static final Logger _rootLogger = Logger.getLogger("com.mongodb.ReplicaSetStatus");
    static final int slaveAcceptableLatencyMS = Integer.parseInt(System.getProperty("com.mongodb.slaveAcceptableLatencyMS", "15"));
    static final int inetAddrCacheMS = Integer.parseInt(System.getProperty("com.mongodb.inetAddrCacheMS", "300000"));

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReplicaSetStatus(Mongo mongo, List<ServerAddress> initial) {
        super(initial, mongo);
        this._replicaSetHolder = new ReplicaSetHolder();
        this._logger = new AtomicReference<>(_rootLogger);
        this._lastPrimarySignal = new AtomicReference<>();
        this._updater = new Updater(initial);
    }

    public String getName() {
        return this._replicaSetHolder.get().getSetName();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{replSetName: ").append(this._replicaSetHolder.get().getSetName());
        sb.append(", members: ").append(this._replicaSetHolder);
        sb.append(", updaterIntervalMS: ").append(updaterIntervalMS);
        sb.append(", updaterIntervalNoMasterMS: ").append(updaterIntervalNoMasterMS);
        sb.append(", slaveAcceptableLatencyMS: ").append(slaveAcceptableLatencyMS);
        sb.append(", inetAddrCacheMS: ").append(inetAddrCacheMS);
        sb.append(", latencySmoothFactor: ").append(latencySmoothFactor);
        sb.append("}");
        return sb.toString();
    }

    public ServerAddress getMaster() {
        ReplicaSetNode n = getMasterNode();
        if (n == null) {
            return null;
        }
        return n.getServerAddress();
    }

    ReplicaSetNode getMasterNode() {
        checkClosed();
        return this._replicaSetHolder.get().getMaster();
    }

    public boolean isMaster(ServerAddress srv) {
        if (srv == null) {
            return false;
        }
        return srv.equals(getMaster());
    }

    ServerAddress getASecondary() {
        ReplicaSetNode node = this._replicaSetHolder.get().getASecondary();
        if (node == null) {
            return null;
        }
        return node._addr;
    }

    @Override // com.mongodb.ConnectionStatus
    boolean hasServerUp() {
        for (ReplicaSetNode node : this._replicaSetHolder.get().getAll()) {
            if (node.isOk()) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @ThreadSafe
    /* loaded from: classes.dex */
    public static class ReplicaSetHolder {
        private volatile ReplicaSet members;

        ReplicaSetHolder() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized ReplicaSet get() {
            while (this.members == null) {
                try {
                    wait(ReplicaSetStatus.mongoOptionsDefaults.socketTimeout);
                } catch (InterruptedException e) {
                    throw new MongoInterruptedException("Interrupted while waiting for next update to replica set status", e);
                }
            }
            return this.members;
        }

        synchronized void set(ReplicaSet members) {
            if (members == null) {
                throw new IllegalArgumentException("members can not be null");
            }
            this.members = members;
            notifyAll();
        }

        synchronized void waitForNextUpdate() {
            try {
                wait(ReplicaSetStatus.mongoOptionsDefaults.socketTimeout);
            } catch (InterruptedException e) {
                throw new MongoInterruptedException("Interrupted while waiting for next update to replica set status", e);
            }
        }

        public synchronized void close() {
            this.members = null;
            notifyAll();
        }

        public String toString() {
            ReplicaSet cur = this.members;
            return cur != null ? cur.toString() : "none";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Immutable
    /* loaded from: classes.dex */
    public static class ReplicaSet {
        private int acceptableLatencyMS;
        final List<ReplicaSetNode> all;
        final List<ReplicaSetNode> goodMembers;
        final List<ReplicaSetNode> goodSecondaries;
        final Random random;
        final ReplicaSetErrorStatus errorStatus = validate();
        final String setName = determineSetName();
        final ReplicaSetNode master = findMaster();

        public ReplicaSet(List<ReplicaSetNode> nodeList, Random random, int acceptableLatencyMS) {
            this.random = random;
            this.all = Collections.unmodifiableList(new ArrayList(nodeList));
            this.acceptableLatencyMS = acceptableLatencyMS;
            this.goodSecondaries = Collections.unmodifiableList(calculateGoodSecondaries(this.all, calculateBestPingTime(this.all), acceptableLatencyMS));
            this.goodMembers = Collections.unmodifiableList(calculateGoodMembers(this.all, calculateBestPingTime(this.all), acceptableLatencyMS));
        }

        public List<ReplicaSetNode> getAll() {
            checkStatus();
            return this.all;
        }

        public boolean hasMaster() {
            return getMaster() != null;
        }

        public ReplicaSetNode getMaster() {
            checkStatus();
            return this.master;
        }

        public int getMaxBsonObjectSize() {
            if (hasMaster()) {
                return getMaster().getMaxBsonObjectSize();
            }
            return 4194304;
        }

        public ReplicaSetNode getASecondary() {
            checkStatus();
            if (this.goodSecondaries.isEmpty()) {
                return null;
            }
            return this.goodSecondaries.get(this.random.nextInt(this.goodSecondaries.size()));
        }

        public ReplicaSetNode getASecondary(List<Tag> tags) {
            checkStatus();
            if (tags.isEmpty()) {
                return getASecondary();
            }
            List<ReplicaSetNode> acceptableTaggedSecondaries = getGoodSecondariesByTags(tags);
            if (acceptableTaggedSecondaries.isEmpty()) {
                return null;
            }
            return acceptableTaggedSecondaries.get(this.random.nextInt(acceptableTaggedSecondaries.size()));
        }

        public ReplicaSetNode getAMember() {
            checkStatus();
            if (this.goodMembers.isEmpty()) {
                return null;
            }
            return this.goodMembers.get(this.random.nextInt(this.goodMembers.size()));
        }

        public ReplicaSetNode getAMember(List<Tag> tags) {
            checkStatus();
            if (tags.isEmpty()) {
                return getAMember();
            }
            List<ReplicaSetNode> acceptableTaggedMembers = getGoodMembersByTags(tags);
            if (acceptableTaggedMembers.isEmpty()) {
                return null;
            }
            return acceptableTaggedMembers.get(this.random.nextInt(acceptableTaggedMembers.size()));
        }

        public List<ReplicaSetNode> getGoodSecondariesByTags(List<Tag> tags) {
            checkStatus();
            List<ReplicaSetNode> taggedSecondaries = getMembersByTags(this.all, tags);
            return calculateGoodSecondaries(taggedSecondaries, calculateBestPingTime(taggedSecondaries), this.acceptableLatencyMS);
        }

        public List<ReplicaSetNode> getGoodMembersByTags(List<Tag> tags) {
            checkStatus();
            List<ReplicaSetNode> taggedMembers = getMembersByTags(this.all, tags);
            return calculateGoodMembers(taggedMembers, calculateBestPingTime(taggedMembers), this.acceptableLatencyMS);
        }

        public List<ReplicaSetNode> getGoodMembers() {
            checkStatus();
            return calculateGoodMembers(this.all, calculateBestPingTime(this.all), this.acceptableLatencyMS);
        }

        public String getSetName() {
            checkStatus();
            return this.setName;
        }

        public ReplicaSetErrorStatus getErrorStatus() {
            return this.errorStatus;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[ ");
            for (ReplicaSetNode node : getAll()) {
                sb.append(node.toJSON()).append(Constants.TERM);
            }
            sb.setLength(sb.length() - 1);
            sb.append(" ]");
            return sb.toString();
        }

        private void checkStatus() {
            if (!this.errorStatus.isOk()) {
                throw new MongoException(this.errorStatus.getError());
            }
        }

        private ReplicaSetNode findMaster() {
            for (ReplicaSetNode node : this.all) {
                if (node.master()) {
                    return node;
                }
            }
            return null;
        }

        private String determineSetName() {
            for (ReplicaSetNode node : this.all) {
                String nodeSetName = node.getSetName();
                if (nodeSetName != null && !nodeSetName.equals(StringUtils.EMPTY)) {
                    return nodeSetName;
                }
            }
            return null;
        }

        private ReplicaSetErrorStatus validate() {
            HashSet<String> nodeNames = new HashSet<>();
            for (ReplicaSetNode node : this.all) {
                String nodeSetName = node.getSetName();
                if (nodeSetName != null && !nodeSetName.equals(StringUtils.EMPTY)) {
                    nodeNames.add(nodeSetName);
                }
            }
            return nodeNames.size() <= 1 ? new ReplicaSetErrorStatus(true, null) : new ReplicaSetErrorStatus(false, "nodes with different set names detected: " + nodeNames.toString());
        }

        static float calculateBestPingTime(List<ReplicaSetNode> members) {
            float bestPingTime = Float.MAX_VALUE;
            for (ReplicaSetNode cur : members) {
                if (cur.secondary() && cur._pingTime < bestPingTime) {
                    bestPingTime = cur._pingTime;
                }
            }
            return bestPingTime;
        }

        static List<ReplicaSetNode> calculateGoodMembers(List<ReplicaSetNode> members, float bestPingTime, int acceptableLatencyMS) {
            List<ReplicaSetNode> goodSecondaries = new ArrayList<>(members.size());
            for (ReplicaSetNode cur : members) {
                if (cur.isOk() && cur._pingTime - acceptableLatencyMS <= bestPingTime) {
                    goodSecondaries.add(cur);
                }
            }
            return goodSecondaries;
        }

        static List<ReplicaSetNode> calculateGoodSecondaries(List<ReplicaSetNode> members, float bestPingTime, int acceptableLatencyMS) {
            List<ReplicaSetNode> goodSecondaries = new ArrayList<>(members.size());
            for (ReplicaSetNode cur : members) {
                if (cur.secondary() && cur._pingTime - acceptableLatencyMS <= bestPingTime) {
                    goodSecondaries.add(cur);
                }
            }
            return goodSecondaries;
        }

        static List<ReplicaSetNode> getMembersByTags(List<ReplicaSetNode> members, List<Tag> tags) {
            List<ReplicaSetNode> membersByTag = new ArrayList<>();
            for (ReplicaSetNode cur : members) {
                if (tags != null && cur.getTags() != null && cur.getTags().containsAll(tags)) {
                    membersByTag.add(cur);
                }
            }
            return membersByTag;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Immutable
    /* loaded from: classes.dex */
    public static class ReplicaSetNode extends ConnectionStatus.Node {
        private final boolean _isMaster;
        private final boolean _isSecondary;
        private final Set<String> _names;
        private final String _setName;
        private final Set<Tag> _tags;

        ReplicaSetNode(ServerAddress addr, Set<String> names, String setName, float pingTime, boolean ok, boolean isMaster, boolean isSecondary, LinkedHashMap<String, String> tags, int maxBsonObjectSize) {
            super(pingTime, addr, maxBsonObjectSize, ok);
            this._names = Collections.unmodifiableSet(new HashSet(names));
            this._setName = setName;
            this._isMaster = isMaster;
            this._isSecondary = isSecondary;
            this._tags = Collections.unmodifiableSet(getTagsFromMap(tags));
        }

        private static Set<Tag> getTagsFromMap(LinkedHashMap<String, String> tagMap) {
            Set<Tag> tagSet = new HashSet<>();
            for (Map.Entry<String, String> curEntry : tagMap.entrySet()) {
                tagSet.add(new Tag(curEntry.getKey(), curEntry.getValue()));
            }
            return tagSet;
        }

        public boolean master() {
            return this._ok && this._isMaster;
        }

        public boolean secondary() {
            return this._ok && this._isSecondary;
        }

        public Set<String> getNames() {
            return this._names;
        }

        public String getSetName() {
            return this._setName;
        }

        public Set<Tag> getTags() {
            return this._tags;
        }

        public float getPingTime() {
            return this._pingTime;
        }

        @Override // com.mongodb.ConnectionStatus.Node
        public String toJSON() {
            StringBuilder buf = new StringBuilder();
            buf.append("{ address:'").append(this._addr).append("', ");
            buf.append("ok:").append(this._ok).append(", ");
            buf.append("ping:").append(this._pingTime).append(", ");
            buf.append("isMaster:").append(this._isMaster).append(", ");
            buf.append("isSecondary:").append(this._isSecondary).append(", ");
            buf.append("setName:").append(this._setName).append(", ");
            buf.append("maxBsonObjectSize:").append(this._maxBsonObjectSize).append(", ");
            if (this._tags != null && this._tags.size() > 0) {
                List<DBObject> tagObjects = new ArrayList<>();
                for (Tag tag : this._tags) {
                    tagObjects.add(tag.toDBObject());
                }
                buf.append(new BasicDBObject("tags", tagObjects));
            }
            buf.append("}");
            return buf.toString();
        }

        @Override // com.mongodb.ConnectionStatus.Node
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ReplicaSetNode node = (ReplicaSetNode) o;
            return this._isMaster == node._isMaster && this._maxBsonObjectSize == node._maxBsonObjectSize && this._isSecondary == node._isSecondary && this._ok == node._ok && Float.compare(node._pingTime, this._pingTime) == 0 && this._addr.equals(node._addr) && this._names.equals(node._names) && this._tags.equals(node._tags) && this._setName.equals(node._setName);
        }

        @Override // com.mongodb.ConnectionStatus.Node
        public int hashCode() {
            int result = this._addr.hashCode();
            return (((((((((((((((result * 31) + (this._pingTime != 0.0f ? Float.floatToIntBits(this._pingTime) : 0)) * 31) + this._names.hashCode()) * 31) + this._tags.hashCode()) * 31) + (this._ok ? 1 : 0)) * 31) + (this._isMaster ? 1 : 0)) * 31) + (this._isSecondary ? 1 : 0)) * 31) + this._setName.hashCode()) * 31) + this._maxBsonObjectSize;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Immutable
    /* loaded from: classes.dex */
    public static final class ReplicaSetErrorStatus {
        final String error;
        final boolean ok;

        ReplicaSetErrorStatus(boolean ok, String error) {
            this.ok = ok;
            this.error = error;
        }

        public boolean isOk() {
            return this.ok;
        }

        public String getError() {
            return this.error;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Immutable
    /* loaded from: classes.dex */
    public static final class Tag {
        final String key;
        final String value;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Tag(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Tag tag = (Tag) o;
            if (this.key == null ? tag.key != null : !this.key.equals(tag.key)) {
                return false;
            }
            if (this.value != null) {
                if (this.value.equals(tag.value)) {
                    return true;
                }
            } else if (tag.value == null) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            int result = this.key != null ? this.key.hashCode() : 0;
            return (result * 31) + (this.value != null ? this.value.hashCode() : 0);
        }

        public DBObject toDBObject() {
            return new BasicDBObject(this.key, this.value);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class UpdatableReplicaSetNode extends ConnectionStatus.UpdatableNode {
        private final List<UpdatableReplicaSetNode> _all;
        boolean _isMaster;
        boolean _isSecondary;
        private final AtomicReference<String> _lastPrimarySignal;
        private final AtomicReference<Logger> _logger;
        private final Set<String> _names;
        String _setName;
        final LinkedHashMap<String, String> _tags;

        UpdatableReplicaSetNode(ServerAddress addr, List<UpdatableReplicaSetNode> all, AtomicReference<Logger> logger, Mongo mongo, MongoOptions mongoOptions, AtomicReference<String> lastPrimarySignal) {
            super(addr, mongo, mongoOptions);
            this._names = Collections.synchronizedSet(new HashSet());
            this._tags = new LinkedHashMap<>();
            this._isMaster = false;
            this._isSecondary = false;
            this._all = all;
            this._names.add(addr.toString());
            this._logger = logger;
            this._lastPrimarySignal = lastPrimarySignal;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateAddr() {
            try {
                if (this._addr.updateInetAddress()) {
                    this._port = new DBPort(this._addr, null, this._mongoOptions);
                    this._mongo.getConnector().updatePortPool(this._addr);
                    this._logger.get().log(Level.INFO, "Address of host " + this._addr.toString() + " changed to " + this._addr.getSocketAddress().toString());
                }
            } catch (UnknownHostException ex) {
                this._logger.get().log(Level.WARNING, (String) null, (Throwable) ex);
            }
        }

        void update(Set<UpdatableReplicaSetNode> seenNodes) {
            CommandResult res = update();
            if (res != null && this._ok) {
                this._isMaster = res.getBoolean("ismaster", false);
                this._isSecondary = res.getBoolean("secondary", false);
                this._lastPrimarySignal.set(res.getString("primary"));
                if (res.containsField("hosts")) {
                    for (Object x : (List) res.get("hosts")) {
                        String host = x.toString();
                        UpdatableReplicaSetNode node = _addIfNotHere(host);
                        if (node != null && seenNodes != null) {
                            seenNodes.add(node);
                        }
                    }
                }
                if (res.containsField("passives")) {
                    for (Object x2 : (List) res.get("passives")) {
                        String host2 = x2.toString();
                        UpdatableReplicaSetNode node2 = _addIfNotHere(host2);
                        if (node2 != null && seenNodes != null) {
                            seenNodes.add(node2);
                        }
                    }
                }
                if (res.containsField("tags")) {
                    DBObject tags = (DBObject) res.get("tags");
                    for (String key : tags.keySet()) {
                        this._tags.put(key, tags.get(key).toString());
                    }
                }
                if (res.containsField("setName")) {
                    this._setName = res.getString("setName", StringUtils.EMPTY);
                    if (this._logger.get() == null) {
                        this._logger.set(Logger.getLogger(ReplicaSetStatus._rootLogger.getName() + "." + this._setName));
                    }
                }
            }
        }

        @Override // com.mongodb.ConnectionStatus.UpdatableNode
        protected Logger getLogger() {
            return this._logger.get();
        }

        UpdatableReplicaSetNode _addIfNotHere(String host) {
            UpdatableReplicaSetNode n;
            UpdatableReplicaSetNode n2 = findNode(host, this._all, this._logger);
            if (n2 != null) {
                return n2;
            }
            try {
                n = new UpdatableReplicaSetNode(new ServerAddress(host), this._all, this._logger, this._mongo, this._mongoOptions, this._lastPrimarySignal);
                try {
                    this._all.add(n);
                    return n;
                } catch (UnknownHostException e) {
                    this._logger.get().log(Level.WARNING, "couldn't resolve host [" + host + "]");
                    return n;
                }
            } catch (UnknownHostException e2) {
                n = n2;
            }
        }

        private UpdatableReplicaSetNode findNode(String host, List<UpdatableReplicaSetNode> members, AtomicReference<Logger> logger) {
            for (UpdatableReplicaSetNode node : members) {
                if (node._names.contains(host)) {
                    return node;
                }
            }
            try {
                ServerAddress addr = new ServerAddress(host);
                for (UpdatableReplicaSetNode node2 : members) {
                    if (node2._addr.equals(addr)) {
                        node2._names.add(host);
                        return node2;
                    }
                }
                return null;
            } catch (UnknownHostException e) {
                logger.get().log(Level.WARNING, "couldn't resolve host [" + host + "]");
                return null;
            }
        }

        public void close() {
            this._port.close();
            this._port = null;
        }
    }

    /* loaded from: classes.dex */
    class Updater extends ConnectionStatus.BackgroundUpdater {
        private final List<UpdatableReplicaSetNode> _all;
        private volatile long _nextResolveTime;
        private final Random _random;

        Updater(List<ServerAddress> initial) {
            super("ReplicaSetStatus:Updater");
            this._random = new Random();
            this._all = new ArrayList(initial.size());
            for (ServerAddress addr : initial) {
                this._all.add(new UpdatableReplicaSetNode(addr, this._all, ReplicaSetStatus.this._logger, ReplicaSetStatus.this._mongo, ReplicaSetStatus.this._mongoOptions, ReplicaSetStatus.this._lastPrimarySignal));
            }
            this._nextResolveTime = System.currentTimeMillis() + ReplicaSetStatus.inetAddrCacheMS;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    int curUpdateIntervalMS = ConnectionStatus.updaterIntervalNoMasterMS;
                    try {
                        updateAll();
                        updateInetAddresses();
                        ReplicaSet replicaSet = new ReplicaSet(createNodeList(), this._random, ReplicaSetStatus.slaveAcceptableLatencyMS);
                        ReplicaSetStatus.this._replicaSetHolder.set(replicaSet);
                        if (replicaSet.getErrorStatus().isOk() && replicaSet.hasMaster()) {
                            ReplicaSetStatus.this._mongo.getConnector().setMaster(replicaSet.getMaster());
                            curUpdateIntervalMS = ConnectionStatus.updaterIntervalMS;
                        }
                    } catch (Exception e) {
                        ((Logger) ReplicaSetStatus.this._logger.get()).log(Level.WARNING, "couldn't do update pass", (Throwable) e);
                    }
                    Thread.sleep(curUpdateIntervalMS);
                } catch (InterruptedException e2) {
                }
            }
            ReplicaSetStatus.this._replicaSetHolder.close();
            closeAllNodes();
        }

        public synchronized void updateAll() {
            HashSet<UpdatableReplicaSetNode> seenNodes = new HashSet<>();
            for (int i = 0; i < this._all.size(); i++) {
                this._all.get(i).update(seenNodes);
            }
            if (seenNodes.size() > 0) {
                Iterator<UpdatableReplicaSetNode> it = this._all.iterator();
                while (it.hasNext()) {
                    if (!seenNodes.contains(it.next())) {
                        it.remove();
                    }
                }
            }
        }

        private List<ReplicaSetNode> createNodeList() {
            List<ReplicaSetNode> nodeList = new ArrayList<>(this._all.size());
            for (UpdatableReplicaSetNode cur : this._all) {
                nodeList.add(new ReplicaSetNode(cur._addr, cur._names, cur._setName, cur._pingTimeMS, cur._ok, cur._isMaster, cur._isSecondary, cur._tags, cur._maxBsonObjectSize));
            }
            return nodeList;
        }

        private void updateInetAddresses() {
            long now = System.currentTimeMillis();
            if (ReplicaSetStatus.inetAddrCacheMS > 0 && this._nextResolveTime < now) {
                this._nextResolveTime = ReplicaSetStatus.inetAddrCacheMS + now;
                for (UpdatableReplicaSetNode node : this._all) {
                    node.updateAddr();
                }
            }
        }

        private void closeAllNodes() {
            for (UpdatableReplicaSetNode node : this._all) {
                try {
                    node.close();
                } catch (Throwable th) {
                }
            }
        }
    }

    @Override // com.mongodb.ConnectionStatus
    ConnectionStatus.Node ensureMaster() {
        ReplicaSetNode masterNode = getMasterNode();
        if (masterNode != null) {
            return masterNode;
        }
        this._replicaSetHolder.waitForNextUpdate();
        ReplicaSetNode masterNode2 = getMasterNode();
        if (masterNode2 != null) {
            return masterNode2;
        }
        return null;
    }

    @Override // com.mongodb.ConnectionStatus
    List<ServerAddress> getServerAddressList() {
        List<ServerAddress> addrs = new ArrayList<>();
        for (ReplicaSetNode node : this._replicaSetHolder.get().getAll()) {
            addrs.add(node.getServerAddress());
        }
        return addrs;
    }

    public int getMaxBsonObjectSize() {
        return this._replicaSetHolder.get().getMaxBsonObjectSize();
    }
}
