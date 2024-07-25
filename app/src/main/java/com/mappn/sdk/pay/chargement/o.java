package com.mappn.sdk.pay.chargement;

import android.text.method.NumberKeyListener;

/* loaded from: classes.dex */
final class o extends NumberKeyListener {
    /* JADX INFO: Access modifiers changed from: package-private */
    public o(PhoneCardFragment phoneCardFragment) {
    }

    @Override // android.text.method.NumberKeyListener
    protected final char[] getAcceptedChars() {
        return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    }

    @Override // android.text.method.KeyListener
    public final int getInputType() {
        return 2;
    }
}
