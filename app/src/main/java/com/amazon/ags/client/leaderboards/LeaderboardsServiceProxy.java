package com.amazon.ags.client.leaderboards;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.leaderboards.GetLeaderboardPercentilesResponse;
import com.amazon.ags.api.leaderboards.GetLeaderboardsResponse;
import com.amazon.ags.api.leaderboards.GetPlayerScoreResponse;
import com.amazon.ags.api.leaderboards.GetScoresResponse;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;
import com.amazon.ags.client.AmazonGamesService;
import com.amazon.ags.client.GCResponseHandleImpl;
import com.amazon.ags.client.RequestResponseImp;
import com.amazon.ags.constants.BindingKeys;
import com.amazon.ags.constants.LeaderboardBindingKeys;
import com.amazon.ags.constants.LeaderboardFilter;
import com.amazon.ags.overlay.PopUpPrefs;

/* loaded from: classes.dex */
public class LeaderboardsServiceProxy implements LeaderboardsService {
    private static final String FEATURE_NAME = "LB";
    private static final String TAG = "LB_" + LeaderboardsServiceProxy.class.getSimpleName();
    private AmazonGamesClient agClient;
    private AmazonGamesService amazonGamesService;
    private final Handler apiHandler;

    public LeaderboardsServiceProxy(AmazonGamesClient agClient, AmazonGamesService amazonGamesService, Handler apiHandler) {
        this.amazonGamesService = amazonGamesService;
        this.agClient = agClient;
        this.apiHandler = apiHandler;
    }

