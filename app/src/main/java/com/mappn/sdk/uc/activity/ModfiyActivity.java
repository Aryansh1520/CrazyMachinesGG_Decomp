package com.mappn.sdk.uc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.DESUtil;
import com.mappn.sdk.common.utils.ToastUtil;
import com.mappn.sdk.statitistics.GfanPayAgent;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.GfanUCenter;
import com.mappn.sdk.uc.User;
import com.mappn.sdk.uc.UserDao;
import com.mappn.sdk.uc.UserVo;
import com.mappn.sdk.uc.net.Api;
import com.mappn.sdk.uc.util.PrefUtil;
import com.mappn.sdk.uc.util.TopBar;
import com.mappn.sdk.uc.util.UserUtil;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class ModfiyActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener, ApiRequestListener {
    public static final int ERROR_CODE_CANNOT_MODIFY = 234;
    public static final int ERROR_CODE_PASSWORD_INVALID = 217;
    public static final int ERROR_CODE_USERNAME_EXIST = 214;
    public static final int ERROR_CODE_USERNAME_INVALID = 213;
    private EditText a;
    private EditText b;
    private EditText c;
    private Button d;
    private User e;
    private UserVo f;
    private DESUtil g;
    private long h;
    private String i;
    private GfanUCCallback j;

    private boolean a() {
        int i;
        String obj = this.a.getText().toString();
        if (TextUtils.isEmpty(obj) || TextUtils.isEmpty(obj.trim())) {
            this.a.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_username_empty")));
            return false;
        }
        this.a.setError(null);
        try {
            i = obj.getBytes("UTF8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            i = 0;
        }
        if (i < 3 || i > 15) {
            this.a.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_username_length_invalid")));
            return false;
        }
        this.a.setError(null);
        if (!obj.equals(UserUtil.getOnekeyName(getApplicationContext()))) {
            return true;
        }
        this.a.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_username_must_modfiy")));
        return false;
    }

    private boolean b() {
        String obj = this.b.getText().toString();
        if (TextUtils.isEmpty(obj)) {
            this.b.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_password_empty")));
            return false;
        }
        this.b.setError(null);
        if (obj.length() > 16) {
            this.b.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_password_length_invalid")));
            return false;
        }
        this.b.setError(null);
        return true;
    }

    private boolean c() {
        if (this.c.getText().toString().equals(this.b.getText().toString())) {
            this.c.setError(null);
            return true;
        }
        this.c.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_insure_same_the_two_password")));
        return false;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != BaseUtils.get_R_id(getApplicationContext(), "btn_modfiy")) {
            if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "btn_cancel")) {
                finish();
                return;
            }
            return;
        }
        BaseUtils.D("ModfiyActivity", "onclick:");
        if (a() && b() && c()) {
            Api.modfiy(getApplicationContext(), this, Long.valueOf(this.h), this.a.getText().toString(), this.b.getText().toString(), this.i);
            if (isFinishing()) {
                return;
            }
            showDialog(6);
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        BaseUtils.D("ModfiyActivity", "ModfiyActivity onCreate");
        super.onCreate(bundle);
        this.j = GfanUCenter.gfanUCCallback;
        setContentView(BaseUtils.get_R_Layout(getApplicationContext(), "gfan_activity_uc_modfiy"));
        TopBar.createTopBar(getApplicationContext(), new View[]{findViewById(BaseUtils.get_R_id(getApplicationContext(), "top_bar_title"))}, new int[]{0}, getString(BaseUtils.get_R_String(getApplicationContext(), "modfiy_info")));
        this.a = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "et_username"));
        this.a.setOnFocusChangeListener(this);
        this.b = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "et_password"));
        this.b.setOnFocusChangeListener(this);
        this.c = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "et_password2"));
        this.c.setOnFocusChangeListener(this);
        this.d = (Button) findViewById(BaseUtils.get_R_id(getApplicationContext(), "btn_modfiy"));
        this.d.setOnClickListener(this);
        ((Button) findViewById(BaseUtils.get_R_id(getApplicationContext(), "btn_cancel"))).setOnClickListener(this);
        if (PrefUtil.isLogin(getApplicationContext())) {
            this.h = PrefUtil.getUid(getApplicationContext());
            if (this.h != -1) {
                try {
                    this.f = UserDao.getUserByUid(getApplicationContext(), this.h);
                    if (this.f != null) {
                        if (this.g == null) {
                            this.g = new DESUtil(getApplicationContext());
                        }
                        this.i = this.g.getDesAndBase64String(this.f.getUserName());
                        this.a.setText(this.i);
                    }
                } catch (SQLiteException e) {
                }
            }
            BaseUtils.D("ModfiyActivity", "getOnekeyName :" + UserUtil.getOnekeyName(getApplicationContext()) + " oldUserName :" + this.i);
            if (!UserUtil.getOnekeyName(getApplicationContext()).equals(this.i)) {
                ToastUtil.showLong(this, getString(BaseUtils.get_R_String(getApplicationContext(), "error_only_onekey_can")));
                finish();
            }
        } else {
            ToastUtil.showLong(this, getString(BaseUtils.get_R_String(getApplicationContext(), "not_login")));
            finish();
        }
        GfanPayAgent.setReportUncaughtExceptions(true);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        if (i != 6) {
            return super.onCreateDialog(i);
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(0);
        progressDialog.setMessage(getString(BaseUtils.get_R_String(getApplicationContext(), "modfiying")));
        return progressDialog;
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        String string;
        try {
            dismissDialog(6);
        } catch (IllegalArgumentException e) {
            BaseUtils.E("ModfiyActivity", e.getMessage());
        }
        switch (i2) {
            case 213:
                string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_username_invalid"));
                break;
            case 214:
                string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_username_exist"));
                break;
            case 217:
                string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_password_invalid"));
                break;
            case ERROR_CODE_CANNOT_MODIFY /* 234 */:
                string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_cannot_modify"));
                break;
            default:
                string = getString(BaseUtils.get_R_String(getApplicationContext(), "error_other"));
                break;
        }
        if (string != null) {
            ToastUtil.showLong(getApplicationContext(), "错误代码：" + i2 + " 原因：" + string);
        }
        this.j.onError(2);
    }

    @Override // android.view.View.OnFocusChangeListener
    public void onFocusChange(View view, boolean z) {
        if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "et_username")) {
            if (z) {
                return;
            }
            a();
        } else if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "et_password")) {
            if (z) {
                return;
            }
            b();
        } else {
            if (view.getId() != BaseUtils.get_R_id(getApplicationContext(), "et_password2") || z) {
                return;
            }
            c();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (4 != i) {
            return true;
        }
        finish();
        return true;
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        GfanPayAgent.onPause(this);
    }

    @Override // android.app.Activity
    protected void onPrepareDialog(int i, Dialog dialog) {
        super.onPrepareDialog(i, dialog);
        if (dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                BaseUtils.D("ModfiyActivity", e.getMessage());
            }
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        GfanPayAgent.onResume(this);
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onSuccess(int i, Object obj) {
        this.f = (UserVo) obj;
        if (this.g == null) {
            this.g = new DESUtil(getApplicationContext());
        }
        this.f.setUserName(this.g.getEncAndBase64String(this.f.getUserName()));
        this.f.setPassword(this.g.getEncAndBase64String(this.b.getText().toString()));
        this.e = new User();
        this.e.setUid(this.f.getUid());
        if (this.g == null) {
            this.g = new DESUtil(getApplicationContext());
        }
        this.e.setUserName(this.g.getDesAndBase64String(this.f.getUserName()));
        this.e.setToken(this.f.getToken());
        PrefUtil.setUid(getApplicationContext(), this.f.getUid());
        try {
            try {
                UserDao.deleteUserByUid(getApplicationContext(), this.f.getUid());
                UserDao.insertUser(getApplicationContext(), this.f);
                try {
                    dismissDialog(6);
                } catch (IllegalArgumentException e) {
                    BaseUtils.E("ModfiyActivity", e.getMessage());
                }
            } catch (Throwable th) {
                try {
                    dismissDialog(6);
                } catch (IllegalArgumentException e2) {
                    BaseUtils.E("ModfiyActivity", e2.getMessage());
                }
                throw th;
            }
        } catch (SQLiteDiskIOException e3) {
            BaseUtils.D("ModfiyActivity", "user info lost when login", e3);
            try {
                dismissDialog(6);
            } catch (IllegalArgumentException e4) {
                BaseUtils.E("ModfiyActivity", e4.getMessage());
            }
        } catch (SQLiteException e5) {
            try {
                dismissDialog(6);
            } catch (IllegalArgumentException e6) {
                BaseUtils.E("ModfiyActivity", e6.getMessage());
            }
        }
        finish();
        this.j.onSuccess(this.e, 2);
    }
}
