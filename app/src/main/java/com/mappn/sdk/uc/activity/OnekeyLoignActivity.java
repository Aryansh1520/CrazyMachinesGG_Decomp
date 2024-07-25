package com.mappn.sdk.uc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Html;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.DESUtil;
import com.mappn.sdk.common.utils.GfanProgressDialog;
import com.mappn.sdk.common.utils.ToastUtil;
import com.mappn.sdk.statitistics.GfanPayAgent;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.GfanUCenter;
import com.mappn.sdk.uc.User;
import com.mappn.sdk.uc.UserDao;
import com.mappn.sdk.uc.UserVo;
import com.mappn.sdk.uc.net.Api;
import com.mappn.sdk.uc.util.PrefUtil;
import com.mappn.sdk.uc.util.UserUtil;

/* loaded from: classes.dex */
public class OnekeyLoignActivity extends Activity implements ApiRequestListener {
    private DESUtil a;
    private UserVo b;
    private String c;
    private String d;
    private Boolean e = false;

    private void a(int i) {
        ToastUtil.showLong(this, Html.fromHtml(getString(BaseUtils.get_R_String(getApplicationContext(), "error_onekey_tips")) + "<br />错误码：" + i).toString());
        finish();
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (UserUtil.getOnekeyName(getApplicationContext()) == null) {
            ToastUtil.showLong(this, getString(BaseUtils.get_R_String(getApplicationContext(), "onekey_login_error_later")));
            finish();
        }
        if (!isFinishing()) {
            showDialog(5);
        }
        GfanUCCallback gfanUCCallback = GfanUCenter.gfanUCCallback;
        this.c = UserUtil.getOnekeyName(getApplicationContext());
        this.d = UserUtil.getPwdByIMEI(getApplicationContext());
        Api.login(getApplicationContext(), this, this.c, this.d);
        BaseUtils.D("OnekeyLoignActivity", this.c + "#" + this.d);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        return i == 5 ? GfanProgressDialog.createProgressDialog(this, getString(BaseUtils.get_R_String(getApplicationContext(), "onekeying")), false, 0L, null) : super.onCreateDialog(i);
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        String string;
        GfanProgressDialog.dismissDialog();
        switch (i) {
            case 0:
                if (this.e.booleanValue()) {
                    a(i2);
                } else {
                    Api.register(getApplicationContext(), this, this.c, this.d, UserUtil.randomMail(), 1);
                }
                BaseUtils.D("OnekeyLoignActivity", this.c + "#" + this.d + "登录失败，准备注册" + (i2 == 211 ? getString(BaseUtils.get_R_String(getApplicationContext(), "error_login_username")) : i2 == 212 ? getString(BaseUtils.get_R_String(getApplicationContext(), "error_login_password")) : i2 == 610 ? getString(BaseUtils.get_R_String(getApplicationContext(), "warning_login_error_unknown")) : getString(BaseUtils.get_R_String(getApplicationContext(), "error_login_other"))));
                this.e = true;
                return;
            case 1:
                switch (i2) {
                    case 213:
                        string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_username_invalid"));
                        break;
                    case 214:
                        string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_username_exist"));
                        break;
                    case 215:
                        string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_email_invalid"));
                        break;
                    case 216:
                        string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_email_exist"));
                        break;
                    case 217:
                        string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_password_invalid"));
                        break;
                    default:
                        string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_other"));
                        break;
                }
                BaseUtils.D("OnekeyLoignActivity", this.c + "#" + this.d + "注册失败" + string + "@" + i2);
                try {
                    dismissDialog(5);
                } catch (IllegalArgumentException e) {
                    BaseUtils.E("OnekeyLoignActivity", e.getMessage());
                }
                a(i2);
                return;
            default:
                return;
        }
    }

    @Override // android.app.Activity
    protected void onPrepareDialog(int i, Dialog dialog) {
        super.onPrepareDialog(i, dialog);
        if (dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                BaseUtils.D("OnekeyLoignActivity", e.getMessage());
            }
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onSuccess(int i, Object obj) {
        GfanProgressDialog.dismissDialog();
        switch (i) {
            case 0:
                GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_login", "登录机锋");
                this.b = (UserVo) obj;
                if (this.a == null) {
                    this.a = new DESUtil(getApplicationContext());
                }
                this.b.setUserName(this.a.getEncAndBase64String(this.b.getUserName()));
                String encAndBase64String = this.a.getEncAndBase64String(this.d);
                this.b.setPassword(encAndBase64String);
                User user = new User();
                user.setUid(this.b.getUid());
                user.setToken(this.b.getToken());
                user.setUserName(this.b.getUserName());
                PrefUtil.setUid(getApplicationContext(), this.b.getUid());
                try {
                    try {
                        PrefUtil.setUPW(getApplicationContext(), encAndBase64String);
                        if (UserDao.getUserByUserName(getApplicationContext(), user.getUserName()) != null) {
                            UserDao.updateToken(getApplicationContext(), user.getToken(), user.getUserName());
                        } else {
                            UserDao.insertUser(getApplicationContext(), this.b);
                        }
                        try {
                            dismissDialog(5);
                        } catch (IllegalArgumentException e) {
                            BaseUtils.E("OnekeyLoignActivity", e.getMessage());
                        }
                        if (this.a == null) {
                            this.a = new DESUtil(getApplicationContext());
                        }
                        user.setUserName(this.a.getDesAndBase64String(user.getUserName()));
                        Intent intent = new Intent();
                        intent.putExtra("extra.user", user);
                        setResult(-1, intent);
                        finish();
                        return;
                    } catch (Throwable th) {
                        try {
                            dismissDialog(5);
                        } catch (IllegalArgumentException e2) {
                            BaseUtils.E("OnekeyLoignActivity", e2.getMessage());
                        }
                        if (this.a == null) {
                            this.a = new DESUtil(getApplicationContext());
                        }
                        user.setUserName(this.a.getDesAndBase64String(user.getUserName()));
                        Intent intent2 = new Intent();
                        intent2.putExtra("extra.user", user);
                        setResult(-1, intent2);
                        finish();
                        throw th;
                    }
                } catch (SQLiteDiskIOException e3) {
                    BaseUtils.D("OnekeyLoignActivity", "user info lost when login", e3);
                    try {
                        dismissDialog(5);
                    } catch (IllegalArgumentException e4) {
                        BaseUtils.E("OnekeyLoignActivity", e4.getMessage());
                    }
                    if (this.a == null) {
                        this.a = new DESUtil(getApplicationContext());
                    }
                    user.setUserName(this.a.getDesAndBase64String(user.getUserName()));
                    Intent intent3 = new Intent();
                    intent3.putExtra("extra.user", user);
                    setResult(-1, intent3);
                    finish();
                    return;
                } catch (SQLiteException e5) {
                    try {
                        dismissDialog(5);
                    } catch (IllegalArgumentException e6) {
                        BaseUtils.E("OnekeyLoignActivity", e6.getMessage());
                    }
                    if (this.a == null) {
                        this.a = new DESUtil(getApplicationContext());
                    }
                    user.setUserName(this.a.getDesAndBase64String(user.getUserName()));
                    Intent intent4 = new Intent();
                    intent4.putExtra("extra.user", user);
                    setResult(-1, intent4);
                    finish();
                    return;
                }
            case 1:
                GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_reg", "注册机锋");
                Api.login(getApplicationContext(), this, this.c, this.d);
                return;
            default:
                return;
        }
    }
}
