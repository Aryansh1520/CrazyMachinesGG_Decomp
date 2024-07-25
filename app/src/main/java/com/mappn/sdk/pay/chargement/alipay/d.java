package com.mappn.sdk.pay.chargement.alipay;

import android.os.Handler;
import android.os.Message;

/* loaded from: classes.dex */
final class d extends Handler {
    private /* synthetic */ MobileSecurePayHelper a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(MobileSecurePayHelper mobileSecurePayHelper) {
        this.a = mobileSecurePayHelper;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        try {
            switch (message.what) {
                case 2:
                    this.a.a();
                    this.a.showInstallConfirmDialog(this.a.a, (String) message.obj);
                    break;
            }
            super.handleMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
