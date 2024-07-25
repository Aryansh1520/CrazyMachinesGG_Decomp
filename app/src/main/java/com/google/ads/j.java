package com.google.ads;

import com.google.ads.AdRequest;
import com.google.ads.g;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class j implements MediationBannerListener {
    private final h a;
    private boolean b;

    public j(h hVar) {
        this.a = hVar;
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onReceivedAd(MediationBannerAdapter<?, ?> adapter) {
        synchronized (this.a) {
            com.google.ads.util.a.a(adapter, this.a.i());
            try {
                this.a.a(adapter.getBannerView());
                if (!this.a.c()) {
                    this.b = false;
                    this.a.a(true, g.a.AD);
                } else {
                    this.b = true;
                    this.a.j().a(this.a, this.a.f());
                }
            } catch (Throwable th) {
                com.google.ads.util.b.b("Error while getting banner View from adapter (" + this.a.h() + "): ", th);
                if (!this.a.c()) {
                    this.a.a(false, g.a.EXCEPTION);
                }
            }
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onFailedToReceiveAd(MediationBannerAdapter<?, ?> adapter, AdRequest.ErrorCode error) {
        synchronized (this.a) {
            com.google.ads.util.a.a(adapter, this.a.i());
            com.google.ads.util.b.a("Mediation adapter " + adapter.getClass().getName() + " failed to receive ad with error code: " + error);
            if (!this.a.c()) {
                this.a.a(false, error == AdRequest.ErrorCode.NO_FILL ? g.a.NO_FILL : g.a.ERROR);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onPresentScreen(MediationBannerAdapter<?, ?> adapter) {
        synchronized (this.a) {
            this.a.j().a(this.a);
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onDismissScreen(MediationBannerAdapter<?, ?> adapter) {
        synchronized (this.a) {
            this.a.j().b(this.a);
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onLeaveApplication(MediationBannerAdapter<?, ?> adapter) {
        synchronized (this.a) {
            this.a.j().c(this.a);
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onClick(MediationBannerAdapter<?, ?> adapter) {
        synchronized (this.a) {
            com.google.ads.util.a.a(this.a.c());
            this.a.j().a(this.a, this.b);
        }
    }
}
