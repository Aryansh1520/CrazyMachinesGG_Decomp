package com.mappn.sdk.pay.weight;

import android.app.Activity;
import android.view.View;

/* loaded from: classes.dex */
public abstract class AbsFragment {
    protected Activity mActivity;
    protected View mContentView;

    protected abstract View buildView();

    public void init(Activity activity) {
        this.mActivity = activity;
        this.mContentView = buildView();
        if (this.mContentView == null) {
            new NullPointerException("buildView() 方法不能返回为空间!!");
        }
    }

    public void showView() {
        if (this.mActivity == null) {
            new NullPointerException("实例化对象后,init(T activity) 方法没有调用.");
        }
        this.mActivity.setContentView(this.mContentView);
    }
}
