package com.xiaomi.gamecenter.sdk;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.RemoteViews;
import com.mappn.sdk.pay.util.Constants;
import com.xiaomi.gamecenter.sdk.IServiceCallback;
import com.xiaomi.gamecenter.sdk.entry.CardBuyInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppEntry;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOffline;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOnline;
import com.xiaomi.gamecenter.sdk.entry.PayMode;
import com.xiaomi.gamecenter.sdk.entry.ScreenOrientation;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

/* loaded from: classes.dex */
public final class MiCommplatform {
    private static boolean isReport = false;
    private static volatile MiCommplatform sInstance;
    private Object _check_service_lock_;
    private Activity activity;
    private MiAppInfo appInfo;
    private Context ctx;
    private IGameCenterSDK sdk;
    private int service_isntall_ask;
    private Object _Lock_ = new Object();
    private boolean mTouch = false;
    private volatile int connect_flag = Integer.MIN_VALUE;
    private ServiceConnection connection = new i(this);
    private IServiceCallback serviceCallback = new IServiceCallback.Stub() { // from class: com.xiaomi.gamecenter.sdk.MiCommplatform.2
        @Override // com.xiaomi.gamecenter.sdk.IServiceCallback
        public void startActivity(String str, String str2, Bundle bundle) {
            Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
            bundle.setClassLoader(MiAppEntry.class.getClassLoader());
            intent.putExtras(bundle);
            intent.setClassName(str, str2);
            MiCommplatform.this.activity.startActivity(intent);
        }
    };

    private MiCommplatform(Context context, MiAppInfo miAppInfo) {
        this.ctx = context;
        this.appInfo = miAppInfo;
    }

