package com.amazon.ags.jni.whispersync;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.whispersync.SynchronizeBlobCallback;

/* loaded from: classes.dex */
public class SynchronizeBlobJniCallback implements SynchronizeBlobCallback {
    private static final String TAG = SynchronizeBlobJniCallback.class.getSimpleName();
    protected long m_CallbackPointer;
    protected int m_DeveloperTag;

    public SynchronizeBlobJniCallback(int developerTag, long callbackPointer) {
        this.m_DeveloperTag = developerTag;
        this.m_CallbackPointer = callbackPointer;
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeCallback
    public void onAlreadySynchronized() {
        WhisperSyncJni.getSynchronizeBlobAlreadySynchronized(this.m_CallbackPointer, this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeCallback
    public void onConflictDeferral() {
        WhisperSyncJni.getSynchronizeBlobConflictDeferral(this.m_CallbackPointer, this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeCallback
    public void onGameUploadSuccess() {
        WhisperSyncJni.getSynchronizeBlobGameUploadSuccess(this.m_CallbackPointer, this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeCallback
    public void onSynchronizeFailure(ErrorCode errorCode) {
        WhisperSyncJni.getSynchronizeBlobSynchronizeFailure(this.m_CallbackPointer, errorCode.ordinal(), this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeBlobCallback
    public boolean onNewGameData(byte[] data) {
        return WhisperSyncJni.getSynchronizeBlobResponseNewGameData(data, this.m_CallbackPointer, this.m_DeveloperTag);
    }
}
