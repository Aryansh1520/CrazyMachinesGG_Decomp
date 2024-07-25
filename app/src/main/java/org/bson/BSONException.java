package org.bson;

/* loaded from: classes.dex */
public class BSONException extends RuntimeException {
    private static final long serialVersionUID = -4415279469780082174L;
    private Integer _errorCode;

    public BSONException(String msg) {
        super(msg);
        this._errorCode = null;
    }

    public BSONException(int errorCode, String msg) {
        super(msg);
        this._errorCode = null;
        this._errorCode = Integer.valueOf(errorCode);
    }

    public BSONException(String msg, Throwable t) {
        super(msg, t);
        this._errorCode = null;
    }

    public BSONException(int errorCode, String msg, Throwable t) {
        super(msg, t);
        this._errorCode = null;
        this._errorCode = Integer.valueOf(errorCode);
    }

    public Integer getErrorCode() {
        return this._errorCode;
    }

    public boolean hasErrorCode() {
        return this._errorCode != null;
    }
}
