package com.vivamedia.CMTablet;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import com.vivamedia.CMTablet.GoogleBillingService;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class GooglePurchaseObserver {
    private static final String DB_INITIALIZED = "db_initialized";
    private static final Class[] START_INTENT_SENDER_SIG = {IntentSender.class, Intent.class, Integer.TYPE, Integer.TYPE, Integer.TYPE};
    private static final String TAG = "GooglePurchaseObserver";
    private final Activity mActivity;
    private final Handler mHandler;
    private Method mStartIntentSender;
    private Object[] mStartIntentSenderArgs = new Object[5];

    /* loaded from: classes.dex */
    public enum PurchaseState {
        PURCHASED,
        CANCELED,
        REFUNDED;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static PurchaseState[] valuesCustom() {
            PurchaseState[] valuesCustom = values();
            int length = valuesCustom.length;
            PurchaseState[] purchaseStateArr = new PurchaseState[length];
            System.arraycopy(valuesCustom, 0, purchaseStateArr, 0, length);
            return purchaseStateArr;
        }

        public static PurchaseState valueOf(int index) {
            PurchaseState[] values = valuesCustom();
            return (index < 0 || index >= values.length) ? CANCELED : values[index];
        }
    }

    public GooglePurchaseObserver(Activity activity, Handler handler) {
        this.mActivity = activity;
        this.mHandler = handler;
        initCompatibilityLayer();
    }

    public void onBillingSupported(GoogleBillingService service, boolean supported) {
        if (supported) {
            SharedPreferences prefs = this.mActivity.getPreferences(0);
            boolean bInitialized = prefs.getBoolean(DB_INITIALIZED, false);
            if (!bInitialized) {
                service.restoreTransactions();
            }
        }
    }

    public void onPurchaseStateChange(PurchaseState purchaseState, String itemId, int quantity, long purchaseTime, String developerPayload) {
        if (purchaseState == PurchaseState.PURCHASED) {
            MainActivity.onPurchaseDoneNative(itemId, 0);
        } else if (purchaseState == PurchaseState.CANCELED) {
            MainActivity.onPurchaseDoneNative(itemId, 2);
        } else {
            MainActivity.onPurchaseDoneNative(itemId, 1);
        }
    }

    public void onRequestPurchaseResponse(GoogleBillingService.RequestPurchase request, GoogleBillingService.ResponseCode responseCode) {
        if (responseCode != GoogleBillingService.ResponseCode.RESULT_OK) {
            if (responseCode == GoogleBillingService.ResponseCode.RESULT_USER_CANCELED) {
                MainActivity.onPurchaseDoneNative("-PURCHASECANCELED-", 99);
            } else {
                MainActivity.onPurchaseDoneNative("-PURCHASEFAILED-", 99);
            }
        }
    }

    public void onRestoreTransactionsResponse(GoogleBillingService.RestoreTransactions request, GoogleBillingService.ResponseCode responseCode) {
        if (responseCode == GoogleBillingService.ResponseCode.RESULT_OK) {
            SharedPreferences prefs = this.mActivity.getPreferences(0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(DB_INITIALIZED, true);
            edit.commit();
        }
    }

    private void initCompatibilityLayer() {
        try {
            this.mStartIntentSender = this.mActivity.getClass().getMethod("startIntentSender", START_INTENT_SENDER_SIG);
        } catch (NoSuchMethodException e) {
            this.mStartIntentSender = null;
        } catch (SecurityException e2) {
            this.mStartIntentSender = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startBuyPageActivity(PendingIntent pendingIntent, Intent intent) {
        if (this.mStartIntentSender != null) {
            try {
                this.mStartIntentSenderArgs[0] = pendingIntent.getIntentSender();
                this.mStartIntentSenderArgs[1] = intent;
                this.mStartIntentSenderArgs[2] = 0;
                this.mStartIntentSenderArgs[3] = 0;
                this.mStartIntentSenderArgs[4] = 0;
                this.mStartIntentSender.invoke(this.mActivity, this.mStartIntentSenderArgs);
                return;
            } catch (Exception e) {
                Log.e(TAG, "Exeption invoking BuyPage: ", e);
                return;
            }
        }
        try {
            pendingIntent.send(this.mActivity, 0, intent);
        } catch (PendingIntent.CanceledException e2) {
            Log.e(TAG, "Cancel Exception: ", e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void postPurchaseStateChange(final PurchaseState purchaseState, final String itemId, final int quantity, final long purchaseTime, final String developerPayload) {
        this.mHandler.post(new Runnable() { // from class: com.vivamedia.CMTablet.GooglePurchaseObserver.1
            @Override // java.lang.Runnable
            public void run() {
                GooglePurchaseObserver.this.onPurchaseStateChange(purchaseState, itemId, quantity, purchaseTime, developerPayload);
            }
        });
    }
}
