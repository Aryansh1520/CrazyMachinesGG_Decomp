package com.mappn.sdk.pay;

import android.app.Activity;
import android.content.Context;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.model.Order;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.statitistics.GfanPayAgent;
import com.mappn.sdk.uc.util.Constants;
import java.util.HashMap;

/* loaded from: classes.dex */
public class GfanPay {
    private static String a = GfanPay.class.getSimpleName();
    private static GfanPay b;
    private static ServiceConnector c;
    private Context d;

    private GfanPay(Context context) {
        this.d = context;
    }

    public static GfanPay getInstance(Context context) {
        if (b == null) {
            b = new GfanPay(context);
        }
        return b;
    }

    public static void submitLogin(Context context) {
        GfanPayAgent.onEvent(context, "login");
    }

    public static void submitNewRole(Context context) {
        GfanPayAgent.onEvent(context, "new_account");
    }

    public static void submitRegist(Context context) {
        GfanPayAgent.onEvent(context, "reg");
    }

    public void charge(GfanChargeCallback gfanChargeCallback) {
        BaseUtils.D(a, "charge()");
        if (c == null) {
            c = ServiceConnector.getInstance(this.d);
        }
        if (c.a()) {
            c.a(gfanChargeCallback);
        } else {
            c.doBindService(new c(this, gfanChargeCallback));
        }
    }

    public void chargeByType(String str, Activity activity, PaymentInfo paymentInfo, GfanChargeCallback gfanChargeCallback) {
        BaseUtils.D(a, "chargeByType()");
        HashMap hashMap = new HashMap();
        hashMap.put(Constants.PUSH_TYPE, str);
        hashMap.put("paymentInfo", paymentInfo);
        if (c == null) {
            c = ServiceConnector.getInstance(this.d);
        }
        if (c.a()) {
            c.a(hashMap, gfanChargeCallback);
        } else {
            c.doBindService(new d(this, hashMap, gfanChargeCallback));
        }
    }

    public void confirmPay(GfanConfirmOrderCallback gfanConfirmOrderCallback) {
        new ConfirmPayTask(this.d, gfanConfirmOrderCallback).execute(new Void[0]);
    }

    public void init() {
        ServiceConnector serviceConnector = ServiceConnector.getInstance(this.d);
        c = serviceConnector;
        serviceConnector.doBindService(null);
    }

    public void onDestroy() {
        if (c != null) {
            c.doUnbindService();
        }
    }

    public void pay(Order order, GfanPayCallback gfanPayCallback) {
        BaseUtils.D(a, "pay()");
        if (c == null) {
            c = ServiceConnector.getInstance(this.d);
        }
        if (c.a()) {
            c.a(order, gfanPayCallback);
        } else {
            c.doBindService(new b(this, order, gfanPayCallback));
        }
    }
}
