package com.amazon.ags.client.whispersync.savedgame;

import com.amazon.ags.client.whispersync.GameSummary;

/* loaded from: classes.dex */
public class PendingDownload {
    private final byte[] data;
    private final GameSummary summary;

    public PendingDownload(byte[] data, GameSummary summary) {
        this.data = data;
        this.summary = summary;
    }

    public final byte[] getData() {
        return this.data;
    }

    public final GameSummary getSummary() {
        return this.summary;
    }
}
