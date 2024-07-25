package com.vivamedia.CMTablet;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.vending.billing.IMarketBillingService;
import com.vivamedia.CMTablet.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes.dex */
public class GoogleBillingService extends Service implements ServiceConnection {
    public static final String ACTION_CONFIRM_NOTIFICATION = "com.vivamedia.CMTablet.CONFIRM_NOTIFICATION";
    public static final String ACTION_GET_PURCHASE_INFORMATION = "com.vivamedia.CMTablet.GET_PURCHASE_INFORMATION";
    public static final String ACTION_NOTIFY = "com.android.vending.billing.IN_APP_NOTIFY";
    public static final String ACTION_PURCHASE_STATE_CHANGED = "com.android.vending.billing.PURCHASE_STATE_CHANGED";
    public static final String ACTION_RESPONSE_CODE = "com.android.vending.billing.RESPONSE_CODE";
    public static final String ACTION_RESTORE_TRANSACTIONS = "com.vivamedia.CMTablet.RESTORE_TRANSACTIONS";
    public static final String BILLING_REQUEST_API_VERSION = "API_VERSION";
    public static final String BILLING_REQUEST_DEVELOPER_PAYLOAD = "DEVELOPER_PAYLOAD";
    public static final String BILLING_REQUEST_ITEM_ID = "ITEM_ID";
    public static final String BILLING_REQUEST_METHOD = "BILLING_REQUEST";
    public static final String BILLING_REQUEST_NONCE = "NONCE";
    public static final String BILLING_REQUEST_NOTIFY_IDS = "NOTIFY_IDS";
    public static final String BILLING_REQUEST_PACKAGE_NAME = "PACKAGE_NAME";
    public static final long BILLING_RESPONSE_INVALID_REQUEST_ID = -1;
    public static final String BILLING_RESPONSE_PURCHASE_INTENT = "PURCHASE_INTENT";
    public static final String BILLING_RESPONSE_REQUEST_ID = "REQUEST_ID";
    public static final String BILLING_RESPONSE_RESPONSE_CODE = "RESPONSE_CODE";
    public static final String INAPP_REQUEST_ID = "request_id";
    public static final String INAPP_RESPONSE_CODE = "response_code";
    public static final String INAPP_SIGNATURE = "inapp_signature";
    public static final String INAPP_SIGNED_DATA = "inapp_signed_data";
    public static final String MARKET_BILLING_SERVICE_ACTION = "com.android.vending.billing.MarketBillingService.BIND";
    public static final String NOTIFICATION_ID = "notification_id";
    private static final String TAG = "GoogleBillingService";
    private static IMarketBillingService marketBillingService;
    private static LinkedList<BillingRequest> pendingRequests = new LinkedList<>();
    private static HashMap<Long, BillingRequest> sentRequests = new HashMap<>();

    /* loaded from: classes.dex */
    public enum ResponseCode {
        RESULT_OK,
        RESULT_USER_CANCELED,
        RESULT_SERVICE_UNAVAILABLE,
        RESULT_BILLING_UNAVAILABLE,
        RESULT_ITEM_UNAVAILABLE,
        RESULT_DEVELOPER_ERROR,
        RESULT_ERROR;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static ResponseCode[] valuesCustom() {
            ResponseCode[] valuesCustom = values();
            int length = valuesCustom.length;
            ResponseCode[] responseCodeArr = new ResponseCode[length];
            System.arraycopy(valuesCustom, 0, responseCodeArr, 0, length);
            return responseCodeArr;
        }

        public static ResponseCode valueOf(int index) {
            ResponseCode[] values = valuesCustom();
            return (index < 0 || index >= values.length) ? RESULT_ERROR : values[index];
        }
    }

    /* loaded from: classes.dex */
    public abstract class BillingRequest {
        protected long requestId;
        private final int startId;

        protected abstract long run() throws RemoteException;

        public BillingRequest(int id) {
            this.startId = id;
        }

        public int getStartId() {
            return this.startId;
        }

        public boolean runRequest() {
            if (runIfConnected()) {
                return true;
            }
            if (GoogleBillingService.this.bindToMarketBillingService()) {
                GoogleBillingService.pendingRequests.add(this);
                return true;
            }
            return false;
        }

        public boolean runIfConnected() {
            if (GoogleBillingService.marketBillingService != null) {
                try {
                    this.requestId = run();
                    if (this.requestId >= 0) {
                        GoogleBillingService.sentRequests.put(Long.valueOf(this.requestId), this);
                    }
                    return true;
                } catch (RemoteException e) {
                    onRemoteException(e);
                }
            }
            return false;
        }

        protected void onRemoteException(RemoteException e) {
            GoogleBillingService.marketBillingService = null;
        }

        protected void responseCodeReceived(ResponseCode responseCode) {
        }

        protected Bundle makeRequestBundle(String method) {
            Bundle request = new Bundle();
            request.putString(GoogleBillingService.BILLING_REQUEST_METHOD, method);
            request.putInt(GoogleBillingService.BILLING_REQUEST_API_VERSION, 1);
            request.putString("PACKAGE_NAME", GoogleBillingService.this.getPackageName());
            return request;
        }

