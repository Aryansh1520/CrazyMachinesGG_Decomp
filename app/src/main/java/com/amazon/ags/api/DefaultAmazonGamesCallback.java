package com.amazon.ags.api;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/* loaded from: classes.dex */
public class DefaultAmazonGamesCallback implements AmazonGamesCallback {
    private static final String FEATURE_NAME = "AGC";
    private static final String SERVICE_CONNECTED = "Client Ready";
    private static final String SERVICE_DISCONNECTED = "Client not Ready: ";
    private static final String TAG = "AGC_" + DefaultAmazonGamesCallback.class.getSimpleName();
    private final Context context;

    public DefaultAmazonGamesCallback(Context context) {
        this.context = context;
    }

    @Override // com.amazon.ags.api.AmazonGamesCallback
    public void onServiceReady() {
        updateStatus(SERVICE_CONNECTED);
    }

    @Override // com.amazon.ags.api.AmazonGamesCallback
    public void onServiceNotReady(AmazonGamesStatus reason) {
        updateStatus(SERVICE_DISCONNECTED + reason);
    }

    private void updateStatus(String message) {
        Log.d(TAG, message);
        Toast.makeText(this.context, message, 0).show();
    }
}
