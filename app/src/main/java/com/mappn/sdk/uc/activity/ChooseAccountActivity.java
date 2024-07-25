package com.mappn.sdk.uc.activity;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
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
import com.mappn.sdk.uc.util.Constants;
import com.mappn.sdk.uc.util.PrefUtil;
import com.mappn.sdk.uc.util.TopBar;
import com.mokredit.payment.StringUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class ChooseAccountActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, ApiRequestListener, BtnOnClick {
    private Boolean a = false;
    private int b;
    private List c;
    private UserVo d;
    private c e;
    private ArrayList f;
    private ArrayList g;
    private long h;
    private long i;
    private String j;
    private DESUtil k;
    private ListView l;
    private Button m;
    private FrameLayout n;
    private ProgressBar o;
    private GfanUCCallback p;

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        try {
            this.c = UserDao.queryAllUsers(getApplicationContext());
            if (this.c != null) {
                this.b = this.c.size();
            }
            if (this.b == 0) {
                f();
            }
            if (this.k == null) {
                this.k = new DESUtil(getApplicationContext());
            }
            for (int i = 0; i < this.b; i++) {
                UserVo userVo = (UserVo) this.c.get(i);
                HashMap hashMap = new HashMap();
                hashMap.put(Constants.ACCOUNT_NAME, this.k.getDesAndBase64String(userVo.getUserName()));
                hashMap.put(Constants.ACCOUNT_CHECK, this.a);
                hashMap.put(Constants.ACCOUNT_VISIABLE, this.a);
                hashMap.put(Constants.ACCOUNT_VIEW_TYPE, 0);
                hashMap.put(Constants.ACCOUNT_UID, Long.valueOf(userVo.getUid()));
                this.f.add(hashMap);
            }
        } catch (SQLiteException e) {
            if (this.c != null) {
                this.b = this.c.size();
            }
            if (this.b == 0) {
                f();
            }
            if (this.k == null) {
                this.k = new DESUtil(getApplicationContext());
            }
            for (int i2 = 0; i2 < this.b; i2++) {
                UserVo userVo2 = (UserVo) this.c.get(i2);
                HashMap hashMap2 = new HashMap();
                hashMap2.put(Constants.ACCOUNT_NAME, this.k.getDesAndBase64String(userVo2.getUserName()));
                hashMap2.put(Constants.ACCOUNT_CHECK, this.a);
                hashMap2.put(Constants.ACCOUNT_VISIABLE, this.a);
                hashMap2.put(Constants.ACCOUNT_VIEW_TYPE, 0);
                hashMap2.put(Constants.ACCOUNT_UID, Long.valueOf(userVo2.getUid()));
                this.f.add(hashMap2);
            }
        } catch (Throwable th) {
            if (this.c != null) {
                this.b = this.c.size();
            }
            if (this.b == 0) {
                f();
            }
            if (this.k == null) {
                this.k = new DESUtil(getApplicationContext());
            }
            for (int i3 = 0; i3 < this.b; i3++) {
                UserVo userVo3 = (UserVo) this.c.get(i3);
                HashMap hashMap3 = new HashMap();
                hashMap3.put(Constants.ACCOUNT_NAME, this.k.getDesAndBase64String(userVo3.getUserName()));
                hashMap3.put(Constants.ACCOUNT_CHECK, this.a);
                hashMap3.put(Constants.ACCOUNT_VISIABLE, this.a);
                hashMap3.put(Constants.ACCOUNT_VIEW_TYPE, 0);
                hashMap3.put(Constants.ACCOUNT_UID, Long.valueOf(userVo3.getUid()));
                this.f.add(hashMap3);
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i) {
        onCreateDialog(i).show();
    }

    private void b() {
        BaseUtils.D("ChooseAccountActivity", "toDelAccount is run" + this.f);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.f.size()) {
                this.a = true;
                c();
                e();
                BaseUtils.D("ChooseAccountActivity", "toDelAccount is run");
                this.e.notifyDataSetChanged();
                return;
            }
            BaseUtils.D("ChooseAccountActivity", "toDelAccount is run" + ((HashMap) this.f.get(i2)).get(Constants.ACCOUNT_NAME));
            i = i2 + 1;
        }
    }

    private void c() {
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= this.f.size()) {
                return;
            }
            ((HashMap) this.f.get(i2)).put(Constants.ACCOUNT_VISIABLE, this.a);
            i = i2 + 1;
        }
    }

    private void d() {
        this.a = false;
        this.f.remove(0);
        BaseUtils.D("ChooseAccountActivity", "removeButton is run:" + this.f.size());
        c();
        BaseUtils.D("ChooseAccountActivity", "cancleDelAccount is run");
        this.e.notifyDataSetChanged();
    }

    private void e() {
        int i = 0;
        HashMap hashMap = new HashMap();
        hashMap.put(Constants.ACCOUNT_VISIABLE, this.a);
        hashMap.put(Constants.ACCOUNT_VIEW_TYPE, 1);
        this.f.add(0, hashMap);
        BaseUtils.D("ChooseAccountActivity", "addButton is run :" + this.f.size());
        while (true) {
            int i2 = i;
            if (i2 >= this.f.size()) {
                return;
            }
            BaseUtils.D("ChooseAccountActivity", "data " + i2 + ":" + ((HashMap) this.f.get(i2)).get(Constants.ACCOUNT_VIEW_TYPE));
            i = i2 + 1;
        }
    }

    private void f() {
        Intent intent = new Intent(this, (Class<?>) LoginActivity.class);
        PrefUtil.logout(getApplicationContext());
        startActivity(intent);
        finish();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        BaseUtils.D("ChooseAccountActivity", StringUtils.EMPTY);
        if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "btn_add_account")) {
            BaseUtils.D("ChooseAccountActivity", "增加");
            f();
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        Log.d("ChooseAccountActivity", "ChooseAccountActivity onCreate");
        super.onCreate(bundle);
        this.p = GfanUCenter.gfanUCCallback;
        setContentView(BaseUtils.get_R_Layout(getApplicationContext(), "gfan_activity_uc_get_accounts"));
        TopBar.createTopBar(this, new View[]{findViewById(BaseUtils.get_R_id(getApplicationContext(), "top_bar_title"))}, new int[]{0}, getString(BaseUtils.get_R_String(getApplicationContext(), "title_choose_account")));
        this.n = (FrameLayout) findViewById(BaseUtils.get_R_id(getApplicationContext(), "loading_app"));
        this.o = (ProgressBar) findViewById(BaseUtils.get_R_id(getApplicationContext(), "progressbar_app"));
        this.n.setVisibility(0);
        this.o.setVisibility(0);
        this.l = (ListView) findViewById(BaseUtils.get_R_id(getApplicationContext(), "account_lv"));
        this.l.setOnItemClickListener(this);
        this.l.setOnItemLongClickListener(this);
        this.m = (Button) findViewById(BaseUtils.get_R_id(getApplicationContext(), "btn_add_account"));
        this.m.setOnClickListener(this);
        GfanPayAgent.setReportUncaughtExceptions(true);
        this.f = new ArrayList();
        a();
        this.e = new c(this, getApplicationContext(), this.f, this);
        this.l.setAdapter((ListAdapter) this.e);
        this.o.setVisibility(8);
        this.n.setVisibility(8);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        switch (i) {
            case 2:
                return GfanProgressDialog.createProgressDialog(this, getString(BaseUtils.get_R_String(getApplicationContext(), "check_choose")), false, 0L, null);
            case 3:
                return new AlertDialog.Builder(this).setIcon(R.drawable.ic_dialog_info).setTitle(BaseUtils.get_R_String(getApplicationContext(), "dialog_del")).setMessage(getString(BaseUtils.get_R_String(getApplicationContext(), "dialog_del_account"), new Object[]{this.j})).setPositiveButton(BaseUtils.get_R_String(getApplicationContext(), "ok"), new b(this)).setNegativeButton(BaseUtils.get_R_String(getApplicationContext(), "cancel"), new a(this)).show();
            default:
                return super.onCreateDialog(i);
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!this.a.booleanValue()) {
            menu.add(0, 1, 1, getString(BaseUtils.get_R_String(getApplicationContext(), "menu_del")));
            menu.add(0, 2, 2, getString(BaseUtils.get_R_String(getApplicationContext(), "menu_cancle")));
        }
        return true;
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        GfanProgressDialog.dismissDialog();
        switch (i) {
            case 2:
                if (-1 != i2 && -2 != i2) {
                    if (600 == i2) {
                        ToastUtil.showLong(getApplicationContext(), BaseUtils.get_R_String(getApplicationContext(), "error_network_time_out"));
                        return;
                    } else {
                        ToastUtil.showLong(getApplicationContext(), Html.fromHtml(BaseUtils.get_R_String(getApplicationContext(), "error_unknown") + "<br />错误码：" + i2).toString());
                        return;
                    }
                }
                Toast.makeText(getApplicationContext(), BaseUtils.get_R_String(getApplicationContext(), "token_error"), 0).show();
                Intent intent = new Intent(this, (Class<?>) LoginActivity.class);
                intent.putExtra("extra.login.uid", this.i);
                startActivity(intent);
                finish();
                return;
            default:
                return;
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        BaseUtils.D("ChooseAccountActivity", "onItemClick");
        if (!this.a.booleanValue()) {
            PrefUtil.getUid(getApplicationContext());
            this.d = (UserVo) this.c.get(i);
            this.i = this.d.getUid();
            if (isFinishing()) {
                return;
            }
            a(2);
            Api.verifyToken(getApplicationContext(), this, this.d.getToken());
            return;
        }
        if (i != 0) {
            HashMap hashMap = (HashMap) this.f.get(i);
            boolean booleanValue = ((Boolean) hashMap.get(Constants.ACCOUNT_CHECK)).booleanValue();
            BaseUtils.D("ChooseAccountActivity", "position " + i + "onclick " + booleanValue);
            hashMap.put(Constants.ACCOUNT_CHECK, Boolean.valueOf(!booleanValue));
            BaseUtils.D("ChooseAccountActivity", "position " + i + "check " + ((HashMap) this.f.get(i)).get(Constants.ACCOUNT_CHECK));
            this.e.notifyDataSetChanged();
        }
    }

    @Override // android.widget.AdapterView.OnItemLongClickListener
    public boolean onItemLongClick(AdapterView adapterView, View view, int i, long j) {
        if (this.a.booleanValue()) {
            return true;
        }
        this.h = ((Long) ((HashMap) this.f.get(i)).get(Constants.ACCOUNT_UID)).longValue();
        this.j = (String) ((HashMap) this.f.get(i)).get(Constants.ACCOUNT_NAME);
        a(3);
        return true;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0001. Please report as an issue. */
    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        switch (i) {
            case 4:
                if (this.a.booleanValue()) {
                    d();
                    return true;
                }
                if (4 != i) {
                    return true;
                }
                Intent intent = new Intent();
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            case 82:
                if (this.a.booleanValue()) {
                    return true;
                }
            default:
                return super.onKeyDown(i, keyEvent);
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (this.a.booleanValue()) {
            return true;
        }
        BaseUtils.D("ChooseAccountActivity", "onOptionsItemSelected " + menuItem.getItemId());
        switch (menuItem.getItemId()) {
            case 1:
                b();
                return true;
            default:
                return true;
        }
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
            case 2:
                PrefUtil.setUid(getApplicationContext(), this.d.getUid());
                finish();
                User user = new User();
                user.setUid(this.d.getUid());
                user.setToken(this.d.getToken());
                if (this.k == null) {
                    this.k = new DESUtil(getApplicationContext());
                }
                user.setUserName(this.k.getDesAndBase64String(this.d.getUserName()));
                if (this.p != null) {
                    GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_login", "登录机锋");
                    this.p.onSuccess(user, 0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    @Override // com.mappn.sdk.uc.activity.BtnOnClick
    public void setOnClick(View view) {
        int id = view.getId();
        if (id == BaseUtils.get_R_id(getApplicationContext(), "account_header_left")) {
            BaseUtils.D("ChooseAccountActivity", "左");
            d();
            return;
        }
        if (id == BaseUtils.get_R_id(getApplicationContext(), "account_header_right")) {
            BaseUtils.D("ChooseAccountActivity", "右");
            this.g = new ArrayList();
            for (int i = 0; i < this.f.size(); i++) {
                Boolean bool = (Boolean) ((HashMap) this.f.get(i)).get(Constants.ACCOUNT_CHECK);
                if (bool != null && bool.booleanValue()) {
                    this.g.add((Long) ((HashMap) this.f.get(i)).get(Constants.ACCOUNT_UID));
                }
            }
            int size = this.g.size();
            if (size <= 0) {
                d();
                return;
            }
            for (int i2 = 0; i2 < size; i2++) {
                UserDao.deleteUserByUid(getApplicationContext(), ((Long) this.g.get(i2)).longValue());
            }
            this.f.clear();
            this.a = false;
            a();
            this.e.notifyDataSetChanged();
        }
    }
}
