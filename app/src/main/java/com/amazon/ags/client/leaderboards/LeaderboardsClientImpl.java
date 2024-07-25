package com.amazon.ags.client.leaderboards;

import android.text.TextUtils;
import android.util.Log;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGamesStatus;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.leaderboards.GetLeaderboardPercentilesResponse;
import com.amazon.ags.api.leaderboards.GetLeaderboardsResponse;
import com.amazon.ags.api.leaderboards.GetPlayerScoreResponse;
import com.amazon.ags.api.leaderboards.GetScoresResponse;
import com.amazon.ags.api.leaderboards.LeaderboardsClient;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;
import com.amazon.ags.client.AmazonGamesService;
import com.amazon.ags.client.GCResponseHandleImpl;
import com.amazon.ags.client.RequestResponseImp;
import com.amazon.ags.constants.LeaderboardFilter;

/* loaded from: classes.dex */
public class LeaderboardsClientImpl implements LeaderboardsClient {
    private static final String FEATURE_NAME = "LB";
    private static final String TAG = "LB_" + LeaderboardsClientImpl.class.getSimpleName();
    private final AmazonGamesService amazonGamesService;
    private final LeaderboardsService leaderboardsService;

    public LeaderboardsClientImpl(AmazonGamesService amazonGamesService, LeaderboardsService leaderboardsService) {
        this.amazonGamesService = amazonGamesService;
        this.leaderboardsService = leaderboardsService;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardsClient
    public final AGResponseHandle<GetLeaderboardsResponse> getLeaderboards(Object... userData) {
        GCResponseHandleImpl<GetLeaderboardsResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not bound");
            handle.setResponse(new GetLeaderboardsResponseImp(18, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.leaderboardsService.getLeaderboards(handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardsClient
    public final AGResponseHandle<SubmitScoreResponse> submitScore(String leaderboardId, long score, Object... userData) {
        GCResponseHandleImpl<SubmitScoreResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!canSubmitScore()) {
            Log.d(TAG, "Service not authorized");
            handle.setResponse(new SubmitScoreResponseImp(18, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.leaderboardsService.submitScore(leaderboardId, score, handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardsClient
    public final AGResponseHandle<GetScoresResponse> getScores(String leaderboardId, LeaderboardFilter filter, int startRank, int count, Object... userData) {
        GCResponseHandleImpl<GetScoresResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new GetScoresResponseImp(18, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.leaderboardsService.requestScores(leaderboardId, filter, startRank, count, handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardsClient
    public final AGResponseHandle<GetPlayerScoreResponse> getLocalPlayerScore(String leaderboardId, LeaderboardFilter filter, Object... userData) {
        GCResponseHandleImpl<GetPlayerScoreResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new GetPlayerScoreResponseImp(18, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.leaderboardsService.requestLocalPlayerScore(leaderboardId, filter, handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardsClient
    public final AGResponseHandle<GetLeaderboardPercentilesResponse> getPercentileRanks(String leaderboardId, LeaderboardFilter filter, Object... userData) {
        GCResponseHandleImpl<GetLeaderboardPercentilesResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new GetLeaderboardPercentilesResponseImp(18, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.leaderboardsService.getPercentileRanks(leaderboardId, filter, handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardsClient
    public final AGResponseHandle<RequestResponse> showLeaderboardsOverlay(Object... userData) {
        GCResponseHandleImpl<RequestResponse> handle = new GCResponseHandleImpl<>(userData);
        if (this.amazonGamesService.isReady() || this.amazonGamesService.getStatus() == AmazonGamesStatus.SERVICE_NOT_OPTED_IN) {
            this.leaderboardsService.showLeaderboardsOverlay(handle);
        } else {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new RequestResponseImp(18, ErrorCode.SERVICE_NOT_READY));
        }
        return handle;
    }

    @Override // com.amazon.ags.api.leaderboards.LeaderboardsClient
    public final AGResponseHandle<RequestResponse> showLeaderboardOverlay(String leaderboardId, Object... userData) {
        GCResponseHandleImpl<RequestResponse> handle = new GCResponseHandleImpl<>(userData);
        if (TextUtils.isEmpty(leaderboardId)) {
            Log.d(TAG, "Provided leaderboard ID is empty.");
            handle.setResponse(new RequestResponseImp(18, ErrorCode.DATA_VALIDATION_ERROR));
        } else if (this.amazonGamesService.isReady() || this.amazonGamesService.getStatus() == AmazonGamesStatus.SERVICE_NOT_OPTED_IN) {
            this.leaderboardsService.showRanksOverlay(leaderboardId, handle);
        } else {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new RequestResponseImp(18, ErrorCode.SERVICE_NOT_READY));
        }
        return handle;
    }

    private boolean canSubmitScore() {
        AmazonGamesStatus status = this.amazonGamesService.getStatus();
        switch (status) {
            case INITIALIZING:
            case SERVICE_DISCONNECTED:
            case CANNOT_BIND:
            case SERVICE_NOT_OPTED_IN:
                return false;
            default:
                return true;
        }
    }
}
