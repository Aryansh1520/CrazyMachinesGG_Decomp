package com.chartboost.sdk.NativeViews;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.chartboost.sdk.Libraries.CBUtility;
import com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol;
import com.google.ads.AdActivity;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CBNativeMoreAppsWebViewCell extends CBNativeMoreAppsCell implements CBNativeMoreAppsViewProtocol.MoreAppsCellInterface {
    private WebView webView;

    public CBNativeMoreAppsWebViewCell(Context context) {
        super(context);
        this.webView = new WebView(context);
        addView(this.webView, new LinearLayout.LayoutParams(-1, -1));
        this.webView.setBackgroundColor(0);
        this.webView.setWebViewClient(new WebViewClient() { // from class: com.chartboost.sdk.NativeViews.CBNativeMoreAppsWebViewCell.1
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) {
                    return false;
                }
                if (url.contains("chartboost") && url.contains("click") && CBNativeMoreAppsWebViewCell.this.clickListener != null) {
                    CBNativeMoreAppsWebViewCell.this.clickListener.onClick(CBNativeMoreAppsWebViewCell.this);
                }
                return true;
            }
        });
    }

    @Override // com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.MoreAppsCellInterface
    public void prepareWithCellMeta(JSONObject cellMeta, int position) {
        String html = cellMeta.optString(AdActivity.HTML_PARAM);
        if (html != null) {
            try {
                this.webView.loadDataWithBaseURL("file:///android_res/", html, "text/html", "UTF-8", null);
            } catch (Exception e) {
            }
        }
    }

    @Override // com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.MoreAppsCellInterface
    public int height() {
        return CBUtility.dpToPixels(100, getContext());
    }

    @Override // com.chartboost.sdk.NativeViews.CBNativeMoreAppsCell
    protected void layoutSubviews() {
    }
}
