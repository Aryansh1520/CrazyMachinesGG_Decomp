package com.mappn.sdk.pay.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import com.mappn.sdk.pay.util.DialogUtil;

/* loaded from: classes.dex */
final class b implements DialogInterface.OnClickListener {
    private /* synthetic */ Context a;
    private /* synthetic */ int b;
    private /* synthetic */ DialogUtil.WarningDialogListener c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(Context context, int i, DialogUtil.WarningDialogListener warningDialogListener) {
        this.a = context;
        this.b = i;
        this.c = warningDialogListener;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        if (this.a instanceof Activity) {
            ((Activity) this.a).removeDialog(this.b);
        }
        if (this.c != null) {
            this.c.onWarningDialogOK(this.b);
        }
    }
}
