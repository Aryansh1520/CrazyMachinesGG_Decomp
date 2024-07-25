package com.chartboost.sdk.View;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;
import com.chartboost.sdk.Libraries.CBWebImageCache;
import com.chartboost.sdk.Model.CBImpression;
import com.chartboost.sdk.View.CBPopupImpressionView;
import com.mappn.sdk.uc.util.Constants;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class CBViewProtocol {
    protected JSONObject assets;
    private int imagesToLoad;
    protected CBImpression impression;
    private int loadedCount;
    private int processedCount;
    protected int totalImages;
    public BlockInterface closeCallback = null;
    public ClickBlockInterface clickCallback = null;
    public BlockInterface displayCallback = null;
    public BlockInterface failCallback = null;
    protected int expectedImagesCount = 0;
    private CBViewBase view = null;

    /* loaded from: classes.dex */
    public interface BlockInterface {
        void execute();
    }

    /* loaded from: classes.dex */
    public interface ClickBlockInterface {
        void execute(String str, JSONObject jSONObject);
    }

    protected abstract CBViewBase createViewObject(Context context);

    /* loaded from: classes.dex */
    public abstract class CBViewBase extends RelativeLayout implements CBPopupImpressionView.CBViewUpdater {
        protected boolean ignore;

        protected abstract void layoutSubviews(int i, int i2);

        public CBViewBase(Context context) {
            super(context);
            this.ignore = false;
            setFocusableInTouchMode(true);
            requestFocus();
        }

        @Override // android.view.View
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            if (!this.ignore) {
                CBConstants.CBOrientation.Difference orDiff = Chartboost.sharedChartboost().getForcedOrientationDifference();
                if (orDiff.isOdd()) {
                    tryLayout(h, w);
                } else {
                    tryLayout(w, h);
                }
            }
        }

        private boolean tryLayout(int w, int h) {
            try {
                layoutSubviews(w, h);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override // com.chartboost.sdk.View.CBPopupImpressionView.CBViewUpdater
        public void onViewUpdateRequired() {
            tryLayout((Activity) getContext());
        }

        public void destroy() {
        }

        public boolean tryLayout(Activity cx) {
            int height;
            int width;
            try {
                width = getWidth();
                height = getHeight();
                if (width == 0 || height == 0) {
                    View root = cx.getWindow().findViewById(R.id.content);
                    if (root == null) {
                        root = cx.getWindow().getDecorView();
                    }
                    width = root.getWidth();
                    height = root.getHeight();
                }
            } catch (Exception e) {
                height = 0;
                width = 0;
            }
            if (width == 0 || height == 0) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                Chartboost.sharedChartboost().getContext().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                width = displaymetrics.widthPixels;
                height = displaymetrics.heightPixels;
            }
            CBConstants.CBOrientation.Difference orDiff = Chartboost.sharedChartboost().getForcedOrientationDifference();
            if (orDiff.isOdd()) {
                int temp = width;
                width = height;
                height = temp;
            }
            return tryLayout(width, height);
        }
    }

    public CBViewProtocol(CBImpression impression) {
        this.impression = impression;
    }

    public void prepareWithResponse(JSONObject response) {
        this.processedCount = 0;
        this.imagesToLoad = 0;
        this.loadedCount = 0;
        this.assets = response.optJSONObject("assets");
        if (this.assets != null || this.failCallback == null) {
            return;
        }
        this.failCallback.execute();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void PROCESS_LOADING_ASSET(String key, CBWebImageCache.CBWebImageProtocol callback) {
        PROCESS_LOADING_ASSET(this.assets, key, callback, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void PROCESS_LOADING_ASSET(JSONObject assetsObject, String key, CBWebImageCache.CBWebImageProtocol callback, Bundle bundle) {
        JSONObject propInfo = assetsObject.optJSONObject(key);
        if (propInfo != null) {
            this.imagesToLoad++;
            String propUrl = propInfo.optString(Constants.PUSH_URL);
            String propChecksum = propInfo.optString("checksum");
            CBWebImageCache.sharedCache().loadImageWithURL(propUrl, propChecksum, callback, null, bundle);
            return;
        }
        onBitmapLoaded(null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onBitmapLoaded(Bitmap bitmap) {
        if (bitmap != null) {
            this.loadedCount++;
        }
        this.processedCount++;
        if (this.processedCount != this.expectedImagesCount || setReadyToDisplay() || this.failCallback == null) {
            return;
        }
        this.failCallback.execute();
    }

    public boolean setReadyToDisplay() {
        if (this.loadedCount != this.imagesToLoad) {
            return false;
        }
        if (this.displayCallback != null) {
            this.displayCallback.execute();
        }
        return true;
    }

    public boolean createView() {
        if (this.impression.state != CBImpression.CBImpressionState.CBImpressionStateWaitingForDisplay) {
            return false;
        }
        Chartboost.sharedChartboost().setActiveImpression(this.impression);
        Activity context = Chartboost.sharedChartboost().getContext();
        if (context == null) {
            this.view = null;
            return false;
        }
        this.view = createViewObject(context);
        if (this.view.tryLayout(context)) {
            return true;
        }
        this.view = null;
        return false;
    }

    public void destroy() {
        destroyView();
        this.displayCallback = null;
        this.failCallback = null;
        this.clickCallback = null;
        this.closeCallback = null;
        this.assets = null;
    }

    public CBViewBase getView() {
        return this.view;
    }

    public void destroyView() {
        if (this.view != null) {
            this.view.destroy();
        }
        this.view = null;
    }
}
