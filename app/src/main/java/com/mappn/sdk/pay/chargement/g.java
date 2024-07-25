package com.mappn.sdk.pay.chargement;

import android.os.Handler;
import android.os.Message;
import com.mappn.sdk.pay.net.Api;
import com.mokredit.payment.StringUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class g extends Handler {
    private /* synthetic */ AlipayOrGFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(AlipayOrGFragment alipayOrGFragment) {
        this.a = alipayOrGFragment;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        ChargeActivity chargeActivity;
        ChargeActivity chargeActivity2;
        ChargeActivity chargeActivity3;
        ChargeActivity chargeActivity4;
        ChargeActivity chargeActivity5;
        ChargeActivity chargeActivity6;
        ChargeActivity chargeActivity7;
        ChargeActivity chargeActivity8;
        ChargeActivity chargeActivity9;
        ChargeActivity chargeActivity10;
        ChargeActivity chargeActivity11;
        String str = (String) message.obj;
        switch (message.what) {
            case 1:
                try {
                    int intValue = Integer.valueOf(str.split(";")[0].split("=")[1].replace("{", StringUtils.EMPTY).replace("}", StringUtils.EMPTY)).intValue();
                    if (6001 != intValue && 4000 != intValue && 6000 != intValue) {
                        chargeActivity9 = this.a.b;
                        chargeActivity9.mLastTime = System.currentTimeMillis();
                        chargeActivity10 = this.a.b;
                        AlipayOrGFragment alipayOrGFragment = this.a;
                        chargeActivity11 = this.a.b;
                        Api.queryAliPayResult(chargeActivity10, alipayOrGFragment, chargeActivity11.mPaymentInfo.getOrder().getOrderID());
                        break;
                    } else {
                        chargeActivity3 = this.a.b;
                        chargeActivity3.removeDialog(7);
                        chargeActivity4 = this.a.b;
                        if (1 != chargeActivity4.mChargeFlag) {
                            chargeActivity5 = this.a.b;
                            String userName = chargeActivity5.mPaymentInfo.getUser().getUserName();
                            chargeActivity6 = this.a.b;
                            if (chargeActivity6.mPaymentInfo.getUser().getUserName() == null && userName != null) {
                                chargeActivity8 = this.a.b;
                                chargeActivity8.mPaymentInfo.getUser().setUserName(userName);
                            }
                            chargeActivity7 = this.a.b;
                            chargeActivity7.finish();
                            break;
                        }
                    }
                } catch (Exception e) {
                    chargeActivity = this.a.b;
                    chargeActivity.removeDialog(7);
                    chargeActivity2 = this.a.b;
                    chargeActivity2.showDialog(10);
                    break;
                }
                break;
        }
        super.handleMessage(message);
    }
}
