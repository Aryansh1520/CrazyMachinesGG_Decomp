package com.mappn.sdk.pay.database;

import java.util.Map;

/* loaded from: classes.dex */
public class PayVo {
    private int a;
    private Map b;

    public Map getPayParameters() {
        return this.b;
    }

    public int get_id() {
        return this.a;
    }

    public void setPayParameters(Map map) {
        this.b = map;
    }

    public void set_id(int i) {
        this.a = i;
    }
}
