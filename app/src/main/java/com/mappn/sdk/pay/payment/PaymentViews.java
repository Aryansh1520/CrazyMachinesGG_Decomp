package com.mappn.sdk.pay.payment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.GfanProgressDialog;
import com.mappn.sdk.pay.GfanPay;
import com.mappn.sdk.pay.GfanPayService;
import com.mappn.sdk.pay.ServiceConnector;
import com.mappn.sdk.pay.database.PayDB;
import com.mappn.sdk.pay.database.PayVo;
import com.mappn.sdk.pay.model.ChargeType;
import com.mappn.sdk.pay.model.IType;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.net.chain.HandlerProxy;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.DBUtil;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.pay.util.TopBar;
import com.mappn.sdk.pay.util.Utils;
import com.mappn.sdk.pay.weight.CustomAdapter;
import com.mappn.sdk.statitistics.GfanPayAgent;
import com.mappn.sdk.uc.GfanUCenter;
import com.mappn.sdk.uc.User;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
public class PaymentViews implements View.OnClickListener, AdapterView.OnItemClickListener, ApiRequestListener {
    private static PaymentViews a;
    private PaymentsActivity b;
    private Button c;
    private TextView d;
    private TextView e;
    private TextView f;
    private TextView g;
    private ListView h;
    private int i = 3;
    public int mTimeoutCounter;

