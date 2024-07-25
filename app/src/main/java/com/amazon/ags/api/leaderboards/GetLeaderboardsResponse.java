package com.amazon.ags.api.leaderboards;

import com.amazon.ags.api.RequestResponse;
import java.util.List;

/* loaded from: classes.dex */
public interface GetLeaderboardsResponse extends RequestResponse {
    List<Leaderboard> getLeaderboards();

    int getNumLeaderboards();
}
