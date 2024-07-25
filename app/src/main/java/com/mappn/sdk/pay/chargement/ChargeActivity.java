package com.mappn.sdk.pay.chargement;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.DESUtil;
import com.mappn.sdk.common.utils.GfanAlertDialog;
import com.mappn.sdk.common.utils.GfanProgressDialog;
import com.mappn.sdk.pay.GfanPayService;
import com.mappn.sdk.pay.ServiceConnector;
import com.mappn.sdk.pay.model.Order;
import com.mappn.sdk.pay.model.PaymentInfo;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.net.chain.SyncChargeChannelHandler;
import com.mappn.sdk.pay.net.chain.SyncPayChannelHandler;
import com.mappn.sdk.pay.net.chain.SyncSmsInfoHandler;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.DialogUtil;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.pay.util.Utils;
import com.mappn.sdk.statitistics.GfanPayAgent;
import com.mappn.sdk.uc.GfanUCenter;
import com.mappn.sdk.uc.User;
import com.mappn.sdk.uc.UserDao;
import com.mappn.sdk.uc.UserVo;
import com.mappn.sdk.uc.util.UserUtil;
import com.mongodb.util.TimeConstants;
import java.util.HashMap;
import java.util.Stack;

/* loaded from: classes.dex */
public class ChargeActivity extends Activity implements DialogUtil.WarningDialogListener {
    public static final int CODE_CARD_OR_PWD_FAILED = 223;
    public static final int CODE_CHARGEING = 224;
    public static final int CODE_CHARGE_CARD_NO_ENOUGH_BALANCE = 220;
    public static final int CODE_CHARGE_SUCCESS_BUT_ADD_JIFENGQUAN_FAILED = 222;
    public static final int CODE_FAILED = 221;
    public static final int DIALOG_ACCOUNT_NUM_WRONG = 3;
    public static final int DIALOG_CARD_NUMBER_IS_EMPTY = 1;
    public static final int DIALOG_CHARGE_CARD_NO_ENOUGH_BALANCE_ERROR = 12;
    public static final int DIALOG_CHARGE_CARD_OR_PWD_FAILED = 14;
    public static final int DIALOG_CHARGE_CONNECT_FAILED = 11;
    public static final int DIALOG_CHARGE_FAILED = 10;
    public static final int DIALOG_CHARGE_INFO_TOTAL = 21;
    public static final int DIALOG_CHARGE_NETWORK_ERROR = 13;
    public static final int DIALOG_CHARGE_SUCCESS = 9;
    public static final int DIALOG_CHARGE_SUCCESS_BUT_ADD_JIFENGQUAN_FAILED = 15;
    public static final int DIALOG_CHECKBOX_IS_EMPTY = 0;
    public static final int DIALOG_CONFIRM = 6;
    public static final int DIALOG_G_NUM_WRONG = 20;
    public static final int DIALOG_LOADING_G = 18;
    public static final int DIALOG_NOT_ENOUGH_G = 19;
    public static final int DIALOG_NO_CARD_CHOOSE = 17;
    public static final int DIALOG_OUT_TIME = 8;
    public static final int DIALOG_PASSWORD_IS_EMPTY = 2;
    public static final int DIALOG_PAY_DES = 16;
    public static final int DIALOG_PROGRESS_BAR = 7;
    public static final int DIALOG_PSD_NUM_WRONG = 4;
    public static final int DIALOG_START_ERROR_CPID_INVALID = 5;
    public static final int ERROR_CANCEL = 6001;
    public static final int ERROR_UNFORMAT = 4000;
    public static final String EXTRA_CHARGE_ONLY = "extra.charge.charge.only";
    public static final String EXTRA_PAYMENT_INFO = "extra.charge.paymentinfo";
    public static final String EXTRA_TYPE = "extra.charge.type";
    public static final int ID_CHANGE_CHARGE_TYPE = 1;
    public static final int ID_CHARGE_ALIPAY_AND_G = 7;
    public static final int ID_CHARGE_PHONECARD = 100;
    public static final int RESULT_LOGIN_ACTIVITY = 1;
    public static final long TIMEOUT = 174000;
    public static final int TYPE_CHARGE_ONLY = 1;
    public static final String TYPE_CHARGE_TYPE_LIST = "null";
    private static final String b = ChargeActivity.class.getSimpleName();
    int a;
    private AlipayOrGFragment d;
    private ChargeTypeListFragment e;
    private PhoneCardFragment f;
    protected int mBalance;
    public int mChargeFlag;
    protected int mChargeMoney;
    public long mLastTime;
    public PaymentInfo mPaymentInfo;
    public String mType;
    protected User mUser;
    public Stack mViewStacks;
    private boolean c = false;
    public boolean mToCancleDialog = false;

