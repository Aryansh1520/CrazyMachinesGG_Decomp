package com.xiaomi.gamecenter.sdk;

import android.content.DialogInterface;
import android.os.Process;

/* loaded from: classes.dex */
class k implements DialogInterface.OnClickListener {
    final /* synthetic */ MiCommplatform a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(MiCommplatform miCommplatform) {
        this.a = miCommplatform;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        Process.killProcess(Process.myPid());
    }
}
