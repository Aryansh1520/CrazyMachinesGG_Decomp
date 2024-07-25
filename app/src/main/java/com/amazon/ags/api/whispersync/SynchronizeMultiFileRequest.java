package com.amazon.ags.api.whispersync;

import com.amazon.ags.constants.whispersync.ConflictStrategy;

/* loaded from: classes.dex */
public class SynchronizeMultiFileRequest {
    private static final ConflictStrategy DEFAULT_STRATEGY = ConflictStrategy.PLAYER_SELECT;
    private SynchronizeMultiFileCallback callback;
    private ConflictStrategy conflictStrategy = DEFAULT_STRATEGY;

    public SynchronizeMultiFileRequest(SynchronizeMultiFileCallback callback) {
        this.callback = callback;
    }

    public final SynchronizeMultiFileCallback getCallback() {
        return this.callback;
    }

    public final void setConflictStrategy(ConflictStrategy conflictStrategy) {
        this.conflictStrategy = conflictStrategy;
    }

    public final ConflictStrategy getConflictStrategy() {
        return this.conflictStrategy;
    }
}
