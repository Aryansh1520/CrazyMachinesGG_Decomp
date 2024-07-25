package com.mappn.sdk.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.mappn.sdk.common.utils.BaseUtils;
import java.util.HashMap;

/* loaded from: classes.dex */
final class f extends BroadcastReceiver {
    private /* synthetic */ GfanPayService a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(GfanPayService gfanPayService) {
        this.a = gfanPayService;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        String str;
        String str2;
        Messenger messenger;
        String str3;
        Messenger messenger2;
        String str4;
        Messenger messenger3;
        String str5;
        Messenger messenger4;
        str = GfanPayService.a;
        BaseUtils.D(str, "onReceive");
        int intExtra = intent.getIntExtra(GfanPayService.EXTRA_KEY_TYPE, 0);
        Message obtain = Message.obtain();
        HashMap hashMap = new HashMap();
        switch (intExtra) {
            case 0:
                obtain.what = 1;
                hashMap.put("com.mappn.sdk.order", intent.getSerializableExtra("com.mappn.sdk.order"));
                hashMap.put(GfanPayService.EXTRA_KEY_USER, intent.getSerializableExtra(GfanPayService.EXTRA_KEY_USER));
                obtain.obj = hashMap;
                try {
                    messenger4 = this.a.b;
                    messenger4.send(obtain);
                    return;
                } catch (RemoteException e) {
                    str5 = GfanPayService.a;
                    BaseUtils.E(str5, "get exception when send message", e);
                    return;
                }
            case 1:
                obtain.what = 3;
                hashMap.put(GfanPayService.EXTRA_KEY_USER, intent.getSerializableExtra(GfanPayService.EXTRA_KEY_USER));
                obtain.obj = hashMap;
                try {
                    messenger2 = this.a.b;
                    messenger2.send(obtain);
                    return;
                } catch (RemoteException e2) {
                    str3 = GfanPayService.a;
                    BaseUtils.E(str3, "get exception when send message", e2);
                    return;
                }
            case 2:
                obtain.what = 2;
                hashMap.put(GfanPayService.EXTRA_KEY_USER, intent.getSerializableExtra(GfanPayService.EXTRA_KEY_USER));
                obtain.obj = hashMap;
                try {
                    messenger3 = this.a.b;
                    messenger3.send(obtain);
                    return;
                } catch (RemoteException e3) {
                    str4 = GfanPayService.a;
                    BaseUtils.E(str4, "get exception when send message", e3);
                    return;
                }
            case 3:
                obtain.what = 4;
                hashMap.put(GfanPayService.EXTRA_KEY_USER, intent.getSerializableExtra(GfanPayService.EXTRA_KEY_USER));
                obtain.obj = hashMap;
                try {
                    messenger = this.a.b;
                    messenger.send(obtain);
                    return;
                } catch (RemoteException e4) {
                    str2 = GfanPayService.a;
                    BaseUtils.E(str2, "get exception when send message", e4);
                    return;
                }
            default:
                return;
        }
    }
}
