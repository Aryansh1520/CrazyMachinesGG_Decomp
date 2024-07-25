package com.amazon.ags.jni.leaderboards;

import com.amazon.ags.api.leaderboards.GetLeaderboardPercentilesResponse;
import com.amazon.ags.api.leaderboards.GetLeaderboardsResponse;
import com.amazon.ags.api.leaderboards.GetPlayerScoreResponse;
import com.amazon.ags.api.leaderboards.GetScoresResponse;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;

/* loaded from: classes.dex */
public class LeaderboardsJni {
    public static native void getLeaderboardsResponseFailure(long j, int i, int i2);

    public static native void getLeaderboardsResponseSuccess(GetLeaderboardsResponse getLeaderboardsResponse, long j, int i);

    public static native void getPercentilesResponseFailure(long j, int i, int i2);

    public static native void getPercentilesResponseSuccess(GetLeaderboardPercentilesResponse getLeaderboardPercentilesResponse, long j, int i);

    public static native void getPlayerScoreResponseFailure(long j, int i, int i2);

    public static native void getPlayerScoreResponseSuccess(GetPlayerScoreResponse getPlayerScoreResponse, String str, long j, int i);

    public static native void getScoresResponseFailure(long j, int i, int i2);

    public static native void getScoresResponseSuccess(GetScoresResponse getScoresResponse, long j, int i);

    public static native void submitScoreResponseFailure(long j, int i, int i2);

    public static native void submitScoreResponseSuccess(SubmitScoreResponse submitScoreResponse, long j, int i);
}
