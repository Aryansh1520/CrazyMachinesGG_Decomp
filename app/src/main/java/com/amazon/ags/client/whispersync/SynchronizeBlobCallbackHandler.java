package com.amazon.ags.client.whispersync;

import com.amazon.ags.api.whispersync.SynchronizeBlobCallback;
import com.amazon.ags.client.whispersync.savedgame.SummaryRepository;

/* loaded from: classes.dex */
public class SynchronizeBlobCallbackHandler extends SynchronizeCallbackHandler {
    private static final String FEATURE_NAME = "STC";
    private static final String TAG = "STC_" + SynchronizeBlobCallbackHandler.class.getSimpleName();
    private SynchronizeBlobCallback callback;

    public SynchronizeBlobCallbackHandler(SynchronizeBlobCallback callback, SummaryRepository summaryRepository) {
        super(callback, summaryRepository);
        this.callback = callback;
    }

    @Override // com.amazon.ags.client.whispersync.SynchronizeCallbackHandler
    protected final boolean processDownloadedData(byte[] gameData) {
        return this.callback.onNewGameData(gameData);
    }
}
