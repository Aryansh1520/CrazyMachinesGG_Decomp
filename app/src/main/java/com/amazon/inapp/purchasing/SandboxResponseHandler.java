package com.amazon.inapp.purchasing;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.amazon.inapp.purchasing.GetUserIdResponse;
import com.amazon.inapp.purchasing.Item;
import com.amazon.inapp.purchasing.ItemDataResponse;
import com.amazon.inapp.purchasing.PurchaseResponse;
import com.amazon.inapp.purchasing.PurchaseUpdatesResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
final class SandboxResponseHandler implements ResponseHandler {
    private static final String TAG = "SandboxResponseHandler";
    private final HandlerAdapter _handler = HandlerManager.getMainHandlerAdapter();

    SandboxResponseHandler() {
    }

    private Item getItem(String str, JSONObject jSONObject) {
        return new Item(str, jSONObject.optString("price"), Item.ItemType.valueOf(jSONObject.optString("itemType")), jSONObject.optString("title"), jSONObject.optString("description"), jSONObject.optString("smallIconUrl"));
    }

    private ItemDataResponse getItemDataResponse(Intent intent) {
        Exception exc;
        HashSet hashSet;
        ItemDataResponse.ItemDataRequestStatus itemDataRequestStatus;
        String str;
        HashSet hashSet2;
        HashMap hashMap;
        HashMap hashMap2 = null;
        ItemDataResponse.ItemDataRequestStatus itemDataRequestStatus2 = ItemDataResponse.ItemDataRequestStatus.FAILED;
        try {
            JSONObject jSONObject = new JSONObject(intent.getStringExtra("itemDataOutput"));
            String optString = jSONObject.optString("requestId");
            try {
                itemDataRequestStatus2 = ItemDataResponse.ItemDataRequestStatus.valueOf(jSONObject.optString("status"));
                if (itemDataRequestStatus2 != ItemDataResponse.ItemDataRequestStatus.FAILED) {
                    hashSet = new HashSet();
                    try {
                        hashMap = new HashMap();
                    } catch (Exception e) {
                        itemDataRequestStatus = itemDataRequestStatus2;
                        str = optString;
                        exc = e;
                    }
                    try {
                        JSONArray optJSONArray = jSONObject.optJSONArray("unavailableSkus");
                        if (optJSONArray != null) {
                            for (int i = 0; i < optJSONArray.length(); i++) {
                                hashSet.add(optJSONArray.getString(i));
                            }
                        }
                        JSONObject optJSONObject = jSONObject.optJSONObject("items");
                        if (optJSONObject != null) {
                            Iterator<String> keys = optJSONObject.keys();
                            while (keys.hasNext()) {
                                String next = keys.next();
                                hashMap.put(next, getItem(next, optJSONObject.optJSONObject(next)));
                            }
                        }
                        hashMap2 = hashMap;
                        hashSet2 = hashSet;
                    } catch (Exception e2) {
                        hashMap2 = hashMap;
                        itemDataRequestStatus = itemDataRequestStatus2;
                        str = optString;
                        exc = e2;
                        Log.e(TAG, "Error parsing item data output", exc);
                        return new ItemDataResponse(str, hashSet, itemDataRequestStatus, hashMap2);
                    }
                } else {
                    hashSet2 = null;
                }
                hashSet = hashSet2;
                itemDataRequestStatus = itemDataRequestStatus2;
                str = optString;
            } catch (Exception e3) {
                hashSet = null;
                ItemDataResponse.ItemDataRequestStatus itemDataRequestStatus3 = itemDataRequestStatus2;
                str = optString;
                exc = e3;
                itemDataRequestStatus = itemDataRequestStatus3;
            }
        } catch (Exception e4) {
            exc = e4;
            hashSet = null;
            itemDataRequestStatus = itemDataRequestStatus2;
            str = null;
        }
        return new ItemDataResponse(str, hashSet, itemDataRequestStatus, hashMap2);
    }

