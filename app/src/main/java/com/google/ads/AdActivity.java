package com.google.ads;

import android.R;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import com.google.ads.ah;
import com.google.ads.internal.AdVideoView;
import com.google.ads.internal.AdWebView;
import com.google.ads.l;
import com.google.ads.util.AdUtil;
import java.util.HashMap;

/* loaded from: classes.dex */
public class AdActivity extends Activity implements View.OnClickListener {
    public static final String BASE_URL_PARAM = "baseurl";
    public static final String HTML_PARAM = "html";
    public static final String INTENT_ACTION_PARAM = "i";
    public static final String ORIENTATION_PARAM = "o";
    public static final String TYPE_PARAM = "m";
    public static final String URL_PARAM = "u";
    private static final com.google.ads.internal.a a = com.google.ads.internal.a.a.b();
    private static final Object b = new Object();
    private static AdActivity c = null;
    private static com.google.ads.internal.d d = null;
    private static AdActivity e = null;
    private static AdActivity f = null;
    private static final StaticMethodWrapper g = new StaticMethodWrapper();
    private AdWebView h;
    private boolean j;
    private long k;
    private RelativeLayout l;
    private boolean n;
    private boolean o;
    private boolean p;
    private boolean q;
    private AdVideoView r;
    private ViewGroup i = null;
    private AdActivity m = null;

    /* loaded from: classes.dex */
    public static class StaticMethodWrapper {
        public boolean isShowing() {
            boolean z;
            synchronized (AdActivity.b) {
                z = AdActivity.e != null;
            }
            return z;
        }

        public void launchAdActivity(com.google.ads.internal.d adManager, com.google.ads.internal.e adOpener) {
            synchronized (AdActivity.b) {
                if (AdActivity.d == null) {
                    com.google.ads.internal.d unused = AdActivity.d = adManager;
                } else if (AdActivity.d != adManager) {
                    com.google.ads.util.b.b("Tried to launch a new AdActivity with a different AdManager.");
                    return;
                }
                Activity a = adManager.h().c.a();
                if (a == null) {
                    com.google.ads.util.b.e("activity was null while launching an AdActivity.");
                    return;
                }
                Intent intent = new Intent(a.getApplicationContext(), (Class<?>) AdActivity.class);
                intent.putExtra("com.google.ads.AdOpener", adOpener.a());
                try {
                    com.google.ads.util.b.a("Launching AdActivity.");
                    a.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    com.google.ads.util.b.b("Activity not found.", e);
                }
            }
        }
    }

    protected View a(int i) {
        ImageButton imageButton = new ImageButton(getApplicationContext());
        imageButton.setImageResource(R.drawable.btn_dialog);
        imageButton.setBackgroundColor(0);
        imageButton.setOnClickListener(this);
        imageButton.setPadding(0, 0, 0, 0);
        int applyDimension = (int) TypedValue.applyDimension(1, i, getResources().getDisplayMetrics());
        FrameLayout frameLayout = new FrameLayout(getApplicationContext());
        frameLayout.addView(imageButton, applyDimension, applyDimension);
        return frameLayout;
    }

    private void a(String str) {
        com.google.ads.util.b.b(str);
        finish();
    }

    private void a(String str, Throwable th) {
        com.google.ads.util.b.b(str, th);
        finish();
    }

    public AdVideoView getAdVideoView() {
        return this.r;
    }

    public AdWebView getOpeningAdWebView() {
        AdWebView adWebView = null;
        if (this.m != null) {
            return this.m.h;
        }
        synchronized (b) {
            if (d == null) {
                com.google.ads.util.b.e("currentAdManager was null while trying to get the opening AdWebView.");
            } else {
                AdWebView k = d.k();
                if (k != this.h) {
                    adWebView = k;
                }
            }
        }
        return adWebView;
    }

    public static boolean isShowing() {
        return g.isShowing();
    }

    public static void launchAdActivity(com.google.ads.internal.d adManager, com.google.ads.internal.e adOpener) {
        g.launchAdActivity(adManager, adOpener);
    }

