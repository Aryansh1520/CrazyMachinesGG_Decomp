package com.chartboost.sdk.Networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.chartboost.sdk.Chartboost;

/* loaded from: classes.dex */
public class CBReachability {
    public static boolean reachabilityForInternetConnection() {
        NetworkInfo netInfo;
        try {
            Context cx = Chartboost.sharedChartboost().getContext();
            ConnectivityManager cm = (ConnectivityManager) cx.getSystemService("connectivity");
            if (cm != null && (netInfo = cm.getActiveNetworkInfo()) != null) {
                if (netInfo.isConnectedOrConnecting()) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
