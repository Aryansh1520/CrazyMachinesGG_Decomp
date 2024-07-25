package com.mongodb;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class QueryOpBuilder {
    private boolean explain;
    private DBObject hintObj;
    private String hintStr;
    private DBObject orderBy;
    private DBObject query;
    private DBObject readPref;
    private boolean snapshot;
    private DBObject specialFields;

    public QueryOpBuilder addQuery(DBObject query) {
        this.query = query;
        return this;
    }

    public QueryOpBuilder addOrderBy(DBObject orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public QueryOpBuilder addHint(String hint) {
        this.hintStr = hint;
        return this;
    }

    public QueryOpBuilder addHint(DBObject hint) {
        this.hintObj = hint;
        return this;
    }

    public QueryOpBuilder addSpecialFields(DBObject specialFields) {
        this.specialFields = specialFields;
        return this;
    }

    public QueryOpBuilder addExplain(boolean explain) {
        this.explain = explain;
        return this;
    }

    public QueryOpBuilder addSnapshot(boolean snapshot) {
        this.snapshot = snapshot;
        return this;
    }

    public QueryOpBuilder addReadPreference(DBObject readPref) {
        this.readPref = readPref;
        return this;
    }

    public DBObject get() {
        DBObject lclQuery = this.query;
        if (lclQuery == null) {
            lclQuery = new BasicDBObject();
        }
        if (!hasSpecialQueryFields()) {
            return lclQuery;
        }
        DBObject queryop = this.specialFields == null ? new BasicDBObject() : this.specialFields;
        addToQueryObject(queryop, "query", lclQuery, true);
        addToQueryObject(queryop, "orderby", this.orderBy, false);
        if (this.hintStr != null) {
            addToQueryObject(queryop, "$hint", this.hintStr);
        }
        if (this.hintObj != null) {
            addToQueryObject(queryop, "$hint", this.hintObj);
        }
        if (this.explain) {
            queryop.put("$explain", true);
        }
        if (this.snapshot) {
            queryop.put("$snapshot", true);
        }
        if (this.readPref != null) {
            queryop.put("$readPreference", this.readPref);
            return queryop;
        }
        return queryop;
    }

    private boolean hasSpecialQueryFields() {
        if (this.readPref == null && this.specialFields == null) {
            return (this.orderBy != null && this.orderBy.keySet().size() > 0) || this.hintStr != null || this.hintObj != null || this.snapshot || this.explain;
        }
        return true;
    }

    private void addToQueryObject(DBObject dbobj, String field, DBObject obj, boolean sendEmpty) {
        if (obj != null) {
            if (sendEmpty || obj.keySet().size() != 0) {
                addToQueryObject(dbobj, field, obj);
            }
        }
    }

    private void addToQueryObject(DBObject dbobj, String field, Object obj) {
        if (obj != null) {
            dbobj.put(field, obj);
        }
    }
}
