package com.amazon.ags.client.whispersync;

import com.amazon.ags.api.whispersync.SynchronizeMultiFileCallback;
import com.amazon.ags.client.whispersync.savedgame.SummaryRepository;

/* loaded from: classes.dex */
public class SynchronizeMultiFileCallbackHandler extends SynchronizeCallbackHandler {
    private SynchronizeMultiFileCallback callback;

    public SynchronizeMultiFileCallbackHandler(SynchronizeMultiFileCallback callback, SummaryRepository summaryRepository) {
        super(callback, summaryRepository);
        this.callback = callback;
    }

    @Override // com.amazon.ags.client.whispersync.SynchronizeCallbackHandler
    protected final boolean processDownloadedData(byte[] gameData) {
        this.callback.onNewGameData();
        return false;
    }
}
