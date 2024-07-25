package com.chartboost.sdk.Analytics;

import android.content.Context;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Networking.CBAPIConnection;
import com.chartboost.sdk.Networking.CBAPIRequest;
import java.math.BigDecimal;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CBAnalytics {
    public static final String TAG = "Chartboost Analytics";
    private static CBAnalytics sharedAnalytics = null;
    private CBAPIConnection connection = new CBAPIConnection(null, null, "CBAnalytics");

    @Deprecated
    public static synchronized CBAnalytics getSharedAnalytics(Context Context) {
        CBAnalytics sharedAnalytics2;
        synchronized (CBAnalytics.class) {
            sharedAnalytics2 = sharedAnalytics();
        }
        return sharedAnalytics2;
    }

    public static synchronized CBAnalytics sharedAnalytics() {
        CBAnalytics cBAnalytics;
        synchronized (CBAnalytics.class) {
            if (sharedAnalytics == null) {
                sharedAnalytics = new CBAnalytics();
            }
            cBAnalytics = sharedAnalytics;
        }
        return cBAnalytics;
    }

    private CBAnalytics() {
        this.connection.retryDeliveries();
    }

    private String round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return new StringBuilder(String.valueOf(rounded.doubleValue())).toString();
    }

    public Boolean recordPaymentTransaction(String sku, String title, double price, String currency, int quantity, Map<String, Object> meta) {
        Chartboost cb = Chartboost.sharedChartboost();
        if (cb.getContext() == null) {
            throw new IllegalStateException("The context must be set through the Chartboost method onCreate() before calling recordPaymentTransaction().");
        }
        if (cb.appId == null || cb.appSignature == null) {
            return false;
        }
        CBAPIRequest request = new CBAPIRequest("api", "purchase");
        request.appendDeviceInfoParams();
        request.appendBodyArgument("product_id", sku);
        request.appendBodyArgument("title", title);
        request.appendBodyArgument("price", round(price, 2, 4));
        request.appendBodyArgument("currency", currency);
        request.appendBodyArgument("quantity", new StringBuilder(String.valueOf(quantity)).toString());
        request.appendBodyArgument("timestamp", new StringBuilder(String.valueOf(System.currentTimeMillis() / 1000.0d)).toString());
        if (meta != null) {
            JSONObject jsonObj = new JSONObject(meta);
            request.appendBodyArgument("meta", jsonObj.toString());
        }
        request.sign(cb.appId, cb.appSignature);
        this.connection.sendRequest(request);
        return true;
    }

    public Boolean trackEvent(String eventIdentifier) {
        trackEvent(eventIdentifier, 1.0d, null);
        return true;
    }

    public Boolean trackEvent(String eventIdentifier, double value) {
        trackEvent(eventIdentifier, value, null);
        return true;
    }

    public Boolean trackEvent(String eventIdentifier, double value, Map<String, Object> meta) {
        Chartboost cb = Chartboost.sharedChartboost();
        if (cb.getContext() == null) {
            throw new IllegalStateException("The context must be set through the Chartboost method onCreate() before calling trackEvent().");
        }
        if (cb.appId == null || cb.appSignature == null) {
            return false;
        }
        CBAPIRequest request = new CBAPIRequest("api", "event");
        request.appendDeviceInfoParams();
        request.appendBodyArgument("key", eventIdentifier);
        request.appendBodyArgument("value", new StringBuilder(String.valueOf(value)).toString());
        request.appendBodyArgument("timestamp", new StringBuilder(String.valueOf(System.currentTimeMillis() / 1000.0d)).toString());
        if (meta != null) {
            JSONObject jsonObj = new JSONObject(meta);
            request.appendBodyArgument("meta", jsonObj.toString());
        }
        request.sign(cb.appId, cb.appSignature);
        request.ensureDelivery = true;
        this.connection.sendRequest(request);
        return true;
    }
}
