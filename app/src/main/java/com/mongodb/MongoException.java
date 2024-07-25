package com.mongodb;

import java.io.IOException;
import org.bson.BSONObject;

/* loaded from: classes.dex */
public class MongoException extends RuntimeException {
    private static final long serialVersionUID = -4415279469780082174L;
    final int _code;

    public MongoException(String msg) {
        super(msg);
        this._code = -3;
    }

    public MongoException(int code, String msg) {
        super(msg);
        this._code = code;
    }

    public MongoException(String msg, Throwable t) {
        super(msg, _massage(t));
        this._code = -4;
    }

    public MongoException(int code, String msg, Throwable t) {
        super(msg, _massage(t));
        this._code = code;
    }

    public MongoException(BSONObject o) {
        this(ServerError.getCode(o), ServerError.getMsg(o, "UNKNOWN"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MongoException parse(BSONObject o) {
        String s = ServerError.getMsg(o, null);
        if (s == null) {
            return null;
        }
        return new MongoException(ServerError.getCode(o), s);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Throwable _massage(Throwable t) {
        if (t instanceof Network) {
            return ((Network) t)._ioe;
        }
        return t;
    }

    /* loaded from: classes.dex */
    public static class Network extends MongoException {
        private static final long serialVersionUID = -4415279469780082174L;
        final IOException _ioe;

        public Network(String msg, IOException ioe) {
            super(-2, msg, ioe);
            this._ioe = ioe;
        }

        public Network(IOException ioe) {
            super(ioe.toString(), ioe);
            this._ioe = ioe;
        }
    }

    /* loaded from: classes.dex */
    public static class DuplicateKey extends MongoException {
        private static final long serialVersionUID = -4415279469780082174L;

        public DuplicateKey(int code, String msg) {
            super(code, msg);
        }
    }

    /* loaded from: classes.dex */
    public static class CursorNotFound extends MongoException {
        private static final long serialVersionUID = -4415279469780082174L;
        private final long cursorId;
        private final ServerAddress serverAddress;

        public CursorNotFound(long cursorId, ServerAddress serverAddress) {
            super(-5, "cursor " + cursorId + " not found on server " + serverAddress);
            this.cursorId = cursorId;
            this.serverAddress = serverAddress;
        }

        public long getCursorId() {
            return this.cursorId;
        }

        public ServerAddress getServerAddress() {
            return this.serverAddress;
        }
    }

    public int getCode() {
        return this._code;
    }
}
