package com.xiaomi.gamecenter.sdk;

import android.app.Activity;
import android.content.Context;
import com.xiaomi.gamecenter.sdk.entry.CardBuyInfo;

/* loaded from: classes.dex */
class c extends Thread {
    final /* synthetic */ Activity a;
    final /* synthetic */ OnCardPayProcessListener b;
    final /* synthetic */ CardBuyInfo c;
    final /* synthetic */ MiCommplatform d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(MiCommplatform miCommplatform, Activity activity, OnCardPayProcessListener onCardPayProcessListener, CardBuyInfo cardBuyInfo) {
        this.d = miCommplatform;
        this.a = activity;
        this.b = onCardPayProcessListener;
        this.c = cardBuyInfo;
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
                this.b.finishCardPayProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE);
                return;
            }
            iGameCenterSDK = this.d.sdk;
            int miCardPay = iGameCenterSDK.miCardPay(this.c);
            this.d.mTouch = false;
            if (!this.d.check_user_changed(this.a, miCardPay)) {
                this.b.finishCardPayProcess(miCardPay);
            }
        } catch (Exception e) {
            this.d.mTouch = false;
            this.b.finishCardPayProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE);
        } finally {
            this.d.mTouch = false;
            MiCommplatform miCommplatform = this.d;
            context = this.d.ctx;
            miCommplatform.disconnect(context);
        }
    }
}
