package com.amazon.ags.jni.whispersync;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.whispersync.SynchronizeMultiFileCallback;

/* loaded from: classes.dex */
public class SynchronizeMultiFileJniCallback implements SynchronizeMultiFileCallback {
    private static final String TAG = SynchronizeMultiFileJniCallback.class.getSimpleName();
    protected long m_CallbackPointer;
    protected int m_DeveloperTag;

    public SynchronizeMultiFileJniCallback(int developerTag, long callbackPointer) {
        this.m_DeveloperTag = developerTag;
        this.m_CallbackPointer = callbackPointer;
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeCallback
    public void onAlreadySynchronized() {
        WhisperSyncJni.getSynchronizeMultiFileAlreadySynchronized(this.m_CallbackPointer, this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeCallback
    public void onConflictDeferral() {
        WhisperSyncJni.getSynchronizeMultiFileConflictDeferral(this.m_CallbackPointer, this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeCallback
    public void onGameUploadSuccess() {
        WhisperSyncJni.getSynchronizeMultiFileGameUploadSuccess(this.m_CallbackPointer, this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeCallback
    public void onSynchronizeFailure(ErrorCode errorCode) {
        WhisperSyncJni.getSynchronizeMultiFileSynchronizeFailure(this.m_CallbackPointer, errorCode.ordinal(), this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.SynchronizeMultiFileCallback
    public void onNewGameData() {
        WhisperSyncJni.getSynchronizeMultiFileNewGameData(this.m_CallbackPointer, this.m_DeveloperTag);
    }
}
