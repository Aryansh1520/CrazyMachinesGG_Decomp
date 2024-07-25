package com.mappn.sdk.pay.chargement;

import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.model.IType;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.pay.util.TopBar;
import com.mappn.sdk.pay.weight.CustomAdapter;
import com.mappn.sdk.statitistics.GfanPayAgent;
import com.mappn.sdk.uc.GfanUCenter;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ChargeTypeListFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static ChargeTypeListFragment a;
    private ChargeActivity b;
    private TextView c;
    private TextView d;
    private ListView e;
    private IType[] f;

    private ChargeTypeListFragment(ChargeActivity chargeActivity) {
        this.b = chargeActivity;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i) {
        if (this.b.mType == null || this.b.mViewStacks.empty() || (!this.b.mViewStacks.empty() && !((String) this.b.mViewStacks.peek()).equals(ChargeActivity.TYPE_CHARGE_TYPE_LIST))) {
            this.b.mViewStacks.push(ChargeActivity.TYPE_CHARGE_TYPE_LIST);
        }
        this.b.mType = this.f[i - 1].getId();
        if (TypeFactory.TYPE_CHARGE_PHONECARD.equals(this.b.mType)) {
            HashMap hashMap = new HashMap();
            hashMap.put("version", Constants.VERSION);
            GfanPayAgent.onEvent(this.b, "gfanapi_pay_step8", hashMap);
        } else if ("alipay".equals(this.b.mType)) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put("version", Constants.VERSION);
            GfanPayAgent.onEvent(this.b, "gfanapi_pay_step9", hashMap2);
        } else {
            String str = this.b.mType;
        }
        this.d.setVisibility(8);
        this.b.showViewByType(this.b.mType);
    }

    public static synchronized ChargeTypeListFragment instance(ChargeActivity chargeActivity) {
        ChargeTypeListFragment chargeTypeListFragment;
        synchronized (ChargeTypeListFragment.class) {
            if (a == null) {
                a = new ChargeTypeListFragment(chargeActivity);
            }
            chargeTypeListFragment = a;
        }
        return chargeTypeListFragment;
    }

    public View generateFooterView() {
        View inflate = LayoutInflater.from(this.b).inflate(BaseUtils.get_R_Layout(this.b, "gfan_pay_footview"), (ViewGroup) null);
        this.c = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "footview_tv_charge_note"));
        this.c.setText(Html.fromHtml(Constants.TEXT_PAYMENT_CHARGE_INFO_TITLE));
        this.c.setLinkTextColor(-24576);
        this.c.setOnClickListener(this);
        TextView textView = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "footview_left"));
        textView.setText(Constants.TEXT_CONTACT_INFO);
        textView.setTextColor(-13487566);
        TextView textView2 = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "footview_right"));
        textView2.setFocusable(true);
        textView2.setClickable(true);
        textView2.setLinkTextColor(-24576);
        textView2.setText(Constants.TEXT_CONTACT_VALUE);
        Linkify.addLinks(textView2, 6);
        return inflate;
    }

    public View generateHeaderView() {
        View inflate = LayoutInflater.from(this.b).inflate(BaseUtils.get_R_Layout(this.b, "gfan_error_view"), (ViewGroup) null);
        this.d = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "error_tv"));
        this.d.setTextColor(-13487566);
        this.d.setBackgroundColor(Constants.COLOR_ERROR_BACKGROUND);
        return inflate;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.c) {
            this.b.showDialog(21);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDestroy() {
        BaseUtils.D("ChargeTypeListFrgment", "onDestroy ********************************");
        a = null;
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        if (i == 0) {
            return;
        }
        if (!PrefUtil.isLogin(this.b.getApplicationContext()) || this.b.mPaymentInfo.getUser() == null) {
            GfanUCenter.login(this.b, new m(this, i));
        } else {
            a(i);
        }
    }

    public void showListView() {
        this.b.setContentView(BaseUtils.get_R_Layout(this.b, "gfan_activity_pay_chargeview_list"));
        TopBar.createTopBar(this.b, new View[]{this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_title")), this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_img"))}, new int[]{0, 0}, Constants.TEXT_CHARGE_CHOOSE_TYPE);
        this.e = (ListView) this.b.findViewById(BaseUtils.get_R_id(this.b, "payment_charge_lv"));
        this.e.addHeaderView(generateHeaderView(), null, true);
        this.e.addFooterView(generateFooterView(), null, true);
        this.e.setDivider(new ColorDrawable(Constants.COLOR_LISTVIEW_DIVIDER));
        this.e.setBackgroundColor(-1);
        this.e.setCacheColorHint(-1);
        this.e.setOnItemClickListener(this);
        this.f = PrefUtil.getAvailableChargeType(this.b, false);
        this.e.setAdapter((ListAdapter) new CustomAdapter(this.b, this.f));
        boolean hasError = this.b.hasError();
        ChargeActivity chargeActivity = this.b;
        if (!hasError) {
            this.d.setVisibility(8);
            return;
        }
        String name = TypeFactory.factory(this.b.mType, this.b).getName();
        if (PrefUtil.supportChargeType(this.b, this.b.mType)) {
            this.d.setText(String.format(Constants.TEXT_CHARGE_TYPE_FAILED, name));
        } else {
            this.d.setText(String.format(Constants.TEXT_CHARGE_TYPE_NOT_AVAILABLE, name));
        }
        this.d.setVisibility(0);
    }
}
