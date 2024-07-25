package com.mappn.sdk.pay.payment;

import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.GfanChargeCallback;
import com.mappn.sdk.uc.User;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class f implements GfanChargeCallback {
    private /* synthetic */ PaymentViews a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(PaymentViews paymentViews) {
        this.a = paymentViews;
    }

    @Override // com.mappn.sdk.pay.GfanChargeCallback
    public final void onError(User user) {
        PaymentsActivity paymentsActivity;
        paymentsActivity = this.a.b;
        paymentsActivity.mPaymentInfo.setUser(user);
        BaseUtils.D("PaymentViews", "chargeByType onerror ");
    }

    @Override // com.mappn.sdk.pay.GfanChargeCallback
    public final void onSuccess(User user) {
        PaymentsActivity paymentsActivity;
        paymentsActivity = this.a.b;
        paymentsActivity.mPaymentInfo.setUser(user);
        this.a.queryBalance();
    }
}
