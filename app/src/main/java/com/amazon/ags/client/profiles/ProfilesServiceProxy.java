package com.amazon.ags.client.profiles;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.profiles.RequestPlayerProfileResponse;
import com.amazon.ags.client.AmazonGamesService;
import com.amazon.ags.client.GCResponseHandleImpl;

/* loaded from: classes.dex */
public class ProfilesServiceProxy implements ProfilesService {
    private static final String FEATURE_NAME = "PROFILES";
    private static final String TAG = "PROFILES_" + ProfilesServiceProxy.class.getSimpleName();
    private final AmazonGamesClient agClient;
    private final AmazonGamesService amazonGamesService;
    private final Handler apiHandler;

    public ProfilesServiceProxy(AmazonGamesClient agClient, AmazonGamesService amazonGamesService, Handler apiHandler) {
        this.amazonGamesService = amazonGamesService;
        this.agClient = agClient;
        this.apiHandler = apiHandler;
    }

    @Override // com.amazon.ags.client.profiles.ProfilesService
    public void requestLocalPlayerProfile(final GCResponseHandleImpl<RequestPlayerProfileResponse> handle) {
        Log.d(TAG, "Request Local Player Profile called Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 18;
        final ProfilesReplyMessengerFactory<RequestPlayerProfileResponse> replyMessengerFactory = new ProfilesReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.profiles.ProfilesServiceProxy.1
            @Override // java.lang.Runnable
            public void run() {
                msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                msg.setData(ProfilesServiceProxy.this.createRequestBundle());
                try {
                    ProfilesServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new RequestPlayerProfileResponseImp(22, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundle() {
        return new Bundle();
    }
}
