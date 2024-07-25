package com.mappn.sdk.uc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.mappn.sdk.uc.util.TopBar;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class RegisterActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener, ApiRequestListener {
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+");
    public static final int ERROR_CODE_EMAIL_EXIST = 216;
    public static final int ERROR_CODE_EMAIL_INVALID_FORMAT = 215;
    public static final int ERROR_CODE_PASSWORD_INVALID = 217;
    public static final int ERROR_CODE_USERNAME_EXIST = 214;
    public static final int ERROR_CODE_USERNAME_INVALID = 213;
    private EditText a;
    private EditText b;
    private EditText c;
    private EditText d;
    private Button e;
    private User f;
    private UserVo g;
    private boolean h;
    private DESUtil i;
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
        return true;
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

    private boolean d() {
        String obj = this.d.getText().toString();
        if (TextUtils.isEmpty(obj)) {
            this.d.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_email_empty")));
            return false;
        }
        this.d.setError(null);
        int length = obj.length();
        if (length < 6 || length > 32) {
            this.d.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_email_length_invalid")));
            return false;
        }
        this.d.setError(null);
        if (EMAIL_ADDRESS_PATTERN.matcher(obj).find()) {
            this.d.setError(null);
            return true;
        }
        this.d.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_email_format_invalid")));
        return false;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() != BaseUtils.get_R_id(getApplicationContext(), "register")) {
            if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "btn_login")) {
                finish();
                if (this.j != null) {
                    this.j.onError(1);
                    return;
                }
                return;
            }
            return;
        }
        BaseUtils.D("RegisterActivity", "onclick:");
        if (a() && b() && c() && d()) {
            Api.register(getApplicationContext(), this, this.a.getText().toString(), this.b.getText().toString(), this.d.getText().toString());
            if (isFinishing()) {
                return;
            }
            showDialog(4);
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        BaseUtils.D("RegisterActivity", "RegisterActivity onCreate");
        super.onCreate(bundle);
        this.j = GfanUCenter.gfanUCCallback;
        this.h = getIntent().getBooleanExtra(LoginActivity.EXTRA_REGIST_FROM_LOGIN, false);
        setContentView(BaseUtils.get_R_Layout(getApplicationContext(), "gfan_activity_uc_register"));
        TopBar.createTopBar(this, new View[]{findViewById(BaseUtils.get_R_id(getApplicationContext(), "top_bar_title"))}, new int[]{0}, getString(BaseUtils.get_R_String(getApplicationContext(), "register")));
        this.a = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "et_username"));
        this.a.setOnFocusChangeListener(this);
        this.b = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "et_password"));
        this.b.setOnFocusChangeListener(this);
        this.c = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "et_password2"));
        this.c.setOnFocusChangeListener(this);
        this.d = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "et_email"));
        this.d.setOnFocusChangeListener(this);
        this.e = (Button) findViewById(BaseUtils.get_R_id(getApplicationContext(), "register"));
        this.e.setOnClickListener(this);
        ((Button) findViewById(BaseUtils.get_R_id(getApplicationContext(), "btn_login"))).setOnClickListener(this);
        GfanPayAgent.setReportUncaughtExceptions(true);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        return i == 4 ? GfanProgressDialog.createProgressDialog(this, getString(BaseUtils.get_R_String(getApplicationContext(), "registering")), false, 0L, null) : super.onCreateDialog(i);
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        String string;
        GfanProgressDialog.dismissDialog();
        try {
            dismissDialog(4);
        } catch (IllegalArgumentException e) {
            BaseUtils.E("RegisterActivity", e.getMessage());
        }
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
        if (string != null) {
            ToastUtil.showLong(getApplicationContext(), "错误代码：" + i2 + " 原因：" + string);
        }
        if (this.j != null) {
            this.j.onError(1);
        }
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
        } else if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "et_password2")) {
            if (z) {
                return;
            }
            c();
        } else {
            if (view.getId() != BaseUtils.get_R_id(getApplicationContext(), "et_email") || z) {
                return;
            }
            d();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (4 == i) {
            finish();
            if (this.j != null) {
                this.j.onError(1);
            }
        }
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
                BaseUtils.D("RegisterActivity", e.getMessage());
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
        GfanProgressDialog.dismissDialog();
        switch (i) {
            case 1:
                this.g = (UserVo) obj;
                if (this.i == null) {
                    this.i = new DESUtil(getApplicationContext());
                }
                this.g.setUserName(this.i.getEncAndBase64String(this.g.getUserName()));
                this.g.setPassword(this.i.getEncAndBase64String(this.b.getText().toString()));
                this.f = new User();
                this.f.setUid(this.g.getUid());
                if (this.i == null) {
                    this.i = new DESUtil(getApplicationContext());
                }
                this.f.setUserName(this.i.getDesAndBase64String(this.g.getUserName()));
                this.f.setToken(this.g.getToken());
                PrefUtil.setUid(getApplicationContext(), this.g.getUid());
                try {
                    try {
                        UserDao.insertUser(getApplicationContext(), this.g);
                        try {
                            dismissDialog(4);
                        } catch (IllegalArgumentException e) {
                            BaseUtils.E("RegisterActivity", e.getMessage());
                        }
                        if (this.h) {
                            Intent intent = new Intent();
                            intent.putExtra("extra.user", this.f);
                            setResult(-1, intent);
                            finish();
                        } else {
                            GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_reg", "注册机锋");
                            finish();
                            if (this.j != null) {
                                this.j.onSuccess(this.f, 1);
                            }
                        }
                    } catch (Throwable th) {
                        try {
                            dismissDialog(4);
                        } catch (IllegalArgumentException e2) {
                            BaseUtils.E("RegisterActivity", e2.getMessage());
                        }
                        if (this.h) {
                            Intent intent2 = new Intent();
                            intent2.putExtra("extra.user", this.f);
                            setResult(-1, intent2);
                            finish();
                            throw th;
                        }
                        GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_reg", "注册机锋");
                        finish();
                        if (this.j == null) {
                            throw th;
                        }
                        this.j.onSuccess(this.f, 1);
                        throw th;
                    }
                } catch (SQLiteDiskIOException e3) {
                    BaseUtils.D("RegisterActivity", "user info lost when login", e3);
                    try {
                        dismissDialog(4);
                    } catch (IllegalArgumentException e4) {
                        BaseUtils.E("RegisterActivity", e4.getMessage());
                    }
                    if (this.h) {
                        Intent intent3 = new Intent();
                        intent3.putExtra("extra.user", this.f);
                        setResult(-1, intent3);
                        finish();
                    } else {
                        GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_reg", "注册机锋");
                        finish();
                        if (this.j != null) {
                            this.j.onSuccess(this.f, 1);
                        }
                    }
                } catch (SQLiteException e5) {
                    try {
                        dismissDialog(4);
                    } catch (IllegalArgumentException e6) {
                        BaseUtils.E("RegisterActivity", e6.getMessage());
                    }
                    if (this.h) {
                        Intent intent4 = new Intent();
                        intent4.putExtra("extra.user", this.f);
                        setResult(-1, intent4);
                        finish();
                        return;
                    }
                    GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_reg", "注册机锋");
                    finish();
                    if (this.j != null) {
                        this.j.onSuccess(this.f, 1);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }
}
