package com.amazon.ags.client.leaderboards;

import com.amazon.ags.api.leaderboards.LeaderboardPercentileItem;

/* loaded from: classes.dex */
public class LeaderboardPercentileItemImp implements LeaderboardPercentileItem {
    private final int percentile;
    private final String playerAlias;
    private final long playerScore;

    public LeaderboardPercentileItemImp(String playerAlias, long playerScore, int percentile) {
        this.playerAlias = playerAlias;
        this.playerScore = playerScore;
        this.percentile = percentile;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardPercentileItem
    public String getPlayerAlias() {
        return this.playerAlias;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardPercentileItem
    public long getPlayerScore() {
        return this.playerScore;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardPercentileItem
    public int getPercentile() {
        return this.percentile;
    }
}
