package com.amazon.ags.jni.achievements;

import android.util.Log;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.achievements.AchievementsClient;
import com.amazon.ags.api.achievements.GetAchievementResponse;
import com.amazon.ags.api.achievements.GetAchievementsResponse;
import com.amazon.ags.api.achievements.UpdateProgressResponse;

/* loaded from: classes.dex */
public class AchievementsNativeHandler {
    private static String TAG = "AchievementsNativeHandler";
    private static AchievementsClient m_AchievementsClient = null;

    public static void initializeNativeHandler(AmazonGamesClient amazonGamesClient) {
        m_AchievementsClient = amazonGamesClient.getAchievementsClient();
    }

    public static void requestAchievements(int developerTag, long callbackPointer) {
        if (m_AchievementsClient == null) {
            Log.e(TAG, "requestAchievements - initializeJni was not called beforehand.");
        } else {
            m_AchievementsClient.getAchievements(new Object[0]).setCallback(new GetAchievementsJniRespHandler(developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<GetAchievementsResponse> requestAchievementsHandle(int developerTag) {
        if (m_AchievementsClient != null) {
            return m_AchievementsClient.getAchievements(Integer.valueOf(developerTag));
        }
        Log.e(TAG, "requestAchievementsHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static void requestAchievement(String achievementId, int developerTag, long callbackPointer) {
        if (m_AchievementsClient == null) {
            Log.e(TAG, "requestAchievement - initializeJni was not called beforehand.");
        } else {
            m_AchievementsClient.getAchievement(achievementId, new Object[0]).setCallback(new GetAchievementJniRespHandler(developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<GetAchievementResponse> requestAchievementHandle(String achievementId, int developerTag) {
        if (m_AchievementsClient != null) {
            return m_AchievementsClient.getAchievement(achievementId, Integer.valueOf(developerTag));
        }
        Log.e(TAG, "requestAchievementHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static void updateProgress(String achievementId, float percentComplete, int developerTag, long callbackPointer) {
        if (m_AchievementsClient == null) {
            Log.e(TAG, "updateProgress - initializeJni was not called beforehand.");
        } else {
            m_AchievementsClient.updateProgress(achievementId, percentComplete, new Object[0]).setCallback(new UpdateProgressJniRespHandler(achievementId, developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<UpdateProgressResponse> updateProgressHandle(String achievementId, float percentComplete, int developerTag) {
        if (m_AchievementsClient != null) {
            return m_AchievementsClient.updateProgress(achievementId, percentComplete, Integer.valueOf(developerTag));
        }
        Log.e(TAG, "updateProgressHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static void resetAchievements(int developerTag, long callbackPointer) {
        if (m_AchievementsClient == null) {
            Log.e(TAG, "resetAchievements - initializeJni was not called beforehand.");
        } else {
            m_AchievementsClient.resetAchievements(new Object[0]).setCallback(new ResetAchievementJniRespHandler(developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<RequestResponse> resetAchievementsHandle(int developerTag) {
        if (m_AchievementsClient != null) {
            return m_AchievementsClient.resetAchievements(Integer.valueOf(developerTag));
        }
        Log.e(TAG, "resetAchievementsHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static void resetAchievement(String achievementId, int developerTag, long callbackPointer) {
        if (m_AchievementsClient == null) {
            Log.e(TAG, "resetAchievement - initializeJni was not called beforehand.");
        } else {
            m_AchievementsClient.resetAchievement(achievementId, new Object[0]).setCallback(new ResetAchievementJniRespHandler(developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<RequestResponse> resetAchievementHandle(String achievementId, int developerTag) {
        if (m_AchievementsClient != null) {
            return m_AchievementsClient.resetAchievement(achievementId, Integer.valueOf(developerTag));
        }
        Log.e(TAG, "resetAchievementHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static AGResponseHandle<RequestResponse> showAchievementsOverlay() {
        if (m_AchievementsClient != null) {
            return m_AchievementsClient.showAchievementsOverlay(new Object[0]);
        }
        Log.e(TAG, "showAchievementsOverlay - initializeJni was not called beforehand.");
        return null;
    }
}
