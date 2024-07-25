package com.mappn.sdk.pay.chargement.alipay;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IRemoteServiceCallback;

/* loaded from: classes.dex */
public class MobileSecurePayer {
    Integer a = 0;
    IAlixPay b = null;
    boolean c = false;
    Activity d = null;
    private ServiceConnection e = new e(this);
    private IRemoteServiceCallback f = new g(this);

    public boolean pay(String str, Handler handler, int i, Activity activity) {
        if (this.c) {
            return false;
        }
        this.c = true;
        this.d = activity;
        if (this.b == null) {
            this.d.bindService(new Intent(IAlixPay.class.getName()), this.e, 1);
        }
        new Thread(new f(this, str, i, handler)).start();
        return true;
    }
}
