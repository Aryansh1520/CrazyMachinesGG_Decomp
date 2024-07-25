package com.mongodb;

import org.bson.BSONObject;

/* loaded from: classes.dex */
public interface DBObject extends BSONObject {
    boolean isPartialObject();

    void markAsPartialObject();
}
