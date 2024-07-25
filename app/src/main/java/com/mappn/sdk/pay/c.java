package com.mappn.sdk.pay;

/* loaded from: classes.dex */
final class c implements e {
    private /* synthetic */ GfanChargeCallback a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(GfanPay gfanPay, GfanChargeCallback gfanChargeCallback) {
        this.a = gfanChargeCallback;
    }

    @Override // com.mappn.sdk.pay.e
    public final void a() {
        ServiceConnector serviceConnector;
        serviceConnector = GfanPay.c;
        serviceConnector.a(this.a);
    }
}
