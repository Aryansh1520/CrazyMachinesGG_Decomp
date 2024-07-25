package com.amazon.ags.client.whispersync;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.whispersync.RevertCallback;
import com.amazon.ags.client.whispersync.savedgame.JsonSummaryMarshaller;
import com.amazon.ags.client.whispersync.savedgame.PendingDownload;
import com.amazon.ags.client.whispersync.savedgame.SummaryMarshaller;
import com.amazon.ags.client.whispersync.savedgame.SummaryRepository;
import com.amazon.ags.constants.WhisperSyncBindingKeys;
import com.amazon.ags.constants.whispersync.RevertResultKey;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class RevertCallbackHandler extends Handler {
    private static final String FEATURE_NAME = "STC";
    private static final String TAG = "STC_" + RevertCallbackHandler.class.getSimpleName();
    private RevertCallback callback;
    private final SummaryMarshaller marshaller = new JsonSummaryMarshaller();
    private final SummaryRepository summaryRepository;

    protected abstract boolean processDownloadedData(byte[] bArr);

    public RevertCallbackHandler(RevertCallback callback, SummaryRepository summaryRepository) {
        this.callback = callback;
        this.summaryRepository = summaryRepository;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message msg) {
        Log.i(TAG, "Handling revert blob response message");
        Bundle bundle = msg.getData();
        if (bundle == null) {
            this.callback.onRevertFailure(ErrorCode.UNRECOVERABLE);
        }
        String resultString = bundle.getString(WhisperSyncBindingKeys.WS_REVERT_RESULT_BUNDLE_KEY);
        if (resultString == null) {
            this.callback.onRevertFailure(ErrorCode.UNRECOVERABLE);
            Log.e(TAG, "Received null revert result");
            return;
        }
        try {
            RevertResultKey result = RevertResultKey.valueOf(resultString);
            switch (result) {
                case PLAYER_CANCELLED:
                    Log.d(TAG, "Revert cancelled by player");
                    this.callback.onPlayerCancelled();
                    return;
                case DOWNLOAD_SUCCESS:
                    processDownload(bundle);
                    return;
                case FAILURE:
                    Log.e(TAG, "Revert Failed");
                    int errorCode = bundle.getInt(WhisperSyncBindingKeys.WS_ERROR);
                    this.callback.onRevertFailure(ErrorCode.fromServiceResponseCode(errorCode));
                    return;
                default:
                    Log.w(TAG, "Unexpected message received.  Result=" + result + " " + msg.toString());
                    return;
            }
        } catch (IllegalArgumentException e) {
            this.callback.onRevertFailure(ErrorCode.UNRECOVERABLE);
            Log.e(TAG, "Received unrecognized revert result of: <" + resultString + ">");
        }
    }

    private void processDownload(Bundle bundle) {
        Log.d(TAG, "Revert Download Success");
        byte[] data = bundle.getByteArray(WhisperSyncBindingKeys.WS_DATA_BUNDLE_KEY);
        if (data == null || data.length == 0) {
            Log.e(TAG, "Expected downloaded gameData");
            this.callback.onRevertFailure(ErrorCode.IO_ERROR);
            return;
        }
        Log.d(TAG, "Received callback DOWNLOAD_SUCCESS data.length = " + data.length);
        String summaryJson = bundle.getString(WhisperSyncBindingKeys.WS_LATEST_CLOUD_GAME_SUMMARY_KEY);
        GameSummary summary = this.marshaller.unmarshal(summaryJson);
        if (!storePendingDownload(data, summary)) {
            this.callback.onRevertFailure(ErrorCode.IO_ERROR);
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
