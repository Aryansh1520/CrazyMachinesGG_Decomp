package com.google.ads;

import android.net.Uri;
import android.webkit.WebView;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class q extends t {
    @Override // com.google.ads.t, com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        Uri parse;
        String host;
        String str = hashMap.get(AdActivity.URL_PARAM);
        if (str == null) {
            com.google.ads.util.b.e("Could not get URL from click gmsg.");
            return;
        }
        com.google.ads.internal.g m = dVar.m();
        if (m != null && (host = (parse = Uri.parse(str)).getHost()) != null && host.toLowerCase(Locale.US).endsWith(".admob.com")) {
            String str2 = null;
            String path = parse.getPath();
            if (path != null) {
                String[] split = path.split("/");
                if (split.length >= 4) {
                    str2 = split[2] + "/" + split[3];
                }
            }
            m.b(str2);
        }
        super.a(dVar, hashMap, webView);
    }
}
