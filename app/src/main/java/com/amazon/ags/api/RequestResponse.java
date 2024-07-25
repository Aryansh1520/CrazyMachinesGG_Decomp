package com.amazon.ags.api;

/* loaded from: classes.dex */
public interface RequestResponse {
    ErrorCode getError();

    Object[] getUserData();

    boolean isError();

    void setUserData(Object[] objArr);

    String toString();
}
