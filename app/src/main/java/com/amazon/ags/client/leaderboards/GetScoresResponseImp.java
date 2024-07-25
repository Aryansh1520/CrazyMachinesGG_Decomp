package com.amazon.ags.client.leaderboards;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.leaderboards.GetScoresResponse;
import com.amazon.ags.api.leaderboards.Score;
import com.amazon.ags.client.RequestResponseImp;
import com.amazon.ags.constants.ScoreFormat;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class GetScoresResponseImp extends RequestResponseImp implements GetScoresResponse {
    private String displayText;
    private String leaderboardId;
    private String leaderboardName;
    private int numScores;
    private ScoreFormat scoreFormat;
    private List<Score> scores;

    public GetScoresResponseImp(Score[] scores, String displayText, ScoreFormat scoreFormat, String name, String uniqueId, int responseCode) {
        super(responseCode);
        this.scores = Arrays.asList(scores);
        this.numScores = this.scores.size();
        this.displayText = displayText;
        this.scoreFormat = scoreFormat;
        this.leaderboardName = name;
        this.leaderboardId = uniqueId;
    }

    public GetScoresResponseImp(int responseCode, ErrorCode errorCode) {
        super(responseCode, errorCode);
        this.scores = null;
        this.numScores = 0;
    }

    @Override // com.amazon.ags.api.leaderboards.GetScoresResponse
    public final List<Score> getScores() {
        return this.scores;
    }

    @Override // com.amazon.ags.api.leaderboards.GetScoresResponse
    public final int getNumScores() {
        return this.numScores;
    }

    @Override // com.amazon.ags.api.leaderboards.GetScoresResponse
    public final String getDisplayText() {
        return this.displayText;
    }

    @Override // com.amazon.ags.api.leaderboards.GetScoresResponse
    public final ScoreFormat getScoreFormat() {
        return this.scoreFormat;
    }

    @Override // com.amazon.ags.api.leaderboards.GetScoresResponse
    public final String getLeaderboardName() {
        return this.leaderboardName;
    }

    @Override // com.amazon.ags.api.leaderboards.GetScoresResponse
    public final String getLeaderboardId() {
        return this.leaderboardId;
    }

    @Override // com.amazon.ags.client.RequestResponseImp
    public final int getEventType() {
        return 7;
    }

    @Override // com.amazon.ags.client.RequestResponseImp, com.amazon.ags.api.RequestResponse
    public final String toString() {
        String text = super.toString();
        return ((((text + "\n numScores: " + this.numScores) + "\n displayText: " + this.displayText) + "\n dataFormat: " + this.scoreFormat) + "\n leaderboardName: " + this.leaderboardName) + "\n leaderboardID: " + this.leaderboardId;
    }
}
