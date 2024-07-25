package com.google.ads;

import com.google.ads.AdRequest;
import com.google.ads.g;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class k implements MediationInterstitialListener {
    private final h a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(h hVar) {
        this.a = hVar;
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onReceivedAd(MediationInterstitialAdapter<?, ?> adapter) {
        synchronized (this.a) {
            com.google.ads.util.a.a(adapter, this.a.i());
            if (this.a.c()) {
                com.google.ads.util.b.b("Got an onReceivedAd() callback after loadAdTask is done from an interstitial adapter. Ignoring callback.");
            } else {
                this.a.a(true, g.a.AD);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onFailedToReceiveAd(MediationInterstitialAdapter<?, ?> adapter, AdRequest.ErrorCode error) {
        synchronized (this.a) {
            com.google.ads.util.a.a(adapter, this.a.i());
            com.google.ads.util.b.a("Mediation adapter " + adapter.getClass().getName() + " failed to receive ad with error code: " + error);
            if (this.a.c()) {
                com.google.ads.util.b.b("Got an onFailedToReceiveAd() callback after loadAdTask is done from an interstitial adapter.  Ignoring callback.");
            } else {
                this.a.a(false, error == AdRequest.ErrorCode.NO_FILL ? g.a.NO_FILL : g.a.ERROR);
            }
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onPresentScreen(MediationInterstitialAdapter<?, ?> adapter) {
        synchronized (this.a) {
            this.a.j().a(this.a);
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onDismissScreen(MediationInterstitialAdapter<?, ?> adapter) {
        synchronized (this.a) {
            this.a.j().b(this.a);
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onLeaveApplication(MediationInterstitialAdapter<?, ?> adapter) {
        synchronized (this.a) {
            this.a.j().c(this.a);
        }
    }
}
