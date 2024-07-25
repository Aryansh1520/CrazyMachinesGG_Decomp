package com.mappn.sdk.pay.chargement;

import android.R;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.chargement.ChargeActivity;
import com.mappn.sdk.pay.chargement.phonecard.CardInfo;
import com.mappn.sdk.pay.chargement.phonecard.CardsVerification;
import com.mappn.sdk.pay.chargement.phonecard.CardsVerifications;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.net.chain.Handler;
import com.mappn.sdk.pay.net.chain.HandlerProxy;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.pay.util.TopBar;
import com.mappn.sdk.pay.util.Utils;
import com.mappn.sdk.pay.weight.TitleSpinner;
import com.mokredit.payment.StringUtils;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class PhoneCardFragment implements DialogInterface.OnClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, ApiRequestListener, Handler.OnFinishListener {
    private static PhoneCardFragment c;
    EditText a;
    EditText b;
    private ChargeActivity d;
    private ViewAnimator e;
    private TitleSpinner f;
    private TitleSpinner g;
    private ChargeActivity.LoadingOrErrorView h;
    private NumberKeyListener i = new o(this);
    public CardInfo mCard;
    public int[] mCardMoney;
    public CardsVerification mCardVerification;
    public ArrayList mCards;
    public int mCheckedId;

    private PhoneCardFragment(ChargeActivity chargeActivity) {
        this.d = chargeActivity;
    }

    private void a(CardsVerifications cardsVerifications) {
        if (1 == this.e.getChildCount()) {
            ViewAnimator viewAnimator = this.e;
            if (this.d.getIntent().hasExtra(Constants.EXTRA_JIFENGQUAN_BALANCE)) {
                this.d.mBalance = this.d.getIntent().getIntExtra(Constants.EXTRA_JIFENGQUAN_BALANCE, 0);
            }
            View inflate = LayoutInflater.from(this.d).inflate(BaseUtils.get_R_Layout(this.d, "gfan_charge_phonecard"), (ViewGroup) null);
            this.mCards = cardsVerifications.cards;
            ArrayAdapter arrayAdapter = new ArrayAdapter(this.d, R.layout.simple_spinner_item, cardsVerifications.getCardNames());
            arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            this.f = (TitleSpinner) inflate.findViewById(BaseUtils.get_R_id(this.d, "phonecard_cardspinner"));
            this.f.setText(Constants.TEXT_CHARGE_CHOOSE_PHONECARD_TYPE);
            this.f.setPrompt(Constants.TEXT_CHARGE_CHOOSE_PHONECARD_TYPE);
            this.f.setAdapter(arrayAdapter);
            this.f.setTag(cardsVerifications);
            this.f.setOnClickListener((DialogInterface.OnClickListener) this);
            this.g = (TitleSpinner) inflate.findViewById(BaseUtils.get_R_id(this.d, "phonecard_paynumspinner"));
            this.g.setText(Constants.TEXT_CHARGE_CHOOSE_MONEY);
            this.g.setPrompt(Constants.TEXT_CHARGE_CHOOSE_MONEY);
            this.a = (EditText) inflate.findViewById(BaseUtils.get_R_id(this.d, "phonecard_edittext_cardnum"));
            this.a.setHint(Constants.TEXT_CHARGE_INPUT_CARDNUMBER);
            this.a.setSingleLine(true);
            this.a.setTextColor(ColorStateList.valueOf(-13487566));
            this.a.setKeyListener(this.i);
            this.b = (EditText) inflate.findViewById(BaseUtils.get_R_id(this.d, "phonecard_edittext_cardpsd"));
            this.b.setHint(Constants.TEXT_CHARGE_INPUT_CARDPASSWORD);
            this.b.setSingleLine(true);
            this.a.setTextColor(ColorStateList.valueOf(-13487566));
            this.b.setKeyListener(this.i);
            TextView textView = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.d, "phonecard_textview_confirm"));
            textView.setText(Constants.TEXT_CHARGE_INFO);
            textView.setTextColor(-13487566);
            textView.setFocusable(true);
            textView.setClickable(true);
            textView.setOnClickListener(this);
            Button button = (Button) inflate.findViewById(BaseUtils.get_R_id(this.d, "phonecard_btn_ok"));
            button.setId(0);
            button.setText(Constants.TEXT_CHARGE_SUBMIT);
            button.setTextColor(BaseUtils.get_R_Color(this.d, "text_orange"));
            button.setTextAppearance(this.d, R.attr.textAppearanceMedium);
            button.setBackgroundResource(BaseUtils.get_R_Drawable(this.d, "gfan_selector_btn_orange"));
            button.setOnClickListener(this);
            viewAnimator.addView(inflate, new LinearLayout.LayoutParams(-1, -1));
        }
        this.d.showDialog(18);
        Api.queryUserProfile(this.d, this.d.mPaymentInfo.getUser().getUid(), this);
        this.e.setDisplayedChild(1);
    }

    public static synchronized PhoneCardFragment instance(ChargeActivity chargeActivity) {
        PhoneCardFragment phoneCardFragment;
        synchronized (PhoneCardFragment.class) {
            if (c == null) {
                c = new PhoneCardFragment(chargeActivity);
            }
            phoneCardFragment = c;
        }
        return phoneCardFragment;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            PrefUtil.setDefaultChargeType(this.d, this.d.mType);
        } else {
            PrefUtil.setDefaultChargeType(this.d, null);
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        this.mCardVerification = (CardsVerification) this.mCards.get(i);
        String[] split = this.mCardVerification.credit.split(Constants.TERM);
        int length = split.length;
        this.mCardMoney = new int[length];
        String[] strArr = new String[length];
        for (int i2 = 0; i2 < length; i2++) {
            this.mCardMoney[i2] = Integer.parseInt(split[i2]);
            strArr[i2] = this.mCardMoney[i2] + Constants.TEXT_YUAN;
        }
        this.mCard = new CardInfo();
        this.mCard.payType = this.mCardVerification.pay_type;
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.d, R.layout.simple_spinner_item, strArr);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        this.g.setAdapter(arrayAdapter);
        this.g.setOnClickListener(new q(this));
        String str = StringUtils.EMPTY;
        if (this.mCardVerification != null) {
            str = String.format(Constants.TEXT_CHARGE_PAYCARD_NUM, Integer.valueOf(this.mCardVerification.accountNum));
        }
        this.a.setHint(Constants.TEXT_CHARGE_INPUT_CARDNUMBER + str);
        this.a.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.mCardVerification.accountNum)});
        String str2 = StringUtils.EMPTY;
        if (this.mCardVerification != null) {
            str2 = String.format(Constants.TEXT_CHARGE_PAYCARD_NUM, Integer.valueOf(this.mCardVerification.passwordNum));
        }
        this.b.setHint("密码" + str2);
        this.b.setFilters(new InputFilter[]{new InputFilter.LengthFilter(this.mCardVerification.passwordNum)});
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.h.btnRetry) {
            Api.syncCardInfo(this.d, this);
            return;
        }
        switch (view.getId()) {
            case 0:
                String obj = this.a.getText().toString();
                String obj2 = this.b.getText().toString();
                if (this.mCard == null) {
                    this.d.showDialog(17);
                    return;
                }
                if (this.mCheckedId == -1) {
                    this.d.showDialog(0);
                    return;
                }
                if (TextUtils.isEmpty(obj)) {
                    this.d.showDialog(1);
                    return;
                }
                if (obj.length() != this.mCardVerification.accountNum) {
                    this.d.showDialog(3);
                    return;
                }
                if (TextUtils.isEmpty(obj2)) {
                    this.d.showDialog(2);
                    return;
                }
                if (obj2.length() != this.mCardVerification.passwordNum) {
                    this.d.showDialog(4);
                    return;
                }
                this.mCard.cardAccount = obj;
                this.mCard.cardPassword = obj2;
                this.mCard.cardCredit = this.mCardMoney[this.mCheckedId] * 100;
                this.d.mChargeMoney = this.mCard.cardCredit;
                if (PrefUtil.isLogin(this.d)) {
                    this.d.showDialog(6);
                    return;
                } else {
                    this.d.goLoginActivity();
                    return;
                }
            default:
                this.d.showDialog(16);
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDestroy() {
        BaseUtils.D("PhoneCardFragment", "onDestroy ********************************");
        c = null;
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        switch (i) {
            case 10:
                this.d.removeDialog(7);
                switch (i2) {
                    case -1:
                        this.d.showDialog(13);
                        return;
                    case 0:
                    default:
                        this.d.showDialog(11);
                        return;
                    case 1:
                        this.d.showDialog(8);
                        return;
                }
            case 11:
                this.e.setDisplayedChild(0);
                this.h.pb.setVisibility(8);
                this.h.tvHint.setText(Constants.TEXT_CHARGE_SYNC_ERROR);
                this.h.btnRetry.setVisibility(0);
                return;
            case 12:
                this.d.a = i2;
                switch (i2) {
                    case -1:
                        this.d.removeDialog(7);
                        this.d.showDialog(13);
                        return;
                    case 18:
                        this.d.removeDialog(18);
                        return;
                    case ChargeActivity.CODE_CHARGE_CARD_NO_ENOUGH_BALANCE /* 220 */:
                        this.d.removeDialog(7);
                        this.d.showDialog(12);
                        return;
                    case ChargeActivity.CODE_FAILED /* 221 */:
                        this.d.removeDialog(7);
                        this.d.showDialog(10);
                        return;
                    case ChargeActivity.CODE_CHARGE_SUCCESS_BUT_ADD_JIFENGQUAN_FAILED /* 222 */:
                        this.d.removeDialog(7);
                        this.d.showDialog(15);
                        return;
                    case ChargeActivity.CODE_CARD_OR_PWD_FAILED /* 223 */:
                        this.d.removeDialog(7);
                        this.d.showDialog(14);
                        return;
                    case ChargeActivity.CODE_CHARGEING /* 224 */:
                        if (!this.d.isOutTime()) {
                            new Thread(new p(this)).start();
                            return;
                        } else {
                            this.d.removeDialog(7);
                            this.d.showDialog(8);
                            return;
                        }
                    default:
                        this.d.removeDialog(7);
                        this.d.showDialog(10);
                        return;
                }
            default:
                return;
        }
    }

    @Override // com.mappn.sdk.pay.net.chain.Handler.OnFinishListener
    public void onFinish() {
        if (PrefUtil.supportChargeType(this.d, this.d.mType)) {
            a(Utils.getCardsVerifications());
        } else {
            this.d.showTypeListView(true);
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onSuccess(int i, Object obj) {
        switch (i) {
            case 10:
                this.d.mPaymentInfo.getOrder().setOrderID((String) obj);
                this.d.mLastTime = System.currentTimeMillis();
                Api.queryPhoneCardChargeResult(this.d, this, (String) obj);
                return;
            case 11:
                Utils.setCardsVerifications((CardsVerifications) obj);
                new HandlerProxy(this.d, this).handleRequest();
                return;
            case 12:
                this.d.mChargeMoney /= 100;
                this.d.mChargeMoney *= 10;
                this.d.removeDialog(7);
                this.d.showDialog(9);
                return;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            default:
                return;
            case 18:
                this.d.removeDialog(18);
                try {
                    this.d.mBalance = Integer.parseInt((String) obj);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    this.d.mBalance = 0;
                    return;
                }
        }
    }

    public void showPhonecardView() {
        this.d.setContentView(BaseUtils.get_R_Layout(this.d, "gfan_phonecard_view"));
        String name = TypeFactory.factory(this.d.mType, this.d).getName();
        TopBar.createTopBar(this.d, new View[]{this.d.findViewById(BaseUtils.get_R_id(this.d, "top_bar_pay_title")), this.d.findViewById(BaseUtils.get_R_id(this.d, "top_bar_pay_img")), this.d.findViewById(BaseUtils.get_R_id(this.d, "top_bar_pay_tv"))}, new int[]{0, 0, 0}, name.subSequence(0, Math.min(7, name.length())).toString());
        TextView textView = (TextView) this.d.findViewById(BaseUtils.get_R_id(this.d, "top_bar_pay_title"));
        textView.setCompoundDrawablePadding(10);
        BaseUtils.D("test", "mType :" + this.d.mType);
        String typeIconFileName = TypeFactory.getTypeIconFileName(this.d.mType);
        BaseUtils.D("test", "pic :" + typeIconFileName + " id : " + BaseUtils.get_R_Drawable(this.d, typeIconFileName));
        textView.setCompoundDrawablesWithIntrinsicBounds(this.d.getResources().getDrawable(BaseUtils.get_R_Drawable(this.d, typeIconFileName)), (Drawable) null, (Drawable) null, (Drawable) null);
        textView.setTextColor(-1);
        textView.setBackgroundColor(16711680);
        TextView textView2 = (TextView) this.d.findViewById(BaseUtils.get_R_id(this.d, "top_bar_pay_tv"));
        textView2.setSingleLine();
        textView2.setFocusable(true);
        textView2.setClickable(true);
        textView2.setLinkTextColor(-1);
        textView2.setText(Html.fromHtml(Constants.TEXT_CHARGE_CHANGE_TYPE));
        textView2.setOnClickListener(new n(this));
        this.e = (ViewAnimator) this.d.findViewById(BaseUtils.get_R_id(this.d, "phonecard_viewanimator"));
        this.h = this.d.getLoadingOrErrorView();
        this.e.addView(this.h, new LinearLayout.LayoutParams(-1, -1));
        this.e.setDisplayedChild(0);
        this.h.pb.setVisibility(0);
        this.h.tvHint.setText(Constants.TEXT_CHARGE_SYNC_CARD_INFO);
        this.h.btnRetry.setVisibility(8);
        if (Utils.getCardsVerifications() == null) {
            Api.syncCardInfo(this.d, this);
        } else {
            a(Utils.getCardsVerifications());
        }
        this.mCheckedId = -1;
    }
}
