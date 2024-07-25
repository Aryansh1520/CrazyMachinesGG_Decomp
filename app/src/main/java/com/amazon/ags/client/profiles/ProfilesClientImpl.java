package com.amazon.ags.client.profiles;

import android.util.Log;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.profiles.ProfilesClient;
import com.amazon.ags.api.profiles.RequestPlayerProfileResponse;
import com.amazon.ags.client.AmazonGamesService;
import com.amazon.ags.client.GCResponseHandleImpl;

/* loaded from: classes.dex */
public class ProfilesClientImpl implements ProfilesClient {
    private static final String FEATURE_NAME = "PROFILES";
    private static final String TAG = "PROFILES_" + ProfilesClientImpl.class.getSimpleName();
    private AmazonGamesService amazonGamesService;
    private ProfilesService profilesService;

    public ProfilesClientImpl(AmazonGamesService amazonGamesService, ProfilesService profilesService) {
        this.amazonGamesService = amazonGamesService;
        this.profilesService = profilesService;
    }

    @Override // com.amazon.ags.api.profiles.ProfilesClient
    public final AGResponseHandle<RequestPlayerProfileResponse> getLocalPlayerProfile(Object... userData) {
        GCResponseHandleImpl<RequestPlayerProfileResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not bound");
            handle.setResponse(new RequestPlayerProfileResponseImp(22, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.profilesService.requestLocalPlayerProfile(handle);
        }
        return handle;
    }
}
