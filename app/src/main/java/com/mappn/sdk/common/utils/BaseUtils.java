package com.mappn.sdk.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BaseUtils {
    private static WeakReference<Calendar> calendarRef;
    public static boolean sDebug = true;
    private static String appKey = "";

    public static void D(String tag, String msg) {
        if (sDebug && msg != null) {
            Log.d(tag, msg);
        }
    }

    public static void D(String tag, String msg, Throwable tr) {
        if (sDebug && msg != null) {
            Log.d(tag, msg, tr);
        }
    }

    public static void E(String tag, String msg) {
        if (sDebug && msg != null) {
            Log.e(tag, msg);
        }
    }

    public static void E(String tag, String msg, Throwable tr) {
        if (sDebug && msg != null) {
            Log.e(tag, msg, tr);
        }
    }

    public static void I(String tag, String msg) {
        if (sDebug && msg != null) {
            Log.i(tag, msg);
        }
    }

    public static void I(String tag, String msg, Throwable tr) {
        if (sDebug && msg != null) {
            Log.i(tag, msg, tr);
        }
    }

    public static void V(String tag, String msg) {
        if (sDebug && msg != null) {
            Log.v(tag, msg);
        }
    }

    public static void V(String tag, String msg, Throwable tr) {
        if (sDebug && msg != null) {
            Log.v(tag, msg, tr);
        }
    }

    public static void W(String tag, String msg) {
        if (sDebug && msg != null) {
            Log.w(tag, msg);
        }
    }

    public static void W(String tag, String msg, Throwable tr) {
        if (sDebug && msg != null) {
            Log.w(tag, msg, tr);
        }
    }

    public static void copyFile(InputStream inputStream, FileOutputStream fileOutputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        fileOutputStream.close();
    }

    public static String escapeSpecialCharForUrlSegments(String str) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, "UTF-8")
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
    }

    public static String formatDate(long timestamp) {
        if (calendarRef == null || calendarRef.get() == null) {
            calendarRef = new WeakReference<>(Calendar.getInstance());
        }
        Calendar calendar = calendarRef.get();
        calendar.setTimeInMillis(timestamp);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static String formatDateTime(long timestamp) {
        if (calendarRef == null || calendarRef.get() == null) {
            calendarRef = new WeakReference<>(Calendar.getInstance());
        }
        Calendar calendar = calendarRef.get();
        calendar.setTimeInMillis(timestamp);
        return new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
    }

    public static String formatTime(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timestamp));
    }

    public static Drawable getAppIcon(Context context) {
        try {
            return context.getPackageManager().getApplicationIcon(getPackageName(context));
        } catch (PackageManager.NameNotFoundException e) {
            E("BaseUtils", "getAppIcon", e);
            return null;
        }
    }

    public static String getAppKey(Context context) {
        Bundle metaDataInfo = getMetaDataInfo(context);
        if (metaDataInfo == null || metaDataInfo.get("gfan_pay_appkey") == null) {
            throw new IllegalArgumentException("AppKey must be set in metadata.");
        }
        return metaDataInfo.get("gfan_pay_appkey").toString();
    }

    public static String getAppName(Context context) {
        return context.getPackageManager().getApplicationLabel(context.getApplicationInfo()).toString();
    }

    public static String getCPID(Context context) {
        Bundle metaDataInfo = getMetaDataInfo(context);
        if (metaDataInfo == null || metaDataInfo.get("gfan_cpid") == null) {
            return null;
        }
        return metaDataInfo.get("gfan_cpid").toString();
    }

    public static float getFloat(String str) {
        try {
            return Float.parseFloat(str.trim());
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    public static String getGHeader(Context context) {
        return getModel() + "/" + Build.VERSION.RELEASE + "/" + getAppName(context) + "/" + getVersionName(context) + "/9/" + getIMEI(context) + "/" + getSim(context) + "/" + getMAC(context);
    }

    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getDeviceId() : null;
    }

    public static InputStream getInputStreamResponse(HttpURLConnection connection) {
        try {
            return connection.getInputStream();
        } catch (IOException e) {
            D("BaseUtils", "getInputStreamResponse error", e);
            return null;
        }
    }

    public static int getInt(String str) {
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long getLong(String str) {
        try {
            return Long.parseLong(str.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static String getMAC(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null ? wifiManager.getConnectionInfo().getMacAddress() : null;
    }

    public static Bundle getMetaDataInfo(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            E("BaseUtils", "getMetaDataInfo", e);
            return null;
        }
    }

    public static String getModel() {
        try {
            return URLEncoder.encode(Build.MODEL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            E("BaseUtils", "getModel", e);
            return Build.MODEL;
        }
    }

    public static String getPackageName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            E("BaseUtils", "getPackageName", e);
            return "";
        }
    }

    public static String getPayBroadcast(Context context) {
        if (TextUtils.isEmpty(appKey)) {
            appKey = "com.mappn.pay." + getPackageName(context);
        }
        D("PAYBROADCAST", appKey);
        return appKey;
    }

    public static String getSim(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSimSerialNumber() : null;
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            E("BaseUtils", "getVersionName", e);
            return "";
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            }
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }

    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean isWiFiAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    public static void saveToFile(String data, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data.getBytes());
        } catch (IOException e) {
            E("BaseUtils", "saveToFile", e);
        }
    }
}
