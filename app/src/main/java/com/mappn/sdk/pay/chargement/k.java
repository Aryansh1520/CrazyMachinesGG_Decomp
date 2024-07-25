package com.mappn.sdk.pay.chargement;

import com.mappn.sdk.common.utils.GfanAlertDialog;
import com.mappn.sdk.uc.GfanUCenter;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class k implements GfanAlertDialog.GfanAlertDialogListener {
    final /* synthetic */ ChargeActivity a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(ChargeActivity chargeActivity) {
        this.a = chargeActivity;
    }

    @Override // com.mappn.sdk.common.utils.GfanAlertDialog.GfanAlertDialogListener
    public final void onButtonClick() {
        this.a.b();
        GfanUCenter.modfiy(this.a, new l(this));
        this.a.finish();
    }
}
