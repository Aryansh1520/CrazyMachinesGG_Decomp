package com.mappn.sdk.pay.chargement;

import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.util.Constants;

/* loaded from: classes.dex */
final class c implements Runnable {
    private /* synthetic */ AlipayOrGFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(AlipayOrGFragment alipayOrGFragment) {
        this.a = alipayOrGFragment;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ChargeActivity chargeActivity;
        ChargeActivity chargeActivity2;
        try {
            Thread.sleep(Constants.CHARGE_QUERY_RESULT_TIME);
        } catch (InterruptedException e) {
        }
        chargeActivity = this.a.b;
        AlipayOrGFragment alipayOrGFragment = this.a;
        chargeActivity2 = this.a.b;
        Api.queryAliPayResult(chargeActivity, alipayOrGFragment, chargeActivity2.mPaymentInfo.getOrder().getOrderID());
    }
}
