package com.mongodb;

import org.bson.BSONObject;

/* loaded from: classes.dex */
public class ServerError {
    final int _code;
    final String _err;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ServerError(DBObject o) {
        this._err = getMsg(o, null);
        if (this._err == null) {
            throw new IllegalArgumentException("need to have $err");
        }
        this._code = getCode(o);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getMsg(BSONObject o, String def) {
        Object e = o.get("$err");
        if (e == null) {
            e = o.get("err");
        }
        if (e == null) {
            e = o.get("errmsg");
        }
        if (e == null) {
            return def;
        }
        String def2 = e.toString();
        return def2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getCode(BSONObject o) {
        Object c = o.get("code");
        if (c == null) {
            c = o.get("$code");
        }
        if (c == null) {
            c = o.get("assertionCode");
        }
        if (c == null) {
            return -5;
        }
        return ((Number) c).intValue();
    }

    public String getError() {
        return this._err;
    }

    public int getCode() {
        return this._code;
    }

    public boolean isNotMasterError() {
        switch (this._code) {
            case 10054:
            case 10056:
            case 10058:
            case 10107:
            case 13435:
            case 13436:
                return true;
            default:
                return this._err.startsWith("not master");
        }
    }

    public String toString() {
        return this._code > 0 ? this._code + " " + this._err : this._err;
    }
}
