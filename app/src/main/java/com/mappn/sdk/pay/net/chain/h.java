package com.mappn.sdk.pay.net.chain;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class h implements Runnable {
    final /* synthetic */ SyncSmsInfoHandler a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(SyncSmsInfoHandler syncSmsInfoHandler) {
        this.a = syncSmsInfoHandler;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Integer num;
        Integer num2;
        num = this.a.c;
        synchronized (num) {
            try {
                num2 = this.a.c;
                num2.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.a.mHandler.post(new i(this));
    }
}
