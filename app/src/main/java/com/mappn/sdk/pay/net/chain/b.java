package com.mappn.sdk.pay.net.chain;

/* loaded from: classes.dex */
final class b implements Runnable {
    private /* synthetic */ a a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(a aVar) {
        this.a = aVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.a.handleRequest();
    }
}
