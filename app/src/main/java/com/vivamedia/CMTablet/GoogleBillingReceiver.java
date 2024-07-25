package com.vivamedia.CMTablet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.vivamedia.CMTablet.GoogleBillingService;

/* loaded from: classes.dex */
public class GoogleBillingReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (GoogleBillingService.ACTION_PURCHASE_STATE_CHANGED.equals(action)) {
            String signedData = intent.getStringExtra(GoogleBillingService.INAPP_SIGNED_DATA);
            String signature = intent.getStringExtra(GoogleBillingService.INAPP_SIGNATURE);
            purchaseStateChanged(context, signedData, signature);
        } else if (GoogleBillingService.ACTION_NOTIFY.equals(action)) {
            String notifyId = intent.getStringExtra(GoogleBillingService.NOTIFICATION_ID);
            notify(context, notifyId);
        } else if (GoogleBillingService.ACTION_RESPONSE_CODE.equals(action)) {
            long requestId = intent.getLongExtra(GoogleBillingService.INAPP_REQUEST_ID, -1L);
            int responseCodeIndex = intent.getIntExtra(GoogleBillingService.INAPP_RESPONSE_CODE, GoogleBillingService.ResponseCode.RESULT_ERROR.ordinal());
            checkResponseCode(context, requestId, responseCodeIndex);
        }
    }

    private void purchaseStateChanged(Context context, String signedData, String signature) {
        Intent intent = new Intent(GoogleBillingService.ACTION_PURCHASE_STATE_CHANGED);
        intent.setClass(context, GoogleBillingService.class);
        intent.putExtra(GoogleBillingService.INAPP_SIGNED_DATA, signedData);
        intent.putExtra(GoogleBillingService.INAPP_SIGNATURE, signature);
        context.startService(intent);
    }

    private void notify(Context context, String notifyId) {
        Intent intent = new Intent(GoogleBillingService.ACTION_GET_PURCHASE_INFORMATION);
        intent.setClass(context, GoogleBillingService.class);
        intent.putExtra(GoogleBillingService.NOTIFICATION_ID, notifyId);
        context.startService(intent);
    }

    private void checkResponseCode(Context context, long requestId, int responseCodeIndex) {
        Intent intent = new Intent(GoogleBillingService.ACTION_RESPONSE_CODE);
        intent.setClass(context, GoogleBillingService.class);
        intent.putExtra(GoogleBillingService.INAPP_REQUEST_ID, requestId);
        intent.putExtra(GoogleBillingService.INAPP_RESPONSE_CODE, responseCodeIndex);
        context.startService(intent);
    }
}
