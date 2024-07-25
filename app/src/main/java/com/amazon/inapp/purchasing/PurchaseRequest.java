package com.amazon.inapp.purchasing;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class PurchaseRequest extends Request {
    private final String _sku;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurchaseRequest(String str) {
        Validator.validateNotNull(str, "sku");
        this._sku = str;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.amazon.inapp.purchasing.Request
    public Runnable getRunnable() {
        return new Runnable() { // from class: com.amazon.inapp.purchasing.PurchaseRequest.1
            @Override // java.lang.Runnable
            public void run() {
                ImplementationFactory.getRequestHandler().sendPurchaseRequest(PurchaseRequest.this._sku, PurchaseRequest.this.getRequestId());
            }
        };
    }
}
