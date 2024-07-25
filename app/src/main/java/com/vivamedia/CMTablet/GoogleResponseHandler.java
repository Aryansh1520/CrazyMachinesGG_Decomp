package com.vivamedia.CMTablet;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.vivamedia.CMTablet.GoogleBillingService;
import com.vivamedia.CMTablet.GooglePurchaseObserver;

/* loaded from: classes.dex */
public class GoogleResponseHandler {
    private static GooglePurchaseObserver purchaseObserver;

    public static synchronized void register(GooglePurchaseObserver observer) {
        synchronized (GoogleResponseHandler.class) {
            purchaseObserver = observer;
        }
    }

    public static synchronized void unregister(GooglePurchaseObserver observer) {
        synchronized (GoogleResponseHandler.class) {
            purchaseObserver = null;
        }
    }

    public static void checkBillingSupportedResponse(GoogleBillingService service, boolean supported) {
        if (purchaseObserver != null) {
            purchaseObserver.onBillingSupported(service, supported);
        }
    }

    public static void buyPageIntentResponse(PendingIntent pendingIntent, Intent intent) {
        if (purchaseObserver != null) {
            purchaseObserver.startBuyPageActivity(pendingIntent, intent);
        }
    }

    public static void purchaseResponse(Context context, final GooglePurchaseObserver.PurchaseState purchaseState, final String productId, String orderId, final long purchaseTime, final String developerPayload) {
        new Thread(new Runnable() { // from class: com.vivamedia.CMTablet.GoogleResponseHandler.1
            @Override // java.lang.Runnable
            public void run() {
                synchronized (GoogleResponseHandler.class) {
                    if (GoogleResponseHandler.purchaseObserver != null) {
                        GoogleResponseHandler.purchaseObserver.postPurchaseStateChange(GooglePurchaseObserver.PurchaseState.this, productId, 1, purchaseTime, developerPayload);
                    }
                }
            }
        }).start();
    }

    public static void responseCodeReceived(Context context, GoogleBillingService.RequestPurchase request, GoogleBillingService.ResponseCode responseCode) {
        if (purchaseObserver != null) {
            purchaseObserver.onRequestPurchaseResponse(request, responseCode);
        }
    }

    public static void responseCodeReceived(Context context, GoogleBillingService.RestoreTransactions request, GoogleBillingService.ResponseCode responseCode) {
        if (purchaseObserver != null) {
            purchaseObserver.onRestoreTransactionsResponse(request, responseCode);
        }
    }
}
