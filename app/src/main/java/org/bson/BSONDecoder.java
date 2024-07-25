package org.bson;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public interface BSONDecoder {
    int decode(InputStream inputStream, BSONCallback bSONCallback) throws IOException;

    int decode(byte[] bArr, BSONCallback bSONCallback);

    BSONObject readObject(InputStream inputStream) throws IOException;

    BSONObject readObject(byte[] bArr);
}
