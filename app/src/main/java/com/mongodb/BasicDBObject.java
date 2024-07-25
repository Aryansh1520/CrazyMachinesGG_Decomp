package com.mongodb;

import com.mongodb.util.JSON;
import java.util.Map;
import org.bson.BasicBSONObject;

/* loaded from: classes.dex */
public class BasicDBObject extends BasicBSONObject implements DBObject {
    private static final long serialVersionUID = -4415279469780082174L;
    private boolean _isPartialObject;

    public BasicDBObject() {
    }

    public BasicDBObject(int size) {
        super(size);
    }

    public BasicDBObject(String key, Object value) {
        super(key, value);
    }

    public BasicDBObject(Map m) {
        super(m);
    }

    @Override // com.mongodb.DBObject
    public boolean isPartialObject() {
        return this._isPartialObject;
    }

    @Override // com.mongodb.DBObject
    public void markAsPartialObject() {
        this._isPartialObject = true;
    }

    @Override // org.bson.BasicBSONObject, java.util.AbstractMap
    public String toString() {
        return JSON.serialize(this);
    }

    @Override // org.bson.BasicBSONObject
    public BasicDBObject append(String key, Object val) {
        put(key, val);
        return this;
    }

    public Object copy() {
        BasicDBObject newobj = new BasicDBObject(toMap());
        for (String field : keySet()) {
            Object val = get(field);
            if (val instanceof BasicDBObject) {
                newobj.put(field, ((BasicDBObject) val).copy());
            } else if (val instanceof BasicDBList) {
                newobj.put(field, ((BasicDBList) val).copy());
            }
        }
        return newobj;
    }
}
