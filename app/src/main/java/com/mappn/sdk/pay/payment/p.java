package com.mappn.sdk.pay.payment;

import android.app.Activity;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.pay.payment.sms.SmsInfo;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.Utils;
import com.mokredit.payment.StringUtils;

/* loaded from: classes.dex */
final class p implements ApiRequestListener {
    private /* synthetic */ SmsPaymentFragment a;

    private p(SmsPaymentFragment smsPaymentFragment) {
        this.a = smsPaymentFragment;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ p(SmsPaymentFragment smsPaymentFragment, byte b) {
        this(smsPaymentFragment);
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public final void onError(int i, int i2) {
        Activity activity;
        Activity activity2;
        Activity activity3;
        SmsInfo smsInfo;
        SmsInfo smsInfo2;
        Activity activity4;
        Activity activity5;
        String cpID;
        Activity activity6;
        Activity activity7;
        switch (i) {
            case 8:
                long j = -1;
                activity = this.a.mActivity;
                if (((PaymentsActivity) activity).mPaymentInfo.getUser() != null) {
                    activity7 = this.a.mActivity;
                    j = ((PaymentsActivity) activity7).mPaymentInfo.getUser().getUid();
                }
                StringBuilder sb = new StringBuilder();
                activity2 = this.a.mActivity;
                StringBuilder append = sb.append(((PaymentsActivity) activity2).mPaymentInfo.getAppkey()).append(Constants.TERM);
                activity3 = this.a.mActivity;
                StringBuilder append2 = append.append(((PaymentsActivity) activity3).mPaymentInfo.getOrder().getPayName()).append(Constants.TERM);
                smsInfo = this.a.h;
                StringBuilder append3 = append2.append(smsInfo.money).append(Constants.TERM);
                smsInfo2 = this.a.h;
                StringBuilder append4 = append3.append(smsInfo2.getType()).append(Constants.TERM);
                activity4 = this.a.mActivity;
                if (((PaymentsActivity) activity4).mPaymentInfo.getCpID() == null) {
                    cpID = StringUtils.EMPTY;
                } else {
                    activity5 = this.a.mActivity;
                    cpID = ((PaymentsActivity) activity5).mPaymentInfo.getCpID();
                }
                String sb2 = append4.append(cpID).append(Constants.TERM).append(j).toString();
                activity6 = this.a.mActivity;
                Utils.writeSmsInfoPayment(activity6, sb2);
                return;
            default:
                return;
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public final void onSuccess(int i, Object obj) {
    }
}
