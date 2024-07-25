package com.mappn.sdk.uc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.mappn.sdk.uc.net.Api;

/* loaded from: classes.dex */
public class WeiboLoignActivity extends Activity {
    private WebView a;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.a = new WebView(this);
        this.a.getSettings().setSupportZoom(true);
        this.a.getSettings().setJavaScriptEnabled(true);
        this.a.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.a.addJavascriptInterface(new f(this), "weibologinCallback");
        this.a.setWebViewClient(new WebViewClient());
        this.a.loadUrl(Api.API_UC_WEIBO_LOGIN);
        addContentView(this.a, new ViewGroup.LayoutParams(-1, -1));
    }
}
