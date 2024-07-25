package com.amazon.ags.client.achievements;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.achievements.GetAchievementResponse;
import com.amazon.ags.api.achievements.GetAchievementsResponse;
import com.amazon.ags.api.achievements.LoadIconResponse;
import com.amazon.ags.api.achievements.UpdateProgressResponse;
import com.amazon.ags.client.AmazonGamesService;
import com.amazon.ags.client.GCResponseHandleImpl;
import com.amazon.ags.client.RequestResponseImp;
import com.amazon.ags.constants.AchievementsBindingKeys;
import com.amazon.ags.constants.BindingKeys;
import com.amazon.ags.constants.IconSize;
import com.amazon.ags.overlay.PopUpPrefs;

/* loaded from: classes.dex */
public class AchievementsServiceProxy implements AchievementsService {
    private static final String FEATURE_NAME = "AC";
    private static final String TAG = "AC_" + AchievementsServiceProxy.class.getSimpleName();
    private final AmazonGamesClient agClient;
    private AmazonGamesService amazonGamesService;
    private final Handler apiHandler;

    public AchievementsServiceProxy(AmazonGamesClient agClient, AmazonGamesService amazonGamesService, Handler apiHandler) {
        this.amazonGamesService = amazonGamesService;
        this.agClient = agClient;
        this.apiHandler = apiHandler;
    }

    @Override // com.amazon.ags.client.achievements.AchievementsService
    public void loadIcon(final String achievementId, final IconSize iconSize, final boolean unlocked, final GCResponseHandleImpl<LoadIconResponse> handle) {
        Log.d(TAG, "Requested icon for " + achievementId + " Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 16;
        final AchievementsReplyMessengerFactory<LoadIconResponse> replyMessengerFactory = new AchievementsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.achievements.AchievementsServiceProxy.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(AchievementsServiceProxy.this.createRequestBundleWithIcon(achievementId, iconSize.name(), unlocked));
                    AchievementsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new LoadIconResponseImp(20, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.achievements.AchievementsService
    public void requestAchievements(final GCResponseHandleImpl<GetAchievementsResponse> handle) {
        Log.d(TAG, "Requested Achievements Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 17;
        final AchievementsReplyMessengerFactory<GetAchievementsResponse> replyMessengerFactory = new AchievementsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.achievements.AchievementsServiceProxy.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(AchievementsServiceProxy.this.createRequestBundle());
                    AchievementsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new GetAchievementsResponseImp(20, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.achievements.AchievementsService
    public void requestAchievement(final String achievementId, final GCResponseHandleImpl<GetAchievementResponse> handle) {
        Log.d(TAG, "Requested Achievement " + achievementId + " Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 12;
        final AchievementsReplyMessengerFactory<GetAchievementResponse> replyMessengerFactory = new AchievementsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.achievements.AchievementsServiceProxy.3
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(AchievementsServiceProxy.this.createRequestBundleWithAchievement(achievementId));
                    AchievementsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new GetAchievementResponseImp(20, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.achievements.AchievementsService
    public void updateProgress(final String achievementId, final float percentComplete, final GCResponseHandleImpl<UpdateProgressResponse> handle) {
        Log.d(TAG, "Update Progress called on " + achievementId + " Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 13;
        final AchievementsReplyMessengerFactory<UpdateProgressResponse> replyMessengerFactory = new AchievementsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.achievements.AchievementsServiceProxy.4
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(AchievementsServiceProxy.this.createRequestBundleWithAchievementProgress(achievementId, percentComplete));
                    AchievementsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new UpdateProgressResponseImp(20, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.achievements.AchievementsService
    public void resetAchievements(final GCResponseHandleImpl<RequestResponse> handle) {
        Log.d(TAG, "Reset achievements called Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 14;
        final AchievementsReplyMessengerFactory<RequestResponse> replyMessengerFactory = new AchievementsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.achievements.AchievementsServiceProxy.5
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(AchievementsServiceProxy.this.createRequestBundle());
                    AchievementsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new RequestResponseImp(20, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.achievements.AchievementsService
    public void showAchievementsOverlay(final GCResponseHandleImpl<RequestResponse> handle) {
        Log.d(TAG, "Show achievements overlay called Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 26;
        final AchievementsReplyMessengerFactory<RequestResponse> replyMessengerFactory = new AchievementsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.achievements.AchievementsServiceProxy.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(AchievementsServiceProxy.this.createRequestBundle());
                    AchievementsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new RequestResponseImp(20, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    @Override // com.amazon.ags.client.achievements.AchievementsService
    public void resetAchievement(final String achievementId, final GCResponseHandleImpl<RequestResponse> handle) {
        Log.d(TAG, "Reset Achievement called on " + achievementId + " Asynchronously with handle.");
        final Message msg = Message.obtain();
        msg.what = 15;
        final AchievementsReplyMessengerFactory<RequestResponse> replyMessengerFactory = new AchievementsReplyMessengerFactory<>(this.agClient.getAppContext());
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.client.achievements.AchievementsServiceProxy.7
            @Override // java.lang.Runnable
            public void run() {
                try {
                    msg.replyTo = replyMessengerFactory.getReplyHandleMessenger(handle);
                    msg.setData(AchievementsServiceProxy.this.createRequestBundleWithAchievement(achievementId));
                    AchievementsServiceProxy.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    handle.setResponse(new RequestResponseImp(20, ErrorCode.UNRECOVERABLE));
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundleWithIcon(String id, String size, boolean unlocked) {
        Bundle bundle = createRequestBundleWithAchievement(id);
        bundle.putString(AchievementsBindingKeys.ACHIEVEMENT_ICON_SIZE_KEY, size);
        bundle.putBoolean(AchievementsBindingKeys.ACHIEVEMENT_UNLOCKED_KEY, unlocked);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundleWithAchievementProgress(String id, float percentComplete) {
        Bundle bundle = createRequestBundleWithAchievement(id);
        bundle.putFloat(AchievementsBindingKeys.ACHIEVEMENT_UPDATE_PERCENT_KEY, percentComplete);
        bundle.putString(BindingKeys.POP_UP_LOCATION, PopUpPrefs.INSTANCE.getLocation().toString());
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundleWithAchievement(String id) {
        Bundle bundle = createRequestBundle();
        bundle.putString(AchievementsBindingKeys.ACHIEVEMENT_ID_KEY, id);
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle createRequestBundle() {
        return new Bundle();
    }
}
