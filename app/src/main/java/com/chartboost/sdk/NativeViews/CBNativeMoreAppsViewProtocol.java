package com.chartboost.sdk.NativeViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;
import com.chartboost.sdk.Libraries.CBUtility;
import com.chartboost.sdk.Libraries.CBWebImageCache;
import com.chartboost.sdk.Model.CBImpression;
import com.chartboost.sdk.View.CBListView;
import com.chartboost.sdk.View.CBRotatableContainer;
import com.chartboost.sdk.View.CBViewProtocol;
import com.mappn.sdk.uc.util.Constants;
import com.mokredit.payment.StringUtils;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CBNativeMoreAppsViewProtocol extends CBViewProtocol {
    private List<JSONObject> cells;
    private Bitmap closeImage;
    private Bitmap headerImage;
    private Bitmap headerTileImage;
    private SparseArray<Bitmap> iconImages;
    private static int kCBNativeMoreAppsHeaderHeight = 50;
    private static int kCBNativeMoreAppsCloseButtonWidth = 50;
    private static int kCBNativeMoreAppsCloseButtonHeight = 30;

    /* loaded from: classes.dex */
    public interface MoreAppsCellInterface {
        int height();

        void prepareWithCellMeta(JSONObject jSONObject, int i);
    }

    /* loaded from: classes.dex */
    public class CBNativeMoreAppsView extends CBViewProtocol.CBViewBase {
        private static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference;
        private MoreAppsAdapter adapter;
        private ImageView closeButton;
        private CBRotatableContainer closeFrame;
        private CBListView tableView;
        private CBRotatableContainer titleFrame;
        private ImageView titleView;
        private FrameLayout titleViewBG;

        static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference() {
            int[] iArr = $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference;
            if (iArr == null) {
                iArr = new int[CBConstants.CBOrientation.Difference.valuesCustom().length];
                try {
                    iArr[CBConstants.CBOrientation.Difference.ANGLE_0.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[CBConstants.CBOrientation.Difference.ANGLE_180.ordinal()] = 3;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[CBConstants.CBOrientation.Difference.ANGLE_270.ordinal()] = 4;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[CBConstants.CBOrientation.Difference.ANGLE_90.ordinal()] = 2;
                } catch (NoSuchFieldError e4) {
                }
                $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference = iArr;
            }
            return iArr;
        }

        private CBNativeMoreAppsView(Context context) {
            super(context);
            setBackgroundColor(-1842205);
            this.titleViewBG = new FrameLayout(context);
            this.titleView = new ImageView(context);
            this.closeButton = new ImageView(context);
            CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
            this.tableView = new CBListView(context, dif.isOdd() ? 0 : 1);
            this.tableView.getContainer().setBackgroundColor(-1842205);
            this.titleViewBG.setFocusable(false);
            this.titleView.setFocusable(false);
            this.closeButton.setFocusable(false);
            this.closeButton.setClickable(true);
            this.closeFrame = new CBRotatableContainer(context, this.closeButton);
            this.titleFrame = new CBRotatableContainer(context, this.titleViewBG);
            addView(this.titleFrame);
            this.titleViewBG.addView(this.titleView);
            addView(this.closeFrame);
            assignID(this.titleView);
            assignID(this.titleViewBG);
            assignID(this.closeButton);
            assignID(this.titleFrame);
            assignID(this.closeFrame);
            this.closeButton.setOnClickListener(new View.OnClickListener() { // from class: com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.CBNativeMoreAppsView.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (CBNativeMoreAppsViewProtocol.this.closeCallback != null) {
                        CBNativeMoreAppsViewProtocol.this.closeCallback.execute();
                    }
                }
            });
            this.adapter = new MoreAppsAdapter(context);
        }

        /* synthetic */ CBNativeMoreAppsView(CBNativeMoreAppsViewProtocol cBNativeMoreAppsViewProtocol, Context context, CBNativeMoreAppsView cBNativeMoreAppsView) {
            this(context);
        }

        private void assignID(View view) {
            int id = 200;
            if (200 == getId()) {
                id = 200 + 1;
            }
            View v = findViewById(id);
            while (v != null) {
                id++;
                v = findViewById(id);
            }
            view.setId(id);
            view.setSaveEnabled(false);
        }

        @Override // com.chartboost.sdk.View.CBViewProtocol.CBViewBase
        protected void layoutSubviews(int w, int h) {
            int i;
            int i2;
            if (this.tableView.getView() != null) {
                removeView(this.tableView.getView());
            }
            FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(-2, -2, 17);
            RelativeLayout.LayoutParams titleBGParams = new RelativeLayout.LayoutParams(-2, -2);
            RelativeLayout.LayoutParams closeParams = new RelativeLayout.LayoutParams(-2, -2);
            RelativeLayout.LayoutParams tableParams = new RelativeLayout.LayoutParams(-2, -2);
            final CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
            titleBGParams.width = dif.isOdd() ? CBUtility.dpToPixels(CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsHeaderHeight, getContext()) : -1;
            titleBGParams.height = dif.isOdd() ? -1 : CBUtility.dpToPixels(CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsHeaderHeight, getContext());
            switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[dif.ordinal()]) {
                case 2:
                    titleBGParams.addRule(11);
                    break;
                case 3:
                    titleBGParams.addRule(12);
                    break;
            }
            BitmapDrawable bgTile = new BitmapDrawable(CBNativeMoreAppsViewProtocol.this.headerTileImage);
            bgTile.setTileModeX(Shader.TileMode.REPEAT);
            bgTile.setTileModeY(Shader.TileMode.CLAMP);
            this.titleViewBG.setBackgroundDrawable(bgTile);
            if (CBNativeMoreAppsViewProtocol.this.headerImage != null) {
                this.titleView.setImageBitmap(CBNativeMoreAppsViewProtocol.this.headerImage);
                titleParams.width = CBUtility.dpToPixels(CBNativeMoreAppsViewProtocol.this.headerImage.getWidth(), getContext());
                titleParams.height = CBUtility.dpToPixels(Math.min(CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsHeaderHeight, CBNativeMoreAppsViewProtocol.this.headerImage.getHeight()), getContext());
            }
            this.closeButton.setImageBitmap(CBNativeMoreAppsViewProtocol.this.closeImage);
            if (!dif.isOdd()) {
                i = CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsCloseButtonWidth;
            } else {
                i = CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsCloseButtonHeight;
            }
            closeParams.width = CBUtility.dpToPixels(i, getContext());
            if (dif.isOdd()) {
                i2 = CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsCloseButtonWidth;
            } else {
                i2 = CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsCloseButtonHeight;
            }
            closeParams.height = CBUtility.dpToPixels(i2, getContext());
            switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[dif.ordinal()]) {
                case 2:
                    closeParams.bottomMargin = CBUtility.dpToPixels(10, getContext());
                    closeParams.rightMargin = CBUtility.dpToPixels((CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsHeaderHeight - CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsCloseButtonHeight) / 2, getContext());
                    closeParams.addRule(11);
                    closeParams.addRule(12);
                    break;
                case 3:
                    closeParams.leftMargin = CBUtility.dpToPixels(10, getContext());
                    closeParams.bottomMargin = CBUtility.dpToPixels((CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsHeaderHeight - CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsCloseButtonHeight) / 2, getContext());
                    closeParams.addRule(12);
                    break;
                case 4:
                    closeParams.topMargin = CBUtility.dpToPixels(10, getContext());
                    closeParams.leftMargin = CBUtility.dpToPixels((CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsHeaderHeight - CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsCloseButtonHeight) / 2, getContext());
                    break;
                default:
                    closeParams.rightMargin = CBUtility.dpToPixels(10, getContext());
                    closeParams.topMargin = CBUtility.dpToPixels((CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsHeaderHeight - CBNativeMoreAppsViewProtocol.kCBNativeMoreAppsCloseButtonHeight) / 2, getContext());
                    closeParams.addRule(11);
                    break;
            }
            tableParams.width = -1;
            tableParams.height = -1;
            switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[dif.ordinal()]) {
                case 2:
                    tableParams.addRule(0, this.titleFrame.getId());
                    break;
                case 3:
                    tableParams.addRule(2, this.titleFrame.getId());
                    break;
                case 4:
                    tableParams.addRule(1, this.titleFrame.getId());
                    break;
                default:
                    tableParams.addRule(3, this.titleFrame.getId());
                    break;
            }
            this.tableView.setOrientation(dif.isOdd() ? 0 : 1);
            assignID(this.tableView.getView());
            this.tableView.setAdapter(this.adapter);
            addView(this.tableView.getView(), tableParams);
            if (dif == CBConstants.CBOrientation.Difference.ANGLE_180) {
                this.tableView.getContainer().setGravity(80);
            } else if (dif == CBConstants.CBOrientation.Difference.ANGLE_90) {
                this.tableView.getContainer().setGravity(5);
            } else {
                this.tableView.getContainer().setGravity(0);
            }
            this.titleFrame.setLayoutParams(titleBGParams);
            this.titleView.setLayoutParams(titleParams);
            this.titleView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            this.closeFrame.setLayoutParams(closeParams);
            this.closeButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            post(new Runnable() { // from class: com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.CBNativeMoreAppsView.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        CBNativeMoreAppsView.this.ignore = true;
                        CBNativeMoreAppsView.this.requestLayout();
                        CBNativeMoreAppsView.this.tableView.getView().requestLayout();
                        CBNativeMoreAppsView.this.tableView.getContainer().requestLayout();
                        CBNativeMoreAppsView.this.ignore = false;
                        if (dif == CBConstants.CBOrientation.Difference.ANGLE_180 || dif == CBConstants.CBOrientation.Difference.ANGLE_90) {
                            CBNativeMoreAppsView.this.tableView.scrollToEnd();
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }

        @Override // com.chartboost.sdk.View.CBViewProtocol.CBViewBase
        public void destroy() {
            super.destroy();
            this.closeButton = null;
            this.titleView = null;
            this.tableView = null;
        }

        /* loaded from: classes.dex */
        public class MoreAppsAdapter extends ArrayAdapter<JSONObject> {
            Context context;

            public MoreAppsAdapter(Context context) {
                super(context, 0, CBNativeMoreAppsViewProtocol.this.cells);
                this.context = context;
            }

            @Override // android.widget.ArrayAdapter, android.widget.Adapter
            public View getView(int position, View convertView, ViewGroup parent) {
                CBRotatableContainer container;
                CBConstants.CBOrientation.Difference dif = Chartboost.sharedChartboost().getForcedOrientationDifference();
                if (dif.isReverse()) {
                    position = (getCount() - 1) - position;
                }
                final JSONObject cellMeta = getItem(position);
                String type = cellMeta.optString(Constants.PUSH_TYPE, StringUtils.EMPTY);
                MoreAppsCellInterface moreAppCellView = null;
                if (convertView == null) {
                    if (type.equals("featured")) {
                        moreAppCellView = new CBNativeMoreAppsFeaturedCell(this.context);
                    } else if (type.equals("regular")) {
                        moreAppCellView = new CBNativeMoreAppsRegularCell(this.context);
                    } else if (type.equals("webview")) {
                        moreAppCellView = new CBNativeMoreAppsWebViewCell(this.context);
                    }
                    container = new CBRotatableContainer(this.context, (View) moreAppCellView);
                } else {
                    container = (CBRotatableContainer) convertView;
                    moreAppCellView = (MoreAppsCellInterface) container.getContentView();
                }
                moreAppCellView.prepareWithCellMeta(cellMeta, position);
                CBNativeMoreAppsCell appCellView = (CBNativeMoreAppsCell) moreAppCellView;
                if (dif.isOdd()) {
                    container.setLayoutParams(new AbsListView.LayoutParams(moreAppCellView.height(), -1));
                } else {
                    container.setLayoutParams(new AbsListView.LayoutParams(-1, moreAppCellView.height()));
                }
                View.OnClickListener clickListener = new View.OnClickListener() { // from class: com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.CBNativeMoreAppsView.MoreAppsAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        String url = cellMeta.optString("deep-link");
                        if (url == null || url.equals(StringUtils.EMPTY)) {
                            url = cellMeta.optString("link");
                        }
                        if (CBNativeMoreAppsViewProtocol.this.clickCallback != null) {
                            CBNativeMoreAppsViewProtocol.this.clickCallback.execute(url, cellMeta);
                        }
                    }
                };
                appCellView.clickListener = clickListener;
                appCellView.setOnClickListener(clickListener);
                if (moreAppCellView instanceof CBNativeMoreAppsRegularCell) {
                    ((CBNativeMoreAppsRegularCell) moreAppCellView).clickButton.setOnClickListener(clickListener);
                }
                return container;
            }

            @Override // android.widget.ArrayAdapter, android.widget.Adapter
            public int getCount() {
                int size = CBNativeMoreAppsViewProtocol.this.cells.size();
                return size;
            }

            @Override // android.widget.ArrayAdapter, android.widget.Adapter
            public JSONObject getItem(int position) {
                return (JSONObject) CBNativeMoreAppsViewProtocol.this.cells.get(position);
            }
        }
    }

    public CBNativeMoreAppsViewProtocol(CBImpression impression) {
        super(impression);
        this.expectedImagesCount = 3;
        this.cells = new ArrayList();
    }

    @Override // com.chartboost.sdk.View.CBViewProtocol
    protected CBViewProtocol.CBViewBase createViewObject(Context context) {
        return new CBNativeMoreAppsView(this, context, null);
    }

    @Override // com.chartboost.sdk.View.CBViewProtocol
    public void prepareWithResponse(JSONObject response) {
        super.prepareWithResponse(response);
        JSONArray cellsMeta = response.optJSONArray("cells");
        if (cellsMeta == null) {
            if (this.failCallback != null) {
                this.failCallback.execute();
                return;
            }
            return;
        }
        this.iconImages = new SparseArray<>();
        CBWebImageCache.CBWebImageProtocol cbIcons = new CBWebImageCache.CBWebImageProtocol() { // from class: com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.1
            @Override // com.chartboost.sdk.Libraries.CBWebImageCache.CBWebImageProtocol
            public void execute(Bitmap bitmap, Bundle bundle) {
                CBNativeMoreAppsViewProtocol.this.iconImages.put(bundle.getInt("index"), bitmap);
                CBNativeMoreAppsViewProtocol.this.onBitmapLoaded(bitmap);
            }
        };
        for (int i = 0; i < cellsMeta.length(); i++) {
            JSONObject cellMeta = cellsMeta.optJSONObject(i);
            this.cells.add(cellMeta);
            String type = cellMeta.optString(Constants.PUSH_TYPE, StringUtils.EMPTY);
            if (type.equals("regular")) {
                JSONObject assets = cellMeta.optJSONObject("assets");
                if (assets != null) {
                    this.expectedImagesCount++;
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", i);
                    PROCESS_LOADING_ASSET(assets, Constants.ICON, cbIcons, bundle);
                }
            } else if (type.equals("featured")) {
                JSONObject assets2 = cellMeta.optJSONObject("assets");
                if (assets2 != null) {
                    this.expectedImagesCount++;
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("index", i);
                    PROCESS_LOADING_ASSET(assets2, "portrait", cbIcons, bundle2);
                    this.expectedImagesCount++;
                    Bundle bundle22 = new Bundle();
                    bundle22.putInt("index", i);
                    PROCESS_LOADING_ASSET(assets2, "landscape", cbIcons, bundle22);
                }
            } else {
                type.equals("webview");
            }
        }
        CBWebImageCache.CBWebImageProtocol cb1 = new CBWebImageCache.CBWebImageProtocol() { // from class: com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.2
            @Override // com.chartboost.sdk.Libraries.CBWebImageCache.CBWebImageProtocol
            public void execute(Bitmap bitmap, Bundle bundle3) {
                CBNativeMoreAppsViewProtocol.this.closeImage = bitmap;
                CBNativeMoreAppsViewProtocol.this.onBitmapLoaded(bitmap);
            }
        };
        CBWebImageCache.CBWebImageProtocol cb2 = new CBWebImageCache.CBWebImageProtocol() { // from class: com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.3
            @Override // com.chartboost.sdk.Libraries.CBWebImageCache.CBWebImageProtocol
            public void execute(Bitmap bitmap, Bundle bundle3) {
                CBNativeMoreAppsViewProtocol.this.headerImage = bitmap;
                CBNativeMoreAppsViewProtocol.this.onBitmapLoaded(bitmap);
            }
        };
        CBWebImageCache.CBWebImageProtocol cb3 = new CBWebImageCache.CBWebImageProtocol() { // from class: com.chartboost.sdk.NativeViews.CBNativeMoreAppsViewProtocol.4
            @Override // com.chartboost.sdk.Libraries.CBWebImageCache.CBWebImageProtocol
            public void execute(Bitmap bitmap, Bundle bundle3) {
                CBNativeMoreAppsViewProtocol.this.headerTileImage = bitmap;
                CBNativeMoreAppsViewProtocol.this.onBitmapLoaded(bitmap);
            }
        };
        PROCESS_LOADING_ASSET("close", cb1);
        PROCESS_LOADING_ASSET("header-center", cb2);
        PROCESS_LOADING_ASSET("header-tile", cb3);
    }

    @Override // com.chartboost.sdk.View.CBViewProtocol
    public void destroy() {
        super.destroy();
        this.cells = null;
        this.closeImage = null;
        this.headerTileImage = null;
        this.headerImage = null;
    }
}
