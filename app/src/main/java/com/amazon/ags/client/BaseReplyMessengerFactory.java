package com.amazon.ags.client;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.overlay.PopUpManager;

/* loaded from: classes.dex */
public abstract class BaseReplyMessengerFactory<T extends RequestResponse> {
    private static final String FEATURE_NAME = "GC";
    private static final String TAG = "GC_" + BaseReplyMessengerFactory.class.getSimpleName();
    protected final PopUpManager popUpManager = PopUpManager.getInstance();

    protected abstract T processMessage(Message message);

    public BaseReplyMessengerFactory(Context context) {
    }

    public final Messenger getReplyHandleMessenger(GCResponseHandleImpl<T> handle) {
        return new Messenger(new ServiceHandleHandler(handle));
    }

    /* loaded from: classes.dex */
    private class ServiceHandleHandler extends Handler {
        private GCResponseHandleImpl<T> handle;

        public ServiceHandleHandler(GCResponseHandleImpl<T> handle) {
            Log.d(BaseReplyMessengerFactory.TAG, "Constructing a ServiceHandleHandler for the outgoing asynchronous event");
            this.handle = handle;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            RequestResponse processMessage = BaseReplyMessengerFactory.this.processMessage(message);
            if (processMessage != null && this.handle != null) {
                Log.d(BaseReplyMessengerFactory.TAG, "Setting handle data with response: " + processMessage.toString());
                this.handle.setResponse(processMessage);
            }
        }
    }
}
