package com.amazon.ags.overlay;

import android.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import com.amazon.ags.api.overlay.PopUpLocation;

/* loaded from: classes.dex */
public class AGSToast extends RelativeLayout {
    private static final String FEATURE_NAME = "AGS_OL";
    private final Context context;
    private final int fadeInDuration;
    private final int fadeOutDuration;
    private Animation hideAnimation;
    private boolean isShowing;
    private final PopUpContent popUpContent;
    private final PopUpLocation popUpLocation;
    private Animation showAnimation;
    private WindowManager windowManager;
    private static final String TAG = "AGS_OL_" + AGSToast.class.getSimpleName();
    private static int BACKGROUND_COLOR = 0;
    private static float VERTICAL_MARGIN_TOP_PORTRAIT = 0.02f;
    private static float VERTICAL_MARGIN_BOTTOM_PORTRAIT = 0.04f;
    private static float VERTICAL_MARGIN_TOP_LANDSCAPE = 0.02f;
    private static float VERTICAL_MARGIN_BOTTOM_LANDSCAPE = 0.04f;

    public AGSToast(Context context, PopUpContent popUpContent, PopUpLocation popUpLocation, int fadeInDuration, int fadeOutDuration) {
        super(context);
        this.isShowing = false;
        this.context = context;
        this.popUpContent = popUpContent;
        this.fadeInDuration = fadeInDuration;
        this.fadeOutDuration = fadeOutDuration;
        this.popUpLocation = popUpLocation;
        setupGameCircleToast();
    }

    private void setupGameCircleToast() {
        this.windowManager = (WindowManager) getContext().getSystemService("window");
        setupRemoteView(this.popUpContent.getRemoteViews());
        setupWindow();
        setupDefaultAnimations();
        setupShowAnimationCallBack();
        setupHideAnimationCallBack();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = 288;
        params.height = -2;
        params.width = -2;
        params.format = 1;
        params.gravity = getGravity(this.popUpLocation);
        params.verticalMargin = getVerticalMargin(this.popUpLocation);
        this.windowManager.updateViewLayout(this, params);
    }

    public void destroy() {
        finishHide();
    }

    private void setupDefaultAnimations() {
        this.showAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        this.showAnimation.setDuration(this.fadeInDuration);
        this.hideAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        this.hideAnimation.setDuration(this.fadeOutDuration);
    }

    private void setupWindow() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.flags = 288;
        params.height = -2;
        params.width = -2;
        params.format = 1;
        params.gravity = getGravity(this.popUpLocation);
        params.verticalMargin = getVerticalMargin(this.popUpLocation);
        params.type = 2005;
        this.windowManager.addView(this, params);
    }

    private float getVerticalMargin(PopUpLocation location) {
        int orientation = getResources().getConfiguration().orientation;
        switch (location) {
            case TOP_LEFT:
            case TOP_RIGHT:
            case TOP_CENTER:
                return orientation == 1 ? VERTICAL_MARGIN_TOP_PORTRAIT : VERTICAL_MARGIN_TOP_LANDSCAPE;
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
            case BOTTOM_CENTER:
                return orientation == 1 ? VERTICAL_MARGIN_BOTTOM_PORTRAIT : VERTICAL_MARGIN_BOTTOM_LANDSCAPE;
            default:
                return orientation == 1 ? VERTICAL_MARGIN_TOP_PORTRAIT : VERTICAL_MARGIN_TOP_LANDSCAPE;
        }
    }

    private int getGravity(PopUpLocation location) {
        switch (location) {
            case TOP_LEFT:
            default:
                return 51;
            case TOP_RIGHT:
                return 53;
            case TOP_CENTER:
                return 49;
            case BOTTOM_LEFT:
                return 83;
            case BOTTOM_RIGHT:
                return 85;
            case BOTTOM_CENTER:
                return 81;
        }
    }

    private void setupRemoteView(RemoteViews remoteViews) {
        Log.d(TAG, "Entering setupRemoteView...");
        setBackgroundColor(BACKGROUND_COLOR);
        View viewContent = remoteViews.apply(this.context, this);
        addView(viewContent);
        setVisibility(8);
    }

    private void setupHideAnimationCallBack() {
        this.hideAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.amazon.ags.overlay.AGSToast.1
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                AGSToast.this.finishHide();
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    private void setupShowAnimationCallBack() {
        this.showAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.amazon.ags.overlay.AGSToast.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation) {
                AGSToast.this.hide();
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    public void show() {
        if (!this.isShowing) {
            View v = getChildAt(0);
            if (v == null) {
                Log.w(TAG, "pop-up child view is null");
                return;
            }
            v.startAnimation(this.showAnimation);
            setVisibility(0);
            this.isShowing = true;
        }
    }

    public void hide() {
        if (this.isShowing) {
            View v = getChildAt(0);
            if (v == null) {
                Log.w(TAG, "pop-up child view is null");
            } else {
                v.startAnimation(this.hideAnimation);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishHide() {
        setVisibility(8);
        this.isShowing = false;
        this.windowManager.removeView(this);
    }
}
