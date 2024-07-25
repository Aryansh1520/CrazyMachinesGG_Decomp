package com.mappn.sdk.pay.net;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.DESUtil;
import com.mappn.sdk.pay.chargement.phonecard.CardInfo;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.pay.util.Utils;
import com.mappn.sdk.uc.UserDao;
import com.mappn.sdk.uc.UserVo;
import com.mokredit.payment.StringUtils;
import java.util.HashMap;

/* loaded from: classes.dex */
public class Api {
    public static final int ACTION_ARRIVE_PAY_POINT = 9;
    public static final int ACTION_CHARGE_G = 13;
    public static final int ACTION_CHARGE_PHONECARD = 10;
    public static final int ACTION_CHECK_USERNAME = 2;
    public static final int ACTION_CONFIRM_PAY_RESULT = 5;
    public static final int ACTION_GET_ALIPAY_ORDER_INFO = 15;
    public static final int ACTION_GET_MO9_ORDER_INFO = 21;
    public static final int ACTION_GET_NETBANK_ORDER_INFO = 19;
    public static final int ACTION_LOGIN = 0;
    public static final int ACTION_PAY = 3;
    public static final int ACTION_POST_SMS_PAYMENT = 8;
    public static final int ACTION_QUERY_APPNAME = 4;
    public static final int ACTION_QUERY_CHARGE_ALIPAY_RESULT = 16;
    public static final int ACTION_QUERY_CHARGE_CHANNEL = 17;
    public static final int ACTION_QUERY_CHARGE_MO9_RESULT = 22;
    public static final int ACTION_QUERY_CHARGE_NETBANK_RESULT = 20;
    public static final int ACTION_QUERY_CHARGE_PHONECARD_RESULT = 12;
    public static final int ACTION_QUERY_JIFENGQUAN_AND_G_BALANCE = 14;
    public static final int ACTION_QUERY_PAY_CHANNEL = 6;
    public static final int ACTION_QUERY_USER_PROFILE = 18;
    public static final int ACTION_REGISTER = 1;
    public static final int ACTION_SYNC_CARD_INFO = 11;
    public static final int ACTION_SYNC_SMS_INFO = 7;
    public static final String[] API_URLS = {"http://api.gfan.com/uc1/common/login_token", "http://api.gfan.com/uc1/common/register_token", "http://api.gfan.com/uc1/common/check_username", "http://api.gfan.com/sdk/pay/sdkPay", "http://api.gfan.com/sdk/pay/sdkAppname", "http://api.gfan.com/sdk/pay/sdkConfirm", "http://api.gfan.com/sdk/pay/getPayType", "http://api.gfan.com/sdk/pay/getMessagePayCode", "http://api.gfan.com/sdk/pay/getClientMessagePay", "http://api.gfan.com/sdk/pay/sdkPayPointArrive", "http://api.gfan.com/pay/szf/servlet/rechargeRequest", "http://api.gfan.com/pay/szf/getCardConfigServlet", "http://api.gfan.com/pay/szf/sdk/queryServlet", "http://api.gfan.com/pay/szf/servlet/businessProcess.do", "http://api.gfan.com/pay/szf/servlet/businessProcess.do", "http://api.gfan.com/pay/szf/servlet/businessProcess.do", "http://api.gfan.com/pay/szf/servlet/businessProcess.do", "http://api.gfan.com/pay/szf/servlet/businessProcess.do", "http://api.gfan.com/uc1/common/query_user_profile", "http://api.gfan.com/pay/szf/servlet/businessProcess.do", "http://api.gfan.com/pay/szf/servlet/businessProcess.do", "http://api.gfan.com/pay/szf/servlet/businessProcess.do", "http://api.gfan.com/pay/szf/servlet/businessProcess.do"};
    public static final String HOST = "http://api.gfan.com";
    public static final String HOST_CHARGE = "http://api.gfan.com/pay/szf/";
    public static final String HOST_PAY = "http://api.gfan.com/sdk/pay/";
    public static final String HOST_UC = "http://api.gfan.com/uc1/common/";

