package com.mappn.sdk.pay.payment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteAbortException;
import android.net.Uri;
import android.os.Handler;
import com.mappn.sdk.common.utils.BaseConstants;
import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.payment.sms.SmsInfo;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.PrefUtil;

/* loaded from: classes.dex */
final class o extends ContentObserver {
    private Cursor a;
    private /* synthetic */ SmsPaymentFragment b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public o(SmsPaymentFragment smsPaymentFragment, Handler handler) {
        super(handler);
        this.b = smsPaymentFragment;
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z) {
        Activity activity;
        SmsInfo smsInfo;
        int i;
        int i2;
        Activity activity2;
        Activity activity3;
        SmsInfo smsInfo2;
        Activity activity4;
        Activity activity5;
        Activity activity6;
        Activity activity7;
        Activity activity8;
        Activity activity9;
        SmsInfo smsInfo3;
        SmsInfo smsInfo4;
        Activity activity10;
        Activity activity11;
        int i3;
        SmsInfo smsInfo5;
        SmsInfo smsInfo6;
        Activity activity12;
        Activity activity13;
        int i4;
        Activity activity14;
        Activity activity15;
        Activity activity16;
        Activity activity17;
        Activity activity18;
        SmsInfo smsInfo7;
        SmsInfo smsInfo8;
        Activity activity19;
        Activity activity20;
        SmsInfo smsInfo9;
        SmsInfo smsInfo10;
        SmsInfo smsInfo11;
        Activity activity21;
        Activity activity22;
        Activity activity23;
        int i5;
        Handler handler;
        Runnable runnable;
        Activity activity24;
        ContentObserver contentObserver;
        Activity activity25;
        BroadcastReceiver broadcastReceiver;
        int i6;
        byte b = 0;
        super.onChange(z);
        Uri parse = Uri.parse("content://sms/inbox");
        activity = this.b.mActivity;
        StringBuilder sb = new StringBuilder();
        smsInfo = this.b.h;
        this.a = ((PaymentsActivity) activity).managedQuery(parse, new String[]{"_id", "address", "body"}, " address like ? and read=?", new String[]{sb.append(smsInfo.getCenterNumber()).append("%%").toString(), "0"}, "date desc");
        if (this.a == null) {
            return;
        }
        if (this.a.moveToFirst()) {
            i = this.b.l;
            if (i > 0) {
                i6 = this.b.l;
                if (i6 > this.a.getInt(0)) {
                    this.a.close();
                    return;
                }
            }
            i2 = this.b.i;
            if (i2 <= 1) {
                handler = this.b.n;
                runnable = this.b.q;
                handler.removeCallbacks(runnable);
                activity24 = this.b.mActivity;
                ContentResolver contentResolver = ((PaymentsActivity) activity24).getContentResolver();
                contentObserver = this.b.m;
                contentResolver.unregisterContentObserver(contentObserver);
                try {
                    activity25 = this.b.mActivity;
                    broadcastReceiver = this.b.p;
                    ((PaymentsActivity) activity25).unregisterReceiver(broadcastReceiver);
                } catch (IllegalArgumentException e) {
                }
            }
            this.b.l = this.a.getInt(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put("read", BaseConstants.DEFAULT_UC_CNO);
            try {
                activity23 = this.b.mActivity;
                ContentResolver contentResolver2 = ((PaymentsActivity) activity23).getContentResolver();
                StringBuilder sb2 = new StringBuilder();
                i5 = this.b.l;
                contentResolver2.update(parse, contentValues, " _id=?", new String[]{sb2.append(i5).toString()});
            } catch (SQLiteAbortException e2) {
                e2.printStackTrace();
            }
            String string = this.a.getString(this.a.getColumnIndex("body"));
            String string2 = this.a.getString(this.a.getColumnIndex("address"));
            p pVar = new p(this.b, b);
            activity2 = this.b.mActivity;
            if (3 == ((PaymentsActivity) activity2).mType) {
                i3 = this.b.i;
                if (i3 <= 1) {
                    activity22 = this.b.mActivity;
                    ((PaymentsActivity) activity22).removeDialog(17);
                }
                smsInfo5 = this.b.h;
                if (smsInfo5.isNeedSendConfirmSms()) {
                    smsInfo9 = this.b.h;
                    String parseConfirmSmsSupportTelNumber = smsInfo9.parseConfirmSmsSupportTelNumber(string);
                    smsInfo10 = this.b.h;
                    String parseConfirmSmsConfirmNumber = smsInfo10.parseConfirmSmsConfirmNumber(string);
                    smsInfo11 = this.b.h;
                    if (!smsInfo11.isSuccess(string) || parseConfirmSmsConfirmNumber == null) {
                        this.b.mSmsResultInfo = Constants.TEXT_PAY_SMS_FAILED_INSUFFENT_BALANCE;
                        activity21 = this.b.mActivity;
                        ((PaymentsActivity) activity21).showDialog(21);
                    } else {
                        this.b.buildSmsConfirmView(string2, parseConfirmSmsConfirmNumber, parseConfirmSmsSupportTelNumber);
                    }
                } else {
                    smsInfo6 = this.b.h;
                    if (smsInfo6.isSuccess(string)) {
                        activity13 = this.b.mActivity;
                        PrefUtil.setPayedAmount(activity13, this.b.getPayedAmount());
                        i4 = this.b.i;
                        if (i4 > 1) {
                            SmsPaymentFragment.F(this.b);
                        } else {
                            this.b.mSmsResultInfo = string.replace(Constants.SMS_SUCCESS, Constants.TEXT_PAY_SMS_SUCCESS);
                            activity14 = this.b.mActivity;
                            ((PaymentsActivity) activity14).showDialog(20);
                        }
                        long j = -1;
                        activity15 = this.b.mActivity;
                        if (((PaymentsActivity) activity15).mPaymentInfo.getUser() != null) {
                            activity20 = this.b.mActivity;
                            j = ((PaymentsActivity) activity20).mPaymentInfo.getUser().getUid();
                        }
                        activity16 = this.b.mActivity;
                        activity17 = this.b.mActivity;
                        String appkey = ((PaymentsActivity) activity17).mPaymentInfo.getAppkey();
                        activity18 = this.b.mActivity;
                        String payName = ((PaymentsActivity) activity18).mPaymentInfo.getOrder().getPayName();
                        smsInfo7 = this.b.h;
                        int i7 = smsInfo7.money;
                        smsInfo8 = this.b.h;
                        String type = smsInfo8.getType();
                        activity19 = this.b.mActivity;
                        Api.postSmsPayment(activity16, pVar, appkey, payName, i7, type, ((PaymentsActivity) activity19).mPaymentInfo.getCpID(), j);
                    } else {
                        this.b.mSmsResultInfo = string;
                        activity12 = this.b.mActivity;
                        ((PaymentsActivity) activity12).showDialog(21);
                    }
                }
            } else {
                activity3 = this.b.mActivity;
                ((PaymentsActivity) activity3).removeDialog(17);
                smsInfo2 = this.b.h;
                if (smsInfo2.parseConfirmResultSms(string)) {
                    this.b.mSmsResultInfo = string.replace(Constants.SMS_SUCCESS, Constants.TEXT_PAY_SMS_SUCCESS);
                    activity5 = this.b.mActivity;
                    ((PaymentsActivity) activity5).showDialog(20);
                    long j2 = -1;
                    activity6 = this.b.mActivity;
                    if (((PaymentsActivity) activity6).mPaymentInfo.getUser() != null) {
                        activity11 = this.b.mActivity;
                        j2 = ((PaymentsActivity) activity11).mPaymentInfo.getUser().getUid();
                    }
                    activity7 = this.b.mActivity;
                    activity8 = this.b.mActivity;
                    String appkey2 = ((PaymentsActivity) activity8).mPaymentInfo.getAppkey();
                    activity9 = this.b.mActivity;
                    String payName2 = ((PaymentsActivity) activity9).mPaymentInfo.getOrder().getPayName();
                    smsInfo3 = this.b.h;
                    int i8 = smsInfo3.money;
                    smsInfo4 = this.b.h;
                    String type2 = smsInfo4.getType();
                    activity10 = this.b.mActivity;
                    Api.postSmsPayment(activity7, pVar, appkey2, payName2, i8, type2, ((PaymentsActivity) activity10).mPaymentInfo.getCpID(), j2);
                } else {
                    this.b.mSmsResultInfo = string;
                    activity4 = this.b.mActivity;
                    ((PaymentsActivity) activity4).showDialog(21);
                }
            }
        }
        this.a.close();
    }
}
