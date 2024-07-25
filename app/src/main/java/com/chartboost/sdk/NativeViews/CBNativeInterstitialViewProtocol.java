package com.chartboost.sdk.NativeViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;
import com.chartboost.sdk.Libraries.CBUtility;
import com.chartboost.sdk.Libraries.CBWebImageCache;
import com.chartboost.sdk.Model.CBImpression;
import com.chartboost.sdk.View.CBRotatableContainer;
import com.chartboost.sdk.View.CBViewProtocol;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CBNativeInterstitialViewProtocol extends CBViewProtocol {
    public Bitmap adImageLandscape;
    public Bitmap adImagePortrait;
    public Bitmap closeImage;
    public Bitmap frameImageLandscape;
    public Bitmap frameImagePortrait;

    /* loaded from: classes.dex */
    public class CBNativeInterstitialView extends CBViewProtocol.CBViewBase {
        public ImageView adUnit;
        public Button closeButton;
        public CBRotatableContainer container;
        public ImageView frame;

        private CBNativeInterstitialView(Context context) {
            super(context);
            setBackgroundColor(0);
            setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            this.container = new CBRotatableContainer(context);
            this.adUnit = new ImageView(context);
            this.closeButton = new Button(context);
            this.frame = new ImageView(context);
            this.closeButton.setOnClickListener(new View.OnClickListener() { // from class: com.chartboost.sdk.NativeViews.CBNativeInterstitialViewProtocol.CBNativeInterstitialView.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (CBNativeInterstitialViewProtocol.this.closeCallback != null) {
                        CBNativeInterstitialViewProtocol.this.closeCallback.execute();
                    }
                }
            });
            this.adUnit.setClickable(true);
            this.adUnit.setOnClickListener(new View.OnClickListener() { // from class: com.chartboost.sdk.NativeViews.CBNativeInterstitialViewProtocol.CBNativeInterstitialView.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (CBNativeInterstitialViewProtocol.this.clickCallback != null) {
                        CBNativeInterstitialViewProtocol.this.clickCallback.execute(null, null);
                    }
                }
            });
            this.container.addViewToContainer(this.frame);
            this.container.addViewToContainer(this.adUnit);
            this.container.addViewToContainer(this.closeButton);
            addView(this.container, new RelativeLayout.LayoutParams(-1, -1));
        }

        /* synthetic */ CBNativeInterstitialView(CBNativeInterstitialViewProtocol cBNativeInterstitialViewProtocol, Context context, CBNativeInterstitialView cBNativeInterstitialView) {
            this(context);
        }

        @Override // com.chartboost.sdk.View.CBViewProtocol.CBViewBase
        protected void layoutSubviews(int w, int h) {
            Chartboost cb = Chartboost.sharedChartboost();
            CBConstants.CBOrientation orientation = cb.orientation();
            boolean isPortrait = orientation.isPortrait();
            Bitmap adImage = isPortrait ? CBNativeInterstitialViewProtocol.this.adImagePortrait : CBNativeInterstitialViewProtocol.this.adImageLandscape;
            Bitmap frameImage = isPortrait ? CBNativeInterstitialViewProtocol.this.frameImagePortrait : CBNativeInterstitialViewProtocol.this.frameImageLandscape;
            RelativeLayout.LayoutParams frameParams = (RelativeLayout.LayoutParams) this.frame.getLayoutParams();
            RelativeLayout.LayoutParams adParams = (RelativeLayout.LayoutParams) this.adUnit.getLayoutParams();
            RelativeLayout.LayoutParams closeParams = (RelativeLayout.LayoutParams) this.closeButton.getLayoutParams();
            float scale = isPortrait ? Math.max(320.0f / w, 480.0f / h) : Math.max(320.0f / h, 480.0f / w);
            frameParams.width = (int) (frameImage.getWidth() / scale);
            frameParams.height = (int) (frameImage.getHeight() / scale);
            Point frameOffset = getOffset(isPortrait ? "frame-portrait" : "frame-landscape");
            frameParams.leftMargin = Math.round(((w - frameParams.width) / 2.0f) + (frameOffset.x / scale));
            frameParams.topMargin = Math.round(((h - frameParams.height) / 2.0f) + (frameOffset.y / scale));
            this.adUnit.setId(100);
            adParams.width = (int) (adImage.getWidth() / scale);
            adParams.height = (int) (adImage.getHeight() / scale);
            Point adOffset = getOffset(isPortrait ? "ad-portrait" : "ad-landscape");
            adParams.leftMargin = Math.round(((w - adParams.width) / 2.0f) + (adOffset.x / scale));
            adParams.topMargin = Math.round((isPortrait ? -CBUtility.dpToPixelsF(5, getContext()) : 0.0f) + (adOffset.y / scale) + ((h - adParams.height) / 2.0f));
            closeParams.width = (int) (CBNativeInterstitialViewProtocol.this.closeImage.getWidth() / scale);
            closeParams.height = (int) (CBNativeInterstitialViewProtocol.this.closeImage.getHeight() / scale);
            Point closeOffset = getOffset("close");
            closeParams.leftMargin = adParams.leftMargin + adParams.width + Math.round((closeOffset.x / scale) - CBUtility.dpToPixelsF(10, getContext()));
            closeParams.topMargin = (adParams.topMargin - closeParams.height) + Math.round((closeOffset.y / scale) - CBUtility.dpToPixelsF(10, getContext()));
            this.frame.setLayoutParams(frameParams);
            this.adUnit.setLayoutParams(adParams);
            this.closeButton.setLayoutParams(closeParams);
            Drawable frameDrawable = new BitmapDrawable(frameImage);
            this.frame.setImageDrawable(frameDrawable);
            Drawable adDrawable = new BitmapDrawable(adImage);
            this.adUnit.setImageDrawable(adDrawable);
            Drawable closeDrawable = new BitmapDrawable(CBNativeInterstitialViewProtocol.this.closeImage);
            this.closeButton.setBackgroundDrawable(closeDrawable);
        }

        @Override // com.chartboost.sdk.View.CBViewProtocol.CBViewBase
        public void destroy() {
            super.destroy();
            this.adUnit = null;
            this.frame = null;
            this.closeButton = null;
        }

        private Point getOffset(String param) {
            JSONObject offsetObj;
            JSONObject marginObj = CBNativeInterstitialViewProtocol.this.assets.optJSONObject(param);
            return (marginObj == null || (offsetObj = marginObj.optJSONObject("offset")) == null) ? new Point(0, 0) : new Point(offsetObj.optInt("x", 0), offsetObj.optInt("y", 0));
        }
    }

    public CBNativeInterstitialViewProtocol(CBImpression impression) {
        super(impression);
        this.expectedImagesCount = 5;
        this.frameImagePortrait = null;
        this.frameImageLandscape = null;
        this.adImagePortrait = null;
        this.adImageLandscape = null;
        this.closeImage = null;
    }

    @Override // com.chartboost.sdk.View.CBViewProtocol
    protected CBViewProtocol.CBViewBase createViewObject(Context context) {
        return new CBNativeInterstitialView(this, context, null);
    }

    @Override // com.chartboost.sdk.View.CBViewProtocol
    public void prepareWithResponse(JSONObject response) {
        super.prepareWithResponse(response);
        CBWebImageCache.CBWebImageProtocol cb1 = new CBWebImageCache.CBWebImageProtocol() { // from class: com.chartboost.sdk.NativeViews.CBNativeInterstitialViewProtocol.1
            @Override // com.chartboost.sdk.Libraries.CBWebImageCache.CBWebImageProtocol
            public void execute(Bitmap bitmap, Bundle bundle) {
                CBNativeInterstitialViewProtocol.this.adImageLandscape = bitmap;
                CBNativeInterstitialViewProtocol.this.onBitmapLoaded(bitmap);
            }
        };
        CBWebImageCache.CBWebImageProtocol cb2 = new CBWebImageCache.CBWebImageProtocol() { // from class: com.chartboost.sdk.NativeViews.CBNativeInterstitialViewProtocol.2
            @Override // com.chartboost.sdk.Libraries.CBWebImageCache.CBWebImageProtocol
            public void execute(Bitmap bitmap, Bundle bundle) {
                CBNativeInterstitialViewProtocol.this.adImagePortrait = bitmap;
                CBNativeInterstitialViewProtocol.this.onBitmapLoaded(bitmap);
            }
        };
        CBWebImageCache.CBWebImageProtocol cb3 = new CBWebImageCache.CBWebImageProtocol() { // from class: com.chartboost.sdk.NativeViews.CBNativeInterstitialViewProtocol.3
            @Override // com.chartboost.sdk.Libraries.CBWebImageCache.CBWebImageProtocol
            public void execute(Bitmap bitmap, Bundle bundle) {
                CBNativeInterstitialViewProtocol.this.frameImageLandscape = bitmap;
                CBNativeInterstitialViewProtocol.this.onBitmapLoaded(bitmap);
            }
        };
        CBWebImageCache.CBWebImageProtocol cb4 = new CBWebImageCache.CBWebImageProtocol() { // from class: com.chartboost.sdk.NativeViews.CBNativeInterstitialViewProtocol.4
            @Override // com.chartboost.sdk.Libraries.CBWebImageCache.CBWebImageProtocol
            public void execute(Bitmap bitmap, Bundle bundle) {
                CBNativeInterstitialViewProtocol.this.frameImagePortrait = bitmap;
                CBNativeInterstitialViewProtocol.this.onBitmapLoaded(bitmap);
            }
        };
        CBWebImageCache.CBWebImageProtocol cb5 = new CBWebImageCache.CBWebImageProtocol() { // from class: com.chartboost.sdk.NativeViews.CBNativeInterstitialViewProtocol.5
            @Override // com.chartboost.sdk.Libraries.CBWebImageCache.CBWebImageProtocol
            public void execute(Bitmap bitmap, Bundle bundle) {
                CBNativeInterstitialViewProtocol.this.closeImage = bitmap;
                CBNativeInterstitialViewProtocol.this.onBitmapLoaded(bitmap);
            }
        };
        PROCESS_LOADING_ASSET("ad-landscape", cb1);
        PROCESS_LOADING_ASSET("ad-portrait", cb2);
        PROCESS_LOADING_ASSET("frame-landscape", cb3);
        PROCESS_LOADING_ASSET("frame-portrait", cb4);
        PROCESS_LOADING_ASSET("close", cb5);
    }

    @Override // com.chartboost.sdk.View.CBViewProtocol
    public void destroy() {
        super.destroy();
        this.adImageLandscape = null;
        this.adImagePortrait = null;
        this.frameImageLandscape = null;
        this.frameImagePortrait = null;
        this.closeImage = null;
    }
}
