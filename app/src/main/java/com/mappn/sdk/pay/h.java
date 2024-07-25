package com.mappn.sdk.pay;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.mappn.sdk.common.utils.BaseUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class h implements ServiceConnection {
    private /* synthetic */ ServiceConnector a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(ServiceConnector serviceConnector) {
        this.a = serviceConnector;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        String str;
        Messenger messenger;
        String str2;
        Messenger messenger2;
        str = ServiceConnector.a;
        BaseUtils.D(str, "onServiceConnected");
        this.a.f = true;
        this.a.c = new Messenger(iBinder);
        this.a.d = new Messenger(new i(this.a));
        Message obtain = Message.obtain();
        messenger = this.a.d;
        obtain.replyTo = messenger;
        obtain.what = 0;
        try {
            messenger2 = this.a.c;
            messenger2.send(obtain);
        } catch (RemoteException e) {
            str2 = ServiceConnector.a;
            Log.e(str2, "get exception when send message", e);
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        String str;
        str = ServiceConnector.a;
        BaseUtils.D(str, "onServiceDisconnected");
        this.a.f = false;
        this.a.c = null;
    }
}
