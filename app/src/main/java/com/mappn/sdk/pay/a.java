package com.mappn.sdk.pay;

import android.content.Context;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.database.PayDB;
import com.mappn.sdk.pay.database.PayVo;
import com.mappn.sdk.pay.model.Order;
import java.util.HashMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class a implements ApiRequestListener {
    private /* synthetic */ ConfirmPayTask a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(ConfirmPayTask confirmPayTask) {
        this.a = confirmPayTask;
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public final void onError(int i, int i2) {
        BaseUtils.I("payThread", "confirmPayResult is error and statusCode :" + i2);
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public final void onSuccess(int i, Object obj) {
        String str;
        Context context;
        GfanConfirmOrderCallback gfanConfirmOrderCallback;
        GfanConfirmOrderCallback gfanConfirmOrderCallback2;
        if (obj == null) {
            return;
        }
        BaseUtils.I("payThread", "confirmPayResult is success");
        String obj2 = obj.toString();
        str = ConfirmPayTask.c;
        if (str.equals(obj2)) {
            BaseUtils.I("payThread", "订单存在");
        }
        context = this.a.a;
        PayVo queryPayVo = PayDB.queryPayVo(context);
        if (queryPayVo != null) {
            Order order = (Order) ((HashMap) queryPayVo.getPayParameters()).get("com.mappn.sdk.order");
            BaseUtils.I("payThread", "漏单  mOrderId :" + order.getOrderID());
            gfanConfirmOrderCallback2 = this.a.b;
            gfanConfirmOrderCallback2.onExist(order);
        } else {
            gfanConfirmOrderCallback = this.a.b;
            gfanConfirmOrderCallback.onNotExist();
        }
        BaseUtils.I("payThread", "没有漏单 :");
    }
}