    private PaymentViews(PaymentsActivity paymentsActivity) {
        this.b = paymentsActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int a(PaymentViews paymentViews, int i) {
        paymentViews.i = 0;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        IType[] iTypeArr;
        IType[] availableChargeType = (PaymentInfo.PAYTYPE_OVERAGE.equals(this.b.mPaymentInfo.getPaymentType()) || "all".equals(this.b.mPaymentInfo.getPaymentType())) ? PrefUtil.getAvailableChargeType(this.b, false) : null;
        ArrayList availablePayType = PrefUtil.getAvailablePayType(this.b, this.b.mPaymentInfo.getPaymentType());
        if (availableChargeType == null) {
            iTypeArr = new IType[availablePayType.size()];
        } else {
            iTypeArr = "all".equals(this.b.mPaymentInfo.getPaymentType()) ? new IType[(availablePayType.size() + availableChargeType.length) - 1] : new IType[availableChargeType.length];
            System.arraycopy(availableChargeType, 0, iTypeArr, 0, availableChargeType.length);
        }
        this.h.setAdapter((ListAdapter) new CustomAdapter(this.b.getApplicationContext(), iTypeArr));
        GfanProgressDialog.dismissDialog();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Failed to check method usage
    java.util.ConcurrentModificationException
    	at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1661)
    	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
    	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:474)
    	at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:913)
    	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
    	at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:578)
    	at jadx.core.codegen.ClassGen.skipMethod(ClassGen.java:361)
    	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:327)
    	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$3(ClassGen.java:301)
    	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
    	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
    	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
     */
    public static /* synthetic */ void a(PaymentViews paymentViews) {
        paymentViews.a();
    }

    private void b() {
        HashMap hashMap = new HashMap();
        hashMap.put("version", Constants.VERSION);
        GfanPayAgent.onEvent(this.b, "gfanapi_pay_step2", hashMap);
        this.b.mType = 2;
        this.b.setContentView(LayoutInflater.from(this.b).inflate(BaseUtils.get_R_Layout(this.b, "gfan_activity_pay_confirm_view"), (ViewGroup) null));
        TopBar.createTopBar(this.b, new View[]{this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_title")), this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_img"))}, new int[]{0, 0}, Constants.TEXT_SUBTITLE_PAYMENT);
        TextView textView = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "confirm_tv_account"));
        textView.setTextSize(16.0f);
        textView.setTextColor(-13487566);
        textView.setText(String.format(Constants.TEXT_PAYMENT_PAY_ACCOUNT, this.b.mPaymentInfo.getUser().getUserName()));
        this.e = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "confirm_tv_balance"));
        this.e.setTextSize(16.0f);
        this.e.setTextColor(-13487566);
        this.e.setText(String.format("账户余额 : %d机锋券", Integer.valueOf(this.b.mPaymentInfo.getGfanBalance())));
        this.d = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "confirm_tv_charge"));
        this.d.setTextSize(16.0f);
        this.d.setTextColor(-13487566);
        this.d.setFocusable(true);
        this.d.setClickable(true);
        this.d.setLinkTextColor(-24576);
        this.d.setText(Html.fromHtml(Constants.TEXT_PAYMENT_CHARGE_LINK));
        this.d.setOnClickListener(this);
        TextView textView2 = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "confirm_tv_pay_name"));
        textView2.setTextSize(16.0f);
        textView2.setTextColor(-13487566);
        textView2.setText(String.format("支付名称 : %s", this.b.mPaymentInfo.getOrder().getPayName()));
        this.f = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "confirm_tv_product_name"));
        this.f.setTextSize(16.0f);
        this.f.setText(Constants.TEXT_PAYMENT_PAY_NAME);
        this.f.setTextColor(-13487566);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(String.format(Constants.TEXT_PAYMENT_PAYMENT, Integer.valueOf(this.b.mPaymentInfo.getOrder().getMoney())));
        spannableStringBuilder.setSpan(new ForegroundColorSpan(-24576), 7, String.valueOf(this.b.mPaymentInfo.getOrder().getMoney()).length() + 7, 33);
        TextView textView3 = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "confirm_tv_num"));
        textView3.setTextSize(16.0f);
        textView3.setText(spannableStringBuilder);
        textView3.setTextColor(-13487566);
        TextView textView4 = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "confirm_tv_content"));
        textView4.setTextSize(16.0f);
        textView4.setText(String.format(Constants.TEXT_PAYMENT_CONTENT, this.b.mPaymentInfo.getOrder().getPayDesc()));
        textView4.setTextColor(-13487566);
        this.c = (Button) this.b.findViewById(BaseUtils.get_R_id(this.b, "confirm_btn_pay"));
        this.c.setText(Constants.TEXT_PAYMENT_CONFIRME_PAY);
        this.c.setOnClickListener(this);
        this.c.setVisibility(0);
        Api.queryAppname(this.b, this, this.b.mPaymentInfo.getAppkey());
    }

    public static synchronized PaymentViews instance(PaymentsActivity paymentsActivity) {
        PaymentViews paymentViews;
        synchronized (PaymentViews.class) {
            if (a == null) {
                a = new PaymentViews(paymentsActivity);
            }
            paymentViews = a;
        }
        return paymentViews;
    }

    public void confirmEnterPaymentPoint() {
        PrefUtil.increaseArriveCount(this.b.getApplicationContext());
        PrefUtil.confirmEnterPaymentPoint(this.b.getApplicationContext());
    }

    public View generateFooterView() {
        View inflate = LayoutInflater.from(this.b).inflate(BaseUtils.get_R_Layout(this.b, "gfan_pay_footview"), (ViewGroup) null);
        this.g = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "footview_tv_charge_note"));
        this.g.setText(Html.fromHtml(Constants.TEXT_PAYMENT_CHARGE_INFO_TITLE));
        this.g.setLinkTextColor(-24576);
        this.g.setOnClickListener(this);
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
        View inflate = LayoutInflater.from(this.b).inflate(BaseUtils.get_R_Layout(this.b, "gfan_pay_headerview"), (ViewGroup) null);
        RelativeLayout relativeLayout = (RelativeLayout) inflate.findViewById(BaseUtils.get_R_id(this.b, "headerview_hint_layout"));
        relativeLayout.setVisibility(8);
        TextView textView = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "headerview_tv_payaccount"));
        textView.setTextSize(16.0f);
        textView.setTextColor(-13487566);
        textView.setText(String.format(Constants.TEXT_CHARGE_USER, this.b.mPaymentInfo.getUser().getUserName()));
        TextView textView2 = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "headerview_tv_account_balance"));
        textView2.setTextSize(16.0f);
        textView2.setTextColor(-13487566);
        textView2.setText(String.format("账户余额 : %d机锋券", Integer.valueOf(this.b.mPaymentInfo.getGfanBalance())));
        Drawable drawable = this.b.getResources().getDrawable(BaseUtils.get_R_Drawable(this.b, Utils.isHdpi() ? Constants.RES_LIST_ITEM_DOWN_ICON_HDPI : Constants.RES_LIST_ITEM_DOWN_ICON));
        textView2.setCompoundDrawablePadding(6);
        textView2.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, drawable, (Drawable) null);
        textView2.setOnClickListener(new b(this, relativeLayout, textView2));
        TextView textView3 = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "headerview_tv_desc"));
        textView3.setTextSize(16.0f);
        textView3.setText(String.format("支付名称 : %s", this.b.mPaymentInfo.getOrder().getPayName()));
        textView3.setTextColor(-13487566);
        this.f = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "headerview_tv_product_name"));
        this.f.setTextSize(16.0f);
        this.f.setText(String.format(Constants.TEXT_PAYMENT_PRODUCT, this.b.mPaymentInfo.getOrder().getNumber()));
        this.f.setTextColor(-13487566);
        TextView textView4 = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "headerview_tv_pay_money"));
        textView4.setTextSize(16.0f);
        textView4.setText(String.format(Constants.TEXT_PAYMENT_PAYMENT, Integer.valueOf(this.b.mPaymentInfo.getOrder().getMoney())));
        textView4.setTextColor(-13487566);
        TextView textView5 = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.b, "headerview_tv_content"));
        textView5.setTextSize(16.0f);
        textView5.setText(String.format(Constants.TEXT_PAYMENT_CONTENT, this.b.mPaymentInfo.getOrder().getPayDesc()));
        textView5.setTextColor(-13487566);
        Drawable drawable2 = this.b.getResources().getDrawable(BaseUtils.get_R_Drawable(this.b, Utils.isHdpi() ? Constants.RES_LIST_ITEM_UP_ICON_HDPI : Constants.RES_LIST_ITEM_UP_ICON));
        textView5.setCompoundDrawablePadding(6);
        textView5.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, drawable2, (Drawable) null);
        textView5.setOnClickListener(new c(this, relativeLayout, textView2));
        Api.queryAppname(this.b, this, this.b.mPaymentInfo.getAppkey());
        return inflate;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.c) {
            HashMap hashMap = new HashMap();
            hashMap.put("version", Constants.VERSION);
            GfanPayAgent.onEvent(this.b.getApplicationContext(), "gfanapi_pay_step10", hashMap);
            this.c.setEnabled(false);
            pay();
            return;
        }
        if (view == null) {
            GfanUCenter.chooseAccount(this.b, new d(this));
            return;
        }
        if (view != this.d) {
            if (view == this.g) {
                this.b.showDialog(28);
            }
        } else {
            HashMap hashMap2 = new HashMap();
            hashMap2.put("version", Constants.VERSION);
            GfanPayAgent.onEvent(this.b.getApplicationContext(), "gfanapi_pay_step3", hashMap2);
            startChargeActivity();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDestroy() {
        BaseUtils.D("PaymentViews", "onDestroy ********************************");
        a = null;
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        this.b.mErrorCode = i2;
        switch (i) {
            case 3:
                switch (i2) {
                    case -1:
                        this.mTimeoutCounter++;
                        if (this.mTimeoutCounter < 3) {
                            Api.pay(this.b, this, this.b.mPaymentInfo);
                            return;
                        }
                        this.mTimeoutCounter = 0;
                        if (this.i == 3) {
                            this.c.setEnabled(true);
                        }
                        GfanProgressDialog.dismissDialog();
                        this.b.showDialog(10);
                        return;
                    case Constants.ERROR_CODE_INSUFFICIENT_BALANCE /* 219 */:
                        queryBalance();
                        GfanProgressDialog.dismissDialog();
                        return;
                    case Constants.ERROR_CODE_ARG_OUT_OF_SCROPE /* 425 */:
                        if (this.i == 3) {
                            this.c.setEnabled(true);
                        }
                        GfanProgressDialog.dismissDialog();
                        this.b.showDialog(8);
                        return;
                    case Constants.ERROR_CODE_APPKEY_WRONG /* 426 */:
                        if (this.i == 3) {
                            this.c.setEnabled(true);
                        }
                        GfanProgressDialog.dismissDialog();
                        this.b.showDialog(4);
                        return;
                    case 500:
                        if (this.i == 3) {
                            this.c.setEnabled(true);
                        }
                        GfanProgressDialog.dismissDialog();
                        this.b.showDialog(11);
                        return;
                    default:
                        if (this.i == 3) {
                            this.c.setEnabled(true);
                        }
                        GfanProgressDialog.dismissDialog();
                        this.b.showDialog(7);
                        return;
                }
            case 4:
                this.f.setText(Constants.TEXT_PAYMENT_GET_PAYNAME_FAILED);
                return;
            case 18:
                GfanProgressDialog.dismissDialog();
                this.b.showDialog(15);
                return;
            default:
                return;
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        IType item = ((CustomAdapter) ((HeaderViewListAdapter) adapterView.getAdapter()).getWrappedAdapter()).getItem(i - 1);
        String id = item.getId();
        if (TypeFactory.TYPE_PAY_JIFENGQUAN.equals(id)) {
            b();
        } else if ("sms".equals(id)) {
            this.b.showSmsPaymentView();
        } else if (item instanceof ChargeType) {
            startChargeActivity(id);
        }
    }

    public void onPaySuccess() {
        if (ServiceConnector.getInstance(this.b.getApplicationContext()).getIsConnected()) {
            User user = new User();
            user.setToken(this.b.mPaymentInfo.getUser().getToken());
            user.setUid(this.b.mPaymentInfo.getUser().getUid());
            user.setUserName(this.b.mPaymentInfo.getUser().getUserName());
            this.b.sendBroadcast(new Intent(BaseUtils.getPayBroadcast(this.b.getApplicationContext())).putExtra(GfanPayService.EXTRA_KEY_TYPE, 0).putExtra("com.mappn.sdk.order", this.b.mPaymentInfo.getOrder()).putExtra(GfanPayService.EXTRA_KEY_USER, user));
        } else {
            PayVo payVo = new PayVo();
            HashMap hashMap = new HashMap();
            hashMap.put("com.mappn.sdk.order", this.b.mPaymentInfo.getOrder());
            payVo.setPayParameters(hashMap);
            PayDB.insertPayVo(this.b.getApplicationContext(), payVo);
        }
        PrefUtil.setLoginFlag(this.b.getApplicationContext(), false);
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onSuccess(int i, Object obj) {
        switch (i) {
            case 3:
                this.b.mPaymentInfo.getOrder().setNumber((String) obj);
                Api.confirmPayResult(this.b, this, this.b.mPaymentInfo.getOrder().getOrderID(), this.b.mPaymentInfo.getAppkey());
                GfanProgressDialog.dismissDialog();
                this.b.showDialog(13);
                return;
            case 4:
                String str = (String) obj;
                this.f.setText(TextUtils.isEmpty(str) ? Constants.TEXT_PAYMENT_GET_PAYNAME_FAILED : Constants.TEXT_PAYMENT_PAY_NAME + str);
                BaseUtils.D("onSuccess", "onSuccess4");
                GfanProgressDialog.dismissDialog();
                return;
            case 18:
                try {
                    this.b.mPaymentInfo.setGfanBalance(Integer.parseInt((String) obj));
                } catch (Exception e) {
                    this.b.mPaymentInfo.setGfanBalance(0);
                }
                if (this.b.mPaymentInfo.getGfanBalance() >= this.b.mPaymentInfo.getOrder().getMoney()) {
                    if (this.i != 1 && this.i != 2) {
                        b();
                        return;
                    }
                    HashMap hashMap = new HashMap();
                    hashMap.put("version", Constants.VERSION);
                    GfanPayAgent.onEvent(this.b.getApplicationContext(), "gfanapi_pay_step10", hashMap);
                    pay();
                    this.i = 0;
                    return;
                }
                HashMap hashMap2 = new HashMap();
                hashMap2.put("version", Constants.VERSION);
                GfanPayAgent.onEvent(this.b.getApplicationContext(), "gfanapi_pay_step4", hashMap2);
                this.b.setContentView(BaseUtils.get_R_Layout(this.b, "gfan_activity_pay_chargeview_list"));
                TopBar.createTopBar(this.b, new View[]{this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_title")), this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_img"))}, new int[]{0, 0}, Constants.TEXT_SUBTITLE_CHARGE);
                this.b.mType = 1;
                this.h = (ListView) this.b.findViewById(BaseUtils.get_R_id(this.b, "payment_charge_lv"));
                this.h.addHeaderView(generateHeaderView(), null, false);
                this.h.setDivider(new ColorDrawable(Constants.COLOR_LISTVIEW_DIVIDER));
                this.h.setOnItemClickListener(this);
                this.h.addFooterView(generateFooterView(), null, true);
                a();
                new HandlerProxy(this.b, new a(this)).handleRequest();
                return;
            default:
                return;
        }
    }

    protected void pay() {
        this.b.showDialog(6);
        if (2 != com.mappn.sdk.uc.util.PrefUtil.getUtype(this.b)) {
            Api.pay(this.b, this, this.b.mPaymentInfo);
        }
    }

    public void queryBalance() {
        BaseUtils.D(DBUtil.TABLE_PAY, "PaymentViews queryBalance !");
        Api.queryUserProfile(this.b, this.b.mPaymentInfo.getUser().getUid(), this);
    }

    protected void startChargeActivity() {
        String defaultChargeType = PrefUtil.getDefaultChargeType(this.b);
        this.i = 2;
        GfanPay.getInstance(this.b.getApplicationContext()).chargeByType(defaultChargeType, this.b, this.b.mPaymentInfo, new e(this));
    }

    public void startChargeActivity(String str) {
        if ("alipay".equals(str)) {
            this.b.mToCancleDialog = true;
        }
        this.i = 1;
        GfanPay.getInstance(this.b.getApplicationContext()).chargeByType(str, this.b, this.b.mPaymentInfo, new f(this));
    }
}
