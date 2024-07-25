package com.xiaomi.gamecenter.sdk;

import android.app.AlertDialog;

/* loaded from: classes.dex */
class l implements Runnable {
    final /* synthetic */ AlertDialog.Builder a;
    final /* synthetic */ MiCommplatform b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(MiCommplatform miCommplatform, AlertDialog.Builder builder) {
        this.b = miCommplatform;
        this.a = builder;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.a.show();
    }
}
