package com.amazon.ags.api;

import com.amazon.ags.api.RequestResponse;

/* loaded from: classes.dex */
public interface GCResponseHandle<T extends RequestResponse> {
    T getResponse();

    boolean isDone();
}
