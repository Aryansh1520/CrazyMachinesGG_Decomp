package com.mappn.sdk.pay.payment;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.Utils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class c implements View.OnClickListener {
    private /* synthetic */ RelativeLayout a;
    private /* synthetic */ TextView b;
    private /* synthetic */ PaymentViews c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(PaymentViews paymentViews, RelativeLayout relativeLayout, TextView textView) {
        this.c = paymentViews;
        this.a = relativeLayout;
        this.b = textView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        PaymentsActivity paymentsActivity;
        PaymentsActivity paymentsActivity2;
        this.a.setVisibility(8);
        paymentsActivity = this.c.b;
        Resources resources = paymentsActivity.getResources();
        paymentsActivity2 = this.c.b;
        Drawable drawable = resources.getDrawable(BaseUtils.get_R_Drawable(paymentsActivity2, Utils.isHdpi() ? Constants.RES_LIST_ITEM_DOWN_ICON_HDPI : Constants.RES_LIST_ITEM_DOWN_ICON));
        this.b.setCompoundDrawablePadding(6);
        this.b.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, drawable, (Drawable) null);
    }
}
