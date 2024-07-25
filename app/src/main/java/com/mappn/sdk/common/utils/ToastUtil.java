package com.mappn.sdk.common.utils;

import android.content.Context;
import android.widget.Toast;

/* loaded from: classes.dex */
public class ToastUtil {
    public static void showLong(Context context, int i) {
        Toast.makeText(context, i, 1).show();
    }

    public static void showLong(Context context, String str) {
        Toast.makeText(context, str, 1).show();
    }
}
