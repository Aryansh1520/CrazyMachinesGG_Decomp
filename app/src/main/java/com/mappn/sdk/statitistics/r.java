package com.mappn.sdk.statitistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.mappn.sdk.common.utils.BaseConstants;
import com.mappn.sdk.pay.chargement.ChargeActivity;
import com.mappn.sdk.statitistics.entity.GfanPayInitProfile;
import com.mokredit.payment.StringUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bson.BSON;

/* loaded from: classes.dex */
final class r {
    private static TelephonyManager a;
    private static char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a() {
        return Build.VERSION.SDK;
    }

    public static synchronized String a(Context context) {
        String string;
        synchronized (r.class) {
            string = PreferenceManager.getDefaultSharedPreferences(context).getString("pref.deviceid.key", null);
            if (d(string)) {
                string = a("/tmp/.tid");
            }
            if (d(string)) {
                string = "mounted".equals(Environment.getExternalStorageState()) ? a(Environment.getExternalStorageDirectory() + "/.tid") : StringUtils.EMPTY;
            }
            if (d(string)) {
                string = m(context);
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                edit.putString("pref.deviceid.key", string);
                edit.commit();
                a("/tmp/.tid", string);
                a(Environment.getExternalStorageDirectory() + "/.tid", string);
            }
        }
        return string;
    }

