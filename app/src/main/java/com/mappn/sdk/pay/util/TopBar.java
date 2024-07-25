package com.mappn.sdk.pay.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mappn.sdk.common.utils.BaseUtils;

/* loaded from: classes.dex */
public class TopBar {
    public static void createTopBar(Context context, View[] viewArr, int[] iArr, String str) {
        int length = viewArr.length;
        for (int i = 0; i < length; i++) {
            View view = viewArr[i];
            view.setVisibility(iArr[i]);
            if (8 != iArr[i]) {
                if (view.getId() == BaseUtils.get_R_id(context, "top_bar_pay_title")) {
                    ((TextView) view).setText(str);
                } else if (view.getId() == BaseUtils.get_R_id(context, "top_bar_pay_img")) {
                    ((ImageView) view).setImageDrawable(context.getResources().getDrawable(BaseUtils.get_R_Drawable(context, !Utils.isHdpi() ? Constants.RES_GFAN_LOGO_HALF : Constants.RES_GFAN_LOGO_HALF_HDPI)));
                }
            }
        }
    }
}
