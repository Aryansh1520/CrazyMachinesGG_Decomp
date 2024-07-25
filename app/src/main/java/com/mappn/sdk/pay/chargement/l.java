package com.mappn.sdk.pay.chargement;

import com.mappn.sdk.common.utils.ToastUtil;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.User;

/* loaded from: classes.dex */
final class l implements GfanUCCallback {
    private /* synthetic */ k a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(k kVar) {
        this.a = kVar;
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