    private PurchaseResponse getPurchaseResponse(Intent intent) {
        String str;
        String str2;
        PurchaseResponse.PurchaseRequestStatus purchaseRequestStatus;
        Exception e;
        Receipt receipt = null;
        PurchaseResponse.PurchaseRequestStatus purchaseRequestStatus2 = PurchaseResponse.PurchaseRequestStatus.FAILED;
        try {
            JSONObject jSONObject = new JSONObject(intent.getStringExtra("purchaseOutput"));
            str2 = jSONObject.optString("requestId");
            try {
                str = jSONObject.optString("userId");
                try {
                    purchaseRequestStatus = PurchaseResponse.PurchaseRequestStatus.valueOf(jSONObject.optString("purchaseStatus"));
                    try {
                        JSONObject optJSONObject = jSONObject.optJSONObject("receipt");
                        if (optJSONObject != null) {
                            receipt = getReceipt(optJSONObject);
                        }
                    } catch (Exception e2) {
                        e = e2;
                        Log.e(TAG, "Error parsing purchase output", e);
                        return new PurchaseResponse(str2, str, receipt, purchaseRequestStatus);
                    }
                } catch (Exception e3) {
                    purchaseRequestStatus = purchaseRequestStatus2;
                    e = e3;
                }
            } catch (Exception e4) {
                str = null;
                e = e4;
                purchaseRequestStatus = purchaseRequestStatus2;
            }
        } catch (Exception e5) {
            str = null;
            str2 = null;
            purchaseRequestStatus = purchaseRequestStatus2;
            e = e5;
        }
        return new PurchaseResponse(str2, str, receipt, purchaseRequestStatus);
    }

