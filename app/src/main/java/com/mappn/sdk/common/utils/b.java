package com.mappn.sdk.common.utils;

import android.app.AlertDialog;
import android.view.View;
import com.mappn.sdk.common.utils.GfanAlertDialog;

/* loaded from: classes.dex */
final class b implements View.OnClickListener {
    private /* synthetic */ AlertDialog a;
    private /* synthetic */ GfanAlertDialog.GfanAlertDialogListener b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(AlertDialog alertDialog, GfanAlertDialog.GfanAlertDialogListener gfanAlertDialogListener) {
        this.a = alertDialog;
        this.b = gfanAlertDialogListener;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.a.dismiss();
        if (this.b != null) {
            this.b.onButtonClick();
        }
    }
}
