package com.mappn.sdk.pay.chargement.alipay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.alipay.android.app.IRemoteServiceCallback;

/* loaded from: classes.dex */
final class g extends IRemoteServiceCallback.Stub {
    private /* synthetic */ MobileSecurePayer a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(MobileSecurePayer mobileSecurePayer) {
        this.a = mobileSecurePayer;
    }

    @Override // com.alipay.android.app.IRemoteServiceCallback
    public final void startActivity(String str, String str2, int i, Bundle bundle) {
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        if (bundle == null) {
            bundle = new Bundle();
        }
        try {
            bundle.putInt("CallingPid", i);
            intent.putExtras(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        intent.setClassName(str, str2);
        this.a.d.startActivity(intent);
    }
}
