package com.mappn.sdk.statitistics;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.statitistics.GfanPayNetworkWatcher;
import com.mappn.sdk.statitistics.entity.GfanPayAppProfile;
import com.mappn.sdk.statitistics.entity.GfanPayDeviceProfile;
import com.mappn.sdk.statitistics.entity.GfanPayPrintag;
import com.mokredit.payment.StringUtils;
import java.io.Closeable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

/* loaded from: classes.dex */
public final class GfanPayAgent {
    public static final int APP_START_SEND_POLICY = 0;
    public static final int MSG_ON_DELAYPOST = 5;
    public static final int NORMAL_SEND_MESSAGE_INTERVAL = 300000;
    public static final int PER_DAY_SEND_POLICY = 2;
    public static final int REAL_TIME_SEND_POLICY = 1;
    public static final String TC_SDK_VERSION = "Android+gfan+V1.0.3";
    private static String g;
    private static String h;
    private static long i;
    static String a = "gfan.com";
    static boolean b = false;
    private static boolean d = true;
    private static WeakReference e = null;
    private static volatile boolean f = false;
    static int c = 0;
    private static boolean j = false;
    private static int k = 0;
    private static GfanPayNetworkWatcher.NetworkWatcherCallback l = new a();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static GfanPayAppProfile a() {
        Context context = getContext();
        if (context == null) {
            return null;
        }
        GfanPayAppProfile gfanPayAppProfile = new GfanPayAppProfile();
        gfanPayAppProfile.mAppPackageName = context.getPackageName();
        gfanPayAppProfile.mAppVersionName = r.g(context);
        gfanPayAppProfile.mAppVersionCode = r.f(context);
        gfanPayAppProfile.mStartTime = b(context);
        gfanPayAppProfile.mSdkVersion = TC_SDK_VERSION;
        gfanPayAppProfile.mPartnerId = a;
        gfanPayAppProfile.installationTime = r.h(context);
        r.b();
        gfanPayAppProfile.purchaseTime = 0L;
        return gfanPayAppProfile;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ String a(Bundle bundle, String str) {
        Iterator<String> it = bundle.keySet().iterator();
        while (it.hasNext()) {
            if (it.next().equalsIgnoreCase(str)) {
                return String.valueOf(bundle.get(str));
            }
        }
        return StringUtils.EMPTY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Context context, long j2) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putLong("pref.sendtime.key", j2);
        edit.commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Context context, boolean z) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean("pref.profile.key", false);
        edit.commit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Message message) {
        boolean z;
        try {
            j.a(getContext());
            switch (message.what) {
                case 1:
                    if (k >= 2) {
                        delaySendSession(20000L);
                    }
                    k = 0;
                    j = true;
                    Activity activity = (Activity) message.obj;
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    long currentTimeMillis = System.currentTimeMillis();
                    long j2 = PreferenceManager.getDefaultSharedPreferences(activity).getLong("pref.start.key", 0L);
                    long j3 = PreferenceManager.getDefaultSharedPreferences(activity).getLong("pref.end.key", 0L);
                    long j4 = j3 > j2 ? j3 : j2;
                    long j5 = j4 - j2;
                    if (j5 <= 1000) {
                        j5 = 1000;
                    }
                    String string = PreferenceManager.getDefaultSharedPreferences(activity).getString("pref.lastactivity.key", StringUtils.EMPTY);
                    if (currentTimeMillis - j4 > 30000) {
                        if (!TextUtils.isEmpty(h)) {
                            j.a(h, ((int) j5) / Constants.PAYMENT_MAX);
                        }
                        h = UUID.randomUUID().toString();
                        Context context = getContext();
                        String str = h;
                        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
                        edit.putString("pref.session.key", str);
                        edit.commit();
                        SharedPreferences.Editor edit2 = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        edit2.putLong("pref.start.key", currentTimeMillis);
                        edit2.commit();
                        long j6 = PreferenceManager.getDefaultSharedPreferences(getContext()).getLong("pref.lastsessionstart.key", 0L);
                        long j7 = currentTimeMillis - j6;
                        if (0 == j6) {
                            j7 = 0;
                        }
                        SharedPreferences.Editor edit3 = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        edit3.putLong("pref.lastsessionstart.key", currentTimeMillis);
                        edit3.commit();
                        j.a(h, currentTimeMillis, j7, GfanPayNetworkWatcher.b() ? 1 : -1);
                        string = StringUtils.EMPTY;
                    }
                    String localClassName = activity.getLocalClassName();
                    SharedPreferences.Editor edit4 = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                    edit4.putLong("pref.actstart.key", currentTimeMillis);
                    edit4.commit();
                    SharedPreferences.Editor edit5 = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                    edit5.putString("pref.lastactivity.key", localClassName);
                    edit5.commit();
                    i = j.a(h, localClassName, currentTimeMillis, 0, string, elapsedRealtime);
                    z = false;
                    break;
                case 2:
                    j = false;
                    Activity activity2 = (Activity) message.obj;
                    activity2.getTaskId();
                    long currentTimeMillis2 = System.currentTimeMillis();
                    long elapsedRealtime2 = SystemClock.elapsedRealtime();
                    if (i != -1) {
                        j.a(i, elapsedRealtime2);
                    }
                    b(activity2, currentTimeMillis2);
                    z = false;
                    break;
                case 3:
                    i iVar = (i) message.obj;
                    j.a(h, iVar.a, iVar.b, iVar.e, iVar.f);
                    z = false;
                    break;
                case 4:
                    i iVar2 = (i) message.obj;
                    j.a(iVar2.c, iVar2.d);
                    b(getContext(), iVar2.c);
                    z = false;
                    break;
                case 5:
                    if (!j) {
                        k++;
                    }
                    z = true;
                    break;
                default:
                    z = false;
                    break;
            }
            j.a();
            if (z) {
                s.a().c();
                if (k < 2) {
                    delaySendSession(300000L);
                }
            }
        } catch (Throwable th) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0026, code lost:
    
        r1 = 50;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void a(java.lang.StringBuilder r7, java.lang.StackTraceElement[] r8, java.lang.Throwable r9, int r10) {
        /*
            r2 = 50
        L2:
            java.lang.StackTraceElement[] r0 = r9.getStackTrace()
            int r1 = r0.length
            int r3 = r1 + (-1)
            int r1 = r8.length
            int r1 = r1 + (-1)
            r6 = r1
            r1 = r3
            r3 = r6
        Lf:
            if (r1 < 0) goto L24
            if (r3 < 0) goto L24
            r4 = r0[r1]
            r5 = r8[r3]
            boolean r4 = r4.equals(r5)
            if (r4 == 0) goto L24
            int r4 = r1 + (-1)
            int r1 = r3 + (-1)
            r3 = r1
            r1 = r4
            goto Lf
        L24:
            if (r1 <= r2) goto L27
            r1 = r2
        L27:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Caused by : "
            r3.<init>(r4)
            java.lang.StringBuilder r3 = r3.append(r9)
            java.lang.String r4 = "\r\n"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r7.append(r3)
            r3 = 0
        L40:
            if (r3 > r1) goto L5f
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "\t"
            r4.<init>(r5)
            r5 = r0[r3]
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = "\r\n"
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            r7.append(r4)
            int r3 = r3 + 1
            goto L40
        L5f:
            r1 = 5
            if (r10 < r1) goto L63
        L62:
            return
        L63:
            java.lang.Throwable r1 = r9.getCause()
            if (r1 == 0) goto L62
            int r10 = r10 + 1
            r8 = r0
            goto L2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.statitistics.GfanPayAgent.a(java.lang.StringBuilder, java.lang.StackTraceElement[], java.lang.Throwable, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void a(Throwable th) {
        if (f) {
            i iVar = new i();
            iVar.c = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append(th.toString());
            sb.append(GfanPayPrintag.OBJENDTAG);
            StackTraceElement[] stackTrace = th.getStackTrace();
            int length = stackTrace.length <= 50 ? stackTrace.length : 50;
            for (int i2 = 0; i2 < length; i2++) {
                sb.append("\t" + stackTrace[i2] + GfanPayPrintag.OBJENDTAG);
            }
            Throwable cause = th.getCause();
            if (cause != null) {
                a(sb, stackTrace, cause, 1);
            }
            iVar.d = sb.toString();
            s.b().sendMessage(Message.obtain(s.b(), 4, iVar));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref.profile.key", true);
    }

    private static long b(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong("pref.init.key", 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static GfanPayDeviceProfile b() {
        Context context = getContext();
        if (context == null) {
            return null;
        }
        GfanPayDeviceProfile gfanPayDeviceProfile = new GfanPayDeviceProfile();
        gfanPayDeviceProfile.mMobileModel = Build.MODEL;
        gfanPayDeviceProfile.mOsSdkVersion = r.a();
        gfanPayDeviceProfile.mGis = r.e(context);
        gfanPayDeviceProfile.mCpuABI = Build.CPU_ABI;
        gfanPayDeviceProfile.mPixelMetric = r.i(context);
        gfanPayDeviceProfile.mCountry = r.c();
        gfanPayDeviceProfile.mCarrier = r.d(context);
        gfanPayDeviceProfile.mLanguage = r.d();
        gfanPayDeviceProfile.mTimezone = ((TimeZone.getDefault().getRawOffset() / Constants.PAYMENT_MAX) / 60) / 60;
        gfanPayDeviceProfile.mOsVersion = "Android+" + Build.VERSION.RELEASE;
        gfanPayDeviceProfile.mChannel = GfanPayNetworkWatcher.a() ? 0 : 1;
        gfanPayDeviceProfile.m2G_3G = r.j(context);
        gfanPayDeviceProfile.mNetworkOperator = r.b(context);
        gfanPayDeviceProfile.mSimOperator = r.c(context);
        gfanPayDeviceProfile.hostName = r.e();
        gfanPayDeviceProfile.deviceName = r.f();
        r.g();
        gfanPayDeviceProfile.kernBootTime = 0L;
        return gfanPayDeviceProfile;
    }

    private static void b(Context context, long j2) {
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putLong("pref.end.key", j2);
        edit.commit();
    }

    public static void delaySendSession(long j2) {
        Message obtain = Message.obtain(s.b(), 5);
        Handler b2 = s.b();
        b2.removeMessages(5);
        b2.sendMessageDelayed(obtain, j2);
    }

    public static String getAppId() {
        return g;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Context getContext() {
        if (e != null) {
            return (Context) e.get();
        }
        return null;
    }

    public static void init(Context context) {
        e = new WeakReference(context.getApplicationContext());
        GfanPaySingleThreadedExecutor.execute(new b(context.getApplicationContext()));
    }

    public static void init(Context context, String str, String str2) {
        if (f) {
            return;
        }
        if (TextUtils.isEmpty(g)) {
            new String[1][0] = "Not found app key id, please check your meta-data";
            GfanPayTCLog.a();
        }
        g = str;
        a = str2;
        h = PreferenceManager.getDefaultSharedPreferences(context).getString("pref.session.key", null);
        GfanPayNetworkWatcher.a((Context) e.get(), l);
        if (b(context) == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
            edit.putLong("pref.init.key", currentTimeMillis);
            edit.commit();
        }
        f = true;
        setReportUncaughtExceptions(d);
        new String[1][0] = "TCAgent init";
        GfanPayTCLog.a();
        k = 0;
        delaySendSession(20000L);
    }

    public static void onError(Context context, Throwable th) {
        if (th == null) {
            return;
        }
        GfanPaySingleThreadedExecutor.execute(new g(context, th));
    }

    public static void onEvent(Context context, String str) {
        new String[1][0] = "eventId = " + str;
        GfanPayTCLog.a();
        onEvent(context, str, StringUtils.EMPTY);
    }

    public static void onEvent(Context context, String str, String str2) {
        new String[1][0] = "eventId = " + str;
        GfanPayTCLog.a();
        GfanPaySingleThreadedExecutor.execute(new e(context, str, str2));
    }

    public static void onEvent(Context context, String str, Map map) {
        new String[1][0] = "eventId = " + str;
        GfanPayTCLog.a();
        GfanPaySingleThreadedExecutor.execute(new f(context, str, map));
    }

    public static void onPause(Activity activity) {
        String[] strArr = {"onPause", activity.getLocalClassName()};
        GfanPayTCLog.a();
        GfanPaySingleThreadedExecutor.execute(new d(activity));
    }

    public static void onResume(Activity activity) {
        GfanPaySingleThreadedExecutor.execute(new c(activity));
    }

    public static void setReportUncaughtExceptions(boolean z) {
        try {
            d = z;
            if (z) {
                Thread.setDefaultUncaughtExceptionHandler(h.a());
            } else {
                Thread.setDefaultUncaughtExceptionHandler(h.b());
            }
        } catch (Throwable th) {
        }
    }

    public static void setSendPolicy(int i2) {
        c = i2;
    }

    public static void setWifiOnly(boolean z) {
        b = z;
    }
}
