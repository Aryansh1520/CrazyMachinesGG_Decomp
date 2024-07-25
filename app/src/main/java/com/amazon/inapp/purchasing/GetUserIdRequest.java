package com.amazon.inapp.purchasing;

/* loaded from: classes.dex */
final class GetUserIdRequest extends Request {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.amazon.inapp.purchasing.Request
    public Runnable getRunnable() {
        return new Runnable() { // from class: com.amazon.inapp.purchasing.GetUserIdRequest.1
            @Override // java.lang.Runnable
            public void run() {
                ImplementationFactory.getRequestHandler().sendGetUserIdRequest(GetUserIdRequest.this.getRequestId());
            }
        };
    }
}
