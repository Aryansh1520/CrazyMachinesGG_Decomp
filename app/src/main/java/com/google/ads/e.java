package com.google.ads;

import android.app.Activity;
import android.os.SystemClock;
import android.view.View;
import com.google.ads.g;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class e {
    private final com.google.ads.internal.d a;
    private h b;
    private Object c;
    private Thread d;
    private Object e;
    private boolean f;
    private Object g;

    public e(com.google.ads.internal.d dVar) {
        this.b = null;
        this.c = new Object();
        this.d = null;
        this.e = new Object();
        this.f = false;
        this.g = new Object();
        com.google.ads.util.a.b(dVar);
        this.a = dVar;
    }

    public boolean a() {
        boolean z;
        synchronized (this.e) {
            z = this.d != null;
        }
        return z;
    }

    public void b() {
        synchronized (this.g) {
            this.f = true;
            d(null);
            synchronized (this.e) {
                if (this.d != null) {
                    this.d.interrupt();
                }
            }
        }
    }

    public void a(final c cVar, final AdRequest adRequest) {
        synchronized (this.e) {
            if (a()) {
                com.google.ads.util.b.c("Mediation thread is not done executing previous mediation  request. Ignoring new mediation request");
                return;
            }
            a(cVar, this.a);
            this.d = new Thread(new Runnable() { // from class: com.google.ads.e.1
                @Override // java.lang.Runnable
                public void run() {
                    e.this.b(cVar, adRequest);
                    synchronized (e.this.e) {
                        e.this.d = null;
                    }
                }
            });
            this.d.start();
        }
    }

    public static boolean a(c cVar, com.google.ads.internal.d dVar) {
        if (cVar.j() == null) {
            return true;
        }
        if (dVar.h().b()) {
            if (cVar.j().a()) {
                return true;
            }
            com.google.ads.util.b.e("InterstitialAd received a mediation response corresponding to a non-interstitial ad. Make sure you specify 'interstitial' as the ad-type in the mediation UI.");
            return false;
        }
        AdSize b = dVar.h().i.a().b();
        if (cVar.j().a()) {
            com.google.ads.util.b.e("AdView received a mediation response corresponding to an interstitial ad. Make sure you specify the banner ad size corresponding to the AdSize you used in your AdView  (" + b + ") in the ad-type field in the mediation UI.");
            return false;
        }
        AdSize b2 = cVar.j().b();
        if (b2 == b) {
            return true;
        }
        com.google.ads.util.b.e("Mediation server returned ad size: '" + b2 + "', while the AdView was created with ad size: '" + b + "'. Using the ad-size passed to the AdView on creation.");
        return false;
    }

    private boolean a(h hVar, String str) {
        if (e() == hVar) {
            return true;
        }
        com.google.ads.util.b.c("GWController: ignoring callback to " + str + " from non showing ambassador with adapter class: '" + hVar.h() + "'.");
        return false;
    }

    public void a(h hVar, final boolean z) {
        if (a(hVar, "onAdClicked()")) {
            final f a = hVar.a();
            this.a.a(new Runnable() { // from class: com.google.ads.e.2
                @Override // java.lang.Runnable
                public void run() {
                    e.this.a.a(a, z);
                }
            });
        }
    }

    public void a(h hVar, final View view) {
        if (e() != hVar) {
            com.google.ads.util.b.c("GWController: ignoring onAdRefreshed() callback from non-showing ambassador (adapter class name is '" + hVar.h() + "').");
            return;
        }
        this.a.m().a(g.a.AD);
        final f a = this.b.a();
        this.a.a(new Runnable() { // from class: com.google.ads.e.3
            @Override // java.lang.Runnable
            public void run() {
                e.this.a.a(view, e.this.b, a, true);
            }
        });
    }

    public void a(h hVar) {
        if (a(hVar, "onPresentScreen")) {
            this.a.a(new Runnable() { // from class: com.google.ads.e.4
                @Override // java.lang.Runnable
                public void run() {
                    e.this.a.u();
                }
            });
        }
    }

    public void b(h hVar) {
        if (a(hVar, "onDismissScreen")) {
            this.a.a(new Runnable() { // from class: com.google.ads.e.5
                @Override // java.lang.Runnable
                public void run() {
                    e.this.a.t();
                }
            });
        }
    }

    public void c(h hVar) {
        if (a(hVar, "onLeaveApplication")) {
            this.a.a(new Runnable() { // from class: com.google.ads.e.6
                @Override // java.lang.Runnable
                public void run() {
                    e.this.a.v();
                }
            });
        }
    }

    public boolean c() {
        com.google.ads.util.a.a(this.a.h().b());
        h e = e();
        if (e != null) {
            e.g();
            return true;
        }
        com.google.ads.util.b.b("There is no ad ready to show.");
        return false;
    }

    protected e() {
        this.b = null;
        this.c = new Object();
        this.d = null;
        this.e = new Object();
        this.f = false;
        this.g = new Object();
        this.a = null;
    }

    private boolean d() {
        boolean z;
        synchronized (this.g) {
            z = this.f;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(final c cVar, AdRequest adRequest) {
        synchronized (this.e) {
            com.google.ads.util.a.a(Thread.currentThread(), this.d);
        }
        List<a> f = cVar.f();
        long b = cVar.a() ? cVar.b() : 10000L;
        for (a aVar : f) {
            com.google.ads.util.b.a("Looking to fetch ads from network: " + aVar.b());
            List<String> c = aVar.c();
            HashMap<String, String> d = aVar.d();
            f fVar = new f(aVar.a(), aVar.b(), cVar.c(), cVar.g(), cVar.h(), cVar.i());
            for (String str : c) {
                Activity a = this.a.h().c.a();
                if (a == null) {
                    com.google.ads.util.b.a("Activity is null while mediating.  Terminating mediation thread.");
                    return;
                }
                this.a.m().c();
                if (!a(str, a, adRequest, fVar, d, b)) {
                    if (d()) {
                        com.google.ads.util.b.a("GWController.destroy() called. Terminating mediation thread.");
                        return;
                    }
                } else {
                    return;
                }
            }
        }
        this.a.a(new Runnable() { // from class: com.google.ads.e.7
            @Override // java.lang.Runnable
            public void run() {
                e.this.a.b(cVar);
            }
        });
    }

    private boolean a(String str, Activity activity, AdRequest adRequest, final f fVar, HashMap<String, String> hashMap, long j) {
        final h hVar = new h(this, this.a.h().i.a(), fVar, str, adRequest, hashMap);
        synchronized (hVar) {
            hVar.a(activity);
            while (!hVar.c() && j > 0) {
                try {
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    hVar.wait(j);
                    j -= SystemClock.elapsedRealtime() - elapsedRealtime;
                } catch (InterruptedException e) {
                    com.google.ads.util.b.a("Interrupted while waiting for ad network to load ad using adapter class: " + str);
                }
            }
            this.a.m().a(hVar.e());
            if (hVar.c() && hVar.d()) {
                final View f = this.a.h().b() ? null : hVar.f();
                this.a.a(new Runnable() { // from class: com.google.ads.e.8
                    @Override // java.lang.Runnable
                    public void run() {
                        if (!e.this.e(hVar)) {
                            e.this.a.a(f, hVar, fVar, false);
                        } else {
                            com.google.ads.util.b.a("Trying to switch GWAdNetworkAmbassadors, but GWController().destroy() has been called. Destroying the new ambassador and terminating mediation.");
                        }
                    }
                });
                return true;
            }
            hVar.b();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean e(h hVar) {
        boolean z;
        synchronized (this.g) {
            if (d()) {
                hVar.b();
                z = true;
            } else {
                z = false;
            }
        }
        return z;
    }

    private h e() {
        h hVar;
        synchronized (this.c) {
            hVar = this.b;
        }
        return hVar;
    }

    public void d(h hVar) {
        synchronized (this.c) {
            if (this.b != hVar) {
                if (this.b != null) {
                    this.b.b();
                }
                this.b = hVar;
            }
        }
    }
}
