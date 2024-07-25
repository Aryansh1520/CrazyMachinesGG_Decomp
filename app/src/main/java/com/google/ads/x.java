package com.google.ads;

import android.webkit.WebView;
import java.util.HashMap;

/* loaded from: classes.dex */
public class x implements n {
    @Override // com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        com.google.ads.util.b.c("Received log message: <\"string\": \"" + hashMap.get("string") + "\", \"afmaNotifyDt\": \"" + hashMap.get("afma_notify_dt") + "\">");
    }
}
