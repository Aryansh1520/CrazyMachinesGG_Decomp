package com.mappn.sdk.uc.util;

import android.content.Context;
import com.mappn.sdk.common.codec.binary.Base64;
import com.mappn.sdk.common.utils.BaseConstants;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mokredit.payment.StringUtils;
import java.util.Date;

/* loaded from: classes.dex */
public class UserUtil {
    public static String getOnekeyName(Context context) {
        String mac = BaseUtils.getMAC(context);
        BaseUtils.D("UserUtil", "getOnekeyName : mac :" + mac);
        if (mac == null) {
            return null;
        }
        String replace = mac.replace(":", StringUtils.EMPTY);
        String imei = BaseUtils.getIMEI(context);
        return (Base64.encodeBase64String(StringUtil.hexString2Bytes(replace)) + Base64.encodeBase64String(StringUtil.hexString2Bytes((imei == null || imei.length() <= 13) ? "9876" : imei.substring(11)))).replace("\r", StringUtils.EMPTY).replace("\n", StringUtils.EMPTY).replace("=", "_").replace("+", "gf").replace("/", "fg");
    }

    public static String getPwdByIMEI(Context context) {
        try {
            String replace = BaseUtils.getMAC(context).replace(":", StringUtils.EMPTY);
            String imei = BaseUtils.getIMEI(context);
            return (replace.substring(2, 8) + ((imei == null || imei.length() <= 13) ? "5432" : imei.substring(1, 5))).replace("A", "3").replace("5", BaseConstants.DEFAULT_UC_CNO).replace("6", "8").replace("9", "5").replace("B", "Q").replace("F", "A");
        } catch (Exception e) {
            return "gfan00123";
        }
    }

    public static String randomMail() {
        return (new Date().getTime() + Math.round(Math.random() * 10.0d)) + "@gfan.com";
    }
}
