package com.chartboost.sdk.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class CBUnderfinedProgressBar extends View {
    private static final int COLOR_MAIN = -1;
    private static final float kCBProgressBarOutlineWidth = 3.0f;
    private static final float kCBProgressBarRefreshInterval = 0.016666668f;
    private static final float kCBProgressBarRefreshStep = 1.0f;
    private Path elementShape;
    private Paint fillPaint;
    private Handler handler;
    private long lastFrame;
    private Path maskPath;
    private RectF maskRect;
    private float offset;
    private RectF outlineRect;
    private Runnable step;
    private Paint strokePaint;

    public CBUnderfinedProgressBar(Context context) {
        super(context);
        this.step = new Runnable() { // from class: com.chartboost.sdk.View.CBUnderfinedProgressBar.1
            @Override // java.lang.Runnable
            public void run() {
                CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
                float density = CBUnderfinedProgressBar.this.getContext().getResources().getDisplayMetrics().density;
                CBUnderfinedProgressBar.this.offset += CBUnderfinedProgressBar.kCBProgressBarRefreshStep * density;
                float elementSize = (dif.isOdd() ? CBUnderfinedProgressBar.this.getWidth() : CBUnderfinedProgressBar.this.getHeight()) - (9.0f * density);
                if (CBUnderfinedProgressBar.this.offset > elementSize) {
                    CBUnderfinedProgressBar.this.offset -= 2.0f * elementSize;
                }
                if (CBUnderfinedProgressBar.this.getWindowVisibility() == 0) {
                    CBUnderfinedProgressBar.this.invalidate();
                }
            }
        };
        init(context);
    }

    public CBUnderfinedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.step = new Runnable() { // from class: com.chartboost.sdk.View.CBUnderfinedProgressBar.1
            @Override // java.lang.Runnable
            public void run() {
                CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
                float density = CBUnderfinedProgressBar.this.getContext().getResources().getDisplayMetrics().density;
                CBUnderfinedProgressBar.this.offset += CBUnderfinedProgressBar.kCBProgressBarRefreshStep * density;
                float elementSize = (dif.isOdd() ? CBUnderfinedProgressBar.this.getWidth() : CBUnderfinedProgressBar.this.getHeight()) - (9.0f * density);
                if (CBUnderfinedProgressBar.this.offset > elementSize) {
                    CBUnderfinedProgressBar.this.offset -= 2.0f * elementSize;
                }
                if (CBUnderfinedProgressBar.this.getWindowVisibility() == 0) {
                    CBUnderfinedProgressBar.this.invalidate();
                }
            }
        };
        init(context);
    }

    public CBUnderfinedProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.step = new Runnable() { // from class: com.chartboost.sdk.View.CBUnderfinedProgressBar.1
            @Override // java.lang.Runnable
            public void run() {
                CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
                float density = CBUnderfinedProgressBar.this.getContext().getResources().getDisplayMetrics().density;
                CBUnderfinedProgressBar.this.offset += CBUnderfinedProgressBar.kCBProgressBarRefreshStep * density;
                float elementSize = (dif.isOdd() ? CBUnderfinedProgressBar.this.getWidth() : CBUnderfinedProgressBar.this.getHeight()) - (9.0f * density);
                if (CBUnderfinedProgressBar.this.offset > elementSize) {
                    CBUnderfinedProgressBar.this.offset -= 2.0f * elementSize;
                }
                if (CBUnderfinedProgressBar.this.getWindowVisibility() == 0) {
                    CBUnderfinedProgressBar.this.invalidate();
                }
            }
        };
        init(context);
    }

    private void init(Context cx) {
        float density = cx.getResources().getDisplayMetrics().density;
        this.offset = 0.0f;
        this.handler = new Handler();
        this.lastFrame = (long) (System.nanoTime() / 1000000.0d);
        this.strokePaint = new Paint();
        this.strokePaint.setColor(-1);
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setStrokeWidth(kCBProgressBarOutlineWidth * density);
        this.strokePaint.setAntiAlias(true);
        this.fillPaint = new Paint();
        this.fillPaint.setColor(-1);
        this.fillPaint.setStyle(Paint.Style.FILL);
        this.fillPaint.setAntiAlias(true);
        this.maskPath = new Path();
        this.elementShape = new Path();
        this.maskRect = new RectF();
        this.outlineRect = new RectF();
        try {
            Method setLayerTypeMethod = getClass().getMethod("setLayerType", Integer.TYPE, Paint.class);
            Object[] objArr = new Object[2];
            objArr[0] = 1;
            setLayerTypeMethod.invoke(this, objArr);
        } catch (Exception e) {
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        float density = getContext().getResources().getDisplayMetrics().density;
        CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
        canvas.save();
        if (dif.isReverse()) {
            canvas.rotate(180.0f, getWidth() / 2.0f, getHeight() / 2.0f);
        }
        this.outlineRect.set(0.0f, 0.0f, getWidth(), getHeight());
        this.outlineRect.inset(1.5f * density, 1.5f * density);
        float cornerRadius = (dif.isOdd() ? getWidth() : getHeight()) / 2.0f;
        canvas.drawRoundRect(this.outlineRect, cornerRadius, cornerRadius, this.strokePaint);
        this.maskRect.set(this.outlineRect);
        this.maskRect.inset(kCBProgressBarOutlineWidth * density, kCBProgressBarOutlineWidth * density);
        float cornerRadius2 = (dif.isOdd() ? this.maskRect.width() : this.maskRect.height()) / 2.0f;
        this.maskPath.reset();
        this.maskPath.addRoundRect(this.maskRect, cornerRadius2, cornerRadius2, Path.Direction.CW);
        float elementSize = dif.isOdd() ? this.maskRect.width() : this.maskRect.height();
        this.elementShape.reset();
        if (dif.isOdd()) {
            this.elementShape.moveTo(elementSize, 0.0f);
            this.elementShape.lineTo(elementSize, elementSize);
            this.elementShape.lineTo(0.0f, 2.0f * elementSize);
            this.elementShape.lineTo(0.0f, elementSize);
        } else {
            this.elementShape.moveTo(0.0f, elementSize);
            this.elementShape.lineTo(elementSize, elementSize);
            this.elementShape.lineTo(2.0f * elementSize, 0.0f);
            this.elementShape.lineTo(elementSize, 0.0f);
        }
        this.elementShape.close();
        canvas.save();
        canvas.clipPath(this.maskPath);
        float left = (-elementSize) + this.offset;
        while (true) {
            if (left < (dif.isOdd() ? this.maskRect.height() : this.maskRect.width()) + elementSize) {
                float elementOriginX = (dif.isOdd() ? this.maskRect.top : this.maskRect.left) + left;
                canvas.save();
                if (dif.isOdd()) {
                    canvas.translate(this.maskRect.left, elementOriginX);
                } else {
                    canvas.translate(elementOriginX, this.maskRect.top);
                }
                canvas.drawPath(this.elementShape, this.fillPaint);
                canvas.restore();
                left += 2.0f * elementSize;
            } else {
                canvas.restore();
                canvas.restore();
                long now = (long) (System.nanoTime() / 1000000.0d);
                long interval = now - this.lastFrame;
                long timeUntilNextFrame = Math.max(0L, 16 - interval);
                this.handler.removeCallbacks(this.step);
                this.handler.postDelayed(this.step, timeUntilNextFrame);
                return;
            }
        }
    }

    @Override // android.view.View
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.handler.removeCallbacks(this.step);
        if (visibility == 0) {
            this.handler.post(this.step);
        }
    }

    @Override // android.view.View
    protected void onAttachedToWindow() {
        this.handler.removeCallbacks(this.step);
        this.handler.post(this.step);
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        this.handler.removeCallbacks(this.step);
    }
}
