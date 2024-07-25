package com.amazon.ags.api.whispersync;

import java.io.FilenameFilter;
import java.io.IOException;

/* loaded from: classes.dex */
public interface WhisperSyncClient {
    boolean hasNewMultiFileGameData();

    void requestRevert(RevertBlobCallback revertBlobCallback);

    void requestRevert(RevertMultiFileCallback revertMultiFileCallback);

    void setFilter(FilenameFilter filenameFilter);

    void synchronize(SynchronizeBlobCallback synchronizeBlobCallback);

    void synchronize(SynchronizeBlobRequest synchronizeBlobRequest);

    void synchronize(SynchronizeMultiFileCallback synchronizeMultiFileCallback);

    void synchronize(SynchronizeMultiFileRequest synchronizeMultiFileRequest);

    void synchronizeProgress(SynchronizeBlobProgressRequest synchronizeBlobProgressRequest);

    void synchronizeProgress(SynchronizeMultiFileProgressRequest synchronizeMultiFileProgressRequest);

    void unpackNewMultiFileGameData() throws IOException;
}
