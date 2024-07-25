package com.mappn.sdk.pay.net.chain;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class d implements Runnable {
    final /* synthetic */ SyncChargeChannelHandler a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(SyncChargeChannelHandler syncChargeChannelHandler) {
        this.a = syncChargeChannelHandler;
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
        this.a.mHandler.post(new e(this));
    }
}
