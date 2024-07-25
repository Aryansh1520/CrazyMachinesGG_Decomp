package org.bson;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.io.BSONByteBuffer;

/* loaded from: classes.dex */
public class LazyDBList extends LazyBSONList implements DBObject {
    private static final long serialVersionUID = -4415279469780082174L;
    private boolean _isPartialObject;

    public LazyDBList(byte[] data, LazyBSONCallback callback) {
        super(data, callback);
    }

    public LazyDBList(byte[] data, int offset, LazyBSONCallback callback) {
        super(data, offset, callback);
    }

    public LazyDBList(BSONByteBuffer buffer, LazyBSONCallback callback) {
        super(buffer, callback);
    }

    public LazyDBList(BSONByteBuffer buffer, int offset, LazyBSONCallback callback) {
        super(buffer, offset, callback);
    }

    @Override // org.bson.LazyBSONObject
    public String toString() {
        return JSON.serialize(this);
    }

    @Override // com.mongodb.DBObject
    public boolean isPartialObject() {
        return this._isPartialObject;
    }

    @Override // com.mongodb.DBObject
    public void markAsPartialObject() {
        this._isPartialObject = true;
    }
}
