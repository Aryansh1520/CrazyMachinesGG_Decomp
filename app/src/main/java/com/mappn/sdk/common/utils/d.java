package com.mappn.sdk.common.utils;

import android.os.Handler;
import android.os.Message;
import java.util.TimerTask;

/* loaded from: classes.dex */
final class d extends TimerTask {
    private /* synthetic */ GfanProgressDialog a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(GfanProgressDialog gfanProgressDialog) {
        this.a = gfanProgressDialog;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public final void run() {
        Handler handler;
        Handler handler2;
        handler = this.a.g;
        Message obtainMessage = handler.obtainMessage();
        handler2 = this.a.g;
        handler2.sendMessage(obtainMessage);
    }
}
