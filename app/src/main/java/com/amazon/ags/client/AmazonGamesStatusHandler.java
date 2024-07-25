package com.amazon.ags.client;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.amazon.ags.api.AmazonGamesCallback;
import com.amazon.ags.api.AmazonGamesStatus;

/* loaded from: classes.dex */
public class AmazonGamesStatusHandler extends Handler {
    private static final String FEATURE_NAME = "AGC";
    private static final String TAG = "AGC_" + AmazonGamesStatusHandler.class.getSimpleName();
    private AmazonGamesCallback amazonGamesCallback;

    public AmazonGamesStatusHandler(AmazonGamesCallback amazonGamesCallback) {
        this.amazonGamesCallback = amazonGamesCallback;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message msg) {
        if (msg.obj instanceof AmazonGamesStatus) {
            AmazonGamesStatus status = (AmazonGamesStatus) msg.obj;
            Log.i(TAG, "Game received status update of: " + status);
            if (this.amazonGamesCallback != null) {
                if (status == AmazonGamesStatus.SERVICE_CONNECTED) {
                    this.amazonGamesCallback.onServiceReady();
                    return;
                } else {
                    this.amazonGamesCallback.onServiceNotReady(status);
                    return;
                }
            }
            return;
        }
        Log.e(TAG, "Expecting message object of AmazonGamesStatus, received: " + msg.obj);
    }
}
