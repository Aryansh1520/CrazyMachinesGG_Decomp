package com.amazon.ags.client.achievements;

import android.util.Log;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGamesStatus;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.achievements.AchievementsClient;
import com.amazon.ags.api.achievements.GetAchievementResponse;
import com.amazon.ags.api.achievements.GetAchievementsResponse;
import com.amazon.ags.api.achievements.LoadIconResponse;
import com.amazon.ags.api.achievements.UpdateProgressResponse;
import com.amazon.ags.client.AmazonGamesService;
import com.amazon.ags.client.GCResponseHandleImpl;
import com.amazon.ags.client.RequestResponseImp;
import com.amazon.ags.constants.IconSize;

/* loaded from: classes.dex */
public class AchievementsClientImpl implements AchievementsClient {
    private static final String FEATURE_NAME = "AC";
    private static final String TAG = "AC_" + AchievementsClientImpl.class.getSimpleName();
    private final AchievementsService achievementsService;
    private final AmazonGamesService amazonGamesService;

    public AchievementsClientImpl(AmazonGamesService amazonGamesService, AchievementsService achievementsService) {
        this.amazonGamesService = amazonGamesService;
        this.achievementsService = achievementsService;
    }

    @Override // com.amazon.ags.api.achievements.AchievementsClient
    public final AGResponseHandle<GetAchievementsResponse> getAchievements(Object... userData) {
        GCResponseHandleImpl<GetAchievementsResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new GetAchievementsResponseImp(20, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.achievementsService.requestAchievements(handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.achievements.AchievementsClient
    public final AGResponseHandle<GetAchievementResponse> getAchievement(String achievementId, Object... userData) {
        GCResponseHandleImpl<GetAchievementResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new GetAchievementResponseImp(20, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.achievementsService.requestAchievement(achievementId, handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.achievements.AchievementsClient
    public final AGResponseHandle<UpdateProgressResponse> updateProgress(String achievementId, float percentComplete, Object... userData) {
        GCResponseHandleImpl<UpdateProgressResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!canUpdateAchievement() && this.amazonGamesService.getStatus() != AmazonGamesStatus.SERVICE_NOT_OPTED_IN) {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new UpdateProgressResponseImp(20, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.achievementsService.updateProgress(achievementId, percentComplete, handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.achievements.AchievementsClient
    public final AGResponseHandle<RequestResponse> resetAchievements(Object... userData) {
        GCResponseHandleImpl<RequestResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new RequestResponseImp(20, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.achievementsService.resetAchievements(handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.achievements.AchievementsClient
    public AGResponseHandle<LoadIconResponse> loadIcon(String achievementId, IconSize iconSize, boolean unlocked, Object... userData) {
        GCResponseHandleImpl<LoadIconResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new LoadIconResponseImp(20, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.achievementsService.loadIcon(achievementId, iconSize, unlocked, handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.achievements.AchievementsClient
    public final AGResponseHandle<RequestResponse> resetAchievement(String achievementId, Object... userData) {
        GCResponseHandleImpl<RequestResponse> handle = new GCResponseHandleImpl<>(userData);
        if (!this.amazonGamesService.isReady()) {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new RequestResponseImp(20, ErrorCode.SERVICE_NOT_READY));
        } else {
            this.achievementsService.resetAchievement(achievementId, handle);
        }
        return handle;
    }

    @Override // com.amazon.ags.api.achievements.AchievementsClient
    public final AGResponseHandle<RequestResponse> showAchievementsOverlay(Object... userData) {
        GCResponseHandleImpl<RequestResponse> handle = new GCResponseHandleImpl<>(userData);
        if (this.amazonGamesService.isReady() || this.amazonGamesService.getStatus() == AmazonGamesStatus.SERVICE_NOT_OPTED_IN) {
            this.achievementsService.showAchievementsOverlay(handle);
        } else {
            Log.d(TAG, "Service not ready");
            handle.setResponse(new RequestResponseImp(20, ErrorCode.SERVICE_NOT_READY));
        }
        return handle;
    }

    private boolean canUpdateAchievement() {
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
