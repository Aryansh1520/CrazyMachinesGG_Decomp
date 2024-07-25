package com.mappn.sdk.pay.payment.sms;

import android.content.Context;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.pay.util.Constants;
import com.mokredit.payment.StringUtils;

/* loaded from: classes.dex */
public class ZhenshiSmsInfo extends SmsInfo {
    public static final String PARSE_ID = "真石";

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public void buildExtInfo(Context context, PaymentInfo paymentInfo) {
        this.extInfo = paymentInfo.getAppkey() + Constants.TERM + this.money;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String getCenterNumber() {
        return Constants.SMS_NUMBER_ZHENSHI;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    protected String getContent() {
        return this.goodsId + (this.extInfo == null ? StringUtils.EMPTY : Constants.TERM + this.extInfo);
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String getInfoBeforeSend() {
        return StringUtils.EMPTY;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    protected int getSmsMaxLength() {
        return 15;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String getType() {
        return PARSE_ID;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public boolean isNeedSendConfirmSms() {
        return false;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public boolean isSuccess(String str) {
        return (str.contains(Constants.TEXT_INSUFFENT_BALANCE) || str.contains(Constants.TEXT_INSUFFENT_BALANCE2)) ? false : true;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public boolean parseConfirmResultSms(String str) {
        return true;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String parseConfirmSmsConfirmNumber(String str) {
        return null;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public String parseConfirmSmsSupportTelNumber(String str) {
        return null;
    }

    @Override // com.mappn.sdk.pay.payment.sms.SmsInfo
    public boolean supportMultiple() {
        return true;
    }
}
