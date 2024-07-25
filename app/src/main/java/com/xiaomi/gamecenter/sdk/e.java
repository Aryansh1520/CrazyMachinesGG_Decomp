package com.xiaomi.gamecenter.sdk;

import android.app.Activity;
import android.content.Context;
import com.xiaomi.gamecenter.sdk.entry.LoginResult;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class e extends Thread {
    final /* synthetic */ Activity a;
    final /* synthetic */ OnLoginProcessListener b;
    final /* synthetic */ MiCommplatform c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(MiCommplatform miCommplatform, Activity activity, OnLoginProcessListener onLoginProcessListener) {
        this.c = miCommplatform;
        this.a = activity;
        this.b = onLoginProcessListener;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Context context;
        int check_and_connect;
        IGameCenterSDK iGameCenterSDK;
        MiAppInfo miAppInfo;
        try {
            check_and_connect = this.c.check_and_connect(this.a);
            if (check_and_connect != 0) {
                this.c.mTouch = false;
                this.b.finishLoginProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGIN_FAIL, null);
                return;
            }
            iGameCenterSDK = this.c.sdk;
            LoginResult miLogin = iGameCenterSDK.miLogin();
            this.c.mTouch = false;
            if (miLogin != null) {
                miAppInfo = this.c.appInfo;
                miAppInfo.setAccount(miLogin.getAccount());
                this.b.finishLoginProcess(miLogin.getErrcode(), miLogin.getAccount());
            } else {
                this.b.finishLoginProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGIN_FAIL, null);
            }
        } catch (Exception e) {
            this.c.mTouch = false;
            this.b.finishLoginProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGIN_FAIL, null);
            e.printStackTrace();
        } finally {
            this.c.mTouch = false;
            MiCommplatform miCommplatform = this.c;
            context = this.c.ctx;
            miCommplatform.disconnect(context);
        }
    }
}
