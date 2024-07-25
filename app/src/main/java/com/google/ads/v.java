package com.google.ads;

import android.webkit.WebView;
import com.mappn.sdk.common.utils.BaseConstants;
import com.mappn.sdk.uc.util.Constants;
import java.util.HashMap;

/* loaded from: classes.dex */
public class v implements n {
    @Override // com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        String str = hashMap.get(Constants.PUSH_URL);
        String str2 = hashMap.get(Constants.PUSH_TYPE);
        String str3 = hashMap.get("afma_notify_dt");
        boolean equals = BaseConstants.DEFAULT_UC_CNO.equals(hashMap.get("drt_include"));
        com.google.ads.util.b.c("Received ad url: <url: \"" + str + "\" type: \"" + str2 + "\" afmaNotifyDt: \"" + str3 + "\">");
        com.google.ads.internal.c j = dVar.j();
        if (j != null) {
            j.c(equals);
            j.d(str);
        }
    }
}
