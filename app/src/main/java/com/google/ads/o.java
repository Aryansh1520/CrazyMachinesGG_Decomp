package com.google.ads;

import android.webkit.WebView;
import com.mappn.sdk.uc.util.Constants;
import java.util.HashMap;

/* loaded from: classes.dex */
public class o implements n {
    @Override // com.google.ads.n
    public void a(com.google.ads.internal.d dVar, HashMap<String, String> hashMap, WebView webView) {
        String str = hashMap.get("name");
        if (str == null) {
            com.google.ads.util.b.b("Error: App event with no name parameter.");
        } else {
            dVar.a(str, hashMap.get(Constants.KEY_PRODUCT_INFO));
        }
    }
}