        protected void logResponseCode(String method, Bundle response) {
        }
    }

    /* loaded from: classes.dex */
    public class CheckBillingSupported extends BillingRequest {
        public CheckBillingSupported() {
            super(-1);
        }

        @Override // com.vivamedia.CMTablet.GoogleBillingService.BillingRequest
        protected long run() throws RemoteException {
            Bundle request = makeRequestBundle("CHECK_BILLING_SUPPORTED");
            Bundle response = GoogleBillingService.marketBillingService.sendBillingRequest(request);
            int responseCode = response.getInt(GoogleBillingService.BILLING_RESPONSE_RESPONSE_CODE);
            boolean billingSupported = responseCode == ResponseCode.RESULT_OK.ordinal();
            GoogleResponseHandler.checkBillingSupportedResponse(GoogleBillingService.this, billingSupported);
            return -1L;
        }
    }

    /* loaded from: classes.dex */
    public class RequestPurchase extends BillingRequest {
        public final String developerPayload;
        public final String productId;

        public RequestPurchase(GoogleBillingService googleBillingService, String itemId) {
            this(itemId, null);
        }

        public RequestPurchase(String itemId, String payload) {
            super(-1);
            this.productId = itemId;
            this.developerPayload = payload;
        }

        @Override // com.vivamedia.CMTablet.GoogleBillingService.BillingRequest
        protected long run() throws RemoteException {
            Bundle request = makeRequestBundle("REQUEST_PURCHASE");
            request.putString(GoogleBillingService.BILLING_REQUEST_ITEM_ID, this.productId);
            if (this.developerPayload != null) {
                request.putString(GoogleBillingService.BILLING_REQUEST_DEVELOPER_PAYLOAD, this.developerPayload);
            }
            Bundle response = GoogleBillingService.marketBillingService.sendBillingRequest(request);
            PendingIntent pendingIntent = (PendingIntent) response.getParcelable(GoogleBillingService.BILLING_RESPONSE_PURCHASE_INTENT);
            if (pendingIntent == null) {
                return -1L;
            }
            Intent intent = new Intent();
            GoogleResponseHandler.buyPageIntentResponse(pendingIntent, intent);
            return response.getLong(GoogleBillingService.BILLING_RESPONSE_REQUEST_ID, -1L);
        }

        @Override // com.vivamedia.CMTablet.GoogleBillingService.BillingRequest
        protected void responseCodeReceived(ResponseCode responseCode) {
            GoogleResponseHandler.responseCodeReceived(GoogleBillingService.this, this, responseCode);
        }
    }

    /* loaded from: classes.dex */
    public class ConfirmNotifications extends BillingRequest {
        final String[] notifyIds;

        public ConfirmNotifications(int startId, String[] rgNotifyIds) {
            super(startId);
            this.notifyIds = rgNotifyIds;
        }

        @Override // com.vivamedia.CMTablet.GoogleBillingService.BillingRequest
        protected long run() throws RemoteException {
            Bundle request = makeRequestBundle("CONFIRM_NOTIFICATIONS");
            request.putStringArray(GoogleBillingService.BILLING_REQUEST_NOTIFY_IDS, this.notifyIds);
            Bundle response = GoogleBillingService.marketBillingService.sendBillingRequest(request);
            logResponseCode("confirmNotifications", response);
            return response.getLong(GoogleBillingService.BILLING_RESPONSE_REQUEST_ID, -1L);
        }
    }

    /* loaded from: classes.dex */
    public class GetPurchaseInformation extends BillingRequest {
        long nonce;
        final String[] notifyIds;

        public GetPurchaseInformation(int startId, String[] rgNotifyIds) {
            super(startId);
            this.notifyIds = rgNotifyIds;
        }

        @Override // com.vivamedia.CMTablet.GoogleBillingService.BillingRequest
        protected long run() throws RemoteException {
            this.nonce = Security.generateNonce();
            Bundle request = makeRequestBundle("GET_PURCHASE_INFORMATION");
            request.putLong(GoogleBillingService.BILLING_REQUEST_NONCE, this.nonce);
            request.putStringArray(GoogleBillingService.BILLING_REQUEST_NOTIFY_IDS, this.notifyIds);
            Bundle response = GoogleBillingService.marketBillingService.sendBillingRequest(request);
            logResponseCode("getPurchaseInformation", response);
            return response.getLong(GoogleBillingService.BILLING_RESPONSE_REQUEST_ID, -1L);
        }

        @Override // com.vivamedia.CMTablet.GoogleBillingService.BillingRequest
        protected void onRemoteException(RemoteException e) {
            super.onRemoteException(e);
            Security.removeNonce(this.nonce);
        }
    }

    /* loaded from: classes.dex */
    public class RestoreTransactions extends BillingRequest {
        long nonce;

        public RestoreTransactions() {
            super(-1);
        }