    public static void Init(Context context, MiAppInfo miAppInfo) {
        if (sInstance == null) {
            sInstance = new MiCommplatform(context, miAppInfo);
            if (miAppInfo == null) {
                throw new NullPointerException("MiAppInfo is Null");
            }
            isReport = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int check_and_connect(Context context) {
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
        if (!isSdkServiceExist(context) && !install_service_apk(context)) {
            return -1;
        }
        if (this.appInfo == null) {
            Log.e("MiCommplatform", "使用SDK前请先调用MiCommplatform.Init()且MiappInfo参数不可为null");
            return -1;
        }
        synchronized (this._Lock_) {
            if (this.sdk != null && this.sdk.asBinder().isBinderAlive()) {
                return 0;
            }
            this.connect_flag = Integer.MIN_VALUE;
            context.getApplicationContext().bindService(new Intent("com.xiaomi.gamecenter.sdk.service"), this.connection, 1);
            try {
                this._Lock_.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.sdk == null) {
                return -1;
            }
            Log.e(">>>", "sdk.ConnService");
            if (!isReport) {
                openApp(this.appInfo);
                isReport = true;
            }
            return this.connect_flag;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnect(Context context) {
        try {
            if (this.sdk == null) {
                return;
            }
            context.getApplicationContext().unbindService(this.connection);
            this.sdk.unregistCallBack(this.serviceCallback);
            boolean stopService = context.getApplicationContext().stopService(new Intent("com.xiaomi.gamecenter.sdk.service"));
            this.sdk = null;
            Log.e(">>>>", "disconnect:" + stopService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MiCommplatform getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("使用SDK前请先调用MiCommplatform.Init()且MiappInfo参数不可为null");
        }
        return sInstance;
    }

    public static String getVersionCode() {
        return "SDK_TY_2.1.0";
    }

    private void openApp(MiAppInfo miAppInfo) {
        try {
            if (this.sdk == null) {
                return;
            }
            this.sdk.openAppReport(miAppInfo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showView(RemoteViews remoteViews, Activity activity) {
        new Handler(Looper.getMainLooper()).post(new f(this, remoteViews, activity));
    }

    public boolean check_user_changed(Activity activity, int i) {
        if (i != -51) {
            return false;
        }
        this.appInfo.setAccount(null);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示");
        builder.setMessage("系统账户发生变化，需要关闭游戏！");
        builder.setCancelable(false);
        builder.setPositiveButton(Constants.TEXT_OK, new k(this));
        activity.runOnUiThread(new l(this, builder));
        return true;
    }

    void chmod(String str, String str2) {
        try {
            Runtime.getRuntime().exec("chmod " + str + " " + str2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean export_install_service_apk() {
        try {
            InputStream open = this.ctx.getAssets().open("MiGameCenterSDKService.apk");
            File file = new File(this.ctx.getCacheDir(), "MiGameCenterSDKService.apk");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read == -1) {
                    fileOutputStream.close();
                    open.close();
                    chmod("777", file.getAbsolutePath());
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.addFlags(268435456);
                    intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
                    this.ctx.startActivity(intent);
                    return true;
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateCpOrderId() {
        return getMD5(UUID.randomUUID().toString().getBytes());
    }

    String getMD5(byte[] bArr) {
        if (bArr != null) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(bArr);
                StringBuffer stringBuffer = new StringBuffer();
                byte[] digest = messageDigest.digest();
                for (int i = 0; i < digest.length; i++) {
                    int i2 = digest[i];
                    if (i2 < 0) {
                        i2 += 256;
                    }
                    if (i2 < 25) {
                        stringBuffer.append("0");
                    }
                    stringBuffer.append(Integer.toHexString(i2));
                }
                return stringBuffer.toString().substring(5, 30);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    boolean install_service_apk(Context context) {
        this.service_isntall_ask = 0;
        this._check_service_lock_ = new Object();
        new d(this, Looper.getMainLooper(), context).sendEmptyMessage(0);
        synchronized (this._check_service_lock_) {
            try {
                this._check_service_lock_.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.service_isntall_ask != 1) {
            this._check_service_lock_ = null;
            return false;
        }
        if (!export_install_service_apk()) {
            return false;
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        while (!isTopActivity((Activity) context)) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
        return isSdkServiceExist(context);
    }

    boolean isSdkServiceExist(Context context) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            if (installedPackages.get(i).packageName.equalsIgnoreCase("com.xiaomi.gamecenter.sdk.service")) {
                return true;
            }
        }
        return false;
    }

    boolean isTopActivity(Activity activity) {
        String packageName = activity.getPackageName();
        List<ActivityManager.RunningTaskInfo> runningTasks = ((ActivityManager) activity.getSystemService("activity")).getRunningTasks(1);
        return runningTasks.size() > 0 && packageName.equals(runningTasks.get(0).topActivity.getPackageName());
    }

    public void miCardPay(Activity activity, CardBuyInfo cardBuyInfo, OnCardPayProcessListener onCardPayProcessListener) {
        if (this.mTouch) {
            onCardPayProcessListener.finishCardPayProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED);
        } else {
            this.mTouch = true;
            new c(this, activity, onCardPayProcessListener, cardBuyInfo).start();
        }
    }

    public void miLogin(Activity activity, OnLoginProcessListener onLoginProcessListener) {
        if (this.mTouch) {
            onLoginProcessListener.finishLoginProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED, null);
        } else {
            this.mTouch = true;
            new e(this, activity, onLoginProcessListener).start();
        }
    }

    public void miLogout(OnLoginProcessListener onLoginProcessListener) {
        if (this.mTouch) {
            onLoginProcessListener.finishLoginProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED, null);
        } else {
            this.mTouch = true;
            new g(this, onLoginProcessListener).start();
        }
    }

    public int miUniPayOffline(Activity activity, MiBuyInfoOffline miBuyInfoOffline, OnPayProcessListener onPayProcessListener) {
        if (this.mTouch) {
            onPayProcessListener.finishPayProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED);
            return -1;
        }
        this.mTouch = true;
        if (!miBuyInfoOffline.isValid()) {
            return -1;
        }
        new a(this, activity, onPayProcessListener, miBuyInfoOffline).start();
        return 0;
    }

    public int miUniPayOnline(Activity activity, MiBuyInfoOnline miBuyInfoOnline, OnPayProcessListener onPayProcessListener) {
        if (this.mTouch) {
            onPayProcessListener.finishPayProcess(MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED);
            return -1;
        }
        this.mTouch = true;
        if (!miBuyInfoOnline.isValid()) {
            return -1;
        }
        new b(this, activity, onPayProcessListener, miBuyInfoOnline).start();
        return 0;
    }

    public void update_pay_mode(PayMode payMode) {
        this.appInfo.setPayMode(payMode);
    }

    public void update_screen_orientation(ScreenOrientation screenOrientation) {
        this.appInfo.setOrientation(screenOrientation);
    }
}
