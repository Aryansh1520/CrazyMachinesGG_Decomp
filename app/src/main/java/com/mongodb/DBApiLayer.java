package com.mongodb;

import com.mongodb.MongoException;
import com.mongodb.util.JSON;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.BSONObject;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class DBApiLayer extends DB {
    static final int NUM_CURSORS_BEFORE_KILL = 100;
    static final int NUM_CURSORS_PER_BATCH = 20000;
    static final Level TRACE_LEVEL;
    static final Logger TRACE_LOGGER = Logger.getLogger("com.mongodb.TRACE");
    final ConcurrentHashMap<String, MyCollection> _collections;
    final DBConnector _connector;
    ConcurrentLinkedQueue<DeadCursor> _deadCursorIds;
    final String _root;
    final String _rootPlusDot;

    static {
        TRACE_LEVEL = Boolean.getBoolean("DB.TRACE") ? Level.INFO : Level.FINEST;
    }

    static boolean willTrace() {
        return TRACE_LOGGER.isLoggable(TRACE_LEVEL);
    }

    static void trace(String s) {
        TRACE_LOGGER.log(TRACE_LEVEL, s);
    }

    static int chooseBatchSize(int batchSize, int limit, int fetched) {
        int res;
        int bs = Math.abs(batchSize);
        int remaining = limit > 0 ? limit - fetched : 0;
        if (bs == 0 && remaining > 0) {
            res = remaining;
        } else if (bs > 0 && remaining == 0) {
            res = bs;
        } else {
            res = Math.min(bs, remaining);
        }
        if (batchSize < 0) {
            res = -res;
        }
        if (res == 1) {
            return -1;
        }
        return res;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DBApiLayer(Mongo mongo, String name, DBConnector connector) {
        super(mongo, name);
        this._collections = new ConcurrentHashMap<>();
        this._deadCursorIds = new ConcurrentLinkedQueue<>();
        if (connector == null) {
            throw new IllegalArgumentException("need a connector: " + name);
        }
        this._root = name;
        this._rootPlusDot = this._root + ".";
        this._connector = connector;
    }

    @Override // com.mongodb.DB
    public void requestStart() {
        this._connector.requestStart();
    }

    @Override // com.mongodb.DB
    public void requestDone() {
        this._connector.requestDone();
    }

    @Override // com.mongodb.DB
    public void requestEnsureConnection() {
        this._connector.requestEnsureConnection();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.mongodb.DB
    public MyCollection doGetCollection(String name) {
        MyCollection c = this._collections.get(name);
        if (c != null) {
            return c;
        }
        MyCollection c2 = new MyCollection(name);
        MyCollection old = this._collections.putIfAbsent(name, c2);
        return old == null ? c2 : old;
    }

    @Override // com.mongodb.DB
    public void cleanCursors(boolean force) {
        int sz = this._deadCursorIds.size();
        if (sz != 0) {
            if (force || sz >= 100) {
                Bytes.LOGGER.info("going to kill cursors : " + sz);
                Map<ServerAddress, List<Long>> m = new HashMap<>();
                while (true) {
                    DeadCursor c = this._deadCursorIds.poll();
                    if (c == null) {
                        break;
                    }
                    List<Long> x = m.get(c.host);
                    if (x == null) {
                        x = new LinkedList<>();
                        m.put(c.host, x);
                    }
                    x.add(Long.valueOf(c.id));
                }
                for (Map.Entry<ServerAddress, List<Long>> e : m.entrySet()) {
                    try {
                        killCursors(e.getKey(), e.getValue());
                    } catch (Throwable t) {
                        Bytes.LOGGER.log(Level.WARNING, "can't clean cursors", t);
                        Iterator i$ = e.getValue().iterator();
                        while (i$.hasNext()) {
                            this._deadCursorIds.add(new DeadCursor(i$.next().longValue(), e.getKey()));
                        }
                    }
                }
            }
        }
    }

    void killCursors(ServerAddress addr, List<Long> all) {
        if (all != null && all.size() != 0) {
            OutMessage om = OutMessage.killCursors(this._mongo, Math.min(NUM_CURSORS_PER_BATCH, all.size()));
            int soFar = 0;
            int totalSoFar = 0;
            for (Long l : all) {
                om.writeLong(l.longValue());
                totalSoFar++;
                soFar++;
                if (soFar >= NUM_CURSORS_PER_BATCH) {
                    this._connector.say(this, om, WriteConcern.NONE);
                    om = OutMessage.killCursors(this._mongo, Math.min(NUM_CURSORS_PER_BATCH, all.size() - totalSoFar));
                    soFar = 0;
                }
            }
            this._connector.say(this, om, WriteConcern.NONE, addr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class MyCollection extends DBCollection {
        final String _fullNameSpace;

        MyCollection(String name) {
            super(DBApiLayer.this, name);
            this._fullNameSpace = DBApiLayer.this._root + "." + name;
        }

        @Override // com.mongodb.DBCollection
        public void doapply(DBObject o) {
        }

        @Override // com.mongodb.DBCollection
        public void drop() {
            DBApiLayer.this._collections.remove(getName());
            super.drop();
        }

        @Override // com.mongodb.DBCollection
        public WriteResult insert(DBObject[] arr, WriteConcern concern, DBEncoder encoder) {
            if (concern == null) {
                throw new IllegalArgumentException("Write concern can not be null");
            }
            return insert(arr, true, concern, encoder);
        }

        protected WriteResult insert(DBObject[] arr, boolean shouldApply, WriteConcern concern, DBEncoder encoder) {
            if (encoder == null) {
                encoder = DefaultDBEncoder.FACTORY.create();
            }
            if (DBApiLayer.willTrace()) {
                for (DBObject dBObject : arr) {
                    DBApiLayer.trace("save:  " + this._fullNameSpace + " " + JSON.serialize(dBObject));
                }
            }
            if (shouldApply) {
                for (DBObject o : arr) {
                    apply(o);
                    _checkObject(o, false, false);
                    Object id = o.get("_id");
                    if (id instanceof ObjectId) {
                        ((ObjectId) id).notNew();
                    }
                }
            }
            WriteResult last = null;
            int cur = 0;
            int maxsize = DBApiLayer.this._mongo.getMaxBsonObjectSize();
            while (cur < arr.length) {
                OutMessage om = OutMessage.insert(this, encoder, concern);
                while (true) {
                    if (cur < arr.length) {
                        om.putObject(arr[cur]);
                        if (om.size() <= maxsize * 2) {
                            cur++;
                        } else {
                            cur++;
                            break;
                        }
                    }
                }
                last = DBApiLayer.this._connector.say(this._db, om, concern);
            }
            return last;
        }

        @Override // com.mongodb.DBCollection
        public WriteResult remove(DBObject o, WriteConcern concern, DBEncoder encoder) {
            if (concern == null) {
                throw new IllegalArgumentException("Write concern can not be null");
            }
            if (encoder == null) {
                encoder = DefaultDBEncoder.FACTORY.create();
            }
            if (DBApiLayer.willTrace()) {
                DBApiLayer.trace("remove: " + this._fullNameSpace + " " + JSON.serialize(o));
            }
            OutMessage om = OutMessage.remove(this, encoder, o);
            return DBApiLayer.this._connector.say(this._db, om, concern);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.mongodb.DBCollection
        public Iterator<DBObject> __find(DBObject ref, DBObject fields, int numToSkip, int batchSize, int limit, int options, ReadPreference readPref, DBDecoder decoder) {
            return __find(ref, fields, numToSkip, batchSize, limit, options, readPref, decoder, DefaultDBEncoder.FACTORY.create());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.mongodb.DBCollection
        public Iterator<DBObject> __find(DBObject ref, DBObject fields, int numToSkip, int batchSize, int limit, int options, ReadPreference readPref, DBDecoder decoder, DBEncoder encoder) {
            if (ref == null) {
                ref = new BasicDBObject();
            }
            if (DBApiLayer.willTrace()) {
                DBApiLayer.trace("find: " + this._fullNameSpace + " " + JSON.serialize(ref));
            }
            OutMessage query = OutMessage.query(this, options, numToSkip, DBApiLayer.chooseBatchSize(batchSize, limit, 0), ref, fields, readPref, encoder);
            Response res = DBApiLayer.this._connector.call(this._db, this, query, null, 2, readPref, decoder);
            if (res.size() == 1) {
                BSONObject foo = res.get(0);
                MongoException e = MongoException.parse(foo);
                if (e != null && !this._name.equals("$cmd")) {
                    throw e;
                }
            }
            return new Result(DBApiLayer.this, this, res, batchSize, limit, options, decoder);
        }

        @Override // com.mongodb.DBCollection
        public WriteResult update(DBObject query, DBObject o, boolean upsert, boolean multi, WriteConcern concern, DBEncoder encoder) {
            if (o == null) {
                throw new IllegalArgumentException("update can not be null");
            }
            if (concern == null) {
                throw new IllegalArgumentException("Write concern can not be null");
            }
            if (encoder == null) {
                encoder = DefaultDBEncoder.FACTORY.create();
            }
            if (!o.keySet().isEmpty()) {
                String key = o.keySet().iterator().next();
                if (!key.startsWith("$")) {
                    _checkObject(o, false, false);
                }
            }
            if (DBApiLayer.willTrace()) {
                DBApiLayer.trace("update: " + this._fullNameSpace + " " + JSON.serialize(query) + " " + JSON.serialize(o));
            }
            OutMessage om = OutMessage.update(this, encoder, upsert, multi, query, o);
            return DBApiLayer.this._connector.say(this._db, om, concern);
        }

        @Override // com.mongodb.DBCollection
        public void createIndex(DBObject keys, DBObject options, DBEncoder encoder) {
            if (encoder == null) {
                encoder = DefaultDBEncoder.FACTORY.create();
            }
            DBObject full = new BasicDBObject();
            for (String k : options.keySet()) {
                full.put(k, options.get(k));
            }
            full.put("key", keys);
            MyCollection idxs = DBApiLayer.this.doGetCollection("system.indexes");
            if (idxs.findOne(full) == null) {
                idxs.insert(new DBObject[]{full}, false, WriteConcern.SAFE, encoder);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class Result implements Iterator<DBObject> {
        int _batchSize;
        final MyCollection _collection;
        Iterator<DBObject> _cur;
        Response _curResult;
        final DBDecoder _decoder;
        final ServerAddress _host;
        int _limit;
        private final OptionalFinalizer _optionalFinalizer;
        final int _options;
        final /* synthetic */ DBApiLayer this$0;
        private long _totalBytes = 0;
        private int _numGetMores = 0;
        private List<Integer> _sizes = new ArrayList();
        private int _numFetched = 0;

        Result(DBApiLayer dBApiLayer, MyCollection coll, Response res, int batchSize, int limit, int options, DBDecoder decoder) {
            this.this$0 = dBApiLayer;
            this._collection = coll;
            this._batchSize = batchSize;
            this._limit = limit;
            this._options = options;
            this._host = res._host;
            this._decoder = decoder;
            init(res);
            this._optionalFinalizer = (!dBApiLayer._mongo.getMongoOptions().isCursorFinalizerEnabled() || res.cursor() == 0) ? null : new OptionalFinalizer();
        }

        private void init(Response res) {
            this._totalBytes += res._len;
            this._curResult = res;
            this._cur = res.iterator();
            this._sizes.add(Integer.valueOf(res.size()));
            this._numFetched += res.size();
            if ((res._flags & 1) > 0) {
                throw new MongoException.CursorNotFound(res._cursor, res.serverUsed());
            }
            if (res._cursor != 0 && this._limit > 0 && this._limit - this._numFetched <= 0) {
                killCursor();
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public DBObject next() {
            if (this._cur.hasNext()) {
                return this._cur.next();
            }
            if (!this._curResult.hasGetMore(this._options)) {
                throw new RuntimeException("no more");
            }
            _advance();
            return next();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            boolean hasNext = this._cur.hasNext();
            while (!hasNext) {
                if (!this._curResult.hasGetMore(this._options)) {
                    return false;
                }
                _advance();
                hasNext = this._cur.hasNext();
                if (!hasNext) {
                    if ((this._options & 32) == 0) {
                        return false;
                    }
                    if ((this._curResult._flags & 8) == 0) {
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            throw new MongoInterruptedException(e);
                        }
                    } else {
                        continue;
                    }
                }
            }
            return hasNext;
        }

        private void _advance() {
            if (this._curResult.cursor() <= 0) {
                throw new RuntimeException("can't advance a cursor <= 0");
            }
            OutMessage m = OutMessage.getMore(this._collection, this._curResult.cursor(), DBApiLayer.chooseBatchSize(this._batchSize, this._limit, this._numFetched));
            Response res = this.this$0._connector.call(this.this$0, this._collection, m, this._host, this._decoder);
            this._numGetMores++;
            init(res);
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new RuntimeException("can't remove this way");
        }

        public int getBatchSize() {
            return this._batchSize;
        }

        public void setBatchSize(int size) {
            this._batchSize = size;
        }

        public String toString() {
            return "DBCursor";
        }

        public long totalBytes() {
            return this._totalBytes;
        }

        public long getCursorId() {
            if (this._curResult == null) {
                return 0L;
            }
            return this._curResult._cursor;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int numGetMores() {
            return this._numGetMores;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public List<Integer> getSizes() {
            return Collections.unmodifiableList(this._sizes);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void close() {
            if (this._curResult != null) {
                killCursor();
                this._curResult = null;
                this._cur = null;
            }
        }

        void killCursor() {
            if (this._curResult != null) {
                long curId = this._curResult.cursor();
                if (curId != 0) {
                    List<Long> l = new ArrayList<>();
                    l.add(Long.valueOf(curId));
                    try {
                        this.this$0.killCursors(this._host, l);
                    } catch (Throwable t) {
                        Bytes.LOGGER.log(Level.WARNING, "can't clean 1 cursor", t);
                        this.this$0._deadCursorIds.add(new DeadCursor(curId, this._host));
                    }
                    this._curResult._cursor = 0L;
                }
            }
        }

        public ServerAddress getServerAddress() {
            return this._host;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean hasFinalizer() {
            return this._optionalFinalizer != null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class OptionalFinalizer {
            private OptionalFinalizer() {
            }

            protected void finalize() {
                if (Result.this._curResult != null) {
                    long curId = Result.this._curResult.cursor();
                    Result.this._curResult = null;
                    Result.this._cur = null;
                    if (curId != 0) {
                        Result.this.this$0._deadCursorIds.add(new DeadCursor(curId, Result.this._host));
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class DeadCursor {
        final ServerAddress host;
        final long id;

        DeadCursor(long a, ServerAddress b) {
            this.id = a;
            this.host = b;
        }
    }
}
