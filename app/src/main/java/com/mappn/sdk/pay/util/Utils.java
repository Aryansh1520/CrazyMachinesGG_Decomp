package com.mappn.sdk.pay.util;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mappn.sdk.common.codec.digest.DigestUtils;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.SecurityUtil;
import com.mappn.sdk.pay.chargement.phonecard.CardsVerifications;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.pay.payment.sms.SimCardNotSupportException;
import com.mappn.sdk.pay.payment.sms.SmsInfo;
import com.mappn.sdk.pay.payment.sms.SmsInfos;
import com.mokredit.payment.StringUtils;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Utils {
    private static PaymentInfo a;
    private static CardsVerifications b;
    private static boolean c;
    private static SmsInfos d;

    public static void CheckSimCardSupprotInfo(Context context) {
        if (isAirMode(context)) {
            throw new SimCardNotSupportException(Constants.TEXT_PAY_SMS_ERROR_AIR_MODE);
        }
        if (5 != ((TelephonyManager) context.getSystemService("phone")).getSimState()) {
            throw new SimCardNotSupportException(Constants.TEXT_PAY_SMS_ERROR_NO_SIM);
        }
    }

    private static String a() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress nextElement = inetAddresses.nextElement();
                    if (!nextElement.isLoopbackAddress()) {
                        return nextElement.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
        }
        return StringUtils.EMPTY;
    }

    private static String a(Context context) {
        String simSerialNumber = ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
        if (TextUtils.isEmpty(simSerialNumber)) {
            throw new SimCardNotSupportException(Constants.TEXT_PAY_SMS_ERROR_ABROAD);
        }
        return simSerialNumber;
    }

    public static void clearSmsInfos() {
        if (d != null) {
            d = null;
        }
    }

    public static TextView generateBorderView(Context context) {
        TextView textView = new TextView(context);
        textView.setBackgroundResource(R.drawable.divider_horizontal_dark);
        return textView;
    }

    public static String generateJsonRequestBody(Context context, Object obj) {
        if (obj == null || !(obj instanceof HashMap)) {
            return StringUtils.EMPTY;
        }
        HashMap hashMap = (HashMap) obj;
        JSONObject jSONObject = new JSONObject();
        for (String str : hashMap.keySet()) {
            try {
                if (hashMap.get(str) instanceof List) {
                    JSONArray jSONArray = new JSONArray();
                    List list = (List) hashMap.get(str);
                    for (Object obj2 : list) {
                        if (obj2 instanceof HashMap) {
                            HashMap hashMap2 = (HashMap) obj2;
                            JSONObject jSONObject2 = new JSONObject();
                            for (String str2 : hashMap2.keySet()) {
                                if (hashMap2.get(str2) == null) {
                                    jSONObject2.put(str2, JSONObject.NULL);
                                } else {
                                    jSONObject2.put(str2, hashMap2.get(str2));
                                }
                            }
                            jSONArray.put(jSONObject2);
                        } else {
                            jSONArray.put(obj2);
                        }
                    }
                    if (list.size() == 0) {
                        jSONObject.put(str, JSONObject.NULL);
                    } else {
                        jSONObject.put(str, jSONArray);
                    }
                } else if (hashMap.get(str) instanceof HashMap) {
                    HashMap hashMap3 = (HashMap) hashMap.get(str);
                    JSONObject jSONObject3 = new JSONObject();
                    for (String str3 : hashMap3.keySet()) {
                        if (hashMap3.get(str3) == null) {
                            jSONObject3.put(str3, JSONObject.NULL);
                        } else {
                            jSONObject3.put(str3, hashMap3.get(str3));
                        }
                    }
                    if (jSONObject3.length() == 0) {
                        jSONObject.put(str, JSONObject.NULL);
                    } else {
                        jSONObject.put(str, jSONObject3);
                    }
                } else {
                    jSONObject.put(str, hashMap.get(str));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return StringUtils.EMPTY;
            }
        }
        return jSONObject.toString();
    }

    public static void generateOrderId(PaymentInfo paymentInfo) {
        String str;
        try {
            str = URLEncoder.encode(paymentInfo.getUser().getUserName(), "UTF-8") + paymentInfo.getAppkey() + URLEncoder.encode(paymentInfo.getOrder().getPayName(), "UTF-8") + System.currentTimeMillis() + a();
        } catch (UnsupportedEncodingException e) {
            str = StringUtils.EMPTY;
        }
        paymentInfo.getOrder().setOrderID(DigestUtils.md5Hex(str));
    }

    public static String getAppkey(Context context) {
        return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.get("gfan_pay_appkey").toString();
    }

    public static String getBodyString(int i, HttpResponse httpResponse) {
        String str = null;
        try {
            str = (11 == i || 10 == i || 12 == i || i == 0 || 1 == i || 2 == i || 4 == i || 3 == i || 6 == i || 7 == i || 18 == i) ? EntityUtils.toString(httpResponse.getEntity(), "UTF-8") : SecurityUtil.decryptHttpChargeAlipayBody(EntityUtils.toByteArray(httpResponse.getEntity()));
        } catch (IOException e) {
        } catch (ParseException e2) {
        }
        return str;
    }

    public static CardsVerifications getCardsVerifications() {
        return b;
    }

    public static String getCompany(Context context) {
        String a2 = a(context);
        return a2.length() < 6 ? SmsInfo.COMPANY_CODE_CHINAMOBILE : a2.substring(4, 6);
    }

    public static String getCpID(Context context) {
        return context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.get("gfan_cpid").toString();
    }

    public static double getDouble(String str) {
        return Double.parseDouble(str);
    }

    public static String getErrorMsg(int i) {
        switch (i) {
            case -2:
                return Constants.ERROR_ACCOUNT_PARSER;
            case -1:
                return Constants.ERROR_ACCOUNT_NETWORK;
            case 211:
                return Constants.ERROR_ACCOUNT_USERNAME_NOT_EXIST;
            case 212:
                return Constants.ERROR_ACCOUNT_PASSWORD;
            case 213:
                return Constants.ERROR_ACCOUNT_USERNAME_INVALIDATE;
            case 214:
                return Constants.ERROR_ACCOUNT_USERNAME_HAVE_EXIST;
            case 215:
                return Constants.ERROR_ACCOUNT_EMAIL_WRONG_FORMAT;
            case 216:
                return Constants.ERROR_ACCOUNT_EMAIL_HAVE_EXIST;
            case 217:
                return Constants.ERROR_ACCOUNT_PASSWORD_INVALIDATE;
            case 421:
                return Constants.ERROR_ACCOUNT_USERAGENT_PARAM_EMPTY;
            case 422:
                return Constants.ERROR_ACCOUNT_XML_PARSE_FAILED;
            case 423:
                return Constants.ERROR_ACCOUNT_XML_PARSE_FAILED2;
            case Constants.ERROR_CODE_CHANEL_NOT_EXIST /* 424 */:
                return Constants.ERROR_ACCOUNT_CHANEL_NOT_EXIST;
            case Constants.ERROR_CODE_ARG_OUT_OF_SCROPE /* 425 */:
                return Constants.ERROR_ACCOUNT_ARG_OUT_OF_SCROPE;
            case Constants.ERROR_CODE_APPKEY_WRONG /* 426 */:
                return Constants.ERROR_ACCOUNT_APPKEY_WRONG;
            case 427:
                return Constants.ERROR_ACCOUNT_REQUEST_DECODE_FAILED;
            case 500:
                return "抱歉，未知服务器端错误";
            default:
                return "抱歉，未知服务器端错误";
        }
    }

    public static int getInt(int i, String str) {
        return getInt(i, str, 0);
    }

    public static int getInt(int i, String str, int i2) {
        if (str == null) {
            return i2;
        }
        try {
            return (int) Long.parseLong(str.trim(), i);
        } catch (NumberFormatException e) {
            return i2;
        }
    }

    public static int getInt(String str) {
        return getInt(10, str);
    }

    public static int getInt(String str, int i) {
        return getInt(10, str, i);
    }

    public static String getJsonRequestBody(HashMap hashMap) {
        if (hashMap == null) {
            return StringUtils.EMPTY;
        }
        JSONObject jSONObject = new JSONObject();
        for (String str : hashMap.keySet()) {
            try {
                jSONObject.put(str, hashMap.get(str));
            } catch (JSONException e) {
                e.printStackTrace();
                return StringUtils.EMPTY;
            }
        }
        return jSONObject.toString();
    }

    public static long getLong(String str) {
        if (str == null) {
            return 0L;
        }
        try {
            return Long.parseLong(str.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static PaymentInfo getPaymentInfo() {
        return a;
    }

    public static String getProvinceCode(Context context) {
        String a2 = a(context);
        return a2.length() < 10 ? SmsInfo.COMPANY_CODE_CHINAUNICOM : a2.substring(8, 10);
    }

    public static SmsInfos getSmsInfos() {
        return d;
    }

    public static int getSmsPayment() {
        return getPaymentInfo().getOrder().getMoney() / 5;
    }

    public static String getUserAgent(Context context) {
        return "packageName=" + context.getPackageName() + ",channelID=1,did=" + (BaseUtils.getIMEI(context) + "&" + BaseUtils.getMAC(context));
    }

    public static String getXmlRequestBody(HashMap hashMap, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("<request");
        if (hashMap.containsKey("local_version")) {
            sb.append(" local_version=\"" + hashMap.get("local_version") + "\" ");
            hashMap.remove("local_version");
        }
        sb.append(">");
        for (String str : hashMap.keySet()) {
            sb.append("<").append(str).append(">");
            sb.append(hashMap.get(str));
            sb.append("</").append(str).append(">");
        }
        sb.append("</request>");
        String sb2 = sb.toString();
        BaseUtils.D(DBUtil.TABLE_PAY, sb2);
        return sb2;
    }

    public static void init(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            try {
                try {
                    try {
                        if (applicationInfo.getClass().getField("targetSdkVersion").getInt(applicationInfo) < 4) {
                            c = false;
                        } else {
                            WindowManager windowManager = (WindowManager) context.getSystemService("window");
                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                            c = ((double) displayMetrics.density) > 1.0d;
                        }
                    } catch (NoSuchFieldException e) {
                        c = false;
                    }
                } catch (IllegalAccessException e2) {
                    c = false;
                }
            } catch (IllegalArgumentException e3) {
                c = false;
            } catch (SecurityException e4) {
                c = false;
            }
        } catch (PackageManager.NameNotFoundException e5) {
            c = false;
        }
    }

    public static RelativeLayout initSubTitle(Context context, String str) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setBackgroundColor(Constants.COLOR_LISTVIEW_ITEM_BACKGROUND);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(10, -1);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setBackgroundResource(BaseUtils.get_R_Drawable(context, !isHdpi() ? Constants.RES_TITLE_BACKGROUND : Constants.RES_TITLE_BACKGROUND_HDPI));
        TextView textView = new TextView(context);
        textView.setTextColor(-1);
        textView.setPadding(10, 10, 0, 10);
        textView.setTextSize(23.0f);
        textView.setText(str);
        new RelativeLayout.LayoutParams(-2, -2).addRule(9, -1);
        relativeLayout.addView(textView);
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(context.getResources().getDrawable(BaseUtils.get_R_Drawable(context, !isHdpi() ? Constants.RES_GFAN_LOGO_HALF : Constants.RES_GFAN_LOGO_HALF_HDPI)));
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.setMargins(0, 0, 10, 0);
        layoutParams2.addRule(11, -1);
        imageView.setLayoutParams(layoutParams2);
        imageView.setPadding(10, 10, 0, 0);
        relativeLayout.addView(imageView);
        return relativeLayout;
    }

    public static void initTitleBar(Activity activity) {
        activity.requestWindowFeature(1);
    }

    public static boolean isAirMode(Context context) {
        return Settings.System.getInt(context.getContentResolver(), "airplane_mode_on") == 1;
    }

    public static boolean isHdpi() {
        return c;
    }

    public static void setCardsVerifications(CardsVerifications cardsVerifications) {
        b = cardsVerifications;
    }

    public static void setPaymentInfo(PaymentInfo paymentInfo) {
        a = paymentInfo;
    }

    public static void setSmsInfo(SmsInfos smsInfos) {
        d = smsInfos;
    }

    public static void writeSmsInfoPayment(Context context, String str) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(context.getFilesDir().getAbsolutePath() + "/" + new StringBuilder().append(System.currentTimeMillis()).toString() + ".smspayment"));
            bufferedWriter.write(str.toString());
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
