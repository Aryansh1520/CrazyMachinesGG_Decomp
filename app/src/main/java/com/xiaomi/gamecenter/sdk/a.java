package com.xiaomi.gamecenter.sdk;

import android.app.Activity;
import android.content.Context;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOffline;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class a extends Thread {
    final /* synthetic */ Activity a;
    final /* synthetic */ OnPayProcessListener b;
    final /* synthetic */ MiBuyInfoOffline c;
    final /* synthetic */ MiCommplatform d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(MiCommplatform miCommplatform, Activity activity, OnPayProcessListener onPayProcessListener, MiBuyInfoOffline miBuyInfoOffline) {
        this.d = miCommplatform;
        this.a = activity;
        this.b = onPayProcessListener;
        this.c = miBuyInfoOffline;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Context context;
        int check_and_connect;
        IGameCenterSDK iGameCenterSDK;
        try {
            check_and_connect = this.d.check_and_connect(this.a);
            if (check_and_connect != 0) {
                this.d.mTouch = false;
                this.b.finishPayProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE);
                return;
            }
            iGameCenterSDK = this.d.sdk;
            int miUniPayOffline = iGameCenterSDK.miUniPayOffline(this.c);
            this.d.mTouch = false;
            if (!this.d.check_user_changed(this.a, miUniPayOffline)) {
                this.b.finishPayProcess(miUniPayOffline);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.d.mTouch = false;
            this.b.finishPayProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE);
        } finally {
            this.d.mTouch = false;
            MiCommplatform miCommplatform = this.d;
            context = this.d.ctx;
            miCommplatform.disconnect(context);
        }
    }
}
