package com.mappn.sdk.pay.payment.sms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.mappn.sdk.pay.util.DBUtil;

/* loaded from: classes.dex */
public class SmsReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        Object[] objArr = (Object[]) extras.get("pdus");
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= objArr.length) {
                return;
            }
            Log.i(DBUtil.TABLE_PAY, "receiver number:" + SmsMessage.createFromPdu((byte[]) objArr[i2]).getOriginatingAddress());
            abortBroadcast();
            i = i2 + 1;
        }
    }
}
