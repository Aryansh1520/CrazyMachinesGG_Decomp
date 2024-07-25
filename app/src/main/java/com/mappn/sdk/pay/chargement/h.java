package com.mappn.sdk.pay.chargement;

import android.content.Intent;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.GfanPayService;
import com.mappn.sdk.pay.ServiceConnector;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.User;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class h implements GfanUCCallback {
    private /* synthetic */ ChargeActivity a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(ChargeActivity chargeActivity) {
        this.a = chargeActivity;
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onError(int i) {
        String str;
        if ("alipay".equals(this.a.mType) || TypeFactory.TYPE_CHARGE_G.equals(this.a.mType) || "mo9".equals(this.a.mType)) {
            if (ServiceConnector.getInstance(this.a.getApplicationContext()).getIsConnected()) {
                this.a.sendBroadcast(new Intent(BaseUtils.getPayBroadcast(this.a.getApplicationContext())).putExtra(GfanPayService.EXTRA_KEY_TYPE, 3).putExtra(GfanPayService.EXTRA_KEY_USER, this.a.mPaymentInfo.getUser()));
            } else {
                str = ChargeActivity.b;
                BaseUtils.D(str, "connection disconnect");
            }
            PrefUtil.setLoginFlag(this.a.getApplicationContext(), false);
            if (this.a.mViewStacks.size() > 0) {
                this.a.mViewStacks.pop();
            } else {
                this.a.c();
                this.a.finish();
            }
        }
    }

    @Override // com.mappn.sdk.uc.GfanUCCallback
    public final void onSuccess(User user, int i) {
        PrefUtil.setLoginFlag(this.a.getApplicationContext(), true);
        this.a.mPaymentInfo.setUser(user);
        this.a.showViewByType(this.a.mType);
    }
}
