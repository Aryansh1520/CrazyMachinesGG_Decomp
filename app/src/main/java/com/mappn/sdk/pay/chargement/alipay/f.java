package com.mappn.sdk.pay.chargement.alipay;

import android.app.Activity;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Message;
import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IRemoteServiceCallback;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class f implements Runnable {
    private /* synthetic */ String a;
    private /* synthetic */ int b;
    private /* synthetic */ Handler c;
    private /* synthetic */ MobileSecurePayer d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(MobileSecurePayer mobileSecurePayer, String str, int i, Handler handler) {
        this.d = mobileSecurePayer;
        this.a = str;
        this.b = i;
        this.c = handler;
    }

    @Override // java.lang.Runnable
    public final void run() {
        IRemoteServiceCallback iRemoteServiceCallback;
        IRemoteServiceCallback iRemoteServiceCallback2;
        ServiceConnection serviceConnection;
        try {
            synchronized (this.d.a) {
                if (this.d.b == null) {
                    this.d.a.wait();
                }
            }
            IAlixPay iAlixPay = this.d.b;
            iRemoteServiceCallback = this.d.f;
            iAlixPay.registerCallback(iRemoteServiceCallback);
            String Pay = this.d.b.Pay(this.a);
            this.d.c = false;
            IAlixPay iAlixPay2 = this.d.b;
            iRemoteServiceCallback2 = this.d.f;
            iAlixPay2.unregisterCallback(iRemoteServiceCallback2);
            Activity activity = this.d.d;
            serviceConnection = this.d.e;
            activity.unbindService(serviceConnection);
            Message message = new Message();
            message.what = this.b;
            message.obj = Pay;
            this.c.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            Message message2 = new Message();
            message2.what = this.b;
            message2.obj = e.toString();
            this.c.sendMessage(message2);
        }
    }
}
