package com.amazon.ags.api.whispersync;

import com.amazon.ags.api.ErrorCode;

/* loaded from: classes.dex */
public interface SynchronizeCallback {
    void onAlreadySynchronized();

    void onConflictDeferral();

    void onGameUploadSuccess();

    void onSynchronizeFailure(ErrorCode errorCode);
}
