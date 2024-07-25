package com.mongodb;

/* loaded from: classes.dex */
public class DBRefBase {
    final DB _db;
    final Object _id;
    private boolean _loadedPointedTo = false;
    final String _ns;
    private DBObject _pointedTo;

    public DBRefBase(DB db, String ns, Object id) {
        this._db = db;
        this._ns = ns.intern();
        this._id = id;
    }

    public DBObject fetch() throws MongoException {
        if (this._loadedPointedTo) {
            return this._pointedTo;
        }
        if (this._db == null) {
            throw new RuntimeException("no db");
        }
        DBCollection coll = this._db.getCollectionFromString(this._ns);
        this._pointedTo = coll.findOne(this._id);
        this._loadedPointedTo = true;
        return this._pointedTo;
    }

    public String toString() {
        return "{ \"$ref\" : \"" + this._ns + "\", \"$id\" : \"" + this._id + "\" }";
    }

    public Object getId() {
        return this._id;
    }

    public String getRef() {
        return this._ns;
    }

    public DB getDB() {
        return this._db;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DBRefBase dbRefBase = (DBRefBase) o;
        if (this._id == null ? dbRefBase._id != null : !this._id.equals(dbRefBase._id)) {
            return false;
        }
        if (this._ns != null) {
            if (this._ns.equals(dbRefBase._ns)) {
                return true;
            }
        } else if (dbRefBase._ns == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = this._id != null ? this._id.hashCode() : 0;
        return (result * 31) + (this._ns != null ? this._ns.hashCode() : 0);
    }
}
