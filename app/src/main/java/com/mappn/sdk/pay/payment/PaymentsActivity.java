package com.mappn.sdk.pay.payment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ListView;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.GfanAlertDialog;
import com.mappn.sdk.common.utils.GfanProgressDialog;
import com.mappn.sdk.pay.GfanPayService;
import com.mappn.sdk.pay.ServiceConnector;
import com.mappn.sdk.pay.model.Order;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.net.chain.HandlerProxy;
import com.mappn.sdk.pay.net.chain.PostSmsPaymentHandler;
import com.mappn.sdk.pay.net.chain.SyncChargeChannelHandler;
import com.mappn.sdk.pay.net.chain.SyncPayChannelHandler;
import com.mappn.sdk.pay.net.chain.SyncSmsInfoHandler;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.DialogUtil;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.pay.util.Utils;
import com.mappn.sdk.statitistics.GfanPayAgent;
import com.mappn.sdk.uc.GfanUCenter;
import com.mappn.sdk.uc.util.UserUtil;
import com.mongodb.util.TimeConstants;
import java.util.HashMap;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class PaymentsActivity extends Activity implements DialogUtil.WarningDialogListener {
    public static final int DIALOG_500 = 11;
    public static final int DIALOG_CHARGE_DES = 12;
    public static final int DIALOG_CHARGE_INFO_TOTAL = 28;
    public static final int DIALOG_CONNECTING = 14;
    public static final int DIALOG_CONNECTING_FAILED = 15;
    public static final int DIALOG_INSUFFICIENT_BALANCE = 5;
    public static final int DIALOG_INVALID_ASSOCIATION = 4;
    public static final int DIALOG_PAY_DES = 3;
    public static final int DIALOG_PAY_ERROR_INPUT = 8;
    public static final int DIALOG_PAY_FAILED = 7;
    public static final int DIALOG_PAY_INVALID_ACCOUNT = 9;
    public static final int DIALOG_PAY_NEED_TO_CONFIRM_PAY_RESULT = 10;
    public static final int DIALOG_PAY_SMS_BACK_CONFIRM = 24;
    public static final int DIALOG_PAY_SMS_DELETE_BACK_CONFIRM = 25;
    public static final int DIALOG_PAY_SMS_DELETE_CONFIRM = 23;
    public static final int DIALOG_PAY_SMS_ERROR = 16;
    public static final int DIALOG_PAY_SMS_FAILED = 21;
    public static final int DIALOG_PAY_SMS_RETRY = 26;
    public static final int DIALOG_PAY_SMS_RETRY_MULTIPLE = 27;
    public static final int DIALOG_PAY_SMS_SENDING = 17;
    public static final int DIALOG_PAY_SMS_SEND_FAILED = 18;
    public static final int DIALOG_PAY_SMS_SUCCESS = 20;
    public static final int DIALOG_PAY_SMS_SYNC = 19;
    public static final int DIALOG_PAY_SUCCESS = 13;
    public static final int DIALOG_PROGRESS_BAR = 6;
    public static final int DIALOG_START_ERROR_APPKEY_INVALID = 1;
    public static final int DIALOG_START_ERROR_CPID_INVALID = 22;
    public static final int DIALOG_START_ERROR_PAYMENT_INVALID = 0;
    public static final int DIALOG_TIP = 2;
    public static final String EXTRA_KEY_NUMBER = "number";
    public static final String EXTRA_KEY_ORDER = "com.mappn.sdk.order";
    public static final String EXTRA_KEY_ORDER_ID = "orderId";
    public static final String EXTRA_KEY_PAYMENTTYPE = "com.mappn.sdk.paymenttype";
    public static final String PAYTYPE_SMS = "SMS";
    public static final int RESULT_CHARGE_ACTIVITY = 1;
    public static final int RESULT_CHARGE_ACTIVITY_2 = 3;
    public static final int RESULT_LOGIN = 0;
    public static final int TYPE_PAYMENT_ENTER = 0;
    public static final int TYPE_PAYMENT_JIFENGQUAN = 1;
    public static final int TYPE_PAYMENT_JIFENGQUAN_CONFIRM = 2;
    public static final int TYPE_PAYMENT_LOGIN_ERROR = 5;
    public static final int TYPE_PAYMENT_SMS = 3;
    public static final int TYPE_PAYMENT_SMS_CONFIRM = 4;
    private static final String a = PaymentsActivity.class.getSimpleName();
    private boolean b;
    private SmsPaymentFragment c;
    private PaymentViews d;
    public ListView mListView;
    public PaymentInfo mPaymentInfo;
    public int mType;
    public boolean mToCancleDialog = false;
    public int mErrorCode = 0;

    private static boolean a(int i) {
        switch (i) {
            case 5:
            case 10:
            case 25:
            case 50:
            case 75:
            case 100:
                return true;
            default:
                return false;
        }
    }

    private boolean b() {
        String cpID;
        this.d = PaymentViews.instance(this);
        Order order = (Order) getIntent().getSerializableExtra("com.mappn.sdk.order");
        if (order == null) {
            throw new IllegalArgumentException("must set the Order");
        }
        if (this.mPaymentInfo == null) {
            this.mPaymentInfo = PaymentInfo.getInstance();
            try {
                String appkey = Utils.getAppkey(getApplicationContext());
                if (TextUtils.isEmpty(appkey)) {
                    throw new IllegalArgumentException("gfan_appkey can not be empty");
                }
                this.mPaymentInfo.setAppkey(appkey);
            } catch (PackageManager.NameNotFoundException e) {
                throw new IllegalArgumentException("must set gfan_pay_appkey in meta-data");
            }
        }
        this.mPaymentInfo.setOrder(order);
        this.mPaymentInfo.setPaymentType(PaymentInfo.PAYTYPE_OVERAGE);
        Utils.setPaymentInfo(this.mPaymentInfo);
        int money = this.mPaymentInfo.getOrder().getMoney();
        if (!Pattern.compile("^\\+?[1-9][0-9]*$").matcher(String.valueOf(money)).matches() || money <= 0) {
            showDialog(0);
            return false;
        }
        if (!a(money) && this.mPaymentInfo.getPaymentType().equals("sms")) {
            throw new IllegalArgumentException("the sms just support 5,10,25,50,75,100,a few number of recharge.");
        }
        if (this.mPaymentInfo.getOrder().getPayName() == null) {
            throw new IllegalArgumentException("must set the pay name");
        }
        if (this.mPaymentInfo.getOrder().getPayDesc() == null) {
            throw new IllegalArgumentException("must set the pay desc");
        }
        if (this.mPaymentInfo.getOrder().getPayName().length() > 50) {
            this.mPaymentInfo.getOrder().setPayName(this.mPaymentInfo.getOrder().getPayName().substring(0, 50));
        }
        if (this.mPaymentInfo.getOrder().getPayDesc().length() > 100) {
            this.mPaymentInfo.getOrder().setPayDesc(this.mPaymentInfo.getOrder().getPayDesc().substring(0, 100));
        }
        try {
            this.mPaymentInfo.setAppkey(Utils.getAppkey(getApplicationContext()));
            try {
                cpID = Utils.getCpID(getApplicationContext());
            } catch (PackageManager.NameNotFoundException e2) {
            } catch (NullPointerException e3) {
            }
            if (cpID.getBytes().length > 10) {
                showDialog(22);
                return false;
            }
            this.mPaymentInfo.setCpID(cpID);
            return true;
        } catch (PackageManager.NameNotFoundException e4) {
            showDialog(1);
            return false;
        } catch (NullPointerException e5) {
            showDialog(1);
            return false;
        }
    }

    public static void init(Context context) {
        SyncChargeChannelHandler.init();
        SyncPayChannelHandler.init();
        SyncSmsInfoHandler.init();
        PostSmsPaymentHandler.init();
        new HandlerProxy(context).handleRequest();
    }

    @Override // android.app.Activity
    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.b = b();
        if (this.b) {
            Utils.init(getApplicationContext());
            Utils.initTitleBar(this);
        }
        HashMap hashMap = new HashMap();
        hashMap.put("version", Constants.VERSION);
        GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_pay_step1", hashMap);
        showDialog(14);
        if (!PrefUtil.isLogin(this) || this.mPaymentInfo.getUser() == null) {
            GfanUCenter.login(this, new g(this));
        }
        GfanPayAgent.setReportUncaughtExceptions(true);
        this.d.confirmEnterPaymentPoint();
    }

    @Override // android.app.Activity
    protected final Dialog onCreateDialog(int i) {
        String format;
        i iVar;
        String str;
        switch (i) {
            case 0:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("该应用要求支付的机锋券超过了限制，不能成功支付。<br />错误码：" + this.mErrorCode), this);
            case 1:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("该应用的支付KEY无效，不能成功支付。<br />错误码：" + this.mErrorCode), this);
            case 2:
                return DialogUtil.createOKWarningDialogSupportLink(this, i, Constants.TEXT_WHAT_IS_JIFENGQUAN_TITLE, Constants.TEXT_JIFENGQUAN_INFO, null);
            case 3:
            default:
                return super.onCreateDialog(i);
            case 4:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("支付不成功，无效的应用KEY。\n请联系客服4006-400-401。<br />错误码：" + this.mErrorCode), null);
            case 5:
                return DialogUtil.createTwoButtonsDialog(this, i, Constants.TEXT_PAYMENT_INSUFFICIENT, Constants.TEXT_CHANGE, "取消", this);
            case 6:
                return GfanProgressDialog.createProgressDialog(this, Constants.TEXT_PAYING, false, TimeConstants.MS_MINUTE, null);
            case 7:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("支付不成功，请确定您的账户当中的余额充足并网络连接正常。\n请联系客服4006-400-401。<br />错误码：" + this.mErrorCode), null);
            case 8:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("支付不成功，请确定您的账户当中的余额充足并网络连接正常。\n请联系客服4006-400-401。<br />错误码：" + this.mErrorCode), null);
            case 9:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("支付不成功，请确定您的账户当中的余额充足并网络连接正常。\n请联系客服4006-400-401。<br />错误码：" + this.mErrorCode), null);
            case 10:
                return DialogUtil.createYesNoDialog(this, i, Html.fromHtml("支付不成功，连接服务器超时，是否重试?<br />错误码：" + this.mErrorCode), this);
            case 11:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("支付不成功，请确定您的账户当中的余额充足并网络连接正常。\n请联系客服4006-400-401。<br />错误码：" + this.mErrorCode), null);
            case 12:
                return DialogUtil.createOKWarningDialogSupportLink(this, i, Constants.TEXT_PAYMENT_CHARGE_INFO_TITLE, Constants.TEXT_PAYMENT_CHARGE_INFO_TOTAL, null);
            case 13:
                int gfanBalance = this.mPaymentInfo.getGfanBalance() - this.mPaymentInfo.getOrder().getMoney();
                this.mPaymentInfo.setGfanBalance(gfanBalance);
                h hVar = new h(this);
                if (this.mPaymentInfo.getUser().getUserName().equals(UserUtil.getOnekeyName(this))) {
                    format = String.format(Constants.TEXT_PAYMENT_SUCCESS_INFO, this.mPaymentInfo.getOrder().getNumber(), Integer.valueOf(gfanBalance)) + Constants.TEXT_COMPLETE_ACCOUNT_TIP;
                    str = Constants.TEXT_COMPLETE_ACCOUNT;
                    iVar = new i(this);
                } else {
                    format = String.format(Constants.TEXT_PAYMENT_SUCCESS_INFO, this.mPaymentInfo.getOrder().getNumber(), Integer.valueOf(gfanBalance));
                    iVar = null;
                    str = null;
                }
                GfanAlertDialog.show(this, Constants.TEXT_PAYMENT_SUCCESS, format, str, Constants.TEXT_OK, iVar, hVar);
                return null;
            case 14:
                return GfanProgressDialog.createProgressDialog(this, Constants.TEXT_CONNETING, false, TimeConstants.MS_MINUTE, null);
            case 15:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("网络连接错误，请检查网络后再试。<br />错误码：" + this.mErrorCode), this);
            case 16:
                return DialogUtil.createOKWarningDialog(this, i, this.c.mSmsResultInfo, this);
            case 17:
                return GfanProgressDialog.createProgressDialog(this, Constants.TEXT_PAY_SMS_SENDING, false, TimeConstants.MS_MINUTE, null);
            case 18:
                return DialogUtil.createOKWarningDialog(this, i, Constants.TEXT_PAY_SMS_SEND_FAILED, null);
            case 19:
                return GfanProgressDialog.createProgressDialog(this, Constants.TEXT_PAY_SMS_SYNCING, false, TimeConstants.MS_MINUTE, null);
            case 20:
                return DialogUtil.createOKWarningDialog(this, i, Constants.TEXT_PAY_SMS_SUCCESS, this.c.mSmsResultInfo, this);
            case 21:
                return DialogUtil.createOKWarningDialog(this, i, this.c.mSmsResultInfo, this);
            case 22:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("CPID为字母、数字及\".\"任意组合，长度不超过10个英文字符。<br />错误码：" + this.mErrorCode), this);
            case 23:
                return DialogUtil.createTwoButtonsDialog(this, i, Constants.TEXT_PAY_SMS_DELETE_CONFIRM, Constants.TEXT_DELETE, Constants.TEXT_NOT_DELETE, this);
            case 24:
                return DialogUtil.createTwoButtonsDialog(this, i, Html.fromHtml(Constants.TEXT_PAY_SMS_BACK_CONFIRM), Constants.TEXT_BACK_TO_PAYPOINT, Constants.TEXT_EXIT, this);
            case 25:
                return DialogUtil.createTwoButtonsDialog(this, i, Constants.TEXT_PAY_SMS_DELETE_BACK_CONFIRM, Constants.TEXT_BACK_TO_PAYPOINT, Constants.TEXT_EXIT, this);
            case 26:
                return DialogUtil.createOKWarningDialog(this, i, Constants.TEXT_PAY_SMS_RETRY, this);
            case 27:
                return DialogUtil.createTwoButtonsDialog(this, i, String.format(Constants.TEXT_PAY_SMS_CHANCEL_CONFIRM, Integer.valueOf(this.c.getPayedAmount() + PrefUtil.getPayedAmount(getApplicationContext())), this.mPaymentInfo.getOrder().getPayName(), this.mPaymentInfo.getOrder().getPayName()), Constants.TEXT_BACK_TO_PAY, Constants.TEXT_CONFIRM_TO_CANCEL, this);
            case 28:
                GfanAlertDialog.show(this, Constants.TEXT_PAYMENT_CHARGE_INFO_TITLE_NO_THEME, Constants.TEXT_PAYMENT_CHARGE_INFO_TOTAL, Constants.TEXT_OK, null, null, null);
                return null;
        }
    }

    @Override // android.app.Activity
    protected final void onDestroy() {
        super.onDestroy();
        this.d.onDestroy();
        Utils.clearSmsInfos();
        TypeFactory.clear();
        SyncChargeChannelHandler.init();
        SyncPayChannelHandler.init();
        SyncSmsInfoHandler.init();
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0002. Please report as an issue. */
    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x000c. Please report as an issue. */
    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        switch (i) {
            case 4:
                switch (this.mType) {
                    case 2:
                        finish();
                        this.d.onDestroy();
                        if (ServiceConnector.getInstance(getApplicationContext()).getIsConnected()) {
                            sendBroadcast(new Intent(BaseUtils.getPayBroadcast(getApplicationContext())).putExtra(GfanPayService.EXTRA_KEY_TYPE, 1).putExtra(GfanPayService.EXTRA_KEY_USER, this.mPaymentInfo.getUser()));
                        } else {
                            BaseUtils.D(a, "connection disconnect");
                        }
                        PrefUtil.setLoginFlag(getApplicationContext(), false);
                        return true;
                    case 3:
                        this.c.onKeyBack();
                        return true;
                    case 4:
                        showDialog(23);
                        return true;
                    default:
                        if (this.mType == 1 && this.mPaymentInfo.getPaymentType().equals("all") && a(this.mPaymentInfo.getOrder().getMoney())) {
                            return true;
                        }
                        if (ServiceConnector.getInstance(getApplicationContext()).getIsConnected()) {
                            sendBroadcast(new Intent(BaseUtils.getPayBroadcast(getApplicationContext())).putExtra(GfanPayService.EXTRA_KEY_TYPE, 1).putExtra(GfanPayService.EXTRA_KEY_USER, this.mPaymentInfo.getUser()));
                        } else {
                            BaseUtils.D(a, "connection disconnect");
                        }
                        PrefUtil.setLoginFlag(getApplicationContext(), false);
                        this.d.onDestroy();
                        break;
                }
            default:
                return super.onKeyDown(i, keyEvent);
        }
    }

    @Override // android.app.Activity
    protected final void onPause() {
        super.onPause();
        BaseUtils.D(a, "onpause mToCancleDialog:" + this.mToCancleDialog);
        if (this.mToCancleDialog) {
            BaseUtils.D(a, "onpause dismiss Alipay dialog");
            GfanProgressDialog.dismissDialog();
            this.mToCancleDialog = false;
        }
        GfanPayAgent.onPause(this);
    }

    @Override // android.app.Activity
    protected final void onPrepareDialog(int i, Dialog dialog) {
        super.onPrepareDialog(i, dialog);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override // android.app.Activity
    protected final void onResume() {
        super.onResume();
        GfanPayAgent.onResume(this);
    }

    @Override // com.mappn.sdk.pay.util.DialogUtil.WarningDialogListener
    public final void onWarningDialogCancel(int i) {
        switch (i) {
            case 13:
                this.d.onPaySuccess();
                GfanUCenter.modfiy(this, new k(this));
                this.d.onDestroy();
                finish();
                return;
            case 23:
                showDialog(24);
                return;
            case 24:
            case 25:
                getIntent().putExtra(EXTRA_KEY_PAYMENTTYPE, PAYTYPE_SMS);
                this.c.cancelSmsListener();
                setResult(0, getIntent());
                this.d.onDestroy();
                finish();
                return;
            case 27:
                this.c.cancelSmsListener();
                return;
            default:
                return;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0001. Please report as an issue. */
    @Override // com.mappn.sdk.pay.util.DialogUtil.WarningDialogListener
    public final void onWarningDialogOK(int i) {
        switch (i) {
            case 0:
            case 1:
            case 22:
                setResult(1);
                this.d.onDestroy();
                finish();
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 14:
            case 17:
            case 18:
            case 19:
            default:
                return;
            case 13:
                this.d.onPaySuccess();
                this.d.onDestroy();
                finish();
                return;
            case 15:
            case 16:
            case 24:
                setResult(1);
                this.d.onDestroy();
                finish();
                return;
            case 20:
                PrefUtil.clearPayedAmount(getApplicationContext());
                this.d.onPaySuccess();
                this.d.onDestroy();
                finish();
                return;
            case 21:
            case 25:
                this.c.cancelSmsListener();
                setResult(1);
                this.d.onDestroy();
                finish();
                return;
            case 23:
                this.c.deleteSms();
                showDialog(25);
                return;
            case 26:
                this.c.paySmsRetry();
                return;
        }
    }

    public final void showSmsPaymentView() {
        this.c = SmsPaymentFragment.instance();
        this.c.init(this);
        this.c.showView();
    }
}
