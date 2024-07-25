package com.google.ads;

/* loaded from: classes.dex */
public interface Ad {
    boolean isReady();

    void loadAd(AdRequest adRequest);

    void setAdListener(AdListener adListener);

    void stopLoading();
}
