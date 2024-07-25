package com.mappn.sdk.statitistics;

import android.content.Context;

/* loaded from: classes.dex */
final class q implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ p b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(p pVar, Context context) {
        this.b = pVar;
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        try {
            new String[1][0] = "onReceive network changed. ";
            GfanPayTCLog.a();
            this.b.a.a(this.a);
        } catch (Throwable th) {
        }
    }
}
