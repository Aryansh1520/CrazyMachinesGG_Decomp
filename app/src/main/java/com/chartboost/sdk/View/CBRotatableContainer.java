package com.chartboost.sdk.View;

import android.content.Context;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.RelativeLayout;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;

/* loaded from: classes.dex */
public class CBRotatableContainer extends AbsoluteLayout {
    private RelativeLayout builtInContainer;
    private View content;
    private Matrix mForward;
    private Matrix mReverse;
    private float[] mTemp;

    public CBRotatableContainer(Context context) {
        super(context);
        this.mForward = new Matrix();
        this.mReverse = new Matrix();
        this.mTemp = new float[2];
        this.builtInContainer = new RelativeLayout(context);
        addView(this.builtInContainer, new AbsoluteLayout.LayoutParams(-1, -1, 0, 0));
        this.content = this.builtInContainer;
    }

    public void addViewToContainer(View view) {
        addViewToContainer(view, new RelativeLayout.LayoutParams(-2, -2));
    }

    public void addViewToContainer(View view, RelativeLayout.LayoutParams rllp) {
        if (this.builtInContainer != null) {
            this.builtInContainer.addView(view, rllp);
            return;
        }
        throw new IllegalStateException("cannot call addViewToContainer() on CBRotatableContainer that was set up with a default view");
    }

    public CBRotatableContainer(Context context, View view) {
        super(context);
        this.mForward = new Matrix();
        this.mReverse = new Matrix();
        this.mTemp = new float[2];
        addView(view, new AbsoluteLayout.LayoutParams(-1, -1, 0, 0));
        this.content = view;
    }

