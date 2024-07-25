package com.amazon.ags.jni.profiles;

import android.util.Log;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.profiles.ProfilesClient;
import com.amazon.ags.api.profiles.RequestPlayerProfileResponse;

/* loaded from: classes.dex */
public class ProfilesNativeHandler {
    private static final String TAG = ProfilesNativeHandler.class.getSimpleName();
    private static ProfilesClient m_ProfilesClient = null;

    public static void initializeNativeHandler(AmazonGamesClient amazonGamesClient) {
        m_ProfilesClient = amazonGamesClient.getProfilesClient();
    }

    public static void getLocalPlayerProfile(int developerTag, long callbackPointer) {
        if (m_ProfilesClient == null) {
            Log.e(TAG, "requestLocalPlayerProfile - initializeJni was not called beforehand.");
        } else {
            m_ProfilesClient.getLocalPlayerProfile(new Object[0]).setCallback(new RequestLocalPlayerProfileJniResponseHandler(developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<RequestPlayerProfileResponse> getLocalPlayerProfileHandle(int developerTag) {
        if (m_ProfilesClient != null) {
            return m_ProfilesClient.getLocalPlayerProfile(Integer.valueOf(developerTag));
        }
        Log.e(TAG, "requestLocalPlayerProfileHandle - initializeJni was not called beforehand.");
        return null;
    }
}
