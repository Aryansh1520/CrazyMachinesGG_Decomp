package com.mappn.sdk.pay.payment;

import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.util.DBUtil;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.User;

/* loaded from: classes.dex */
final class d implements GfanUCCallback {
    private /* synthetic */ PaymentViews a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(PaymentViews paymentViews) {
        this.a = paymentViews;
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onError(int i) {
        BaseUtils.D("PaymentViews", "PaymentsViews onError.");
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onSuccess(User user, int i) {
        PaymentsActivity paymentsActivity;
        BaseUtils.D(DBUtil.TABLE_PAY, "PaymentViews onsucess !");
        paymentsActivity = this.a.b;
        paymentsActivity.mPaymentInfo.setUser(user);
        PaymentViews.a(this.a, 0);
        this.a.queryBalance();
    }
}
