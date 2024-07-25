package com.mappn.sdk.pay.payment;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class b implements View.OnClickListener {
    private /* synthetic */ RelativeLayout a;
    private /* synthetic */ TextView b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(PaymentViews paymentViews, RelativeLayout relativeLayout, TextView textView) {
        this.a = relativeLayout;
        this.b = textView;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.a.setVisibility(0);
        this.b.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
    }
}
