package com.amazon.ags.api;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.amazon.ags.api.achievements.AchievementsClient;
import com.amazon.ags.api.leaderboards.LeaderboardsClient;
import com.amazon.ags.api.overlay.PopUpLocation;
import com.amazon.ags.api.profiles.ProfilesClient;
import com.amazon.ags.api.whispersync.WhisperSyncClient;
import com.amazon.ags.client.AmazonGamesService;
import com.amazon.ags.client.AmazonGamesStatusHandler;
import com.amazon.ags.client.ServiceProxy;
import com.amazon.ags.client.achievements.AchievementsClientImpl;
import com.amazon.ags.client.achievements.AchievementsService;
import com.amazon.ags.client.achievements.AchievementsServiceProxy;
import com.amazon.ags.client.leaderboards.LeaderboardsClientImpl;
import com.amazon.ags.client.leaderboards.LeaderboardsService;
import com.amazon.ags.client.leaderboards.LeaderboardsServiceProxy;
import com.amazon.ags.client.profiles.ProfilesClientImpl;
import com.amazon.ags.client.profiles.ProfilesService;
import com.amazon.ags.client.profiles.ProfilesServiceProxy;
import com.amazon.ags.client.whispersync.WhisperSyncServiceProxy;
import com.amazon.ags.jni.AGSJniHandler;
import com.amazon.ags.overlay.PopUpManager;
import com.amazon.ags.overlay.PopUpPrefs;
import dalvik.system.PathClassLoader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;

/* loaded from: classes.dex */
public class AmazonGamesClient implements AmazonGames {
    private static final String APK_PACKAGE_NAME = "com.amazon.ags.app";
    private static final String FEATURE_NAME = "GC";
    private static final String SOFTKEY_BAR_WRAPPER_CLASS_NAME = "com.amazon.ags.app.util.SoftkeyBarWrapper";
    private static final String SOFTKEY_BAR_WRAPPER_SETUP_METHOD_NAME = "setup";
    private AchievementsService achievementsService;
    private AmazonGamesService amazonGamesService;
    public final Handler apiHandler;
    private final Application application;
    private final Context context;
    private LeaderboardsService leaderboardsService;
    private ProfilesService profilesService;
    private static final String TAG = "GC_" + AmazonGamesClient.class.getSimpleName();
    private static AmazonGames INSTANCE = null;

    private AmazonGamesClient(Application application, AmazonGamesCallback amazonGamesCallback, EnumSet<AmazonGamesFeature> features) {
        this.application = application;
        this.context = this.application.getApplicationContext();
        Handler statusHandler = new AmazonGamesStatusHandler(amazonGamesCallback);
        this.amazonGamesService = new ServiceProxy(statusHandler, features);
        this.amazonGamesService.bind(this.context);
        this.apiHandler = new Handler();
        this.leaderboardsService = new LeaderboardsServiceProxy(this, this.amazonGamesService, this.apiHandler);
        this.achievementsService = new AchievementsServiceProxy(this, this.amazonGamesService, this.apiHandler);
        this.profilesService = new ProfilesServiceProxy(this, this.amazonGamesService, this.apiHandler);
        PopUpManager.initialize(this.context, this.leaderboardsService, this.achievementsService, this.amazonGamesService);
        enableSoftKeyButton();
    }

    public static synchronized AmazonGames initialize(Application application, AmazonGamesCallback callback, EnumSet<AmazonGamesFeature> features) {
        AmazonGames amazonGames;
        synchronized (AmazonGamesClient.class) {
            INSTANCE = new AmazonGamesClient(application, callback, features);
            amazonGames = INSTANCE;
        }
        return amazonGames;
    }

    public static AmazonGames getInstance() {
        if (INSTANCE == null) {
            Log.e(TAG, "AmazonGames is not initialized.  Be sure to call AmazonGamesClient.initialize() first");
        }
        return INSTANCE;
    }

    public AmazonGamesClient(Application application, AmazonGamesService amazonGames) {
        this.application = application;
        this.context = this.application.getApplicationContext();
        this.amazonGamesService = amazonGames;
        this.amazonGamesService.bind(this.context);
        this.apiHandler = new Handler();
        this.leaderboardsService = new LeaderboardsServiceProxy(this, this.amazonGamesService, this.apiHandler);
        this.achievementsService = new AchievementsServiceProxy(this, this.amazonGamesService, this.apiHandler);
        this.profilesService = new ProfilesServiceProxy(this, this.amazonGamesService, this.apiHandler);
        PopUpManager.initialize(this.context, this.leaderboardsService, this.achievementsService, this.amazonGamesService);
        enableSoftKeyButton();
    }

