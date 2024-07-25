package com.mappn.sdk.pay.payment;

import com.mappn.sdk.common.utils.ToastUtil;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.User;

/* loaded from: classes.dex */
final class j implements GfanUCCallback {
    private /* synthetic */ i a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(i iVar) {
        this.a = iVar;
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onError(int i) {
        ToastUtil.showLong(this.a.a.getApplicationContext(), Constants.TEXT_COMPLETE_ACCOUNT_ERROR);
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onSuccess(User user, int i) {
        ToastUtil.showLong(this.a.a.getApplicationContext(), Constants.TEXT_COMPLETE_ACCOUNT_SUCCESS);
    }
}
