package com.google.ads;

import android.text.TextUtils;
import android.webkit.WebView;
import com.google.ads.internal.AdWebView;
import com.google.ads.l;
import com.google.ads.util.AdUtil;
import com.google.ads.util.i;
import java.util.HashMap;

/* loaded from: classes.dex */
public class w implements n {
    @Override // com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        m h = dVar.h();
        l.a a = h.a.a().a.a();
        a(hashMap, "min_hwa_banner", a.a);
        a(hashMap, "min_hwa_overlay", a.b);
        c(hashMap, "mraid_banner_path", a.c);
        c(hashMap, "mraid_expanded_banner_path", a.d);
        c(hashMap, "mraid_interstitial_path", a.e);
        b(hashMap, "ac_max_size", a.f);
        b(hashMap, "ac_padding", a.g);
        b(hashMap, "ac_total_quota", a.h);
        b(hashMap, "db_total_quota", a.i);
        b(hashMap, "db_quota_per_origin", a.j);
        b(hashMap, "db_quota_step_size", a.k);
        AdWebView k = dVar.k();
        if (AdUtil.a >= 11) {
            com.google.ads.util.g.a(k.getSettings(), h);
            com.google.ads.util.g.a(webView.getSettings(), h);
        }
        if (!h.i.a().a()) {
            boolean e = k.e();
            boolean z = AdUtil.a < a.a.a().intValue();
            if (!z && e) {
                com.google.ads.util.b.a("Re-enabling hardware acceleration for a banner after reading constants.");
                k.c();
            } else if (z && !e) {
                com.google.ads.util.b.a("Disabling hardware acceleration for a banner after reading constants.");
                k.b();
            }
        }
        a.l.a(true);
    }

    private void a(HashMap<String, String> hashMap, String str, i.c<Integer> cVar) {
        try {
            String str2 = hashMap.get(str);
            if (!TextUtils.isEmpty(str2)) {
                cVar.a(Integer.valueOf(str2));
            }
        } catch (NumberFormatException e) {
            com.google.ads.util.b.a("Could not parse \"" + str + "\" constant.");
        }
    }

    private void b(HashMap<String, String> hashMap, String str, i.c<Long> cVar) {
        try {
            String str2 = hashMap.get(str);
            if (!TextUtils.isEmpty(str2)) {
                cVar.a(new Long(str2));
            }
        } catch (NumberFormatException e) {
            com.google.ads.util.b.a("Could not parse \"" + str + "\" constant.");
        }
    }

    private void c(HashMap<String, String> hashMap, String str, i.c<String> cVar) {
        String str2 = hashMap.get(str);
        if (!TextUtils.isEmpty(str2)) {
            cVar.a(str2);
        }
    }
}
