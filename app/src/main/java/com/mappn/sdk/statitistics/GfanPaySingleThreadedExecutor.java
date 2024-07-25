package com.mappn.sdk.statitistics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public class GfanPaySingleThreadedExecutor {
    private static ExecutorService a = Executors.newSingleThreadExecutor();

    public static void execute(Runnable runnable) {
        a.execute(runnable);
    }
}
