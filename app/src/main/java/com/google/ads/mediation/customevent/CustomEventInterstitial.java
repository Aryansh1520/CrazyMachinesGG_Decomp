package com.google.ads.mediation.customevent;

import android.app.Activity;
import com.google.ads.mediation.MediationAdRequest;

/* loaded from: classes.dex */
public interface CustomEventInterstitial {
    void requestInterstitialAd(CustomEventInterstitialListener customEventInterstitialListener, Activity activity, String str, String str2, MediationAdRequest mediationAdRequest);

    void showInterstitial();
}
