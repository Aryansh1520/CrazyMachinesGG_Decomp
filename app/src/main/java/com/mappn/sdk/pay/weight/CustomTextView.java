package com.mappn.sdk.pay.weight;

import android.R;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.mappn.sdk.pay.util.Constants;

/* loaded from: classes.dex */
public class CustomTextView extends TextView {
    public CustomTextView(Context context, int i) {
        super(context);
        setMinHeight(53);
        setGravity(16);
        setPadding(6, 0, 0, 0);
        setTextAppearance(context, R.style.TextAppearance.Large.Inverse);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(View.PRESSED_ENABLED_STATE_SET, new ColorDrawable(Constants.COLOR_PRESSED));
        if (1 != i % 2) {
            stateListDrawable.addState(View.ENABLED_STATE_SET, new ColorDrawable(Constants.COLOR_LISTVIEW_ITEM_BACKGROUND));
        } else {
            stateListDrawable.addState(View.ENABLED_STATE_SET, new ColorDrawable(-1));
        }
        setBackgroundDrawable(stateListDrawable);
    }

    public CustomTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
