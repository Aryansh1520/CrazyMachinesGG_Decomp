package com.amazon.ags.api.whispersync;

import com.amazon.ags.constants.whispersync.ConflictStrategy;

/* loaded from: classes.dex */
public class SynchronizeBlobRequest {
    private static final ConflictStrategy DEFAULT_STRATEGY = ConflictStrategy.PLAYER_SELECT;
    private SynchronizeBlobCallback callback;
    private ConflictStrategy conflictStrategy = DEFAULT_STRATEGY;

    public SynchronizeBlobRequest(SynchronizeBlobCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Callback may not be null");
        }
        this.callback = callback;
    }

    public final void setConflictStrategy(ConflictStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Conflict Strategy may not be null");
        }
        this.conflictStrategy = strategy;
    }

    public final ConflictStrategy getConflictStrategy() {
        return this.conflictStrategy;
    }

    public final SynchronizeBlobCallback getCallback() {
        return this.callback;
    }
}
