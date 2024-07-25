package com.amazon.ags.jni.whispersync;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.whispersync.RevertBlobCallback;

/* loaded from: classes.dex */
public class RevertBlobJniCallback implements RevertBlobCallback {
    private static final String TAG = RevertBlobJniCallback.class.getSimpleName();
    protected long m_CallbackPointer;
    protected int m_DeveloperTag;

    public RevertBlobJniCallback(int developerTag, long callbackPointer) {
        this.m_DeveloperTag = developerTag;
        this.m_CallbackPointer = callbackPointer;
    }

    @Override // com.amazon.ags.api.whispersync.RevertCallback
    public void onPlayerCancelled() {
        WhisperSyncJni.getRequestRevertBlobPlayerCancelled(this.m_CallbackPointer, this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.RevertCallback
    public void onRevertFailure(ErrorCode errorCode) {
        WhisperSyncJni.getRequestRevertBlobRevertFailure(this.m_CallbackPointer, errorCode.ordinal(), this.m_DeveloperTag);
    }

    @Override // com.amazon.ags.api.whispersync.RevertBlobCallback
    public boolean onRevertedGameData(byte[] data) {
        return WhisperSyncJni.getRequestRevertBlobRevertedGameData(data, this.m_CallbackPointer, this.m_DeveloperTag);
    }
}
