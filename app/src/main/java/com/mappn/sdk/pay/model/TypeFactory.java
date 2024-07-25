package com.mappn.sdk.pay.model;

import android.content.Context;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.Utils;
import java.lang.ref.SoftReference;
import java.util.HashMap;

/* loaded from: classes.dex */
public class TypeFactory {
    public static final String TYPE_CHARGE_ALIPAY = "alipay";
    public static final String TYPE_CHARGE_G = "g";
    public static final String TYPE_CHARGE_MO9 = "mo9";
    public static final String TYPE_CHARGE_NETBANK = "netbank";
    public static final String TYPE_CHARGE_PHONECARD = "phonecard";
    public static final String TYPE_PAY_JIFENGQUAN = "jifengquan";
    public static final String TYPE_PAY_SMS = "sms";
    private static HashMap a;

    private static String a(String str) {
        if (TYPE_PAY_JIFENGQUAN.equals(str)) {
            return Constants.TEXT_PAYMENT_TYPE_JIFENG;
        }
        if ("sms".equals(str)) {
            return String.format(Constants.TEXT_PAYMENT_TYPE_SMS, Integer.valueOf(Utils.getSmsPayment()));
        }
        if ("alipay".equals(str)) {
            return Constants.TEXT_PAYMENT_TYPE_ALIPAY;
        }
        if (TYPE_CHARGE_G.equals(str)) {
            return Constants.TEXT_PAYMENT_TYPE_G;
        }
        if (TYPE_CHARGE_PHONECARD.equals(str)) {
            return Constants.TEXT_PAYMENT_TYPE_PHONECARD;
        }
        if (TYPE_CHARGE_NETBANK.equals(str)) {
            return Constants.TEXT_PAYMENT_TYPE_NETBANK;
        }
        if ("mo9".equals(str)) {
            return Constants.TEXT_PAYMENT_TYPE_MO9;
        }
        throw new IllegalArgumentException("type not supported. " + str);
    }

    private static String b(String str) {
        if ("alipay".equals(str)) {
            return Constants.TEXT_CHARGE_ALIPAY;
        }
        if (TYPE_CHARGE_G.equals(str)) {
            return Constants.TEXT_CHARGE_G;
        }
        if (TYPE_CHARGE_PHONECARD.equals(str)) {
            return Constants.TEXT_CHARGE_PHONECARD;
        }
        if (TYPE_CHARGE_NETBANK.equals(str)) {
            return Constants.TEXT_CHARGE_NETBANK;
        }
        if ("mo9".equals(str)) {
            return Constants.TEXT_CHARGE_MO9;
        }
        return null;
    }

    public static void clear() {
        if (a != null) {
            a = null;
        }
    }

    public static synchronized IType factory(String str, Context context) {
        IType payType;
        synchronized (TypeFactory.class) {
            if (a == null) {
                a = new HashMap(6);
            }
            if (!a.containsKey(str) || (payType = (IType) ((SoftReference) a.get(str)).get()) == null) {
                payType = (TYPE_PAY_JIFENGQUAN.equals(str) || "sms".equals(str)) ? new PayType(str, b(str), a(str), getTypeIconFileName(str), context) : new ChargeType(str, b(str), a(str), getTypeIconFileName(str), context);
                a.put(str, new SoftReference(payType));
            }
        }
        return payType;
    }

    public static String getTypeIconFileName(String str) {
        boolean isHdpi = Utils.isHdpi();
        BaseUtils.D("test", "getTypeIconFileName isHdpi :" + isHdpi);
        if (TYPE_PAY_JIFENGQUAN.equals(str)) {
            return isHdpi ? Constants.RES_TYPE_GFAN_ICON_HDPI : Constants.RES_TYPE_GFAN_ICON;
        }
        if ("sms".equals(str)) {
            return isHdpi ? Constants.RES_TYPE_SMS_ICON_HDPI : Constants.RES_TYPE_SMS_ICON;
        }
        if ("alipay".equals(str)) {
            if (isHdpi) {
                BaseUtils.D("test", "hdpi");
                return Constants.RES_TYPE_ALIPAY_ICON_HDPI;
            }
            BaseUtils.D("test", "normal");
            return Constants.RES_TYPE_ALIPAY_ICON;
        }
        if (TYPE_CHARGE_G.equals(str)) {
            return isHdpi ? Constants.RES_TYPE_G_ICON_HDPI : Constants.RES_TYPE_G_ICON;
        }
        if (TYPE_CHARGE_PHONECARD.equals(str)) {
            return isHdpi ? Constants.RES_TYPE_PHONECARD_ICON_HDPI : Constants.RES_TYPE_PHONECARD_ICON;
        }
        if (TYPE_CHARGE_NETBANK.equals(str)) {
            return isHdpi ? Constants.RES_TYPE_NETBANK_ICON_HDPI : Constants.RES_TYPE_NETBANK_ICON;
        }
        if (!"mo9".equals(str)) {
            if (TYPE_CHARGE_G.equals(str)) {
                return isHdpi ? Constants.RES_TYPE_GET_G_ICON_HDPI : Constants.RES_TYPE_GET_G_ICON;
            }
            throw new IllegalArgumentException(Constants.ERROR_TYPE_NOT_SUPPORTED);
        }
        if (isHdpi) {
            BaseUtils.D("test", "hdpi");
            return Constants.RES_TYPE_MO9_ICON_HDPI;
        }
        BaseUtils.D("test", "normal");
        return Constants.RES_TYPE_MO9_ICON;
    }
}
