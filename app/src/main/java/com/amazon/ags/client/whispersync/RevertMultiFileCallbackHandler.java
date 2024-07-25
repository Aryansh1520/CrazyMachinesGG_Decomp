package com.amazon.ags.client.whispersync;

import com.amazon.ags.api.whispersync.RevertMultiFileCallback;
import com.amazon.ags.client.whispersync.savedgame.SummaryRepository;

/* loaded from: classes.dex */
public class RevertMultiFileCallbackHandler extends RevertCallbackHandler {
    private final RevertMultiFileCallback callback;

    public RevertMultiFileCallbackHandler(RevertMultiFileCallback callback, SummaryRepository summaryRepository) {
        super(callback, summaryRepository);
        this.callback = callback;
    }

    @Override // com.amazon.ags.client.whispersync.RevertCallbackHandler
    protected boolean processDownloadedData(byte[] gameData) {
        this.callback.onRevertedGameData();
        return false;
    }
}
