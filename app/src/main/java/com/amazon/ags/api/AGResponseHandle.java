package com.amazon.ags.api;

import com.amazon.ags.api.RequestResponse;

/* loaded from: classes.dex */
public interface AGResponseHandle<T extends RequestResponse> {
    T getResponse();

    AGHandleStatus getStatus();

    void setCallback(AGResponseCallback<T> aGResponseCallback);
}
