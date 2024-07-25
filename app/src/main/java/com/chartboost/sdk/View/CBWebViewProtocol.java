package com.chartboost.sdk.View;

import android.content.Context;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.chartboost.sdk.Model.CBImpression;
import com.chartboost.sdk.View.CBViewProtocol;
import com.google.ads.AdActivity;
import java.net.URI;
import java.net.URLDecoder;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes.dex */
public class CBWebViewProtocol extends CBViewProtocol {
    public static final String TAG = "Chartboost WebView";
    private String html;

    /* loaded from: classes.dex */
    public class CBWebView extends CBViewProtocol.CBViewBase {
        public WebView webView;

        public CBWebView(Context context, String html) {
            super(context);
            setFocusable(false);
            this.webView = new CustomWebView(context);
            this.webView.setWebViewClient(new CustomWebViewClient(CBWebViewProtocol.this));
            addView(this.webView);
            this.webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
        }

        @Override // com.chartboost.sdk.View.CBViewProtocol.CBViewBase
        protected void layoutSubviews(int w, int h) {
        }
    }

    public CBWebViewProtocol(CBImpression impression) {
        super(impression);
        this.html = null;
    }

    @Override // com.chartboost.sdk.View.CBViewProtocol
    protected CBViewProtocol.CBViewBase createViewObject(Context context) {
        return new CBWebView(context, this.html);
    }

    @Override // com.chartboost.sdk.View.CBViewProtocol
    public void prepareWithResponse(JSONObject response) {
        String html = response.optString(AdActivity.HTML_PARAM);
        if (html != null) {
            this.html = html;
            setReadyToDisplay();
        }
    }

    /* loaded from: classes.dex */
    private class CustomWebView extends WebView {
        public CustomWebView(Context context) {
            super(context);
            setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            setBackgroundColor(0);
            getSettings().setJavaScriptEnabled(true);
        }

        @Override // android.webkit.WebView, android.view.View, android.view.KeyEvent.Callback
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if ((keyCode == 4 || keyCode == 3) && CBWebViewProtocol.this.closeCallback != null) {
                CBWebViewProtocol.this.closeCallback.execute();
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    /* loaded from: classes.dex */
    private class CustomWebViewClient extends WebViewClient {
        private CBWebViewProtocol delegate;

        public CustomWebViewClient(CBWebViewProtocol _delegate) {
            this.delegate = _delegate;
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (this.delegate == null || this.delegate.displayCallback == null) {
                return;
            }
            this.delegate.displayCallback.execute();
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (this.delegate.failCallback != null) {
                this.delegate.failCallback.execute();
            }
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            try {
                URI uri = new URI(url);
                String protocol = uri.getScheme();
                if (protocol.equals("chartboost")) {
                    String[] items = url.split("/");
                    Integer urlCount = Integer.valueOf(items.length);
                    if (urlCount.intValue() < 3) {
                        if (this.delegate.closeCallback != null) {
                            this.delegate.closeCallback.execute();
                        }
                        return false;
                    }
                    String function = items[2];
                    if (function.equals("close")) {
                        if (this.delegate.closeCallback != null) {
                            this.delegate.closeCallback.execute();
                        }
                    } else if (function.equals("link")) {
                        if (urlCount.intValue() < 4) {
                            if (this.delegate.closeCallback != null) {
                                this.delegate.closeCallback.execute();
                            }
                            return false;
                        }
                        JSONObject moreData = null;
                        String decodedUrl = null;
                        try {
                            decodedUrl = URLDecoder.decode(items[3], "UTF-8");
                            if (urlCount.intValue() < 4) {
                                JSONTokener tokener = new JSONTokener(URLDecoder.decode(items[4], "UTF-8"));
                                moreData = new JSONObject(tokener);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (this.delegate.clickCallback != null) {
                            this.delegate.clickCallback.execute(decodedUrl, moreData);
                        }
                    }
                }
                return true;
            } catch (Exception e2) {
                if (this.delegate.closeCallback != null) {
                    this.delegate.closeCallback.execute();
                }
                return false;
            }
        }
    }
}
