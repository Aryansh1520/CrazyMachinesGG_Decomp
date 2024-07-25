package com.mongodb;

import org.bson.BSONObject;
import org.bson.io.OutputBuffer;

/* loaded from: classes.dex */
public interface DBEncoder {
    int writeObject(OutputBuffer outputBuffer, BSONObject bSONObject);
}
