package com.mappn.sdk.uc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.ToastUtil;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.statitistics.GfanPayAgent;
import com.mappn.sdk.uc.GfanUCCallback;
import com.mappn.sdk.uc.GfanUCenter;
import com.mappn.sdk.uc.User;
import com.mappn.sdk.uc.net.Api;
import com.mappn.sdk.uc.util.PrefUtil;
import com.mappn.sdk.uc.util.TopBar;
import com.mokredit.payment.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class InviteFriendsActivity extends Activity implements View.OnClickListener, ApiRequestListener {
    List a;
    private GfanUCCallback c;
    private ListView d;
    private UserListAdapter e;
    private String f;
    private EditText g;
    private Button h;
    private ProgressDialog j;
    List b = new ArrayList();
    private int i = 0;

    /* loaded from: classes.dex */
    public class OnFriendChangeListener implements CompoundButton.OnCheckedChangeListener {
        private Long a;

        public OnFriendChangeListener(long j) {
            this.a = Long.valueOf(j);
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (z) {
                InviteFriendsActivity.this.b.add(this.a);
            } else {
                InviteFriendsActivity.this.b.remove(this.a);
            }
        }
    }

    /* loaded from: classes.dex */
    public class UserListAdapter extends BaseAdapter {

        /* loaded from: classes.dex */
        public final class ViewHolder {
            public CheckBox checkbox;
            public TextView title;

            public ViewHolder(UserListAdapter userListAdapter) {
            }
        }

        public UserListAdapter() {
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (InviteFriendsActivity.this.a == null) {
                return 0;
            }
            return InviteFriendsActivity.this.a.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            if (InviteFriendsActivity.this.a == null || InviteFriendsActivity.this.a.size() <= i) {
                return null;
            }
            return InviteFriendsActivity.this.a.get(i);
        }

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            if (getItem(i) != null) {
                return ((User) getItem(i)).getUid();
            }
            return 0L;
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            LayoutInflater from = LayoutInflater.from(InviteFriendsActivity.this);
            if (view == null) {
                viewHolder = new ViewHolder(this);
                view = from.inflate(BaseUtils.get_R_Layout(InviteFriendsActivity.this.getApplicationContext(), "gfan_item_uc_friends"), (ViewGroup) null);
                viewHolder.title = (TextView) view.findViewById(BaseUtils.get_R_id(InviteFriendsActivity.this.getApplicationContext(), "uname"));
                viewHolder.checkbox = (CheckBox) view.findViewById(BaseUtils.get_R_id(InviteFriendsActivity.this.getApplicationContext(), "checkbox"));
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.title.setText(((User) getItem(i)).getUserName());
            viewHolder.checkbox.setOnCheckedChangeListener(new OnFriendChangeListener(getItemId(i)));
            return view;
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == BaseUtils.get_R_id(getApplicationContext(), "btn_cancel")) {
            finish();
            return;
        }
        if (view.getId() != BaseUtils.get_R_id(getApplicationContext(), "btn_invite")) {
            return;
        }
        if (this.i != 1) {
            this.i = 1;
            this.d.setVisibility(8);
            this.g.setVisibility(0);
            this.h.setText(getString(BaseUtils.get_R_String(getApplicationContext(), "ok")));
            this.g.setText("我从机锋市场下载了《" + BaseUtils.getAppName(this) + "》，大家来陪我一起玩吧！还有数十万免费android软件和游戏，快来一起玩吧。" + this.f);
            return;
        }
        if (this.b == null || this.b.size() == 0) {
            return;
        }
        long valueOf = Long.valueOf(PrefUtil.getUid(this));
        if (BaseUtils.sDebug) {
            valueOf = 6L;
        }
        String str = StringUtils.EMPTY;
        Iterator it = this.b.iterator();
        while (true) {
            String str2 = str;
            if (!it.hasNext()) {
                String replaceFirst = str2.replaceFirst(Constants.TERM, StringUtils.EMPTY);
                showDialog(8);
                Api.sentMsg(this, this, valueOf, replaceFirst, "我发现这个游戏不错，你也来试试吧", this.g.getText().toString().replace(BaseUtils.getAppName(this), "[url=" + this.f + "]" + BaseUtils.getAppName(this) + "[/url]"));
                return;
            }
            str = str2 + Constants.TERM + ((Long) it.next());
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.c = GfanUCenter.gfanUCCallback;
        if (!isFinishing()) {
            showDialog(8);
        }
        setContentView(BaseUtils.get_R_Layout(getApplicationContext(), "gfan_activity_uc_invite"));
        TopBar.createTopBar(this, new View[]{findViewById(BaseUtils.get_R_id(getApplicationContext(), "top_bar_title"))}, new int[]{0}, getString(BaseUtils.get_R_String(getApplicationContext(), "invite_friends")));
        this.d = (ListView) findViewById(BaseUtils.get_R_id(getApplicationContext(), "list"));
        this.g = (EditText) findViewById(BaseUtils.get_R_id(getApplicationContext(), "msg"));
        findViewById(BaseUtils.get_R_id(getApplicationContext(), "btn_cancel")).setOnClickListener(this);
        this.h = (Button) findViewById(BaseUtils.get_R_id(getApplicationContext(), "btn_invite"));
        this.h.setOnClickListener(this);
        if (PrefUtil.isLogin(getApplicationContext())) {
            String packageName = BaseUtils.getPackageName(this);
            if (BaseUtils.sDebug) {
                packageName = "com.qihoo360.mobilesafe";
            }
            Api.getApkUrl(this, this, packageName);
        } else {
            Toast.makeText(this, getString(BaseUtils.get_R_String(getApplicationContext(), "not_login")), 1).show();
            ToastUtil.showLong(this, getString(BaseUtils.get_R_String(getApplicationContext(), "not_login")));
            finish();
        }
        GfanPayAgent.setReportUncaughtExceptions(true);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        if (i != 8) {
            return super.onCreateDialog(i);
        }
        this.j = new ProgressDialog(this);
        this.j.setProgressStyle(0);
        this.j.setMessage(getString(BaseUtils.get_R_String(getApplicationContext(), "invite_friends")));
        return this.j;
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        this.c.onError(i);
        finish();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (4 != i) {
            return true;
        }
        finish();
        if (this.c == null) {
            return true;
        }
        this.c.onError(0);
        return true;
    }

    @Override // android.app.Activity
    protected void onPrepareDialog(int i, Dialog dialog) {
        super.onPrepareDialog(i, dialog);
        if (dialog.isShowing()) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                BaseUtils.D("InviteFriendsActivity", e.getMessage());
            }
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onSuccess(int i, Object obj) {
        if (i == 6) {
            BaseUtils.D("InviteFriendsActivity", obj.toString());
            this.a = (List) obj;
            dismissDialog(8);
            if (this.a != null) {
                BaseUtils.D("InviteFriendsActivity", "get frieds:" + this.a.size());
            }
            this.e = new UserListAdapter();
            if (this.d != null) {
                this.d.setAdapter((ListAdapter) this.e);
                return;
            }
            return;
        }
        if (i != 5) {
            if (i == 7) {
                this.c.onSuccess(null, i);
                BaseUtils.D("InviteFriendsActivity", "sent msg success");
                finish();
                return;
            }
            return;
        }
        BaseUtils.D("InviteFriendsActivity", obj.toString());
        this.f = obj.toString();
        long valueOf = Long.valueOf(PrefUtil.getUid(this));
        if (BaseUtils.sDebug) {
            valueOf = 6L;
        }
        Api.getFriendList(this, this, valueOf, 1, 200);
    }
}