        @Override // com.vivamedia.CMTablet.GoogleBillingService.BillingRequest
        protected long run() throws RemoteException {
            this.nonce = Security.generateNonce();
            Bundle request = makeRequestBundle("RESTORE_TRANSACTIONS");
            request.putLong(GoogleBillingService.BILLING_REQUEST_NONCE, this.nonce);
            Bundle response = GoogleBillingService.marketBillingService.sendBillingRequest(request);
            logResponseCode("restoreTransactions", response);
            return response.getLong(GoogleBillingService.BILLING_RESPONSE_REQUEST_ID, -1L);
        }

        @Override // com.vivamedia.CMTablet.GoogleBillingService.BillingRequest
        protected void onRemoteException(RemoteException e) {
            super.onRemoteException(e);
            Security.removeNonce(this.nonce);
        }

        @Override // com.vivamedia.CMTablet.GoogleBillingService.BillingRequest
        protected void responseCodeReceived(ResponseCode responseCode) {
            GoogleResponseHandler.responseCodeReceived(GoogleBillingService.this, this, responseCode);
        }
    }

    public void setContext(Context context) {
        attachBaseContext(context);
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int startId) {
        handleCommand(intent, startId);
    }

    public void handleCommand(Intent intent, int startId) {
        String action;
        if (intent != null && (action = intent.getAction()) != null) {
            if (ACTION_CONFIRM_NOTIFICATION.equals(action)) {
                String[] notifyIds = intent.getStringArrayExtra(NOTIFICATION_ID);
                confirmNotifications(startId, notifyIds);
                return;
            }
            if (ACTION_GET_PURCHASE_INFORMATION.equals(action)) {
                String notifyId = intent.getStringExtra(NOTIFICATION_ID);
                getPurchaseInformation(startId, new String[]{notifyId});
                return;
            }
            if (ACTION_PURCHASE_STATE_CHANGED.equals(action)) {
                String signedData = intent.getStringExtra(INAPP_SIGNED_DATA);
                String signature = intent.getStringExtra(INAPP_SIGNATURE);
                purchaseStateChanged(startId, signedData, signature);
            } else if (ACTION_RESPONSE_CODE.equals(action)) {
                long requestId = intent.getLongExtra(INAPP_REQUEST_ID, -1L);
                int responseCodeIndex = intent.getIntExtra(INAPP_RESPONSE_CODE, ResponseCode.RESULT_ERROR.ordinal());
                ResponseCode responseCode = ResponseCode.valueOf(responseCodeIndex);
                checkResponseCode(requestId, responseCode);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean bindToMarketBillingService() {
        boolean bindResult;
        try {
            bindResult = bindService(new Intent(MARKET_BILLING_SERVICE_ACTION), this, 1);
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException: " + e);
        }
        return bindResult;
    }

    public boolean checkBillingSupported() {
        return new CheckBillingSupported().runRequest();
    }

    public boolean requestPurchase(String productId, String developerPayload) {
        return new RequestPurchase(productId, developerPayload).runRequest();
    }

    public boolean restoreTransactions() {
        return new RestoreTransactions().runRequest();
    }

    private boolean confirmNotifications(int startId, String[] notifyIds) {
        return new ConfirmNotifications(startId, notifyIds).runRequest();
    }

    private boolean getPurchaseInformation(int startId, String[] notifyIds) {
        return new GetPurchaseInformation(startId, notifyIds).runRequest();
    }

    private void purchaseStateChanged(int startId, String signedData, String signature) {
        ArrayList<Security.VerifiedPurchase> purchases = Security.verifyPurchase(signedData, signature);
        if (purchases != null) {
            ArrayList<String> notifyList = new ArrayList<>();
            Iterator<Security.VerifiedPurchase> it = purchases.iterator();
            while (it.hasNext()) {
                Security.VerifiedPurchase vp = it.next();
                if (vp.notificationId != null) {
                    notifyList.add(vp.notificationId);
                }
                GoogleResponseHandler.purchaseResponse(this, vp.purchaseState, vp.productId, vp.orderId, vp.purchaseTime, vp.developerPayload);
            }
            if (!notifyList.isEmpty()) {
                String[] notifyIds = (String[]) notifyList.toArray(new String[notifyList.size()]);
                confirmNotifications(startId, notifyIds);
            }
        }
    }

    private void checkResponseCode(long requestId, ResponseCode responseCode) {
        BillingRequest request = sentRequests.get(Long.valueOf(requestId));
        if (request != null) {
            request.responseCodeReceived(responseCode);
        }
        sentRequests.remove(Long.valueOf(requestId));
    }

    private void runPendingRequests() {
        int maxStartId = -1;
        while (true) {
            BillingRequest request = pendingRequests.peek();
            if (request != null) {
                if (request.runIfConnected()) {
                    pendingRequests.remove();
                    if (maxStartId < request.getStartId()) {
                        maxStartId = request.getStartId();
                    }
                } else {
                    bindToMarketBillingService();
                    return;
                }
            } else {
                if (maxStartId >= 0) {
                    stopSelf(maxStartId);
                    return;
                }
                return;
            }
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName name, IBinder service) {
        marketBillingService = IMarketBillingService.Stub.asInterface(service);
        runPendingRequests();
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName name) {
        marketBillingService = null;
    }

    public void unbind() {
        try {
            unbindService(this);
        } catch (IllegalArgumentException e) {
        }
    }
}
