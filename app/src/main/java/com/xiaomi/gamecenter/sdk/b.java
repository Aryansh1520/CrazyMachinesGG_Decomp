package com.xiaomi.gamecenter.sdk;

import android.app.Activity;
import android.content.Context;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOnline;

/* loaded from: classes.dex */
class b extends Thread {
    final /* synthetic */ Activity a;
    final /* synthetic */ OnPayProcessListener b;
    final /* synthetic */ MiBuyInfoOnline c;
    final /* synthetic */ MiCommplatform d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(MiCommplatform miCommplatform, Activity activity, OnPayProcessListener onPayProcessListener, MiBuyInfoOnline miBuyInfoOnline) {
        this.d = miCommplatform;
        this.a = activity;
        this.b = onPayProcessListener;
        this.c = miBuyInfoOnline;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Context context;
        Context context2;
        int check_and_connect;
        IGameCenterSDK iGameCenterSDK;
        Context context3;
        Context context4;
        try {
            try {
                check_and_connect = this.d.check_and_connect(this.a);
                if (check_and_connect != 0) {
                    this.d.mTouch = false;
                    this.b.finishPayProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE);
                    this.d.mTouch = false;
                    MiCommplatform miCommplatform = this.d;
                    context4 = this.d.ctx;
                    miCommplatform.disconnect(context4);
                    return;
                }
                iGameCenterSDK = this.d.sdk;
                int miUniPayOnline = iGameCenterSDK.miUniPayOnline(this.c);
                this.d.mTouch = false;
                if (!this.d.check_user_changed(this.a, miUniPayOnline)) {
                    this.b.finishPayProcess(miUniPayOnline);
                }
                this.d.mTouch = false;
                MiCommplatform miCommplatform2 = this.d;
                context3 = this.d.ctx;
                miCommplatform2.disconnect(context3);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    this.d.mTouch = false;
                    this.b.finishPayProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                this.d.mTouch = false;
                MiCommplatform miCommplatform3 = this.d;
                context = this.d.ctx;
                miCommplatform3.disconnect(context);
            }
        } catch (Throwable th) {
            this.d.mTouch = false;
            MiCommplatform miCommplatform4 = this.d;
            context2 = this.d.ctx;
            miCommplatform4.disconnect(context2);
            throw th;
        }
    }
}
