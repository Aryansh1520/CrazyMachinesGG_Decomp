package com.mappn.sdk.pay.chargement;

import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.User;

/* loaded from: classes.dex */
final class m implements GfanUCCallback {
    private /* synthetic */ int a;
    private /* synthetic */ ChargeTypeListFragment b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(ChargeTypeListFragment chargeTypeListFragment, int i) {
        this.b = chargeTypeListFragment;
        this.a = i;
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onError(int i) {
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onSuccess(User user, int i) {
        ChargeActivity chargeActivity;
        ChargeActivity chargeActivity2;
        chargeActivity = this.b.b;
        PrefUtil.setLoginFlag(chargeActivity.getApplicationContext(), true);
        chargeActivity2 = this.b.b;
        chargeActivity2.mPaymentInfo.setUser(user);
        this.b.a(this.a);
    }
}
