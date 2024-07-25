package com.chartboost.sdk.View;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/* loaded from: classes.dex */
public class CBFlipAnimation extends Animation {
    private Camera m_Camera;
    private final float m_CenterX;
    private final float m_CenterY;
    private final float m_FromDegrees;
    private final float m_ToDegrees;
    private boolean m_YAxis;

    public CBFlipAnimation(float from_degrees, float to_degrees, float x_center, float y_center, boolean yaxis) {
        this.m_YAxis = true;
        this.m_FromDegrees = from_degrees;
        this.m_ToDegrees = to_degrees;
        this.m_CenterX = x_center;
        this.m_CenterY = y_center;
        this.m_YAxis = yaxis;
    }

    @Override // android.view.animation.Animation
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.m_Camera = new Camera();
    }

    @Override // android.view.animation.Animation
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degrees = this.m_FromDegrees + ((this.m_ToDegrees - this.m_FromDegrees) * interpolatedTime);
        Camera camera = this.m_Camera;
        Matrix matrix = t.getMatrix();
        camera.save();
        if (this.m_YAxis) {
            camera.rotateY(degrees);
        } else {
            camera.rotateX(degrees);
        }
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-this.m_CenterX, -this.m_CenterY);
        matrix.postTranslate(this.m_CenterX, this.m_CenterY);
    }
}
