package com.amazon.ags.client.leaderboards;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.leaderboards.GetLeaderboardsResponse;
import com.amazon.ags.api.leaderboards.Leaderboard;
import com.amazon.ags.client.RequestResponseImp;
import java.util.List;

/* loaded from: classes.dex */
public class GetLeaderboardsResponseImp extends RequestResponseImp implements GetLeaderboardsResponse {
    private final List<Leaderboard> leaderboards;
    private final int numLeaderboards;

    public GetLeaderboardsResponseImp(List<Leaderboard> leaderboards, int responseCode) {
        super(responseCode);
        this.leaderboards = leaderboards;
        this.numLeaderboards = leaderboards.size();
    }

    public GetLeaderboardsResponseImp(int responseCode, ErrorCode error) {
        super(responseCode, error);
        this.numLeaderboards = 0;
        this.leaderboards = null;
    }

    @Override // com.amazon.ags.api.leaderboards.GetLeaderboardsResponse
    public final int getNumLeaderboards() {
        return this.numLeaderboards;
    }

    @Override // com.amazon.ags.api.leaderboards.GetLeaderboardsResponse
    public final List<Leaderboard> getLeaderboards() {
        return this.leaderboards;
    }

    @Override // com.amazon.ags.client.RequestResponseImp
    public final int getEventType() {
        return 9;
    }

    @Override // com.amazon.ags.client.RequestResponseImp, com.amazon.ags.api.RequestResponse
    public final String toString() {
        String text = super.toString();
        return text + "\n numLeaderboards: " + this.numLeaderboards;
    }
}
