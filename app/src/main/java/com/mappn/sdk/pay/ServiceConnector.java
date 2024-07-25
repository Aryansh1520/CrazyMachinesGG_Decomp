package com.mappn.sdk.pay;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.model.Order;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ServiceConnector {
    private static String a = "GFanServiceConnector";
    private static ServiceConnector b;
    private boolean e;
    private boolean f;
    private Context g;
    private GfanPayCallback h;
    private GfanChargeCallback i;
    private e j;
    private Messenger c = null;
    private Messenger d = null;
    private ServiceConnection k = new h(this);

    private ServiceConnector(Context context) {
        this.g = context;
    }

    public static synchronized ServiceConnector getInstance(Context context) {
        ServiceConnector serviceConnector;
        synchronized (ServiceConnector.class) {
            if (b == null) {
                b = new ServiceConnector(context);
            }
            serviceConnector = b;
        }
        return serviceConnector;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(GfanChargeCallback gfanChargeCallback) {
        this.i = gfanChargeCallback;
        Message obtain = Message.obtain();
        obtain.replyTo = this.d;
        obtain.what = 2;
        try {
            this.c.send(obtain);
        } catch (RemoteException e) {
            Log.e(a, "get exception when send message", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(Order order, GfanPayCallback gfanPayCallback) {
        this.h = gfanPayCallback;
        Message obtain = Message.obtain();
        obtain.replyTo = this.d;
        obtain.what = 1;
        obtain.obj = order;
        try {
            this.c.send(obtain);
        } catch (RemoteException e) {
            Log.e(a, "get exception when send message", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(HashMap hashMap, GfanChargeCallback gfanChargeCallback) {
        this.i = gfanChargeCallback;
        Message obtain = Message.obtain();
        obtain.replyTo = this.d;
        obtain.what = 3;
        obtain.obj = hashMap;
        try {
            this.c.send(obtain);
        } catch (RemoteException e) {
            Log.e(a, "get exception when send message", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean a() {
        return this.e;
    }

    public void doBindService(e eVar) {
        if (this.e) {
            return;
        }
        this.j = eVar;
        this.g.bindService(new Intent(this.g, (Class<?>) GfanPayService.class), this.k, 1);
        this.e = true;
    }

    public void doUnbindService() {
        if (this.e) {
            this.g.unbindService(this.k);
            this.e = false;
        }
    }

    public boolean getIsConnected() {
        return this.f;
    }

    public Messenger getMessenger() {
        BaseUtils.D(a, this.c.toString());
        return this.c;
    }
}
