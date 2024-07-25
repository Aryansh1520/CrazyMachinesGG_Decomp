package com.mappn.sdk.pay;

import com.mappn.sdk.uc.User;

/* loaded from: classes.dex */
public interface GfanChargeCallback {
    void onError(User user);

    void onSuccess(User user);
}
