package com.amazon.ags.client.whispersync;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.whispersync.RevertBlobCallback;
import com.amazon.ags.api.whispersync.RevertCallback;
import com.amazon.ags.api.whispersync.RevertMultiFileCallback;
import com.amazon.ags.api.whispersync.SynchronizeBlobCallback;
import com.amazon.ags.api.whispersync.SynchronizeBlobProgressRequest;
import com.amazon.ags.api.whispersync.SynchronizeBlobRequest;
import com.amazon.ags.api.whispersync.SynchronizeCallback;
import com.amazon.ags.api.whispersync.SynchronizeMultiFileCallback;
import com.amazon.ags.api.whispersync.SynchronizeMultiFileProgressRequest;
import com.amazon.ags.api.whispersync.SynchronizeMultiFileRequest;
import com.amazon.ags.api.whispersync.WhisperSyncClient;
import com.amazon.ags.client.AmazonGamesService;
import com.amazon.ags.client.whispersync.savedgame.FileBackedSummaryRepository;
import com.amazon.ags.client.whispersync.savedgame.JsonSummaryMarshaller;
import com.amazon.ags.client.whispersync.savedgame.PendingDownload;
import com.amazon.ags.client.whispersync.savedgame.PendingUpload;
import com.amazon.ags.client.whispersync.savedgame.SummaryMarshaller;
import com.amazon.ags.client.whispersync.savedgame.SummaryRepository;
import com.amazon.ags.constants.WhisperSyncBindingKeys;
import com.amazon.ags.constants.whispersync.ConflictStrategy;
import java.io.FilenameFilter;
import java.io.IOException;

/* loaded from: classes.dex */
public final class WhisperSyncServiceProxy implements WhisperSyncClient {
    private static final String FEATURE_NAME = "STC";
    private final AmazonGamesService amazonGamesService;
    private final Handler apiHandler;
    private final Context context;
    private GameSavePackager gamePackager;
    private final SummaryMarshaller summaryMarshaller = new JsonSummaryMarshaller();
    private final SummaryRepository summaryRepository;
    private static final String TAG = "STC_" + WhisperSyncServiceProxy.class.getSimpleName();
    private static final ConflictStrategy DEFAULT_CONFLICT_STRATEGY = ConflictStrategy.PLAYER_SELECT;

