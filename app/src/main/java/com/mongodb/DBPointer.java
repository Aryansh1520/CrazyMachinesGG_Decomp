package com.mongodb;

import org.bson.types.ObjectId;

@Deprecated
/* loaded from: classes.dex */
public class DBPointer extends DBRefBase {
    static final boolean D = Boolean.getBoolean("DEBUG.DBPOINTER");
    final String _fieldName;
    final DBObject _parent;

    public DBPointer(String ns, ObjectId id) {
        this(null, null, null, ns, id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DBPointer(DBObject parent, String fieldName, DB db, String ns, ObjectId id) {
        super(db, ns, id);
        this._parent = parent;
        this._fieldName = fieldName;
    }

    @Override // com.mongodb.DBRefBase
    public String toString() {
        return "{ \"$ref\" : \"" + this._ns + "\", \"$id\" : ObjectId(\"" + this._id + "\") }";
    }

    @Override // com.mongodb.DBRefBase
    public ObjectId getId() {
        return (ObjectId) this._id;
    }
}
