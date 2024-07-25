package com.amazon.ags.api.whispersync;

import com.amazon.ags.api.AmazonGames;
import com.amazon.ags.api.AmazonGamesClient;
import java.io.FilenameFilter;
import java.io.IOException;

/* loaded from: classes.dex */
public class AGWhispersync {
    public static void synchronize(SynchronizeBlobCallback callback) {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.synchronize(callback);
    }

    public static void synchronize(SynchronizeBlobRequest request) {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.synchronize(request);
    }

    public static void synchronize(SynchronizeMultiFileCallback callback) {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.synchronize(callback);
    }

    public static void synchronize(SynchronizeMultiFileRequest request) {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.synchronize(request);
    }

    public static void synchronizeProgress(SynchronizeBlobProgressRequest request) {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.synchronizeProgress(request);
    }

    public static void synchronizeProgress(SynchronizeMultiFileProgressRequest request) {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.synchronizeProgress(request);
    }

    public static void requestRevert(RevertBlobCallback callback) {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.requestRevert(callback);
    }

    public static void requestRevert(RevertMultiFileCallback callback) {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.requestRevert(callback);
    }

    public static void setFilter(FilenameFilter filter) {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.setFilter(filter);
    }

    public static boolean hasNewMultiFileGameData() {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        return whisperSyncClient.hasNewMultiFileGameData();
    }

    public static void unpackNewMultiFileGameData() throws IOException {
        AmazonGames client = AmazonGamesClient.getInstance();
        WhisperSyncClient whisperSyncClient = client.getWhisperSyncClient();
        whisperSyncClient.unpackNewMultiFileGameData();
    }
}
