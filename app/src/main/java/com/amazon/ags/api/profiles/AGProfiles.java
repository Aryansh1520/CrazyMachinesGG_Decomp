package com.amazon.ags.api.profiles;

import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGames;
import com.amazon.ags.api.AmazonGamesClient;

/* loaded from: classes.dex */
public class AGProfiles {
    private AGProfiles() {
    }

    public static void getLocalPlayerProfile(AGResponseCallback<RequestPlayerProfileResponse> callback, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        ProfilesClient profilesClient = client.getProfilesClient();
        profilesClient.getLocalPlayerProfile(userData).setCallback(callback);
    }

    public static AGResponseHandle<RequestPlayerProfileResponse> getLocalPlayerProfile(Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        ProfilesClient profilesClient = client.getProfilesClient();
        return profilesClient.getLocalPlayerProfile(userData);
    }
}
