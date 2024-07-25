package com.mappn.sdk.pay.chargement;

import android.view.View;
import com.mappn.sdk.pay.model.TypeFactory;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class b implements View.OnClickListener {
    private /* synthetic */ AlipayOrGFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(AlipayOrGFragment alipayOrGFragment) {
        this.a = alipayOrGFragment;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        ChargeActivity chargeActivity;
        ChargeActivity chargeActivity2;
        ChargeActivity chargeActivity3;
        ChargeActivity chargeActivity4;
        String obj = this.a.mEtInputChargeMoney.getText().toString();
        chargeActivity = this.a.b;
        if ("alipay".equals(chargeActivity.mType)) {
            chargeActivity4 = this.a.b;
            chargeActivity4.mToCancleDialog = true;
            AlipayOrGFragment.a(this.a, obj);
            return;
        }
        chargeActivity2 = this.a.b;
        if (TypeFactory.TYPE_CHARGE_NETBANK.equals(chargeActivity2.mType)) {
            AlipayOrGFragment.b(this.a, obj);
            return;
        }
        chargeActivity3 = this.a.b;
        if ("mo9".equals(chargeActivity3.mType)) {
            AlipayOrGFragment.c(this.a, obj);
        } else {
            AlipayOrGFragment.d(this.a, obj);
        }
    }
}
