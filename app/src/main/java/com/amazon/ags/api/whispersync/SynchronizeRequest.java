package com.amazon.ags.api.whispersync;

import com.amazon.ags.constants.whispersync.ConflictStrategy;

/* loaded from: classes.dex */
public abstract class SynchronizeRequest {
    private static final ConflictStrategy DEFAULT_STRATEGY = ConflictStrategy.PLAYER_SELECT;
    private ConflictStrategy conflictStrategy = DEFAULT_STRATEGY;
    private String description;

    public final void setDescription(String description) {
        this.description = description;
    }

    public final String getDescription() {
        return this.description;
    }

    public void setConflictStrategy(ConflictStrategy conflictStrategy) {
        this.conflictStrategy = conflictStrategy;
    }

    public ConflictStrategy getConflictStrategy() {
        return this.conflictStrategy;
    }
}
