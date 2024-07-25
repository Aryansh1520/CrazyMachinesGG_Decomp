package com.mappn.sdk.pay.net.chain;

import android.content.Context;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.model.IType;
import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.net.chain.Handler;
import com.mappn.sdk.pay.util.PrefUtil;

/* loaded from: classes.dex */
public class SyncChargeChannelHandler extends Handler implements ApiRequestListener {
    private static int b;
    private Handler.OnFinishListener a;
    private final Integer c;

    public SyncChargeChannelHandler(Context context) {
        super(context);
        this.c = 0;
    }

    public SyncChargeChannelHandler(Context context, Handler.OnFinishListener onFinishListener) {
        super(context);
        this.c = 0;
        this.a = onFinishListener;
    }

    public static void init() {
        b = 0;
    }

    @Override // com.mappn.sdk.pay.net.chain.Handler
    public void handleRequest() {
        switch (b) {
            case 0:
                b = 1;
                Api.syncChargeChannel(this.mContext, this);
                return;
            case 1:
                new Thread(new d(this)).start();
                return;
            case 2:
                if (getSuccessor() != null) {
                    getSuccessor().handleRequest();
                    return;
                } else {
                    if (this.a != null) {
                        this.a.onFinish();
                        return;
                    }
                    return;
                }
            default:
                return;
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        b = 0;
        synchronized (this.c) {
            this.c.notifyAll();
        }
        if (getSuccessor() != null) {
            getSuccessor().handleRequest();
        } else if (this.a != null) {
            this.a.onFinish();
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onSuccess(int i, Object obj) {
        BaseUtils.D("SyncChargeChannelHandler", "Type" + PrefUtil.getDefaultChargeType(this.mContext));
        PrefUtil.syncChargeChannels(this.mContext, (String[]) obj);
        IType[] availableChargeType = PrefUtil.getAvailableChargeType(this.mContext, true);
        int length = availableChargeType.length;
        for (int i2 = 0; i2 < length; i2++) {
            BaseUtils.D("SyncChargeChannelHandler", "Type " + i2 + " :" + availableChargeType[i2]);
        }
        b = 2;
        synchronized (this.c) {
            this.c.notifyAll();
        }
        if (getSuccessor() != null) {
            getSuccessor().handleRequest();
        } else if (this.a != null) {
            this.a.onFinish();
        }
    }
}
