package com.chartboost.sdk.View;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.chartboost.sdk.Model.CBImpression;
import com.chartboost.sdk.View.CBAnimationManager;

/* loaded from: classes.dex */
public class CBDefaultViewController {
    private static CBDefaultViewController sharedController = null;
    private CBPopupImpressionView impressionPopup;
    private CBLoadingView loadingView;
    public boolean loadingViewIsVisible;
    private CBPopupImpressionView loadingViewPopup;
    private boolean viewIsVisible;

    public static synchronized CBDefaultViewController sharedController() {
        CBDefaultViewController cBDefaultViewController;
        synchronized (CBDefaultViewController.class) {
            if (sharedController == null) {
                sharedController = new CBDefaultViewController();
            }
            cBDefaultViewController = sharedController;
        }
        return cBDefaultViewController;
    }

    private CBDefaultViewController() {
    }

    public void displayInterstitial(CBImpression interstitialImpression, Context context) {
        displayImpression(interstitialImpression, context);
    }

    public void displayMoreApps(CBImpression moreAppsImpression, Context context) {
        displayImpression(moreAppsImpression, context);
    }

    private void displayImpression(CBImpression impression, Context context) {
        if (!this.viewIsVisible) {
            impression.state = CBImpression.CBImpressionState.CBImpressionStateWaitingForDisplay;
            if (!impression.view.createView()) {
                if (impression.view.failCallback != null) {
                    impression.view.failCallback.execute();
                    return;
                }
                return;
            }
            orientView(impression.view.getView());
            Activity activity = (Activity) context;
            if (impression.overrideAnimation) {
                impression.overrideAnimation = false;
                this.impressionPopup = new CBPopupImpressionView(context, impression.view.getView());
                activity.addContentView(this.impressionPopup, new FrameLayout.LayoutParams(-1, -1));
                impression.state = CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController;
                impression.parentView = this.impressionPopup;
                this.viewIsVisible = true;
                return;
            }
            this.impressionPopup = new CBPopupImpressionView(context, impression.view.getView());
            activity.addContentView(this.impressionPopup, new FrameLayout.LayoutParams(-1, -1));
            this.impressionPopup.getBackgroundView().fadeIn();
            CBAnimationManager.CBAnimationType animation = CBAnimationManager.CBAnimationType.CBAnimationTypePerspectiveRotate;
            if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeMoreApps) {
                animation = CBAnimationManager.CBAnimationType.CBAnimationTypePerspectiveZoom;
            }
            if (impression.responseContext.optInt("animation") != 0) {
                animation = CBAnimationManager.CBAnimationType.valuesCustom()[impression.responseContext.optInt("animation")];
            }
            impression.state = CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController;
            impression.parentView = this.impressionPopup;
            CBAnimationManager.transitionInWithAnimationType(animation, impression);
            this.viewIsVisible = true;
        }
    }

    public void dismissInterstitial(CBImpression interstitialImpression, Context context) {
        dismissImpression(interstitialImpression, context);
    }

    public void dismissMoreApps(CBImpression moreAppsImpression, Context context) {
        dismissImpression(moreAppsImpression, context);
    }

    public void dismissImpression(CBImpression impression, Context context) {
        this.viewIsVisible = false;
        impression.state = CBImpression.CBImpressionState.CBImpressionStateWaitingForDismissal;
        CBAnimationManager.CBAnimationType animation = CBAnimationManager.CBAnimationType.CBAnimationTypePerspectiveRotate;
        if (impression.type == CBImpression.CBImpressionType.CBImpressionTypeMoreApps) {
            animation = CBAnimationManager.CBAnimationType.CBAnimationTypePerspectiveZoom;
        }
        if (impression.responseContext.optInt("animation") != 0) {
            animation = CBAnimationManager.CBAnimationType.valuesCustom()[impression.responseContext.optInt("animation")];
        }
        CBAnimationManager.CBAnimationProtocol block = getBlockForImpression(impression, context);
        CBAnimationManager.transitionOutWithAnimationType(animation, impression, block);
    }

    public void dismissImpressionSilently(CBImpression impression, Context context) {
        this.viewIsVisible = false;
        CBAnimationManager.CBAnimationProtocol block = getBlockForImpression(impression, context);
        block.execute(impression);
        if (impression.state == CBImpression.CBImpressionState.CBImpressionStateDisplayedByDefaultController) {
            impression.state = CBImpression.CBImpressionState.CBImpressionStateWaitingForDisplay;
        } else {
            impression.state = CBImpression.CBImpressionState.CBImpressionStateOther;
        }
        impression.cleanUpViews();
        try {
            ViewGroup vg = (ViewGroup) this.impressionPopup.getParent();
            vg.removeView(this.impressionPopup);
        } catch (Exception e) {
        }
    }

    public void displayLoadingView(Context context) {
        this.loadingView = new CBLoadingView(context);
        orientView(this.loadingView);
        Activity activity = (Activity) context;
        this.loadingViewPopup = new CBPopupImpressionView(context, this.loadingView);
        this.loadingViewPopup.getBackgroundView().setGradientReversed(true);
        activity.addContentView(this.loadingViewPopup, new FrameLayout.LayoutParams(-1, -1));
        this.loadingViewPopup.getBackgroundView().fadeIn();
        this.loadingViewPopup.getBackgroundView().fadeIn((View) this.loadingView.getParent());
        this.loadingViewIsVisible = true;
    }

    public void dismissLoadingView(Context context) {
        if (this.loadingViewIsVisible) {
            try {
                ViewGroup vg = (ViewGroup) this.loadingViewPopup.getParent();
                vg.removeView(this.loadingViewPopup);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.loadingView = null;
            this.loadingViewPopup = null;
            this.loadingViewIsVisible = false;
        }
    }

    public void orientView(View view) {
    }

    public void removeImpression(CBImpression impression, Context context) {
        if (this.impressionPopup != null) {
            try {
                ViewGroup vg = (ViewGroup) this.impressionPopup.getParent();
                vg.removeView(this.impressionPopup);
            } catch (Exception e) {
                e.printStackTrace();
            }
            impression.destroy();
            this.impressionPopup = null;
        }
    }

    private CBAnimationManager.CBAnimationProtocol getBlockForImpression(CBImpression impression, final Context context) {
        CBAnimationManager.CBAnimationProtocol block = new CBAnimationManager.CBAnimationProtocol() { // from class: com.chartboost.sdk.View.CBDefaultViewController.1
            @Override // com.chartboost.sdk.View.CBAnimationManager.CBAnimationProtocol
            public void execute(final CBImpression impression2) {
                Handler handler = new Handler();
                final Context context2 = context;
                handler.post(new Runnable() { // from class: com.chartboost.sdk.View.CBDefaultViewController.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (impression2.state == CBImpression.CBImpressionState.CBImpressionStateWaitingForDismissal) {
                            impression2.state = CBImpression.CBImpressionState.CBImpressionStateOther;
                            CBDefaultViewController.this.removeImpression(impression2, context2);
                        }
                    }
                });
            }
        };
        return block;
    }
}
