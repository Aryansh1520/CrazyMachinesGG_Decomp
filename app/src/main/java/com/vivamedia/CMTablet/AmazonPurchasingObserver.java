package com.vivamedia.CMTablet;

import android.app.Activity;
import com.amazon.inapp.purchasing.BasePurchasingObserver;
import com.amazon.inapp.purchasing.Item;
import com.amazon.inapp.purchasing.ItemDataResponse;
import com.amazon.inapp.purchasing.PurchaseResponse;
import com.amazon.inapp.purchasing.PurchaseUpdatesResponse;
import com.amazon.inapp.purchasing.PurchasingManager;
import com.amazon.inapp.purchasing.Receipt;

/* loaded from: classes.dex */
public class AmazonPurchasingObserver extends BasePurchasingObserver {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$inapp$purchasing$Item$ItemType;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$inapp$purchasing$ItemDataResponse$ItemDataRequestStatus;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseResponse$PurchaseRequestStatus;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseUpdatesResponse$PurchaseUpdatesRequestStatus;

    static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$inapp$purchasing$Item$ItemType() {
        int[] iArr = $SWITCH_TABLE$com$amazon$inapp$purchasing$Item$ItemType;
        if (iArr == null) {
            iArr = new int[Item.ItemType.values().length];
            try {
                iArr[Item.ItemType.CONSUMABLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Item.ItemType.ENTITLED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Item.ItemType.SUBSCRIPTION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$amazon$inapp$purchasing$Item$ItemType = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$inapp$purchasing$ItemDataResponse$ItemDataRequestStatus() {
        int[] iArr = $SWITCH_TABLE$com$amazon$inapp$purchasing$ItemDataResponse$ItemDataRequestStatus;
        if (iArr == null) {
            iArr = new int[ItemDataResponse.ItemDataRequestStatus.values().length];
            try {
                iArr[ItemDataResponse.ItemDataRequestStatus.FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ItemDataResponse.ItemDataRequestStatus.SUCCESSFUL.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ItemDataResponse.ItemDataRequestStatus.SUCCESSFUL_WITH_UNAVAILABLE_SKUS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$amazon$inapp$purchasing$ItemDataResponse$ItemDataRequestStatus = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseResponse$PurchaseRequestStatus() {
        int[] iArr = $SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseResponse$PurchaseRequestStatus;
        if (iArr == null) {
            iArr = new int[PurchaseResponse.PurchaseRequestStatus.values().length];
            try {
                iArr[PurchaseResponse.PurchaseRequestStatus.ALREADY_ENTITLED.ordinal()] = 4;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PurchaseResponse.PurchaseRequestStatus.FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PurchaseResponse.PurchaseRequestStatus.INVALID_SKU.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PurchaseResponse.PurchaseRequestStatus.SUCCESSFUL.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseResponse$PurchaseRequestStatus = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseUpdatesResponse$PurchaseUpdatesRequestStatus() {
        int[] iArr = $SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseUpdatesResponse$PurchaseUpdatesRequestStatus;
        if (iArr == null) {
            iArr = new int[PurchaseUpdatesResponse.PurchaseUpdatesRequestStatus.values().length];
            try {
                iArr[PurchaseUpdatesResponse.PurchaseUpdatesRequestStatus.FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PurchaseUpdatesResponse.PurchaseUpdatesRequestStatus.SUCCESSFUL.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseUpdatesResponse$PurchaseUpdatesRequestStatus = iArr;
        }
        return iArr;
    }

    public AmazonPurchasingObserver(Activity activity) {
        super(activity);
    }

    @Override // com.amazon.inapp.purchasing.BasePurchasingObserver, com.amazon.inapp.purchasing.PurchasingObserver
    public void onSdkAvailable(boolean isSandboxMode) {
    }

    @Override // com.amazon.inapp.purchasing.BasePurchasingObserver, com.amazon.inapp.purchasing.PurchasingObserver
    public void onItemDataResponse(ItemDataResponse itemDataResponse) {
        switch ($SWITCH_TABLE$com$amazon$inapp$purchasing$ItemDataResponse$ItemDataRequestStatus()[itemDataResponse.getItemDataRequestStatus().ordinal()]) {
            case 1:
            case 2:
            case 3:
            default:
                return;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0028, code lost:
    
        com.vivamedia.CMTablet.MainActivity.onPurchaseDoneNative(r0.getSku(), 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:?, code lost:
    
        return;
     */
    @Override // com.amazon.inapp.purchasing.BasePurchasingObserver, com.amazon.inapp.purchasing.PurchasingObserver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onPurchaseResponse(com.amazon.inapp.purchasing.PurchaseResponse r5) {
        /*
            r4 = this;
            r3 = 2
            int[] r1 = $SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseResponse$PurchaseRequestStatus()
            com.amazon.inapp.purchasing.PurchaseResponse$PurchaseRequestStatus r2 = r5.getPurchaseRequestStatus()
            int r2 = r2.ordinal()
            r1 = r1[r2]
            switch(r1) {
                case 1: goto L13;
                case 2: goto L31;
                case 3: goto L37;
                default: goto L12;
            }
        L12:
            return
        L13:
            com.amazon.inapp.purchasing.Receipt r0 = r5.getReceipt()
            int[] r1 = $SWITCH_TABLE$com$amazon$inapp$purchasing$Item$ItemType()
            com.amazon.inapp.purchasing.Item$ItemType r2 = r0.getItemType()
            int r2 = r2.ordinal()
            r1 = r1[r2]
            switch(r1) {
                case 1: goto L28;
                default: goto L28;
            }
        L28:
            java.lang.String r1 = r0.getSku()
            r2 = 0
            com.vivamedia.CMTablet.MainActivity.onPurchaseDoneNative(r1, r2)
            goto L12
        L31:
            java.lang.String r1 = ""
            com.vivamedia.CMTablet.MainActivity.onPurchaseDoneNative(r1, r3)
            goto L12
        L37:
            java.lang.String r1 = ""
            com.vivamedia.CMTablet.MainActivity.onPurchaseDoneNative(r1, r3)
            goto L12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivamedia.CMTablet.AmazonPurchasingObserver.onPurchaseResponse(com.amazon.inapp.purchasing.PurchaseResponse):void");
    }

    @Override // com.amazon.inapp.purchasing.BasePurchasingObserver, com.amazon.inapp.purchasing.PurchasingObserver
    public void onPurchaseUpdatesResponse(PurchaseUpdatesResponse purchaseUpdatesResponse) {
        switch ($SWITCH_TABLE$com$amazon$inapp$purchasing$PurchaseUpdatesResponse$PurchaseUpdatesRequestStatus()[purchaseUpdatesResponse.getPurchaseUpdatesRequestStatus().ordinal()]) {
            case 1:
                for (Receipt receipt : purchaseUpdatesResponse.getReceipts()) {
                    MainActivity.onPurchaseDoneNative(receipt.getSku(), 0);
                }
                break;
        }
        if (purchaseUpdatesResponse.isMore()) {
            PurchasingManager.initiatePurchaseUpdatesRequest(purchaseUpdatesResponse.getOffset());
        }
    }
}
