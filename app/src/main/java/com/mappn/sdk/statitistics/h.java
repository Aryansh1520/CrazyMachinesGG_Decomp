package com.mappn.sdk.statitistics;

import java.lang.Thread;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class h implements Thread.UncaughtExceptionHandler {
    private static Thread.UncaughtExceptionHandler a = Thread.getDefaultUncaughtExceptionHandler();
    private static h b = null;

    h() {
    }

    public static h a() {
        if (b == null) {
            b = new h();
        }
        return b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Thread.UncaughtExceptionHandler b() {
        return a;
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final void uncaughtException(Thread thread, Throwable th) {
        GfanPayAgent.a(th);
        if (a != null) {
            a.uncaughtException(thread, th);
        }
    }
}
