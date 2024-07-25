package com.amazon.inapp.purchasing;

import android.os.Handler;

/* loaded from: classes.dex */
class HandlerAdapter {
    private Handler _handler;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HandlerAdapter(Handler handler) {
        this._handler = handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean post(Runnable runnable) {
        return this._handler.post(runnable);
    }
}
