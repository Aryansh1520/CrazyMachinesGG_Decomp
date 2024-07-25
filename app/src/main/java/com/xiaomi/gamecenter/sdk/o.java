package com.xiaomi.gamecenter.sdk;

import android.content.DialogInterface;

/* loaded from: classes.dex */
class o implements DialogInterface.OnCancelListener {
    final /* synthetic */ d a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(d dVar) {
        this.a = dVar;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        Object obj;
        Object obj2;
        this.a.b.service_isntall_ask = 2;
        obj = this.a.b._check_service_lock_;
        synchronized (obj) {
            obj2 = this.a.b._check_service_lock_;
            obj2.notifyAll();
        }
    }
}
