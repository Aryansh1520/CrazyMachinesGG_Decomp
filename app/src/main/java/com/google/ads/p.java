package com.google.ads;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.webkit.WebView;
import com.mappn.sdk.pay.util.Constants;
import java.util.HashMap;

/* loaded from: classes.dex */
public class p implements n {
    private static final com.google.ads.internal.a a = com.google.ads.internal.a.a.b();

    @Override // com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        String str = hashMap.get("urls");
        if (str == null) {
            com.google.ads.util.b.e("Could not get the urls param from canOpenURLs gmsg.");
            return;
        }
        String[] split = str.split(Constants.TERM);
        HashMap hashMap2 = new HashMap();
        PackageManager packageManager = webView.getContext().getPackageManager();
        for (String str2 : split) {
            String[] split2 = str2.split(";", 2);
            hashMap2.put(str2, Boolean.valueOf(packageManager.resolveActivity(new Intent(split2.length >= 2 ? split2[1] : "android.intent.action.VIEW", Uri.parse(split2[0])), AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) != null));
        }
        a.a(webView, hashMap2);
    }
}
