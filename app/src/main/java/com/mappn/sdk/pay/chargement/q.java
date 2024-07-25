package com.mappn.sdk.pay.chargement;

import android.content.DialogInterface;

/* loaded from: classes.dex */
final class q implements DialogInterface.OnClickListener {
    private /* synthetic */ PhoneCardFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public q(PhoneCardFragment phoneCardFragment) {
        this.a = phoneCardFragment;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        this.a.mCheckedId = i;
    }
}
