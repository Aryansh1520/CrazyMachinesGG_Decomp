package com.amazon.ags.api;

import com.amazon.ags.api.achievements.AchievementsClient;
import com.amazon.ags.api.leaderboards.LeaderboardsClient;
import com.amazon.ags.api.overlay.PopUpLocation;
import com.amazon.ags.api.profiles.ProfilesClient;
import com.amazon.ags.api.whispersync.WhisperSyncClient;

/* loaded from: classes.dex */
public interface AmazonGames {
    AchievementsClient getAchievementsClient();

    LeaderboardsClient getLeaderboardsClient();

    ProfilesClient getProfilesClient();

    AmazonGamesStatus getStatus();

    WhisperSyncClient getWhisperSyncClient();

    void initializeJni();

    boolean isReady();

    void setPopUpLocation(PopUpLocation popUpLocation);
}
