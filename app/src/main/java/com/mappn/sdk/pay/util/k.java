package com.mappn.sdk.pay.util;

import android.content.Context;
import com.mappn.sdk.common.net.ApiRequestListener;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class k implements ApiRequestListener {
    private /* synthetic */ Context a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(Context context) {
        this.a = context;
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public final void onError(int i, int i2) {
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public final void onSuccess(int i, Object obj) {
        PrefUtil.decreaseArriveCount(this.a);
        PrefUtil.confirmEnterPaymentPoint(this.a);
    }
}
