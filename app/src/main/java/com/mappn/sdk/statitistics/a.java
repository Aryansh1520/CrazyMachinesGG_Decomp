package com.mappn.sdk.statitistics;

import com.mappn.sdk.statitistics.GfanPayNetworkWatcher;

/* loaded from: classes.dex */
final class a implements GfanPayNetworkWatcher.NetworkWatcherCallback {
    @Override // com.mappn.sdk.statitistics.GfanPayNetworkWatcher.NetworkWatcherCallback
    public final void onConnected() {
        new String[1][0] = "NetworkWatcherCallback TCAgent onConnected....";
        GfanPayTCLog.a();
    }

    @Override // com.mappn.sdk.statitistics.GfanPayNetworkWatcher.NetworkWatcherCallback
    public final void onDisconnected() {
        new String[1][0] = "!!!! NetworkWatcherCallback TCAgent onDisconnected....";
        GfanPayTCLog.a();
    }
}
