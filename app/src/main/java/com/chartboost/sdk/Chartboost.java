package com.chartboost.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import com.chartboost.sdk.Libraries.CBConstants;
import com.chartboost.sdk.Libraries.CBUtility;
import com.chartboost.sdk.Libraries.CBWebImageCache;
import com.chartboost.sdk.Model.CBImpression;
import com.chartboost.sdk.Networking.CBAPIConnection;
import com.chartboost.sdk.Networking.CBAPIRequest;
import com.chartboost.sdk.Networking.CBReachability;
import com.chartboost.sdk.Networking.CBURLOpener;
import com.chartboost.sdk.View.CBDefaultViewController;
import com.mappn.sdk.common.utils.BaseConstants;
import com.mappn.sdk.pay.chargement.ChargeActivity;
import com.mappn.sdk.pay.util.Constants;
import com.mokredit.payment.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class Chartboost implements CBAPIConnection.CBAPIConnectionDelegate, CBURLOpener.CBUrlOpenerDelegate, CBImpression.CBImpressionProtocol {
    private static final long MINIMUM_SESSION_INTERVAL = 10000;
    public static final String TAG = "Chartboost";
    private static final long kCBCachedInterstitialExpirationTimeout = 86400;
    public static final String kCBDefaultLocation = "Default";
    public static final String kCBSessionCountKey = "CBSessionCountKey";
    private static Chartboost sharedChartboost = null;

    @Deprecated
    public String appId;

    @Deprecated
    public String appSignature;

    @Deprecated
    public ChartboostDelegate delegate;
    private Handler handler;
    private CBConstants.CBOrientation orientation;
    private boolean orientationOverridden;
    private CBImpression visibleImpression;
    public int timeout = 30000;
    private Activity activity = null;
    private boolean activityIsStarted = false;
    private boolean createLoadingViewOnStart = false;
    private long lastOnStopTime = 0;
    public CBAPIConnection connection = new CBAPIConnection(null, this, null);
    private Map<String, CBImpression> cachedInterstitials = new HashMap();
    private CBImpression cachedMoreApps = null;

    @Deprecated
    public static synchronized Chartboost getSharedChartBoost(Context context) {
        Chartboost sharedChartboost2;
        synchronized (Chartboost.class) {
            sharedChartboost2 = sharedChartboost();
        }
        return sharedChartboost2;
    }

    public static synchronized Chartboost sharedChartboost() {
        Chartboost chartboost;
        synchronized (Chartboost.class) {
            if (sharedChartboost == null) {
                sharedChartboost = new Chartboost();
            }
            chartboost = sharedChartboost;
        }
        return chartboost;
    }

    private Chartboost() {
    }

    public void startSession() {
        if (this.activity == null) {
            throw new IllegalStateException("The context must be set through the Chartboost method onCreate() before calling startSession().");
        }
        long now = (long) (System.nanoTime() / 1000000.0d);
        if (now - this.lastOnStopTime >= MINIMUM_SESSION_INTERVAL) {
            CBWebImageCache.sharedCache().clearCache();
            SharedPreferences prefs = CBUtility.getPreferences();
            int sessionCount = prefs.getInt(CBUtility.PREFERENCES_KEY_SESSION_COUNT, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(CBUtility.PREFERENCES_KEY_SESSION_COUNT, sessionCount + 1);
            editor.commit();
            CBAPIRequest request = new CBAPIRequest("api", "install");
            request.appendDeviceInfoParams();
            request.sign(this.appId, this.appSignature);
            request.contextBlock = new CBAPIRequest.CBAPIResponseBlock() { // from class: com.chartboost.sdk.Chartboost.1
                @Override // com.chartboost.sdk.Networking.CBAPIRequest.CBAPIResponseBlock
                public void execute(JSONObject response) {
                    String latestSDKVersion;
                    boolean isDebug = CBUtility.isDebugBuild(Chartboost.this.activity);
                    if (isDebug && (latestSDKVersion = response.optString("latest-sdk-version")) != null && latestSDKVersion.equals(CBConstants.kCBAPIVersion)) {
                        Log.w(Chartboost.TAG, String.format("WARNING: you have an outdated version of the SDK (Current: %s, Latest: %s). Get the latest version at chartboost.com/support/sdk#android", latestSDKVersion, CBConstants.kCBAPIVersion));
                    }
                }
            };
            this.connection.sendRequest(request);
        }
    }

    @Deprecated
    public void install() {
        startSession();
    }

    public void cacheInterstitial() {
        cacheInterstitial(kCBDefaultLocation);
    }

    public void cacheInterstitial(String location) {
        if (this.activity == null) {
            throw new IllegalStateException("The context must be set through the Chartboost method onCreate() before calling cacheInterstitial().");
        }
        if (this.delegate != null && !this.delegate.shouldRequestInterstitialsInFirstSession()) {
            int sessionCount = CBUtility.getPreferences().getInt(CBUtility.PREFERENCES_KEY_SESSION_COUNT, 0);
            if (sessionCount <= 1) {
                return;
            }
        }
        loadInterstitial(location, true);
    }

    public void showInterstitial() {
        showInterstitial(kCBDefaultLocation);
    }

    public void showInterstitial(String location) {
        if (this.activity == null) {
            throw new IllegalStateException("The context must be set through the Chartboost method onCreate() before calling showInterstitial().");
        }
        if (this.delegate != null && !this.delegate.shouldRequestInterstitialsInFirstSession()) {
            int sessionCount = CBUtility.getPreferences().getInt(CBUtility.PREFERENCES_KEY_SESSION_COUNT, 0);
            if (sessionCount == 1) {
                return;
            }
        }
        Runnable showInterstitial = new ShowInterstitialRunnable(location);
        this.handler.post(showInterstitial);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ShowInterstitialRunnable implements Runnable {
        private String location;

        public ShowInterstitialRunnable(String location) {
            this.location = location;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!Chartboost.this.hasCachedInterstitial(this.location)) {
                Chartboost.this.loadInterstitial(this.location, false);
            } else {
                Chartboost.this.attemptShowingInterstitial((CBImpression) Chartboost.this.cachedInterstitials.get(this.location));
            }
        }
    }

    public boolean hasCachedInterstitial() {
        return hasCachedInterstitial(kCBDefaultLocation);
    }

    public boolean hasCachedMoreApps() {
        return this.cachedMoreApps != null;
    }

    public boolean hasCachedInterstitial(String location) {
        CBImpression interstitial = this.cachedInterstitials.get(location);
        if (interstitial == null) {
            return false;
        }
        long diffInSecs = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - interstitial.responseDate.getTime());
        return diffInSecs < 86400;
    }

    public void cacheMoreApps() {
        if (this.activity == null) {
            throw new IllegalStateException("The context must be set through the Chartboost method onCreate() before calling cacheMoreApps().");
        }
        loadMoreAppsShouldCache(true);
    }

    public void showMoreApps() {
        if (this.activity == null) {
            throw new IllegalStateException("The context must be set through the Chartboost method onCreate() before calling showMoreApps().");
        }
        Runnable showMoreApps = new ShowMoreAppsRunnable(this, null);
        this.handler.post(showMoreApps);
    }

    /* loaded from: classes.dex */
    private class ShowMoreAppsRunnable implements Runnable {
        private ShowMoreAppsRunnable() {
        }

        /* synthetic */ ShowMoreAppsRunnable(Chartboost chartboost, ShowMoreAppsRunnable showMoreAppsRunnable) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Chartboost.this.cachedMoreApps == null) {
                Chartboost.this.loadMoreAppsShouldCache(false);
            } else {
                Chartboost.this.attemptShowingMoreApps(Chartboost.this.cachedMoreApps);
            }
        }
    }

    public void setIdentityTrackingDisabledOnThisDevice(boolean disabled) {
        SharedPreferences.Editor editor = CBUtility.getPreferences().edit();
        editor.putBoolean(CBUtility.PREFERENCES_KEY_ID_TRACKING_DISABLED, disabled);
        editor.commit();
    }

    public boolean isIdentityTrackingDisabledOnThisDevice() {
        return CBUtility.isIdentityTrackingDisabledOnThisDevice();
    }

    public void setOrientation(CBConstants.CBOrientation _orientation) {
        this.orientationOverridden = _orientation != CBConstants.CBOrientation.UNSPECIFIED;
        this.orientation = _orientation;
    }

    public CBConstants.CBOrientation orientation() {
        if (this.activity == null) {
            throw new IllegalStateException("The context must be set through the Chartboost method onCreate() before calling orientation().");
        }
        return (!this.orientationOverridden || this.orientation == CBConstants.CBOrientation.UNSPECIFIED) ? CBUtility.getOrientation(this.activity) : this.orientation;
    }

    public CBConstants.CBOrientation.Difference getForcedOrientationDifference() {
        if (!this.orientationOverridden) {
            return CBConstants.CBOrientation.Difference.ANGLE_0;
        }
        CBConstants.CBOrientation real = CBUtility.getOrientation(this.activity);
        CBConstants.CBOrientation fake = orientation();
        if (fake == CBConstants.CBOrientation.UNSPECIFIED || fake == real) {
            return CBConstants.CBOrientation.Difference.ANGLE_0;
        }
        if (fake == real.rotate90()) {
            return CBConstants.CBOrientation.Difference.ANGLE_90;
        }
        if (fake == real.rotate180()) {
            return CBConstants.CBOrientation.Difference.ANGLE_180;
        }
        return CBConstants.CBOrientation.Difference.ANGLE_270;
    }

    private void handleChartboostViewError(CBImpression.CBImpressionType type, String location) {
        if (type == CBImpression.CBImpressionType.CBImpressionTypeMoreApps && CBDefaultViewController.sharedController().loadingViewIsVisible) {
            CBDefaultViewController.sharedController().dismissLoadingView(this.activity);
        }
        if (type == CBImpression.CBImpressionType.CBImpressionTypeInterstitial && this.delegate != null) {
            this.delegate.didFailToLoadInterstitial(location);
        }
        if (type == CBImpression.CBImpressionType.CBImpressionTypeMoreApps && this.delegate != null) {
            this.delegate.didFailToLoadMoreApps();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadInterstitial(final String location, final boolean shouldCache) {
        if (this.delegate == null || this.delegate.shouldRequestInterstitial(location)) {
            if (!CBReachability.reachabilityForInternetConnection()) {
                if (this.delegate != null) {
                    this.delegate.didFailToLoadInterstitial(location);
                    return;
                }
                return;
            }
            CBAPIRequest request = new CBAPIRequest("api", "get");
            request.appendDeviceInfoParams();
            request.appendBodyArgument("location", location);
            if (shouldCache) {
                request.appendBodyArgument(CBAPIRequest.CB_PARAM_CACHE, BaseConstants.DEFAULT_UC_CNO);
            }
            request.sign(this.appId, this.appSignature);
            request.contextBlock = new CBAPIRequest.CBAPIResponseBlock() { // from class: com.chartboost.sdk.Chartboost.2
                @Override // com.chartboost.sdk.Networking.CBAPIRequest.CBAPIResponseBlock
                public void execute(JSONObject response) {
                    Chartboost.sharedChartboost().loadChartboostViewForResponse(response, CBImpression.CBImpressionType.CBImpressionTypeInterstitial, shouldCache, location);
                }
            };
            this.connection.sendRequest(request);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadMoreAppsShouldCache(final boolean shouldCache) {
        if (!CBReachability.reachabilityForInternetConnection()) {
            if (this.delegate != null) {
                this.delegate.didFailToLoadMoreApps();
                return;
            }
            return;
        }
        CBAPIRequest request = new CBAPIRequest("api", Constants.RES_LIST_ITEM_MORE_ICON);
        request.appendDeviceInfoParams();
        if (shouldCache) {
            request.appendBodyArgument(CBAPIRequest.CB_PARAM_CACHE, BaseConstants.DEFAULT_UC_CNO);
        }
        request.sign(this.appId, this.appSignature);
        request.contextBlock = new CBAPIRequest.CBAPIResponseBlock() { // from class: com.chartboost.sdk.Chartboost.3
            @Override // com.chartboost.sdk.Networking.CBAPIRequest.CBAPIResponseBlock
            public void execute(JSONObject response) {
                Chartboost.sharedChartboost().loadChartboostViewForResponse(response, CBImpression.CBImpressionType.CBImpressionTypeMoreApps, shouldCache, null);
            }
        };
        this.connection.sendRequest(request);
        if (!shouldCache && this.delegate != null && this.delegate.shouldDisplayLoadingViewForMoreApps()) {
            CBDefaultViewController.sharedController().displayLoadingView(this.activity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadChartboostViewForResponse(JSONObject response, CBImpression.CBImpressionType type, boolean shouldCache, String location) {
        CBImpression.CBImpressionState initialState;
        if (!response.optString("status", StringUtils.EMPTY).equals("200")) {
            handleChartboostViewError(type, location);
            return;
        }
        if (type != CBImpression.CBImpressionType.CBImpressionTypeMoreApps || shouldCache || CBDefaultViewController.sharedController().loadingViewIsVisible) {
            if (shouldCache) {
                initialState = CBImpression.CBImpressionState.CBImpressionStateWaitingForCaching;
            } else {
                initialState = CBImpression.CBImpressionState.CBImpressionStateWaitingForDisplay;
            }
            new CBImpression(response, type, this, initialState, location);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clickResponseReceived(JSONObject response, String url) {
        CBURLOpener.openerWithDelegate(this).open(url, this.activity);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attemptShowingInterstitial(CBImpression interstitial) {
        if (interstitial.overrideAskToShow || this.delegate == null || this.delegate.shouldDisplayInterstitial(interstitial.location)) {
            if (interstitial.state == CBImpression.CBImpressionState.CBImpressionStateCached && this.cachedInterstitials.get(interstitial.location) == interstitial) {
                this.cachedInterstitials.remove(interstitial.location);
                CBAPIRequest request = new CBAPIRequest("api", "show");
                request.appendDeviceInfoParams();
                String ad_id = interstitial.responseContext.optString(CBAPIRequest.CB_PARAM_AD_ID);
                if (ad_id != null) {
                    request.appendBodyArgument(CBAPIRequest.CB_PARAM_AD_ID, ad_id);
                }
                request.sign(this.appId, this.appSignature);
                this.connection.sendRequest(request);
            }
            CBDefaultViewController.sharedController().displayInterstitial(interstitial, this.activity);
            interstitial.state = CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attemptShowingMoreApps(CBImpression moreApps) {
        if (moreApps.overrideAskToShow || this.delegate == null || this.delegate.shouldDisplayMoreApps()) {
            if (moreApps == this.cachedMoreApps) {
                this.cachedMoreApps = null;
            }
            boolean loadingCached = moreApps.state == CBImpression.CBImpressionState.CBImpressionStateCached;
            moreApps.state = CBImpression.CBImpressionState.CBImpressionStateOther;
            if (CBDefaultViewController.sharedController().loadingViewIsVisible) {
                CBDefaultViewController.sharedController().dismissLoadingView(this.activity);
            } else if (!loadingCached && !moreApps.overrideLoadingViewRequirement) {
                return;
            }
            CBDefaultViewController.sharedController().displayMoreApps(moreApps, this.activity);
            moreApps.state = CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController;
        }
    }

    @Override // com.chartboost.sdk.Networking.CBURLOpener.CBUrlOpenerDelegate
    public void urlWillOpen(String url) {
        if (CBDefaultViewController.sharedController().loadingViewIsVisible) {
            CBDefaultViewController.sharedController().dismissLoadingView(this.activity);
        }
    }

    @Override // com.chartboost.sdk.Model.CBImpression.CBImpressionProtocol
    public void impressionReadyToBeDisplayed(CBImpression impression) {
        if (impression.state == CBImpression.CBImpressionState.CBImpressionStateWaitingForDisplay) {
            impression.state = CBImpression.CBImpressionState.CBImpressionStateOther;
            if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeInterstitial) {
                attemptShowingInterstitial(impression);
                return;
            } else {
                if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeMoreApps) {
                    attemptShowingMoreApps(impression);
                    return;
                }
                return;
            }
        }
        if (impression.state == CBImpression.CBImpressionState.CBImpressionStateWaitingForCaching) {
            if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeInterstitial && impression.location != null) {
                if (this.delegate != null) {
                    this.delegate.didCacheInterstitial(impression.location);
                }
                this.cachedInterstitials.put(impression.location, impression);
            } else if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeMoreApps) {
                if (this.delegate != null) {
                    this.delegate.didCacheMoreApps();
                }
                if (this.cachedMoreApps != null) {
                    this.cachedMoreApps = null;
                }
                this.cachedMoreApps = impression;
            }
            impression.state = CBImpression.CBImpressionState.CBImpressionStateCached;
        }
    }

    @Override // com.chartboost.sdk.Model.CBImpression.CBImpressionProtocol
    public void impressionCloseTriggered(CBImpression impression) {
        this.visibleImpression = null;
        if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeInterstitial) {
            if (this.delegate != null) {
                this.delegate.didDismissInterstitial(impression.location);
            }
            if (this.delegate != null) {
                this.delegate.didCloseInterstitial(impression.location);
            }
            if (impression.state == CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController) {
                CBDefaultViewController.sharedController().dismissInterstitial(impression, this.activity);
                return;
            }
            return;
        }
        if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeMoreApps) {
            if (this.delegate != null) {
                this.delegate.didDismissMoreApps();
            }
            if (this.delegate != null) {
                this.delegate.didCloseMoreApps();
            }
            if (impression.state == CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController) {
                CBDefaultViewController.sharedController().dismissMoreApps(impression, this.activity);
            }
        }
    }

    private void SET_API_KEY_SAFELY(JSONObject from, String key, CBAPIRequest request) {
        if (from != null && from.optString(key) != null) {
            request.appendBodyArgument(key, from.optString(key));
        }
    }

    @Override // com.chartboost.sdk.Model.CBImpression.CBImpressionProtocol
    public void impressionClickTriggered(CBImpression impression, final String urlString, JSONObject moreData) {
        this.visibleImpression = null;
        if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeInterstitial) {
            if (this.delegate != null) {
                this.delegate.didDismissInterstitial(impression.location);
            }
            if (this.delegate != null) {
                this.delegate.didClickInterstitial(impression.location);
            }
            if (impression.state == CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController) {
                CBDefaultViewController.sharedController().dismissInterstitial(impression, this.activity);
            }
        } else if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeMoreApps) {
            if (this.delegate != null) {
                this.delegate.didDismissMoreApps();
            }
            if (this.delegate != null) {
                this.delegate.didClickMoreApps();
            }
            if (impression.state == CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController) {
                CBDefaultViewController.sharedController().dismissMoreApps(impression, this.activity);
            }
        }
        CBAPIRequest request = new CBAPIRequest("api", "click");
        request.appendDeviceInfoParams();
        SET_API_KEY_SAFELY(impression.responseContext, "to", request);
        SET_API_KEY_SAFELY(impression.responseContext, "cgn", request);
        SET_API_KEY_SAFELY(impression.responseContext, "creative", request);
        SET_API_KEY_SAFELY(impression.responseContext, CBAPIRequest.CB_PARAM_AD_ID, request);
        SET_API_KEY_SAFELY(moreData, "cgn", request);
        SET_API_KEY_SAFELY(moreData, "creative", request);
        SET_API_KEY_SAFELY(moreData, com.mappn.sdk.uc.util.Constants.PUSH_TYPE, request);
        SET_API_KEY_SAFELY(moreData, "more_type", request);
        request.sign(this.appId, this.appSignature);
        if (urlString != null && !urlString.equals(StringUtils.EMPTY) && !urlString.equals(ChargeActivity.TYPE_CHARGE_TYPE_LIST)) {
            request.contextBlock = new CBAPIRequest.CBAPIResponseBlock() { // from class: com.chartboost.sdk.Chartboost.4
                @Override // com.chartboost.sdk.Networking.CBAPIRequest.CBAPIResponseBlock
                public void execute(JSONObject response) {
                    Chartboost.sharedChartboost().clickResponseReceived(response, urlString);
                }
            };
            CBDefaultViewController.sharedController().displayLoadingView(this.activity);
        }
        this.connection.sendRequest(request);
    }

    @Override // com.chartboost.sdk.Model.CBImpression.CBImpressionProtocol
    public void impressionFailedToInitialize(CBImpression impression) {
        handleChartboostViewError(impression.type, impression.type == CBImpression.CBImpressionType.CBImpressionTypeInterstitial ? impression.location : null);
    }

    @Override // com.chartboost.sdk.Networking.CBAPIConnection.CBAPIConnectionDelegate
    public void didFailToReceiveAPIResponseForRequest(CBAPIRequest request) {
        if (request.action.equals("get")) {
            handleChartboostViewError(CBImpression.CBImpressionType.CBImpressionTypeInterstitial, request.body.optString("location", kCBDefaultLocation));
        } else if (request.action.equals(Constants.RES_LIST_ITEM_MORE_ICON)) {
            handleChartboostViewError(CBImpression.CBImpressionType.CBImpressionTypeMoreApps, request.body.optString("location", kCBDefaultLocation));
        }
        if (CBDefaultViewController.sharedController().loadingViewIsVisible) {
            CBDefaultViewController.sharedController().dismissLoadingView(this.activity);
        }
    }

    @Override // com.chartboost.sdk.Networking.CBAPIConnection.CBAPIConnectionDelegate
    public void didReceiveAPIResponse(JSONObject response, CBAPIRequest request) {
        if (request.contextBlock != null) {
            request.contextBlock.execute(response);
        }
    }

    public void forceDismissViewNotification() {
        if (this.visibleImpression != null) {
            impressionCloseTriggered(this.visibleImpression);
        }
    }

    public Activity getContext() {
        return this.activity;
    }

    @Deprecated
    public void setContext(Context _context) {
        if (_context == null || !(_context instanceof Activity)) {
            Log.e(TAG, "Chartboost context should be instance of activity");
        }
        this.activity = (Activity) _context;
    }

    public void onCreate(Activity activity, String appId, String appSignature, ChartboostDelegate chartBoostDelegate) {
        if (this.activity != null && this.activityIsStarted && this.activity != activity) {
            onStop(this.activity);
            this.activityIsStarted = false;
        }
        this.activity = activity;
        this.appId = appId;
        this.appSignature = appSignature;
        this.delegate = chartBoostDelegate;
        this.handler = new Handler();
    }

    public void onStart(Activity activity) {
        this.activity = activity;
        this.activityIsStarted = true;
        boolean createLoadingView = false;
        if (this.createLoadingViewOnStart) {
            this.createLoadingViewOnStart = false;
            createLoadingView = true;
        }
        if (this.visibleImpression != null && this.visibleImpression.state == CBImpression.CBImpressionState.CBImpressionStateWaitingForDisplay) {
            boolean viewReady = this.visibleImpression.reinitialize();
            if (viewReady) {
                createLoadingView = false;
            }
        }
        if (createLoadingView) {
            CBDefaultViewController.sharedController().displayLoadingView(this.activity);
        }
    }

    public void onStop(Activity activity) {
        if (this.activity != null && this.activity == activity) {
            this.activityIsStarted = false;
            this.createLoadingViewOnStart = false;
            if (CBDefaultViewController.sharedController().loadingViewIsVisible) {
                CBDefaultViewController.sharedController().dismissLoadingView(this.activity);
                this.createLoadingViewOnStart = true;
            }
            if (this.visibleImpression != null) {
                CBDefaultViewController.sharedController().dismissImpressionSilently(this.visibleImpression, this.activity);
            }
            this.lastOnStopTime = (long) (System.nanoTime() / 1000000.0d);
        }
    }

    public boolean onBackPressed() {
        if (this.activity == null) {
            throw new IllegalStateException("The Chartboost methods onCreate(), onStart(), and onStop() must be called in the corresponding methods of your activity in order for Chartboost to function properly.");
        }
        if (this.visibleImpression != null && this.visibleImpression.state == CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController) {
            forceDismissViewNotification();
            return true;
        }
        if (CBDefaultViewController.sharedController().loadingViewIsVisible) {
            CBDefaultViewController.sharedController().dismissLoadingView(this.activity);
            return true;
        }
        return false;
    }

    public void clearCache() {
        this.cachedInterstitials = new HashMap();
    }

    public void setActiveImpression(CBImpression impression) {
        this.visibleImpression = impression;
    }
}
