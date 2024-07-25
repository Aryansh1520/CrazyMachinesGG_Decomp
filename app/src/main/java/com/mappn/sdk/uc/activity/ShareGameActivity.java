package com.mappn.sdk.uc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.GfanUCenter;
import com.mappn.sdk.uc.net.Api;

/* loaded from: classes.dex */
public class ShareGameActivity extends Activity implements ApiRequestListener {
    private GfanUCCallback a;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.a = GfanUCenter.gfanUCCallback;
        if (!isFinishing()) {
            showDialog(7);
        }
        String packageName = BaseUtils.getPackageName(this);
        if (BaseUtils.sDebug) {
            packageName = "com.qihoo360.mobilesafe";
        }
        Api.getApkUrl(this, this, packageName);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        if (i != 7) {
            return super.onCreateDialog(i);
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(0);
        progressDialog.setMessage(getString(BaseUtils.get_R_String(getApplicationContext(), "game_shareing")));
        return progressDialog;
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        this.a.onError(5);
        finish();
    }

    @Override // android.app.Activity
    protected void onPrepareDialog(int i, Dialog dialog) {
        super.onPrepareDialog(i, dialog);
        if (dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                BaseUtils.D("ShareGameActivity", e.getMessage());
            }
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onSuccess(int i, Object obj) {
        this.a.onSuccess(null, 5);
        String obj2 = obj.toString();
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "精品分享");
        intent.putExtra("android.intent.extra.TEXT", "我从机锋市场下载了《" + BaseUtils.getAppName(this) + "》，大家来陪我一起玩吧！还有数十万免费android软件和游戏，快来一起玩吧。" + obj2);
        startActivity(Intent.createChooser(intent, getString(BaseUtils.get_R_String(this, "game_share_choose_way"))));
        finish();
    }
}
