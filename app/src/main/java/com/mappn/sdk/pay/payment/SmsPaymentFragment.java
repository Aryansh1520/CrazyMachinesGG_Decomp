package com.mappn.sdk.pay.payment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.sqlite.SQLiteAbortException;
import android.net.Uri;
import android.os.Handler;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.GfanPayService;
import com.mappn.sdk.pay.ServiceConnector;
import com.mappn.sdk.pay.net.chain.SyncSmsInfoHandler;
import com.mappn.sdk.pay.payment.sms.SimCardNotSupportException;
import com.mappn.sdk.pay.payment.sms.SmsInfo;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.pay.util.Utils;
import com.mappn.sdk.pay.weight.AbsFragment;
import com.mongodb.util.TimeConstants;

/* loaded from: classes.dex */
public class SmsPaymentFragment extends AbsFragment implements View.OnClickListener {
    private static SmsPaymentFragment a;
    private RelativeLayout b;
    private TextView c;
    private TextView d;
    private Button e;
    private Button f;
    private SmsInfo h;
    private int l;
    private ContentObserver m;
    public String mSmsResultInfo;
    private Handler n;
    private String o;
    private boolean g = false;
    private int i = -1;
    private int j = this.i;
    private boolean k = false;
    private final BroadcastReceiver p = new m(this);
    private final Runnable q = new n(this);

    private SmsPaymentFragment() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int F(SmsPaymentFragment smsPaymentFragment) {
        int i = smsPaymentFragment.i - 1;
        smsPaymentFragment.i = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i) {
        String format;
        String format2;
        a("fillView", 1, "come in");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(150, -2);
        try {
            int payedAmount = PrefUtil.getPayedAmount(this.mActivity);
            a("fillView", 2, "try");
            if (payedAmount == 0) {
                a("fillView", 3, "payedAmount[支付过的金额] == 0");
                if (this.h == null) {
                    a("fillView", 4, "mSmsInfo[短信列表] == null");
                    this.h = Utils.getSmsInfos().filterSmsInfo(this.mActivity, i);
                    this.h.buildExtInfo(this.mActivity, ((PaymentsActivity) this.mActivity).mPaymentInfo);
                }
                if (i == this.h.money) {
                    a("fillView", 5, "if -- smsPayment[需要支付的金额] == mSmsInfo.money[短信支付代码对应的金额],发送一条 ");
                    this.c.setText(String.format(Constants.TEXT_PAY_SMS_INFO, this.h.getNumber(), ((PaymentsActivity) this.mActivity).mPaymentInfo.getOrder().getPayName(), Integer.valueOf(i), Utils.getSmsInfos().supportTel));
                } else {
                    a("fillView", 5, "else -- smsPayment[需要支付的金额] != mSmsInfo.money[短信支付代码对应的金额],发送一条 ");
                    int i2 = i / this.h.money;
                    if (this.j == -1) {
                        a("fillView", 5, "if -- mLeftSmsToSendCount == -1[显示第一屏提示]");
                        this.j = i2;
                        this.i = i2;
                        format2 = String.format(Constants.TEXT_PAY_SMS_MULTIPLE_INFO, Integer.valueOf(this.j), Integer.valueOf(this.h.money), ((PaymentsActivity) this.mActivity).mPaymentInfo.getOrder().getPayName(), Integer.valueOf(i));
                        this.k = true;
                    } else {
                        format2 = String.format(Constants.TEXT_PAY_SMS_MULTIPLE_INFO_MORE, Integer.valueOf((i2 - this.j) + 1));
                        this.f.setVisibility(8);
                        this.e.setText(Constants.TEXT_OK);
                        layoutParams.addRule(14, -1);
                    }
                    this.c.setText(format2);
                    this.d.setText(this.h.getInfoBeforeSend());
                }
            } else {
                int i3 = i - payedAmount;
                if (this.h == null) {
                    this.h = Utils.getSmsInfos().filterSmsInfo(this.mActivity, i3);
                    this.h.buildExtInfo(this.mActivity, ((PaymentsActivity) this.mActivity).mPaymentInfo);
                }
                if (this.j == -1) {
                    this.k = true;
                    int i4 = i3 / this.h.money;
                    this.j = i4;
                    this.i = i4;
                    format = String.format(Constants.TEXT_PAY_SMS_INFO_CONTINUE, Integer.valueOf(payedAmount), ((PaymentsActivity) this.mActivity).mPaymentInfo.getOrder().getPayName(), Integer.valueOf(this.j), Integer.valueOf(this.h.money));
                } else {
                    format = String.format(Constants.TEXT_PAY_SMS_MULTIPLE_INFO_MORE, Integer.valueOf((i3 - this.j) + 1));
                    this.f.setVisibility(8);
                    this.e.setText(Constants.TEXT_OK);
                    layoutParams.addRule(14, -1);
                }
                this.c.setText(format);
                this.d.setText(this.h.getInfoBeforeSend());
            }
            Linkify.addLinks(this.c, 4);
            Linkify.addLinks(this.d, 4);
        } catch (SimCardNotSupportException e) {
            this.mSmsResultInfo = e.getMessage();
            ((PaymentsActivity) this.mActivity).showDialog(16);
        }
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(150, -2);
        layoutParams2.addRule(11, -1);
        layoutParams2.addRule(12, -1);
        layoutParams2.setMargins(10, 20, 10, 10);
        this.f.setLayoutParams(layoutParams2);
        layoutParams.addRule(9, -1);
        layoutParams.addRule(12, -1);
        layoutParams.setMargins(10, 20, 10, 10);
        this.e.setLayoutParams(layoutParams);
        this.b.addView(this.e);
        this.b.addView(this.f);
    }

