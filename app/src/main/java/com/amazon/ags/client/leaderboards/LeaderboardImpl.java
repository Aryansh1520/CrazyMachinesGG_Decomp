package com.amazon.ags.client.leaderboards;

import com.amazon.ags.api.leaderboards.Leaderboard;
import com.amazon.ags.constants.ScoreFormat;

/* loaded from: classes.dex */
public class LeaderboardImpl implements Leaderboard {
    private final String displayText;
    private final String id;
    private final String name;
    private final ScoreFormat scoreFormat;

    public LeaderboardImpl(String id, String name, String displayText, ScoreFormat scoreFormat) {
        this.id = id;
        this.name = name;
        this.displayText = displayText;
        this.scoreFormat = scoreFormat;
    }

    @Override // com.amazon.ags.api.leaderboards.Leaderboard
    public String getId() {
        return this.id;
    }

    @Override // com.amazon.ags.api.leaderboards.Leaderboard
    public String getName() {
        return this.name;
    }

    @Override // com.amazon.ags.api.leaderboards.Leaderboard
    public String getDisplayText() {
        return this.displayText;
    }

    @Override // com.amazon.ags.api.leaderboards.Leaderboard
    public ScoreFormat getScoreFormat() {
        return this.scoreFormat;
    }
}
