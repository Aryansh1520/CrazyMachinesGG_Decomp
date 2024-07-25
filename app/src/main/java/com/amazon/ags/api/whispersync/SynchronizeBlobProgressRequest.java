package com.amazon.ags.api.whispersync;

import com.amazon.ags.constants.whispersync.ConflictStrategy;

/* loaded from: classes.dex */
public class SynchronizeBlobProgressRequest extends SynchronizeBlobRequest {
    private static final ConflictStrategy DEFAULT_STRATEGY = ConflictStrategy.PLAYER_SELECT;
    private byte[] data;
    private String description;

    public SynchronizeBlobProgressRequest(SynchronizeBlobCallback callback) {
        super(callback);
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public final String getDescription() {
        return this.description;
    }

    public final void setData(byte[] data) {
        this.data = data;
    }

    public final byte[] getData() {
        return this.data;
    }
}
