package com.mappn.sdk.uc;

/* loaded from: classes.dex */
public class UserVo {
    private int a;
    private String b;
    private String c;
    private long d;
    private String e;

    public int getId() {
        return this.a;
    }

    public String getPassword() {
        return this.c;
    }

    public String getToken() {
        return this.e;
    }

    public long getUid() {
        return this.d;
    }

    public String getUserName() {
        return this.b;
    }

    public void setId(int i) {
        this.a = i;
    }

    public void setPassword(String str) {
        this.c = str;
    }

    public void setToken(String str) {
        this.e = str;
    }

    public void setUid(long j) {
        this.d = j;
    }

    public void setUserName(String str) {
        this.b = str;
    }
}
