package com.mappn.sdk.uc;

import java.io.Serializable;

/* loaded from: classes.dex */
public interface GfanUCCallback extends Serializable {
    void onError(int i);

    void onSuccess(User user, int i);
}
