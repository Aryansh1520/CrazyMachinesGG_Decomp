package com.mongodb;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import org.bson.BSONObject;
import org.bson.BasicBSONEncoder;
import org.bson.io.PoolOutputBuffer;
import org.bson.types.ObjectId;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class OutMessage extends BasicBSONEncoder {
    static AtomicInteger REQUEST_ID = new AtomicInteger(1);
    private final PoolOutputBuffer _buffer;
    private final DBCollection _collection;
    private final DBEncoder _encoder;
    private final int _id;
    private final Mongo _mongo;
    private volatile int _numDocuments;
    private final OpCode _opCode;
    private final DBObject _query;
    private final int _queryOptions;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum OpCode {
        OP_UPDATE(2001),
        OP_INSERT(2002),
        OP_QUERY(2004),
        OP_GETMORE(2005),
        OP_DELETE(2006),
        OP_KILL_CURSORS(2007);

        private final int value;

        OpCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static OutMessage insert(DBCollection collection, DBEncoder encoder, WriteConcern concern) {
        OutMessage om = new OutMessage(collection, OpCode.OP_INSERT, encoder);
        om.writeInsertPrologue(concern);
        return om;
    }

    public static OutMessage update(DBCollection collection, DBEncoder encoder, boolean upsert, boolean multi, DBObject query, DBObject o) {
        OutMessage om = new OutMessage(collection, OpCode.OP_UPDATE, encoder, query);
        om.writeUpdate(upsert, multi, query, o);
        return om;
    }

    public static OutMessage remove(DBCollection collection, DBEncoder encoder, DBObject query) {
        OutMessage om = new OutMessage(collection, OpCode.OP_DELETE, encoder, query);
        om.writeRemove();
        return om;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OutMessage query(DBCollection collection, int options, int numToSkip, int batchSize, DBObject query, DBObject fields) {
        return query(collection, options, numToSkip, batchSize, query, fields, ReadPreference.primary());
    }

    static OutMessage query(DBCollection collection, int options, int numToSkip, int batchSize, DBObject query, DBObject fields, ReadPreference readPref) {
        return query(collection, options, numToSkip, batchSize, query, fields, readPref, DefaultDBEncoder.FACTORY.create());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OutMessage query(DBCollection collection, int options, int numToSkip, int batchSize, DBObject query, DBObject fields, ReadPreference readPref, DBEncoder enc) {
        OutMessage om = new OutMessage(collection, enc, query, options, readPref);
        om.writeQuery(fields, numToSkip, batchSize);
        return om;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OutMessage getMore(DBCollection collection, long cursorId, int batchSize) {
        OutMessage om = new OutMessage(collection, OpCode.OP_GETMORE);
        om.writeGetMore(cursorId, batchSize);
        return om;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static OutMessage killCursors(Mongo mongo, int numCursors) {
        OutMessage om = new OutMessage(mongo, OpCode.OP_KILL_CURSORS);
        om.writeKillCursorsPrologue(numCursors);
        return om;
    }

    private OutMessage(Mongo m, OpCode opCode) {
        this((DBCollection) null, m, opCode, (DBEncoder) null);
    }

    private OutMessage(DBCollection collection, OpCode opCode) {
        this(collection, opCode, null);
    }

    private OutMessage(DBCollection collection, OpCode opCode, DBEncoder enc) {
        this(collection, collection.getDB().getMongo(), opCode, enc);
    }

    private OutMessage(DBCollection collection, Mongo m, OpCode opCode, DBEncoder enc) {
        this(collection, m, opCode, enc, null, -1, null);
    }

    private OutMessage(DBCollection collection, OpCode opCode, DBEncoder enc, DBObject query) {
        this(collection, collection.getDB().getMongo(), opCode, enc, query, 0, null);
    }

    private OutMessage(DBCollection collection, DBEncoder enc, DBObject query, int options, ReadPreference readPref) {
        this(collection, collection.getDB().getMongo(), OpCode.OP_QUERY, enc, query, options, readPref);
    }

    private OutMessage(DBCollection collection, Mongo m, OpCode opCode, DBEncoder enc, DBObject query, int options, ReadPreference readPref) {
        this._collection = collection;
        this._mongo = m;
        this._encoder = enc;
        this._buffer = this._mongo._bufferPool.get();
        this._buffer.reset();
        set(this._buffer);
        this._id = REQUEST_ID.getAndIncrement();
        this._opCode = opCode;
        writeMessagePrologue(opCode);
        if (query == null) {
            this._query = null;
            this._queryOptions = 0;
            return;
        }
        this._query = query;
        int allOptions = options;
        if (readPref != null && readPref.isSlaveOk()) {
            allOptions |= 4;
        }
        this._queryOptions = allOptions;
    }

    private void writeInsertPrologue(WriteConcern concern) {
        int flags = 0;
        if (concern.getContinueOnErrorForInsert()) {
            flags = 0 | 1;
        }
        writeInt(flags);
        writeCString(this._collection.getFullName());
    }

    private void writeUpdate(boolean upsert, boolean multi, DBObject query, DBObject o) {
        writeInt(0);
        writeCString(this._collection.getFullName());
        int flags = upsert ? 0 | 1 : 0;
        if (multi) {
            flags |= 2;
        }
        writeInt(flags);
        putObject(query);
        putObject(o);
    }

    private void writeRemove() {
        writeInt(0);
        writeCString(this._collection.getFullName());
        Collection<String> keys = this._query.keySet();
        if (keys.size() == 1 && keys.iterator().next().equals("_id") && (this._query.get(keys.iterator().next()) instanceof ObjectId)) {
            writeInt(1);
        } else {
            writeInt(0);
        }
        putObject(this._query);
    }

    private void writeGetMore(long cursorId, int batchSize) {
        writeInt(0);
        writeCString(this._collection.getFullName());
        writeInt(batchSize);
        writeLong(cursorId);
    }

    private void writeKillCursorsPrologue(int numCursors) {
        writeInt(0);
        writeInt(numCursors);
    }

    private void writeQuery(DBObject fields, int numToSkip, int batchSize) {
        writeInt(this._queryOptions);
        writeCString(this._collection.getFullName());
        writeInt(numToSkip);
        writeInt(batchSize);
        putObject(this._query);
        if (fields != null) {
            putObject(fields);
        }
    }

    private void writeMessagePrologue(OpCode opCode) {
        writeInt(0);
        writeInt(this._id);
        writeInt(0);
        writeInt(opCode.getValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepare() {
        this._buffer.writeInt(0, this._buffer.size());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pipe(OutputStream out) throws IOException {
        this._buffer.pipe(out);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return this._buffer.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void doneWithMessage() {
        this._buffer.reset();
        this._mongo._bufferPool.done(this._buffer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasOption(int option) {
        return (this._queryOptions & option) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getId() {
        return this._id;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpCode getOpCode() {
        return this._opCode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DBObject getQuery() {
        return this._query;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getNamespace() {
        if (this._collection != null) {
            return this._collection.getFullName();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getNumDocuments() {
        return this._numDocuments;
    }

    @Override // org.bson.BasicBSONEncoder, org.bson.BSONEncoder
    public int putObject(BSONObject o) {
        int objectSize = this._encoder.writeObject(this._buf, o);
        if (objectSize > Math.max(this._mongo.getConnector().getMaxBsonObjectSize(), 4194304)) {
            throw new MongoInternalException("DBObject of size " + objectSize + " is over Max BSON size " + this._mongo.getMaxBsonObjectSize());
        }
        this._numDocuments++;
        return objectSize;
    }
}