    private void a(SmsInfo smsInfo) {
        ((PaymentsActivity) this.mActivity).showDialog(17);
        smsInfo.sendFirstSms(this.mActivity);
    }

    private void a(String str) {
        ((PaymentsActivity) this.mActivity).showDialog(17);
        SmsInfo.sendSms(this.mActivity, str.split(",,,,")[0], str.split(",,,,")[1]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void a(String str, int i, String str2) {
        Log.i("SmsPaymentFragment", str + "--" + String.valueOf(i) + "--" + str2 + "--");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean a(SmsPaymentFragment smsPaymentFragment, boolean z) {
        smsPaymentFragment.g = true;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int i(SmsPaymentFragment smsPaymentFragment) {
        int i = smsPaymentFragment.j - 1;
        smsPaymentFragment.j = i;
        return i;
    }

    public static synchronized SmsPaymentFragment instance() {
        SmsPaymentFragment smsPaymentFragment;
        synchronized (SmsPaymentFragment.class) {
            if (a == null) {
                a = new SmsPaymentFragment();
            }
            smsPaymentFragment = a;
        }
        return smsPaymentFragment;
    }

    public void buildSmsConfirmView(String str, String str2, String str3) {
        ((PaymentsActivity) this.mActivity).mType = 4;
        try {
            Utils.CheckSimCardSupprotInfo(this.mActivity);
            RelativeLayout relativeLayout = new RelativeLayout(this.mActivity);
            relativeLayout.setBackgroundColor(-1);
            relativeLayout.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            View initSubTitle = Utils.initSubTitle(this.mActivity, Constants.TEXT_SUBTITLE_PAYMENT);
            initSubTitle.setId(1);
            initSubTitle.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
            relativeLayout.addView(initSubTitle);
            TextView textView = new TextView(this.mActivity);
            textView.setId(2);
            textView.setTextSize(16.0f);
            textView.setTextColor(-13487566);
            textView.setPadding(10, 10, 0, 0);
            textView.setFocusable(true);
            textView.setClickable(true);
            textView.setLinkTextColor(-24576);
            textView.setText(String.format(Constants.TEXT_PAY_SMS_CONFIRM_INFO, str, ((PaymentsActivity) this.mActivity).mPaymentInfo.getOrder().getPayName(), Integer.valueOf(Utils.getSmsPayment()), str3));
            Linkify.addLinks(textView, 4);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams.addRule(3, 1);
            textView.setLayoutParams(layoutParams);
            relativeLayout.addView(textView);
            Button button = new Button(this.mActivity);
            button.setText(Constants.TEXT_NEXT);
            button.setId(5);
            this.o = str + ",,,," + str2;
            button.setTag(this.o);
            button.setOnClickListener(this);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(150, -2);
            layoutParams2.addRule(12, -1);
            layoutParams2.setMargins(10, 20, 10, 10);
            button.setLayoutParams(layoutParams2);
            relativeLayout.addView(button);
            Button button2 = new Button(this.mActivity);
            button2.setText("取消");
            button2.setId(6);
            button2.setOnClickListener(this);
            RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(150, -2);
            layoutParams3.addRule(11, -1);
            layoutParams3.addRule(12, -1);
            layoutParams3.setMargins(10, 20, 10, 10);
            button2.setLayoutParams(layoutParams3);
            relativeLayout.addView(button2);
            ((PaymentsActivity) this.mActivity).setContentView(relativeLayout);
        } catch (SimCardNotSupportException e) {
            this.mSmsResultInfo = e.getMessage();
            ((PaymentsActivity) this.mActivity).showDialog(16);
        }
    }

    @Override // com.mappn.sdk.pay.weight.AbsFragment
    public View buildView() {
        ((PaymentsActivity) this.mActivity).mType = 3;
        a("buildView", 0, "come in");
        this.b = new RelativeLayout(this.mActivity);
        this.b.setBackgroundColor(-1);
        this.b.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        RelativeLayout initSubTitle = Utils.initSubTitle(this.mActivity, Constants.TEXT_SUBTITLE_PAYMENT);
        initSubTitle.setId(1);
        initSubTitle.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        this.b.addView(initSubTitle);
        this.c = new TextView(this.mActivity);
        this.c.setId(2);
        this.c.setTextSize(18.0f);
        this.c.setTextColor(-13487566);
        this.c.setPadding(10, 10, 0, 0);
        this.c.setFocusable(true);
        this.c.setClickable(true);
        this.c.setLinkTextColor(-24576);
        this.c.setText(Constants.TEXT_PAY_SMS_INFO_SYNC);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(3, 1);
        this.c.setLayoutParams(layoutParams);
        this.b.addView(this.c);
        this.d = new TextView(this.mActivity);
        this.d.setId(3);
        this.d.setTextSize(16.0f);
        this.d.setTextColor(-7829368);
        this.d.setPadding(10, 30, 0, 0);
        this.d.setFocusable(true);
        this.d.setClickable(true);
        this.d.setLinkTextColor(-24576);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams2.addRule(3, 2);
        this.d.setLayoutParams(layoutParams2);
        this.b.addView(this.d);
        this.e = new Button(this.mActivity);
        this.e.setText(Constants.TEXT_NEXT);
        this.e.setId(3);
        this.e.setOnClickListener(this);
        this.f = new Button(this.mActivity);
        this.f.setText("取消");
        this.f.setId(4);
        this.f.setOnClickListener(this);
        try {
            Utils.CheckSimCardSupprotInfo(this.mActivity);
            a("buildView", 1, "try + CheckSimCardSupprotInfo");
            int smsPayment = Utils.getSmsPayment();
            if (smsPayment <= PrefUtil.getPayedAmount(this.mActivity)) {
                a("buildView", 3, "smsPayment<短信支付金额>");
                this.mSmsResultInfo = null;
                ((PaymentsActivity) this.mActivity).showDialog(20);
                return this.b;
            }
            if (this.g) {
                a("buildView", 6, "mIsSynced=" + this.g + "else已经同步");
                a(smsPayment);
            } else {
                ((PaymentsActivity) this.mActivity).showDialog(19);
                a("buildView", 4, "mIsSynced=" + this.g + "if没有同步");
                new SyncSmsInfoHandler(this.mActivity, new l(this, smsPayment)).handleRequest();
            }
            a("buildView", 7, "exit");
            this.c.setVisibility(0);
            return this.b;
        } catch (SimCardNotSupportException e) {
            a("buildView", 2, "catch + CheckSimCardSupprotInfo");
            this.mSmsResultInfo = e.getMessage();
            ((PaymentsActivity) this.mActivity).showDialog(16);
            return this.b;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cancelSmsListener() {
        if (this.n == null || this.q == null) {
            return;
        }
        this.n.removeCallbacks(this.q);
        if (this.m != null) {
            ((PaymentsActivity) this.mActivity).getContentResolver().unregisterContentObserver(this.m);
        }
        try {
            ((PaymentsActivity) this.mActivity).unregisterReceiver(this.p);
        } catch (IllegalArgumentException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void deleteSms() {
        try {
            ((PaymentsActivity) this.mActivity).getContentResolver().delete(Uri.parse("content://sms/" + this.l), null, null);
        } catch (SQLiteAbortException e) {
            e.printStackTrace();
        }
    }

    public int getPayedAmount() {
        int smsPayment = (Utils.getSmsPayment() - PrefUtil.getPayedAmount(this.mActivity)) / this.h.money;
        return (smsPayment - this.j) * this.h.money;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        switch (view.getId()) {
            case 3:
                if (this.k) {
                    ((PaymentsActivity) this.mActivity).showSmsPaymentView();
                    this.k = false;
                    return;
                } else {
                    if (this.h != null) {
                        if (this.n == null) {
                            this.n = new Handler();
                            this.n.postDelayed(this.q, TimeConstants.MS_MINUTE);
                            ((PaymentsActivity) this.mActivity).registerReceiver(this.p, new IntentFilter(SmsInfo.ACTION_SMS_SENT));
                        }
                        a(this.h);
                        return;
                    }
                    return;
                }
            case 4:
                if (ServiceConnector.getInstance(((PaymentsActivity) this.mActivity).getApplicationContext()).getIsConnected()) {
                    ((PaymentsActivity) this.mActivity).sendBroadcast(new Intent(BaseUtils.getPayBroadcast(((PaymentsActivity) this.mActivity).getApplicationContext())).putExtra(GfanPayService.EXTRA_KEY_TYPE, 1).putExtra("com.mappn.sdk.order", ((PaymentsActivity) this.mActivity).mPaymentInfo.getOrder()).putExtra(GfanPayService.EXTRA_KEY_USER, ((PaymentsActivity) this.mActivity).mPaymentInfo.getUser()));
                }
                PrefUtil.setLoginFlag(((PaymentsActivity) this.mActivity).getApplicationContext(), false);
                ((PaymentsActivity) this.mActivity).finish();
                return;
            case 5:
                if (this.o == null || !this.o.contains(",,,,")) {
                    return;
                }
                this.n.postDelayed(this.q, TimeConstants.MS_MINUTE);
                ((PaymentsActivity) this.mActivity).registerReceiver(this.p, new IntentFilter(SmsInfo.ACTION_SMS_SENT));
                a(this.o);
                return;
            case 6:
                ((PaymentsActivity) this.mActivity).showDialog(23);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onKeyBack() {
        if (this.h.isNeedSendConfirmSms() || this.k) {
            int smsPayment = Utils.getSmsPayment() / this.h.money;
            if (this.j == -1 || this.j == smsPayment) {
                return;
            }
            ((PaymentsActivity) this.mActivity).showDialog(27);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void paySmsRetry() {
        if (3 == ((PaymentsActivity) this.mActivity).mType) {
            a(this.h);
        } else {
            a(this.o);
        }
    }
}
