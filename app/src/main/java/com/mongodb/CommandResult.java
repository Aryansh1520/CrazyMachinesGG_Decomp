package com.mongodb;

import com.mongodb.MongoException;

/* loaded from: classes.dex */
public class CommandResult extends BasicDBObject {
    private static final long serialVersionUID = 1;
    private final DBObject _cmd;
    private final ServerAddress _host;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CommandResult(ServerAddress srv) {
        this(null, srv);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CommandResult(DBObject cmd, ServerAddress srv) {
        if (srv == null) {
            throw new IllegalArgumentException("server address is null");
        }
        this._cmd = cmd;
        this._host = srv;
        put("serverUsed", (Object) srv.toString());
    }

    public boolean ok() {
        Object o = get("ok");
        if (o == null) {
            throw new IllegalArgumentException("'ok' should never be null...");
        }
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        }
        if (o instanceof Number) {
            return ((Number) o).intValue() == 1;
        }
        throw new IllegalArgumentException("can't figure out what to do with: " + o.getClass().getName());
    }

    public String getErrorMessage() {
        Object foo = get("errmsg");
        if (foo == null) {
            return null;
        }
        return foo.toString();
    }

    public MongoException getException() {
        if (!ok()) {
            StringBuilder buf = new StringBuilder();
            if (this._cmd != null) {
                String cmdName = this._cmd.keySet().iterator().next();
                buf.append("command failed [").append(cmdName).append("]: ");
            } else {
                buf.append("operation failed: ");
            }
            buf.append(toString());
            return new CommandFailure(this, buf.toString());
        }
        if (hasErr()) {
            Object foo = get("err");
            int code = getCode();
            String s = foo.toString();
            if (code == 11000 || code == 11001 || s.startsWith("E11000") || s.startsWith("E11001")) {
                return new MongoException.DuplicateKey(code, s);
            }
            return new MongoException(code, s);
        }
        return null;
    }

    private int getCode() {
        if (!(get("code") instanceof Number)) {
            return -1;
        }
        int code = ((Number) get("code")).intValue();
        return code;
    }

    boolean hasErr() {
        Object o = get("err");
        return o != null && ((String) o).length() > 0;
    }

    public void throwOnError() {
        if (!ok() || hasErr()) {
            throw getException();
        }
    }

    public ServerAddress getServerUsed() {
        return this._host;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class CommandFailure extends MongoException {
        private static final long serialVersionUID = 1;

        public CommandFailure(CommandResult res, String msg) {
            super(ServerError.getCode(res), msg);
        }
    }
}
