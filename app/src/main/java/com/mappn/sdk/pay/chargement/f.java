package com.mappn.sdk.pay.chargement;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.util.Constants;
import com.mokredit.payment.StringUtils;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class f implements TextWatcher {
    private /* synthetic */ AlipayOrGFragment a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(AlipayOrGFragment alipayOrGFragment) {
        this.a = alipayOrGFragment;
    }

    @Override // android.text.TextWatcher
    public final void afterTextChanged(Editable editable) {
        ChargeActivity chargeActivity;
        Button button;
        TextView textView;
        int parseInt;
        Button button2;
        TextView textView2;
        ChargeActivity chargeActivity2;
        ChargeActivity chargeActivity3;
        Button button3;
        TextView textView3;
        TextView textView4;
        int i;
        Button button4;
        Button button5;
        chargeActivity = this.a.b;
        if (!"alipay".equals(chargeActivity.mType)) {
            chargeActivity2 = this.a.b;
            if (!TypeFactory.TYPE_CHARGE_NETBANK.equals(chargeActivity2.mType)) {
                chargeActivity3 = this.a.b;
                if (!"mo9".equals(chargeActivity3.mType)) {
                    if (editable.length() <= 0) {
                        button3 = this.a.c;
                        button3.setEnabled(false);
                        textView3 = this.a.d;
                        textView3.setText(Constants.TEXT_JIFENGQUAN);
                        return;
                    }
                    int parseInt2 = Integer.parseInt(editable.toString());
                    if (parseInt2 != 0) {
                        int i2 = parseInt2 * 60;
                        textView4 = this.a.d;
                        textView4.setText(String.format(Constants.TEXT_CHARGE_G_INFO2, Integer.valueOf(i2)));
                        i = this.a.f;
                        if (i2 > i) {
                            AlipayOrGFragment.e(this.a).setText(Constants.TEXT_CHARGE_G_INSUFFENT_BALANCE);
                            AlipayOrGFragment.e(this.a).setVisibility(0);
                            button5 = this.a.c;
                            button5.setEnabled(false);
                            return;
                        }
                        AlipayOrGFragment.e(this.a).setVisibility(8);
                        AlipayOrGFragment.e(this.a).setText(StringUtils.EMPTY);
                        button4 = this.a.c;
                        button4.setEnabled(true);
                        return;
                    }
                    return;
                }
            }
        }
        if (editable.length() <= 0 || (parseInt = Integer.parseInt(editable.toString())) <= 0) {
            button = this.a.c;
            button.setEnabled(false);
            textView = this.a.d;
            textView.setText(Constants.TEXT_YUAN);
            return;
        }
        button2 = this.a.c;
        button2.setEnabled(true);
        textView2 = this.a.d;
        textView2.setText(String.format(Constants.TEXT_CHARGE_ALIPAY_CHARGE_TIP, Integer.valueOf(parseInt * 10)));
    }

    @Override // android.text.TextWatcher
    public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.text.TextWatcher
    public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }
}
