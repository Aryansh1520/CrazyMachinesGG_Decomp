package com.google.ads;

import android.webkit.WebView;
import com.google.ads.AdRequest;
import com.mappn.sdk.uc.util.Constants;
import java.util.HashMap;

/* loaded from: classes.dex */
public class u implements n {
    @Override // com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        com.google.ads.util.b.e("Invalid " + hashMap.get(Constants.PUSH_TYPE) + " request error: " + hashMap.get("errors"));
        com.google.ads.internal.c j = dVar.j();
        if (j != null) {
            j.a(AdRequest.ErrorCode.INVALID_REQUEST);
        }
    }
}
