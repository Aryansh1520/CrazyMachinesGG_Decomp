package com.mappn.sdk.pay.model;

import java.io.Serializable;

/* loaded from: classes.dex */
public class Order implements Serializable {
    private String a;
    private String b;
    private int c;
    private String d;
    private String e;

    public Order(String str, String str2, int i) {
        this.a = str;
        this.b = str2;
        this.c = i;
    }

    public Order(String str, String str2, int i, String str3) {
        this.a = str;
        this.b = str2;
        this.c = i;
        this.d = str3;
    }

    public int getMoney() {
        return this.c;
    }

    public String getNumber() {
        return this.e;
    }

    public String getOrderID() {
        return this.d;
    }

    public String getPayDesc() {
        return this.b;
    }

    public String getPayName() {
        return this.a;
    }

    public void setMoney(int i) {
        this.c = i;
    }

    public void setNumber(String str) {
        this.e = str;
    }

    public void setOrderID(String str) {
        this.d = str;
    }

    public void setPayDesc(String str) {
        this.b = str;
    }

    public void setPayName(String str) {
        this.a = str;
    }
}
