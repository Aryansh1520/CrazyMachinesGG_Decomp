package com.mongodb;

import java.io.IOException;
import java.io.InputStream;
import org.bson.BSONDecoder;

/* loaded from: classes.dex */
public interface DBDecoder extends BSONDecoder {
    DBObject decode(InputStream inputStream, DBCollection dBCollection) throws IOException;

    DBObject decode(byte[] bArr, DBCollection dBCollection);

    DBCallback getDBCallback(DBCollection dBCollection);
}
