package com.mongodb;

import com.mongodb.util.JSON;
import org.bson.types.BasicBSONList;

/* loaded from: classes.dex */
public class BasicDBList extends BasicBSONList implements DBObject {
    private static final long serialVersionUID = -4415279469780082174L;
    private boolean _isPartialObject;

    @Override // java.util.AbstractCollection
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

    public Object copy() {
        BasicDBList newobj = new BasicDBList();
        for (int i = 0; i < size(); i++) {
            Object val = get(i);
            if (val instanceof BasicDBObject) {
                val = ((BasicDBObject) val).copy();
            } else if (val instanceof BasicDBList) {
                val = ((BasicDBList) val).copy();
            }
            newobj.add(val);
        }
        return newobj;
    }
}
