package com.amazon.inapp.purchasing;

/* loaded from: classes.dex */
class Logger {
    Logger() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void error(String str, String str2) {
        if (isErrorOn()) {
            ImplementationFactory.getLogHandler().error(str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isErrorOn() {
        return ImplementationFactory.getLogHandler().isErrorOn();
    }

    static boolean isTestOn() {
        return ImplementationFactory.getLogHandler().isTestOn();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isTraceOn() {
        return ImplementationFactory.getLogHandler().isTraceOn();
    }

    static void test(String str, String str2) {
        if (isTestOn()) {
            ImplementationFactory.getLogHandler().test(str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void trace(String str, String str2) {
        if (isTraceOn()) {
            ImplementationFactory.getLogHandler().trace(str, str2);
        }
    }
}
