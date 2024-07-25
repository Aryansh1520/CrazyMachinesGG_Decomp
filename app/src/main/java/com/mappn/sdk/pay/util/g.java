package com.mappn.sdk.pay.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

/* loaded from: classes.dex */
final class g implements DialogInterface.OnCancelListener {
    private /* synthetic */ Context a;
    private /* synthetic */ int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(Context context, int i) {
        this.a = context;
        this.b = i;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        if (this.a instanceof Activity) {
            ((Activity) this.a).removeDialog(this.b);
        }
    }
}
