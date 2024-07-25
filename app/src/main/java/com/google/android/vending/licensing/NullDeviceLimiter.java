package com.google.android.vending.licensing;

/* loaded from: classes.dex */
public class NullDeviceLimiter implements DeviceLimiter {
    @Override // com.google.android.vending.licensing.DeviceLimiter
    public int isDeviceAllowed(String userId) {
        return 256;
    }
}
