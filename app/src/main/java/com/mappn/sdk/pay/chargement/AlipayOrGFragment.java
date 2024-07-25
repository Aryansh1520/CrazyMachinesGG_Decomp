package com.mappn.sdk.pay.chargement;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.GfanProgressDialog;
import com.mappn.sdk.pay.chargement.alipay.MobileSecurePayHelper;
import com.mappn.sdk.pay.chargement.alipay.MobileSecurePayer;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.pay.util.TopBar;
import com.mappn.sdk.pay.util.Utils;
import com.mokredit.payment.MktPayment;
import com.mokredit.payment.MktPluginSetting;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class AlipayOrGFragment implements CompoundButton.OnCheckedChangeListener, ApiRequestListener {
    private static AlipayOrGFragment a;
    private ChargeActivity b;
    private Button c;
    private TextView d;
    private TextView e;
    private int f;
    private TextWatcher g = new f(this);
    private Handler h = new g(this);
    public EditText mEtInputChargeMoney;

    private AlipayOrGFragment(ChargeActivity chargeActivity) {
        this.b = chargeActivity;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void a(AlipayOrGFragment alipayOrGFragment, String str) {
        alipayOrGFragment.b.mChargeMoney = Integer.parseInt(str);
        if (new MobileSecurePayHelper(alipayOrGFragment.b).detectMobile_sp()) {
            alipayOrGFragment.b.showDialog(7);
            Api.getAliPayOrder(alipayOrGFragment.b.getApplicationContext(), alipayOrGFragment, alipayOrGFragment.b.mPaymentInfo.getUser().getUid(), alipayOrGFragment.b.mChargeMoney, Constants.TEXT_CHARGE_ALIPAY_NAME, Constants.TEXT_CHARGE_ALIPAY_DESC, alipayOrGFragment.b.mPaymentInfo.getAppkey(), alipayOrGFragment.b.mPaymentInfo.getCpID());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void b(AlipayOrGFragment alipayOrGFragment, String str) {
        BaseUtils.D("AlipayOrGFragment", "go to netbank");
        alipayOrGFragment.b.mChargeMoney = Integer.parseInt(str);
        alipayOrGFragment.b.showDialog(7);
        Api.getNetBankOrder(alipayOrGFragment.b.getApplicationContext(), alipayOrGFragment, alipayOrGFragment.b.mPaymentInfo.getUser().getUid(), alipayOrGFragment.b.mChargeMoney, Constants.TEXT_CHARGE_ALIPAY_NAME, Constants.TEXT_CHARGE_ALIPAY_DESC, alipayOrGFragment.b.mPaymentInfo.getAppkey(), alipayOrGFragment.b.mPaymentInfo.getCpID());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void c(AlipayOrGFragment alipayOrGFragment, String str) {
        BaseUtils.D("AlipayOrGFragment", "go to MO9");
        alipayOrGFragment.b.mChargeMoney = Integer.parseInt(str);
        alipayOrGFragment.b.showDialog(7);
        Api.getProductMo9Order(alipayOrGFragment.b.getApplicationContext(), alipayOrGFragment, alipayOrGFragment.b.mPaymentInfo.getUser().getUid(), alipayOrGFragment.b.mChargeMoney, Constants.TEXT_CHARGE_ALIPAY_NAME, Constants.TEXT_CHARGE_ALIPAY_DESC, alipayOrGFragment.b.mPaymentInfo.getAppkey(), alipayOrGFragment.b.mPaymentInfo.getCpID());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void d(AlipayOrGFragment alipayOrGFragment, String str) {
        alipayOrGFragment.b.showDialog(7);
        alipayOrGFragment.b.mChargeMoney = Integer.parseInt(str);
        Api.chargeG(alipayOrGFragment.b, alipayOrGFragment, alipayOrGFragment.b.mPaymentInfo.getUser().getUid(), alipayOrGFragment.b.mChargeMoney, alipayOrGFragment.b.mPaymentInfo.getAppkey(), alipayOrGFragment.b.mPaymentInfo.getCpID());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ TextView e(AlipayOrGFragment alipayOrGFragment) {
        return null;
    }

    public static synchronized AlipayOrGFragment instance(ChargeActivity chargeActivity) {
        AlipayOrGFragment alipayOrGFragment;
        synchronized (AlipayOrGFragment.class) {
            if (a == null) {
                a = new AlipayOrGFragment(chargeActivity);
            }
            alipayOrGFragment = a;
        }
        return alipayOrGFragment;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            PrefUtil.setDefaultChargeType(this.b, this.b.mType);
        } else {
            PrefUtil.setDefaultChargeType(this.b, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDestroy() {
        BaseUtils.D("AlipayOrGFrament", "onDestroy ********************************");
        a = null;
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        this.b.a = i2;
        switch (i) {
            case 13:
                this.b.removeDialog(7);
                switch (i2) {
                    case com.mappn.sdk.uc.util.Constants.RETURN_7_ERR_NULL_APP /* -7 */:
                        this.b.showDialog(19);
                        return;
                    case com.mappn.sdk.uc.util.Constants.RETURN_4_ERR_NULL_PACKAGES /* -4 */:
                        this.b.showDialog(20);
                        return;
                    case -1:
                        this.b.showDialog(13);
                        return;
                    default:
                        this.b.a = i2;
                        this.b.showDialog(10);
                        return;
                }
            case 14:
            case 17:
            case 18:
            default:
                return;
            case 15:
                this.b.removeDialog(7);
                if (-1 == i2) {
                    this.b.showDialog(13);
                    return;
                } else {
                    this.b.showDialog(10);
                    return;
                }
            case 16:
                switch (i2) {
                    case -1:
                        this.b.removeDialog(7);
                        this.b.showDialog(13);
                        return;
                    case 0:
                    case 1:
                    default:
                        this.b.a = i2;
                        this.b.removeDialog(7);
                        this.b.showDialog(10);
                        return;
                    case 2:
                        if (!this.b.isOutTime()) {
                            new Thread(new c(this)).start();
                            return;
                        } else {
                            this.b.removeDialog(7);
                            this.b.showDialog(8);
                            return;
                        }
                }
            case 19:
                this.b.removeDialog(7);
                if (-1 == i2) {
                    this.b.showDialog(13);
                    return;
                } else {
                    this.b.showDialog(10);
                    return;
                }
            case 20:
                switch (i2) {
                    case -1:
                        this.b.removeDialog(7);
                        this.b.showDialog(13);
                        return;
                    case 2:
                        if (!this.b.isOutTime()) {
                            new Thread(new d(this)).start();
                            return;
                        } else {
                            this.b.removeDialog(7);
                            this.b.showDialog(8);
                            return;
                        }
                    case 21:
                        BaseUtils.D("AlipayOrGFragment", "onError ACTION_GET_MO9_ORDER_INFO");
                        this.b.removeDialog(7);
                        if (-1 == i2) {
                            BaseUtils.D("AlipayOrGFragment", "onError ACTION_GET_MO9_ORDER_INFO  DIALOG_CHARGE_NETWORK_ERROR");
                            this.b.showDialog(13);
                            return;
                        } else {
                            BaseUtils.D("AlipayOrGFragment", "onError ACTION_GET_MO9_ORDER_INFO  DIALOG_CHARGE_NETWORK_ERROR");
                            this.b.showDialog(10);
                            return;
                        }
                    case 22:
                        switch (i2) {
                            case 2:
                                if (!this.b.isOutTime()) {
                                    new Thread(new e(this)).start();
                                    BaseUtils.D("AlipayOrGFragment", "onError ACTION_QUERY_CHARGE_MO9_RESULT  onerror again");
                                    return;
                                } else {
                                    BaseUtils.D("AlipayOrGFragment", "onError ACTION_QUERY_CHARGE_MO9_RESULT  timeout");
                                    this.b.removeDialog(7);
                                    this.b.showDialog(8);
                                    return;
                                }
                            default:
                                return;
                        }
                    default:
                        this.b.a = i2;
                        this.b.removeDialog(7);
                        this.b.showDialog(10);
                        return;
                }
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onSuccess(int i, Object obj) {
        switch (i) {
            case 13:
            case 16:
                this.b.mChargeMoney *= 10;
                this.b.removeDialog(7);
                this.b.showDialog(9);
                return;
            case 14:
            case 17:
            case 19:
            case 20:
            default:
                return;
            case 15:
                ArrayList arrayList = (ArrayList) obj;
                String str = (String) arrayList.get(0);
                this.b.mPaymentInfo.getOrder().setOrderID((String) arrayList.get(1));
                if (new MobileSecurePayer().pay(str, this.h, 1, this.b)) {
                    return;
                }
                GfanProgressDialog.dismissDialog();
                this.b.showDialog(10);
                return;
            case 18:
                this.b.removeDialog(18);
                try {
                    this.b.mBalance = Integer.parseInt((String) obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.b.mBalance = 0;
                }
                this.f = 0;
                this.b.getIntent().putExtra(Constants.EXTRA_JIFENGQUAN_BALANCE, this.b.mBalance).putExtra(Constants.EXTRA_G_BALANCE, this.f);
                if (TypeFactory.TYPE_CHARGE_G.equals(this.b.mType)) {
                    this.e.setText(String.format(Constants.TEXT_CHARGE_G_TIP_UPDATED, this.b.mPaymentInfo.getUser().getUserName(), Integer.valueOf(this.f), Integer.valueOf(this.b.mBalance)));
                    if (this.f <= 0) {
                        this.mEtInputChargeMoney.setText("0");
                        return;
                    }
                    int money = (this.f / 60) - this.b.mPaymentInfo.getOrder().getMoney();
                    if (money > 0) {
                        this.mEtInputChargeMoney.setText(new StringBuilder().append(money).toString());
                        this.c.setEnabled(true);
                    } else {
                        int i2 = this.f / 60;
                        if (i2 <= 0) {
                            this.c.setEnabled(false);
                        } else {
                            this.c.setEnabled(true);
                        }
                        this.mEtInputChargeMoney.setText(new StringBuilder().append(i2).toString());
                    }
                } else {
                    this.e.setText(String.format(Constants.TEXT_CHARGE_ALIPAY_TIP, this.b.mPaymentInfo.getUser().getUserName(), Integer.valueOf(this.b.mBalance)));
                    if (1 != this.b.mChargeFlag) {
                        int money2 = this.b.mPaymentInfo.getOrder().getMoney() - this.b.mBalance;
                        if (money2 <= 0) {
                            this.mEtInputChargeMoney.setText("10");
                        } else if (money2 % 10 == 0) {
                            this.mEtInputChargeMoney.setText(new StringBuilder().append(money2 / 10).toString());
                        } else {
                            this.mEtInputChargeMoney.setText(new StringBuilder().append((money2 / 10) + 1).toString());
                        }
                    } else {
                        this.mEtInputChargeMoney.setText("10");
                    }
                }
                Selection.setSelection(this.mEtInputChargeMoney.getText(), this.mEtInputChargeMoney.length());
                return;
            case 21:
                BaseUtils.D("AlipayOrGFragment", "onSuccess ACTION_GET_MO9_ORDER_INFO");
                ArrayList arrayList2 = (ArrayList) obj;
                String str2 = (String) arrayList2.get(0);
                this.b.mPaymentInfo.getOrder().setOrderID((String) arrayList2.get(1));
                MktPluginSetting mktPluginSetting = new MktPluginSetting(str2);
                Intent intent = new Intent();
                intent.setClass(this.b, MktPayment.class);
                intent.putExtra("mokredit_android", mktPluginSetting);
                this.b.startActivityForResult(intent, 100);
                return;
            case 22:
                this.b.mChargeMoney *= 10;
                this.b.removeDialog(7);
                this.b.showDialog(9);
                return;
        }
    }

    public void queryMO9Result() {
        BaseUtils.D("AlipayOrGFragment", "queryMO9Result is run ");
        Api.queryM9Result(this.b, this, this.b.mPaymentInfo.getOrder().getOrderID());
    }

    public void removeTextListener() {
        if (this.mEtInputChargeMoney != null) {
            this.mEtInputChargeMoney.removeTextChangedListener(this.g);
        }
    }

    public void showAliOrGView() {
        this.b.setContentView(BaseUtils.get_R_Layout(this.b, "gfan_charge_alipay"));
        String name = TypeFactory.factory(this.b.mType, this.b).getName();
        TopBar.createTopBar(this.b, new View[]{this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_title")), this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_img")), this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_tv"))}, new int[]{0, 0, 0}, name.subSequence(0, Math.min(7, name.length())).toString());
        TextView textView = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_title"));
        textView.setCompoundDrawablePadding(10);
        BaseUtils.D("test", "mType :" + this.b.mType);
        String typeIconFileName = TypeFactory.getTypeIconFileName(this.b.mType);
        BaseUtils.D("test", "pic :" + typeIconFileName);
        textView.setCompoundDrawablesWithIntrinsicBounds(this.b.getResources().getDrawable(BaseUtils.get_R_Drawable(this.b, typeIconFileName)), (Drawable) null, (Drawable) null, (Drawable) null);
        textView.setTextColor(-1);
        textView.setBackgroundColor(16711680);
        TextView textView2 = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "top_bar_pay_tv"));
        textView2.setSingleLine();
        textView2.setFocusable(true);
        textView2.setClickable(true);
        textView2.setLinkTextColor(-1);
        textView2.setText(Html.fromHtml(Constants.TEXT_CHARGE_CHANGE_TYPE));
        textView2.setOnClickListener(new a(this));
        this.e = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "charge_tv_balance_info"));
        this.e.setTextColor(-13487566);
        TextView textView3 = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "charge_tv_input"));
        textView3.setTextColor(-13487566);
        if ("alipay".equals(this.b.mType)) {
            textView3.setText(Html.fromHtml(Constants.TEXT_CHARGE_PHONECARD_INFO));
            textView3.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.b.getResources().getDrawable(BaseUtils.get_R_Drawable(this.b, Utils.isHdpi() ? Constants.RES_ALIPAY_HDPI : "alipay")), (Drawable) null);
            textView3.setCompoundDrawablePadding(10);
        } else if (TypeFactory.TYPE_CHARGE_G.equals(this.b.mType)) {
            textView3.setText(Html.fromHtml(Constants.TEXT_CHARGE_G_INFO));
        } else if (TypeFactory.TYPE_CHARGE_NETBANK.equals(this.b.mType)) {
            textView3.setText(Html.fromHtml(Constants.TEXT_CHARGE_PHONECARD_INFO));
            textView3.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.b.getResources().getDrawable(BaseUtils.get_R_Drawable(this.b, Utils.isHdpi() ? Constants.RES_NETBANK_HDPI : Constants.RES_NETBANK)), (Drawable) null);
            textView3.setCompoundDrawablePadding(10);
        } else if ("mo9".equals(this.b.mType)) {
            textView3.setText(Html.fromHtml(Constants.TEXT_CHARGE_PHONECARD_INFO));
            textView3.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.b.getResources().getDrawable(BaseUtils.get_R_Drawable(this.b, Utils.isHdpi() ? Constants.RES_MO9_HDPI : "mo9")), (Drawable) null);
            textView3.setCompoundDrawablePadding(10);
        }
        this.mEtInputChargeMoney = (EditText) this.b.findViewById(BaseUtils.get_R_id(this.b, "charge_edit_input"));
        this.mEtInputChargeMoney.setSingleLine();
        this.mEtInputChargeMoney.setTextColor(ColorStateList.valueOf(-13487566));
        this.d = (TextView) this.b.findViewById(BaseUtils.get_R_id(this.b, "charge_tv_exchangeinfo"));
        this.d.setTextColor(-13487566);
        this.c = (Button) this.b.findViewById(BaseUtils.get_R_id(this.b, "charge_btn_ok"));
        this.c.setOnClickListener(new b(this));
        this.c.setEnabled(false);
        Intent intent = this.b.getIntent();
        if (TypeFactory.TYPE_CHARGE_G.equals(this.b.mType)) {
            this.c.setText(Constants.TEXT_CHARGE_OK_G);
            this.e.setText(String.format(Constants.TEXT_CHARGE_G_TIP, this.b.mPaymentInfo.getUser().getUserName()));
            this.mEtInputChargeMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.CHARGE_G_INPUT_LENGTH_MAX)});
            this.d.setText(Constants.TEXT_JIFENGQUAN);
            this.mEtInputChargeMoney.addTextChangedListener(this.g);
            if (!intent.hasExtra(Constants.EXTRA_G_BALANCE)) {
                this.b.showDialog(18);
                Api.queryUserProfile(this.b, this.b.mPaymentInfo.getUser().getUid(), this);
                return;
            }
            this.f = intent.getIntExtra(Constants.EXTRA_G_BALANCE, 0);
            this.e.setText(String.format(Constants.TEXT_CHARGE_G_TIP_UPDATED, this.b.mPaymentInfo.getUser().getUserName(), Integer.valueOf(this.f), Integer.valueOf(intent.getIntExtra(Constants.EXTRA_JIFENGQUAN_BALANCE, 0))));
            int money = (this.f / 60) - this.b.mPaymentInfo.getOrder().getMoney();
            if (money > 0) {
                this.mEtInputChargeMoney.setText(new StringBuilder().append(money).toString());
                this.c.setEnabled(true);
            } else {
                int i = this.f / 60;
                if (i <= 0) {
                    this.c.setEnabled(false);
                } else {
                    this.c.setEnabled(true);
                }
                this.mEtInputChargeMoney.setText(new StringBuilder().append(i).toString());
            }
            Selection.setSelection(this.mEtInputChargeMoney.getText(), this.mEtInputChargeMoney.length());
            return;
        }
        if ("alipay".equals(this.b.mType)) {
            this.c.setText(Constants.TEXT_CHARGE_OK_ALIPAY);
            if (intent.hasExtra(Constants.EXTRA_JIFENGQUAN_BALANCE)) {
                this.b.mBalance = intent.getIntExtra(Constants.EXTRA_JIFENGQUAN_BALANCE, 0);
                this.e.setText(String.format(Constants.TEXT_CHARGE_ALIPAY_TIP, this.b.mPaymentInfo.getUser().getUserName(), Integer.valueOf(this.b.mBalance)));
            } else {
                this.e.setText(String.format(Constants.TEXT_CHARGE_G_TIP, this.b.mPaymentInfo.getUser().getUserName()));
                this.b.showDialog(18);
                Api.queryUserProfile(this.b, this.b.mPaymentInfo.getUser().getUid(), this);
            }
            this.mEtInputChargeMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.CHARGE_ALIPAY_INPUT_LENGTH_MAX)});
            this.d.setText(Constants.TEXT_YUAN);
            this.mEtInputChargeMoney.addTextChangedListener(this.g);
            if (1 != this.b.mChargeFlag) {
                int money2 = this.b.mPaymentInfo.getOrder().getMoney() - this.b.mBalance;
                if (money2 <= 0) {
                    this.mEtInputChargeMoney.setText("10");
                } else if (money2 % 10 == 0) {
                    this.mEtInputChargeMoney.setText(new StringBuilder().append(money2 / 10).toString());
                } else {
                    this.mEtInputChargeMoney.setText(new StringBuilder().append((money2 / 10) + 1).toString());
                }
            } else {
                this.mEtInputChargeMoney.setText("10");
            }
            Selection.setSelection(this.mEtInputChargeMoney.getText(), this.mEtInputChargeMoney.length());
            return;
        }
        if (TypeFactory.TYPE_CHARGE_NETBANK.equals(this.b.mType)) {
            this.c.setText(Constants.TEXT_CHARGE_OK_NETBANK);
            if (intent.hasExtra(Constants.EXTRA_JIFENGQUAN_BALANCE)) {
                this.b.mBalance = intent.getIntExtra(Constants.EXTRA_JIFENGQUAN_BALANCE, 0);
                this.e.setText(String.format(Constants.TEXT_CHARGE_ALIPAY_TIP, this.b.mPaymentInfo.getUser().getUserName(), Integer.valueOf(this.b.mBalance)));
            } else {
                this.e.setText(String.format(Constants.TEXT_CHARGE_G_TIP, this.b.mPaymentInfo.getUser().getUserName()));
                this.b.showDialog(18);
                Api.queryUserProfile(this.b, this.b.mPaymentInfo.getUser().getUid(), this);
            }
            this.mEtInputChargeMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.CHARGE_ALIPAY_INPUT_LENGTH_MAX)});
            this.d.setText(Constants.TEXT_YUAN);
            this.mEtInputChargeMoney.addTextChangedListener(this.g);
            if (1 != this.b.mChargeFlag) {
                int money3 = this.b.mPaymentInfo.getOrder().getMoney() - this.b.mBalance;
                if (money3 <= 0) {
                    this.mEtInputChargeMoney.setText("10");
                } else if (money3 % 10 == 0) {
                    this.mEtInputChargeMoney.setText(new StringBuilder().append(money3 / 10).toString());
                } else {
                    this.mEtInputChargeMoney.setText(new StringBuilder().append((money3 / 10) + 1).toString());
                }
            } else {
                this.mEtInputChargeMoney.setText("10");
            }
            Selection.setSelection(this.mEtInputChargeMoney.getText(), this.mEtInputChargeMoney.length());
            return;
        }
        if ("mo9".equals(this.b.mType)) {
            this.c.setText(Constants.TEXT_CHARGE_OK_MO9);
            if (intent.hasExtra(Constants.EXTRA_JIFENGQUAN_BALANCE)) {
                this.b.mBalance = intent.getIntExtra(Constants.EXTRA_JIFENGQUAN_BALANCE, 0);
                this.e.setText(String.format(Constants.TEXT_CHARGE_ALIPAY_TIP, this.b.mPaymentInfo.getUser().getUserName(), Integer.valueOf(this.b.mBalance)));
            } else {
                this.e.setText(String.format(Constants.TEXT_CHARGE_G_TIP, this.b.mPaymentInfo.getUser().getUserName()));
                this.b.showDialog(18);
                Api.queryUserProfile(this.b, this.b.mPaymentInfo.getUser().getUid(), this);
            }
            this.mEtInputChargeMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Constants.CHARGE_ALIPAY_INPUT_LENGTH_MAX)});
            this.d.setText(Constants.TEXT_YUAN);
            this.mEtInputChargeMoney.addTextChangedListener(this.g);
            if (1 != this.b.mChargeFlag) {
                int money4 = this.b.mPaymentInfo.getOrder().getMoney() - this.b.mBalance;
                if (money4 <= 0) {
                    this.mEtInputChargeMoney.setText("10");
                } else if (money4 % 10 == 0) {
                    this.mEtInputChargeMoney.setText(new StringBuilder().append(money4 / 10).toString());
                } else {
                    this.mEtInputChargeMoney.setText(new StringBuilder().append((money4 / 10) + 1).toString());
                }
            } else {
                this.mEtInputChargeMoney.setText("10");
            }
            Selection.setSelection(this.mEtInputChargeMoney.getText(), this.mEtInputChargeMoney.length());
        }
    }
}
