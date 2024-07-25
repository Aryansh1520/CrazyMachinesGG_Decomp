package com.amazon.ags.client.leaderboards;

import android.util.Log;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;
import com.amazon.ags.client.RequestResponseImp;
import com.amazon.ags.constants.LeaderboardFilter;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class SubmitScoreResponseImp extends RequestResponseImp implements SubmitScoreResponse {
    private static final String FEATURE_NAME = "LB";
    private static final String TAG = "LB_" + SubmitScoreResponseImp.class.getSimpleName();
    private Map<LeaderboardFilter, Boolean> improvedInFilter;
    private Map<LeaderboardFilter, Integer> rankInFilter;

    public SubmitScoreResponseImp(Map<LeaderboardFilter, Boolean> improvedInFilter, Map<LeaderboardFilter, Integer> rankInFilter, int responseCode) {
        super(responseCode);
        this.improvedInFilter = improvedInFilter;
        this.rankInFilter = rankInFilter;
        if (improvedInFilter == null || rankInFilter == null) {
            Log.d(TAG, "Constructing SubmitScoreResponse with null improvements");
            this.improvedInFilter = new HashMap();
            this.rankInFilter = new HashMap();
        }
    }

    public SubmitScoreResponseImp(int responseCode, ErrorCode error) {
        super(responseCode, error);
    }

    @Override // com.amazon.ags.client.RequestResponseImp
    public final int getEventType() {
        return 8;
    }

    @Override // com.amazon.ags.api.leaderboards.SubmitScoreResponse
    public final Map<LeaderboardFilter, Boolean> getRankImproved() {
        return this.improvedInFilter;
    }

    @Override // com.amazon.ags.api.leaderboards.SubmitScoreResponse
    public final Map<LeaderboardFilter, Integer> getNewRank() {
        return this.rankInFilter;
    }

    @Override // com.amazon.ags.client.RequestResponseImp, com.amazon.ags.api.RequestResponse
    public final String toString() {
        String text = super.toString();
        return (text + "\n Improvements: " + this.improvedInFilter) + "\n Ranks: " + this.rankInFilter;
    }
}
