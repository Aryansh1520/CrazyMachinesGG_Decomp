package com.chartboost.sdk.Model;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.NativeViews.CBNativeInterstitialViewProtocol;
import com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol;
import com.chartboost.sdk.View.CBPopupImpressionView;
import com.chartboost.sdk.View.CBViewProtocol;
import com.chartboost.sdk.View.CBWebViewProtocol;
import com.mappn.sdk.uc.util.Constants;
import com.mokredit.payment.StringUtils;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CBImpression {
    public CBImpressionProtocol delegate;
    public String location;
    public boolean overrideAnimation;
    public boolean overrideAskToShow;
    public boolean overrideLoadingViewRequirement;
    public CBPopupImpressionView parentView;
    public JSONObject responseContext;
    public Date responseDate;
    public CBImpressionState state;
    public CBImpressionType type;
    public CBViewProtocol view;

    /* loaded from: classes.dex */
    public interface CBImpressionProtocol {
        void impressionClickTriggered(CBImpression cBImpression, String str, JSONObject jSONObject);

        void impressionCloseTriggered(CBImpression cBImpression);

        void impressionFailedToInitialize(CBImpression cBImpression);

        void impressionReadyToBeDisplayed(CBImpression cBImpression);
    }

    /* loaded from: classes.dex */
    public enum CBImpressionState {
        CBImpressionStateOther,
        CBImpressionStateWaitingForDisplay,
        CBImpressionStateDisplayedByDefaultController,
        CBImpressionStateWaitingForDismissal,
        CBImpressionStateWaitingForCaching,
        CBImpressionStateCached;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static CBImpressionState[] valuesCustom() {
            CBImpressionState[] valuesCustom = values();
            int length = valuesCustom.length;
            CBImpressionState[] cBImpressionStateArr = new CBImpressionState[length];
            System.arraycopy(valuesCustom, 0, cBImpressionStateArr, 0, length);
            return cBImpressionStateArr;
        }
    }

    /* loaded from: classes.dex */
    public enum CBImpressionType {
        CBImpressionTypeOther,
        CBImpressionTypeInterstitial,
        CBImpressionTypeMoreApps;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static CBImpressionType[] valuesCustom() {
            CBImpressionType[] valuesCustom = values();
            int length = valuesCustom.length;
            CBImpressionType[] cBImpressionTypeArr = new CBImpressionType[length];
            System.arraycopy(valuesCustom, 0, cBImpressionTypeArr, 0, length);
            return cBImpressionTypeArr;
        }
    }

    public CBImpression(JSONObject response, CBImpressionType impressionType, CBImpressionProtocol impressionDelegate, CBImpressionState initialState, String initialLocation) {
        response = response == null ? new JSONObject() : response;
        this.state = initialState;
        this.location = initialLocation;
        this.responseContext = response;
        this.responseDate = new Date();
        this.delegate = impressionDelegate;
        this.type = impressionType;
        this.overrideAnimation = false;
        this.overrideLoadingViewRequirement = false;
        this.overrideAskToShow = false;
        boolean useNative = response.optString(Constants.PUSH_TYPE, StringUtils.EMPTY).equals("native");
        if (useNative && this.type == CBImpressionType.CBImpressionTypeInterstitial) {
            CBNativeInterstitialViewProtocol view = new CBNativeInterstitialViewProtocol(this);
            this.view = view;
        } else if (useNative && this.type == CBImpressionType.CBImpressionTypeMoreApps) {
            CBNativeMoreAppsViewProtocol view2 = new CBNativeMoreAppsViewProtocol(this);
            this.view = view2;
        } else {
            CBWebViewProtocol cbWebView = new CBWebViewProtocol(this);
            this.view = cbWebView;
        }
        this.view.displayCallback = new CBViewProtocol.BlockInterface() { // from class: com.chartboost.sdk.Model.CBImpression.1
            @Override // com.chartboost.sdk.View.CBViewProtocol.BlockInterface
            public void execute() {
                if (thisImpression.delegate != null) {
                    thisImpression.delegate.impressionReadyToBeDisplayed(thisImpression);
                }
            }
        };
        this.view.closeCallback = new CBViewProtocol.BlockInterface() { // from class: com.chartboost.sdk.Model.CBImpression.2
            @Override // com.chartboost.sdk.View.CBViewProtocol.BlockInterface
            public void execute() {
                if (thisImpression.delegate != null) {
                    thisImpression.delegate.impressionCloseTriggered(thisImpression);
                }
            }
        };
        this.view.clickCallback = new CBViewProtocol.ClickBlockInterface() { // from class: com.chartboost.sdk.Model.CBImpression.3
            @Override // com.chartboost.sdk.View.CBViewProtocol.ClickBlockInterface
            public void execute(String url, JSONObject moreData) {
                if (url == null) {
                    url = thisImpression.responseContext.optString("link");
                }
                String deepLink = thisImpression.responseContext.optString("deep-link");
                if (deepLink != null && !deepLink.equals(StringUtils.EMPTY)) {
                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(deepLink));
                        PackageManager packageManager = Chartboost.sharedChartboost().getContext().getPackageManager();
                        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                        if (list.size() > 0) {
                            url = deepLink;
                        }
                    } catch (Exception e) {
                    }
                }
                thisImpression.delegate.impressionClickTriggered(thisImpression, url, moreData);
            }
        };
        this.view.failCallback = new CBViewProtocol.BlockInterface() { // from class: com.chartboost.sdk.Model.CBImpression.4
            @Override // com.chartboost.sdk.View.CBViewProtocol.BlockInterface
            public void execute() {
                thisImpression.delegate.impressionFailedToInitialize(thisImpression);
            }
        };
        this.view.prepareWithResponse(response);
    }

    public boolean reinitialize() {
        this.overrideAnimation = true;
        this.overrideLoadingViewRequirement = true;
        this.overrideAskToShow = true;
        this.view.setReadyToDisplay();
        if (this.view.getView() != null) {
            return true;
        }
        this.overrideAnimation = false;
        this.overrideLoadingViewRequirement = false;
        this.overrideAskToShow = false;
        return false;
    }

    public void destroy() {
        this.parentView.destroy();
        this.view.destroy();
        this.responseContext = null;
        this.responseDate = null;
        this.delegate = null;
        this.view = null;
        this.parentView = null;
    }

    public void cleanUpViews() {
        this.parentView.destroy();
        try {
            this.parentView.removeView(this.view.getView());
        } catch (Exception e) {
        }
        this.view.destroyView();
    }
}
