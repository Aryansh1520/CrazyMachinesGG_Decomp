package com.mappn.sdk.statitistics;

import android.app.Activity;
import android.os.Message;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class c implements Runnable {
    private /* synthetic */ Activity a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(Activity activity) {
        this.a = activity;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        try {
            String[] strArr = {"onResume", this.a.getLocalClassName()};
            GfanPayTCLog.a();
            z = GfanPayAgent.f;
            if (!z) {
                GfanPayAgent.init(this.a);
            }
            s.b().sendMessage(Message.obtain(s.b(), 1, this.a));
        } catch (Throwable th) {
        }
    }
}
