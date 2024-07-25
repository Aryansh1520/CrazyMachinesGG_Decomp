package com.mappn.sdk.pay.chargement.alipay;

import android.os.Handler;
import android.os.Message;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class a implements Runnable {
    private /* synthetic */ String a;
    private /* synthetic */ MobileSecurePayHelper b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(MobileSecurePayHelper mobileSecurePayHelper, String str) {
        this.b = mobileSecurePayHelper;
        this.a = str;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Handler handler;
        String checkNewUpdate = this.b.checkNewUpdate(MobileSecurePayHelper.getApkInfo(this.b.a, this.a));
        if (checkNewUpdate != null) {
            this.b.retrieveApkFromNet(this.b.a, checkNewUpdate, this.a);
        }
        Message message = new Message();
        message.what = 2;
        message.obj = this.a;
        handler = this.b.c;
        handler.sendMessage(message);
    }
}
