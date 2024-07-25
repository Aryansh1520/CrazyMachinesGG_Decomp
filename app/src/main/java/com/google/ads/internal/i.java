package com.google.ads.internal;

import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.ads.AdActivity;
import com.google.ads.n;
import com.google.ads.util.AdUtil;
import com.google.ads.util.g;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class i extends WebViewClient {
    private static final a c = a.a.b();
    protected d a;
    private Map<String, n> d;
    private boolean e;
    private boolean f;
    protected boolean b = false;
    private boolean g = false;
    private boolean h = false;

    /* JADX INFO: Access modifiers changed from: protected */
    public i(d dVar, Map<String, n> map, boolean z, boolean z2) {
        this.a = dVar;
        this.d = map;
        this.e = z;
        this.f = z2;
    }

    public static i a(d dVar, Map<String, n> map, boolean z, boolean z2) {
        return AdUtil.a >= 11 ? new g.b(dVar, map, z, z2) : new i(dVar, map, z, z2);
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        String str;
        com.google.ads.util.b.a("shouldOverrideUrlLoading(\"" + url + "\")");
        Uri parse = Uri.parse(url);
        HashMap<String, String> b = AdUtil.b(parse);
        if (b != null && (str = b.get("ai")) != null) {
            this.a.m().a(str);
        }
        if (c.a(parse)) {
            c.a(this.a, this.d, parse, webView);
            return true;
        }
        if (this.f) {
            if (AdUtil.a(parse)) {
                return super.shouldOverrideUrlLoading(webView, url);
            }
            HashMap hashMap = new HashMap();
            hashMap.put(AdActivity.URL_PARAM, url);
            AdActivity.launchAdActivity(this.a, new e("intent", hashMap));
            return true;
        }
        if (this.e) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put(AdActivity.URL_PARAM, parse.toString());
            AdActivity.launchAdActivity(this.a, new e("intent", hashMap2));
            return true;
        }
        com.google.ads.util.b.e("URL is not a GMSG and can't handle URL: " + url);
        return true;
    }

    @Override // android.webkit.WebViewClient
    public void onPageFinished(WebView view, String url) {
        if (this.g) {
            c j = this.a.j();
            if (j != null) {
                j.c();
            } else {
                com.google.ads.util.b.a("adLoader was null while trying to setFinishedLoadingHtml().");
            }
            this.g = false;
        }
        if (this.h) {
            c.a(view);
            this.h = false;
        }
    }

    public void a(boolean z) {
        this.b = z;
    }

    public void b(boolean z) {
        this.f = z;
    }

    public void c(boolean z) {
        this.g = z;
    }

    public void d(boolean z) {
        this.h = z;
    }
}
