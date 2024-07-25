package com.amazon.inapp.purchasing;

import android.os.RemoteException;
import com.amazon.android.framework.exception.KiwiException;
import com.amazon.venezia.command.SuccessResult;

/* loaded from: classes.dex */
final class KiwiResponseReceivedCommandTask extends KiwiBaseCommandTask {
    private static final String COMMAND_NAME = "response_received";
    private static final String COMMAND_VERSION = "1.0";
    private static final String TAG = "KiwiResponseReceivedCommandTask";

    /* JADX INFO: Access modifiers changed from: package-private */
    public KiwiResponseReceivedCommandTask(String str) {
        super(COMMAND_NAME, COMMAND_VERSION, str);
    }

    protected void onSuccess(SuccessResult successResult) throws RemoteException, KiwiException {
        if (Logger.isTraceOn()) {
            Logger.trace(TAG, "onSuccess");
        }
    }

    @Override // com.amazon.inapp.purchasing.KiwiBaseCommandTask
    protected void sendFailedResponse() {
    }
}
