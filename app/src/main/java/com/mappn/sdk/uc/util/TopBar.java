package com.mappn.sdk.uc.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.mappn.sdk.common.utils.BaseUtils;

/* loaded from: classes.dex */
public class TopBar {
    public static void createTopBar(Context context, View[] viewArr, int[] iArr, String str) {
        int length = viewArr.length;
        BaseUtils.D("TopBar", "size : " + length);
        for (int i = 0; i < length; i++) {
            View view = viewArr[i];
            if (view == null) {
                BaseUtils.D("TopBar", "v " + i + " : 为null");
            } else {
                BaseUtils.D("TopBar", "v " + i + " : 不是null");
            }
            view.setVisibility(iArr[i]);
            if (8 != iArr[i] && view.getId() == BaseUtils.get_R_id(context, "top_bar_title")) {
                ((TextView) view).setText(str);
            }
        }
    }
}
