package com.mappn.sdk.pay.model;

import com.mappn.sdk.uc.User;
import java.io.Serializable;

/* loaded from: classes.dex */
public class PaymentInfo implements Serializable {
    public static final String PAYTYPE_ALL = "all";
    public static final String PAYTYPE_OVERAGE = "overage";
    public static final String PAYTYPE_SMS = "sms";
    private static PaymentInfo g;
    private Order a;
    private User b;
    private int c;
    private String d;
    private String e;
    private String f;

    private PaymentInfo() {
    }

    public static synchronized void clearPaymentInfo() {
        synchronized (PaymentInfo.class) {
            if (g != null) {
                g = null;
            }
        }
    }

    public static synchronized PaymentInfo getInstance() {
        PaymentInfo paymentInfo;
        synchronized (PaymentInfo.class) {
            if (g == null) {
                g = new PaymentInfo();
            }
            paymentInfo = g;
        }
        return paymentInfo;
    }

    public String getAppkey() {
        return this.e;
    }

    public String getCpID() {
        return this.f;
    }

    public int getGfanBalance() {
        return this.c;
    }

    public Order getOrder() {
        return this.a;
    }

    public String getPaymentType() {
        return this.d;
    }

    public User getUser() {
        return this.b;
    }

    public void setAppkey(String str) {
        this.e = str;
    }

    public void setCpID(String str) {
        this.f = str;
    }

    public void setGfanBalance(int i) {
        this.c = i;
    }

    public void setOrder(Order order) {
        this.a = order;
    }

    public void setPaymentType(String str) {
        this.d = str;
    }

    public void setUser(User user) {
        this.b = user;
    }
}
