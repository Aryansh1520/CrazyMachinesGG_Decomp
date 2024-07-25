package com.mongodb;

import java.io.IOException;
import org.bson.BSONObject;
import org.bson.io.OutputBuffer;

/* loaded from: classes.dex */
public class LazyDBEncoder implements DBEncoder {
    @Override // com.mongodb.DBEncoder
    public int writeObject(OutputBuffer buf, BSONObject o) {
        if (!(o instanceof LazyDBObject)) {
            throw new IllegalArgumentException("LazyDBEncoder can only encode BSONObject instances of type LazyDBObject");
        }
        LazyDBObject lazyDBObject = (LazyDBObject) o;
        try {
            lazyDBObject.pipe(buf);
            return lazyDBObject.getBSONSize();
        } catch (IOException e) {
            throw new MongoException("Exception serializing a LazyDBObject", e);
        }
    }
}
