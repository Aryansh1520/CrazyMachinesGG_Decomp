package com.mappn.sdk.pay.chargement.alipay;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.alipay.android.app.IAlixPay;

/* loaded from: classes.dex */
final class e implements ServiceConnection {
    private /* synthetic */ MobileSecurePayer a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(MobileSecurePayer mobileSecurePayer) {
        this.a = mobileSecurePayer;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this.a.a) {
            this.a.b = IAlixPay.Stub.asInterface(iBinder);
            this.a.a.notify();
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        this.a.b = null;
    }
}
