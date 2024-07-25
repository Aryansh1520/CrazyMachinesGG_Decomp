package com.mongodb;

/* loaded from: classes.dex */
public class MongoInternalException extends MongoException {
    private static final long serialVersionUID = -4415279469780082174L;

    public MongoInternalException(String msg) {
        super(msg);
    }

    public MongoInternalException(String msg, Throwable t) {
        super(msg, MongoException._massage(t));
    }
}
