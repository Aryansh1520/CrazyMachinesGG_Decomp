package com.mappn.sdk.pay.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

/* loaded from: classes.dex */
final class d implements DialogInterface.OnDismissListener {
    private /* synthetic */ Context a;
    private /* synthetic */ int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(Context context, int i) {
        this.a = context;
        this.b = i;
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        if (this.a instanceof Activity) {
            ((Activity) this.a).removeDialog(this.b);
        }
    }
}
