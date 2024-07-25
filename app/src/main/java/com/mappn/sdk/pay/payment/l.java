package com.mappn.sdk.pay.payment;

import android.app.Activity;
import com.mappn.sdk.pay.net.chain.Handler;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class l implements Handler.OnFinishListener {
    private /* synthetic */ int a;
    private /* synthetic */ SmsPaymentFragment b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(SmsPaymentFragment smsPaymentFragment, int i) {
        this.b = smsPaymentFragment;
        this.a = i;
    }

    @Override // com.mappn.sdk.pay.net.chain.Handler.OnFinishListener
    public final void onFinish() {
        Activity activity;
        SmsPaymentFragment smsPaymentFragment = this.b;
        SmsPaymentFragment.a("buildView", 5, "mIsSynced--同步信息完成.");
        SmsPaymentFragment.a(this.b, true);
        activity = this.b.mActivity;
        ((PaymentsActivity) activity).removeDialog(19);
        this.b.a(this.a);
    }
}
