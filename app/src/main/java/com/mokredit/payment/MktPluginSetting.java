package com.mokredit.payment;

import java.io.Serializable;

/* loaded from: classes.dex */
public class MktPluginSetting implements Serializable {
    private String a;

    public MktPluginSetting(String str) {
        this.a = str;
    }

    public String getUrl() {
        return this.a;
    }

    public void setUrl(String str) {
        this.a = str;
    }
}
