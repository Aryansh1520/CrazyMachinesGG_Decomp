package com.amazon.inapp.purchasing;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class PurchaseUpdatesRequest extends Request {
    private final Offset _offset;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PurchaseUpdatesRequest(Offset offset) {
        Validator.validateNotNull(offset, "offset");
        this._offset = offset;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.amazon.inapp.purchasing.Request
    public Runnable getRunnable() {
        return new Runnable() { // from class: com.amazon.inapp.purchasing.PurchaseUpdatesRequest.1
            @Override // java.lang.Runnable
            public void run() {
                ImplementationFactory.getRequestHandler().sendPurchaseUpdatesRequest(PurchaseUpdatesRequest.this._offset, PurchaseUpdatesRequest.this.getRequestId());
            }
        };
    }
}
