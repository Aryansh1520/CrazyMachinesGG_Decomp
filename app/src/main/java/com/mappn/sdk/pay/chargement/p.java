package com.mappn.sdk.pay.chargement;

import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.util.Constants;

/* loaded from: classes.dex */
final class p implements Runnable {
    private /* synthetic */ PhoneCardFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(PhoneCardFragment phoneCardFragment) {
        this.a = phoneCardFragment;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ChargeActivity chargeActivity;
        ChargeActivity chargeActivity2;
        try {
            Thread.sleep(Constants.CHARGE_QUERY_RESULT_TIME);
        } catch (InterruptedException e) {
        }
        chargeActivity = this.a.d;
        PhoneCardFragment phoneCardFragment = this.a;
        chargeActivity2 = this.a.d;
        Api.queryPhoneCardChargeResult(chargeActivity, phoneCardFragment, chargeActivity2.mPaymentInfo.getOrder().getOrderID());
    }
}
