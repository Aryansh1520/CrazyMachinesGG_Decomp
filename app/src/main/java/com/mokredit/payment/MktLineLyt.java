package com.mokredit.payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.mappn.sdk.pay.util.Constants;
import com.mongodb.util.TimeConstants;
import java.io.File;
import java.util.UUID;

/* loaded from: classes.dex */
public class MktLineLyt extends LinearLayout {
    private static WebView a;
    private static Intent d;
    private ProgressDialog b;
    private MktPluginSetting c;
    private boolean e;
    private String f;
    private String g;
    private Handler h;

    /* loaded from: classes.dex */
    class HttpThread implements Runnable {
        private String url;

        public HttpThread(String str) {
            this.url = str;
        }

        private String getHttpResponse(String str) {
            return HttpConnectionUtil.getResponse(str);
        }

        @Override // java.lang.Runnable
        public void run() {
            Message obtainMessage = MktLineLyt.this.h.obtainMessage();
            try {
                Log.i("ccccccccccccccccccc", "vvvvvvvvvvvvvvvvvv");
                obtainMessage.what = 1;
                obtainMessage.obj = new String[]{this.url, getHttpResponse(this.url)};
                MktLineLyt.this.h.sendMessage(obtainMessage);
            } catch (Exception e) {
                if (e.getMessage().startsWith("Redirect:")) {
                    obtainMessage.what = 2;
                    obtainMessage.obj = StringUtils.substringAfter(e.getMessage(), ":");
                } else {
                    obtainMessage.what = -1;
                }
                MktLineLyt.this.h.sendMessage(obtainMessage);
            }
        }
    }

    /* loaded from: classes.dex */
    class MokreditJS {
        private MktPayment parentActivity;

        public MokreditJS() {
        }

        public MokreditJS(MktPayment mktPayment) {
            this.parentActivity = mktPayment;
        }

        public void close() {
            this.parentActivity.finish();
        }

        public String getAi() {
            SharedPreferences sharedPreferences = MktLineLyt.this.getContext().getSharedPreferences("mokredit", 0);
            String string = sharedPreferences.getString("ai", null);
            if (string == null || !string.startsWith(Constants.TEXT_CHARGE_MO9)) {
                String string2 = Settings.Secure.getString(MktLineLyt.this.getContext().getContentResolver(), "android_id");
                string = !"9774d56d682e549c".equals(string2) ? Constants.TEXT_CHARGE_MO9 + Md5Encrypt.encrypt(string2) : Constants.TEXT_CHARGE_MO9 + Md5Encrypt.encrypt(UUID.randomUUID().toString());
                sharedPreferences.edit().putString("ai", string).commit();
            }
            return string;
        }

        public String getVersion() {
            return String.valueOf(Build.MODEL) + "::" + Build.VERSION.SDK + "::" + Build.VERSION.RELEASE;
        }

        public void setFailed() {
            this.parentActivity.setResult(11);
        }

        public void setSuccess() {
            this.parentActivity.setResult(10);
        }
    }

    /* JADX WARN: Type inference failed for: r0v41, types: [com.mokredit.payment.MktLineLyt$3] */
    public MktLineLyt(Context context) {
        super(context);
        this.e = false;
        this.h = new Handler() { // from class: com.mokredit.payment.MktLineLyt.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case -1:
                    case 0:
                    default:
                        return;
                    case 1:
                        String[] strArr = (String[]) message.obj;
                        MktLineLyt.a.loadDataWithBaseURL(MktLineLyt.this.g, strArr[1], "text/html", "utf-8", strArr[0]);
                        return;
                    case 2:
                        MktLineLyt.a.loadUrl((String) message.obj);
                        return;
                }
            }
        };
        d = ((Activity) context).getIntent();
        this.c = (MktPluginSetting) d.getSerializableExtra("mokredit_android");
        setBackgroundColor(-1);
        getBackground().setAlpha(50);
        setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        setOrientation(1);
        a = new WebView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.topMargin = 20;
        layoutParams.bottomMargin = 20;
        layoutParams.rightMargin = 20;
        layoutParams.leftMargin = 20;
        layoutParams.gravity = 17;
        a.setLayoutParams(layoutParams);
        addView(a);
        File cacheDir = context.getCacheDir() != null ? context.getCacheDir() : context.getExternalCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            this.f = String.valueOf(cacheDir.getPath()) + "/mokredit";
            this.g = "file://" + this.f + "/www/";
            File file = new File(this.f);
            if (!file.exists()) {
                Log.i("Cache dir does not exist", "try creating");
                file.mkdirs();
            }
            if (file.exists()) {
                Log.i("Cache dir exists", "mark cache enabled");
                this.e = true;
            }
        }
        if (this.e) {
            try {
                File file2 = new File(String.valueOf(this.f) + "/www");
                if (!file2.exists() || System.currentTimeMillis() - file2.lastModified() > TimeConstants.MS_WEEK) {
                    ZipUtils.unzipToFolder(getClass().getResourceAsStream("jquery.zip"), this.f);
                    file2.setLastModified(System.currentTimeMillis());
                }
            } catch (Exception e) {
                this.e = false;
                e.printStackTrace();
            }
        }
        a.setVerticalScrollBarEnabled(false);
        a.getSettings().setSupportZoom(false);
        a.getSettings().setSaveFormData(false);
        a.getSettings().setSavePassword(false);
        a.getSettings().setJavaScriptEnabled(true);
        a.addJavascriptInterface(new MokreditJS((MktPayment) context), "mokredit");
        if (this.e) {
            a.getSettings().setUserAgentString(String.valueOf(a.getSettings().getUserAgentString()) + "/mokredit mobile");
        }
        a.requestFocus();
        a.setWebViewClient(new WebViewClient() { // from class: com.mokredit.payment.MktLineLyt.2
            @Override // android.webkit.WebViewClient
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                sslErrorHandler.proceed();
            }

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (!MktLineLyt.a(MktLineLyt.this, str) || !MktLineLyt.this.e) {
                    return super.shouldOverrideUrlLoading(webView, str);
                }
                new Thread(new HttpThread(str)).start();
                return true;
            }
        });
        a.getSettings().setDefaultTextEncodingName("utf-8");
        if (this.c.getUrl() != null) {
            a.loadUrl(this.c.getUrl());
        } else {
            Toast.makeText(context, "url is not empty", 0).show();
        }
        this.b = ProgressDialog.show((Activity) context, StringUtils.EMPTY, "Please Wait...", true, false);
        new Thread() { // from class: com.mokredit.payment.MktLineLyt.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    sleep(Constants.CHARGE_QUERY_RESULT_TIME);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                } finally {
                    MktLineLyt.this.b.dismiss();
                }
            }
        }.start();
    }

    public static int a(Context context, float f) {
        return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
    }

    static /* synthetic */ boolean a(MktLineLyt mktLineLyt, String str) {
        int indexOf = mktLineLyt.c.getUrl().indexOf("gateway");
        if (!str.contains(String.valueOf(mktLineLyt.c.getUrl().substring(0, indexOf)) + "mobile/")) {
            return false;
        }
        Log.i("fafafafafafaf", String.valueOf(mktLineLyt.c.getUrl().substring(0, indexOf)) + "mobile/");
        return true;
    }
}
