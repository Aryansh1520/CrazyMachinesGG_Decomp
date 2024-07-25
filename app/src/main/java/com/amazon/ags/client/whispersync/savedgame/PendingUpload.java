package com.amazon.ags.client.whispersync.savedgame;

import java.util.Date;

/* loaded from: classes.dex */
public class PendingUpload {
    private final byte[] data;
    private final String description;
    private final Date saveTime;

    public PendingUpload(byte[] data, String description) {
        this(data, description, new Date());
    }

    public PendingUpload(byte[] data, String description, Date saveTime) {
        this.data = data;
        this.description = description;
        this.saveTime = new Date();
    }

    public final byte[] getData() {
        return this.data;
    }

    public final String getDescription() {
        return this.description;
    }

    public final Date getSaveTime() {
        return this.saveTime;
    }
}
