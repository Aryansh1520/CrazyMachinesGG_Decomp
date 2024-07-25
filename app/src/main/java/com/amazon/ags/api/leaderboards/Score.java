package com.amazon.ags.api.leaderboards;

import com.amazon.ags.api.profiles.Player;

/* loaded from: classes.dex */
public interface Score {
    String getLeaderboard();

    Player getPlayer();

    int getRank();

    String getScoreString();

    long getScoreValue();
}
