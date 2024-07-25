package com.mappn.sdk.pay.payment;

import com.mappn.sdk.pay.net.chain.Handler;

/* loaded from: classes.dex */
final class a implements Handler.OnFinishListener {
    private /* synthetic */ PaymentViews a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(PaymentViews paymentViews) {
        this.a = paymentViews;
    }

    @Override // com.mappn.sdk.pay.net.chain.Handler.OnFinishListener
    public final void onFinish() {
        this.a.a();
    }
}
