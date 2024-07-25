package com.chartboost.sdk.NativeViews;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chartboost.sdk.Libraries.CBUtility;
import com.chartboost.sdk.Libraries.CBWebImageCache;
import com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol;
import com.mappn.sdk.uc.util.Constants;
import com.mokredit.payment.StringUtils;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CBNativeMoreAppsRegularCell extends CBNativeMoreAppsCell implements CBNativeMoreAppsViewProtocol.MoreAppsCellInterface {
    private static int kCBNativeMoreAppsRegularIconSize = 50;
    private static int kCBNativeMoreAppsRegularMargin = 10;
    public CBNativeMoreAppsSexyButton clickButton;
    private CBNativeMoreAppsSexyImageView iconView;
    private TextView nameLabel;

    public CBNativeMoreAppsRegularCell(Context context) {
        super(context);
        this.iconView = new CBNativeMoreAppsSexyImageView(context);
        this.clickButton = new CBNativeMoreAppsSexyButton(context);
        this.nameLabel = new TextView(context);
        this.nameLabel.setTypeface(null, 1);
        this.nameLabel.setTextSize(2, 16.0f);
        this.nameLabel.setShadowLayer(1.0f, 1.0f, 1.0f, -1);
        this.nameLabel.setBackgroundColor(0);
        this.nameLabel.setTextColor(-16777216);
        this.nameLabel.setEllipsize(TextUtils.TruncateAt.END);
        setBackgroundColor(-3355444);
        setFocusable(false);
        addView(this.iconView);
        addView(this.nameLabel);
        addView(this.clickButton);
    }

    @Override // com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.MoreAppsCellInterface
    public void prepareWithCellMeta(JSONObject meta, int position) {
        JSONObject icon;
        this.nameLabel.setText(meta.optString("name", "Unknown App"));
        String deepText = meta.optString("deep-text");
        if (deepText != null && !deepText.equals(StringUtils.EMPTY)) {
            this.clickButton.setText(deepText);
        } else {
            String text = meta.optString("text", "VIEW");
            this.clickButton.setText(text);
        }
        JSONObject assets = meta.optJSONObject("assets");
        if (assets != null && (icon = assets.optJSONObject(Constants.ICON)) != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", position);
            CBWebImageCache.sharedCache().loadImageWithURL(icon.optString(Constants.PUSH_URL), icon.optString("checksum"), null, this.iconView, bundle);
        }
        layoutSubviews();
    }

    @Override // com.chartboost.sdk.NativeViews.CBNativeMoreAppsCell
    protected void layoutSubviews() {
        int regularIconSize = CBUtility.dpToPixels(kCBNativeMoreAppsRegularIconSize, getContext());
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(regularIconSize, regularIconSize);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(-2, -1);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(-2, -2);
        int regularMargin = CBUtility.dpToPixels(kCBNativeMoreAppsRegularMargin, getContext());
        iconParams.setMargins(regularMargin, regularMargin, regularMargin, regularMargin);
        labelParams.setMargins(regularMargin, regularMargin, regularMargin, regularMargin);
        buttonParams.setMargins(regularMargin, regularMargin, regularMargin, regularMargin);
        labelParams.weight = 1.0f;
        this.nameLabel.setGravity(16);
        buttonParams.gravity = 16;
        this.iconView.setLayoutParams(iconParams);
        this.nameLabel.setLayoutParams(labelParams);
        this.clickButton.setLayoutParams(buttonParams);
    }

    @Override // com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.MoreAppsCellInterface
    public int height() {
        return CBUtility.dpToPixels(kCBNativeMoreAppsRegularIconSize + (kCBNativeMoreAppsRegularMargin * 2), getContext());
    }
}
