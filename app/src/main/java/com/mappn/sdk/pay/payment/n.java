package com.mappn.sdk.pay.payment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.database.ContentObserver;
import com.mappn.sdk.pay.payment.sms.LiandongSmsInfo;
import com.mappn.sdk.pay.payment.sms.SmsInfo;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.PrefUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class n implements Runnable {
    private /* synthetic */ SmsPaymentFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(SmsPaymentFragment smsPaymentFragment) {
        this.a = smsPaymentFragment;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ContentObserver contentObserver;
        Activity activity;
        SmsInfo smsInfo;
        int i;
        Activity activity2;
        Activity activity3;
        Activity activity4;
        Activity activity5;
        BroadcastReceiver broadcastReceiver;
        Activity activity6;
        Activity activity7;
        ContentObserver contentObserver2;
        contentObserver = this.a.m;
        if (contentObserver != null) {
            activity7 = this.a.mActivity;
            ContentResolver contentResolver = ((PaymentsActivity) activity7).getContentResolver();
            contentObserver2 = this.a.m;
            contentResolver.unregisterContentObserver(contentObserver2);
        }
        activity = this.a.mActivity;
        ((PaymentsActivity) activity).removeDialog(17);
        smsInfo = this.a.h;
        if (smsInfo instanceof LiandongSmsInfo) {
            activity6 = this.a.mActivity;
            ((PaymentsActivity) activity6).showDialog(21);
        } else {
            i = this.a.j;
            if (i > 0) {
                this.a.mSmsResultInfo = Constants.TEXT_PAY_SMS_FAILED_BY_TIME_OUT;
                activity3 = this.a.mActivity;
                ((PaymentsActivity) activity3).showDialog(21);
                activity4 = this.a.mActivity;
                PrefUtil.setPayedAmount(activity4, this.a.getPayedAmount());
            } else {
                this.a.mSmsResultInfo = null;
                activity2 = this.a.mActivity;
                ((PaymentsActivity) activity2).showDialog(20);
            }
        }
        try {
            activity5 = this.a.mActivity;
            broadcastReceiver = this.a.p;
            ((PaymentsActivity) activity5).unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
        }
    }
}
