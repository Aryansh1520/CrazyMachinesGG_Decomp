package com.vivamedia.CMTablet;

import android.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.AmazonGames;
import com.amazon.ags.api.AmazonGamesCallback;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.AmazonGamesFeature;
import com.amazon.ags.api.AmazonGamesStatus;
import com.amazon.ags.api.achievements.AchievementsClient;
import com.amazon.ags.api.achievements.UpdateProgressResponse;
import com.amazon.ags.api.leaderboards.GetPlayerScoreResponse;
import com.amazon.ags.api.leaderboards.LeaderboardsClient;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;
import com.amazon.ags.constants.LeaderboardFilter;
import com.amazon.ags.overlay.PopUpPrefs;
import com.amazon.inapp.purchasing.Offset;
import com.amazon.inapp.purchasing.PurchasingManager;
import com.chartboost.sdk.Chartboost;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.vending.licensing.LicenseChecker;
import com.google.android.vending.licensing.LicenseCheckerCallback;
import com.mappn.sdk.pay.GfanPay;
import com.mappn.sdk.pay.GfanPayCallback;
import com.mappn.sdk.pay.model.Order;
import com.mappn.sdk.uc.User;
import com.mokredit.payment.StringUtils;
import com.vivamedia.CMTablet.Build;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOffline;
import com.xiaomi.gamecenter.sdk.entry.MiGameType;
import com.xiaomi.gamecenter.sdk.entry.PayMode;
import com.xiaomi.gamecenter.sdk.entry.ScreenOrientation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/* loaded from: classes.dex */
public class MainActivity extends Activity implements ViewTreeObserver.OnGlobalLayoutListener, OnLoginProcessListener, OnPayProcessListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$vivamedia$CMTablet$Build$Store = null;
    private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt4yMbCMa1o/MfImQyuLUUczT9uHj0NykL9KqpCw1GGaDiu33Qf3ytLFTv+C7U5rJYYxv0+25FJ4Sb0RTnFRD3CkH7tJe2KeYkCHNo3dARR/SRGa7uzlYJ9JoDuN4Dq7H8AcYFpXF5bPLG0/WEmyigWfEtu+SMmIXtF/Kj4B7P+SDehfBSxdHtP4+LzkI3ksk/HdWAKTMd9dl1uY8VlxIu2/QyS2IaPrRr6JgZy3X064Zp+o/hcvJQ44xiTUxwA5+eabqtKLMCSxTPhlcaywjNvXmeaOZlJOm77gacRMwu4XwOKy9jWNYGFcdKSO9khfCzjiVc49G0uL4ZVyOMyPPVQIDAQAB";
    protected static final int LANGUAGE_CHINESE = 4;
    protected static final int LANGUAGE_ENGLISH = 9;
    protected static final int LANGUAGE_FRENCH = 12;
    protected static final int LANGUAGE_GERMAN = 7;
    protected static final int LANGUAGE_ITALIAN = 16;
    protected static final int LANGUAGE_JAPANESE = 17;
    protected static final int LANGUAGE_KOREAN = 18;
    protected static final int LANGUAGE_NEUTRAL = 0;
    protected static final int LANGUAGE_PORTUGESE = 22;
    protected static final int LANGUAGE_RUSSIAN = 25;
    protected static final int LANGUAGE_SPANISH = 10;
    private static final byte[] SALT = {-91, 47, -73, -107, -26, -13, 111, -72, -64, -46, 65, 30, Byte.MIN_VALUE, -103, -47, 74, -64, 51, 88, 89};
    private static boolean xiaomiLoggedIn = false;
    private FrameLayout rootLayout = null;
    private View rootView = null;
    private GLES2SurfaceView surfaceView = null;
    private AdView adView = null;
    private FrameLayout progressView = null;
    private boolean hasFocus = false;
    private boolean isSoftKeyboardOpen = false;
    private Chartboost chartboost = null;
    private boolean gameCircleOK = true;
    private boolean gameCircleErrorShown = false;
    EnumSet<AmazonGamesFeature> gameCircleFeatures = EnumSet.of(AmazonGamesFeature.Achievements, AmazonGamesFeature.Leaderboards);
    private boolean googleBillingSupported = false;
    private Handler googleBillingHandler = null;
    private GoogleBillingService googleBillingService = null;
    private GooglePurchaseObserver googlePurchaseObserver = null;
    private LicenseChecker googleLicenseChecker = null;
    private GoogleLicenseCheckerCallback googleLicenseCheckerCallback = null;
    private Handler googleLicenseHandler = null;
    private MediaPlayer musicPlayer = null;
    private boolean musicResumed = true;
    private ArrayList<HttpRequest> httpRequests = new ArrayList<>();

    protected static native int httpClientGetIDOfNewCreatedPostNative();

    protected static native byte[] httpClientGetPOSTParametersLinkedOfNewCreatedPostNative();

    protected static native byte[] httpClientGetUploadDataOfNewCreatedPostNative();

    protected static native int httpClientGetUploadDataSizeOfNewCreatedPostNative();

    protected static native boolean httpClientHasNewCreatedPostNative();

    protected static native boolean httpClientNewCreatedPostIsDataNative();

    protected static native void httpClientReceiveErrorNative(int i, int i2);

    protected static native void httpClientSetNewPostToWaitingStateNative();

    protected static native void httpClientSetResponseOfPostNative(int i, byte[] bArr, int i2, int i3);

    public static native void onPurchaseDoneNative(String str, int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public static native void onRenderNative();

    /* JADX INFO: Access modifiers changed from: protected */
    public static native void onSurfaceChangedNative(int i, int i2);

    /* JADX INFO: Access modifiers changed from: protected */
    public static native void onSurfaceCreatedNative();

    protected native void dispatchKeyEventNative(int i, int i2, int i3, int i4, long j, long j2);

    protected native void dispatchTouchEventNative(int i, int i2, float[] fArr, float[] fArr2, long j, long j2);

    protected native boolean isAdMobPurchasedNative();

    protected native boolean onBackNative();

    protected native void onCreateNative(String str, String str2, int i, AssetManager assetManager, int i2, int i3);

    protected native void onDestroyNative();

    protected native void onPauseNative(boolean z);

    protected native void onResumeNative();

    protected native void onStartNative();

    protected native void onStopNative();

    static /* synthetic */ int[] $SWITCH_TABLE$com$vivamedia$CMTablet$Build$Store() {
        int[] iArr = $SWITCH_TABLE$com$vivamedia$CMTablet$Build$Store;
        if (iArr == null) {
            iArr = new int[Build.Store.valuesCustom().length];
            try {
                iArr[Build.Store.AMAZON.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Build.Store.GFAN.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Build.Store.GOOGLE.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Build.Store.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Build.Store.XIAOMI.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            $SWITCH_TABLE$com$vivamedia$CMTablet$Build$Store = iArr;
        }
        return iArr;
    }

    static {
        System.loadLibrary("openal");
        System.loadLibrary("cmt");
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.rootLayout = new FrameLayout(this);
        this.surfaceView = new GLES2SurfaceView(this);
        this.rootLayout.addView(this.surfaceView);
        this.rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.rootView = this.rootLayout;
        this.rootView.requestFocus();
        setContentView(this.rootLayout);
        createAdView();
        if (Build.VERSION == Build.Version.LITE) {
            this.chartboost = Chartboost.sharedChartboost();
            switch ($SWITCH_TABLE$com$vivamedia$CMTablet$Build$Store()[Build.STORE.ordinal()]) {
                case 2:
                    this.chartboost.onCreate(this, "51137d0117ba47a35700006f", "483483d3115b31b6baf58b37e3a60cbb9a22000e", null);
                    break;
                default:
                    this.chartboost.onCreate(this, "51137b9e17ba47825900000a", "6d6213c3b1856631e21198d83f743956804743f8", null);
                    break;
            }
            this.chartboost.startSession();
            this.chartboost.showInterstitial();
        }
        if (Build.STORE == Build.Store.AMAZON) {
            if (isAmazonDevice()) {
                AmazonGamesClient.initialize(getApplication(), new AmazonGamesCallback() { // from class: com.vivamedia.CMTablet.MainActivity.1
                    private static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$ags$api$AmazonGamesStatus;

                    static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$ags$api$AmazonGamesStatus() {
                        int[] iArr = $SWITCH_TABLE$com$amazon$ags$api$AmazonGamesStatus;
                        if (iArr == null) {
                            iArr = new int[AmazonGamesStatus.values().length];
                            try {
                                iArr[AmazonGamesStatus.CANNOT_AUTHORIZE.ordinal()] = 6;
                            } catch (NoSuchFieldError e) {
                            }
                            try {
                                iArr[AmazonGamesStatus.CANNOT_BIND.ordinal()] = 4;
                            } catch (NoSuchFieldError e2) {
                            }
                            try {
                                iArr[AmazonGamesStatus.INITIALIZING.ordinal()] = 1;
                            } catch (NoSuchFieldError e3) {
                            }
                            try {
                                iArr[AmazonGamesStatus.INVALID_SESSION.ordinal()] = 5;
                            } catch (NoSuchFieldError e4) {
                            }
                            try {
                                iArr[AmazonGamesStatus.NOT_AUTHENTICATED.ordinal()] = 9;
                            } catch (NoSuchFieldError e5) {
                            }
                            try {
                                iArr[AmazonGamesStatus.NOT_AUTHORIZED.ordinal()] = 7;
                            } catch (NoSuchFieldError e6) {
                            }
                            try {
                                iArr[AmazonGamesStatus.SERVICE_CONNECTED.ordinal()] = 2;
                            } catch (NoSuchFieldError e7) {
                            }
                            try {
                                iArr[AmazonGamesStatus.SERVICE_DISCONNECTED.ordinal()] = 3;
                            } catch (NoSuchFieldError e8) {
                            }
                            try {
                                iArr[AmazonGamesStatus.SERVICE_NOT_OPTED_IN.ordinal()] = 8;
                            } catch (NoSuchFieldError e9) {
                            }
                            $SWITCH_TABLE$com$amazon$ags$api$AmazonGamesStatus = iArr;
                        }
                        return iArr;
                    }

                    @Override // com.amazon.ags.api.AmazonGamesCallback
                    public void onServiceReady() {
                    }

                    @Override // com.amazon.ags.api.AmazonGamesCallback
                    public void onServiceNotReady(AmazonGamesStatus reason) {
                        switch ($SWITCH_TABLE$com$amazon$ags$api$AmazonGamesStatus()[reason.ordinal()]) {
                            case 4:
                                MainActivity.this.gameCircleError("Service cannot authorize. Please check you network connection.");
                                return;
                            case 5:
                            case 8:
                            default:
                                return;
                            case 6:
                                MainActivity.this.gameCircleError("Game cannot authorize. Please check you network connection.");
                                return;
                            case 7:
                                MainActivity.this.gameCircleError("Your device is not registered with an account.");
                                return;
                            case 9:
                                MainActivity.this.gameCircleError("Game Circle could not authenticate. Please check your registration and network connection.");
                                return;
                        }
                    }
                }, this.gameCircleFeatures);
            } else {
                this.gameCircleErrorShown = true;
                this.gameCircleOK = false;
            }
        }
        switch ($SWITCH_TABLE$com$vivamedia$CMTablet$Build$Store()[Build.STORE.ordinal()]) {
            case 3:
                GfanPay.getInstance(getApplicationContext()).init();
                break;
            case 4:
                this.googleBillingHandler = new Handler();
                this.googleBillingService = new GoogleBillingService();
                this.googlePurchaseObserver = new GooglePurchaseObserver(this, this.googleBillingHandler);
                this.googleBillingService.setContext(this);
                this.googleBillingSupported = this.googleBillingService.checkBillingSupported();
                GoogleResponseHandler.register(this.googlePurchaseObserver);
                break;
            case 5:
                xiaomiLoggedIn = false;
                MiAppInfo appInfo = new MiAppInfo();
                appInfo.setAppId(17159);
                appInfo.setAppKey("78b293c3-ed65-5772-8d1a-51edf5f3f672");
                appInfo.setAppType(MiGameType.offline);
                appInfo.setPayMode(PayMode.custom);
                appInfo.setOrientation(ScreenOrientation.horizontal);
                MiCommplatform.Init(this, appInfo);
                this.progressView = new FrameLayout(this);
                this.progressView.setVisibility(8);
                View backgroundView = new View(this);
                backgroundView.setBackgroundColor(-1610612736);
                ProgressBar progressBar = new ProgressBar(this, null, R.attr.progressBarStyle);
                progressBar.setIndeterminate(true);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2, 17);
                this.progressView.addView(backgroundView, layoutParams);
                this.progressView.addView(progressBar, layoutParams);
                this.rootLayout.addView(this.progressView, layoutParams);
                break;
        }
        int eLocale = 9;
        if (Build.STORE == Build.Store.GFAN || Build.STORE == Build.Store.XIAOMI) {
            eLocale = 4;
        } else {
            Locale grLocal = Locale.getDefault();
            if (grLocal.getLanguage().equals(new String("de"))) {
                eLocale = 7;
            } else if (grLocal.getLanguage().equals(new String("fr"))) {
                eLocale = 12;
            } else if (grLocal.getLanguage().equals(new String("it"))) {
                eLocale = 16;
            } else if (grLocal.getLanguage().equals(new String("pt"))) {
                eLocale = 22;
            } else if (grLocal.getLanguage().equals(new String("es"))) {
                eLocale = 10;
            } else if (grLocal.getLanguage().equals(new String("ru"))) {
                eLocale = 25;
            } else if (grLocal.getLanguage().equals(new String("ko"))) {
                eLocale = 18;
            } else if (grLocal.getLanguage().equals(new String("ja"))) {
                eLocale = 17;
            } else if (grLocal.getLanguage().contains("zh")) {
                eLocale = 4;
            }
        }
        int nScreenType = 0;
        if ((getResources().getConfiguration().screenLayout & 15) == 4) {
            nScreenType = 3;
        } else if ((getResources().getConfiguration().screenLayout & 15) == 3) {
            nScreenType = 2;
        } else if ((getResources().getConfiguration().screenLayout & 15) == 2) {
            nScreenType = 1;
        } else if ((getResources().getConfiguration().screenLayout & 15) == 1) {
            nScreenType = 1;
        }
        onCreateNative(getFilesDir().toString(), Environment.getExternalStorageDirectory().toString(), nScreenType, getAssets(), eLocale, Build.VERSION.SDK_INT);
        hideProgressView();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        onDestroyNative();
        if (this.musicPlayer != null) {
            this.musicPlayer.stop();
            this.musicPlayer.release();
            this.musicPlayer = null;
        }
        if (this.adView != null) {
            this.adView.destroy();
            this.adView = null;
        }
        switch ($SWITCH_TABLE$com$vivamedia$CMTablet$Build$Store()[Build.STORE.ordinal()]) {
            case 4:
                this.googleBillingService.unbind();
                this.googleBillingHandler = null;
                this.googleBillingService = null;
                this.googlePurchaseObserver = null;
                break;
        }
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        if (Build.VERSION == Build.Version.LITE) {
            this.chartboost.onStart(this);
        }
        switch ($SWITCH_TABLE$com$vivamedia$CMTablet$Build$Store()[Build.STORE.ordinal()]) {
            case 2:
                PurchasingManager.registerObserver(new AmazonPurchasingObserver(this));
                Set<String> skus = new HashSet<>();
                for (int pack = 0; pack < 5; pack++) {
                    for (int packnumber = 0; packnumber < 5; packnumber++) {
                        skus.add(String.format("gg_packs_%02d_%02d", Integer.valueOf(pack), Integer.valueOf(packnumber)));
                    }
                }
                skus.add("adfree");
                skus.add("completepackage");
                PurchasingManager.initiateItemDataRequest(skus);
                PurchasingManager.initiatePurchaseUpdatesRequest(Offset.BEGINNING);
                break;
            case 4:
                GoogleResponseHandler.register(this.googlePurchaseObserver);
                break;
        }
        onStartNative();
    }

    @Override // android.app.Activity
    protected void onStop() {
        onStopNative();
        if (Build.VERSION == Build.Version.LITE) {
            this.chartboost.onStop(this);
        }
        if (Build.STORE == Build.Store.GOOGLE) {
            GoogleResponseHandler.unregister(this.googlePurchaseObserver);
        }
        super.onStop();
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.surfaceView.onPause();
        onPauseNative(!this.surfaceView.preservesEGLContext);
        if (this.adView != null) {
            destroyAdView();
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.surfaceView.onResume();
        onResumeNative();
        createAdView();
        if (this.adView != null) {
            this.adView.setVisibility(0);
        }
        if (this.hasFocus && !this.musicResumed) {
            resumeMusic();
        }
    }

    public void createAdView() {
        if (Build.VERSION != Build.Version.FULL && !isAdMobPurchasedNative() && this.adView == null) {
            this.adView = new AdView(this, AdSize.BANNER, "a14f26c81ec1108");
            AdRequest adRequest = new AdRequest();
            this.adView.loadAd(adRequest);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2, 1);
            this.rootLayout.addView(this.adView, layoutParams);
            this.adView.setVisibility(4);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean _hasFocus) {
        super.onWindowFocusChanged(this.hasFocus);
        this.hasFocus = _hasFocus;
        if (this.hasFocus) {
            resumeMusic();
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if ((Build.VERSION != Build.Version.LITE || !this.chartboost.onBackPressed()) && !onBackNative()) {
            int pid = Process.myPid();
            Process.killProcess(pid);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchKeyEvent(KeyEvent ke) {
        if (this.progressView == null || this.progressView.getVisibility() != 0) {
            if (ke.getAction() == 2 && ke.getRepeatCount() == 0) {
                String s = ke.getCharacters();
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    dispatchKeyEventNative(0, ke.getKeyCode(), 1, c, ke.getDownTime(), ke.getEventTime());
                    dispatchKeyEventNative(1, ke.getKeyCode(), 1, c, ke.getDownTime(), ke.getEventTime());
                }
            }
            if (ke.getAction() == 1 && ke.getKeyCode() == 66 && this.isSoftKeyboardOpen) {
                hideSoftKeyboard();
                dispatchKeyEventNative(ke.getAction(), ke.getKeyCode(), ke.getRepeatCount(), 0, ke.getDownTime(), ke.getEventTime());
            } else {
                dispatchKeyEventNative(ke.getAction(), ke.getKeyCode(), ke.getRepeatCount(), ke.getUnicodeChar(), ke.getDownTime(), ke.getEventTime());
            }
        }
        return super.dispatchKeyEvent(ke);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (this.progressView == null || this.progressView.getVisibility() != 0) {
            if (this.isSoftKeyboardOpen) {
                hideSoftKeyboard();
            }
            float[] rgPointerX = new float[me.getPointerCount()];
            float[] rgPointerY = new float[me.getPointerCount()];
            for (int i = 0; i < me.getPointerCount(); i++) {
                rgPointerX[i] = me.getX(i);
                rgPointerY[i] = me.getY(i);
            }
            dispatchTouchEventNative(me.getAction(), me.getPointerCount(), rgPointerX, rgPointerY, me.getDownTime(), me.getEventTime());
        }
        return super.dispatchTouchEvent(me);
    }

    public void gameCircleError(String strMsg) {
        AlertDialog dlgError = new AlertDialog.Builder(this).create();
        dlgError.setCancelable(false);
        dlgError.setTitle("Game Circle Problem");
        dlgError.setMessage(strMsg);
        dlgError.setButton("OK", new DialogInterface.OnClickListener() { // from class: com.vivamedia.CMTablet.MainActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dlgError.show();
        this.gameCircleErrorShown = true;
        this.gameCircleOK = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GoogleLicenseCheckerCallback implements LicenseCheckerCallback {
        private GoogleLicenseCheckerCallback() {
        }

        @Override // com.google.android.vending.licensing.LicenseCheckerCallback
        public void allow(int policyReason) {
            if (MainActivity.this.isFinishing()) {
            }
        }

        @Override // com.google.android.vending.licensing.LicenseCheckerCallback
        public void dontAllow(int policyReason) {
            if (MainActivity.this.isFinishing()) {
                return;
            }
            MainActivity.this.displayLicenseDialog(policyReason == 291 ? 1 : 0);
        }

        @Override // com.google.android.vending.licensing.LicenseCheckerCallback
        public void applicationError(int errorCode) {
            if (MainActivity.this.isFinishing()) {
                return;
            }
            MainActivity.this.displayLicenseDialog(errorCode + 2);
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int reason) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setCancelable(false);
        dlg.setTitle("Licensing");
        if (reason == 0) {
            dlg.setMessage("Sorry, the server was unable to verify that you purchased the app. Please make sure you are logged into Google Play with the account the app was purchased under and try again. Code(" + reason + ")");
        } else if (reason == 1) {
            dlg.setMessage("Sorry, the server could not be reached. Please make sure your Internet connection is active and try again. Code(" + reason + ")");
        } else {
            dlg.setMessage("There is something wrong with your app. Please remove and re-download it. Code(" + (reason - 2) + ")");
        }
        if (reason < 2) {
            dlg.setPositiveButton("Retry", new DialogInterface.OnClickListener() { // from class: com.vivamedia.CMTablet.MainActivity.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.checkLicense();
                }
            });
        }
        if (reason == 0) {
            dlg.setNeutralButton("Purchase", new DialogInterface.OnClickListener() { // from class: com.vivamedia.CMTablet.MainActivity.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://market.android.com/details?id=" + MainActivity.this.getPackageName())));
                    } catch (ActivityNotFoundException e) {
                        MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                    }
                    int pid = Process.myPid();
                    Process.killProcess(pid);
                }
            });
        }
        dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.vivamedia.CMTablet.MainActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                int pid = Process.myPid();
                Process.killProcess(pid);
            }
        });
        return dlg.create();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkLicense() {
        this.googleLicenseChecker.checkAccess(this.googleLicenseCheckerCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayLicenseDialog(final int reason) {
        this.googleLicenseHandler.post(new Runnable() { // from class: com.vivamedia.CMTablet.MainActivity.6
            @Override // java.lang.Runnable
            public void run() {
                MainActivity.this.showDialog(reason);
            }
        });
    }

    @Override // com.xiaomi.gamecenter.sdk.OnLoginProcessListener
    public void finishLoginProcess(int code, MiAccountInfo arg1) {
        switch (code) {
            case 0:
                xiaomiLoggedIn = true;
                triggerXiaomiPurchaseOrder();
                return;
            default:
                onPurchaseDoneNative("fullversion", 1);
                hideProgressView();
                return;
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.OnPayProcessListener
    public void finishPayProcess(int arg0) {
        switch (arg0) {
            case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_REPEAT /* -18005 */:
            case 0:
                onPurchaseDoneNative("fullversion", 0);
                hideProgressView();
                return;
            default:
                onPurchaseDoneNative("fullversion", 1);
                hideProgressView();
                return;
        }
    }

    private void triggerXiaomiPurchaseOrder() {
        MiBuyInfoOffline offlineBuyInfo = new MiBuyInfoOffline();
        offlineBuyInfo.setCpOrderId(UUID.randomUUID().toString());
        offlineBuyInfo.setProductCode("fullversion");
        offlineBuyInfo.setCount(1);
        MiCommplatform.getInstance().miUniPayOffline(this, offlineBuyInfo, this);
    }

    private void showProgressView() {
        runOnUiThread(new Runnable() { // from class: com.vivamedia.CMTablet.MainActivity.7
            @Override // java.lang.Runnable
            public void run() {
                if (MainActivity.this.progressView != null) {
                    MainActivity.this.progressView.setVisibility(0);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideProgressView() {
        runOnUiThread(new Runnable() { // from class: com.vivamedia.CMTablet.MainActivity.8
            @Override // java.lang.Runnable
            public void run() {
                if (MainActivity.this.progressView != null) {
                    MainActivity.this.progressView.setVisibility(8);
                }
            }
        });
    }

    public void showSoftKeyboard() {
        if (!this.isSoftKeyboardOpen) {
            InputMethodManager keyboard = (InputMethodManager) getSystemService("input_method");
            keyboard.toggleSoftInput(0, 0);
            this.isSoftKeyboardOpen = true;
        }
    }

    public void hideSoftKeyboard() {
        if (this.isSoftKeyboardOpen) {
            InputMethodManager keyboard = (InputMethodManager) getSystemService("input_method");
            keyboard.toggleSoftInput(0, 0);
            this.isSoftKeyboardOpen = false;
        }
    }

    public void showAchievements() {
        AmazonGames ag;
        AchievementsClient acClient;
        if (Build.STORE != Build.Store.AMAZON || !this.gameCircleOK || (ag = AmazonGamesClient.getInstance()) == null || (acClient = ag.getAchievementsClient()) == null) {
            return;
        }
        acClient.showAchievementsOverlay(new Object[0]);
    }

    public void triggerAchievement(String sAchievement) {
        AmazonGames ag;
        if (Build.STORE == Build.Store.AMAZON && this.gameCircleOK && (ag = AmazonGamesClient.getInstance()) != null) {
            AchievementsClient acClient = ag.getAchievementsClient();
            acClient.updateProgress(sAchievement, 100.0f, StringUtils.EMPTY).setCallback(new AGResponseCallback<UpdateProgressResponse>() { // from class: com.vivamedia.CMTablet.MainActivity.9
                @Override // com.amazon.ags.api.AGResponseCallback
                public void onComplete(UpdateProgressResponse result) {
                }
            });
        }
    }

    public void destroyAdView() {
        runOnUiThread(new Runnable() { // from class: com.vivamedia.CMTablet.MainActivity.10
            @Override // java.lang.Runnable
            public void run() {
                if (MainActivity.this.adView != null) {
                    if (MainActivity.this.rootLayout != null) {
                        MainActivity.this.rootLayout.removeView(MainActivity.this.adView);
                    }
                    MainActivity.this.adView.removeAllViews();
                    MainActivity.this.adView.destroy();
                    MainActivity.this.adView = null;
                }
            }
        });
    }

    public void showAdMob() {
        runOnUiThread(new Runnable() { // from class: com.vivamedia.CMTablet.MainActivity.11
            @Override // java.lang.Runnable
            public void run() {
                if (MainActivity.this.adView != null && MainActivity.this.adView.getVisibility() == 4) {
                    MainActivity.this.adView.setVisibility(0);
                }
            }
        });
    }

    public void hideAdMob() {
        runOnUiThread(new Runnable() { // from class: com.vivamedia.CMTablet.MainActivity.12
            @Override // java.lang.Runnable
            public void run() {
                if (MainActivity.this.adView != null && MainActivity.this.adView.getVisibility() == 0) {
                    MainActivity.this.adView.setVisibility(4);
                }
            }
        });
    }

    public boolean isAmazonDevice() {
        return android.os.Build.MANUFACTURER.equals("Amazon") && (android.os.Build.MODEL.equals("Kindle Fire") || android.os.Build.MODEL.equals("KFOT") || android.os.Build.MODEL.equals("KFTT") || android.os.Build.MODEL.equals("KFJWI") || android.os.Build.MODEL.equals("KFJWA"));
    }

    public void startBrowser(String sUrl) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sUrl)));
    }

    public void startDLCBuy(String sDLC) {
        switch ($SWITCH_TABLE$com$vivamedia$CMTablet$Build$Store()[Build.STORE.ordinal()]) {
            case 2:
                PurchasingManager.initiatePurchaseRequest(sDLC);
                return;
            case 3:
                Order newOrder = new Order("完整版", "解锁完整版游戏", 99, "order" + System.currentTimeMillis());
                showProgressView();
                GfanPay.getInstance(getApplicationContext()).pay(newOrder, new GfanPayCallback() { // from class: com.vivamedia.CMTablet.MainActivity.13
                    @Override // com.mappn.sdk.pay.GfanPayCallback
                    public void onSuccess(User user, Order order) {
                        MainActivity.onPurchaseDoneNative("fullversion", 0);
                        MainActivity.this.hideProgressView();
                    }

                    @Override // com.mappn.sdk.pay.GfanPayCallback
                    public void onError(User user) {
                        MainActivity.onPurchaseDoneNative("fullversion", 1);
                        MainActivity.this.hideProgressView();
                    }
                });
                return;
            case 4:
                if (this.googleBillingSupported) {
                    if (!this.googleBillingService.requestPurchase(sDLC, null)) {
                        Log.e("MUSIC", "+++++++++ onPurchaseDoneNative 2 +++++++++");
                        onPurchaseDoneNative(sDLC, 2);
                        return;
                    }
                    return;
                }
                Log.e("MUSIC", "+++++++++ onPurchaseDoneNative 1 +++++++++");
                onPurchaseDoneNative(sDLC, 1);
                return;
            case 5:
                showProgressView();
                if (!xiaomiLoggedIn) {
                    MiCommplatform.getInstance().miLogin(this, this);
                    return;
                } else {
                    triggerXiaomiPurchaseOrder();
                    return;
                }
            default:
                return;
        }
    }

    public void updateRequests() {
        while (httpClientHasNewCreatedPostNative()) {
            HttpRequest newRequest = new HttpRequest();
            byte[] arrPost = httpClientGetPOSTParametersLinkedOfNewCreatedPostNative();
            int id = httpClientGetIDOfNewCreatedPostNative();
            if (httpClientNewCreatedPostIsDataNative()) {
                byte[] data = httpClientGetUploadDataOfNewCreatedPostNative();
                int size = httpClientGetUploadDataSizeOfNewCreatedPostNative();
                newRequest.startDataPost(arrPost, data, size, id);
            } else {
                newRequest.startCommandPost(arrPost, id);
            }
            this.httpRequests.add(newRequest);
            httpClientSetNewPostToWaitingStateNative();
        }
        boolean error = false;
        int errorState = -1;
        int errorID = -1;
        int i = this.httpRequests.size() - 1;
        while (true) {
            if (i < 0) {
                break;
            }
            HttpRequest r = this.httpRequests.get(i);
            if (r.isError()) {
                error = true;
                errorState = r.getState();
                errorID = r.getID();
                break;
            } else {
                if (r.isOK()) {
                    int ID = r.getID();
                    int mime = r.getMime();
                    byte[] response = r.getResponse();
                    httpClientSetResponseOfPostNative(ID, response, response.length, mime);
                    this.httpRequests.remove(i);
                }
                i--;
            }
        }
        if (error) {
            httpClientReceiveErrorNative(errorState, errorID);
            for (int i2 = 0; i2 < this.httpRequests.size(); i2++) {
                this.httpRequests.get(i2).stop();
            }
            this.httpRequests.clear();
        }
    }

    public void getLeaderboardRank(String sLeaderboard) {
        AmazonGames ag;
        if (Build.STORE == Build.Store.AMAZON && this.gameCircleOK && (ag = AmazonGamesClient.getInstance()) != null) {
            LeaderboardsClient lbClient = ag.getLeaderboardsClient();
            lbClient.getLocalPlayerScore(sLeaderboard, LeaderboardFilter.GLOBAL_ALL_TIME, StringUtils.EMPTY).setCallback(new AGResponseCallback<GetPlayerScoreResponse>() { // from class: com.vivamedia.CMTablet.MainActivity.14
                @Override // com.amazon.ags.api.AGResponseCallback
                public void onComplete(GetPlayerScoreResponse result) {
                }
            });
        }
    }

    public void showLeaderboards() {
        AmazonGames ag;
        LeaderboardsClient lbClient;
        if (Build.STORE != Build.Store.AMAZON || !this.gameCircleOK || (ag = AmazonGamesClient.getInstance()) == null || (lbClient = ag.getLeaderboardsClient()) == null) {
            return;
        }
        lbClient.showLeaderboardsOverlay(new Object[0]);
    }

    public void showLeaderboard(String sLeaderboard) {
        AmazonGames ag;
        LeaderboardsClient lbClient;
        if (Build.STORE != Build.Store.AMAZON || !this.gameCircleOK || (ag = AmazonGamesClient.getInstance()) == null || (lbClient = ag.getLeaderboardsClient()) == null) {
            return;
        }
        lbClient.showLeaderboardOverlay(sLeaderboard, StringUtils.EMPTY);
    }

    public void uploadLeaderboardValue(String sLeaderboard, int nScore) {
        AmazonGames ag;
        if (Build.STORE == Build.Store.AMAZON && this.gameCircleOK && (ag = AmazonGamesClient.getInstance()) != null) {
            if (sLeaderboard == "GeniusExperience") {
                PopUpPrefs.INSTANCE.disable();
            }
            LeaderboardsClient lbClient = ag.getLeaderboardsClient();
            lbClient.submitScore(sLeaderboard, nScore, StringUtils.EMPTY).setCallback(new AGResponseCallback<SubmitScoreResponse>() { // from class: com.vivamedia.CMTablet.MainActivity.15
                @Override // com.amazon.ags.api.AGResponseCallback
                public void onComplete(SubmitScoreResponse result) {
                }
            });
            PopUpPrefs.INSTANCE.enable();
        }
    }

    public void startMusic(String filename) {
        try {
            if (this.musicPlayer != null) {
                this.musicPlayer.release();
                this.musicPlayer = null;
            }
            AssetFileDescriptor afd = getAssets().openFd(filename);
            this.musicPlayer = new MediaPlayer();
            this.musicPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            this.musicPlayer.prepare();
            this.musicPlayer.start();
            this.musicResumed = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalStateException e3) {
            e3.printStackTrace();
        }
    }

    public void stopMusic() {
        if (this.musicPlayer != null && this.musicPlayer.isPlaying()) {
            this.musicPlayer.stop();
            this.musicPlayer.reset();
            this.musicResumed = false;
        }
    }

    public void pauseMusic() {
        if (this.musicPlayer != null && this.musicPlayer.isPlaying()) {
            this.musicPlayer.pause();
            this.musicResumed = false;
        }
    }

    public void resumeMusic() {
        if (!isApplicationBroughtToBackground() && this.musicPlayer != null && !this.musicPlayer.isPlaying()) {
            this.musicPlayer.start();
            this.musicResumed = true;
        }
    }

    public boolean isMusicPlaying() {
        if (this.musicPlayer != null) {
            return this.musicPlayer.isPlaying();
        }
        return false;
    }

    public void setMusicVolume(float fVolume) {
        if (this.musicPlayer != null) {
            this.musicPlayer.setVolume(fVolume, fVolume);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService("connectivity");
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) getSystemService("activity");
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
