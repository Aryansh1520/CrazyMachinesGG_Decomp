package com.mappn.sdk.pay.payment;

import android.content.Intent;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.GfanPayService;
import com.mappn.sdk.pay.ServiceConnector;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.User;

/* loaded from: classes.dex */
final class g implements GfanUCCallback {
    private /* synthetic */ PaymentsActivity a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(PaymentsActivity paymentsActivity) {
        this.a = paymentsActivity;
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onError(int i) {
        String str;
        PaymentViews paymentViews;
        String str2;
        str = PaymentsActivity.a;
        BaseUtils.D(str, "initUser is onError.");
        this.a.finish();
        paymentViews = this.a.d;
        paymentViews.onDestroy();
        if (ServiceConnector.getInstance(this.a.getApplicationContext()).getIsConnected()) {
            this.a.sendBroadcast(new Intent(BaseUtils.getPayBroadcast(this.a.getApplicationContext())).putExtra(GfanPayService.EXTRA_KEY_TYPE, 1));
        } else {
            str2 = PaymentsActivity.a;
            BaseUtils.D(str2, "connection disconnect");
        }
        PrefUtil.setLoginFlag(this.a.getApplicationContext(), false);
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onSuccess(User user, int i) {
        PaymentViews paymentViews;
        PrefUtil.setLoginFlag(this.a.getApplicationContext(), true);
        this.a.mPaymentInfo.setUser(user);
        paymentViews = this.a.d;
        paymentViews.queryBalance();
    }
}