    /* loaded from: classes.dex */
    public class AlixOnCancelListener implements DialogInterface.OnCancelListener {
        private Activity a;

        public AlixOnCancelListener(Activity activity) {
            this.a = activity;
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            this.a.onKeyDown(4, null);
        }
    }

    /* loaded from: classes.dex */
    public class LoadingOrErrorView extends LinearLayout {
        public Button btnRetry;
        public Context mContext;
        public ProgressBar pb;
        public LinearLayout progressLayout;
        public TextView tvHint;

        public LoadingOrErrorView(ChargeActivity chargeActivity, Context context) {
            super(context);
            this.mContext = context;
        }

        public LoadingOrErrorView init() {
            setOrientation(1);
            setGravity(17);
            this.progressLayout = new LinearLayout(this.mContext);
            this.progressLayout.setGravity(17);
            this.pb = new ProgressBar(this.mContext);
            this.pb.setIndeterminate(true);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(18, 18);
            layoutParams.rightMargin = 3;
            this.progressLayout.addView(this.pb, layoutParams);
            this.tvHint = new TextView(this.mContext);
            this.tvHint.setTextColor(-13487566);
            this.progressLayout.addView(this.tvHint, new LinearLayout.LayoutParams(-2, -2));
            this.btnRetry = new Button(this.mContext);
            this.btnRetry.setText(Constants.TEXT_RETRY);
            addView(this.progressLayout, new LinearLayout.LayoutParams(-1, -2));
            addView(this.btnRetry, new LinearLayout.LayoutParams(-2, -2));
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        c();
        if (ServiceConnector.getInstance(getApplicationContext()).getIsConnected()) {
            sendBroadcast(new Intent(BaseUtils.getPayBroadcast(getApplicationContext())).putExtra(GfanPayService.EXTRA_KEY_TYPE, 2).putExtra(GfanPayService.EXTRA_KEY_USER, this.mPaymentInfo.getUser()));
        }
        PrefUtil.setLoginFlag(getApplicationContext(), false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (this.d != null) {
            this.d.removeTextListener();
            this.d.onDestroy();
        }
        if (this.e != null) {
            this.e.onDestroy();
        }
        if (this.f != null) {
            this.f.onDestroy();
        }
    }

    public LoadingOrErrorView getLoadingOrErrorView() {
        return new LoadingOrErrorView(this, this).init();
    }

    public void goLoginActivity() {
        GfanUCenter.login(this, new h(this));
    }

    public boolean hasError() {
        return this.c;
    }

    public boolean isOutTime() {
        return System.currentTimeMillis() - this.mLastTime > TIMEOUT;
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        BaseUtils.D(b, "onActivityResult requestCode :" + i + " resultCode :" + i2);
        if (i == 100 && i2 == 10) {
            BaseUtils.D(b, "mo9 pay success");
            this.d.queryMO9Result();
        } else {
            if (i != 100 || i2 == 10) {
                return;
            }
            removeDialog(7);
            showDialog(10);
        }
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle extras = getIntent().getExtras();
        this.mPaymentInfo = (PaymentInfo) extras.getSerializable(EXTRA_PAYMENT_INFO);
        this.mChargeFlag = extras.getInt(EXTRA_CHARGE_ONLY);
        this.d = AlipayOrGFragment.instance(this);
        this.e = ChargeTypeListFragment.instance(this);
        this.f = PhoneCardFragment.instance(this);
        if (this.mPaymentInfo == null) {
            this.mPaymentInfo = PaymentInfo.getInstance();
            try {
                this.mPaymentInfo.setOrder(new Order(null, null, 0));
                String appkey = Utils.getAppkey(getApplicationContext());
                if (TextUtils.isEmpty(appkey)) {
                    throw new IllegalArgumentException("gfan_pay_appkey can not be empty");
                }
                this.mPaymentInfo.setAppkey(appkey);
                try {
                    String cpID = Utils.getCpID(getApplicationContext());
                    if (cpID.getBytes().length > 10) {
                        showDialog(5);
                    }
                    this.mPaymentInfo.setCpID(cpID);
                } catch (PackageManager.NameNotFoundException e) {
                } catch (NullPointerException e2) {
                }
            } catch (PackageManager.NameNotFoundException e3) {
                throw new IllegalArgumentException("must set gfan_appkey in meta-data");
            }
        }
        Utils.init(getApplicationContext());
        Utils.initTitleBar(this);
        this.a = 0;
        this.mViewStacks = new Stack();
        if (getIntent().hasExtra(EXTRA_TYPE)) {
            this.mType = getIntent().getStringExtra(EXTRA_TYPE);
        } else {
            String defaultChargeType = PrefUtil.getDefaultChargeType(getApplicationContext());
            if (defaultChargeType != null) {
                this.mType = defaultChargeType;
            }
        }
        if (this.mType != null) {
            if (TypeFactory.TYPE_CHARGE_PHONECARD.equals(this.mType)) {
                HashMap hashMap = new HashMap();
                hashMap.put("version", Constants.VERSION);
                GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_pay_step6", hashMap);
            } else if ("alipay".equals(this.mType)) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("version", Constants.VERSION);
                GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_pay_step5", hashMap2);
            } else {
                String str = this.mType;
            }
            showViewByType(this.mType);
        } else {
            HashMap hashMap3 = new HashMap();
            hashMap3.put("version", Constants.VERSION);
            GfanPayAgent.onEvent(getApplicationContext(), "gfanapi_pay_step7", hashMap3);
            showTypeListView(false);
        }
        GfanPayAgent.setReportUncaughtExceptions(true);
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i) {
        k kVar;
        String str;
        String str2;
        switch (i) {
            case 0:
                return DialogUtil.createOKWarningDialog(this, i, Constants.TEXT_CHARGE_CHECKBOX_IS_EMPTY, null);
            case 1:
                return DialogUtil.createOKWarningDialog(this, i, Constants.TEXT_CHARGE_CARD_IS_EMPTY, this);
            case 2:
                return DialogUtil.createOKWarningDialog(this, i, "密码不能为空", this);
            case 3:
                return DialogUtil.createOKWarningDialog(this, i, Constants.TEXT_CHARGE_ACCOUNT_NUM_WRONG, this);
            case 4:
                return DialogUtil.createOKWarningDialog(this, i, String.format(Constants.TEXT_CHARGE_PSD_NUM_WRONG, this.f.mCardVerification.name, Integer.valueOf(this.f.mCardVerification.passwordNum)), this);
            case 5:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml("CPID为字母、数字及\".\"任意组合，长度不超过10个英文字符。<br />错误码：" + this.a), this);
            case 6:
                return DialogUtil.createYesNoDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_CONFIRM, Integer.valueOf(this.f.mCardMoney[this.f.mCheckedId]), this.f.mCardVerification.name)), this);
            case 7:
                return GfanProgressDialog.createProgressDialog(this, Constants.TEXT_CHARGE_LOADING, false, TimeConstants.MS_MINUTE, null);
            case 8:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_FAILED_OUT_TIME, Integer.valueOf(this.a))), this);
            case 9:
                String str3 = Constants.TEXT_CHARGE_SUCCESS;
                if (TypeFactory.TYPE_CHARGE_PHONECARD.equals(this.mType)) {
                    str3 = String.format(Constants.TEXT_CHARGE_SUCCESS_INFO, Integer.valueOf(this.f.mCardMoney[this.f.mCheckedId]), Integer.valueOf(this.f.mCardMoney[this.f.mCheckedId] * 10));
                } else if (TypeFactory.TYPE_CHARGE_G.equals(this.mType)) {
                    int parseInt = Integer.parseInt(this.d.mEtInputChargeMoney.getText().toString());
                    str3 = String.format(Constants.TEXT_CHARGE_SUCCESS_G, Integer.valueOf(parseInt * 60), Integer.valueOf(parseInt));
                }
                j jVar = new j(this);
                if (this.mPaymentInfo.getUser().getUserName().equals(UserUtil.getOnekeyName(this))) {
                    str2 = str3 + Constants.TEXT_COMPLETE_ACCOUNT_TIP;
                    str = Constants.TEXT_COMPLETE_ACCOUNT;
                    kVar = new k(this);
                } else {
                    kVar = null;
                    str = null;
                    str2 = str3;
                }
                GfanAlertDialog.show(this, Constants.TEXT_CHARGE_SUCCESS, str2, str, Constants.TEXT_OK, kVar, jVar);
                return null;
            case 10:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_FAILED, Integer.valueOf(this.a))), this);
            case 11:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_FAILED, Integer.valueOf(this.a))), this);
            case 12:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_INSUFFENT_BALANCE, Integer.valueOf(this.a))), null);
            case 13:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_NETWORD_FAILED, Integer.valueOf(this.a))), this);
            case 14:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_INVALID_CARDNUMBER_OR_PASSWORD, Integer.valueOf(this.a))), null);
            case 15:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_FAILED, Integer.valueOf(this.a))), this);
            case 16:
                return DialogUtil.createOKWarningDialog(this, i, Constants.TEXT_WARNING, Constants.TEXT_CHARGE_DES, null);
            case 17:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_NEED_CHOOSE_CARD_TYPE, Integer.valueOf(this.a))), null);
            case 18:
                return GfanProgressDialog.createProgressDialog(this, Constants.TEXT_CONNETING, false, TimeConstants.MS_MINUTE, null);
            case 19:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_INSUFFENT_G_BALANCE, Integer.valueOf(this.a))), this);
            case 20:
                return DialogUtil.createOKWarningDialog(this, i, Html.fromHtml(String.format(Constants.TEXT_CHARGE_G_NUM_WRONG, Integer.valueOf(this.a))), null);
            case 21:
                GfanAlertDialog.show(this, Constants.TEXT_PAYMENT_CHARGE_INFO_TITLE_NO_THEME, Constants.TEXT_PAYMENT_CHARGE_INFO_TOTAL, Constants.TEXT_OK, null, null, null);
                return null;
            default:
                return super.onCreateDialog(i);
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        c();
        if (1 == this.mChargeFlag) {
            Utils.clearSmsInfos();
            TypeFactory.clear();
            SyncChargeChannelHandler.init();
            SyncPayChannelHandler.init();
            SyncSmsInfoHandler.init();
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0002. Please report as an issue. */
    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        User user = null;
        switch (i) {
            case 4:
                if (!this.mViewStacks.empty()) {
                    String str = (String) this.mViewStacks.pop();
                    if (TYPE_CHARGE_TYPE_LIST.equals(str)) {
                        showTypeListView(false);
                    } else {
                        this.mType = str;
                        showViewByType(this.mType);
                    }
                    return true;
                }
                c();
                if (this.mPaymentInfo.getUser() != null) {
                    user = this.mPaymentInfo.getUser();
                } else if (com.mappn.sdk.uc.util.PrefUtil.isLogin(getApplicationContext())) {
                    Long valueOf = Long.valueOf(com.mappn.sdk.uc.util.PrefUtil.getUid(getApplicationContext()));
                    if (valueOf.longValue() != -1) {
                        UserVo userByUid = UserDao.getUserByUid(getApplicationContext(), valueOf.longValue());
                        user = new User();
                        user.setUid(userByUid.getUid());
                        user.setToken(userByUid.getToken());
                        user.setUserName(new DESUtil(getApplicationContext()).getDesAndBase64String(userByUid.getUserName()));
                    }
                }
                if (ServiceConnector.getInstance(getApplicationContext()).getIsConnected()) {
                    sendBroadcast(new Intent(BaseUtils.getPayBroadcast(getApplicationContext())).putExtra(GfanPayService.EXTRA_KEY_TYPE, 3).putExtra(GfanPayService.EXTRA_KEY_USER, user));
                }
                PrefUtil.setLoginFlag(getApplicationContext(), false);
            default:
                return super.onKeyDown(i, keyEvent);
        }
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        BaseUtils.D(b, "charge onpause mToCancleDialog:" + this.mToCancleDialog);
        if (this.mToCancleDialog) {
            BaseUtils.D(b, "onpause dismiss Alipay dialog");
            GfanProgressDialog.dismissDialog();
            this.mToCancleDialog = false;
        }
        GfanPayAgent.onPause(this);
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        GfanPayAgent.onResume(this);
    }

    @Override // com.mappn.sdk.pay.util.DialogUtil.WarningDialogListener
    public void onWarningDialogCancel(int i) {
        switch (i) {
            case 9:
                b();
                GfanUCenter.modfiy(this, new i(this));
                finish();
                return;
            default:
                return;
        }
    }

    @Override // com.mappn.sdk.pay.util.DialogUtil.WarningDialogListener
    public void onWarningDialogOK(int i) {
        switch (i) {
            case 1:
                this.f.a.requestFocus();
                return;
            case 2:
                this.f.b.requestFocus();
                return;
            case 3:
                this.f.a.requestFocus();
                return;
            case 4:
                this.f.b.requestFocus();
                return;
            case 5:
            case 8:
                if (ServiceConnector.getInstance(getApplicationContext()).getIsConnected()) {
                    User user = new User();
                    user.setToken(this.mPaymentInfo.getUser().getToken());
                    user.setUid(this.mPaymentInfo.getUser().getUid());
                    user.setUserName(this.mPaymentInfo.getUser().getUserName());
                    sendBroadcast(new Intent(BaseUtils.getPayBroadcast(getApplicationContext())).putExtra(GfanPayService.EXTRA_KEY_TYPE, 3).putExtra(GfanPayService.EXTRA_KEY_USER, user));
                }
                c();
                PrefUtil.setLoginFlag(getApplicationContext(), false);
                finish();
                break;
            case 6:
                showDialog(7);
                Api.chargePhoneCard(getApplicationContext(), this.f, this.mPaymentInfo, this.f.mCard, this.mPaymentInfo.getAppkey(), this.mPaymentInfo.getCpID());
                return;
            case 7:
            case 12:
            case 14:
            case 16:
            case 17:
            case 18:
            default:
                return;
            case 9:
                break;
            case 10:
                showTypeListView(true);
                return;
            case 11:
                showTypeListView(true);
                return;
            case 13:
                showTypeListView(true);
                return;
            case 15:
                this.a = 0;
                showTypeListView(true);
                return;
            case 19:
                showDialog(18);
                Api.queryUserProfile(getApplicationContext(), this.mPaymentInfo.getUser().getUid(), this.d);
                return;
        }
        b();
        finish();
    }

    public void showTypeListView(boolean z) {
        this.c = z;
        this.e.showListView();
    }

    public void showViewByType(String str) {
        boolean supportChargeType = PrefUtil.supportChargeType(this, this.mType);
        if (TypeFactory.TYPE_CHARGE_PHONECARD.equals(str) && supportChargeType) {
            this.f.showPhonecardView();
        } else if (supportChargeType) {
            this.d.showAliOrGView();
        } else {
            showTypeListView(true);
        }
    }
}
