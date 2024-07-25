package com.google.ads.util;

import android.annotation.TargetApi;
import android.view.View;
import android.webkit.WebChromeClient;
import com.google.ads.m;
import com.google.ads.util.g;

@TargetApi(14)
/* loaded from: classes.dex */
public class h {

    /* loaded from: classes.dex */
    public static class a extends g.a {
        public a(m mVar) {
            super(mVar);
        }

        @Override // android.webkit.WebChromeClient
        public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback callback) {
            callback.onCustomViewHidden();
        }
    }
}
