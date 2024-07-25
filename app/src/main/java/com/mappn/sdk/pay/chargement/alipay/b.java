package com.mappn.sdk.pay.chargement.alipay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

/* loaded from: classes.dex */
final class b implements DialogInterface.OnClickListener {
    private /* synthetic */ String a;
    private /* synthetic */ Context b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(MobileSecurePayHelper mobileSecurePayHelper, String str, Context context) {
        this.a = str;
        this.b = context;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        BaseHelper.chmod("777", this.a);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(268435456);
        intent.setDataAndType(Uri.parse("file://" + this.a), "application/vnd.android.package-archive");
        this.b.startActivity(intent);
    }
}
