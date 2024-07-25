package com.xiaomi.gamecenter.sdk;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.xiaomi.gamecenter.sdk.IGameCenterSDK;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class i implements ServiceConnection {
    final /* synthetic */ MiCommplatform a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(MiCommplatform miCommplatform) {
        this.a = miCommplatform;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.a.sdk = IGameCenterSDK.Stub.asInterface(iBinder);
        new j(this).start();
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.a.sdk = null;
        Log.e(">>>>", "Service DisConnected");
    }
}
