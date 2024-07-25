package com.mappn.sdk.pay.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.mappn.sdk.pay.model.IType;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.net.Api;
import com.mokredit.payment.StringUtils;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class PrefUtil {
    public static final String P_ARRIVE_COUNT = "pref.com.mappn.sdk.arrive";
    public static final String P_AVAILABLE_CHARGE_TYPE = "pref.com.mappn.sdk.availableChargeType";
    public static final String P_AVAILABLE_PAY_TYPE = "pref.com.mappn.sdk.availablePayType";
    public static final String P_DEFAULT_CHARGE_TYPE = "pref.com.mappn.sdk.defaultChargeType";
    public static final String P_LOGIN_FLAG = "pref.com.mappn.sdk.islogin";
    public static final String P_LOGIN_TIME = "pref.com.mappn.sdk.logintime";
    public static final String P_RESULT = "pref.com.mappn.sdk.result";
    public static final String P_SMSINFO = "pref.com.mappn.sdk.smsinfo";
    public static final String P_SMSINFO_VERSION = "pref.com.mappn.sdk.smsinfo.version";
    public static final String P_UID = "pref.com.mappn.sdk.uid";
    public static final String P_USERNAME = "pref.com.mappn.sdk.username";
    public static SharedPreferences sPref = null;

    private static synchronized void a(Context context) {
        synchronized (PrefUtil.class) {
            if (sPref == null) {
                sPref = context.getSharedPreferences("mappn.sdk.pref", 0);
            }
        }
    }

    private static synchronized void a(Context context, int i) {
        synchronized (PrefUtil.class) {
            if (sPref == null) {
                a(context);
            }
            sPref.edit().putInt(P_ARRIVE_COUNT, i).commit();
        }
    }

    public static void clearArriveCount(Context context) {
        a(context, 0);
    }

    public static void clearPayedAmount(Context context) {
        if (sPref == null) {
            a(context);
        }
        sPref.edit().remove(Utils.getPaymentInfo().getOrder().getPayName() + "_payedAmount").commit();
    }

    public static void confirmEnterPaymentPoint(Context context) {
        if (getArriveCount(context) > 0) {
            Api.arrivePayPoint(context, new k(context), Utils.getPaymentInfo());
        }
    }

    public static synchronized void decreaseArriveCount(Context context) {
        synchronized (PrefUtil.class) {
            int arriveCount = getArriveCount(context);
            if (arriveCount > 0) {
                a(context, arriveCount - 1);
            }
        }
    }

    public static int getArriveCount(Context context) {
        if (sPref == null) {
            a(context);
        }
        return sPref.getInt(P_ARRIVE_COUNT, 0);
    }

    public static IType[] getAvailableChargeType(Context context, boolean z) {
        String string;
        if (sPref == null) {
            a(context);
        }
        if (!sPref.contains(P_AVAILABLE_CHARGE_TYPE) || (string = sPref.getString(P_AVAILABLE_CHARGE_TYPE, null)) == null) {
            return new IType[]{TypeFactory.factory("alipay", context), TypeFactory.factory(TypeFactory.TYPE_CHARGE_PHONECARD, context), TypeFactory.factory("mo9", context)};
        }
        if (!z && string.contains(TypeFactory.TYPE_CHARGE_G)) {
            string = string.indexOf(",g") == -1 ? string.replace(TypeFactory.TYPE_CHARGE_G, StringUtils.EMPTY) : string.replace(",g", StringUtils.EMPTY);
        }
        String[] split = string.split(Constants.TERM);
        IType[] iTypeArr = new IType[split.length];
        int length = split.length;
        for (int i = 0; i < length; i++) {
            iTypeArr[i] = TypeFactory.factory(split[i], context);
        }
        return iTypeArr;
    }

    public static ArrayList getAvailablePayType(Context context, String str) {
        ArrayList arrayList;
        String string;
        if (sPref == null) {
            a(context);
        }
        if (!sPref.contains(P_AVAILABLE_PAY_TYPE) || (string = sPref.getString(P_AVAILABLE_PAY_TYPE, null)) == null) {
            arrayList = new ArrayList();
            if (PaymentInfo.PAYTYPE_OVERAGE.equals(str) || "all".equals(str)) {
                arrayList.add(TypeFactory.factory(TypeFactory.TYPE_PAY_JIFENGQUAN, context));
            }
            if ("sms".equals(str) || "all".equals(str)) {
                arrayList.add(TypeFactory.factory("sms", context));
            }
        } else {
            String[] split = string.split(Constants.TERM);
            arrayList = new ArrayList();
            int length = split.length;
            for (int i = 0; i < length; i++) {
                if ((PaymentInfo.PAYTYPE_OVERAGE.equals(str) && TypeFactory.TYPE_PAY_JIFENGQUAN.equals(split[i])) || (("sms".equals(str) && "sms".equals(split[i])) || "all".equals(str))) {
                    arrayList.add(TypeFactory.factory(split[i], context));
                }
            }
        }
        return arrayList;
    }

    public static String getDefaultChargeType(Context context) {
        if (sPref == null) {
            a(context);
        }
        return sPref.getString(P_DEFAULT_CHARGE_TYPE, null);
    }

    public static SharedPreferences.Editor getEditor() {
        if (sPref == null) {
            return null;
        }
        return sPref.edit();
    }

    public static int getPayedAmount(Context context) {
        String str = Utils.getPaymentInfo().getOrder().getPayName() + "_payedAmount";
        if (sPref == null) {
            a(context);
        }
        return sPref.getInt(str, 0);
    }

    public static String getSmsInfo(Context context) {
        if (sPref == null) {
            a(context);
        }
        return sPref.getString(P_SMSINFO, null);
    }

    public static String getSmsInfoVersion(Context context) {
        if (sPref == null) {
            a(context);
        }
        return sPref.getString(P_SMSINFO_VERSION, null);
    }

    public static String getUPW(Context context) {
        if (sPref == null) {
            a(context);
        }
        return sPref.getString("pref.upw", StringUtils.EMPTY);
    }

    public static synchronized void increaseArriveCount(Context context) {
        synchronized (PrefUtil.class) {
            a(context, getArriveCount(context) + 1);
        }
    }

    public static boolean isLogin(Context context) {
        if (sPref == null) {
            a(context);
        }
        return sPref.getBoolean(P_LOGIN_FLAG, false);
    }

    public static void setDefaultChargeType(Context context, String str) {
        if (sPref == null) {
            a(context);
        }
        SharedPreferences.Editor edit = sPref.edit();
        if (str == null) {
            edit.remove(P_DEFAULT_CHARGE_TYPE);
        } else {
            edit.putString(P_DEFAULT_CHARGE_TYPE, str);
        }
        edit.commit();
    }

    public static void setLoginFlag(Context context, boolean z) {
        a(context);
        sPref.edit().putBoolean(P_LOGIN_FLAG, z).commit();
    }

    public static void setPayedAmount(Context context, int i) {
        if (sPref == null) {
            a(context);
        }
        sPref.edit().putInt(Utils.getPaymentInfo().getOrder().getPayName() + "_payedAmount", getPayedAmount(context) + i).commit();
    }

    public static void setSmsInfo(Context context, String str) {
        if (sPref == null) {
            a(context);
        }
        sPref.edit().putString(P_SMSINFO, str).commit();
    }

    public static void setSmsInfoVersion(Context context, String str) {
        if (sPref == null) {
            a(context);
        }
        sPref.edit().putString(P_SMSINFO_VERSION, str).commit();
    }

    public static boolean supportChargeType(Context context, String str) {
        for (IType iType : getAvailableChargeType(context, true)) {
            if (str.equals(iType.getId())) {
                return true;
            }
        }
        return false;
    }

    public static synchronized void syncChargeChannels(Context context, String[] strArr) {
        synchronized (PrefUtil.class) {
            if (sPref == null) {
                a(context);
            }
            int length = strArr.length;
            for (int i = 0; i < length; i++) {
                if (TypeFactory.TYPE_CHARGE_G.equals(strArr[i])) {
                    strArr[i] = strArr[length - 1];
                    strArr[length - 1] = TypeFactory.TYPE_CHARGE_G;
                } else if ("alipay".equals(strArr[i])) {
                    strArr[i] = strArr[0];
                    strArr[0] = "alipay";
                }
            }
            StringBuilder sb = new StringBuilder();
            for (String str : strArr) {
                sb.append(str).append(Constants.TERM);
            }
            if (sb.indexOf(Constants.TERM) != -1) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sPref.edit().putString(P_AVAILABLE_CHARGE_TYPE, sb.toString()).commit();
        }
    }

    public static synchronized void syncPayChannels(Context context, String str) {
        synchronized (PrefUtil.class) {
            if (sPref == null) {
                a(context);
            }
            if (str.indexOf("sms") > str.indexOf(str)) {
                String[] split = str.split(Constants.TERM);
                int length = split.length;
                for (int i = 0; i < length; i++) {
                    if ("sms".equals(split[i])) {
                        split[i] = split[length - 1];
                        split[length - 1] = "sms";
                    } else if (TypeFactory.TYPE_PAY_JIFENGQUAN.equals(split[i])) {
                        split[i] = split[0];
                        split[0] = TypeFactory.TYPE_PAY_JIFENGQUAN;
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (String str2 : split) {
                    sb.append(str2).append(Constants.TERM);
                }
                if (sb.indexOf(Constants.TERM) != -1) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                str = sb.toString();
            }
            sPref.edit().putString(P_AVAILABLE_PAY_TYPE, str).commit();
        }
    }
}
