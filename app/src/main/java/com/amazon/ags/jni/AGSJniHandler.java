package com.amazon.ags.jni;

import android.util.Log;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.jni.achievements.AchievementsNativeHandler;
import com.amazon.ags.jni.leaderboards.LeaderboardsNativeHandler;
import com.amazon.ags.jni.profiles.ProfilesNativeHandler;
import com.amazon.ags.jni.whispersync.WhisperSyncNativeHandler;

/* loaded from: classes.dex */
public class AGSJniHandler {
    private static final String JNI_LIBRARY_NAME = "AmazonGamesJni";
    public static final String TAG = "AGSJniHandler";

    public static native void isLoaded();

    public static void initializeJni(AmazonGamesClient amazonGamesClient) {
        Log.i(TAG, "Loading Jni library");
        LeaderboardsNativeHandler.initializeNativeHandler(amazonGamesClient);
        AchievementsNativeHandler.initializeNativeHandler(amazonGamesClient);
        WhisperSyncNativeHandler.initializeNativeHandler(amazonGamesClient);
        ProfilesNativeHandler.initializeNativeHandler(amazonGamesClient);
        try {
            isLoaded();
        } catch (UnsatisfiedLinkError e) {
            Log.i(TAG, "AmazonGamesJni is not loaded, trying to load library");
            try {
                System.loadLibrary(JNI_LIBRARY_NAME);
            } catch (UnsatisfiedLinkError ule2) {
                throw new RuntimeException("Could not load AmazonGamesJni: " + ule2.getMessage());
            }
        }
    }
}
