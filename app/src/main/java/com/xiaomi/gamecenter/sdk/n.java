package com.xiaomi.gamecenter.sdk;

import android.content.DialogInterface;

/* loaded from: classes.dex */
class n implements DialogInterface.OnClickListener {
    final /* synthetic */ d a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(d dVar) {
        this.a = dVar;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        Object obj;
        Object obj2;
        this.a.b.service_isntall_ask = 1;
        obj = this.a.b._check_service_lock_;
        synchronized (obj) {
            obj2 = this.a.b._check_service_lock_;
            obj2.notifyAll();
        }
    }
}
