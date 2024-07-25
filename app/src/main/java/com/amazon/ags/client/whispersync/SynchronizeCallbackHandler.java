package com.amazon.ags.client.whispersync;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.whispersync.SynchronizeCallback;
import com.amazon.ags.client.whispersync.savedgame.JsonSummaryMarshaller;
import com.amazon.ags.client.whispersync.savedgame.PendingDownload;
import com.amazon.ags.client.whispersync.savedgame.SummaryMarshaller;
import com.amazon.ags.client.whispersync.savedgame.SummaryRepository;
import com.amazon.ags.constants.WhisperSyncBindingKeys;
import com.amazon.ags.constants.whispersync.SynchronizeResultKey;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class SynchronizeCallbackHandler extends Handler {
    private static final String FEATURE_NAME = "STC";
    private static final String TAG = "STC_" + SynchronizeCallbackHandler.class.getSimpleName();
    private SynchronizeCallback callback;
    private final SummaryMarshaller marshaller = new JsonSummaryMarshaller();
    private final SummaryRepository summaryRepository;

    protected abstract boolean processDownloadedData(byte[] bArr);

    public SynchronizeCallbackHandler(SynchronizeCallback callback, SummaryRepository summaryRepository) {
        this.callback = callback;
        this.summaryRepository = summaryRepository;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message msg) {
        Log.i(TAG, "Handling Synchronize response message");
        Bundle bundle = msg.getData();
        if (bundle == null) {
            this.callback.onSynchronizeFailure(ErrorCode.UNRECOVERABLE);
        }
        String resultString = bundle.getString(WhisperSyncBindingKeys.WS_SYNCHRONIZE_RESULT_BUNDLE_KEY);
        if (resultString == null) {
            this.callback.onSynchronizeFailure(ErrorCode.UNRECOVERABLE);
            Log.e(TAG, "Received null synchronize result");
            return;
        }
        try {
            SynchronizeResultKey result = SynchronizeResultKey.valueOf(resultString);
            switch (result) {
                case ALREADY_SYNCHRONIZED:
                    Log.d(TAG, "Already Synchronized");
                    this.callback.onAlreadySynchronized();
                    return;
                case CONFLICT_DEFERRED:
                    Log.d(TAG, "Conflict Deferred");
                    this.callback.onConflictDeferral();
                    return;
                case UPLOAD_SUCCESS:
                    processUpload(bundle);
                    return;
                case DOWNLOAD_SUCCESS:
                    processDownload(bundle);
                    return;
                case FAILURE:
                    Log.e(TAG, "Synchronize Failed");
                    int errorCode = bundle.getInt(WhisperSyncBindingKeys.WS_ERROR);
                    this.callback.onSynchronizeFailure(ErrorCode.fromServiceResponseCode(errorCode));
                    return;
                default:
                    Log.w(TAG, "Unexpected message received.  Result=" + result + " " + msg.toString());
                    return;
            }
        } catch (IllegalArgumentException e) {
            this.callback.onSynchronizeFailure(ErrorCode.UNRECOVERABLE);
            Log.e(TAG, "Received unrecognized synchronize result of: <" + resultString + ">");
        }
    }

    private void processUpload(Bundle bundle) {
        Log.d(TAG, "Upload Success");
        String cloudSummaryJson = bundle.getString(WhisperSyncBindingKeys.WS_LATEST_CLOUD_GAME_SUMMARY_KEY);
        GameSummary cloudSummary = null;
        if (cloudSummaryJson != null) {
            cloudSummary = this.marshaller.unmarshal(cloudSummaryJson);
        }
        this.summaryRepository.storeSummary(cloudSummary);
        this.summaryRepository.removePendingUpload();
        this.callback.onGameUploadSuccess();
    }

    private void processDownload(Bundle bundle) {
        Log.d(TAG, "Download Success");
        byte[] data = bundle.getByteArray(WhisperSyncBindingKeys.WS_DATA_BUNDLE_KEY);
        if (data == null || data.length == 0) {
            Log.e(TAG, "Expected downloaded data");
            this.callback.onSynchronizeFailure(ErrorCode.IO_ERROR);
            return;
        }
        Log.d(TAG, "Received callback DOWNLOAD_SUCCESS data.length = " + data.length);
        String summaryJson = bundle.getString(WhisperSyncBindingKeys.WS_LATEST_CLOUD_GAME_SUMMARY_KEY);
        GameSummary summary = this.marshaller.unmarshal(summaryJson);
        if (!storePendingDownload(data, summary)) {
            this.callback.onSynchronizeFailure(ErrorCode.IO_ERROR);
            return;
        }
        boolean applied = processDownloadedData(data);
        if (applied) {
            this.summaryRepository.storeSummary(summary);
            this.summaryRepository.removePendingUpload();
            this.summaryRepository.removePendingDownload();
        }
    }

    protected final boolean storePendingDownload(byte[] gameData, GameSummary summary) {
        PendingDownload download = new PendingDownload(gameData, summary);
        try {
            this.summaryRepository.storePendingDownload(download);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed storing PendingDownload");
            return false;
        }
    }
}
