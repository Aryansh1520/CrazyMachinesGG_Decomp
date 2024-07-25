package com.amazon.inapp.purchasing;

import android.content.Context;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public abstract class PurchasingObserver {
    private WeakReference<Context> _contextReference;

    public PurchasingObserver(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Provided Context must not be null");
        }
        this._contextReference = new WeakReference<>(context.getApplicationContext());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Context getContext() {
        return this._contextReference.get();
    }

    public abstract void onGetUserIdResponse(GetUserIdResponse getUserIdResponse);

    public abstract void onItemDataResponse(ItemDataResponse itemDataResponse);

    public abstract void onPurchaseResponse(PurchaseResponse purchaseResponse);

    public abstract void onPurchaseUpdatesResponse(PurchaseUpdatesResponse purchaseUpdatesResponse);

    public abstract void onSdkAvailable(boolean z);
}
