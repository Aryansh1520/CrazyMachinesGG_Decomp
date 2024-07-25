package com.mappn.sdk.pay.chargement;

import android.view.View;
import java.util.Stack;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class n implements View.OnClickListener {
    private /* synthetic */ PhoneCardFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(PhoneCardFragment phoneCardFragment) {
        this.a = phoneCardFragment;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        ChargeActivity chargeActivity;
        ChargeActivity chargeActivity2;
        ChargeActivity chargeActivity3;
        chargeActivity = this.a.d;
        Stack stack = chargeActivity.mViewStacks;
        chargeActivity2 = this.a.d;
        stack.push(chargeActivity2.mType);
        chargeActivity3 = this.a.d;
        chargeActivity3.showTypeListView(false);
    }
}
