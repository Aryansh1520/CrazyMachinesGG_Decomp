package com.google.android.vending.licensing;

/* loaded from: classes.dex */
public class StrictPolicy implements Policy {
    private int mLastResponse = Policy.RETRY;

    @Override // com.google.android.vending.licensing.Policy
    public void processServerResponse(int response, ResponseData rawData) {
        this.mLastResponse = response;
    }

    @Override // com.google.android.vending.licensing.Policy
    public boolean allowAccess() {
        return this.mLastResponse == 256;
    }
}
