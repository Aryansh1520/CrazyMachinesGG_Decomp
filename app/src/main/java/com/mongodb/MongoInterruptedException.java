package com.mongodb;

/* loaded from: classes.dex */
public class MongoInterruptedException extends MongoException {
    public MongoInterruptedException(InterruptedException e) {
        super("A driver operation has been interrupted", e);
    }

    public MongoInterruptedException(String message, InterruptedException e) {
        super(message, e);
    }
}
