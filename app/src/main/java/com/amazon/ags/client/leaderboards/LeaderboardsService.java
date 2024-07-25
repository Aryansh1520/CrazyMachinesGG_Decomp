package com.amazon.ags.client.leaderboards;

import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.leaderboards.GetLeaderboardPercentilesResponse;
import com.amazon.ags.api.leaderboards.GetLeaderboardsResponse;
import com.amazon.ags.api.leaderboards.GetPlayerScoreResponse;
import com.amazon.ags.api.leaderboards.GetScoresResponse;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;
import com.amazon.ags.client.GCResponseHandleImpl;
import com.amazon.ags.constants.LeaderboardFilter;

/* loaded from: classes.dex */
public interface LeaderboardsService {
    void getLeaderboards(GCResponseHandleImpl<GetLeaderboardsResponse> gCResponseHandleImpl);

    void getPercentileRanks(String str, LeaderboardFilter leaderboardFilter, GCResponseHandleImpl<GetLeaderboardPercentilesResponse> gCResponseHandleImpl);

    void requestLocalPlayerScore(String str, LeaderboardFilter leaderboardFilter, GCResponseHandleImpl<GetPlayerScoreResponse> gCResponseHandleImpl);

    void requestScores(String str, LeaderboardFilter leaderboardFilter, int i, int i2, GCResponseHandleImpl<GetScoresResponse> gCResponseHandleImpl);

    void showLeaderboardsOverlay(GCResponseHandleImpl<RequestResponse> gCResponseHandleImpl);

    void showRanksOverlay(String str, GCResponseHandleImpl<RequestResponse> gCResponseHandleImpl);

    void submitScore(String str, long j, GCResponseHandleImpl<SubmitScoreResponse> gCResponseHandleImpl);
}
