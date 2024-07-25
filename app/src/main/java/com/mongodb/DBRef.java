package com.mongodb;

import org.bson.BSONObject;

/* loaded from: classes.dex */
public class DBRef extends DBRefBase {
    static final boolean D = Boolean.getBoolean("DEBUG.DBREF");

    public DBRef(DB db, BSONObject o) {
        super(db, o.get("$ref").toString(), o.get("$id"));
    }

    public DBRef(DB db, String ns, Object id) {
        super(db, ns, id);
    }

    public static DBObject fetch(DB db, DBObject ref) {
        Object id;
        String ns = (String) ref.get("$ref");
        if (ns == null || (id = ref.get("$id")) == null) {
            return null;
        }
        return db.getCollection(ns).findOne((DBObject) new BasicDBObject("_id", id));
    }
}
