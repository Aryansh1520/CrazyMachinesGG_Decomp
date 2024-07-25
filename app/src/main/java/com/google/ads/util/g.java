package com.google.ads.util;

import android.R;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.ads.AdActivity;
import com.google.ads.internal.AdWebView;
import com.google.ads.l;
import com.google.ads.m;
import com.google.ads.n;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

@TargetApi(11)
/* loaded from: classes.dex */
public class g {

    /* loaded from: classes.dex */
    public static class b extends com.google.ads.internal.i {
        public b(com.google.ads.internal.d dVar, Map<String, n> map, boolean z, boolean z2) {
            super(dVar, map, z, z2);
        }

        @Override // android.webkit.WebViewClient
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            try {
                if ("mraid.js".equalsIgnoreCase(new File(url).getName())) {
                    com.google.ads.internal.c j = this.a.j();
                    if (j != null) {
                        j.b(true);
                    } else {
                        this.a.a(true);
                    }
                    l.a a = this.a.h().a.a().a.a();
                    if (!this.a.h().b()) {
                        if (this.b) {
                            String a2 = a.d.a();
                            com.google.ads.util.b.a("shouldInterceptRequest(" + a2 + ")");
                            return a(a2, view.getContext());
                        }
                        String a3 = a.c.a();
                        com.google.ads.util.b.a("shouldInterceptRequest(" + a3 + ")");
                        return a(a3, view.getContext());
                    }
                    String a4 = a.e.a();
                    com.google.ads.util.b.a("shouldInterceptRequest(" + a4 + ")");
                    return a(a4, view.getContext());
                }
            } catch (IOException e) {
                com.google.ads.util.b.d("IOException fetching MRAID JS.", e);
            } catch (Throwable th) {
                com.google.ads.util.b.b("An unknown error occurred fetching MRAID JS.", th);
            }
            return super.shouldInterceptRequest(view, url);
        }

        private static WebResourceResponse a(String str, Context context) throws IOException {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            try {
                AdUtil.a(httpURLConnection, context.getApplicationContext());
                httpURLConnection.connect();
                return new WebResourceResponse("application/javascript", "UTF-8", new ByteArrayInputStream(AdUtil.a(new InputStreamReader(httpURLConnection.getInputStream())).getBytes("UTF-8")));
            } finally {
                httpURLConnection.disconnect();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class a extends WebChromeClient {
        private final m a;

        public a(m mVar) {
            this.a = mVar;
        }

        @Override // android.webkit.WebChromeClient
        public void onCloseWindow(WebView webView) {
            if (webView instanceof AdWebView) {
                ((AdWebView) webView).a();
            }
        }

        @Override // android.webkit.WebChromeClient
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            String str = "JS: " + consoleMessage.message() + " (" + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber() + ")";
            switch (AnonymousClass1.a[consoleMessage.messageLevel().ordinal()]) {
                case 1:
                    com.google.ads.util.b.b(str);
                    break;
                case 2:
                    com.google.ads.util.b.e(str);
                    break;
                case 3:
                case 4:
                    com.google.ads.util.b.c(str);
                    break;
                case 5:
                    com.google.ads.util.b.a(str);
                    break;
            }
            return super.onConsoleMessage(consoleMessage);
        }

        @Override // android.webkit.WebChromeClient
        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            l.a a = this.a.a.a().a.a();
            long longValue = a.i.a().longValue() - totalUsedQuota;
            if (longValue <= 0) {
                quotaUpdater.updateQuota(currentQuota);
                return;
            }
            if (currentQuota == 0) {
                if (estimatedSize > longValue || estimatedSize > a.j.a().longValue()) {
                    estimatedSize = 0;
                }
            } else if (estimatedSize == 0) {
                estimatedSize = Math.min(Math.min(a.k.a().longValue(), longValue) + currentQuota, a.j.a().longValue());
            } else {
                if (estimatedSize <= Math.min(a.j.a().longValue() - currentQuota, longValue)) {
                    currentQuota += estimatedSize;
                }
                estimatedSize = currentQuota;
            }
            quotaUpdater.updateQuota(estimatedSize);
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return a(view, url, message, null, result, null, false);
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return a(view, url, message, null, result, null, false);
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return a(view, url, message, null, result, null, false);
        }

        @Override // android.webkit.WebChromeClient
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return a(view, url, message, defaultValue, null, result, true);
        }

        public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            l.a a = this.a.a.a().a.a();
            long longValue = a.h.a().longValue() - totalUsedQuota;
            long longValue2 = a.g.a().longValue() + spaceNeeded;
            if (longValue < longValue2) {
                quotaUpdater.updateQuota(0L);
            } else {
                quotaUpdater.updateQuota(longValue2);
            }
        }

        @Override // android.webkit.WebChromeClient
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            callback.onCustomViewHidden();
        }

        private static boolean a(WebView webView, String str, String str2, String str3, JsResult jsResult, JsPromptResult jsPromptResult, boolean z) {
            AdActivity d;
            if ((webView instanceof AdWebView) && (d = ((AdWebView) webView).d()) != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(d);
                builder.setTitle(str);
                if (z) {
                    a(builder, d, str2, str3, jsPromptResult);
                } else {
                    a(builder, str2, jsResult);
                }
                return true;
            }
            return false;
        }

        private static void a(AlertDialog.Builder builder, String str, final JsResult jsResult) {
            builder.setMessage(str).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.google.ads.util.g.a.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    jsResult.confirm();
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.google.ads.util.g.a.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    jsResult.cancel();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.google.ads.util.g.a.1
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialog) {
                    jsResult.cancel();
                }
            }).create().show();
        }

        private static void a(AlertDialog.Builder builder, Context context, String str, String str2, final JsPromptResult jsPromptResult) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(1);
            TextView textView = new TextView(context);
            textView.setText(str);
            final EditText editText = new EditText(context);
            editText.setText(str2);
            linearLayout.addView(textView);
            linearLayout.addView(editText);
            builder.setView(linearLayout).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { // from class: com.google.ads.util.g.a.6
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    jsPromptResult.confirm(editText.getText().toString());
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.google.ads.util.g.a.5
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    jsPromptResult.cancel();
                }
            }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.google.ads.util.g.a.4
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialog) {
                    jsPromptResult.cancel();
                }
            }).create().show();
        }
    }

    /* renamed from: com.google.ads.util.g$1, reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] a = new int[ConsoleMessage.MessageLevel.values().length];

        static {
            try {
                a[ConsoleMessage.MessageLevel.ERROR.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[ConsoleMessage.MessageLevel.WARNING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[ConsoleMessage.MessageLevel.LOG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[ConsoleMessage.MessageLevel.TIP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                a[ConsoleMessage.MessageLevel.DEBUG.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public static void a(WebSettings webSettings, m mVar) {
        Context a2 = mVar.d.a();
        l.a a3 = mVar.a.a().a.a();
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(a3.f.a().longValue());
        webSettings.setAppCachePath(new File(a2.getCacheDir(), "admob").getAbsolutePath());
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(a2.getDatabasePath("admob").getAbsolutePath());
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
    }

    public static void a(View view) {
        view.setLayerType(1, null);
    }

    public static void b(View view) {
        view.setLayerType(0, null);
    }

    public static void a(Window window) {
        window.setFlags(16777216, 16777216);
    }
}