    public WhisperSyncServiceProxy(Context context, AmazonGamesService amazonGamesService, Handler apiHandler) {
        this.context = context;
        this.amazonGamesService = amazonGamesService;
        this.apiHandler = apiHandler;
        this.gamePackager = new GameSavePackager(context);
        this.summaryRepository = new FileBackedSummaryRepository(context, this.summaryMarshaller);
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public void setFilter(FilenameFilter filter) {
        this.gamePackager.setFilter(filter);
    }

    public void setPackager(GameSavePackager override) {
        this.gamePackager = override;
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public void synchronize(final SynchronizeBlobCallback callback) {
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.whispersync.WhisperSyncServiceProxy.1
            @Override // java.lang.Runnable
            public void run() {
                Handler callbackHandler = new SynchronizeBlobCallbackHandler(callback, WhisperSyncServiceProxy.this.summaryRepository);
                WhisperSyncServiceProxy.this.synchronizeInternal(callbackHandler, callback, WhisperSyncServiceProxy.DEFAULT_CONFLICT_STRATEGY);
            }
        });
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public void synchronize(final SynchronizeBlobRequest request) {
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.whispersync.WhisperSyncServiceProxy.2
            @Override // java.lang.Runnable
            public void run() {
                SynchronizeBlobCallback callback = request.getCallback();
                Handler callbackHandler = new SynchronizeBlobCallbackHandler(callback, WhisperSyncServiceProxy.this.summaryRepository);
                WhisperSyncServiceProxy.this.synchronizeInternal(callbackHandler, callback, request.getConflictStrategy());
            }
        });
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public void synchronize(final SynchronizeMultiFileCallback callback) {
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.whispersync.WhisperSyncServiceProxy.3
            @Override // java.lang.Runnable
            public void run() {
                Handler callbackHandler = new SynchronizeMultiFileCallbackHandler(callback, WhisperSyncServiceProxy.this.summaryRepository);
                WhisperSyncServiceProxy.this.synchronizeInternal(callbackHandler, callback, WhisperSyncServiceProxy.DEFAULT_CONFLICT_STRATEGY);
            }
        });
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public void synchronize(final SynchronizeMultiFileRequest request) {
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.whispersync.WhisperSyncServiceProxy.4
            @Override // java.lang.Runnable
            public void run() {
                SynchronizeMultiFileCallback callback = request.getCallback();
                Handler callbackHandler = new SynchronizeMultiFileCallbackHandler(callback, WhisperSyncServiceProxy.this.summaryRepository);
                WhisperSyncServiceProxy.this.synchronizeInternal(callbackHandler, callback, request.getConflictStrategy());
            }
        });
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public void synchronizeProgress(final SynchronizeBlobProgressRequest request) {
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.whispersync.WhisperSyncServiceProxy.5
            @Override // java.lang.Runnable
            public void run() {
                SynchronizeBlobCallback callback = request.getCallback();
                byte[] gameData = request.getData();
                if (gameData == null) {
                    Log.e(WhisperSyncServiceProxy.TAG, "No data to be saved");
                    callback.onSynchronizeFailure(ErrorCode.IO_ERROR);
                    return;
                }
                PendingUpload pendingUpload = new PendingUpload(gameData, request.getDescription());
                try {
                    WhisperSyncServiceProxy.this.summaryRepository.storePendingUpload(pendingUpload);
                    Handler callbackHandler = new SynchronizeBlobCallbackHandler(callback, WhisperSyncServiceProxy.this.summaryRepository);
                    WhisperSyncServiceProxy.this.synchronizeInternal(callbackHandler, callback, request.getConflictStrategy());
                } catch (IOException e) {
                    callback.onSynchronizeFailure(ErrorCode.IO_ERROR);
                }
            }
        });
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public void synchronizeProgress(final SynchronizeMultiFileProgressRequest request) {
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.whispersync.WhisperSyncServiceProxy.6
            @Override // java.lang.Runnable
            public void run() {
                SynchronizeMultiFileCallback callback = request.getCallback();
                try {
                    byte[] zipData = WhisperSyncServiceProxy.this.getPackager(request).pack();
                    if (zipData == null) {
                        Log.e(WhisperSyncServiceProxy.TAG, "No data to be saved");
                        callback.onSynchronizeFailure(ErrorCode.IO_ERROR);
                        return;
                    }
                    PendingUpload pendingUpload = new PendingUpload(zipData, request.getDescription());
                    try {
                        WhisperSyncServiceProxy.this.summaryRepository.storePendingUpload(pendingUpload);
                        Handler callbackHandler = new SynchronizeMultiFileCallbackHandler(callback, WhisperSyncServiceProxy.this.summaryRepository);
                        WhisperSyncServiceProxy.this.synchronizeInternal(callbackHandler, callback, request.getConflictStrategy());
                    } catch (IOException e) {
                        callback.onSynchronizeFailure(ErrorCode.IO_ERROR);
                    }
                } catch (IOException e2) {
                    Log.e(WhisperSyncServiceProxy.TAG, "Exception in asynchronous save: ", e2);
                    callback.onSynchronizeFailure(ErrorCode.IO_ERROR);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void synchronizeInternal(Handler callbackHandler, SynchronizeCallback callback, ConflictStrategy conflictStrategy) {
        if (callback == null) {
            throw new IllegalArgumentException("A Callback must be provided to synchronize");
        }
        if (!this.amazonGamesService.isReady()) {
            callback.onSynchronizeFailure(ErrorCode.SERVICE_NOT_READY);
            return;
        }
        try {
            PendingUpload pendingUpload = this.summaryRepository.retrievePendingUpload();
            Message msg = Message.obtain();
            msg.what = 21;
            msg.replyTo = new Messenger(callbackHandler);
            Bundle bundle = createRequestBundleWithData(pendingUpload.getData(), pendingUpload.getDescription());
            bundle.putLong(WhisperSyncBindingKeys.WS_SAVE_TIME_BUNDLE_KEY, pendingUpload.getSaveTime().getTime());
            GameSummary localSummary = this.summaryRepository.retrieveSummary();
            String localSummaryJson = this.summaryMarshaller.marshal(localSummary);
            bundle.putString(WhisperSyncBindingKeys.WS_LATEST_LOCAL_GAME_SUMMARY_KEY, localSummaryJson);
            bundle.putString(WhisperSyncBindingKeys.WS_CONFLICT_STRATEGY_BUNDLE_KEY, conflictStrategy.toString());
            msg.setData(bundle);
            try {
                this.amazonGamesService.sendMessage(msg);
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to send Message to Service: ", e);
                callback.onSynchronizeFailure(ErrorCode.UNRECOVERABLE);
            }
        } catch (IOException e2) {
            callback.onSynchronizeFailure(ErrorCode.IO_ERROR);
        }
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public void requestRevert(final RevertBlobCallback callback) {
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.whispersync.WhisperSyncServiceProxy.7
            @Override // java.lang.Runnable
            public void run() {
                Handler handler = new RevertBlobCallbackHandler(callback, WhisperSyncServiceProxy.this.summaryRepository);
                WhisperSyncServiceProxy.this.requestRevert(callback, handler);
            }
        });
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public void requestRevert(final RevertMultiFileCallback callback) {
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.whispersync.WhisperSyncServiceProxy.8
            @Override // java.lang.Runnable
            public void run() {
                Handler handler = new RevertMultiFileCallbackHandler(callback, WhisperSyncServiceProxy.this.summaryRepository);
                WhisperSyncServiceProxy.this.requestRevert(callback, handler);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestRevert(RevertCallback callback, Handler callbackHandler) {
        if (!this.amazonGamesService.isReady()) {
            callback.onRevertFailure(ErrorCode.SERVICE_NOT_READY);
            return;
        }
        Message msg = Message.obtain();
        msg.what = 22;
        msg.replyTo = new Messenger(callbackHandler);
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        try {
            this.amazonGamesService.sendMessage(msg);
        } catch (RemoteException e) {
            Log.e(TAG, "Unable to send Message to Service: ", e);
            callback.onRevertFailure(ErrorCode.UNRECOVERABLE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public GameSavePackager getPackager(SynchronizeMultiFileProgressRequest request) {
        return request.getFilter() != null ? new GameSavePackager(this.context, request.getFilter()) : this.gamePackager;
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public final boolean hasNewMultiFileGameData() {
        return this.summaryRepository.hasPendingDownload();
    }

    @Override // com.amazon.ags.api.whispersync.WhisperSyncClient
    public final void unpackNewMultiFileGameData() throws IOException {
        PendingDownload pendingDownload = this.summaryRepository.retrievePendingDownload();
        if (pendingDownload != null) {
            this.gamePackager.unpack(pendingDownload.getData());
            this.summaryRepository.removePendingDownload();
            this.summaryRepository.storeSummary(pendingDownload.getSummary());
            Log.d(TAG, "Successfully unpacked new multi-file game data");
            return;
        }
        Log.d(TAG, "Tried to unpack, but no data exists.  Ignoring.");
    }

    private Bundle createRequestBundleWithData(byte[] data, String description) {
        Bundle bundle = new Bundle();
        bundle.putByteArray(WhisperSyncBindingKeys.WS_DATA_BUNDLE_KEY, data);
        bundle.putString(WhisperSyncBindingKeys.WS_DESCRIPTION_BUNDLE_KEY, description);
        return bundle;
    }
}
