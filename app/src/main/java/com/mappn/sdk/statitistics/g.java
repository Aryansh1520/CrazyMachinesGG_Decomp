package com.mappn.sdk.statitistics;

import android.content.Context;

/* loaded from: classes.dex */
final class g implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ Throwable b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(Context context, Throwable th) {
        this.a = context;
        this.b = th;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        try {
            z = GfanPayAgent.f;
            if (!z) {
                GfanPayAgent.init(this.a);
            }
            GfanPayAgent.a(this.b);
        } catch (Throwable th) {
        }
    }
}
