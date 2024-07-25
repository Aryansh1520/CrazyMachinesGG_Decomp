package com.chartboost.sdk.View;

import android.content.Context;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.RelativeLayout;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;

/* loaded from: classes.dex */
public class CBPopupImpressionView extends RelativeLayout {
    private CBDefaultBackgroundView backgroundView;
    private View content;
    private CBConstants.CBOrientation.Difference lastOrientationDiff;
    protected OrientationEventListener orientationListener;

    /* loaded from: classes.dex */
    public interface CBViewUpdater {
        void onViewUpdateRequired();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CBPopupImpressionView(Context context, CBViewUpdater cBViewUpdater) {
        super(context);
        this.lastOrientationDiff = null;
        this.content = (View) cBViewUpdater;
        this.backgroundView = new CBDefaultBackgroundView(context);
        addView(this.backgroundView, new RelativeLayout.LayoutParams(-1, -1));
        addView(this.content, new RelativeLayout.LayoutParams(-1, -1));
        this.lastOrientationDiff = Chartboost.sharedChartboost().getForcedOrientationDifference();
        this.orientationListener = new OrientationEventListener(context, 3) { // from class: com.chartboost.sdk.View.CBPopupImpressionView.1
            @Override // android.view.OrientationEventListener
            public void onOrientationChanged(int orientation) {
                CBConstants.CBOrientation.Difference orientationDiff = Chartboost.sharedChartboost().getForcedOrientationDifference();
                if (CBPopupImpressionView.this.lastOrientationDiff != orientationDiff) {
                    CBPopupImpressionView.this.lastOrientationDiff = orientationDiff;
                    ((CBViewUpdater) CBPopupImpressionView.this.content).onViewUpdateRequired();
                    CBPopupImpressionView.this.invalidate();
                }
            }
        };
        this.orientationListener.enable();
    }

    public void destroy() {
        if (this.orientationListener != null) {
            this.orientationListener.disable();
        }
        this.orientationListener = null;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    public CBDefaultBackgroundView getBackgroundView() {
        return this.backgroundView;
    }

    public View getAnimatedView() {
        return this.content;
    }
}
