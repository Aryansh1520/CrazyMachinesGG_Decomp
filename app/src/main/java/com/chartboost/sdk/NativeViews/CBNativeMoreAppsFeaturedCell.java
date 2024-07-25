package com.chartboost.sdk.NativeViews;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBUtility;
import com.chartboost.sdk.Libraries.CBWebImageCache;
import com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol;
import com.mappn.sdk.uc.util.Constants;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CBNativeMoreAppsFeaturedCell extends CBNativeMoreAppsCell implements CBNativeMoreAppsViewProtocol.MoreAppsCellInterface {
    private static int kCBNativeMoreAppsFeaturedCellAssetHeight = 100;
    private static int kCBNativeMoreAppsFeaturedCellMargin = 5;
    private CBNativeMoreAppsSexyImageView imageView;

    public CBNativeMoreAppsFeaturedCell(Context context) {
        super(context);
        this.imageView = new CBNativeMoreAppsSexyImageView(context);
        addView(this.imageView, new LinearLayout.LayoutParams(-1, -1));
    }

    @Override // com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.MoreAppsCellInterface
    public void prepareWithCellMeta(JSONObject cellMeta, int position) {
        boolean isPortrait = Chartboost.sharedChartboost().orientation().isPortrait();
        JSONObject assets = cellMeta.optJSONObject("assets");
        if (assets != null) {
            JSONObject icon = assets.optJSONObject(isPortrait ? "portrait" : "landscape");
            if (icon != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", position);
                CBWebImageCache.sharedCache().loadImageWithURL(icon.optString(Constants.PUSH_URL), icon.optString("checksum"), null, this.imageView, bundle);
            }
        }
    }

    @Override // com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.MoreAppsCellInterface
    public int height() {
        return CBUtility.dpToPixels(kCBNativeMoreAppsFeaturedCellAssetHeight + (kCBNativeMoreAppsFeaturedCellMargin * 2), getContext());
    }

    @Override // com.chartboost.sdk.NativeViews.CBNativeMoreAppsCell
    protected void layoutSubviews() {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(-1, -1);
        int regularMargin = CBUtility.dpToPixels(kCBNativeMoreAppsFeaturedCellMargin, getContext());
        buttonParams.setMargins(regularMargin, regularMargin, regularMargin, regularMargin);
        this.imageView.setLayoutParams(buttonParams);
    }
}
