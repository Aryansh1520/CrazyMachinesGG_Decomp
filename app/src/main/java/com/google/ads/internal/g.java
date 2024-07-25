package com.google.ads.internal;

import android.os.SystemClock;
import com.google.ads.g;
import com.mappn.sdk.pay.util.Constants;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class g {
    private static long f = 0;
    private static long g = 0;
    private static long i = -1;
    private long b;
    private long c;
    private long d;
    private String h;
    private String l;
    private long m;
    private boolean j = false;
    private boolean k = false;
    private LinkedList<Long> a = new LinkedList<>();
    private LinkedList<Long> e = new LinkedList<>();
    private LinkedList<Long> n = new LinkedList<>();
    private LinkedList<g.a> o = new LinkedList<>();

    /* JADX INFO: Access modifiers changed from: protected */
    public g() {
        a();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void a() {
        this.a.clear();
        this.b = 0L;
        this.c = 0L;
        this.d = 0L;
        this.e.clear();
        this.m = -1L;
        this.n.clear();
        this.o.clear();
        this.h = null;
        this.j = false;
        this.k = false;
    }

    public synchronized void b() {
        this.n.clear();
        this.o.clear();
    }

    public synchronized void c() {
        this.m = SystemClock.elapsedRealtime();
    }

    public synchronized void a(g.a aVar) {
        this.n.add(Long.valueOf(SystemClock.elapsedRealtime() - this.m));
        this.o.add(aVar);
    }

    public synchronized String d() {
        StringBuilder sb;
        sb = new StringBuilder();
        Iterator<Long> it = this.n.iterator();
        while (it.hasNext()) {
            long longValue = it.next().longValue();
            if (sb.length() > 0) {
                sb.append(Constants.TERM);
            }
            sb.append(longValue);
        }
        return sb.toString();
    }

    public synchronized String e() {
        StringBuilder sb;
        sb = new StringBuilder();
        Iterator<g.a> it = this.o.iterator();
        while (it.hasNext()) {
            g.a next = it.next();
            if (sb.length() > 0) {
                sb.append(Constants.TERM);
            }
            sb.append(next.ordinal());
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void f() {
        com.google.ads.util.b.d("Ad clicked.");
        this.a.add(Long.valueOf(SystemClock.elapsedRealtime()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void g() {
        com.google.ads.util.b.d("Ad request loaded.");
        this.b = SystemClock.elapsedRealtime();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void h() {
        com.google.ads.util.b.d("Ad request before rendering.");
        this.c = SystemClock.elapsedRealtime();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void i() {
        com.google.ads.util.b.d("Ad request started.");
        this.d = SystemClock.elapsedRealtime();
        f++;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long j() {
        if (this.a.size() != this.e.size()) {
            return -1L;
        }
        return this.a.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String k() {
        if (this.a.isEmpty() || this.a.size() != this.e.size()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < this.a.size()) {
                if (i3 != 0) {
                    sb.append(Constants.TERM);
                }
                sb.append(Long.toString(this.e.get(i3).longValue() - this.a.get(i3).longValue()));
                i2 = i3 + 1;
            } else {
                return sb.toString();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String l() {
        if (this.a.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < this.a.size()) {
                if (i3 != 0) {
                    sb.append(Constants.TERM);
                }
                sb.append(Long.toString(this.a.get(i3).longValue() - this.b));
                i2 = i3 + 1;
            } else {
                return sb.toString();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long m() {
        return this.b - this.d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized long n() {
        return this.c - this.d;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long o() {
        return f;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long p() {
        return g;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void q() {
        com.google.ads.util.b.d("Ad request network error");
        g++;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String r() {
        return this.h;
    }

    public void a(String str) {
        com.google.ads.util.b.d("Prior ad identifier = " + str);
        this.h = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean s() {
        return this.j;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void t() {
        com.google.ads.util.b.d("Interstitial network error.");
        this.j = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean u() {
        return this.k;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void v() {
        com.google.ads.util.b.d("Interstitial no fill.");
        this.k = true;
    }

    public void w() {
        com.google.ads.util.b.d("Landing page dismissed.");
        this.e.add(Long.valueOf(SystemClock.elapsedRealtime()));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String x() {
        return this.l;
    }

    public void b(String str) {
        com.google.ads.util.b.d("Prior impression ticket = " + str);
        this.l = str;
    }

    public static long y() {
        if (i != -1) {
            return SystemClock.elapsedRealtime() - i;
        }
        i = SystemClock.elapsedRealtime();
        return 0L;
    }
}
