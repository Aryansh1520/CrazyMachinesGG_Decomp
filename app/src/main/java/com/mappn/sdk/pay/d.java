package com.mappn.sdk.pay;

import java.util.HashMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class d implements e {
    private /* synthetic */ HashMap a;
    private /* synthetic */ GfanChargeCallback b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(GfanPay gfanPay, HashMap hashMap, GfanChargeCallback gfanChargeCallback) {
        this.a = hashMap;
        this.b = gfanChargeCallback;
    }

    @Override // com.mappn.sdk.pay.e
    public final void a() {
        ServiceConnector serviceConnector;
        serviceConnector = GfanPay.c;
        serviceConnector.a(this.a, this.b);
    }
}
