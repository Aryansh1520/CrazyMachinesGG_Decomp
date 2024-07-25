package com.mongodb;

/* loaded from: classes.dex */
public class AggregationOutput {
    protected final DBObject _cmd;
    protected final CommandResult _commandResult;
    protected final Iterable<DBObject> _resultSet;

    public Iterable<DBObject> results() {
        return this._resultSet;
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

    public AggregationOutput(DBObject cmd, CommandResult raw) {
        this._commandResult = raw;
        this._cmd = cmd;
        if (raw.containsField("result")) {
            this._resultSet = (Iterable) raw.get("result");
            return;
        }
        throw new IllegalArgumentException("result undefined");
    }
}
