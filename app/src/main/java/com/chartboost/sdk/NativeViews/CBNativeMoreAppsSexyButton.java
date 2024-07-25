package com.chartboost.sdk.NativeViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/* loaded from: classes.dex */
public class CBNativeMoreAppsSexyButton extends Button {
    private static final int COLOR_GRADIENT_BOTTOM = -97280;
    private static final int COLOR_GRADIENT_MIDDLE = -89600;
    private static final int COLOR_GRADIENT_TOP = -81366;
    private static final int COLOR_STROKE = -4496384;
    private static final int COLOR_STROKE_INSET = -1;
    private static final int COLOR_TEXT = -1;
    private static final int COLOR_TEXT_SHADOW = -16757901;
    private static final float CORNER_RADIUS = 6.0f;
    private static final float FONT_SIZE = 13.0f;
    private static final float STROKE_WIDTH = 1.0f;
    private Path bgPath;
    private RectF clipRect;
    private Shader gradientNormal;
    private Paint gradientPaint;
    private Shader gradientPressed;
    private Path insetStrokePath;
    private int lastHeight;
    private Paint strokePaint;
    private Path strokePath;

    public CBNativeMoreAppsSexyButton(Context context) {
        super(context);
        init(context);
    }

    public CBNativeMoreAppsSexyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CBNativeMoreAppsSexyButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        setTextSize(2, FONT_SIZE);
        setShadowLayer(STROKE_WIDTH * density, STROKE_WIDTH * density, STROKE_WIDTH * density, COLOR_TEXT_SHADOW);
        setTypeface(null, 1);
        setTextColor(-1);
        setClickable(true);
        setGravity(17);
        setPadding(Math.round(12.0f * density), Math.round(5.0f * density), Math.round(12.0f * density), Math.round(5.0f * density));
        this.bgPath = new Path();
        this.strokePath = new Path();
        this.insetStrokePath = new Path();
        this.clipRect = new RectF();
        this.strokePaint = new Paint();
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setColor(COLOR_STROKE);
        this.strokePaint.setAntiAlias(true);
        this.lastHeight = -1;
        this.gradientPaint = new Paint();
        this.gradientPaint.setStyle(Paint.Style.FILL);
        this.gradientPaint.setAntiAlias(true);
        setBackgroundDrawable(new Drawable() { // from class: com.chartboost.sdk.NativeViews.CBNativeMoreAppsSexyButton.1
            boolean m_pressed = false;

            @Override // android.graphics.drawable.Drawable
            public void draw(Canvas canvas) {
                float density2 = CBNativeMoreAppsSexyButton.this.getContext().getResources().getDisplayMetrics().density;
                boolean pressed = false;
                int[] states = getState();
                for (int i : states) {
                    if (i == 16842919) {
                        pressed = true;
                    }
                }
                float cornerRadius = CBNativeMoreAppsSexyButton.CORNER_RADIUS * density2;
                CBNativeMoreAppsSexyButton.this.bgPath.reset();
                CBNativeMoreAppsSexyButton.this.clipRect.set(getBounds());
                CBNativeMoreAppsSexyButton.this.bgPath.addRoundRect(CBNativeMoreAppsSexyButton.this.clipRect, cornerRadius, cornerRadius, Path.Direction.CW);
                CBNativeMoreAppsSexyButton.this.updateGradients();
                CBNativeMoreAppsSexyButton.this.gradientPaint.setShader(pressed ? CBNativeMoreAppsSexyButton.this.gradientPressed : CBNativeMoreAppsSexyButton.this.gradientNormal);
                canvas.drawPath(CBNativeMoreAppsSexyButton.this.bgPath, CBNativeMoreAppsSexyButton.this.gradientPaint);
                float strokeWidth = CBNativeMoreAppsSexyButton.STROKE_WIDTH * density2;
                CBNativeMoreAppsSexyButton.this.strokePath.reset();
                CBNativeMoreAppsSexyButton.this.clipRect.inset(strokeWidth / 2.0f, strokeWidth / 2.0f);
                CBNativeMoreAppsSexyButton.this.strokePath.addRoundRect(CBNativeMoreAppsSexyButton.this.clipRect, cornerRadius, cornerRadius, Path.Direction.CW);
                CBNativeMoreAppsSexyButton.this.insetStrokePath.reset();
                CBNativeMoreAppsSexyButton.this.clipRect.offset(0.0f, strokeWidth / 2.0f);
                CBNativeMoreAppsSexyButton.this.insetStrokePath.addRoundRect(CBNativeMoreAppsSexyButton.this.clipRect, cornerRadius, cornerRadius, Path.Direction.CW);
                if (!pressed) {
                    CBNativeMoreAppsSexyButton.this.strokePaint.setColor(-1);
                    canvas.drawPath(CBNativeMoreAppsSexyButton.this.insetStrokePath, CBNativeMoreAppsSexyButton.this.strokePaint);
                }
                CBNativeMoreAppsSexyButton.this.strokePaint.setColor(CBNativeMoreAppsSexyButton.COLOR_STROKE);
                canvas.drawPath(CBNativeMoreAppsSexyButton.this.strokePath, CBNativeMoreAppsSexyButton.this.strokePaint);
            }

            @Override // android.graphics.drawable.Drawable
            public void setAlpha(int alpha) {
                CBNativeMoreAppsSexyButton.this.strokePaint.setAlpha(alpha);
                CBNativeMoreAppsSexyButton.this.gradientPaint.setAlpha(alpha);
            }

            @Override // android.graphics.drawable.Drawable
            public void setColorFilter(ColorFilter cf) {
                CBNativeMoreAppsSexyButton.this.strokePaint.setColorFilter(cf);
                CBNativeMoreAppsSexyButton.this.gradientPaint.setColorFilter(cf);
            }

            @Override // android.graphics.drawable.Drawable
            public int getOpacity() {
                return 1;
            }

            @Override // android.graphics.drawable.Drawable
            protected boolean onStateChange(int[] states) {
                boolean pressed = false;
                for (int i : states) {
                    if (i == 16842919) {
                        pressed = true;
                    }
                }
                if (this.m_pressed != pressed) {
                    this.m_pressed = pressed;
                    invalidateSelf();
                    return true;
                }
                return false;
            }

            @Override // android.graphics.drawable.Drawable
            public boolean isStateful() {
                return true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateGradients() {
        int height = getHeight();
        if (height != this.lastHeight) {
            this.lastHeight = height;
            int[] colorsNormal = {COLOR_GRADIENT_TOP, COLOR_GRADIENT_MIDDLE, COLOR_GRADIENT_BOTTOM};
            this.gradientNormal = new LinearGradient(0.0f, 0.0f, 0.0f, getHeight(), colorsNormal, (float[]) null, Shader.TileMode.CLAMP);
            int[] colorsPressed = {COLOR_GRADIENT_BOTTOM, COLOR_GRADIENT_MIDDLE, COLOR_GRADIENT_TOP};
            this.gradientPressed = new LinearGradient(0.0f, 0.0f, 0.0f, getHeight(), colorsPressed, (float[]) null, Shader.TileMode.CLAMP);
        }
    }
}
