package com.amazon.ags.api.leaderboards;

import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.constants.ScoreFormat;
import java.util.List;

/* loaded from: classes.dex */
public interface GetScoresResponse extends RequestResponse {
    String getDisplayText();

    String getLeaderboardId();

    String getLeaderboardName();

    int getNumScores();

    ScoreFormat getScoreFormat();

    List<Score> getScores();
}
