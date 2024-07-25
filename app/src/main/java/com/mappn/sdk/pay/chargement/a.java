package com.mappn.sdk.pay.chargement;

import android.view.View;
import java.util.Stack;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class a implements View.OnClickListener {
    private /* synthetic */ AlipayOrGFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(AlipayOrGFragment alipayOrGFragment) {
        this.a = alipayOrGFragment;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        ChargeActivity chargeActivity;
        ChargeActivity chargeActivity2;
        ChargeActivity chargeActivity3;
        chargeActivity = this.a.b;
        Stack stack = chargeActivity.mViewStacks;
        chargeActivity2 = this.a.b;
        stack.push(chargeActivity2.mType);
        chargeActivity3 = this.a.b;
        chargeActivity3.showTypeListView(false);
    }
}
