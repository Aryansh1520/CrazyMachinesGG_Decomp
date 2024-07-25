package com.amazon.inapp.purchasing;

/* loaded from: classes.dex */
interface LogHandler {
    void error(String str, String str2);

    boolean isErrorOn();

    boolean isTestOn();

    boolean isTraceOn();

    void test(String str, String str2);

    void trace(String str, String str2);
}
