package org.bson;

import com.mongodb.LazyDBObject;
import java.util.List;
import java.util.logging.Logger;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class LazyBSONCallback extends EmptyBSONCallback {
    private static final Logger log = Logger.getLogger("org.bson.LazyBSONCallback");
    private Object _root;

    @Override // org.bson.EmptyBSONCallback, org.bson.BSONCallback
    public void objectStart() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.EmptyBSONCallback, org.bson.BSONCallback
    public void objectStart(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.EmptyBSONCallback, org.bson.BSONCallback
    public void objectStart(boolean array) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.EmptyBSONCallback, org.bson.BSONCallback
    public Object objectDone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // org.bson.EmptyBSONCallback, org.bson.BSONCallback
    public void reset() {
        this._root = null;
    }

    @Override // org.bson.EmptyBSONCallback, org.bson.BSONCallback
    public Object get() {
        return this._root;
    }

    @Override // org.bson.EmptyBSONCallback, org.bson.BSONCallback
    public void gotBinary(String name, byte type, byte[] data) {
        setRootObject(createObject(data, 0));
    }

    public void setRootObject(Object root) {
        this._root = root;
    }

    public Object createObject(byte[] data, int offset) {
        return new LazyDBObject(data, offset, this);
    }

    public List createArray(byte[] data, int offset) {
        return new LazyDBList(data, offset, this);
    }

    public Object createDBRef(String ns, ObjectId id) {
        return new BasicBSONObject("$ns", ns).append("$id", id);
    }
}
