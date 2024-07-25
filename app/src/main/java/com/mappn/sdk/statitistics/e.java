package com.mappn.sdk.statitistics;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class e implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ String b;
    private /* synthetic */ String c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(Context context, String str, String str2) {
        this.a = context;
        this.b = str;
        this.c = str2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        String str;
        try {
            z = GfanPayAgent.f;
            if (!z) {
                GfanPayAgent.init(this.a);
            }
            str = GfanPayAgent.h;
            if (TextUtils.isEmpty(str)) {
                new String[1][0] = "Not Found Session Id";
                GfanPayTCLog.a();
                return;
            }
            i iVar = new i();
            iVar.a = this.b;
            if (TextUtils.isEmpty(this.c)) {
                iVar.b = "TCLabel";
            } else {
                iVar.b = this.c;
            }
            Handler b = s.b();
            b.sendMessage(Message.obtain(b, 3, iVar));
        } catch (Throwable th) {
        }
    }
}
