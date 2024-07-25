package com.mongodb;

import com.mongodb.MongoException;
import java.io.IOException;

/* loaded from: classes.dex */
public class WriteResult {
    private final DB _db;
    private long _lastCall;
    private WriteConcern _lastConcern;
    private CommandResult _lastErrorResult;
    private final boolean _lazy;
    private final DBPort _port;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WriteResult(CommandResult o, WriteConcern concern) {
        this._lastErrorResult = o;
        this._lastConcern = concern;
        this._lazy = false;
        this._port = null;
        this._db = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WriteResult(DB db, DBPort p, WriteConcern concern) {
        this._db = db;
        this._port = p;
        this._lastCall = p._calls;
        this._lastConcern = concern;
        this._lazy = true;
    }

    public CommandResult getCachedLastError() {
        return this._lastErrorResult;
    }

    public WriteConcern getLastConcern() {
        return this._lastConcern;
    }

    public synchronized CommandResult getLastError() {
        return getLastError(null);
    }

    public synchronized CommandResult getLastError(WriteConcern concern) {
        CommandResult commandResult;
        if (this._lastErrorResult != null && (concern == null || (this._lastConcern != null && this._lastConcern.getW() >= concern.getW()))) {
            commandResult = this._lastErrorResult;
        } else if (this._port != null) {
            try {
                this._lastErrorResult = this._port.tryGetLastError(this._db, this._lastCall, concern == null ? new WriteConcern() : concern);
                if (this._lastErrorResult == null) {
                    throw new IllegalStateException("The connection may have been used since this write, cannot obtain a result");
                }
                this._lastConcern = concern;
                this._lastCall++;
                commandResult = this._lastErrorResult;
            } catch (IOException ioe) {
                throw new MongoException.Network(ioe.getMessage(), ioe);
            }
        } else {
            throw new IllegalStateException("Don't have a port to obtain a write result, and existing one is not good enough.");
        }
        return commandResult;
    }

    public String getError() {
        Object foo = getField("err");
        if (foo == null) {
            return null;
        }
        return foo.toString();
    }

    public int getN() {
        return getLastError().getInt("n");
    }

    public Object getField(String name) {
        return getLastError().get(name);
    }

    public boolean isLazy() {
        return this._lazy;
    }

    public String toString() {
        CommandResult res = getCachedLastError();
        return res != null ? res.toString() : "N/A";
    }
}
