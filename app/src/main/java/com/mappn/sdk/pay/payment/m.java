package com.mappn.sdk.pay.payment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import com.mappn.sdk.pay.util.Constants;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class m extends BroadcastReceiver {
    private /* synthetic */ SmsPaymentFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(SmsPaymentFragment smsPaymentFragment) {
        this.a = smsPaymentFragment;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        Activity activity;
        Activity activity2;
        int i;
        ContentObserver contentObserver;
        Activity activity3;
        ContentObserver contentObserver2;
        Activity activity4;
        ContentObserver contentObserver3;
        Activity activity5;
        int i2;
        int i3;
        Activity activity6;
        Activity activity7;
        ContentObserver contentObserver4;
        Activity activity8;
        ContentObserver contentObserver5;
        Handler handler;
        int i4;
        Activity activity9;
        ContentObserver contentObserver6;
        Activity activity10;
        Activity activity11;
        int i5;
        ContentObserver contentObserver7;
        Activity activity12;
        ContentObserver contentObserver8;
        Activity activity13;
        switch (getResultCode()) {
            case -1:
                contentObserver3 = this.a.m;
                if (contentObserver3 == null) {
                    SmsPaymentFragment smsPaymentFragment = this.a;
                    SmsPaymentFragment smsPaymentFragment2 = this.a;
                    handler = this.a.n;
                    smsPaymentFragment.m = new o(smsPaymentFragment2, handler);
                    i4 = this.a.j;
                    if (i4 != -1) {
                        activity9 = this.a.mActivity;
                        ContentResolver contentResolver = ((PaymentsActivity) activity9).getContentResolver();
                        Uri parse = Uri.parse("content://sms/");
                        contentObserver6 = this.a.m;
                        contentResolver.registerContentObserver(parse, true, contentObserver6);
                    }
                }
                activity5 = this.a.mActivity;
                if (4 == ((PaymentsActivity) activity5).mType) {
                    activity8 = this.a.mActivity;
                    ContentResolver contentResolver2 = ((PaymentsActivity) activity8).getContentResolver();
                    Uri parse2 = Uri.parse("content://sms/");
                    contentObserver5 = this.a.m;
                    contentResolver2.registerContentObserver(parse2, true, contentObserver5);
                }
                i2 = this.a.j;
                if (i2 == -1) {
                    activity7 = this.a.mActivity;
                    ContentResolver contentResolver3 = ((PaymentsActivity) activity7).getContentResolver();
                    Uri parse3 = Uri.parse("content://sms/");
                    contentObserver4 = this.a.m;
                    contentResolver3.registerContentObserver(parse3, true, contentObserver4);
                    return;
                }
                i3 = this.a.j;
                if (i3 == 1) {
                    SmsPaymentFragment.i(this.a);
                    return;
                }
                activity6 = this.a.mActivity;
                ((PaymentsActivity) activity6).removeDialog(17);
                SmsPaymentFragment.i(this.a);
                this.a.buildView();
                return;
            case 1:
            case 133404:
                return;
            case 2:
                activity = this.a.mActivity;
                ((PaymentsActivity) activity).removeDialog(17);
                this.a.mSmsResultInfo = Constants.TEXT_PAY_SMS_ERROR_AIR_MODE;
                activity2 = this.a.mActivity;
                ((PaymentsActivity) activity2).showDialog(21);
                i = this.a.j;
                if (i == -1) {
                    activity4 = this.a.mActivity;
                    if (4 != ((PaymentsActivity) activity4).mType) {
                        return;
                    }
                }
                contentObserver = this.a.m;
                if (contentObserver != null) {
                    activity3 = this.a.mActivity;
                    ContentResolver contentResolver4 = ((PaymentsActivity) activity3).getContentResolver();
                    contentObserver2 = this.a.m;
                    contentResolver4.unregisterContentObserver(contentObserver2);
                    return;
                }
                return;
            default:
                activity10 = this.a.mActivity;
                ((PaymentsActivity) activity10).removeDialog(17);
                this.a.mSmsResultInfo = Constants.TEXT_PAY_SMS_FAILED_INSUFFENT_BALANCE;
                activity11 = this.a.mActivity;
                ((PaymentsActivity) activity11).showDialog(21);
                i5 = this.a.j;
                if (i5 == -1) {
                    activity13 = this.a.mActivity;
                    if (4 != ((PaymentsActivity) activity13).mType) {
                        return;
                    }
                }
                contentObserver7 = this.a.m;
                if (contentObserver7 != null) {
                    activity12 = this.a.mActivity;
                    ContentResolver contentResolver5 = ((PaymentsActivity) activity12).getContentResolver();
                    contentObserver8 = this.a.m;
                    contentResolver5.unregisterContentObserver(contentObserver8);
                    return;
                }
                return;
        }
    }
}
