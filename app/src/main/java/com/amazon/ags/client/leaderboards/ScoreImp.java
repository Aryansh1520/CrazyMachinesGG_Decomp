package com.amazon.ags.client.leaderboards;

import android.util.Log;
import com.amazon.ags.api.leaderboards.Score;
import com.amazon.ags.api.profiles.Player;

/* loaded from: classes.dex */
public class ScoreImp implements Score {
    private static final String FEATURE_NAME = "LB";
    private static final String TAG = "LB_" + ScoreImp.class.getSimpleName();
    private String leaderboard;
    private Player player;
    private int rank;
    private long scoreValue;

    public ScoreImp(long scoreValue, Player player, int rank, String leaderboard) {
        Log.d(TAG, "Creating score with score value " + scoreValue + " player " + player + " rank " + rank + " leaderboard " + leaderboard);
        this.scoreValue = scoreValue;
        this.player = player;
        this.rank = rank;
        this.leaderboard = leaderboard;
    }

    @Override // com.amazon.ags.api.leaderboards.Score
    public final Player getPlayer() {
        return this.player;
    }

    @Override // com.amazon.ags.api.leaderboards.Score
    public final long getScoreValue() {
        return this.scoreValue;
    }

    @Override // com.amazon.ags.api.leaderboards.Score
    public final String getScoreString() {
        return Long.toString(this.scoreValue);
    }

    @Override // com.amazon.ags.api.leaderboards.Score
    public final int getRank() {
        return this.rank;
    }

    @Override // com.amazon.ags.api.leaderboards.Score
    public final String getLeaderboard() {
        return this.leaderboard;
    }
}
