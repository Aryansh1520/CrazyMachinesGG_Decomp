package com.mappn.sdk.pay.chargement;

import com.mappn.sdk.common.utils.GfanAlertDialog;

/* loaded from: classes.dex */
final class j implements GfanAlertDialog.GfanAlertDialogListener {
    private /* synthetic */ ChargeActivity a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(ChargeActivity chargeActivity) {
        this.a = chargeActivity;
    }

    @Override // com.mappn.sdk.common.utils.GfanAlertDialog.GfanAlertDialogListener
    public final void onButtonClick() {
        this.a.b();
        this.a.finish();
    }
}
