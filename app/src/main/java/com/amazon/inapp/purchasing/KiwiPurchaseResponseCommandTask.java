package com.amazon.inapp.purchasing;

import android.os.RemoteException;
import com.amazon.android.framework.exception.KiwiException;
import com.amazon.inapp.purchasing.PurchaseResponse;
import com.amazon.venezia.command.SuccessResult;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
final class KiwiPurchaseResponseCommandTask extends KiwiBaseCommandTask {
    private static final String COMMAND_NAME = "purchase_response";
    private static final String COMMAND_VERSION = "1.0";
    private static final String TAG = "KiwiPurchaseResponseCommandTask";

    /* JADX INFO: Access modifiers changed from: package-private */
    public KiwiPurchaseResponseCommandTask(String str) {
        super(COMMAND_NAME, COMMAND_VERSION, str);
    }

    protected void onSuccess(SuccessResult successResult) throws RemoteException, KiwiException {
        PurchaseResponse.PurchaseRequestStatus purchaseRequestStatus;
        Receipt receipt;
        Receipt receipt2 = null;
        if (Logger.isTraceOn()) {
            Logger.trace(TAG, "onSuccess");
        }
        Map data = successResult.getData();
        if (Logger.isTraceOn()) {
            Logger.trace(TAG, "data: " + data);
        }
        String str = (String) data.get("errorMessage");
        String str2 = (String) data.get("userId");
        String str3 = (String) data.get("receipt");
        if (Logger.isTraceOn()) {
            Logger.trace(TAG, "onSuccess: errorMessage: \"" + str + "\" receipt: \"" + str3 + "\"");
        }
        PurchaseResponse.PurchaseRequestStatus purchaseRequestStatus2 = PurchaseResponse.PurchaseRequestStatus.FAILED;
        if (!isNullOrEmpty(str) || isNullOrEmpty(str3)) {
            purchaseRequestStatus = purchaseRequestStatus2;
        } else {
            try {
                JSONObject jSONObject = new JSONObject(str3);
                String string = jSONObject.getString("orderStatus");
                try {
                    purchaseRequestStatus = PurchaseResponse.PurchaseRequestStatus.valueOf(string);
                } catch (Exception e) {
                    if (Logger.isErrorOn()) {
                        Logger.error(TAG, "Invalid order status " + string);
                    }
                    purchaseRequestStatus = PurchaseResponse.PurchaseRequestStatus.FAILED;
                }
                if (PurchaseResponse.PurchaseRequestStatus.SUCCESSFUL == purchaseRequestStatus) {
                    receipt = getReceiptFromReceiptJson(jSONObject);
                    if (!verifyReceipt(str2, receipt, jSONObject)) {
                        purchaseRequestStatus = PurchaseResponse.PurchaseRequestStatus.FAILED;
                        receipt = null;
                    }
                } else {
                    receipt = null;
                }
                receipt2 = receipt;
            } catch (JSONException e2) {
                if (Logger.isErrorOn()) {
                    Logger.error(TAG, "Error parsing receipt JSON: " + e2.getMessage());
                }
                purchaseRequestStatus = PurchaseResponse.PurchaseRequestStatus.FAILED;
            }
        }
        final PurchaseResponse purchaseResponse = new PurchaseResponse(getRequestId(), str2, receipt2, purchaseRequestStatus);
        postRunnableToMainLooper(new Runnable() { // from class: com.amazon.inapp.purchasing.KiwiPurchaseResponseCommandTask.1
            @Override // java.lang.Runnable
            public void run() {
                PurchasingObserver purchasingObserver = PurchasingManager.getPurchasingObserver();
                if (Logger.isTraceOn()) {
                    Logger.trace(KiwiPurchaseResponseCommandTask.TAG, "About to invoke onPurchaseResponse with PurchasingObserver: " + purchasingObserver);
                }
                if (purchasingObserver != null) {
                    if (Logger.isTraceOn()) {
                        Logger.trace(KiwiPurchaseResponseCommandTask.TAG, "Invoking onPurchaseResponse with " + purchaseResponse);
                    }
                    purchasingObserver.onPurchaseResponse(purchaseResponse);
                    if (Logger.isTraceOn()) {
                        Logger.trace(KiwiPurchaseResponseCommandTask.TAG, "No exceptions were thrown when invoking onPurchaseResponse");
                    }
                    ImplementationFactory.getRequestHandler().sendPurchaseResponseReceivedRequest(KiwiPurchaseResponseCommandTask.this.getRequestId());
                }
            }
        });
    }

    @Override // com.amazon.inapp.purchasing.KiwiBaseCommandTask
    protected void sendFailedResponse() {
        postRunnableToMainLooper(new Runnable() { // from class: com.amazon.inapp.purchasing.KiwiPurchaseResponseCommandTask.2
            @Override // java.lang.Runnable
            public void run() {
                PurchasingObserver purchasingObserver = PurchasingManager.getPurchasingObserver();
                PurchaseResponse purchaseResponse = new PurchaseResponse(KiwiPurchaseResponseCommandTask.this.getRequestId(), null, null, PurchaseResponse.PurchaseRequestStatus.FAILED);
                if (purchasingObserver != null) {
                    if (Logger.isTraceOn()) {
                        Logger.trace(KiwiPurchaseResponseCommandTask.TAG, "Invoking onPurchaseResponse with " + purchaseResponse);
                    }
                    purchasingObserver.onPurchaseResponse(purchaseResponse);
                }
            }
        });
    }
}
