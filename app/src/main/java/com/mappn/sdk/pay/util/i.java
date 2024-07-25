package com.mappn.sdk.pay.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import com.mappn.sdk.pay.util.DialogUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class i implements DialogInterface.OnClickListener {
    private /* synthetic */ Context a;
    private /* synthetic */ int b;
    private /* synthetic */ DialogUtil.WarningDialogListener c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(Context context, int i, DialogUtil.WarningDialogListener warningDialogListener) {
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
