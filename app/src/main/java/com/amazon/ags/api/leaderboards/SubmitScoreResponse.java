package com.amazon.ags.api.leaderboards;

import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.constants.LeaderboardFilter;
import java.util.Map;

/* loaded from: classes.dex */
public interface SubmitScoreResponse extends RequestResponse {
    Map<LeaderboardFilter, Integer> getNewRank();

    Map<LeaderboardFilter, Boolean> getRankImproved();
}
