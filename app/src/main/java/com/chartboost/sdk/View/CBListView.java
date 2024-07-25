package com.chartboost.sdk.View;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class CBListView {
    private BaseAdapter adapter;
    private LinearLayout container;
    private List<List<View>> itemsUnused;
    private List<List<View>> itemsUsed;
    private DataSetObserver observer = new DataSetObserver() { // from class: com.chartboost.sdk.View.CBListView.1
        @Override // android.database.DataSetObserver
        public void onChanged() {
            LinearLayout.LayoutParams lp;
            int count = CBListView.this.container.getChildCount();
            for (int i = 0; i < count; i++) {
                List<View> viewPoolUsed = (List) CBListView.this.itemsUsed.get(((Integer) CBListView.this.oldViewTypes.get(i)).intValue());
                List<View> viewPoolUnused = (List) CBListView.this.itemsUnused.get(((Integer) CBListView.this.oldViewTypes.get(i)).intValue());
                View view = CBListView.this.container.getChildAt(i);
                viewPoolUsed.remove(view);
                viewPoolUnused.add(view);
            }
            CBListView.this.oldViewTypes.clear();
            CBListView.this.container.removeAllViews();
            int count2 = CBListView.this.adapter.getCount();
            for (int i2 = 0; i2 < count2; i2++) {
                int type = CBListView.this.adapter.getItemViewType(i2);
                List<View> viewPoolUsed2 = (List) CBListView.this.itemsUsed.get(type);
                List<View> viewPoolUnused2 = (List) CBListView.this.itemsUnused.get(type);
                CBListView.this.oldViewTypes.add(Integer.valueOf(type));
                View view2 = null;
                if (!viewPoolUnused2.isEmpty()) {
                    View view3 = viewPoolUnused2.get(0);
                    view2 = view3;
                    viewPoolUnused2.remove(0);
                }
                View view4 = CBListView.this.adapter.getView(i2, view2, CBListView.this.container);
                viewPoolUsed2.add(view4);
                if (CBListView.this.orientation == 0) {
                    lp = new LinearLayout.LayoutParams(-2, -1);
                } else {
                    lp = new LinearLayout.LayoutParams(-1, -2);
                }
                if (i2 < count2 - 1) {
                    lp.setMargins(0, 0, 0, 1);
                }
                CBListView.this.container.addView(view4, lp);
            }
            CBListView.this.container.requestLayout();
        }
    };
    private List<Integer> oldViewTypes;
    private int orientation;
    private ViewGroup scroller;
    private HorizontalScrollView scrollerH;
    private ScrollView scrollerV;

    public CBListView(Context context, int orientation) {
        this.container = new LinearLayout(context);
        this.orientation = orientation;
        this.container.setOrientation(orientation);
        if (orientation == 0) {
            this.scroller = createHorizontalScrollView(context);
        } else {
            this.scroller = createScrollView(context);
        }
        this.scroller.addView(this.container);
        this.itemsUnused = new ArrayList();
        this.itemsUsed = new ArrayList();
        this.oldViewTypes = new ArrayList();
        this.container.setOnTouchListener(new View.OnTouchListener() { // from class: com.chartboost.sdk.View.CBListView.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent ev) {
                try {
                    View focusView = ((Activity) CBListView.this.container.getContext()).getCurrentFocus();
                    if (focusView != null) {
                        InputMethodManager imm = (InputMethodManager) CBListView.this.container.getContext().getSystemService("input_method");
                        imm.hideSoftInputFromWindow(focusView.getApplicationWindowToken(), 0);
                    }
                } catch (Exception e) {
                }
                return false;
            }
        });
    }

    private HorizontalScrollView createHorizontalScrollView(Context context) {
        if (this.scrollerH == null) {
            this.scrollerH = new HorizontalScrollView(context);
            this.scrollerH.setFillViewport(true);
        }
        return this.scrollerH;
    }

    private ScrollView createScrollView(Context context) {
        if (this.scrollerV == null) {
            this.scrollerV = new ScrollView(context);
            this.scrollerV.setFillViewport(true);
        }
        return this.scrollerV;
    }

    public ViewGroup getView() {
        return this.scroller;
    }

    public void setAdapter(BaseAdapter a) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(this.observer);
        }
        this.adapter = a;
        this.adapter.registerDataSetObserver(this.observer);
        this.container.removeAllViews();
        this.itemsUnused.clear();
        this.itemsUsed.clear();
        for (int i = 0; i < this.adapter.getViewTypeCount(); i++) {
            this.itemsUnused.add(new ArrayList());
            this.itemsUsed.add(new ArrayList());
        }
        this.oldViewTypes.clear();
        this.adapter.notifyDataSetChanged();
    }

    public BaseAdapter getAdapter() {
        return this.adapter;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        if (orientation != this.orientation) {
            this.orientation = orientation;
            this.container.setOrientation(orientation);
            this.scroller.removeView(this.container);
            if (orientation == 0) {
                this.scroller = createHorizontalScrollView(getContext());
            } else {
                this.scroller = createScrollView(getContext());
            }
            this.scroller.addView(this.container);
        }
    }

    private Context getContext() {
        return this.container.getContext();
    }

    public LinearLayout getContainer() {
        return this.container;
    }

    public void scrollToEnd() {
        if (this.scroller == this.scrollerV && this.scrollerV != null) {
            this.scrollerV.fullScroll(130);
        } else if (this.scroller == this.scrollerH && this.scrollerH != null) {
            this.scrollerH.fullScroll(66);
        }
    }
}
