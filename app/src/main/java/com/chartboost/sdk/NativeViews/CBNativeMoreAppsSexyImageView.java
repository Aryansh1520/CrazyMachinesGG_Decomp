package com.chartboost.sdk.NativeViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/* loaded from: classes.dex */
public class CBNativeMoreAppsSexyImageView extends ImageView {
    private static final float CORNER_RADIUS = 10.0f;
    private static final int STROKE_COLOR = -1509949440;
    private static final float STROKE_WIDTH = 1.0f;
    private RectF clipRect;
    private Paint strokePaint;
    private Xfermode xferMode;

    public CBNativeMoreAppsSexyImageView(Context context) {
        super(context);
        init(context);
    }

    public CBNativeMoreAppsSexyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CBNativeMoreAppsSexyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        float density = getContext().getResources().getDisplayMetrics().density;
        this.xferMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        this.clipRect = new RectF();
        this.strokePaint = new Paint();
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setColor(STROKE_COLOR);
        this.strokePaint.setStrokeWidth(Math.max(STROKE_WIDTH, STROKE_WIDTH * density));
        this.strokePaint.setAntiAlias(true);
    }

    @Override // android.widget.ImageView, android.view.View
    protected void onDraw(Canvas canvas) {
        float density = getContext().getResources().getDisplayMetrics().density;
        float cornerRadius = CORNER_RADIUS * density;
        float strokeWidth = STROKE_WIDTH * density;
        Drawable bmpDrawable = getDrawable();
        if (bmpDrawable instanceof BitmapDrawable) {
            Paint paint = ((BitmapDrawable) bmpDrawable).getPaint();
            Rect bitmapBounds = bmpDrawable.getBounds();
            this.clipRect.set(bitmapBounds);
            if (getImageMatrix() != null) {
                getImageMatrix().mapRect(this.clipRect);
            }
            int saveCount = canvas.saveLayer(this.clipRect, null, 31);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(-16777216);
            canvas.drawRoundRect(this.clipRect, cornerRadius, cornerRadius, paint);
            Xfermode oldMode = paint.getXfermode();
            paint.setXfermode(this.xferMode);
            super.onDraw(canvas);
            paint.setXfermode(oldMode);
            canvas.restoreToCount(saveCount);
        } else {
            super.onDraw(canvas);
        }
        this.clipRect.set(0.0f, 0.0f, getWidth(), getHeight());
        this.clipRect.inset(strokeWidth / 2.0f, strokeWidth / 2.0f);
        canvas.drawRoundRect(this.clipRect, cornerRadius, cornerRadius, this.strokePaint);
    }
}