    private PurchaseUpdatesResponse getPurchaseUpdatesResponse(Intent intent) {
        Exception exc;
        HashSet hashSet;
        HashSet hashSet2;
        boolean z;
        String str;
        String str2;
        String str3;
        HashSet hashSet3;
        String str4 = null;
        HashSet hashSet4 = null;
        PurchaseUpdatesResponse.PurchaseUpdatesRequestStatus purchaseUpdatesRequestStatus = PurchaseUpdatesResponse.PurchaseUpdatesRequestStatus.FAILED;
        try {
            JSONObject jSONObject = new JSONObject(intent.getStringExtra("purchaseUpdatesOutput"));
            String optString = jSONObject.optString("requestId");
            try {
                purchaseUpdatesRequestStatus = PurchaseUpdatesResponse.PurchaseUpdatesRequestStatus.valueOf(jSONObject.optString("status"));
                String optString2 = jSONObject.optString("offset");
                try {
                    z = jSONObject.optBoolean("isMore");
                    try {
                        String optString3 = jSONObject.optString("userId");
                        try {
                            if (purchaseUpdatesRequestStatus == PurchaseUpdatesResponse.PurchaseUpdatesRequestStatus.SUCCESSFUL) {
                                HashSet hashSet5 = new HashSet();
                                try {
                                    hashSet3 = new HashSet();
                                } catch (Exception e) {
                                    str2 = optString2;
                                    hashSet2 = hashSet5;
                                    str = optString3;
                                    hashSet = null;
                                    str4 = optString;
                                    exc = e;
                                }
                                try {
                                    JSONArray optJSONArray = jSONObject.optJSONArray("receipts");
                                    if (optJSONArray != null) {
                                        for (int i = 0; i < optJSONArray.length(); i++) {
                                            hashSet5.add(getReceipt(optJSONArray.optJSONObject(i)));
                                        }
                                    }
                                    JSONArray optJSONArray2 = jSONObject.optJSONArray("revokedSkus");
                                    if (optJSONArray2 != null) {
                                        for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                                            hashSet3.add(optJSONArray2.getString(i2));
                                        }
                                    }
                                    hashSet4 = hashSet5;
                                } catch (Exception e2) {
                                    str4 = optString;
                                    exc = e2;
                                    str = optString3;
                                    hashSet = hashSet3;
                                    str2 = optString2;
                                    hashSet2 = hashSet5;
                                    Log.e(TAG, "Error parsing purchase updates output", exc);
                                    str3 = str2;
                                    return new PurchaseUpdatesResponse(str4, str, purchaseUpdatesRequestStatus, hashSet2, hashSet, Offset.fromString(str3), z);
                                }
                            } else {
                                hashSet3 = null;
                            }
                            str = optString3;
                            hashSet = hashSet3;
                            hashSet2 = hashSet4;
                            str4 = optString;
                            str3 = optString2;
                        } catch (Exception e3) {
                            str = optString3;
                            hashSet = null;
                            str4 = optString;
                            exc = e3;
                            str2 = optString2;
                            hashSet2 = null;
                        }
                    } catch (Exception e4) {
                        hashSet = null;
                        str = null;
                        str4 = optString;
                        exc = e4;
                        str2 = optString2;
                        hashSet2 = null;
                    }
                } catch (Exception e5) {
                    hashSet = null;
                    z = false;
                    str = null;
                    str2 = optString2;
                    hashSet2 = null;
                    str4 = optString;
                    exc = e5;
                }
            } catch (Exception e6) {
                hashSet = null;
                hashSet2 = null;
                z = false;
                str = null;
                str4 = optString;
                exc = e6;
                str2 = null;
            }
        } catch (Exception e7) {
            exc = e7;
            hashSet = null;
            hashSet2 = null;
            z = false;
            str = null;
            str2 = null;
        }
        return new PurchaseUpdatesResponse(str4, str, purchaseUpdatesRequestStatus, hashSet2, hashSet, Offset.fromString(str3), z);
    }

    private Receipt getReceipt(JSONObject jSONObject) throws ParseException {
        SubscriptionPeriod subscriptionPeriod;
        Date date = null;
        String optString = jSONObject.optString("sku");
        Item.ItemType valueOf = Item.ItemType.valueOf(jSONObject.optString("itemType"));
        JSONObject optJSONObject = jSONObject.optJSONObject("subscripionPeriod");
        if (valueOf == Item.ItemType.SUBSCRIPTION) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date parse = simpleDateFormat.parse(optJSONObject.optString("startTime"));
            String optString2 = optJSONObject.optString("endTime");
            if (optString2 != null && optString2.length() != 0) {
                date = simpleDateFormat.parse(optString2);
            }
            subscriptionPeriod = new SubscriptionPeriod(parse, date);
        } else {
            subscriptionPeriod = null;
        }
        return new Receipt(optString, valueOf, false, subscriptionPeriod, jSONObject.optString("token"));
    }

    private GetUserIdResponse getUserIdResponse(Intent intent) {
        String str;
        Exception e;
        GetUserIdResponse.GetUserIdRequestStatus getUserIdRequestStatus;
        String str2;
        GetUserIdResponse.GetUserIdRequestStatus getUserIdRequestStatus2;
        GetUserIdResponse.GetUserIdRequestStatus getUserIdRequestStatus3 = GetUserIdResponse.GetUserIdRequestStatus.FAILED;
        try {
            JSONObject jSONObject = new JSONObject(intent.getStringExtra("userOutput"));
            str = jSONObject.optString("requestId");
            try {
                getUserIdRequestStatus = GetUserIdResponse.GetUserIdRequestStatus.valueOf(jSONObject.optString("status"));
                try {
                    str2 = getUserIdRequestStatus == GetUserIdResponse.GetUserIdRequestStatus.SUCCESSFUL ? jSONObject.optString("userId") : null;
                    getUserIdRequestStatus2 = getUserIdRequestStatus;
                } catch (Exception e2) {
                    e = e2;
                    Log.e(TAG, "Error parsing userid output", e);
                    GetUserIdResponse.GetUserIdRequestStatus getUserIdRequestStatus4 = getUserIdRequestStatus;
                    str2 = null;
                    getUserIdRequestStatus2 = getUserIdRequestStatus4;
                    return new GetUserIdResponse(str, getUserIdRequestStatus2, str2);
                }
            } catch (Exception e3) {
                getUserIdRequestStatus = getUserIdRequestStatus3;
                e = e3;
            }
        } catch (Exception e4) {
            str = null;
            e = e4;
            getUserIdRequestStatus = getUserIdRequestStatus3;
        }
        return new GetUserIdResponse(str, getUserIdRequestStatus2, str2);
    }

    private void handleItemDataResponse(Intent intent) {
        final ItemDataResponse itemDataResponse = getItemDataResponse(intent);
        this._handler.post(new Runnable() { // from class: com.amazon.inapp.purchasing.SandboxResponseHandler.2
            @Override // java.lang.Runnable
            public void run() {
                if (Logger.isTraceOn()) {
                    Logger.trace(SandboxResponseHandler.TAG, "Running Runnable for itemDataResponse with requestId: " + itemDataResponse.getRequestId());
                }
                PurchasingObserver purchasingObserver = PurchasingManager.getPurchasingObserver();
                if (purchasingObserver != null) {
                    purchasingObserver.onItemDataResponse(itemDataResponse);
                }
            }
        });
    }

    private void handlePurchaseResponse(Intent intent) {
        final PurchaseResponse purchaseResponse = getPurchaseResponse(intent);
        this._handler.post(new Runnable() { // from class: com.amazon.inapp.purchasing.SandboxResponseHandler.4
            @Override // java.lang.Runnable
            public void run() {
                if (Logger.isTraceOn()) {
                    Logger.trace(SandboxResponseHandler.TAG, "Running Runnable for purchaseResponse with requestId: " + purchaseResponse.getRequestId());
                }
                PurchasingObserver purchasingObserver = PurchasingManager.getPurchasingObserver();
                if (purchasingObserver != null) {
                    purchasingObserver.onPurchaseResponse(purchaseResponse);
                }
            }
        });
    }

    private void handlePurchaseUpdatesResponse(Intent intent) {
        final PurchaseUpdatesResponse purchaseUpdatesResponse = getPurchaseUpdatesResponse(intent);
        this._handler.post(new Runnable() { // from class: com.amazon.inapp.purchasing.SandboxResponseHandler.1
            @Override // java.lang.Runnable
            public void run() {
                if (Logger.isTraceOn()) {
                    Logger.trace(SandboxResponseHandler.TAG, "Running Runnable for purchaseUpdatesResponse with requestId: " + purchaseUpdatesResponse.getRequestId());
                }
                PurchasingObserver purchasingObserver = PurchasingManager.getPurchasingObserver();
                if (purchasingObserver != null) {
                    purchasingObserver.onPurchaseUpdatesResponse(purchaseUpdatesResponse);
                }
            }
        });
    }

    private void handleUserIdResponse(Intent intent) {
        final GetUserIdResponse userIdResponse = getUserIdResponse(intent);
        this._handler.post(new Runnable() { // from class: com.amazon.inapp.purchasing.SandboxResponseHandler.3
            @Override // java.lang.Runnable
            public void run() {
                if (Logger.isTraceOn()) {
                    Logger.trace(SandboxResponseHandler.TAG, "Running Runnable for userIdResponse with requestId: " + userIdResponse.getRequestId());
                }
                PurchasingObserver purchasingObserver = PurchasingManager.getPurchasingObserver();
                if (purchasingObserver != null) {
                    purchasingObserver.onGetUserIdResponse(userIdResponse);
                }
            }
        });
    }

    @Override // com.amazon.inapp.purchasing.ResponseHandler
    public void handleResponse(Context context, Intent intent) {
        if (Logger.isTraceOn()) {
            Logger.trace(TAG, "handleResponse");
        }
        try {
            String string = intent.getExtras().getString("responseType");
            if (string.equalsIgnoreCase("com.amazon.testclient.iap.purchase")) {
                handlePurchaseResponse(intent);
                return;
            }
            if (string.equalsIgnoreCase("com.amazon.testclient.iap.appUserId")) {
                handleUserIdResponse(intent);
            } else if (string.equalsIgnoreCase("com.amazon.testclient.iap.itemData")) {
                handleItemDataResponse(intent);
            } else if (string.equalsIgnoreCase("com.amazon.testclient.iap.purchaseUpdates")) {
                handlePurchaseUpdatesResponse(intent);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error handling response.", e);
        }
    }
}