    @Override // com.amazon.ags.client.leaderboards.LeaderboardsService
    public final void getLeaderboards(final GCResponseHandleImpl<GetLeaderboardsResponse> handle) {
        Log.d(TAG, "Get leaderboards called with handle. ");
        final Message msg = Message.obtain();
        msg.what = 9;
        final LeaderboardsReplyMessengerFactory<GetLeaderboardsResponse> replyMessengerFactory = new LeaderboardsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.leaderboards.LeaderboardsServiceProxy.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(new Bundle());
                    LeaderboardsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new GetLeaderboardsResponseImp(18, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.leaderboards.LeaderboardsService
    public final void submitScore(final String uniqueID, final long score, final GCResponseHandleImpl<SubmitScoreResponse> handle) {
        Log.d(TAG, "Submit Score " + score + " called Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 8;
        final LeaderboardsReplyMessengerFactory<SubmitScoreResponse> replyMessengerFactory = new LeaderboardsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.leaderboards.LeaderboardsServiceProxy.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(LeaderboardsServiceProxy.this.createRequestBundleWithScore(uniqueID, score));
                    LeaderboardsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new SubmitScoreResponseImp(18, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.leaderboards.LeaderboardsService
    public final void requestScores(final String uniqueID, final LeaderboardFilter leaderboardFilter, final int startRank, final int count, final GCResponseHandleImpl<GetScoresResponse> handle) {
        Log.d(TAG, "Request Scores called on leaderboardId " + uniqueID + " Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 7;
        final LeaderboardsReplyMessengerFactory<GetScoresResponse> replyMessengerFactory = new LeaderboardsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.leaderboards.LeaderboardsServiceProxy.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(LeaderboardsServiceProxy.this.createRequestBundleWithFilter(uniqueID, leaderboardFilter, startRank, count));
                    LeaderboardsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new GetScoresResponseImp(18, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.leaderboards.LeaderboardsService
    public final void requestLocalPlayerScore(final String uniqueID, final LeaderboardFilter leaderboardFilter, final GCResponseHandleImpl<GetPlayerScoreResponse> handle) {
        Log.d(TAG, "Request Local Player Score called on leaderboardId " + uniqueID + " Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 10;
        final LeaderboardsReplyMessengerFactory<GetPlayerScoreResponse> replyMessengerFactory = new LeaderboardsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.leaderboards.LeaderboardsServiceProxy.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(LeaderboardsServiceProxy.this.createRequestBundleWithFilter(uniqueID, leaderboardFilter, 1, 1));
                    LeaderboardsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new GetPlayerScoreResponseImp(18, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.leaderboards.LeaderboardsService
    public void showLeaderboardsOverlay(final GCResponseHandleImpl<RequestResponse> handle) {
        Log.d(TAG, "Show achievements overlay called Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 27;
        final LeaderboardsReplyMessengerFactory<RequestResponse> replyMessengerFactory = new LeaderboardsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.leaderboards.LeaderboardsServiceProxy.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(LeaderboardsServiceProxy.this.createRequestBundle());
                    LeaderboardsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new RequestResponseImp(18, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.leaderboards.LeaderboardsService
    public void showRanksOverlay(final String leaderboardId, final GCResponseHandleImpl<RequestResponse> handle) {
        Log.d(TAG, "Show ranks overlay called Asynchronously with handle and leaderboardId = " + leaderboardId);
        final Message msg = Message.obtain();
        msg.what = 28;
        final LeaderboardsReplyMessengerFactory<RequestResponse> replyMessengerFactory = new LeaderboardsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.leaderboards.LeaderboardsServiceProxy.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(LeaderboardsServiceProxy.this.createRequestBundleWithLeaderboardId(leaderboardId));
                    LeaderboardsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new RequestResponseImp(18, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.leaderboards.LeaderboardsService
    public void getPercentileRanks(final String leaderboardId, final LeaderboardFilter leaderboardFilter, final GCResponseHandleImpl<GetLeaderboardPercentilesResponse> handle) {
        Log.d(TAG, "Get percentiles called Asynchronously with handle and leaderboardId = " + leaderboardId);
        final Message msg = Message.obtain();
        msg.what = 31;
        final LeaderboardsReplyMessengerFactory<GetLeaderboardPercentilesResponse> replyMessengerFactory = new LeaderboardsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.leaderboards.LeaderboardsServiceProxy.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(LeaderboardsServiceProxy.this.createRequestBundleWithFilter(leaderboardId, leaderboardFilter));
                    LeaderboardsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new GetLeaderboardPercentilesResponseImp(18, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundle() {
        return new Bundle();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundleWithLeaderboardId(String leaderboardId) {
        Bundle bundle = createRequestBundle();
        bundle.putString(LeaderboardBindingKeys.LEADERBOARD_ID_KEY, leaderboardId);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundleWithScore(String leaderboardID, long score) {
        Bundle bundle = createRequestBundle();
        bundle.putLong(LeaderboardBindingKeys.LEADERBOARD_SUBMIT_SCORE_KEY, score);
        bundle.putString(LeaderboardBindingKeys.LEADERBOARD_ID_KEY, leaderboardID);
        bundle.putString(BindingKeys.POP_UP_LOCATION, PopUpPrefs.INSTANCE.getLocation().toString());
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundleWithFilter(String leaderboardID, LeaderboardFilter leaderboardFilter) {
        Bundle bundle = createRequestBundle();
        bundle.putString(LeaderboardBindingKeys.LEADERBOARD_ID_KEY, leaderboardID);
        bundle.putString(LeaderboardBindingKeys.LEADERBOARD_FILTER_KEY, leaderboardFilter.toString());
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundleWithFilter(String leaderboardID, LeaderboardFilter leaderboardFilter, int startRank, int count) {
        Bundle bundle = createRequestBundle();
        bundle.putString(LeaderboardBindingKeys.LEADERBOARD_ID_KEY, leaderboardID);
        bundle.putString(LeaderboardBindingKeys.LEADERBOARD_FILTER_KEY, leaderboardFilter.toString());
        bundle.putInt(LeaderboardBindingKeys.LEADERBOARD_START_RANK_KEY, startRank);
        bundle.putInt(LeaderboardBindingKeys.LEADERBOARD_SCORES_COUNT_KEY, count);
        return bundle;
    }
}
