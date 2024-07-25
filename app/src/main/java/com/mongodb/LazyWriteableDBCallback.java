package com.mongodb;

import java.util.Iterator;
import java.util.logging.Logger;

/* loaded from: classes.dex */
public class LazyWriteableDBCallback extends LazyDBCallback {
    private static final Logger log = Logger.getLogger(LazyWriteableDBCallback.class.getName());

    public LazyWriteableDBCallback(DBCollection coll) {
        super(coll);
    }

    @Override // com.mongodb.LazyDBCallback, org.bson.LazyBSONCallback
    public Object createObject(byte[] data, int offset) {
        LazyWriteableDBObject o = new LazyWriteableDBObject(data, offset, this);
        Iterator it = o.keySet().iterator();
        if (it.hasNext() && it.next().equals("$ref") && o.containsField("$id")) {
            return new DBRef(this._db, o);
        }
        return o;
    }
}
