package com.mappn.sdk.pay.payment.sms;

import com.mappn.sdk.pay.util.Constants;

/* loaded from: classes.dex */
public class SmsInfoFactory {
    public static synchronized SmsInfo factory(String str) {
        SmsInfo beiweiSmsInfo;
        synchronized (SmsInfoFactory.class) {
            if (LiandongSmsInfo.PARSE_ID.equals(str)) {
                beiweiSmsInfo = new LiandongSmsInfo();
            } else if (ZhenshiSmsInfo.PARSE_ID.equals(str)) {
                beiweiSmsInfo = new ZhenshiSmsInfo();
            } else {
                if (!BeiweiSmsInfo.PARSE_ID.equals(str)) {
                    throw new IllegalArgumentException(Constants.ERROR_TYPE_NOT_SUPPORTED);
                }
                beiweiSmsInfo = new BeiweiSmsInfo();
            }
        }
        return beiweiSmsInfo;
    }
}
