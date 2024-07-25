package com.amazon.ags.jni.whispersync;

/* loaded from: classes.dex */
public class WhisperSyncJni {
    public static native void getRequestRevertBlobPlayerCancelled(long j, int i);

    public static native void getRequestRevertBlobRevertFailure(long j, int i, int i2);

    public static native boolean getRequestRevertBlobRevertedGameData(byte[] bArr, long j, int i);

    public static native void getRequestRevertMultiFilePlayerCancelled(long j, int i);

    public static native void getRequestRevertMultiFileRevertFailure(long j, int i, int i2);

    public static native void getRequestRevertMultiFileRevertedGameData(long j, int i);

    public static native void getSynchronizeBlobAlreadySynchronized(long j, int i);

    public static native void getSynchronizeBlobConflictDeferral(long j, int i);

    public static native void getSynchronizeBlobGameUploadSuccess(long j, int i);

    public static native boolean getSynchronizeBlobResponseNewGameData(byte[] bArr, long j, int i);

    public static native void getSynchronizeBlobSynchronizeFailure(long j, int i, int i2);

    public static native void getSynchronizeMultiFileAlreadySynchronized(long j, int i);

    public static native void getSynchronizeMultiFileConflictDeferral(long j, int i);

    public static native void getSynchronizeMultiFileGameUploadSuccess(long j, int i);

    public static native boolean getSynchronizeMultiFileNewGameData(long j, int i);

    public static native void getSynchronizeMultiFileSynchronizeFailure(long j, int i, int i2);

    public static native void getSynchronizeMultiFileUnpackComplete(long j, int i);

    public static native void getSynchronizeMultiFileUnpackFailure(long j, int i);
}
