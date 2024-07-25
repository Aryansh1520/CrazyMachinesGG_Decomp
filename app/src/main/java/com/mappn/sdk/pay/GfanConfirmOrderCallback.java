package com.mappn.sdk.pay;

import com.mappn.sdk.pay.model.Order;

/* loaded from: classes.dex */
public interface GfanConfirmOrderCallback {
    void onExist(Order order);

    void onNotExist();
}
