package com.mappn.sdk.statitistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes.dex */
final class p extends BroadcastReceiver {
    final /* synthetic */ GfanPayNetworkWatcher a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(GfanPayNetworkWatcher gfanPayNetworkWatcher) {
        this.a = gfanPayNetworkWatcher;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        GfanPaySingleThreadedExecutor.execute(new q(this, context));
    }
}
