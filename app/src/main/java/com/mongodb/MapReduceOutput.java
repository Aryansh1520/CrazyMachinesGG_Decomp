package com.mongodb;

/* loaded from: classes.dex */
public class MapReduceOutput {
    final DBObject _cmd;
    final DBCollection _coll;
    final String _collname;
    final CommandResult _commandResult;
    final BasicDBObject _counts;
    String _dbname;
    final Iterable<DBObject> _resultSet;

    public MapReduceOutput(DBCollection from, DBObject cmd, CommandResult raw) {
        this._dbname = null;
        this._commandResult = raw;
        this._cmd = cmd;
        if (raw.containsField("results")) {
            this._coll = null;
            this._collname = null;
            this._resultSet = (Iterable) raw.get("results");
        } else {
            Object res = raw.get("result");
            if (res instanceof String) {
                this._collname = (String) res;
            } else {
                BasicDBObject output = (BasicDBObject) res;
                this._collname = output.getString("collection");
                this._dbname = output.getString("db");
            }
            DB db = from._db;
            this._coll = (this._dbname != null ? db.getSisterDB(this._dbname) : db).getCollection(this._collname);
            this._coll.setOptions(this._coll.getOptions() & (-5));
            this._resultSet = this._coll.find();
        }
        this._counts = (BasicDBObject) raw.get("counts");
    }

    public Iterable<DBObject> results() {
        return this._resultSet;
    }

    public void drop() {
        if (this._coll != null) {
            this._coll.drop();
        }
    }

    public DBCollection getOutputCollection() {
        return this._coll;
    }

    @Deprecated
    public BasicDBObject getRaw() {
        return this._commandResult;
    }

    public CommandResult getCommandResult() {
        return this._commandResult;
    }

    public DBObject getCommand() {
        return this._cmd;
    }

    public ServerAddress getServerUsed() {
        return this._commandResult.getServerUsed();
    }

    public String toString() {
        return this._commandResult.toString();
    }
}
