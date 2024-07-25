package com.google.ads;

import android.webkit.WebView;
import com.google.ads.internal.AdWebView;
import java.util.HashMap;

/* loaded from: classes.dex */
public class r implements n {
    @Override // com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        if (webView instanceof AdWebView) {
            ((AdWebView) webView).a();
        } else {
            com.google.ads.util.b.b("Trying to close WebView that isn't an AdWebView");
        }
    }
}
