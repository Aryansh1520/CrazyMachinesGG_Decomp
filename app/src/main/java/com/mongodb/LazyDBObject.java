package com.mongodb;

import org.bson.LazyBSONCallback;
import org.bson.LazyBSONObject;
import org.bson.io.BSONByteBuffer;

/* loaded from: classes.dex */
public class LazyDBObject extends LazyBSONObject implements DBObject {
    private boolean _partial;

    @Override // com.mongodb.DBObject
    public void markAsPartialObject() {
        this._partial = true;
    }

    @Override // com.mongodb.DBObject
    public boolean isPartialObject() {
        return this._partial;
    }

    public LazyDBObject(BSONByteBuffer buff, LazyBSONCallback cbk) {
        super(buff, cbk);
        this._partial = false;
    }

    public LazyDBObject(BSONByteBuffer buff, int offset, LazyBSONCallback cbk) {
        super(buff, offset, cbk);
        this._partial = false;
    }

    public LazyDBObject(byte[] data, LazyBSONCallback cbk) {
        this(data, 0, cbk);
    }

    public LazyDBObject(byte[] data, int offset, LazyBSONCallback cbk) {
        super(data, offset, cbk);
        this._partial = false;
    }
}
