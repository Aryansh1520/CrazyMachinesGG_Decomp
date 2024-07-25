package com.mappn.sdk.pay.net.chain;

/* loaded from: classes.dex */
final class e implements Runnable {
    private /* synthetic */ d a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(d dVar) {
        this.a = dVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.a.handleRequest();
    }
}
