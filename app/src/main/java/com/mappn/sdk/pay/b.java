package com.mappn.sdk.pay;

import com.mappn.sdk.pay.model.Order;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class b implements e {
    private /* synthetic */ Order a;
    private /* synthetic */ GfanPayCallback b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(GfanPay gfanPay, Order order, GfanPayCallback gfanPayCallback) {
        this.a = order;
        this.b = gfanPayCallback;
    }

    @Override // com.mappn.sdk.pay.e
    public final void a() {
        ServiceConnector serviceConnector;
        serviceConnector = GfanPay.c;
        serviceConnector.a(this.a, this.b);
    }
}
