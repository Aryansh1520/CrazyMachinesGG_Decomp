package com.mongodb;

import java.util.Iterator;
import java.util.logging.Logger;
import org.bson.LazyBSONCallback;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class LazyDBCallback extends LazyBSONCallback implements DBCallback {
    private static final Logger log = Logger.getLogger(LazyDBCallback.class.getName());
    final DBCollection _collection;
    final DB _db;

    public LazyDBCallback(DBCollection coll) {
        this._collection = coll;
        this._db = this._collection == null ? null : this._collection.getDB();
    }

    @Override // org.bson.LazyBSONCallback
    public Object createObject(byte[] data, int offset) {
        LazyDBObject o = new LazyDBObject(data, offset, this);
        Iterator it = o.keySet().iterator();
        if (it.hasNext() && it.next().equals("$ref") && o.containsField("$id")) {
            return new DBRef(this._db, o);
        }
        return o;
    }

    @Override // org.bson.LazyBSONCallback
    public Object createDBRef(String ns, ObjectId id) {
        return new DBRef(this._db, ns, id);
    }
}