    public AmazonGamesClient(Application application, AmazonGamesService amazonGames, LeaderboardsService leaderboardsService, AchievementsService achievementsService) {
        this.application = application;
        this.context = this.application.getApplicationContext();
        this.amazonGamesService = amazonGames;
        this.amazonGamesService.bind(this.context);
        this.apiHandler = new Handler();
        this.leaderboardsService = leaderboardsService;
        this.achievementsService = achievementsService;
        this.profilesService = new ProfilesServiceProxy(this, this.amazonGamesService, this.apiHandler);
        PopUpManager.initialize(this.context, leaderboardsService, achievementsService, this.amazonGamesService);
        enableSoftKeyButton();
    }

    static synchronized void initialize(AmazonGames inst) {
        synchronized (AmazonGamesClient.class) {
            INSTANCE = inst;
        }
    }

    @Override // com.amazon.ags.api.AmazonGames
    public final boolean isReady() {
        return this.amazonGamesService.isReady();
    }

    @Override // com.amazon.ags.api.AmazonGames
    public final AmazonGamesStatus getStatus() {
        return this.amazonGamesService.getStatus();
    }

    @Override // com.amazon.ags.api.AmazonGames
    public final WhisperSyncClient getWhisperSyncClient() {
        return new WhisperSyncServiceProxy(this.context, this.amazonGamesService, this.apiHandler);
    }

    @Override // com.amazon.ags.api.AmazonGames
    public final LeaderboardsClient getLeaderboardsClient() {
        return new LeaderboardsClientImpl(this.amazonGamesService, this.leaderboardsService);
    }

    @Override // com.amazon.ags.api.AmazonGames
    public final AchievementsClient getAchievementsClient() {
        return new AchievementsClientImpl(this.amazonGamesService, this.achievementsService);
    }

    @Override // com.amazon.ags.api.AmazonGames
    public final ProfilesClient getProfilesClient() {
        return new ProfilesClientImpl(this.amazonGamesService, this.profilesService);
    }

    @Override // com.amazon.ags.api.AmazonGames
    public void initializeJni() {
        AGSJniHandler.initializeJni(this);
    }

    public final Context getAppContext() {
        return this.context;
    }

    @Override // com.amazon.ags.api.AmazonGames
    public void setPopUpLocation(PopUpLocation location) {
        PopUpPrefs.INSTANCE.setLocation(location);
    }

    private void enableSoftKeyButton() {
        try {
            ApplicationInfo info = this.application.getPackageManager().getApplicationInfo(APK_PACKAGE_NAME, 0);
            String apkName = info.sourceDir;
            PathClassLoader myClassLoader = new PathClassLoader(apkName, ClassLoader.getSystemClassLoader());
            Class<?> classType = Class.forName(SOFTKEY_BAR_WRAPPER_CLASS_NAME, true, myClassLoader);
            Method setupMethod = classType.getDeclaredMethod(SOFTKEY_BAR_WRAPPER_SETUP_METHOD_NAME, Application.class, BroadcastReceiver.class);
            Object instance = classType.newInstance();
            setupMethod.invoke(instance, this.application, new BroadcastReceiver() { // from class: com.amazon.ags.api.AmazonGamesClient.1
                @Override // android.content.BroadcastReceiver
                public final void onReceive(Context context, Intent intent) {
                    Log.d(AmazonGamesClient.TAG, "GameCircle softkey button pressed.");
                    AmazonGamesClient.this.handleSoftkeyButtonPress();
                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to enable softkey button: " + e.toString());
        } catch (ClassNotFoundException e2) {
            Log.e(TAG, "Failed to enable softkey button: " + e2.toString());
        } catch (IllegalAccessException e3) {
            Log.e(TAG, "Failed to enable softkey button: " + e3.toString());
        } catch (IllegalArgumentException e4) {
            Log.e(TAG, "Failed to enable softkey button: " + e4.toString());
        } catch (InstantiationException e5) {
            Log.e(TAG, "Failed to enable softkey button: " + e5.toString());
        } catch (NoSuchMethodException e6) {
            Log.e(TAG, "Failed to enable softkey button: " + e6.toString());
        } catch (SecurityException e7) {
            Log.e(TAG, "Failed to enable softkey button: " + e7.toString());
        } catch (InvocationTargetException e8) {
            Log.e(TAG, "Failed to enable softkey button: " + e8.toString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSoftkeyButtonPress() {
        this.apiHandler.post(new Runnable() { // from class: com.amazon.ags.api.AmazonGamesClient.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    Message msg = Message.obtain();
                    msg.what = 30;
                    msg.setData(new Bundle());
                    AmazonGamesClient.this.amazonGamesService.sendMessage(msg);
                } catch (RemoteException e) {
                    Log.e(AmazonGamesClient.TAG, "Failed to send softkey press to APK: " + e.toString());
                }
            }
        });
    }
}
