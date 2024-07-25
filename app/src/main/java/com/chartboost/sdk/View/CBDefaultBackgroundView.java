package com.chartboost.sdk.View;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

/* loaded from: classes.dex */
public class CBDefaultBackgroundView extends RelativeLayout {
    public BackgroundView backgroundView;
    private boolean gradientReversed;

    public CBDefaultBackgroundView(Context context) {
        super(context);
        this.backgroundView = new BackgroundView(context);
        this.backgroundView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        addView(this.backgroundView);
        setFocusable(false);
    }

    public void fadeIn(View fadeView) {
        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(510L);
        fadeIn.setFillAfter(true);
        fadeView.startAnimation(fadeIn);
    }

    public void fadeIn() {
        fadeIn(this.backgroundView);
    }

    public boolean getGradientReversed() {
        return this.gradientReversed;
    }

    public void setGradientReversed(boolean _gradientReversed) {
        this.gradientReversed = _gradientReversed;
        this.backgroundView.prepareBackground();
    }

    /* loaded from: classes.dex */
    private class BackgroundView extends View {
        public BackgroundView(Context context) {
            super(context);
            setFocusable(false);
        }

        @Override // android.view.View
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            prepareBackground();
        }

        public void prepareBackground() {
            int edgeColor = CBDefaultBackgroundView.this.gradientReversed ? -2013265920 : -872415232;
            int centerColor = CBDefaultBackgroundView.this.gradientReversed ? -872415232 : -2013265920;
            GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{centerColor, edgeColor});
            g.setGradientType(1);
            float radius = Math.min(getWidth(), getHeight());
            g.setGradientRadius(radius / 2.0f);
            g.setGradientCenter(getWidth() / 2.0f, getHeight() / 2.0f);
            setBackgroundDrawable(g);
        }
    }
}
