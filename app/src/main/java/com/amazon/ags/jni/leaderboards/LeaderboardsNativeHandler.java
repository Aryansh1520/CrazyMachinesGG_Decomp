package com.amazon.ags.jni.leaderboards;

import android.util.Log;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.leaderboards.GetLeaderboardPercentilesResponse;
import com.amazon.ags.api.leaderboards.GetLeaderboardsResponse;
import com.amazon.ags.api.leaderboards.GetPlayerScoreResponse;
import com.amazon.ags.api.leaderboards.GetScoresResponse;
import com.amazon.ags.api.leaderboards.LeaderboardsClient;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;
import com.amazon.ags.constants.LeaderboardFilter;

/* loaded from: classes.dex */
public class LeaderboardsNativeHandler {
    private static String TAG = "LeaderboardsNativeHandler";
    private static LeaderboardsClient m_LeaderboardsClient = null;

    public static void initializeNativeHandler(AmazonGamesClient amazonGamesClient) {
        m_LeaderboardsClient = amazonGamesClient.getLeaderboardsClient();
    }

    public static void requestLeaderboards(int developerTag, long callbackPointer) {
        if (m_LeaderboardsClient == null) {
            Log.e(TAG, "requestLeaderboards - initializeJni was not called beforehand.");
        } else {
            m_LeaderboardsClient.getLeaderboards(new Object[0]).setCallback(new GetLbsJniResponseHandler(developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<GetLeaderboardsResponse> requestLeaderboardsHandle(int developerTag) {
        if (m_LeaderboardsClient != null) {
            return m_LeaderboardsClient.getLeaderboards(Integer.valueOf(developerTag));
        }
        Log.e(TAG, "requestLeaderboardsHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static void submitLeaderboardScore(String leaderboardId, long score, int developerTag, long callbackPointer) {
        if (m_LeaderboardsClient == null) {
            Log.e(TAG, "submitLeaderboardScore - initializeJni was not called beforehand.");
        } else {
            m_LeaderboardsClient.submitScore(leaderboardId, score, new Object[0]).setCallback(new SubmitScoreJniResponseHandler(developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<SubmitScoreResponse> submitLeaderboardScoreHandle(String leaderboardId, long score, int developerTag) {
        if (m_LeaderboardsClient != null) {
            return m_LeaderboardsClient.submitScore(leaderboardId, score, Integer.valueOf(developerTag));
        }
        Log.e(TAG, "submitLeaderboardScoreHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static void requestScores(String leaderboardId, int filter, int startRank, int count, int developerTag, long callbackPointer) {
        if (m_LeaderboardsClient == null) {
            Log.e(TAG, "requestScores - initializeJni was not called beforehand.");
        } else {
            m_LeaderboardsClient.getScores(leaderboardId, LeaderboardFilter.fromOrdinal(filter), startRank, count, new Object[0]).setCallback(new GetScoresJniResponseHandler(developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<GetScoresResponse> requestScoresHandle(String leaderboardId, int filter, int startRank, int count, int developerTag) {
        if (m_LeaderboardsClient != null) {
            return m_LeaderboardsClient.getScores(leaderboardId, LeaderboardFilter.fromOrdinal(filter), startRank, count, Integer.valueOf(developerTag));
        }
        Log.e(TAG, "requestScoresHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static void requestLocalPlayerScore(String leaderboardId, int filter, int developerTag, long callbackPointer) {
        if (m_LeaderboardsClient == null) {
            Log.e(TAG, "requestLocalPlayerScore - initializeJni was not called beforehand.");
        } else {
            m_LeaderboardsClient.getLocalPlayerScore(leaderboardId, LeaderboardFilter.fromOrdinal(filter), new Object[0]).setCallback(new GetScoreJniResponseHandler(leaderboardId, developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<GetPlayerScoreResponse> requestLocalPlayerScoreHandle(String leaderboardId, int filter, int developerTag) {
        if (m_LeaderboardsClient != null) {
            return m_LeaderboardsClient.getLocalPlayerScore(leaderboardId, LeaderboardFilter.fromOrdinal(filter), Integer.valueOf(developerTag));
        }
        Log.e(TAG, "requestLocalPlayerScoreHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static void getPercentiles(String leaderboardId, int filter, int developerTag, long callbackPointer) {
        if (m_LeaderboardsClient == null) {
            Log.e(TAG, "getPercentiles - initializeJni was not called beforehand.");
        } else {
            m_LeaderboardsClient.getPercentileRanks(leaderboardId, LeaderboardFilter.fromOrdinal(filter), new Object[0]).setCallback(new GetPercentilesJniResponseHandler(developerTag, callbackPointer));
        }
    }

    public static AGResponseHandle<GetLeaderboardPercentilesResponse> getPercentilesHandle(String leaderboardId, int filter, int developerTag) {
        if (m_LeaderboardsClient != null) {
            return m_LeaderboardsClient.getPercentileRanks(leaderboardId, LeaderboardFilter.fromOrdinal(filter), Integer.valueOf(developerTag));
        }
        Log.e(TAG, "getPercentilesHandle - initializeJni was not called beforehand.");
        return null;
    }

    public static AGResponseHandle<RequestResponse> showLeaderboardsOverlay() {
        if (m_LeaderboardsClient != null) {
            return m_LeaderboardsClient.showLeaderboardsOverlay(new Object[0]);
        }
        Log.e(TAG, "showLeaderboardsOverlay - initializeJni was not called beforehand.");
        return null;
    }

    public static AGResponseHandle<RequestResponse> showLeaderboardOverlay(String leaderboardId) {
        if (m_LeaderboardsClient != null) {
            return m_LeaderboardsClient.showLeaderboardOverlay(leaderboardId, new Object[0]);
        }
        Log.e(TAG, "showLeaderboardOverlay - initializeJni was not called beforehand.");
        return null;
    }
}
