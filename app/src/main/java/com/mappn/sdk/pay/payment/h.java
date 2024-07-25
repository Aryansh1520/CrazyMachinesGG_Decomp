package com.mappn.sdk.pay.payment;

import com.mappn.sdk.common.utils.GfanAlertDialog;

/* loaded from: classes.dex */
final class h implements GfanAlertDialog.GfanAlertDialogListener {
    private /* synthetic */ PaymentsActivity a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(PaymentsActivity paymentsActivity) {
        this.a = paymentsActivity;
    }

    @Override // com.mappn.sdk.common.utils.GfanAlertDialog.GfanAlertDialogListener
    public final void onButtonClick() {
        PaymentViews paymentViews;
        PaymentViews paymentViews2;
        paymentViews = this.a.d;
        paymentViews.onPaySuccess();
        paymentViews2 = this.a.d;
        paymentViews2.onDestroy();
        this.a.finish();
    }
}