    public static void arrivePayPoint(Context context, ApiRequestListener apiRequestListener, PaymentInfo paymentInfo) {
        HashMap hashMap = new HashMap(5);
        hashMap.put("pay_name", paymentInfo.getOrder().getPayName());
        hashMap.put("pay_desc", paymentInfo.getOrder().getPayDesc());
        hashMap.put("app_key", BaseUtils.getAppKey(context));
        hashMap.put("sdk_version", Constants.VERSION);
        if (paymentInfo.getCpID() != null) {
            hashMap.put("channel_id", paymentInfo.getCpID());
        } else {
            hashMap.put("channel_id", StringUtils.EMPTY);
        }
        new ApiAsyncTask(context, 9, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void chargeG(Context context, ApiRequestListener apiRequestListener, long j, int i, String str, String str2) {
        HashMap hashMap = new HashMap(4);
        hashMap.put("uid", Long.valueOf(j));
        hashMap.put("gVolume", Integer.valueOf(i));
        hashMap.put("sdkVersion", Constants.VERSION);
        hashMap.put("productSign", str);
        if (str2 != null) {
            hashMap.put("sdkPublishChannelNo", str2);
        }
        new ApiAsyncTask(context, 13, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void chargePhoneCard(Context context, ApiRequestListener apiRequestListener, PaymentInfo paymentInfo, CardInfo cardInfo, String str, String str2) {
        HashMap hashMap = new HashMap(10);
        hashMap.put("user_id", Long.valueOf(paymentInfo.getUser().getUid()));
        hashMap.put(com.mappn.sdk.uc.util.Constants.DID, BaseUtils.getIMEI(context) + "&amp;" + BaseUtils.getMAC(context));
        hashMap.put(com.mappn.sdk.uc.util.Constants.PUSH_TYPE, "sdk");
        hashMap.put("pay_type", cardInfo.payType);
        hashMap.put("card_account", cardInfo.cardAccount);
        hashMap.put("card_password", cardInfo.cardPassword);
        hashMap.put("card_credit", Integer.valueOf(cardInfo.cardCredit));
        hashMap.put("product_sign", str);
        hashMap.put("sdk_version", Constants.VERSION);
        if (str2 != null) {
            hashMap.put("sdk_publish_channel_no", str2);
        }
        new ApiAsyncTask(context, 10, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void checkUsernameExist(Context context, ApiRequestListener apiRequestListener, String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("name", str);
        new ApiAsyncTask(context, 2, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void confirmPayResult(Context context, ApiRequestListener apiRequestListener, String str, String str2) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("order_id", str);
        hashMap.put("app_key", str2);
        new ApiAsyncTask(context, 5, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void getAliPayOrder(Context context, ApiRequestListener apiRequestListener, long j, int i, String str, String str2, String str3, String str4) {
        HashMap hashMap = new HashMap(6);
        hashMap.put("uid", Long.valueOf(j));
        hashMap.put(com.mappn.sdk.uc.util.Constants.DID, BaseUtils.getIMEI(context) + "&" + BaseUtils.getMAC(context));
        hashMap.put("money", Integer.valueOf(i));
        hashMap.put("productSign", str3);
        hashMap.put("productName", str);
        hashMap.put("productDesc", str2);
        if (str4 != null) {
            hashMap.put("sdkPublishChannelNo", str4);
        }
        hashMap.put("sdkVersion", Constants.VERSION);
        new ApiAsyncTask(context, 15, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void getNetBankOrder(Context context, ApiRequestListener apiRequestListener, long j, int i, String str, String str2, String str3, String str4) {
        HashMap hashMap = new HashMap(6);
        hashMap.put("uid", Long.valueOf(j));
        hashMap.put(com.mappn.sdk.uc.util.Constants.DID, BaseUtils.getIMEI(context) + "&" + BaseUtils.getMAC(context));
        hashMap.put("money", Integer.valueOf(i));
        hashMap.put("productSign", str3);
        hashMap.put("productName", str);
        hashMap.put("productDesc", str2);
        if (str4 != null) {
            hashMap.put("sdkPublishChannelNo", str4);
        }
        hashMap.put("sdkVersion", Constants.VERSION);
        new ApiAsyncTask(context, 19, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void getProductMo9Order(Context context, ApiRequestListener apiRequestListener, long j, int i, String str, String str2, String str3, String str4) {
        HashMap hashMap = new HashMap(6);
        hashMap.put("uid", Long.valueOf(j));
        hashMap.put(com.mappn.sdk.uc.util.Constants.DID, BaseUtils.getIMEI(context) + "&" + BaseUtils.getMAC(context));
        hashMap.put("money", Integer.valueOf(i));
        hashMap.put("productSign", str3);
        hashMap.put("productName", str);
        hashMap.put("productDesc", str2);
        if (str4 != null) {
            hashMap.put("sdkPublishChannelNo", str4);
        }
        hashMap.put("sdkVersion", Constants.VERSION);
        new ApiAsyncTask(context, 21, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void login(Context context, ApiRequestListener apiRequestListener, String str, String str2) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("username", str);
        hashMap.put(com.mappn.sdk.uc.util.Constants.GRANT_TYPE, str2);
        new ApiAsyncTask(context, 0, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void pay(Context context, ApiRequestListener apiRequestListener, PaymentInfo paymentInfo) {
        HashMap hashMap = new HashMap(9);
        hashMap.put("username", paymentInfo.getUser().getUserName());
        UserVo userByUid = UserDao.getUserByUid(context, paymentInfo.getUser().getUid());
        hashMap.put(com.mappn.sdk.uc.util.Constants.GRANT_TYPE, new DESUtil(context).getDesAndBase64String(userByUid == null ? PrefUtil.getUPW(context) : userByUid.getPassword()));
        hashMap.put("money", Integer.valueOf(paymentInfo.getOrder().getMoney()));
        hashMap.put("pay_name", paymentInfo.getOrder().getPayName());
        hashMap.put("pay_desc", paymentInfo.getOrder().getPayDesc());
        hashMap.put("pay_type", PaymentInfo.PAYTYPE_OVERAGE);
        hashMap.put("appkey", paymentInfo.getAppkey());
        hashMap.put("sdk_version", Constants.VERSION);
        if (TextUtils.isEmpty(paymentInfo.getOrder().getOrderID())) {
            Utils.generateOrderId(paymentInfo);
        } else if (BaseUtils.sDebug) {
            Log.e("SDK", "getOrderID = " + paymentInfo.getOrder().getOrderID());
        }
        hashMap.put("order_id", paymentInfo.getOrder().getOrderID());
        if (paymentInfo.getCpID() != null) {
            hashMap.put("channel_id", paymentInfo.getCpID());
        }
        new ApiAsyncTask(context, 3, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void postSmsPayment(Context context, ApiRequestListener apiRequestListener, String str, String str2, int i, String str3, String str4, long j) {
        HashMap hashMap = new HashMap(4);
        hashMap.put("appKey", str);
        hashMap.put("payPoint", str2);
        hashMap.put("money", Integer.valueOf(i));
        hashMap.put("spName", str3);
        if (str4 != null) {
            hashMap.put("channelId", str4);
        }
        if (j != -1) {
            hashMap.put("uid", Long.valueOf(j));
        }
        new ApiAsyncTask(context, 8, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void queryAliPayResult(Context context, ApiRequestListener apiRequestListener, String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("orderNo", str);
        new ApiAsyncTask(context, 16, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void queryAppname(Context context, ApiRequestListener apiRequestListener, String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("appkey", str);
        new ApiAsyncTask(context, 4, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void queryJifengquanAndGBalance(Context context, ApiRequestListener apiRequestListener, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("uid", Long.valueOf(j));
        new ApiAsyncTask(context, 14, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void queryM9Result(Context context, ApiRequestListener apiRequestListener, String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("orderNo", str);
        new ApiAsyncTask(context, 22, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void queryNetBankResult(Context context, ApiRequestListener apiRequestListener, String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("orderNo", str);
        new ApiAsyncTask(context, 20, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void queryPhoneCardChargeResult(Context context, ApiRequestListener apiRequestListener, String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("order_id", str);
        new ApiAsyncTask(context, 12, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void queryUserProfile(Context context, long j, ApiRequestListener apiRequestListener) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("uid", Long.valueOf(j));
        new ApiAsyncTask(context, 18, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void register(Context context, ApiRequestListener apiRequestListener, String str, String str2, String str3) {
        HashMap hashMap = new HashMap(3);
        hashMap.put("username", str);
        hashMap.put(com.mappn.sdk.uc.util.Constants.GRANT_TYPE, str2);
        hashMap.put(com.mappn.sdk.uc.util.Constants.KEY_USER_EMAIL, str3);
        new ApiAsyncTask(context, 1, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void syncCardInfo(Context context, ApiRequestListener apiRequestListener) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("local_version", -1);
        new ApiAsyncTask(context, 11, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void syncChargeChannel(Context context, ApiRequestListener apiRequestListener) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("action", "getRechargeChannels");
        String[] split = Constants.VERSION.split("\\.");
        hashMap.put("version", Double.valueOf(split.length > 2 ? Double.parseDouble(split[0] + "." + split[1]) : Double.parseDouble(Constants.VERSION)));
        new ApiAsyncTask(context, 17, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void syncPayChannel(Context context, ApiRequestListener apiRequestListener) {
        new ApiAsyncTask(context, 6, apiRequestListener, new HashMap(0)).execute(new Void[0]);
    }

    public static void syncSmsInfo(Context context, ApiRequestListener apiRequestListener) {
        HashMap hashMap = new HashMap(1);
        String smsInfoVersion = PrefUtil.getSmsInfoVersion(context);
        if (smsInfoVersion == null) {
            smsInfoVersion = StringUtils.EMPTY;
        }
        hashMap.put("createtime", smsInfoVersion);
        new ApiAsyncTask(context, 7, apiRequestListener, hashMap).execute(new Void[0]);
    }
}
