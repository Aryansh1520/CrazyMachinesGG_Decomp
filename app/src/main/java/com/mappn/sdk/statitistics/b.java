package com.mappn.sdk.statitistics;

import android.content.Context;
import android.os.Bundle;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class b implements Runnable {
    private /* synthetic */ Context a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(Context context) {
        this.a = context;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        try {
            Bundle bundle = this.a.getPackageManager().getApplicationInfo(this.a.getPackageName(), 128).metaData;
            String unused = GfanPayAgent.g = GfanPayAgent.a(bundle, "gfan_pay_appkey");
            GfanPayAgent.a = GfanPayAgent.a(bundle, "gfan_cpid");
        } catch (Throwable th) {
        }
        if (GfanPayAgent.a == null) {
            GfanPayAgent.a = "gfan.com";
        }
        Context context = this.a;
        str = GfanPayAgent.g;
        GfanPayAgent.init(context, str, GfanPayAgent.a);
    }
}
