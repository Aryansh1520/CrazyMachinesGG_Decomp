package com.amazon.ags.api.leaderboards;

import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGames;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.constants.LeaderboardFilter;

/* loaded from: classes.dex */
public class AGLeaderboards {
    private AGLeaderboards() {
    }

    public static void getLeaderboards(AGResponseCallback<GetLeaderboardsResponse> callback, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        leaderboardsClient.getLeaderboards(userData).setCallback(callback);
    }

    public static AGResponseHandle<GetLeaderboardsResponse> getLeaderboards(Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        return leaderboardsClient.getLeaderboards(userData);
    }

    public static void submitScore(String leaderboardId, long score, AGResponseCallback<SubmitScoreResponse> callback, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        leaderboardsClient.submitScore(leaderboardId, score, userData).setCallback(callback);
    }

    public static AGResponseHandle<SubmitScoreResponse> submitScore(String leaderboardId, long score, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        return leaderboardsClient.submitScore(leaderboardId, score, userData);
    }

    public static void getScores(String leaderboardId, LeaderboardFilter filter, int startRank, int count, AGResponseCallback<GetScoresResponse> callback, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        leaderboardsClient.getScores(leaderboardId, filter, startRank, count, userData).setCallback(callback);
    }

    public static AGResponseHandle<GetScoresResponse> getScores(String leaderboardId, LeaderboardFilter filter, int startRank, int count, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        return leaderboardsClient.getScores(leaderboardId, filter, startRank, count, userData);
    }

    public static void getLocalPlayerScore(String leaderboardId, LeaderboardFilter filter, AGResponseCallback<GetPlayerScoreResponse> callback, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        leaderboardsClient.getLocalPlayerScore(leaderboardId, filter, userData).setCallback(callback);
    }

    public static AGResponseHandle<GetPlayerScoreResponse> getLocalPlayerScore(String leaderboardId, LeaderboardFilter filter, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        return leaderboardsClient.getLocalPlayerScore(leaderboardId, filter, userData);
    }

    public static AGResponseHandle<RequestResponse> showLeaderboardsOverlay() {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        return leaderboardsClient.showLeaderboardsOverlay(new Object[0]);
    }

    public static AGResponseHandle<RequestResponse> showLeaderboardOverlay(String leaderboardId) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        return leaderboardsClient.showLeaderboardOverlay(leaderboardId, new Object[0]);
    }

    public static void getPercentileRanks(String leaderboardId, LeaderboardFilter filter, AGResponseCallback<GetLeaderboardPercentilesResponse> callback, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        leaderboardsClient.getPercentileRanks(leaderboardId, filter, userData).setCallback(callback);
    }

    public static AGResponseHandle<GetLeaderboardPercentilesResponse> getPercentileRanks(String leaderboardId, LeaderboardFilter filter, Object... userData) {
        AmazonGames client = AmazonGamesClient.getInstance();
        LeaderboardsClient leaderboardsClient = client.getLeaderboardsClient();
        return leaderboardsClient.getPercentileRanks(leaderboardId, filter, userData);
    }
}
