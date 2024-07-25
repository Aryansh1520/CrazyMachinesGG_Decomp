package com.mappn.sdk.uc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

/* loaded from: classes.dex */
public class LoginActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener, ApiRequestListener {
    public static final int ERROR_CODE_PASSWORD_INVALID = 212;
    public static final int ERROR_CODE_TIMEOUT = 600;
    public static final int ERROR_CODE_USERNAME_NOT_EXIST = 211;
    public static final String EXTRA_REGIST_FROM_LOGIN = "extra.regist.from.login";
    public static final int REQUEST_CODE_ONEKEY = 1;
    public static final int REQUEST_CODE_REGIST = 0;
    public static final int REQUEST_CODE_WEIBO = 2;
    private EditText a;
    private EditText b;
    private Button c;
    private Button d;
    private TextView e;
    private Button f;
    private DESUtil g;
    private UserVo h;
    private User i;
    private GfanUCCallback j;

    private void a(int i) {
        onCreateDialog(i).show();
    }

    private boolean a() {
        int i;
        String obj = this.a.getText().toString();
        if (TextUtils.isEmpty(obj) || TextUtils.isEmpty(obj.trim())) {
            this.a.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_username_empty")));
            this.e.setPadding(21, 0, 0, 21);
            return false;
        }
        this.e.setPadding(0, 0, 0, 0);
        this.a.setError(null);
        try {
            i = obj.getBytes("UTF8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            i = 0;
        }
        if (i < 3 || i > 15) {
            this.a.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_username_length_invalid")));
            this.e.setPadding(21, 0, 0, 21);
            return false;
        }
        this.a.setError(null);
        this.e.setPadding(0, 0, 0, 0);
        return true;
    }

    private boolean a(EditText editText) {
        String obj = editText.getText().toString();
        if (TextUtils.isEmpty(obj)) {
            editText.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_password_empty")));
            return false;
        }
        editText.setError(null);
        if (obj.length() > 32) {
            editText.setError(getString(BaseUtils.get_R_String(getApplicationContext(), "error_password_length_invalid")));
            return false;
        }
        editText.setError(null);
        return true;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i == 0 && -1 == i2) {
            GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_reg", "注册机锋");
            finish();
            if (this.j != null) {
                this.j.onSuccess((User) intent.getSerializableExtra("extra.user"), 1);
            }
        }
        if (1 == i && -1 == i2) {
            BaseUtils.D("LoginActivity", "一键登录成功");
            finish();
            if (this.j != null) {
                this.j.onSuccess((User) intent.getSerializableExtra("extra.user"), 0);
            }
        }
        if (2 == i && -1 == i2) {
            BaseUtils.D("LoginActivity", "微博登录成功");
            finish();
            if (this.j != null) {
                this.j.onSuccess((User) intent.getSerializableExtra("extra.user"), 0);
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "btn_login")) {
            if (a() && a(this.b) && !isFinishing()) {
                a(0);
                Api.login(getApplicationContext(), this, this.a.getText().toString(), this.b.getText().toString());
                return;
            }
            return;
        }
        if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "tv_regist")) {
            Intent intent = new Intent(getApplicationContext(), (Class<?>) RegisterActivity.class);
            intent.putExtra(EXTRA_REGIST_FROM_LOGIN, true);
            startActivityForResult(intent, 0);
        } else {
            if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "to_choose_account")) {
                PrefUtil.logout(getApplicationContext());
                Intent intent2 = new Intent();
                intent2.setClass(this, ChooseAccountActivity.class);
                startActivity(intent2);
                finish();
                return;
            }
            if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "btn_onekey_login")) {
                startActivityForResult(new Intent(getApplicationContext(), (Class<?>) OnekeyLoignActivity.class), 1);
            } else if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "btn_weibo")) {
                startActivityForResult(new Intent(this, (Class<?>) WeiboLoignActivity.class), 2);
            }
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        BaseUtils.D("LoginActivity", "LoginActivity onCreate");
        super.onCreate(bundle);
        this.j = GfanUCenter.gfanUCCallback;
        setContentView(BaseUtils.get_R_Layout(getApplicationContext(), "gfan_activity_uc_login"));
        GfanPayAgent.setReportUncaughtExceptions(true);
        TopBar.createTopBar(this, new View[]{findViewById(BaseUtils.get_R_id(getApplicationContext(), "top_bar_title"))}, new int[]{0}, getString(BaseUtils.get_R_String(getApplicationContext(), "login")));
        this.a = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "et_username"));
        this.a.setOnFocusChangeListener(this);
        this.b = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "et_password"));
        this.b.setOnFocusChangeListener(this);
        this.c = (Button) findViewById(BaseUtils.get_R_id(getApplicationContext(), "btn_login"));
        this.c.setOnClickListener(this);
        this.f = (Button) findViewById(BaseUtils.get_R_id(getApplicationContext(), "btn_onekey_login"));
        this.f.setOnClickListener(this);
        this.d = (Button) findViewById(BaseUtils.get_R_id(getApplicationContext(), "to_choose_account"));
        this.d.setOnClickListener(this);
        if (UserDao.queryAllUsers(getApplicationContext()) == null || UserDao.queryAllUsers(getApplicationContext()).size() <= 0) {
            this.d.setVisibility(8);
        }
        this.e = (TextView) findViewById(BaseUtils.get_R_id(getApplicationContext(), "to_choose_account_margin"));
        TextView textView = (TextView) findViewById(BaseUtils.get_R_id(getApplicationContext(), "tv_regist"));
        CharSequence text = textView.getText();
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        textView.setText(spannableString);
        textView.setOnClickListener(this);
        long longExtra = getIntent().getLongExtra("extra.login.uid", -1L);
        if (longExtra != -1) {
            try {
                this.h = UserDao.getUserByUid(getApplicationContext(), longExtra);
                if (this.h != null) {
                    if (this.g == null) {
                        this.g = new DESUtil(getApplicationContext());
                    }
                    this.a.setText(this.g.getDesAndBase64String(this.h.getUserName()));
                    this.b.setText(this.g.getDesAndBase64String(this.h.getPassword()));
                    return;
                }
            } catch (SQLiteException e) {
                return;
            }
        }
        long uid = PrefUtil.getUid(getApplicationContext());
        if (uid != -1) {
            try {
                this.h = UserDao.getUserByUid(getApplicationContext(), uid);
                if (this.h != null) {
                    if (this.g == null) {
                        this.g = new DESUtil(getApplicationContext());
                    }
                    this.a.setText(this.g.getDesAndBase64String(this.h.getUserName()));
                    this.b.setText(this.g.getDesAndBase64String(this.h.getPassword()));
                    if (!PrefUtil.isLogin(getApplicationContext()) || isFinishing()) {
                        return;
                    }
                    a(1);
                    Api.verifyToken(getApplicationContext(), this, this.h.getToken());
                }
            } catch (SQLiteException e2) {
            }
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        switch (i) {
            case 0:
                return GfanProgressDialog.createProgressDialog(this, getString(BaseUtils.get_R_String(getApplicationContext(), "singin")), false, 0L, null);
            case 1:
                return GfanProgressDialog.createProgressDialog(this, getString(BaseUtils.get_R_String(getApplicationContext(), "check_login")), false, 0L, null);
            default:
                return super.onCreateDialog(i);
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        String str = null;
        GfanProgressDialog.dismissDialog();
        switch (i) {
            case 0:
                str = i2 == 211 ? getString(BaseUtils.get_R_String(getApplicationContext(), "error_login_username")) : i2 == 212 ? getString(BaseUtils.get_R_String(getApplicationContext(), "error_login_password")) : i2 == 610 ? getString(BaseUtils.get_R_String(getApplicationContext(), "warning_login_error_unknown")) : getString(BaseUtils.get_R_String(getApplicationContext(), "error_login_other"));
                ToastUtil.showLong(getApplicationContext(), Html.fromHtml(str + "<br />错误码：" + i2).toString());
                BaseUtils.D("LoginActivity", "登录错误 statusCode : " + i2);
                ToastUtil.showLong(getApplicationContext(), str);
                return;
            case 1:
            default:
                if (i2 <= 0 && i2 != -32 && i2 != -33) {
                    str = i2 == 0 ? Html.fromHtml(getString(BaseUtils.get_R_String(getApplicationContext(), "error_unknown")) + "<br />错误码：" + i2).toString() : getString(BaseUtils.get_R_String(getApplicationContext(), "error_params"), new Object[]{Integer.valueOf(i2)});
                } else if (i2 == 600) {
                    str = getString(BaseUtils.get_R_String(getApplicationContext(), "error_network_time_out"));
                }
                BaseUtils.D("LoginActivity", "登录错误 statusCode : " + i2);
                ToastUtil.showLong(getApplicationContext(), str);
                return;
            case 2:
                if (600 == i2) {
                    ToastUtil.showLong(getApplicationContext(), BaseUtils.get_R_String(getApplicationContext(), "error_network_time_out"));
                    return;
                } else {
                    ToastUtil.showLong(getApplicationContext(), getString(BaseUtils.get_R_String(getApplicationContext(), "error_verify_token")));
                    return;
                }
        }
    }

    @Override // android.view.View.OnFocusChangeListener
    public void onFocusChange(View view, boolean z) {
        if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "et_username")) {
            if (z) {
                return;
            }
            a();
        } else {
            if (view.getId() != BaseUtils.get_R_id(getApplicationContext(), "et_password") || z) {
                return;
            }
            a(this.b);
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (4 != i) {
            return true;
        }
        finish();
        if (this.j == null) {
            return true;
        }
        this.j.onError(0);
        return true;
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        GfanPayAgent.onPause(this);
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
            case 0:
                this.h = (UserVo) obj;
                if (this.g == null) {
                    this.g = new DESUtil(getApplicationContext());
                }
                this.h.setUserName(this.g.getEncAndBase64String(this.h.getUserName()));
                String encAndBase64String = this.g.getEncAndBase64String(this.b.getText().toString());
                this.h.setPassword(encAndBase64String);
                this.i = new User();
                this.i.setUid(this.h.getUid());
                this.i.setToken(this.h.getToken());
                this.i.setUserName(this.h.getUserName());
                PrefUtil.setUid(getApplicationContext(), this.i.getUid());
                try {
                    try {
                        PrefUtil.setUPW(getApplicationContext(), encAndBase64String);
                        if (UserDao.getUserByUserName(getApplicationContext(), this.i.getUserName()) != null) {
                            UserDao.updateToken(getApplicationContext(), this.i.getToken(), this.i.getUserName());
                        } else {
                            UserDao.insertUser(getApplicationContext(), this.h);
                        }
                        GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_login", "登录机锋");
                        finish();
                        if (this.g == null) {
                            this.g = new DESUtil(getApplicationContext());
                        }
                        this.i.setUserName(this.g.getDesAndBase64String(this.i.getUserName()));
                        if (this.j != null) {
                            this.j.onSuccess(this.i, 0);
                            return;
                        }
                        return;
                    } catch (SQLiteDiskIOException e) {
                        BaseUtils.D("LoginActivity", "user info lost when login", e);
                        GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_login", "登录机锋");
                        finish();
                        if (this.g == null) {
                            this.g = new DESUtil(getApplicationContext());
                        }
                        this.i.setUserName(this.g.getDesAndBase64String(this.i.getUserName()));
                        if (this.j != null) {
                            this.j.onSuccess(this.i, 0);
                            return;
                        }
                        return;
                    } catch (SQLiteException e2) {
                        GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_login", "登录机锋");
                        finish();
                        if (this.g == null) {
                            this.g = new DESUtil(getApplicationContext());
                        }
                        this.i.setUserName(this.g.getDesAndBase64String(this.i.getUserName()));
                        if (this.j != null) {
                            this.j.onSuccess(this.i, 0);
                            return;
                        }
                        return;
                    }
                } catch (Throwable th) {
                    GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_login", "登录机锋");
                    finish();
                    if (this.g == null) {
                        this.g = new DESUtil(getApplicationContext());
                    }
                    this.i.setUserName(this.g.getDesAndBase64String(this.i.getUserName()));
                    if (this.j != null) {
                        this.j.onSuccess(this.i, 0);
                    }
                    throw th;
                }
            case 1:
            default:
                return;
            case 2:
                this.i = new User();
                this.i.setUid(this.h.getUid());
                if (this.g == null) {
                    this.g = new DESUtil(getApplicationContext());
                }
                this.i.setUserName(this.g.getDesAndBase64String(this.h.getUserName()));
                this.i.setToken(this.h.getToken());
                GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_login", "登录机锋");
                finish();
                if (this.j != null) {
                    this.j.onSuccess(this.i, 0);
                    return;
                }
                return;
        }
    }
}
