package com.amazon.inapp.purchasing;

import java.util.UUID;

/* loaded from: classes.dex */
abstract class Request {
    private final String _requestId = UUID.randomUUID().toString();

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getRequestId() {
        return this._requestId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Runnable getRunnable();
}
