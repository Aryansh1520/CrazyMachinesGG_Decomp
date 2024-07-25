package com.xiaomi.gamecenter.sdk;

import android.os.RemoteException;
import android.util.Log;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;

/* loaded from: classes.dex */
class j extends Thread {
    final /* synthetic */ i a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(i iVar) {
        this.a = iVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Object obj;
        Object obj2;
        IGameCenterSDK iGameCenterSDK;
        MiAppInfo miAppInfo;
        int i;
        IGameCenterSDK iGameCenterSDK2;
        IServiceCallback iServiceCallback;
        try {
            iGameCenterSDK = this.a.a.sdk;
            miAppInfo = this.a.a.appInfo;
            if (iGameCenterSDK.ConnService(miAppInfo)) {
                this.a.a.connect_flag = 0;
                iGameCenterSDK2 = this.a.a.sdk;
                iServiceCallback = this.a.a.serviceCallback;
                iGameCenterSDK2.registCallback(iServiceCallback);
            } else {
                this.a.a.connect_flag = -2;
            }
            StringBuilder append = new StringBuilder().append("Service Connected ");
            i = this.a.a.connect_flag;
            Log.e(">>>>", append.append(i).toString());
        } catch (RemoteException e) {
            this.a.a.connect_flag = -2;
        }
        obj = this.a.a._Lock_;
        synchronized (obj) {
            obj2 = this.a.a._Lock_;
            obj2.notify();
        }
    }
}
