package com.mongodb;

/* loaded from: classes.dex */
public class GroupCommand {
    DBObject condition;
    String finalize;
    DBObject initial;
    String input;
    DBObject keys;
    String reduce;

    public GroupCommand(DBCollection inputCollection, DBObject keys, DBObject condition, DBObject initial, String reduce, String finalize) {
        this.input = inputCollection.getName();
        this.keys = keys;
        this.condition = condition;
        this.initial = initial;
        this.reduce = reduce;
        this.finalize = finalize;
    }

    public DBObject toDBObject() {
        BasicDBObject args = new BasicDBObject();
        args.put("ns", (Object) this.input);
        args.put("key", (Object) this.keys);
        args.put("cond", (Object) this.condition);
        args.put("$reduce", (Object) this.reduce);
        args.put("initial", (Object) this.initial);
        if (this.finalize != null) {
            args.put("finalize", (Object) this.finalize);
        }
        return new BasicDBObject("group", args);
    }
}
