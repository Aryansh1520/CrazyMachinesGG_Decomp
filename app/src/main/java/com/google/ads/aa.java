package com.google.ads;

import android.app.Activity;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.webkit.WebView;
import com.google.ads.internal.AdVideoView;
import com.google.ads.internal.AdWebView;
import com.google.ads.util.AdUtil;
import com.mappn.sdk.uc.util.Constants;
import java.util.HashMap;

/* loaded from: classes.dex */
public class aa implements n {
    private static final com.google.ads.internal.a a = com.google.ads.internal.a.a.b();

    protected int a(HashMap<String, String> hashMap, String str, int i, DisplayMetrics displayMetrics) {
        String str2 = hashMap.get(str);
        if (str2 != null) {
            try {
                return (int) TypedValue.applyDimension(1, Integer.parseInt(str2), displayMetrics);
            } catch (NumberFormatException e) {
                com.google.ads.util.b.a("Could not parse \"" + str + "\" in a video gmsg: " + str2);
                return i;
            }
        }
        return i;
    }

    @Override // com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        String str = hashMap.get("action");
        if (str == null) {
            com.google.ads.util.b.a("No \"action\" parameter in a video gmsg.");
            return;
        }
        if (webView instanceof AdWebView) {
            AdWebView adWebView = (AdWebView) webView;
            AdActivity d = adWebView.d();
            if (d == null) {
                com.google.ads.util.b.a("Could not get adActivity for a video gmsg.");
                return;
            }
            boolean equals = str.equals("new");
            boolean equals2 = str.equals("position");
            if (equals || equals2) {
                DisplayMetrics a2 = AdUtil.a((Activity) d);
                int a3 = a(hashMap, "x", 0, a2);
                int a4 = a(hashMap, "y", 0, a2);
                int a5 = a(hashMap, "w", -1, a2);
                int a6 = a(hashMap, "h", -1, a2);
                if (equals && d.getAdVideoView() == null) {
                    d.newAdVideoView(a3, a4, a5, a6);
                    return;
                } else {
                    d.moveAdVideoView(a3, a4, a5, a6);
                    return;
                }
            }
            AdVideoView adVideoView = d.getAdVideoView();
            if (adVideoView == null) {
                a.a(adWebView, "onVideoEvent", "{'event': 'error', 'what': 'no_video_view'}");
                return;
            }
            if (str.equals("click")) {
                DisplayMetrics a7 = AdUtil.a((Activity) d);
                int a8 = a(hashMap, "x", 0, a7);
                int a9 = a(hashMap, "y", 0, a7);
                long uptimeMillis = SystemClock.uptimeMillis();
                adVideoView.a(MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, a8, a9, 0));
                return;
            }
            if (str.equals("controls")) {
                String str2 = hashMap.get("enabled");
                if (str2 == null) {
                    com.google.ads.util.b.a("No \"enabled\" parameter in a controls video gmsg.");
                    return;
                } else if (str2.equals("true")) {
                    adVideoView.setMediaControllerEnabled(true);
                    return;
                } else {
                    adVideoView.setMediaControllerEnabled(false);
                    return;
                }
            }
            if (str.equals("currentTime")) {
                String str3 = hashMap.get(Constants.PUSH_TIME);
                if (str3 == null) {
                    com.google.ads.util.b.a("No \"time\" parameter in a currentTime video gmsg.");
                    return;
                }
                try {
                    adVideoView.a((int) (Float.parseFloat(str3) * 1000.0f));
                    return;
                } catch (NumberFormatException e) {
                    com.google.ads.util.b.a("Could not parse \"time\" parameter: " + str3);
                    return;
                }
            }
            if (str.equals("hide")) {
                adVideoView.setVisibility(4);
                return;
            }
            if (str.equals("load")) {
                adVideoView.b();
                return;
            }
            if (str.equals("pause")) {
                adVideoView.c();
                return;
            }
            if (str.equals("play")) {
                adVideoView.d();
                return;
            }
            if (str.equals("show")) {
                adVideoView.setVisibility(0);
                return;
            } else if (str.equals("src")) {
                adVideoView.setSrc(hashMap.get("src"));
                return;
            } else {
                com.google.ads.util.b.a("Unknown video action: " + str);
                return;
            }
        }
        com.google.ads.util.b.a("Could not get adWebView for a video gmsg.");
    }
}
