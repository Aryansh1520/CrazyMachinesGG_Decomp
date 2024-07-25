package com.mappn.sdk.uc;

import java.io.Serializable;

/* loaded from: classes.dex */
public class User implements Serializable {
    private String a;
    private long b;
    private String c;

    public String getToken() {
        return this.c;
    }

    public long getUid() {
        return this.b;
    }

    public String getUserName() {
        return this.a;
    }

    public void setToken(String str) {
        this.c = str;
    }

    public void setUid(long j) {
        this.b = j;
    }

    public void setUserName(String str) {
        this.a = str;
    }
}
