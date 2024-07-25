package com.amazon.ags.client.whispersync.savedgame;

import com.amazon.ags.client.whispersync.GameSummary;
import java.util.Date;

/* loaded from: classes.dex */
public class SimpleGameSummary implements GameSummary {
    private String description;
    private String device;
    private String md5;
    private Date saveDate;
    private String version;

    public SimpleGameSummary(String version, String md5, Date saveDate, String description, String device) {
        this.device = device;
        this.md5 = md5;
        this.saveDate = saveDate;
        this.version = version;
        this.description = description;
    }

    @Override // com.amazon.ags.client.whispersync.GameSummary
    public final String getDevice() {
        return this.device;
    }

    @Override // com.amazon.ags.client.whispersync.GameSummary
    public final String getMd5() {
        return this.md5;
    }

    @Override // com.amazon.ags.client.whispersync.GameSummary
    public final Date getSaveDate() {
        return this.saveDate;
    }

    @Override // com.amazon.ags.client.whispersync.GameSummary
    public final String getVersion() {
        return this.version;
    }

    @Override // com.amazon.ags.client.whispersync.GameSummary
    public final String getDescription() {
        return this.description;
    }
}
