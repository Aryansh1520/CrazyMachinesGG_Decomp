package com.amazon.ags.api;

import com.amazon.ags.api.RequestResponse;

/* loaded from: classes.dex */
public interface GCResponseCallback<T extends RequestResponse> {
    void onFailure(int i, ErrorCode errorCode);

    void onSuccess(T t);
}
