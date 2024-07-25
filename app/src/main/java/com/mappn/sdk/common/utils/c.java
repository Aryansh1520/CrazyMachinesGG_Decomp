package com.mappn.sdk.common.utils;

import android.os.Handler;
import android.os.Message;
import com.mappn.sdk.common.utils.GfanProgressDialog;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class c extends Handler {
    private /* synthetic */ GfanProgressDialog a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(GfanProgressDialog gfanProgressDialog) {
        this.a = gfanProgressDialog;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        GfanProgressDialog.OnTimeOutListener onTimeOutListener;
        GfanProgressDialog gfanProgressDialog;
        GfanProgressDialog.OnTimeOutListener onTimeOutListener2;
        GfanProgressDialog gfanProgressDialog2;
        onTimeOutListener = this.a.b;
        if (onTimeOutListener == null) {
            try {
                gfanProgressDialog = GfanProgressDialog.d;
                gfanProgressDialog.dismiss();
            } catch (Exception e) {
            }
        } else {
            onTimeOutListener2 = this.a.b;
            onTimeOutListener2.onTimeOut(this.a);
            try {
                gfanProgressDialog2 = GfanProgressDialog.d;
                gfanProgressDialog2.dismiss();
            } catch (Exception e2) {
            }
        }
    }
}
