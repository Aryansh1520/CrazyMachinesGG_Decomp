package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.AdWebView;
import java.util.HashMap;

/* loaded from: classes.dex */
public class s implements n {
    private static final com.google.ads.internal.a a = com.google.ads.internal.a.a.b();

    @Override // com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        String str = hashMap.get("js");
        if (str == null) {
            com.google.ads.util.b.b("Could not get the JS to evaluate.");
            return;
        }
        if (!(webView instanceof AdWebView)) {
            com.google.ads.util.b.b("Trying to evaluate JS in a WebView that isn't an AdWebView");
            return;
        }
        AdActivity d = ((AdWebView) webView).d();
        if (d == null) {
            com.google.ads.util.b.b("Could not get the AdActivity from the AdWebView.");
            return;
        }
        AdWebView openingAdWebView = d.getOpeningAdWebView();
        if (openingAdWebView == null) {
            com.google.ads.util.b.b("Could not get the opening WebView.");
        } else {
            a.a(openingAdWebView, str);
        }
    }
}
