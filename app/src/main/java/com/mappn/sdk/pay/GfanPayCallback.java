package com.mappn.sdk.pay;

import com.mappn.sdk.pay.model.Order;
import com.mappn.sdk.uc.User;

/* loaded from: classes.dex */
public interface GfanPayCallback {
    void onError(User user);

    void onSuccess(User user, Order order);
}
