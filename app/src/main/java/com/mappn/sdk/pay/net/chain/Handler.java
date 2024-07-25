package com.mappn.sdk.pay.net.chain;

import android.content.Context;

/* loaded from: classes.dex */
public abstract class Handler {
    protected static final int STATUS_SYNCED = 2;
    protected static final int STATUS_SYNCING = 1;
    protected static final int STATUS_UNSYNC = 0;
    private Handler a;
    protected Context mContext;
    protected android.os.Handler mHandler = new android.os.Handler();

    /* loaded from: classes.dex */
    public interface OnFinishListener {
        void onFinish();
    }

    public Handler(Context context) {
        this.mContext = context;
    }

    public Handler getSuccessor() {
        return this.a;
    }

    public abstract void handleRequest();

    public Handler setSuccessor(Handler handler) {
        this.a = handler;
        return this;
    }
}
