package com.mappn.sdk.pay;

import android.os.Handler;
import android.os.Message;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.model.Order;
import com.mappn.sdk.uc.User;
import java.util.HashMap;

/* loaded from: classes.dex */
final class i extends Handler {
    private /* synthetic */ ServiceConnector a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(ServiceConnector serviceConnector) {
        this.a = serviceConnector;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        String str;
        GfanChargeCallback gfanChargeCallback;
        String str2;
        GfanChargeCallback gfanChargeCallback2;
        String str3;
        GfanPayCallback gfanPayCallback;
        String str4;
        GfanPayCallback gfanPayCallback2;
        String str5;
        e eVar;
        e eVar2;
        switch (message.what) {
            case 0:
                str5 = ServiceConnector.a;
                BaseUtils.D(str5, "BIND_SUCCESS");
                eVar = this.a.j;
                if (eVar != null) {
                    eVar2 = this.a.j;
                    eVar2.a();
                    return;
                }
                return;
            case 1:
                str4 = ServiceConnector.a;
                BaseUtils.D(str4, "PAY_SUCCESS");
                HashMap hashMap = (HashMap) message.obj;
                Order order = (Order) hashMap.get("com.mappn.sdk.order");
                User user = (User) hashMap.get(GfanPayService.EXTRA_KEY_USER);
                gfanPayCallback2 = this.a.h;
                gfanPayCallback2.onSuccess(user, order);
                return;
            case 2:
                str2 = ServiceConnector.a;
                BaseUtils.D(str2, "CHARGE_SUCCESS");
                User user2 = (User) ((HashMap) message.obj).get(GfanPayService.EXTRA_KEY_USER);
                gfanChargeCallback2 = this.a.i;
                gfanChargeCallback2.onSuccess(user2);
                return;
            case 3:
                str3 = ServiceConnector.a;
                BaseUtils.D(str3, "PAY_ERROR");
                User user3 = (User) ((HashMap) message.obj).get(GfanPayService.EXTRA_KEY_USER);
                gfanPayCallback = this.a.h;
                gfanPayCallback.onError(user3);
                return;
            case 4:
                str = ServiceConnector.a;
                BaseUtils.D(str, "CHARGE_ERROR");
                User user4 = (User) ((HashMap) message.obj).get(GfanPayService.EXTRA_KEY_USER);
                gfanChargeCallback = this.a.i;
                gfanChargeCallback.onError(user4);
                return;
            default:
                return;
        }
    }
}
