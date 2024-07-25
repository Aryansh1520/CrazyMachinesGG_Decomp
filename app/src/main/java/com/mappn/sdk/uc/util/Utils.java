package com.mappn.sdk.uc.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.DESUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Utils {
    public static boolean sDebug = false;

    public static String getDebugType(Context context) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            BaseUtils.E("Utils", "getDebugType", e);
        }
        return applicationInfo.metaData != null ? applicationInfo.metaData.get("gfan_debug").toString() : null;
    }

    public static String getInstalledAppName(Context context, ApplicationInfo applicationInfo) {
        return context.getPackageManager().getApplicationLabel(applicationInfo).toString();
    }

    public static List<PackageInfo> getInstalledApps(Context context) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        List<PackageInfo> arrayList = new ArrayList<>();
        for (PackageInfo packageInfo : installedPackages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                arrayList.add(packageInfo);
            }
        }
        return arrayList;
    }

    public static List<String> getInstalledPackages(Context context) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        List<String> arrayList = new ArrayList<>();
        for (PackageInfo packageInfo : installedPackages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                arrayList.add(packageInfo.packageName);
            }
        }
        return arrayList;
    }

    public static String getStringResponse(HttpURLConnection connection, Context context) {
        StringBuilder response = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } catch (IOException e) {
            BaseUtils.D("Utils", "getStringResponse meet IOException", e);
            return null;
        }
        String entityUtils = response.toString();
        BaseUtils.D("Utils", "strResponse" + entityUtils);
        return !TextUtils.isEmpty(entityUtils) ? new DESUtil(context).getDesAndBase64String(entityUtils) : entityUtils;
    }

    // Method to create an HttpURLConnection and retrieve response
    public static String fetchUrlResponse(String urlString, Context context) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return getStringResponse(connection, context);
        } catch (IOException e) {
            BaseUtils.D("Utils", "fetchUrlResponse meet IOException", e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
