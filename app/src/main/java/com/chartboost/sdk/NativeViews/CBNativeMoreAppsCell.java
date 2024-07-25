package com.chartboost.sdk.NativeViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import android.widget.LinearLayout;
import com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol;

/* loaded from: classes.dex */
public abstract class CBNativeMoreAppsCell extends LinearLayout implements CBNativeMoreAppsViewProtocol.MoreAppsCellInterface {
    private static final int COLOR_BORDER_BOTTOM = -3355444;
    private static final int COLOR_BORDER_TOP = -723724;
    private static final int COLOR_GRADIENT_BOTTOM = -2302756;
    private static final int COLOR_GRADIENT_TOP = -1447447;
    private static final float DELTA = 0.1f;
    private Paint borderPaint;
    protected View.OnClickListener clickListener;
    private Paint gradientPaint;
    private RectF lastGradientRect;
    private RectF rect;

    protected abstract void layoutSubviews();

    public CBNativeMoreAppsCell(Context context) {
        super(context);
        this.rect = new RectF();
        this.borderPaint = null;
        this.gradientPaint = null;
        this.lastGradientRect = null;
        this.clickListener = null;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onDraw(Canvas canvas) {
        this.rect.set(0.0f, 0.0f, getWidth(), height());
        drawGradient(canvas, this.rect);
        drawBorders(canvas, this.rect);
    }

    private void drawGradient(Canvas canvas, RectF bounds) {
        if (this.gradientPaint == null) {
            this.gradientPaint = new Paint();
            this.gradientPaint.setStyle(Paint.Style.FILL);
            this.gradientPaint.setAntiAlias(true);
        }
        if (this.lastGradientRect == null || Math.abs(bounds.top - this.lastGradientRect.top) > DELTA || Math.abs(bounds.bottom - this.lastGradientRect.bottom) > DELTA) {
            this.gradientPaint.setShader(new LinearGradient(0.0f, bounds.top, 0.0f, bounds.bottom, COLOR_GRADIENT_TOP, COLOR_GRADIENT_BOTTOM, Shader.TileMode.CLAMP));
        }
        canvas.drawRect(bounds, this.gradientPaint);
    }

    private void drawBorders(Canvas canvas, RectF bounds) {
        if (this.borderPaint == null) {
            this.borderPaint = new Paint();
            this.borderPaint.setStyle(Paint.Style.FILL);
            this.borderPaint.setAntiAlias(true);
        }
        this.borderPaint.setColor(COLOR_BORDER_TOP);
        canvas.drawRect(bounds.left, bounds.top, bounds.right, bounds.top + 1.0f, this.borderPaint);
        this.borderPaint.setColor(COLOR_BORDER_BOTTOM);
        canvas.drawRect(bounds.left, bounds.bottom - 1.0f, bounds.right, bounds.bottom, this.borderPaint);
    }
}
