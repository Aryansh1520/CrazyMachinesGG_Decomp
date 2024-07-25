package com.mappn.sdk.pay.payment;

import com.mappn.sdk.common.utils.GfanAlertDialog;
import com.mappn.sdk.uc.GfanUCenter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class i implements GfanAlertDialog.GfanAlertDialogListener {
    final /* synthetic */ PaymentsActivity a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(PaymentsActivity paymentsActivity) {
        this.a = paymentsActivity;
    }

    @Override // com.mappn.sdk.common.utils.GfanAlertDialog.GfanAlertDialogListener
    public final void onButtonClick() {
        PaymentViews paymentViews;
        PaymentViews paymentViews2;
        paymentViews = this.a.d;
        paymentViews.onPaySuccess();
        GfanUCenter.modfiy(this.a, new j(this));
        paymentViews2 = this.a.d;
        paymentViews2.onDestroy();
        this.a.finish();
    }
}
