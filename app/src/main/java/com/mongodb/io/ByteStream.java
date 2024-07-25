package com.mongodb.io;

import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public interface ByteStream {
    boolean hasMore();

    int write(ByteBuffer byteBuffer);
}
