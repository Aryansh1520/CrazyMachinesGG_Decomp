package com.amazon.ags.client.leaderboards;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.leaderboards.GetPlayerScoreResponse;
import com.amazon.ags.client.RequestResponseImp;

/* loaded from: classes.dex */
public class GetPlayerScoreResponseImp extends RequestResponseImp implements GetPlayerScoreResponse {
    private final int rank;
    private final long scoreValue;

    public GetPlayerScoreResponseImp(long scoreValue, int rank, int responseCode) {
        super(responseCode);
        this.scoreValue = scoreValue;
        this.rank = rank;
    }

    public GetPlayerScoreResponseImp(int responseCode, ErrorCode error) {
        super(responseCode, error);
        this.scoreValue = 0L;
        this.rank = 0;
    }

    @Override // com.amazon.ags.api.leaderboards.GetPlayerScoreResponse
    public final long getScoreValue() {
        return this.scoreValue;
    }

    @Override // com.amazon.ags.api.leaderboards.GetPlayerScoreResponse
    public final int getRank() {
        return this.rank;
    }

    @Override // com.amazon.ags.client.RequestResponseImp
    public final int getEventType() {
        return 10;
    }

    @Override // com.amazon.ags.client.RequestResponseImp, com.amazon.ags.api.RequestResponse
    public final String toString() {
        String text = super.toString();
        return (text + "\n score: " + this.scoreValue) + "\n rank: " + this.rank;
    }
}
