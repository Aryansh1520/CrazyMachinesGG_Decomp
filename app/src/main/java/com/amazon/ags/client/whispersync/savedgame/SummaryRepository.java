package com.amazon.ags.client.whispersync.savedgame;

import com.amazon.ags.client.whispersync.GameSummary;
import java.io.IOException;

/* loaded from: classes.dex */
public interface SummaryRepository {
    boolean hasPendingDownload();

    void removePendingDownload();

    void removePendingUpload();

    void removeSummary();

    PendingDownload retrievePendingDownload() throws IOException;

    PendingUpload retrievePendingUpload() throws IOException;

    GameSummary retrieveSummary();

    void storePendingDownload(PendingDownload pendingDownload) throws IOException;

    void storePendingUpload(PendingUpload pendingUpload) throws IOException;

    void storeSummary(GameSummary gameSummary);
}
