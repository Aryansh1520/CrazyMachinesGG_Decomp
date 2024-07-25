package com.mappn.sdk.pay;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import com.mappn.sdk.pay.chargement.ChargeActivity;
import com.mappn.sdk.pay.model.Order;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.pay.payment.PaymentsActivity;
import com.mappn.sdk.uc.util.Constants;
import java.util.HashMap;

/* loaded from: classes.dex */
final class g extends Handler {
    private /* synthetic */ GfanPayService a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(GfanPayService gfanPayService) {
        this.a = gfanPayService;
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        String str;
        Messenger messenger;
        switch (message.what) {
            case 0:
                Message obtain = Message.obtain();
                obtain.what = 0;
                this.a.b = message.replyTo;
                try {
                    messenger = this.a.b;
                    messenger.send(obtain);
                    return;
                } catch (RemoteException e) {
                    str = GfanPayService.a;
                    Log.e(str, "get exception when send message", e);
                    return;
                }
            case 1:
                Order order = (Order) message.obj;
                Intent intent = new Intent(this.a.getApplicationContext(), (Class<?>) PaymentsActivity.class);
                intent.setFlags(268435456);
                intent.putExtra("com.mappn.sdk.order", order);
                intent.putExtra(PaymentsActivity.EXTRA_KEY_PAYMENTTYPE, PaymentInfo.PAYTYPE_OVERAGE);
                this.a.getApplicationContext().startActivity(intent);
                return;
            case 2:
                Intent intent2 = new Intent(this.a.getApplicationContext(), (Class<?>) ChargeActivity.class);
                intent2.setFlags(268435456);
                intent2.putExtra(ChargeActivity.EXTRA_CHARGE_ONLY, 1);
                intent2.putExtra(PaymentsActivity.EXTRA_KEY_PAYMENTTYPE, PaymentInfo.PAYTYPE_OVERAGE);
                this.a.getApplicationContext().startActivity(intent2);
                return;
            case 3:
                HashMap hashMap = (HashMap) message.obj;
                String str2 = (String) hashMap.get(Constants.PUSH_TYPE);
                this.a.getApplicationContext().startActivity(new Intent(this.a.getApplicationContext(), (Class<?>) ChargeActivity.class).setFlags(268435456).putExtra(ChargeActivity.EXTRA_TYPE, str2).putExtra(ChargeActivity.EXTRA_PAYMENT_INFO, (PaymentInfo) hashMap.get("paymentInfo")));
                return;
            default:
                super.handleMessage(message);
                return;
        }
    }
}
