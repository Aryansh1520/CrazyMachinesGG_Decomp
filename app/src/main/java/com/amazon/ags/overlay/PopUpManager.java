package com.amazon.ags.overlay;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import com.amazon.ags.api.overlay.PopUpLocation;
import com.amazon.ags.client.AmazonGamesService;
import com.amazon.ags.client.GCResponseHandleImpl;
import com.amazon.ags.client.achievements.AchievementsService;
import com.amazon.ags.client.leaderboards.LeaderboardsService;
import com.amazon.ags.constants.LeaderboardBindingKeys;

/* loaded from: classes.dex */
public class PopUpManager {
    private static final String FEATURE_NAME = "AGS_OL";
    private static PopUpManager INSTANCE;
    private static final String TAG = "AGS_OL_" + PopUpManager.class.getSimpleName();
    private final AchievementsService achievementsService;
    private final AmazonGamesService amazonGamesService;
    private final Context context;
    private final LeaderboardsService leaderboardsService;
    private final PopUpPrefs preferences = PopUpPrefs.INSTANCE;

    private PopUpManager(Context context, LeaderboardsService leaderboardsService, AchievementsService achievementsService, AmazonGamesService amazonGamesService) {
        this.context = context;
        this.leaderboardsService = leaderboardsService;
        this.achievementsService = achievementsService;
        this.amazonGamesService = amazonGamesService;
    }

    public static synchronized PopUpManager initialize(Context context, LeaderboardsService leaderboardsService, AchievementsService achievementsService, AmazonGamesService amazonGamesService) {
        PopUpManager popUpManager;
        synchronized (PopUpManager.class) {
            INSTANCE = new PopUpManager(context, leaderboardsService, achievementsService, amazonGamesService);
            popUpManager = INSTANCE;
        }
        return popUpManager;
    }

    public static PopUpManager getInstance() {
        if (INSTANCE == null) {
            Log.e(TAG, "Be sure to call initialize() first");
        }
        return INSTANCE;
    }

    public void executePopUp(RemoteViews remoteViews, int overlayActionCode) {
        Bundle overlayDataBundle = new Bundle();
        PopUpContent content = new PopUpContent(overlayActionCode, remoteViews, overlayDataBundle);
        executePopUp(content);
    }

    public void executePopUp(PopUpContent content) {
        Log.d(TAG, "Entering executePopUp with content: " + content);
        if (!this.preferences.isEnabled()) {
            Log.d(TAG, "Pop-ups disabled, skipping pop-up");
            return;
        }
        PopUpPrefs prefs = PopUpPrefs.INSTANCE;
        AGSToast overlay = new AGSToast(this.context, content, prefs.getLocation(), prefs.getFadeInDuration(), prefs.getFadeOutDuration());
        overlay.setOnClickListener(new AGOverlayClickListener(content, overlay));
        overlay.show();
    }

    public void executeWelcomeBackPopUp(RemoteViews remoteViews, int overlayActionCode) {
        Bundle overlayDataBundle = new Bundle();
        PopUpContent content = new PopUpContent(overlayActionCode, remoteViews, overlayDataBundle);
        PopUpPrefs prefs = PopUpPrefs.INSTANCE;
        AGSToast overlay = new AGSToast(this.context, content, PopUpLocation.BOTTOM_CENTER, prefs.getWelcomeBackFadeInDuration(), prefs.getWelcomeBackFadeOutDuration());
        overlay.setOnClickListener(new AGOverlayClickListener(content, overlay));
        overlay.show();
    }

    /* loaded from: classes.dex */
    public class AGOverlayClickListener implements View.OnClickListener {
        private final PopUpContent content;
        private final AGSToast overlay;

        public AGOverlayClickListener(PopUpContent content, AGSToast overlay) {
            this.content = content;
            this.overlay = overlay;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            this.overlay.destroy();
            switch (this.content.getOverlayActionCode()) {
                case 0:
                    String leaderboardId = this.content.getOverlayData().getString(LeaderboardBindingKeys.LEADERBOARD_ID_KEY);
                    PopUpManager.this.leaderboardsService.showRanksOverlay(leaderboardId, new GCResponseHandleImpl<>(null));
                    return;
                case 1:
                    PopUpManager.this.achievementsService.showAchievementsOverlay(new GCResponseHandleImpl<>(null));
                    return;
                case 2:
                    PopUpManager.this.leaderboardsService.showLeaderboardsOverlay(new GCResponseHandleImpl<>(null));
                    return;
                case 3:
                    Message message = Message.obtain();
                    message.what = 29;
                    try {
                        PopUpManager.this.amazonGamesService.sendMessage(message);
                        return;
                    } catch (RemoteException e) {
                        Log.w(PopUpManager.TAG, "Error sending message to service: " + e);
                        return;
                    }
                default:
                    Log.d(PopUpManager.TAG, "No defined onClick action");
                    return;
            }
        }
    }
}
