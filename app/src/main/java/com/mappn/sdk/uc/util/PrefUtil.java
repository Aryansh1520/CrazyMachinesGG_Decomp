package com.mappn.sdk.uc.util;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class PrefUtil {
    public static final int USER_TYPE_WEIBO = 2;
    public static SharedPreferences sPref = null;

    public static void clear() {
        sPref = null;
    }

    public static SharedPreferences.Editor getEditor() {
        if (sPref == null) {
            return null;
        }
        return sPref.edit();
    }

    public static long getUid(Context context) {
        if (sPref == null) {
            init(context);
        }
        return sPref.getLong("pref.last.uid", -1L);
    }

    public static long getUtype(Context context) {
        if (sPref == null) {
            init(context);
        }
        return sPref.getLong("pref.type", -1L);
    }

    public static synchronized void init(Context context) {
        synchronized (PrefUtil.class) {
            if (sPref == null) {
                sPref = context.getSharedPreferences("mappn.sdk.pref", 0);
            }
        }
    }

    public static boolean isLogin(Context context) {
        if (sPref == null) {
            init(context);
        }
        return sPref.getLong("pref.uid", -1L) != -1;
    }

    public static void logout(Context context) {
        if (sPref == null) {
            init(context);
        }
        SharedPreferences.Editor edit = sPref.edit();
        edit.remove("pref.uid");
        edit.remove("pref.type");
        edit.commit();
    }

    public static void setUPW(Context context, String str) {
        if (sPref == null) {
            init(context);
        }
        SharedPreferences.Editor edit = sPref.edit();
        edit.putString("pref.upw", str);
        edit.commit();
    }

    public static void setUid(Context context, long j) {
        if (sPref == null) {
            init(context);
        }
        SharedPreferences.Editor edit = sPref.edit();
        edit.putLong("pref.uid", j);
        edit.putLong("pref.last.uid", j);
        edit.commit();
    }

    public static void setUtype(Context context, int i) {
        if (sPref == null) {
            init(context);
        }
        SharedPreferences.Editor edit = sPref.edit();
        edit.putInt("pref.type", i);
        edit.commit();
    }
}
