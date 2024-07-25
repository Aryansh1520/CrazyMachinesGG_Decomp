package com.amazon.ags.jni.whispersync;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.whispersync.RevertMultiFileCallback;

/* loaded from: classes.dex */
public class RevertMultiFileJniCallback implements RevertMultiFileCallback {
    private static final String TAG = RevertMultiFileJniCallback.class.getSimpleName();
    protected long m_CallbackPointer;
    protected int m_DeveloperTag;

    public RevertMultiFileJniCallback(int developerTag, long callbackPointer) {
        this.m_DeveloperTag = developerTag;
        this.m_CallbackPointer = callbackPointer;
    }

    @Override // com.amazon.ags.api.whispersync.RevertCallback
    public void onPlayerCancelled() {
        WhisperSyncJni.getRequestRevertMultiFilePlayerCancelled(this.m_CallbackPointer, this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.RevertCallback
    public void onRevertFailure(ErrorCode errorCode) {
        WhisperSyncJni.getRequestRevertMultiFileRevertFailure(this.m_CallbackPointer, errorCode.ordinal(), this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.RevertMultiFileCallback
    public void onRevertedGameData() {
        WhisperSyncJni.getRequestRevertMultiFileRevertedGameData(this.m_CallbackPointer, this.m_DeveloperTag);
    }
}
