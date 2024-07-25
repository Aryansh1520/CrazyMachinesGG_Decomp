package com.amazon.ags.api.whispersync;

import com.amazon.ags.api.ErrorCode;

/* loaded from: classes.dex */
public interface RevertCallback {
    void onPlayerCancelled();

    void onRevertFailure(ErrorCode errorCode);
}
