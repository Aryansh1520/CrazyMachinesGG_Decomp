package com.mappn.sdk.pay.payment.sms;

import android.content.Context;
import com.mappn.sdk.pay.chargement.alipay.Base64;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.pay.util.Constants;
import com.mokredit.payment.StringUtils;

/* loaded from: classes.dex */
public class LiandongSmsInfo extends SmsInfo {
    public static final String PARSE_ID = "联动";

    private String a(String str) {
        while (str.getBytes().length > getSmsMaxLength()) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public void buildExtInfo(Context context, PaymentInfo paymentInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append(paymentInfo.getAppkey()).append(Constants.TERM);
        if (paymentInfo.getUser() != null) {
            sb.append(paymentInfo.getUser().getUid());
        }
        sb.append(Constants.TERM);
        if (paymentInfo.getCpID() != null) {
            sb.append(paymentInfo.getCpID());
        }
        sb.append(Constants.TERM).append(Base64.encode(a(paymentInfo.getOrder().getPayName()).getBytes()));
        sb.append(Constants.TERM).append(this.money);
        this.extInfo = sb.toString();
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String getCenterNumber() {
        return Constants.SMS_NUMBER_LIANDONG;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    protected String getContent() {
        return this.goodsId + (this.extInfo == null ? StringUtils.EMPTY : "#" + this.extInfo);
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String getInfoBeforeSend() {
        return StringUtils.EMPTY;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    protected int getSmsMaxLength() {
        return 24;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String getType() {
        return PARSE_ID;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public boolean isNeedSendConfirmSms() {
        return true;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public boolean isSuccess(String str) {
        return str.startsWith(Constants.TEXT_PAY_SMS_CONFIRM_START_WITH);
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public boolean parseConfirmResultSms(String str) {
        return str.contains(Constants.TEXT_PAY_SMS_CONFRIM_RESULT_CONTAIN);
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String parseConfirmSmsConfirmNumber(String str) {
        try {
            return str.substring(str.indexOf(Constants.TEXT_PAY_SMS_CONFRIM_START_WITH) + 9, str.indexOf(Constants.TEXT_PAY_SMS_CONFIRM_INDEXOF));
        } catch (Exception e) {
            return null;
        }
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String parseConfirmSmsSupportTelNumber(String str) {
        return parseConfirmSmsSupportTelNumber(str, Constants.TEXT_PAY_SMS_CONFRIM_SUPPORTTEL_START_WITH);
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public boolean supportMultiple() {
        return false;
    }
}
