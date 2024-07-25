package com.amazon.ags.api.leaderboards;

import com.amazon.ags.constants.ScoreFormat;

/* loaded from: classes.dex */
public interface Leaderboard {
    String getDisplayText();

    String getId();

    String getName();

    ScoreFormat getScoreFormat();
}
