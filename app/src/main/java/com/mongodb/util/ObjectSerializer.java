package com.mongodb.util;

/* loaded from: classes.dex */
public interface ObjectSerializer {
    String serialize(Object obj);

    void serialize(Object obj, StringBuilder sb);
}
