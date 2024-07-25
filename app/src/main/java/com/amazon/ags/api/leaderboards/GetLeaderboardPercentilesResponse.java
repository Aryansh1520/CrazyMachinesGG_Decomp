package com.amazon.ags.api.leaderboards;

import com.amazon.ags.api.RequestResponse;
import java.util.List;

/* loaded from: classes.dex */
public interface GetLeaderboardPercentilesResponse extends RequestResponse {
    Leaderboard getLeaderboard();

    List<LeaderboardPercentileItem> getPercentileList();

    int getUserIndex();
}
