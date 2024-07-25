package com.mappn.sdk.statitistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class GfanPayNetworkWatcher {
    private static GfanPayNetworkWatcher a;
    private Context b;
    private boolean c;
    private boolean d;
    private NetworkWatcherCallback e;
    private BroadcastReceiver f = new p(this);

    /* loaded from: classes.dex */
    public interface NetworkWatcherCallback {
        void onConnected();

        void onDisconnected();
    }

    private GfanPayNetworkWatcher(Context context, NetworkWatcherCallback networkWatcherCallback) {
        this.b = context.getApplicationContext();
        this.e = networkWatcherCallback;
        this.b.registerReceiver(this.f, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        a(context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Context context) {
        NetworkInfo activeNetworkInfo = context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0 ? ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo() : null;
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            this.c = false;
            this.d = false;
            if (this.e != null) {
                this.e.onDisconnected();
            }
            new String[1][0] = "checkWifiConnected onDisconnected network";
            GfanPayTCLog.a();
            return;
        }
        this.c = true;
        if (activeNetworkInfo.getType() == 1) {
            this.d = true;
        } else {
            this.d = false;
        }
        if (this.e != null) {
            this.e.onConnected();
        }
        new String[1][0] = "checkWifiConnected onConnected network";
        GfanPayTCLog.a();
    }

    public static void a(Context context, NetworkWatcherCallback networkWatcherCallback) {
        if (a == null) {
            a = new GfanPayNetworkWatcher(context, networkWatcherCallback);
        }
    }

    public static boolean a() {
        if (a == null) {
            return false;
        }
        return a.d;
    }

    public static boolean b() {
        if (a == null) {
            return false;
        }
        return a.c;
    }
}
