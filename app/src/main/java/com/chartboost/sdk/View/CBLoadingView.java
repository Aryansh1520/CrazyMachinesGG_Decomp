package com.chartboost.sdk.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;
import com.chartboost.sdk.View.CBPopupImpressionView;

/* loaded from: classes.dex */
public class CBLoadingView extends LinearLayout implements CBPopupImpressionView.CBViewUpdater {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference;
    private TextView label;
    private CBRotatableContainer labelWrapper;
    private CBUnderfinedProgressBar progressBar;

    static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference() {
        int[] iArr = $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference;
        if (iArr == null) {
            iArr = new int[CBConstants.CBOrientation.Difference.valuesCustom().length];
            try {
                iArr[CBConstants.CBOrientation.Difference.ANGLE_0.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CBConstants.CBOrientation.Difference.ANGLE_180.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[CBConstants.CBOrientation.Difference.ANGLE_270.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[CBConstants.CBOrientation.Difference.ANGLE_90.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference = iArr;
        }
        return iArr;
    }

    public CBLoadingView(Context context) {
        super(context);
        init(context);
    }

    public CBLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setGravity(17);
        this.label = new TextView(getContext());
        this.label.setTextColor(-1);
        this.label.setTextSize(2, 16.0f);
        this.label.setTypeface(null, 1);
        this.label.setText("Loading...");
        this.label.setGravity(17);
        this.labelWrapper = new CBRotatableContainer(context, this.label);
        this.progressBar = new CBUnderfinedProgressBar(getContext());
        addView(this.labelWrapper);
        addView(this.progressBar);
        onViewUpdateRequired();
    }

    @Override // com.chartboost.sdk.View.CBPopupImpressionView.CBViewUpdater
    public void onViewUpdateRequired() {
        removeView(this.labelWrapper);
        removeView(this.progressBar);
        float density = getContext().getResources().getDisplayMetrics().density;
        int m = Math.round(20.0f * density);
        CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
        switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[dif.ordinal()]) {
            case 2:
                setOrientation(0);
                LinearLayout.LayoutParams lpBar = new LinearLayout.LayoutParams(Math.round(32.0f * density), -1);
                lpBar.setMargins(m, m, 0, m);
                addView(this.progressBar, lpBar);
                LinearLayout.LayoutParams lpLabel = new LinearLayout.LayoutParams(-2, -1);
                lpLabel.setMargins(m, m, m, m);
                addView(this.labelWrapper, lpLabel);
                return;
            case 3:
                setOrientation(1);
                LinearLayout.LayoutParams lpBar2 = new LinearLayout.LayoutParams(-1, Math.round(32.0f * density));
                lpBar2.setMargins(m, m, m, 0);
                addView(this.progressBar, lpBar2);
                LinearLayout.LayoutParams lpLabel2 = new LinearLayout.LayoutParams(-1, -2);
                lpLabel2.setMargins(m, m, m, m);
                addView(this.labelWrapper, lpLabel2);
                return;
            case 4:
                setOrientation(0);
                LinearLayout.LayoutParams lpLabel3 = new LinearLayout.LayoutParams(-2, -1);
                lpLabel3.setMargins(m, m, 0, m);
                addView(this.labelWrapper, lpLabel3);
                LinearLayout.LayoutParams lpBar3 = new LinearLayout.LayoutParams(Math.round(32.0f * density), -1);
                lpBar3.setMargins(m, m, m, m);
                addView(this.progressBar, lpBar3);
                return;
            default:
                setOrientation(1);
                LinearLayout.LayoutParams lpLabel4 = new LinearLayout.LayoutParams(-1, -2);
                lpLabel4.setMargins(m, m, m, 0);
                addView(this.labelWrapper, lpLabel4);
                LinearLayout.LayoutParams lpBar4 = new LinearLayout.LayoutParams(-1, Math.round(32.0f * density));
                lpBar4.setMargins(m, m, m, m);
                addView(this.progressBar, lpBar4);
                return;
        }
    }
}
