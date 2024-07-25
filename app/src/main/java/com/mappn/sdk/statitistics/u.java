package com.mappn.sdk.statitistics;

import java.lang.reflect.Method;

/* loaded from: classes.dex */
final class u {
    private static Class b = null;
    public static Method a = null;

    public static boolean a() {
        try {
            b = Class.forName("android.os.SystemProperties");
            a = b.getDeclaredMethod("get", String.class);
            return true;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }
    }
}
