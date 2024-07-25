package com.mappn.sdk.pay.net.chain;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class a implements Runnable {
    final /* synthetic */ PostSmsPaymentHandler a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(PostSmsPaymentHandler postSmsPaymentHandler) {
        this.a = postSmsPaymentHandler;
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
        this.a.mHandler.post(new b(this));
    }
}
