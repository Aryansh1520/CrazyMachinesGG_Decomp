package com.amazon.ags.client;

import com.amazon.ags.api.AGHandleStatus;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.RequestResponse;

/* loaded from: classes.dex */
public class GCResponseHandleImpl<T extends RequestResponse> implements AGResponseHandle<T> {
    private final Object[] userData;
    private T response = null;
    private AGHandleStatus status = AGHandleStatus.WAITING;
    private AGResponseCallback<T> callback = null;

    public GCResponseHandleImpl(Object[] userData) {
        this.userData = userData;
    }

    @Override // com.amazon.ags.api.AGResponseHandle
    public synchronized AGHandleStatus getStatus() {
        return this.status;
    }

    @Override // com.amazon.ags.api.AGResponseHandle
    public synchronized T getResponse() {
        return this.response;
    }

    @Override // com.amazon.ags.api.AGResponseHandle
    public void setCallback(AGResponseCallback<T> callback) {
        synchronized (this) {
            boolean existingCallback = this.callback != null;
            this.callback = callback;
            if (!existingCallback && this.response != null) {
                callCallback();
            }
        }
    }

    public synchronized void setResponse(T requestResponse) {
        this.response = requestResponse;
        this.response.setUserData(this.userData);
        if (this.response.isError()) {
            this.status = AGHandleStatus.ERROR;
        } else {
            this.status = AGHandleStatus.SUCCESS;
        }
        callCallback();
    }

    private void callCallback() {
        if (this.callback != null && this.response != null && this.callback != null) {
            this.callback.onComplete(this.response);
        }
    }
}