    protected void a(HashMap<String, String> hashMap, com.google.ads.internal.d dVar) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.google.android.apps.plus", "com.google.android.apps.circles.platform.PlusOneActivity"));
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.putExtras(getIntent().getExtras());
        intent.putExtra("com.google.circles.platform.intent.extra.ENTITY", hashMap.get(URL_PARAM));
        intent.putExtra("com.google.circles.platform.intent.extra.ENTITY_TYPE", ah.b.AD.c);
        intent.putExtra("com.google.circles.platform.intent.extra.ACTION", hashMap.get("a"));
        a(dVar);
        try {
            com.google.ads.util.b.a("Launching Google+ intent from AdActivity.");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e2) {
            a(e2.getMessage(), e2);
        }
    }

    protected void b(HashMap<String, String> hashMap, com.google.ads.internal.d dVar) {
        if (hashMap == null) {
            a("Could not get the paramMap in launchIntent()");
            return;
        }
        String str = hashMap.get(URL_PARAM);
        if (str == null) {
            a("Could not get the URL parameter in launchIntent().");
            return;
        }
        String str2 = hashMap.get(INTENT_ACTION_PARAM);
        String str3 = hashMap.get(TYPE_PARAM);
        Uri parse = Uri.parse(str);
        Intent intent = str2 == null ? new Intent("android.intent.action.VIEW", parse) : new Intent(str2, parse);
        if (str3 != null) {
            intent.setDataAndType(parse, str3);
        }
        a(dVar);
        try {
            com.google.ads.util.b.a("Launching an intent from AdActivity: " + intent.getAction() + " - " + parse);
            startActivity(intent);
        } catch (ActivityNotFoundException e2) {
            a(e2.getMessage(), e2);
        }
    }

    protected void a(com.google.ads.internal.d dVar) {
        this.h = null;
        this.k = SystemClock.elapsedRealtime();
        this.n = true;
        synchronized (b) {
            if (c == null) {
                c = this;
                dVar.v();
            }
        }
    }

    protected AdVideoView a(Activity activity) {
        return new AdVideoView(activity, this.h);
    }

    public void moveAdVideoView(int x, int y, int width, int height) {
        if (this.r != null) {
            this.r.setLayoutParams(a(x, y, width, height));
            this.r.requestLayout();
        }
    }

    public void newAdVideoView(int x, int y, int width, int height) {
        if (this.r == null) {
            this.r = a(this);
            this.l.addView(this.r, 0, a(x, y, width, height));
            synchronized (b) {
                if (d == null) {
                    com.google.ads.util.b.e("currentAdManager was null while trying to get the opening AdWebView.");
                } else {
                    d.l().b(false);
                }
            }
        }
    }

    private RelativeLayout.LayoutParams a(int i, int i2, int i3, int i4) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(i3, i4);
        layoutParams.setMargins(i, i2, 0, 0);
        layoutParams.addRule(10);
        layoutParams.addRule(9);
        return layoutParams;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        finish();
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        int i;
        super.onCreate(savedInstanceState);
        this.j = false;
        synchronized (b) {
            if (d != null) {
                com.google.ads.internal.d dVar = d;
                if (e == null) {
                    e = this;
                    dVar.u();
                }
                if (this.m == null && f != null) {
                    this.m = f;
                }
                f = this;
                if ((dVar.h().a() && e == this) || (dVar.h().b() && this.m == e)) {
                    dVar.w();
                }
                boolean q = dVar.q();
                l.a a2 = dVar.h().a.a().a.a();
                this.q = AdUtil.a >= a2.a.a().intValue();
                this.p = AdUtil.a >= a2.b.a().intValue();
                this.l = null;
                this.n = false;
                this.o = true;
                this.r = null;
                Bundle bundleExtra = getIntent().getBundleExtra("com.google.ads.AdOpener");
                if (bundleExtra == null) {
                    a("Could not get the Bundle used to create AdActivity.");
                    return;
                }
                com.google.ads.internal.e eVar = new com.google.ads.internal.e(bundleExtra);
                String b2 = eVar.b();
                HashMap<String, String> c2 = eVar.c();
                if (b2.equals("plusone")) {
                    a(c2, dVar);
                    return;
                }
                if (b2.equals("intent")) {
                    b(c2, dVar);
                    return;
                }
                this.l = new RelativeLayout(getApplicationContext());
                if (b2.equals("webapp")) {
                    this.h = new AdWebView(dVar.h(), null);
                    com.google.ads.internal.i a3 = com.google.ads.internal.i.a(dVar, com.google.ads.internal.a.c, true, !q);
                    a3.d(true);
                    if (q) {
                        a3.a(true);
                    }
                    this.h.setWebViewClient(a3);
                    String str = c2.get(URL_PARAM);
                    String str2 = c2.get(BASE_URL_PARAM);
                    String str3 = c2.get(HTML_PARAM);
                    if (str != null) {
                        this.h.loadUrl(str);
                    } else if (str3 != null) {
                        this.h.loadDataWithBaseURL(str2, str3, "text/html", "utf-8", null);
                    } else {
                        a("Could not get the URL or HTML parameter to show a web app.");
                        return;
                    }
                    String str4 = c2.get(ORIENTATION_PARAM);
                    if ("p".equals(str4)) {
                        i = AdUtil.b();
                    } else if ("l".equals(str4)) {
                        i = AdUtil.a();
                    } else if (this == e) {
                        i = dVar.n();
                    } else {
                        i = -1;
                    }
                    a(this.h, false, i, q);
                    return;
                }
                if (b2.equals("interstitial") || b2.equals("expand")) {
                    this.h = dVar.k();
                    int n = dVar.n();
                    if (b2.equals("expand")) {
                        this.h.setIsExpandedMraid(true);
                        this.o = false;
                        if (this.p && !this.q) {
                            com.google.ads.util.b.a("Re-enabling hardware acceleration on expanding MRAID WebView.");
                            this.h.c();
                        }
                    }
                    a(this.h, true, n, q);
                    return;
                }
                a("Unknown AdOpener, <action: " + b2 + ">");
                return;
            }
            a("Could not get currentAdManager.");
        }
    }

    protected void a(AdWebView adWebView, boolean z, int i, boolean z2) {
        requestWindowFeature(1);
        Window window = getWindow();
        window.setFlags(1024, 1024);
        if (AdUtil.a >= 11) {
            if (this.p) {
                com.google.ads.util.b.a("Enabling hardware acceleration on the AdActivity window.");
                com.google.ads.util.g.a(window);
            } else {
                com.google.ads.util.b.a("Disabling hardware acceleration on the AdActivity WebView.");
                adWebView.b();
            }
        }
        ViewParent parent = adWebView.getParent();
        if (parent != null) {
            if (z2) {
                if (parent instanceof ViewGroup) {
                    this.i = (ViewGroup) parent;
                    this.i.removeView(adWebView);
                } else {
                    a("MRAID banner was not a child of a ViewGroup.");
                    return;
                }
            } else {
                a("Interstitial created with an AdWebView that has a parent.");
                return;
            }
        }
        if (adWebView.d() != null) {
            a("Interstitial created with an AdWebView that is already in use by another AdActivity.");
            return;
        }
        setRequestedOrientation(i);
        adWebView.setAdActivity(this);
        View a2 = a(z2 ? 50 : 32);
        this.l.addView(adWebView, -1, -1);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        if (z2) {
            layoutParams.addRule(10);
            layoutParams.addRule(11);
        } else {
            layoutParams.addRule(10);
            layoutParams.addRule(9);
        }
        this.l.addView(a2, layoutParams);
        this.l.setKeepScreenOn(true);
        setContentView(this.l);
        this.l.getRootView().setBackgroundColor(-16777216);
        if (z) {
            a.a(adWebView);
        }
    }

    @Override // android.app.Activity
    public void onDestroy() {
        if (this.l != null) {
            this.l.removeAllViews();
        }
        if (isFinishing()) {
            d();
            if (this.o && this.h != null) {
                this.h.stopLoading();
                this.h.destroy();
                this.h = null;
            }
        }
        super.onDestroy();
    }

    @Override // android.app.Activity
    public void onPause() {
        if (isFinishing()) {
            d();
        }
        super.onPause();
    }

    private void d() {
        if (!this.j) {
            if (this.h != null) {
                a.b(this.h);
                this.h.setAdActivity(null);
                this.h.setIsExpandedMraid(false);
                if (!this.o && this.l != null && this.i != null) {
                    if (this.p && !this.q) {
                        com.google.ads.util.b.a("Disabling hardware acceleration on collapsing MRAID WebView.");
                        this.h.b();
                    } else if (!this.p && this.q) {
                        com.google.ads.util.b.a("Re-enabling hardware acceleration on collapsing MRAID WebView.");
                        this.h.c();
                    }
                    this.l.removeView(this.h);
                    this.i.addView(this.h);
                }
            }
            if (this.r != null) {
                this.r.e();
                this.r = null;
            }
            if (this == c) {
                c = null;
            }
            f = this.m;
            synchronized (b) {
                if (d != null && this.o && this.h != null) {
                    if (this.h == d.k()) {
                        d.a();
                    }
                    this.h.stopLoading();
                }
                if (this == e) {
                    e = null;
                    if (d != null) {
                        d.t();
                        d = null;
                    } else {
                        com.google.ads.util.b.e("currentAdManager is null while trying to destroy AdActivity.");
                    }
                }
            }
            this.j = true;
            com.google.ads.util.b.a("AdActivity is closing.");
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        if (this.n && hasFocus && SystemClock.elapsedRealtime() - this.k > 250) {
            com.google.ads.util.b.d("Launcher AdActivity got focus and is closing.");
            finish();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override // android.app.Activity
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        if (getOpeningAdWebView() != null && data != null && data.getExtras() != null && data.getExtras().getString("com.google.circles.platform.result.extra.CONFIRMATION") != null && data.getExtras().getString("com.google.circles.platform.result.extra.ACTION") != null) {
            String string = data.getExtras().getString("com.google.circles.platform.result.extra.CONFIRMATION");
            String string2 = data.getExtras().getString("com.google.circles.platform.result.extra.ACTION");
            if (string.equals("yes")) {
                if (string2.equals("insert")) {
                    af.a((WebView) getOpeningAdWebView(), true);
                } else if (string2.equals("delete")) {
                    af.a((WebView) getOpeningAdWebView(), false);
                }
            }
        }
        finish();
    }
}
