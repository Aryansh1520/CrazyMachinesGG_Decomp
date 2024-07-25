package com.mappn.sdk.statitistics;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class f implements Runnable {
    private /* synthetic */ Context a;
    private /* synthetic */ String b;
    private /* synthetic */ Map c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(Context context, String str, Map map) {
        this.a = context;
        this.b = str;
        this.c = map;
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
            iVar.b = "TCMap";
            iVar.f = this.c;
            Handler b = s.b();
            b.sendMessage(Message.obtain(b, 3, iVar));
        } catch (Throwable th) {
        }
    }
}
