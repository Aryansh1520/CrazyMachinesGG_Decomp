package com.mappn.sdk.pay.net.chain;

/* loaded from: classes.dex */
final class i implements Runnable {
    private /* synthetic */ h a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(h hVar) {
        this.a = hVar;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.a.a.handleRequest();
    }
}
