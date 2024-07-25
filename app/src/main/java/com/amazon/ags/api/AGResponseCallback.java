package com.amazon.ags.api;

import com.amazon.ags.api.RequestResponse;

/* loaded from: classes.dex */
public interface AGResponseCallback<T extends RequestResponse> {
    void onComplete(T t);
}
