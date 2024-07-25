package com.google.ads.internal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import com.google.ads.AdActivity;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AppEventListener;
import com.google.ads.ab;
import com.google.ads.ac;
import com.google.ads.af;
import com.google.ads.m;
import com.google.ads.util.AdUtil;
import com.mongodb.util.TimeConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class d {
    private static final Object a = new Object();
    private final m b;
    private AdWebView f;
    private i g;
    private long i;
    private boolean j;
    private SharedPreferences o;
    private ac q;
    private boolean r;
    private LinkedList<String> s;
    private LinkedList<String> t;
    private Boolean v;
    private com.google.ads.d w;
    private com.google.ads.e x;
    private com.google.ads.f y;
    private int u = -1;
    private String z = null;
    private g e = new g();
    private c c = null;
    private AdRequest d = null;
    private boolean k = false;
    private Handler h = new Handler();
    private long p = TimeConstants.MS_MINUTE;
    private boolean l = false;
    private boolean n = false;
    private boolean m = true;

    public d(m mVar, boolean z) {
        this.b = mVar;
        this.r = z;
        if (mVar != null && mVar.d.a() != null) {
            synchronized (a) {
                this.o = mVar.d.a().getSharedPreferences("GoogleAdMobAdsPrefs", 0);
                if (z) {
                    long j = this.o.getLong("Timeout" + mVar.b, -1L);
                    if (j < 0) {
                        this.i = 5000L;
                    } else {
                        this.i = j;
                    }
                } else {
                    this.i = TimeConstants.MS_MINUTE;
                }
            }
            this.q = new ac(this);
            this.s = new LinkedList<>();
            this.t = new LinkedList<>();
            a();
            AdUtil.h(mVar.d.a());
            this.w = new com.google.ads.d();
            this.x = new com.google.ads.e(this);
            this.v = null;
            this.y = null;
        }
    }

    public synchronized void a() {
        this.f = new AdWebView(this.b, this.b.i.a().b());
        this.f.setVisibility(8);
        this.g = i.a(this, a.c, true, this.b.b());
        this.f.setWebViewClient(this.g);
        if (AdUtil.a < this.b.a.a().a.a().a.a().intValue() && !this.b.i.a().a()) {
            com.google.ads.util.b.a("Disabling hardware acceleration for a banner.");
            this.f.b();
        }
    }

    public synchronized void b() {
        if (this.x != null) {
            this.x.b();
        }
        this.b.k.a(null);
        this.b.l.a(null);
        A();
        if (this.f != null) {
            this.f.destroy();
        }
    }

    public void a(String str) {
        Uri build = new Uri.Builder().encodedQuery(str).build();
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> b = AdUtil.b(build);
        for (String str2 : b.keySet()) {
            sb.append(str2).append(" = ").append(b.get(str2)).append("\n");
        }
        this.z = sb.toString().trim();
        if (TextUtils.isEmpty(this.z)) {
            this.z = null;
        }
    }

    public String c() {
        return this.z;
    }

    public synchronized void d() {
        this.m = false;
        com.google.ads.util.b.a("Refreshing is no longer allowed on this AdView.");
    }

    public synchronized void e() {
        if (this.l) {
            com.google.ads.util.b.a("Disabling refreshing.");
            this.h.removeCallbacks(this.q);
            this.l = false;
        } else {
            com.google.ads.util.b.a("Refreshing is already disabled.");
        }
    }

    public synchronized void f() {
        this.n = false;
        if (this.b.a()) {
            if (this.m) {
                if (!this.l) {
                    com.google.ads.util.b.a("Enabling refreshing every " + this.p + " milliseconds.");
                    this.h.postDelayed(this.q, this.p);
                    this.l = true;
                } else {
                    com.google.ads.util.b.a("Refreshing is already enabled.");
                }
            } else {
                com.google.ads.util.b.a("Refreshing disabled on this AdView");
            }
        } else {
            com.google.ads.util.b.a("Tried to enable refreshing on something other than an AdView.");
        }
    }

    public void g() {
        f();
        this.n = true;
    }

    public m h() {
        return this.b;
    }

    public synchronized com.google.ads.d i() {
        return this.w;
    }

    public synchronized c j() {
        return this.c;
    }

    public synchronized AdWebView k() {
        return this.f;
    }

    public synchronized i l() {
        return this.g;
    }

    public g m() {
        return this.e;
    }

    public synchronized void a(int i) {
        this.u = i;
    }

    public synchronized int n() {
        return this.u;
    }

    public long o() {
        return this.i;
    }

    public synchronized boolean p() {
        return this.c != null;
    }

    public synchronized boolean q() {
        return this.j;
    }

    public synchronized boolean r() {
        return this.k;
    }

    public synchronized boolean s() {
        return this.l;
    }

    public synchronized void a(AdRequest adRequest) {
        if (p()) {
            com.google.ads.util.b.e("loadAd called while the ad is already loading, so aborting.");
        } else if (AdActivity.isShowing()) {
            com.google.ads.util.b.e("loadAd called while an interstitial or landing page is displayed, so aborting");
        } else if (AdUtil.c(this.b.d.a()) && AdUtil.b(this.b.d.a())) {
            if (af.a(this.b.d.a(), this.o.getLong("GoogleAdMobDoritosLife", TimeConstants.MS_MINUTE))) {
                af.a(this.b.c.a());
            }
            this.k = false;
            this.s.clear();
            this.d = adRequest;
            if (this.w.a()) {
                this.x.a(this.w.b(), adRequest);
            } else {
                this.c = new c(this);
                this.c.a(adRequest);
            }
        }
    }

    public synchronized void a(AdRequest.ErrorCode errorCode) {
        this.c = null;
        if (errorCode == AdRequest.ErrorCode.NETWORK_ERROR) {
            m().q();
            a(60.0f);
            if (!s()) {
                g();
            }
        }
        if (this.b.b()) {
            if (errorCode == AdRequest.ErrorCode.NO_FILL) {
                this.e.v();
            } else if (errorCode == AdRequest.ErrorCode.NETWORK_ERROR) {
                this.e.t();
            }
        }
        com.google.ads.util.b.c("onFailedToReceiveAd(" + errorCode + ")");
        AdListener a2 = this.b.k.a();
        if (a2 != null) {
            a2.onFailedToReceiveAd(this.b.f.a(), errorCode);
        }
    }

    public synchronized void a(com.google.ads.c cVar) {
        this.c = null;
        if (cVar.d()) {
            a(cVar.e());
            if (!this.l) {
                f();
            }
        } else if (this.l) {
            e();
        }
        this.x.a(cVar, this.d);
    }

    public synchronized void a(View view, com.google.ads.h hVar, com.google.ads.f fVar, boolean z) {
        com.google.ads.util.b.a("AdManager.onReceiveGWhirlAd() called.");
        this.k = true;
        this.y = fVar;
        if (this.b.a()) {
            a(view);
            a(fVar, Boolean.valueOf(z));
        }
        this.x.d(hVar);
        AdListener a2 = this.b.k.a();
        if (a2 != null) {
            a2.onReceiveAd(this.b.f.a());
        }
    }

    public synchronized void a(com.google.ads.f fVar, boolean z) {
        com.google.ads.util.b.a(String.format(Locale.US, "AdManager.onGWhirlAdClicked(%b) called.", Boolean.valueOf(z)));
        b(fVar, Boolean.valueOf(z));
    }

    public synchronized void b(com.google.ads.c cVar) {
        com.google.ads.util.b.a("AdManager.onGWhirlNoFill() called.");
        a(cVar.i(), cVar.c());
        AdListener a2 = this.b.k.a();
        if (a2 != null) {
            a2.onFailedToReceiveAd(this.b.f.a(), AdRequest.ErrorCode.NO_FILL);
        }
    }

    public synchronized void t() {
        this.e.w();
        com.google.ads.util.b.c("onDismissScreen()");
        AdListener a2 = this.b.k.a();
        if (a2 != null) {
            a2.onDismissScreen(this.b.f.a());
        }
    }

    public synchronized void u() {
        com.google.ads.util.b.c("onPresentScreen()");
        AdListener a2 = this.b.k.a();
        if (a2 != null) {
            a2.onPresentScreen(this.b.f.a());
        }
    }

    public synchronized void v() {
        com.google.ads.util.b.c("onLeaveApplication()");
        AdListener a2 = this.b.k.a();
        if (a2 != null) {
            a2.onLeaveApplication(this.b.f.a());
        }
    }

    public synchronized void a(String str, String str2) {
        AppEventListener a2 = this.b.l.a();
        if (a2 != null) {
            a2.onAppEvent(str, str2);
        }
    }

    public void w() {
        this.e.f();
        B();
    }

    private void a(com.google.ads.f fVar, Boolean bool) {
        List<String> d = fVar.d();
        if (d == null) {
            d = new ArrayList<>();
            d.add("http://e.admob.com/imp?ad_loc=@gw_adlocid@&qdata=@gw_qdata@&ad_network_id=@gw_adnetid@&js=@gw_sdkver@&session_id=@gw_sessid@&seq_num=@gw_seqnum@&nr=@gw_adnetrefresh@&adt=@gw_adt@&aec=@gw_aec@");
        }
        a(d, fVar.a(), fVar.b(), fVar.c(), bool, this.e.d(), this.e.e());
    }

    private void b(com.google.ads.f fVar, Boolean bool) {
        List<String> e = fVar.e();
        if (e == null) {
            e = new ArrayList<>();
            e.add("http://e.admob.com/clk?ad_loc=@gw_adlocid@&qdata=@gw_qdata@&ad_network_id=@gw_adnetid@&js=@gw_sdkver@&session_id=@gw_sessid@&seq_num=@gw_seqnum@&nr=@gw_adnetrefresh@");
        }
        a(e, fVar.a(), fVar.b(), fVar.c(), bool, null, null);
    }

    private void a(List<String> list, String str) {
        List<String> list2;
        if (list == null) {
            list2 = new ArrayList<>();
            list2.add("http://e.admob.com/nofill?ad_loc=@gw_adlocid@&qdata=@gw_qdata@&js=@gw_sdkver@&session_id=@gw_sessid@&seq_num=@gw_seqnum@&adt=@gw_adt@&aec=@gw_aec@");
        } else {
            list2 = list;
        }
        a(list2, null, null, str, null, this.e.d(), this.e.e());
    }

    private void a(List<String> list, String str, String str2, String str3, Boolean bool, String str4, String str5) {
        String a2 = AdUtil.a(this.b.d.a());
        com.google.ads.b a3 = com.google.ads.b.a();
        String bigInteger = a3.b().toString();
        String bigInteger2 = a3.c().toString();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            new Thread(new ab(com.google.ads.g.a(it.next(), this.b.b.a(), bool, a2, str, str2, str3, bigInteger, bigInteger2, str4, str5), this.b.d.a())).start();
        }
        this.e.b();
    }

    public synchronized void x() {
        Activity a2 = this.b.c.a();
        if (a2 == null) {
            com.google.ads.util.b.e("activity was null while trying to ping tracking URLs.");
        } else {
            Iterator<String> it = this.s.iterator();
            while (it.hasNext()) {
                new Thread(new ab(it.next(), a2.getApplicationContext())).start();
            }
        }
    }

    public void a(Runnable runnable) {
        this.h.post(runnable);
    }

    public synchronized void y() {
        if (this.d != null) {
            if (this.b.a()) {
                if (this.b.g.a().isShown() && AdUtil.d()) {
                    com.google.ads.util.b.c("Refreshing ad.");
                    a(this.d);
                } else {
                    com.google.ads.util.b.a("Not refreshing because the ad is not visible.");
                }
                if (this.n) {
                    e();
                } else {
                    this.h.postDelayed(this.q, this.p);
                }
            } else {
                com.google.ads.util.b.a("Tried to refresh an ad that wasn't an AdView.");
            }
        } else {
            com.google.ads.util.b.a("Tried to refresh before calling loadAd().");
        }
    }

    public void a(long j) {
        synchronized (a) {
            SharedPreferences.Editor edit = this.o.edit();
            edit.putLong("Timeout" + this.b.b, j);
            edit.commit();
            if (this.r) {
                this.i = j;
            }
        }
    }

    public synchronized void a(boolean z) {
        this.j = z;
    }

    public void a(View view) {
        this.b.e.a().removeAllViews();
        this.b.e.a().addView(view);
    }

    public synchronized void a(float f) {
        long j = this.p;
        this.p = 1000.0f * f;
        if (s() && this.p != j) {
            e();
            f();
        }
    }

    public synchronized void b(long j) {
        if (j > 0) {
            this.o.edit().putLong("GoogleAdMobDoritosLife", j).commit();
        }
    }

    public synchronized void z() {
        com.google.ads.util.a.a(this.b.b());
        if (this.k) {
            this.k = false;
            if (this.v == null) {
                com.google.ads.util.b.b("isMediationFlag is null in show() with isReady() true. we should have an ad and know whether this is a mediation request or not. ");
            } else if (this.v.booleanValue()) {
                if (this.x.c()) {
                    a(this.y, (Boolean) false);
                }
            } else {
                AdActivity.launchAdActivity(this, new e("interstitial"));
                x();
            }
        } else {
            com.google.ads.util.b.c("Cannot show interstitial because it is not loaded and ready.");
        }
    }

    public synchronized void A() {
        if (this.c != null) {
            this.c.a();
            this.c = null;
        }
        if (this.f != null) {
            this.f.stopLoading();
        }
    }

    protected synchronized void B() {
        Activity a2 = this.b.c.a();
        if (a2 == null) {
            com.google.ads.util.b.e("activity was null while trying to ping click tracking URLs.");
        } else {
            Iterator<String> it = this.t.iterator();
            while (it.hasNext()) {
                new Thread(new ab(it.next(), a2.getApplicationContext())).start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void C() {
        this.c = null;
        this.k = true;
        this.f.setVisibility(0);
        if (this.b.a()) {
            a(this.f);
        }
        this.e.g();
        if (this.b.a()) {
            x();
        }
        com.google.ads.util.b.c("onReceiveAd()");
        AdListener a2 = this.b.k.a();
        if (a2 != null) {
            a2.onReceiveAd(this.b.f.a());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void b(String str) {
        com.google.ads.util.b.a("Adding a tracking URL: " + str);
        this.s.add(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void a(LinkedList<String> linkedList) {
        Iterator<String> it = linkedList.iterator();
        while (it.hasNext()) {
            com.google.ads.util.b.a("Adding a click tracking URL: " + it.next());
        }
        this.t = linkedList;
    }

    public void b(boolean z) {
        this.v = Boolean.valueOf(z);
    }
}
