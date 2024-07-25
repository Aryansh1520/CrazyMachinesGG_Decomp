package org.bson;

import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public interface BSONCallback {
    Object arrayDone();

    void arrayStart();

    void arrayStart(String str);

    BSONCallback createBSONCallback();

    Object get();

    void gotBinary(String str, byte b, byte[] bArr);

    @Deprecated
    void gotBinaryArray(String str, byte[] bArr);

    void gotBoolean(String str, boolean z);

    void gotCode(String str, String str2);

    void gotCodeWScope(String str, String str2, Object obj);

    void gotDBRef(String str, String str2, ObjectId objectId);

    void gotDate(String str, long j);

    void gotDouble(String str, double d);

    void gotInt(String str, int i);

    void gotLong(String str, long j);

    void gotMaxKey(String str);

    void gotMinKey(String str);

    void gotNull(String str);

    void gotObjectId(String str, ObjectId objectId);

    void gotRegex(String str, String str2, String str3);

    void gotString(String str, String str2);

    void gotSymbol(String str, String str2);

    void gotTimestamp(String str, int i, int i2);

    void gotUUID(String str, long j, long j2);

    void gotUndefined(String str);

    Object objectDone();

    void objectStart();

    void objectStart(String str);

    void objectStart(boolean z);

    void reset();
}
