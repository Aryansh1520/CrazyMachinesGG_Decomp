package com.mappn.sdk.statitistics;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
final class t extends Handler {
    /* JADX INFO: Access modifiers changed from: package-private */
    public t(Looper looper) {
        super(looper);
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        GfanPayAgent.a(message);
    }
}
