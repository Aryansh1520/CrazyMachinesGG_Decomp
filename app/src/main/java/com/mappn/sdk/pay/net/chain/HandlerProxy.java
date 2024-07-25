package com.mappn.sdk.pay.net.chain;

import android.content.Context;
import com.mappn.sdk.pay.net.chain.Handler;

/* loaded from: classes.dex */
public class HandlerProxy extends Handler {
    private Handler a;

    public HandlerProxy(Context context) {
        super(context);
        this.a = new SyncChargeChannelHandler(context).setSuccessor(new SyncPayChannelHandler(context).setSuccessor(new SyncSmsInfoHandler(context).setSuccessor(new PostSmsPaymentHandler(context))));
    }

    public HandlerProxy(Context context, Handler.OnFinishListener onFinishListener) {
        super(context);
        this.a = new SyncChargeChannelHandler(context).setSuccessor(new SyncPayChannelHandler(context).setSuccessor(new SyncSmsInfoHandler(context, onFinishListener)));
    }

    @Override // com.mappn.sdk.pay.net.chain.Handler
    public void handleRequest() {
        this.a.handleRequest();
    }
}
