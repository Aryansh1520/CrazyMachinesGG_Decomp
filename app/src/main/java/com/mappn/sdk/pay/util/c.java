package com.mappn.sdk.pay.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import com.mappn.sdk.pay.util.DialogUtil;

/* loaded from: classes.dex */
final class c implements DialogInterface.OnCancelListener {
    private /* synthetic */ Context a;
    private /* synthetic */ int b;
    private /* synthetic */ DialogUtil.ProgressDialogListener c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(Context context, int i, DialogUtil.ProgressDialogListener progressDialogListener) {
        this.a = context;
        this.b = i;
        this.c = progressDialogListener;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        if (this.a instanceof Activity) {
            ((Activity) this.a).removeDialog(this.b);
        }
        if (this.c != null) {
            this.c.onProgressDialogCancel(this.b);
        }
    }
}
