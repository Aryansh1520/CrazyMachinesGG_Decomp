package com.mappn.sdk.pay.payment.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mokredit.payment.StringUtils;

/* loaded from: classes.dex */
public abstract class SmsInfo {
    public static final String ACTION_SMS_SENT = "com.gfan.sdk.SMS_SENT_ACTION";
    public static final String COMPANY_CODE_CHINAMOBILE = "00";
    public static final String COMPANY_CODE_CHINATELECOM = "03";
    public static final String COMPANY_CODE_CHINAUNICOM = "01";
    public String companyId;
    public String disableAreas;
    public String extInfo;
    public String goodsId;
    public String merId;
    public int money;
    public String receiverNumber;

    public static void sendSms(Context context, String str, String str2) {
        SmsManager.getDefault().sendTextMessage(str, null, str2, PendingIntent.getBroadcast(context, 0, new Intent(ACTION_SMS_SENT), 0), null);
    }

    public abstract void buildExtInfo(Context context, PaymentInfo paymentInfo);

    public abstract String getCenterNumber();

    protected abstract String getContent();

    public abstract String getInfoBeforeSend();

    public String getNumber() {
        return this.receiverNumber + this.merId;
    }

    protected abstract int getSmsMaxLength();

    public abstract String getType();

    public abstract boolean isNeedSendConfirmSms();

    public abstract boolean isSuccess(String str);

    public abstract boolean parseConfirmResultSms(String str);

    public abstract String parseConfirmSmsConfirmNumber(String str);

    public abstract String parseConfirmSmsSupportTelNumber(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public final String parseConfirmSmsSupportTelNumber(String str, String str2) {
        try {
            return str.substring(str.lastIndexOf(str2) + str2.length());
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    public void sendFirstSms(Context context) {
        sendSms(context, getNumber(), getContent());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean supportMultiple();
}
