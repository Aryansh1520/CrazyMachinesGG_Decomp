package com.amazon.ags.api.whispersync;

import com.amazon.ags.constants.whispersync.ConflictStrategy;
import java.io.FilenameFilter;

/* loaded from: classes.dex */
public class SynchronizeMultiFileProgressRequest extends SynchronizeMultiFileRequest {
    private static final ConflictStrategy DEFAULT_STRATEGY = ConflictStrategy.PLAYER_SELECT;
    private String description;
    private FilenameFilter filter;

    public SynchronizeMultiFileProgressRequest(SynchronizeMultiFileCallback callback) {
        super(callback);
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public final String getDescription() {
        return this.description;
    }

    public final void setFilter(FilenameFilter filter) {
        this.filter = filter;
    }

    public final FilenameFilter getFilter() {
        return this.filter;
    }
}
