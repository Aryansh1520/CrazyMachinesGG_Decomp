package com.xiaomi.gamecenter.sdk;

import android.content.Context;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;

/* loaded from: classes.dex */
class g extends Thread {
    final /* synthetic */ OnLoginProcessListener a;
    final /* synthetic */ MiCommplatform b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(MiCommplatform miCommplatform, OnLoginProcessListener onLoginProcessListener) {
        this.b = miCommplatform;
        this.a = onLoginProcessListener;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Context context;
        Context context2;
        int check_and_connect;
        IGameCenterSDK iGameCenterSDK;
        MiAppInfo miAppInfo;
        Context context3;
        try {
            MiCommplatform miCommplatform = this.b;
            context2 = this.b.ctx;
            check_and_connect = miCommplatform.check_and_connect(context2);
            if (check_and_connect != 0) {
                this.b.mTouch = false;
                this.a.finishLoginProcess(-103, null);
            } else {
                iGameCenterSDK = this.b.sdk;
                iGameCenterSDK.miLogout();
                miAppInfo = this.b.appInfo;
                miAppInfo.setAccount(null);
                this.b.mTouch = false;
                this.a.finishLoginProcess(-104, null);
                this.b.mTouch = false;
                MiCommplatform miCommplatform2 = this.b;
                context3 = this.b.ctx;
                miCommplatform2.disconnect(context3);
            }
        } catch (Exception e) {
            this.b.mTouch = false;
            this.a.finishLoginProcess(-103, null);
            e.printStackTrace();
        } finally {
            this.b.mTouch = false;
            MiCommplatform miCommplatform3 = this.b;
            context = this.b.ctx;
            miCommplatform3.disconnect(context);
        }
    }
}
