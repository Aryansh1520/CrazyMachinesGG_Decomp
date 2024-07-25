package com.amazon.ags.api.achievements;

import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGames;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.RequestResponse;

/* loaded from: classes.dex */
public class AGAchievements {
    private AGAchievements() {
    }

    public static void getAchievements(AGResponseCallback<GetAchievementsResponse> callback, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        AchievementsClient achievementsClient = client.getAchievementsClient();
        achievementsClient.getAchievements(userData).setCallback(callback);
    }

    public static AGResponseHandle<GetAchievementsResponse> getAchievements(Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        AchievementsClient achievementsClient = client.getAchievementsClient();
        return achievementsClient.getAchievements(userData);
    }

    public static void getAchievement(String achievementId, AGResponseCallback<GetAchievementResponse> callback, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        AchievementsClient achievementsClient = client.getAchievementsClient();
        achievementsClient.getAchievement(achievementId, userData).setCallback(callback);
    }

    public static AGResponseHandle<GetAchievementResponse> getAchievement(String achievementId, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        AchievementsClient achievementsClient = client.getAchievementsClient();
        return achievementsClient.getAchievement(achievementId, userData);
    }

    public static void updateProgress(String achievementId, float percentComplete, AGResponseCallback<UpdateProgressResponse> callback, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        AchievementsClient achievementsClient = client.getAchievementsClient();
        achievementsClient.updateProgress(achievementId, percentComplete, userData).setCallback(callback);
    }

    public static AGResponseHandle<UpdateProgressResponse> updateProgress(String achievementId, float percentComplete, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        AchievementsClient achievementsClient = client.getAchievementsClient();
        return achievementsClient.updateProgress(achievementId, percentComplete, userData);
    }

    public static AGResponseHandle<RequestResponse> showAchievementsOverlay() {
        AmazonGames client = AmazonGamesClient.getInstance();
        AchievementsClient achievementsClient = client.getAchievementsClient();
        return achievementsClient.showAchievementsOverlay(new Object[0]);
    }
}
