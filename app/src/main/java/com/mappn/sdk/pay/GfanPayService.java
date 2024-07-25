package com.mappn.sdk.pay;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Messenger;
import com.mappn.sdk.common.utils.BaseUtils;

/* loaded from: classes.dex */
public class GfanPayService extends Service {
    public static final String EXTRA_KEY_ORDER = "com.mappn.sdk.order";
    public static final String EXTRA_KEY_TYPE = "com.mappn.sdk.type";
    public static final String EXTRA_KEY_USER = "com.mappn.sdk.user";
    public static final int TYPE_CHARGE_ERROR = 3;
    public static final int TYPE_CHARGE_SUCCESS = 2;
    public static final int TYPE_PAY_ERROR = 1;
    public static final int TYPE_PAY_SUCCESS = 0;
    private static final String a = GfanPayService.class.getSimpleName();
    private Messenger b;
    private final Messenger c = new Messenger(new g(this));
    private BroadcastReceiver d = new f(this);

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        registerReceiver(this.d, new IntentFilter(BaseUtils.getPayBroadcast(getApplicationContext())));
        return this.c.getBinder();
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        BaseUtils.D(a, "service destroy");
        this.b = null;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        unregisterReceiver(this.d);
        return super.onUnbind(intent);
    }
}