    @Override // android.view.View
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
        ViewGroup.LayoutParams lp = this.content.getLayoutParams();
        lp.width = dif.isOdd() ? h : w;
        if (!dif.isOdd()) {
            w = h;
        }
        lp.height = w;
        this.content.setLayoutParams(lp);
        this.content.measure(View.MeasureSpec.makeMeasureSpec(lp.width, 1073741824), View.MeasureSpec.makeMeasureSpec(lp.height, 1073741824));
        this.content.layout(0, 0, lp.width, lp.height);
    }

    @Override // android.widget.AbsoluteLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
        if (dif.isOdd()) {
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);
            setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x003e, code lost:
    
        if ((r11 instanceof android.widget.HorizontalScrollView) != false) goto L13;
     */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void dispatchDraw(android.graphics.Canvas r15) {
        /*
            r14 = this;
            r1 = 0
            r13 = 1073741824(0x40000000, float:2.0)
            com.chartboost.sdk.Chartboost r0 = com.chartboost.sdk.Chartboost.sharedChartboost()
            com.chartboost.sdk.Libraries.CBConstants$CBOrientation$Difference r8 = r0.getForcedOrientationDifference()
            int r6 = r8.getAsInt()
            com.chartboost.sdk.Libraries.CBConstants$CBOrientation$Difference r0 = com.chartboost.sdk.Libraries.CBConstants.CBOrientation.Difference.ANGLE_0
            if (r8 != r0) goto L17
            super.dispatchDraw(r15)
        L16:
            return
        L17:
            r15.save()
            int r0 = r14.getWidth()
            float r3 = (float) r0
            int r0 = r14.getHeight()
            float r4 = (float) r0
            android.graphics.Region$Op r5 = android.graphics.Region.Op.REPLACE
            r0 = r15
            r2 = r1
            r0.clipRect(r1, r2, r3, r4, r5)
            android.view.ViewParent r7 = r14.getParent()     // Catch: java.lang.Exception -> Lbe
            android.view.ViewGroup r7 = (android.view.ViewGroup) r7     // Catch: java.lang.Exception -> Lbe
            r10 = r7
            android.view.ViewParent r11 = r7.getParent()     // Catch: java.lang.Exception -> Lc0
            android.view.ViewGroup r11 = (android.view.ViewGroup) r11     // Catch: java.lang.Exception -> Lc0
            boolean r0 = r11 instanceof android.widget.ScrollView     // Catch: java.lang.Exception -> Lc0
            if (r0 != 0) goto L40
            boolean r0 = r11 instanceof android.widget.HorizontalScrollView     // Catch: java.lang.Exception -> Lc0
            if (r0 == 0) goto L41
        L40:
            r10 = r11
        L41:
            int r0 = r14.getLeft()     // Catch: java.lang.Exception -> Lbe
            int r1 = r10.getScrollX()     // Catch: java.lang.Exception -> Lbe
            int r9 = r0 - r1
            int r0 = r14.getTop()     // Catch: java.lang.Exception -> Lbe
            int r1 = r10.getScrollY()     // Catch: java.lang.Exception -> Lbe
            int r12 = r0 - r1
            int r0 = 0 - r9
            float r1 = (float) r0     // Catch: java.lang.Exception -> Lbe
            int r0 = 0 - r12
            float r2 = (float) r0     // Catch: java.lang.Exception -> Lbe
            int r0 = r10.getWidth()     // Catch: java.lang.Exception -> Lbe
            int r0 = r0 - r9
            float r3 = (float) r0     // Catch: java.lang.Exception -> Lbe
            int r0 = r10.getHeight()     // Catch: java.lang.Exception -> Lbe
            int r0 = r0 - r12
            float r4 = (float) r0     // Catch: java.lang.Exception -> Lbe
            android.graphics.Region$Op r5 = android.graphics.Region.Op.INTERSECT     // Catch: java.lang.Exception -> Lbe
            r0 = r15
            r0.clipRect(r1, r2, r3, r4, r5)     // Catch: java.lang.Exception -> Lbe
        L6d:
            int r0 = r14.getWidth()
            float r0 = (float) r0
            float r0 = r0 / r13
            int r1 = r14.getHeight()
            float r1 = (float) r1
            float r1 = r1 / r13
            r15.translate(r0, r1)
            float r0 = (float) r6
            r15.rotate(r0)
            boolean r0 = r8.isOdd()
            if (r0 == 0) goto Lac
            int r0 = r14.getHeight()
            int r0 = -r0
            float r0 = (float) r0
            float r0 = r0 / r13
            int r1 = r14.getWidth()
            int r1 = -r1
            float r1 = (float) r1
            float r1 = r1 / r13
            r15.translate(r0, r1)
        L97:
            android.graphics.Matrix r0 = r15.getMatrix()
            r14.mForward = r0
            android.graphics.Matrix r0 = r14.mForward
            android.graphics.Matrix r1 = r14.mReverse
            r0.invert(r1)
            super.dispatchDraw(r15)
            r15.restore()
            goto L16
        Lac:
            int r0 = r14.getWidth()
            int r0 = -r0
            float r0 = (float) r0
            float r0 = r0 / r13
            int r1 = r14.getHeight()
            int r1 = -r1
            float r1 = (float) r1
            float r1 = r1 / r13
            r15.translate(r0, r1)
            goto L97
        Lbe:
            r0 = move-exception
            goto L6d
        Lc0:
            r0 = move-exception
            goto L41
        */
        throw new UnsupportedOperationException("Method not decompiled: com.chartboost.sdk.View.CBRotatableContainer.dispatchDraw(android.graphics.Canvas):void");
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent event) {
        CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
        if (dif == CBConstants.CBOrientation.Difference.ANGLE_0) {
            return super.dispatchTouchEvent(event);
        }
        float[] temp = this.mTemp;
        temp[0] = event.getRawX();
        temp[1] = event.getRawY();
        this.mReverse.mapPoints(temp);
        event.setLocation(temp[0], temp[1]);
        return super.dispatchTouchEvent(event);
    }

    public View getContentView() {
        return this.content;
    }
}
