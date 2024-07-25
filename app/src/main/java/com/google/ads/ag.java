package com.google.ads;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.accessibility.AccessibilityEventCompat;

/* loaded from: classes.dex */
public class ag {
    public static boolean a(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.google.android.apps.plus", "com.google.android.apps.circles.platform.PlusOneActivity"));
        return a(intent, context);
    }

    public static boolean a(Intent intent, Context context) {
        return context.getPackageManager().queryIntentActivities(intent, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED).size() > 0;
    }
}
