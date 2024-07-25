package com.mappn.sdk.pay.net.chain;

/* loaded from: classes.dex */
final class g implements Runnable {
    private /* synthetic */ f a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(f fVar) {
        this.a = fVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.a.handleRequest();
    }
}
