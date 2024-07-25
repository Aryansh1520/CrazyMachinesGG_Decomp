package com.mappn.sdk.pay.chargement.alipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.util.Constants;
import com.mokredit.payment.StringUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MobileSecurePayHelper {
    Activity a;
    private ProgressDialog b = null;
    private Handler c = new d(this);

    public MobileSecurePayHelper(Activity activity) {
        this.a = null;
        this.a = activity;
    }

    public static PackageInfo getApkInfo(Context context, String str) {
        return context.getPackageManager().getPackageArchiveInfo(str, 128);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a() {
        try {
            if (this.b != null) {
                this.b.dismiss();
                this.b = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String checkNewUpdate(PackageInfo packageInfo) {
        try {
            JSONObject sendCheckNewUpdate = sendCheckNewUpdate(packageInfo.versionName);
            if (sendCheckNewUpdate.getString("needUpdate").equalsIgnoreCase("true")) {
                return sendCheckNewUpdate.getString("updateUrl");
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean detectMobile_sp() {
        boolean isMobile_spExist = isMobile_spExist();
        if (!isMobile_spExist) {
            String str = this.a.getCacheDir().getAbsolutePath() + "/temp.apk";
            retrieveApkFromLib(this.a, Constants.RES_ALIPAY_PLUGIN_APK, str);
            this.b = BaseHelper.showProgress(this.a, null, "正在检测安全支付服务版本", false, true);
            new Thread(new a(this, str)).start();
        }
        return isMobile_spExist;
    }

    public boolean isMobile_spExist() {
        List<PackageInfo> installedPackages = this.a.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            if (installedPackages.get(i).packageName.equalsIgnoreCase("com.alipay.android.app")) {
                return true;
            }
        }
        return false;
    }

    public boolean retrieveApkFromLib(Context context, String str, String str2) {
        try {
            InputStream openRawResource = context.getResources().openRawResource(BaseUtils.get_R_Raw(context, "alipay_plugin"));
            File file = new File(str2);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openRawResource.read(bArr);
                if (read <= 0) {
                    fileOutputStream.close();
                    openRawResource.close();
                    return true;
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean retrieveApkFromNet(Context context, String str, String str2) {
        try {
            return new NetworkManager(this.a).urlDownloadToFile(context, str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject sendCheckNewUpdate(String str) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("action", "update");
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("platform", "android");
            jSONObject2.put("version", str);
            jSONObject2.put("partner", StringUtils.EMPTY);
            jSONObject.put("data", jSONObject2);
            return sendRequest(jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject sendRequest(String str) {
        JSONObject jSONObject;
        String SendAndWaitResponse;
        NetworkManager networkManager = new NetworkManager(this.a);
        try {
            synchronized (networkManager) {
                SendAndWaitResponse = networkManager.SendAndWaitResponse(str, "https://msp.alipay.com/x.htm");
            }
            jSONObject = new JSONObject(SendAndWaitResponse);
        } catch (Exception e) {
            e.printStackTrace();
            jSONObject = null;
        }
        if (jSONObject != null) {
            BaseHelper.log("MobileSecurePayHelper", jSONObject.toString());
        }
        return jSONObject;
    }

    public void showInstallConfirmDialog(Context context, String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("安装提示");
        builder.setMessage("为保证您的交易安全，需要您安装支付宝安全支付服务，才能进行付款。\n\n点击确定，立即安装。");
        builder.setPositiveButton(Constants.TEXT_OK, new b(this, str, context));
        builder.setNegativeButton("取消", new c(this));
        builder.show();
    }
}
