package com.amazon.ags.client.whispersync;

import com.amazon.ags.api.whispersync.RevertBlobCallback;
import com.amazon.ags.client.whispersync.savedgame.SummaryRepository;

/* loaded from: classes.dex */
public class RevertBlobCallbackHandler extends RevertCallbackHandler {
    private RevertBlobCallback callback;

    public RevertBlobCallbackHandler(RevertBlobCallback callback, SummaryRepository summaryRepository) {
        super(callback, summaryRepository);
        this.callback = callback;
    }

    @Override // com.amazon.ags.client.whispersync.RevertCallbackHandler
    protected boolean processDownloadedData(byte[] gameData) {
        return this.callback.onRevertedGameData(gameData);
    }
}
