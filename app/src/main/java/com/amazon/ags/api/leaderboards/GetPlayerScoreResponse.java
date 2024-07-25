package com.amazon.ags.api.leaderboards;

import com.amazon.ags.api.RequestResponse;

/* loaded from: classes.dex */
public interface GetPlayerScoreResponse extends RequestResponse {
    int getRank();

    long getScoreValue();
}
