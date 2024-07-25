package com.amazon.ags.jni.whispersync;

import android.util.Log;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.whispersync.RevertBlobCallback;
import com.amazon.ags.api.whispersync.RevertMultiFileCallback;
import com.amazon.ags.api.whispersync.SynchronizeBlobCallback;
import com.amazon.ags.api.whispersync.SynchronizeBlobProgressRequest;
import com.amazon.ags.api.whispersync.SynchronizeBlobRequest;
import com.amazon.ags.api.whispersync.SynchronizeMultiFileCallback;
import com.amazon.ags.api.whispersync.SynchronizeMultiFileProgressRequest;
import com.amazon.ags.api.whispersync.SynchronizeMultiFileRequest;
import com.amazon.ags.api.whispersync.WhisperSyncClient;
import com.amazon.ags.constants.whispersync.ConflictStrategy;
import java.io.IOException;

/* loaded from: classes.dex */
public class WhisperSyncNativeHandler {
    private static final String TAG = WhisperSyncNativeHandler.class.getSimpleName();
    private static WhisperSyncClient m_WhisperSyncClient = null;

    public static void initializeNativeHandler(AmazonGamesClient amazonGamesClient) {
        m_WhisperSyncClient = amazonGamesClient.getWhisperSyncClient();
    }

    private static ConflictStrategy conflictStrategyEnumToConflictStrategy(int conflictStrategyEnum) {
        switch (conflictStrategyEnum) {
            case 0:
                ConflictStrategy result = ConflictStrategy.PLAYER_SELECT;
                return result;
            case 1:
                ConflictStrategy result2 = ConflictStrategy.AUTO_RESOLVE_TO_CLOUD;
                return result2;
            case 2:
                ConflictStrategy result3 = ConflictStrategy.AUTO_RESOLVE_TO_IGNORE;
                return result3;
            default:
                throw new IllegalArgumentException("Invalid enumeration value");
        }
    }

    public static void synchronizeBlob(int developerTag, long callbackPointer) {
        SynchronizeBlobCallback callback = new SynchronizeBlobJniCallback(developerTag, callbackPointer);
        m_WhisperSyncClient.synchronize(callback);
    }

    public static void synchronizeBlobRequest(int developerTag, long callbackPointer, int conflictStrategyEnum) {
        SynchronizeBlobCallback callback = new SynchronizeBlobJniCallback(developerTag, callbackPointer);
        SynchronizeBlobRequest request = new SynchronizeBlobRequest(callback);
        ConflictStrategy conflictStrategy = conflictStrategyEnumToConflictStrategy(conflictStrategyEnum);
        request.setConflictStrategy(conflictStrategy);
        m_WhisperSyncClient.synchronize(request);
    }

    public static void synchronizeMultiFile(int developerTag, long callbackPointer) {
        SynchronizeMultiFileCallback callback = new SynchronizeMultiFileJniCallback(developerTag, callbackPointer);
        m_WhisperSyncClient.synchronize(callback);
    }

    public static void synchronizeMultiFileRequest(int developerTag, long callbackPointer, int conflictStrategyEnum) {
        SynchronizeMultiFileCallback callback = new SynchronizeMultiFileJniCallback(developerTag, callbackPointer);
        SynchronizeMultiFileRequest request = new SynchronizeMultiFileRequest(callback);
        ConflictStrategy conflictStrategy = conflictStrategyEnumToConflictStrategy(conflictStrategyEnum);
        request.setConflictStrategy(conflictStrategy);
        m_WhisperSyncClient.synchronize(request);
    }

    public static void synchronizeBlobProgressRequest(int developerTag, long callbackPointer, int conflictStrategyEnum, String description, byte[] data) {
        SynchronizeBlobCallback callback = new SynchronizeBlobJniCallback(developerTag, callbackPointer);
        SynchronizeBlobProgressRequest request = new SynchronizeBlobProgressRequest(callback);
        ConflictStrategy conflictStrategy = conflictStrategyEnumToConflictStrategy(conflictStrategyEnum);
        request.setConflictStrategy(conflictStrategy);
        request.setDescription(description);
        request.setData(data);
        m_WhisperSyncClient.synchronizeProgress(request);
    }

    public static void synchronizeMultiFileProgressRequest(int developerTag, long callbackPointer, int conflictStrategyEnum, String description, String filter) {
        SynchronizeMultiFileCallback callback = new SynchronizeMultiFileJniCallback(developerTag, callbackPointer);
        SynchronizeMultiFileProgressRequest request = new SynchronizeMultiFileProgressRequest(callback);
        ConflictStrategy conflictStrategy = conflictStrategyEnumToConflictStrategy(conflictStrategyEnum);
        request.setConflictStrategy(conflictStrategy);
        request.setDescription(description);
        request.setFilter(new SimpleExclusionFilter(filter));
        m_WhisperSyncClient.synchronizeProgress(request);
    }

    public static void requestRevertBlob(int developerTag, long callbackPointer) {
        RevertBlobCallback callback = new RevertBlobJniCallback(developerTag, callbackPointer);
        m_WhisperSyncClient.requestRevert(callback);
    }

    public static void requestRevertMultiFile(int developerTag, long callbackPointer) {
        RevertMultiFileCallback callback = new RevertMultiFileJniCallback(developerTag, callbackPointer);
        m_WhisperSyncClient.requestRevert(callback);
    }

    public static void setFilter(String filter, int developerTag) {
        m_WhisperSyncClient.setFilter(new SimpleExclusionFilter(filter));
    }

    public static boolean hasNewMultiFileGameData() {
        Log.i(TAG, "IN JAVA HASNEWMULTIFILEGAMEDATA");
        return m_WhisperSyncClient.hasNewMultiFileGameData();
    }

    public static void unpackNewMultiFileGameData() throws IOException {
        m_WhisperSyncClient.unpackNewMultiFileGameData();
    }
}