    private static String a(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndex(str));
    }

    private static String a(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            byte[] bArr = new byte[128];
            int read = fileInputStream.read(bArr);
            fileInputStream.close();
            return new String(bArr, 0, read);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    private static void a(String str, String str2) {
        try {
            File file = new File(str);
            if (file.exists() && file.length() == str2.length()) {
                return;
            }
            file.getParentFile().mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(str2.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
        }
    }

    private static boolean a(Context context, String str) {
        return context.getPackageManager().checkPermission(str, context.getPackageName()) == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] a(byte[] bArr) {
        try {
            return MessageDigest.getInstance("MD5").digest(bArr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b() {
        return 0;
    }

    private static int b(String str) {
        try {
            String str2 = StringUtils.EMPTY;
            Matcher matcher = Pattern.compile("([0-9]+)").matcher(str);
            if (matcher.find()) {
                str2 = matcher.toMatchResult().group(0);
            }
            return Integer.valueOf(str2).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == -1) {
            return StringUtils.EMPTY;
        }
        l(context);
        return a.getNetworkOperator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(byte[] bArr) {
        int length = bArr.length;
        if (bArr == null) {
            return ChargeActivity.TYPE_CHARGE_TYPE_LIST;
        }
        int i = length + 0;
        if (i > bArr.length) {
            i = bArr.length;
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(b[(bArr[i2] & 240) >>> 4]).append(b[bArr[i2] & BSON.CODE_W_SCOPE]);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String c() {
        return Locale.getDefault().getCountry();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String c(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == -1) {
            return StringUtils.EMPTY;
        }
        l(context);
        return a.getSimOperator();
    }

    private static String c(String str) {
        String str2 = StringUtils.EMPTY;
        try {
            FileReader fileReader = new FileReader(str);
            try {
                char[] cArr = new char[1024];
                BufferedReader bufferedReader = new BufferedReader(fileReader, 1024);
                while (true) {
                    int read = bufferedReader.read(cArr, 0, 1024);
                    if (-1 == read) {
                        break;
                    }
                    str2 = str2 + new String(cArr, 0, read);
                }
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e) {
                new String[1][0] = "get file content IOException";
                GfanPayTCLog.a();
            }
        } catch (FileNotFoundException e2) {
            new String[1][0] = "file not found exception";
            GfanPayTCLog.a();
        }
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String d() {
        return Locale.getDefault().getLanguage();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String d(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == -1) {
            return StringUtils.EMPTY;
        }
        l(context);
        return a.getNetworkOperatorName();
    }

    private static boolean d(String str) {
        return str == null || StringUtils.EMPTY.equals(str.trim());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0058  */
    /* JADX WARN: Type inference failed for: r0v11, types: [android.location.LocationManager] */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v16 */
    /* JADX WARN: Type inference failed for: r0v18 */
    /* JADX WARN: Type inference failed for: r0v20 */
    /* JADX WARN: Type inference failed for: r0v21 */
    /* JADX WARN: Type inference failed for: r0v22 */
    /* JADX WARN: Type inference failed for: r0v23 */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v7, types: [android.location.Location] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.mappn.sdk.statitistics.entity.GfanPayGis e(android.content.Context r11) {
        /*
            r0 = 0
            r9 = 2
            r8 = 1
            r7 = 0
            com.mappn.sdk.statitistics.entity.GfanPayGis r2 = new com.mappn.sdk.statitistics.entity.GfanPayGis
            r2.<init>()
            r2.lat = r0
            r2.lng = r0
            r1 = 0
            java.lang.String r0 = "location"
            java.lang.Object r0 = r11.getSystemService(r0)     // Catch: java.lang.Exception -> Ldc
            android.location.LocationManager r0 = (android.location.LocationManager) r0     // Catch: java.lang.Exception -> Ldc
            java.lang.String r3 = "android.permission.ACCESS_FINE_LOCATION"
            boolean r3 = a(r11, r3)     // Catch: java.lang.Exception -> Ldc
            if (r3 == 0) goto L65
            java.lang.String r3 = "gps"
            android.location.Location r0 = r0.getLastKnownLocation(r3)     // Catch: java.lang.Exception -> Ldc
            if (r0 == 0) goto L56
            r1 = 2
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch: java.lang.Exception -> La5
            r3 = 0
            java.lang.String r4 = "TCAgent"
            r1[r3] = r4     // Catch: java.lang.Exception -> La5
            r3 = 1
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> La5
            java.lang.String r5 = "get location from gps:"
            r4.<init>(r5)     // Catch: java.lang.Exception -> La5
            double r5 = r0.getLatitude()     // Catch: java.lang.Exception -> La5
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> La5
            java.lang.String r5 = ","
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> La5
            double r5 = r0.getLongitude()     // Catch: java.lang.Exception -> La5
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> La5
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> La5
            r1[r3] = r4     // Catch: java.lang.Exception -> La5
            com.mappn.sdk.statitistics.GfanPayTCLog.a()     // Catch: java.lang.Exception -> La5
        L56:
            if (r0 == 0) goto L64
            double r3 = r0.getLatitude()
            r2.lat = r3
            double r0 = r0.getLongitude()
            r2.lng = r0
        L64:
            return r2
        L65:
            java.lang.String r3 = "android.permission.ACCESS_COARSE_LOCATION"
            boolean r3 = a(r11, r3)     // Catch: java.lang.Exception -> Ldc
            if (r3 == 0) goto Lc9
            java.lang.String r3 = "network"
            android.location.Location r0 = r0.getLastKnownLocation(r3)     // Catch: java.lang.Exception -> Ldc
            if (r0 == 0) goto L56
            r1 = 2
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch: java.lang.Exception -> La5
            r3 = 0
            java.lang.String r4 = "TCAgent"
            r1[r3] = r4     // Catch: java.lang.Exception -> La5
            r3 = 1
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> La5
            java.lang.String r5 = "get location from network:"
            r4.<init>(r5)     // Catch: java.lang.Exception -> La5
            double r5 = r0.getLatitude()     // Catch: java.lang.Exception -> La5
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> La5
            java.lang.String r5 = ","
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> La5
            double r5 = r0.getLongitude()     // Catch: java.lang.Exception -> La5
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> La5
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> La5
            r1[r3] = r4     // Catch: java.lang.Exception -> La5
            com.mappn.sdk.statitistics.GfanPayTCLog.a()     // Catch: java.lang.Exception -> La5
            goto L56
        La5:
            r1 = move-exception
            r10 = r1
            r1 = r0
            r0 = r10
        La9:
            java.lang.String[] r3 = new java.lang.String[r9]
            java.lang.String r4 = "TCAgent"
            r3[r7] = r4
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "getUserLocation"
            r4.<init>(r5)
            java.lang.String r0 = r0.getMessage()
            java.lang.StringBuilder r0 = r4.append(r0)
            java.lang.String r0 = r0.toString()
            r3[r8] = r0
            r0 = r1
            com.mappn.sdk.statitistics.GfanPayTCLog.a()
            goto L56
        Lc9:
            r0 = 2
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch: java.lang.Exception -> Ldc
            r3 = 0
            java.lang.String r4 = "TCAgent"
            r0[r3] = r4     // Catch: java.lang.Exception -> Ldc
            r3 = 1
            java.lang.String r4 = "Could not get location from GPS or Cell-id, lack ACCESS_COARSE_LOCATION or ACCESS_COARSE_LOCATION permission?"
            r0[r3] = r4     // Catch: java.lang.Exception -> Ldc
            r0 = r1
            com.mappn.sdk.statitistics.GfanPayTCLog.a()     // Catch: java.lang.Exception -> Ldc
            goto L56
        Ldc:
            r0 = move-exception
            goto La9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.statitistics.r.e(android.content.Context):com.mappn.sdk.statitistics.entity.GfanPayGis");
    }

    public static String e() {
        return StringUtils.EMPTY;
    }

    public static String f() {
        return StringUtils.EMPTY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String f(Context context) {
        try {
            return String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
        } catch (Exception e) {
            return "unknown";
        }
    }

    public static int g() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String g(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "unknown";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long h(Context context) {
        try {
            if (Integer.valueOf(Build.VERSION.SDK).intValue() < 9) {
                return -1L;
            }
            return i.a(context);
        } catch (Exception e) {
            return -1L;
        }
    }

    private static String[] h() {
        boolean z;
        String[] strArr = new String[4];
        for (int i = 0; i < 4; i++) {
            strArr[i] = StringUtils.EMPTY;
        }
        ArrayList arrayList = new ArrayList();
        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader, 1024);
            while (true) {
                try {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            arrayList.add(readLine);
                        } else {
                            try {
                                break;
                            } catch (FileNotFoundException e) {
                                z = true;
                                new String[1][0] = "file not found exception";
                                GfanPayTCLog.a();
                            } catch (IOException e2) {
                                z = true;
                            }
                        }
                    } catch (IOException e3) {
                        new String[1][0] = "get file content IOException";
                        GfanPayTCLog.a();
                        try {
                            z = false;
                        } catch (IOException e4) {
                            z = false;
                        }
                    }
                } finally {
                    try {
                        bufferedReader.close();
                        fileReader.close();
                    } catch (IOException e5) {
                    }
                }
            }
            bufferedReader.close();
            fileReader.close();
            z = true;
        } catch (FileNotFoundException e6) {
            z = false;
        }
        String[] strArr2 = {"Processor\\s*:\\s*(.*)", "CPU\\s*variant\\s*:\\s*0x(.*)", "CPU\\s*implementer\\s*:\\s*(.*)"};
        if (z) {
            int size = arrayList.size();
            for (int i2 = 0; i2 < 3; i2++) {
                Pattern compile = Pattern.compile(strArr2[i2]);
                for (int i3 = 0; i3 < size; i3++) {
                    Matcher matcher = compile.matcher((String) arrayList.get(i3));
                    if (matcher.find()) {
                        strArr[i2] = matcher.toMatchResult().group(1);
                    }
                }
            }
        }
        strArr[3] = c("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String i(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics != null ? displayMetrics.widthPixels + "*" + displayMetrics.heightPixels + "*" + displayMetrics.density : StringUtils.EMPTY;
    }

    private static int[] i() {
        int[] iArr = {0, 0};
        int[] iArr2 = new int[4];
        for (int i = 0; i < 4; i++) {
            iArr2[i] = 0;
        }
        try {
            FileReader fileReader = new FileReader("/proc/meminfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader, 1024);
            for (int i2 = 0; i2 < 4; i2++) {
                try {
                    try {
                        iArr2[i2] = b(bufferedReader.readLine());
                    } finally {
                        try {
                            bufferedReader.close();
                            fileReader.close();
                        } catch (IOException e) {
                        }
                    }
                } catch (IOException e2) {
                    new String[1][0] = "get file content IOException";
                    GfanPayTCLog.a();
                }
            }
            iArr[0] = iArr2[0];
            iArr[1] = iArr2[3] + iArr2[1] + iArr2[2];
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (IOException e3) {
            }
        } catch (FileNotFoundException e4) {
            new String[1][0] = "file not found exception";
            GfanPayTCLog.a();
        }
        return iArr;
    }

    private static int j() {
        Matcher matcher = Pattern.compile("\\s*([0-9]+)").matcher(c("/sys/class/power_supply/battery/full_bat"));
        if (!matcher.find()) {
            return 0;
        }
        try {
            return Integer.valueOf(matcher.toMatchResult().group(0)).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String j(Context context) {
        String[] strArr = {"UNKNOWN", "GPRS", "EDGE", "UMTS", "CDMA", "EVDO_0", "EVDO_A", "1xRTT", "HSDPA", "HSUPA", "HSPA", "IDEN", "EVDO_B", "LTE", "EHRPD", "HSPAP"};
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == -1) {
            return StringUtils.EMPTY;
        }
        l(context);
        return strArr[a.getNetworkType()];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static GfanPayInitProfile k(Context context) {
        String subscriberId;
        String simSerialNumber;
        if (context == null) {
            return null;
        }
        GfanPayInitProfile gfanPayInitProfile = new GfanPayInitProfile();
        String[] h = h();
        try {
            gfanPayInitProfile.mCpuDiscription = h[0];
            gfanPayInitProfile.mCpuCoreNum = Integer.valueOf(h[1]).intValue();
            gfanPayInitProfile.mCpuImplementor = h[2];
            gfanPayInitProfile.mCpuFrequency = Float.valueOf(h[3]).floatValue();
        } catch (Exception e) {
        }
        String[] strArr = {"vendor", "Renderder"};
        gfanPayInitProfile.mGpuVendor = strArr[0];
        gfanPayInitProfile.mGpuRenderer = strArr[1];
        int[] i = i();
        gfanPayInitProfile.mMemoryTotal = i[0];
        gfanPayInitProfile.mMemoryFree = i[1];
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        int availableBlocks = statFs.getAvailableBlocks();
        StatFs statFs2 = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        int[] iArr = {(statFs.getBlockCount() * (statFs.getBlockSize() / 512)) / 2, ((statFs.getBlockSize() / 512) * availableBlocks) / 2, (statFs2.getBlockCount() * (statFs2.getBlockSize() / 512)) / 2, ((statFs2.getBlockSize() / 512) * statFs2.getAvailableBlocks()) / 2};
        gfanPayInitProfile.mMobileStorageTotal = iArr[0];
        gfanPayInitProfile.mMobileStorageFree = iArr[1];
        gfanPayInitProfile.mSDCardStorageTotal = iArr[2];
        gfanPayInitProfile.mSDCardStorageFree = iArr[3];
        gfanPayInitProfile.mBatteryCapacity = j();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        gfanPayInitProfile.mDisplaMetricWidth = displayMetrics.widthPixels / displayMetrics.xdpi;
        gfanPayInitProfile.mDisplaMetricHeight = displayMetrics.heightPixels / displayMetrics.ydpi;
        gfanPayInitProfile.mDisplayMetricDensity = displayMetrics.densityDpi;
        gfanPayInitProfile.mRomInfo = Build.DISPLAY;
        gfanPayInitProfile.mBaseBand = "unknown";
        if (true == u.a()) {
            try {
                gfanPayInitProfile.mBaseBand = (String) u.a.invoke(null, new String("gsm.version.baseband"));
            } catch (Exception e2) {
                new String[1][0] = "fecth base band failed!";
                GfanPayTCLog.a();
            }
        }
        String n = n(context);
        gfanPayInitProfile.mIMEI = StringUtils.EMPTY;
        if (n != null) {
            try {
                gfanPayInitProfile.mIMEI = b(a(n.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e3) {
                gfanPayInitProfile.mIMEI = StringUtils.EMPTY;
            }
        }
        String o = o(context);
        gfanPayInitProfile.mMACAddress = StringUtils.EMPTY;
        if (o != null) {
            try {
                gfanPayInitProfile.mMACAddress = b(a(o.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e4) {
                gfanPayInitProfile.mMACAddress = StringUtils.EMPTY;
            }
        }
        Cursor query = context.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);
        if (query == null) {
            new String[1][0] = "No prefered apn...";
            GfanPayTCLog.a();
        } else if (query.moveToFirst()) {
            try {
                gfanPayInitProfile.mApnName = a(query, "apn");
                gfanPayInitProfile.mApn_mcc = a(query, "mcc");
                gfanPayInitProfile.mApn_mnc = a(query, "mnc");
                String a2 = a(query, "proxy");
                if (a2 != null && !a2.trim().equals(StringUtils.EMPTY)) {
                    gfanPayInitProfile.mApn_proxy = true;
                }
            } finally {
                query.close();
            }
        }
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == -1) {
            subscriberId = StringUtils.EMPTY;
        } else {
            l(context);
            subscriberId = a.getSubscriberId();
        }
        gfanPayInitProfile.mIMSI = subscriberId;
        gfanPayInitProfile.mUpid = StringUtils.EMPTY;
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == -1) {
            simSerialNumber = StringUtils.EMPTY;
        } else {
            l(context);
            simSerialNumber = a.getSimSerialNumber();
        }
        gfanPayInitProfile.mSimId = simSerialNumber;
        return gfanPayInitProfile;
    }

    private static void l(Context context) {
        if (a == null) {
            a = (TelephonyManager) context.getSystemService("phone");
        }
    }

    private static String m(Context context) {
        String str;
        String str2;
        String o = o(context);
        String str3 = BaseConstants.DEFAULT_UC_CNO;
        if (d(o)) {
            o = n(context);
            str3 = "0";
        }
        if (d(o)) {
            str = UUID.randomUUID().toString();
            str2 = "2";
        } else {
            String str4 = str3;
            str = o;
            str2 = str4;
        }
        try {
            return str2 + b(a(str.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    private static String n(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == -1) {
            return StringUtils.EMPTY;
        }
        l(context);
        return a.getDeviceId();
    }

    private static String o(Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == -1) {
            return StringUtils.EMPTY;
        }
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (!wifiManager.isWifiEnabled()) {
            return StringUtils.EMPTY;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo != null) {
            return connectionInfo.getMacAddress();
        }
        return null;
    }
}
