package com.mappn.sdk.uc.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.uc.util.Constants;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class c extends BaseAdapter implements View.OnClickListener {
    final /* synthetic */ ChooseAccountActivity a;
    private ArrayList b;
    private Context c;
    private BtnOnClick d;

    public c(ChooseAccountActivity chooseAccountActivity, Context context, ArrayList arrayList, BtnOnClick btnOnClick) {
        this.a = chooseAccountActivity;
        this.d = null;
        this.c = context;
        this.b = arrayList;
        this.d = btnOnClick;
    }

    private View a(int i, ViewGroup viewGroup) {
        int intValue = ((Integer) ((HashMap) this.b.get(i)).get(Constants.ACCOUNT_VIEW_TYPE)).intValue();
        LayoutInflater from = LayoutInflater.from(this.c);
        switch (intValue) {
            case 0:
                BaseUtils.D("ChooseAccountActivity", "newView 应该显示text position:" + i);
                e eVar = new e(this);
                View inflate = from.inflate(BaseUtils.get_R_Layout(this.a.getApplicationContext(), "gfan_accounts_items"), viewGroup, false);
                eVar.a = (TextView) inflate.findViewById(BaseUtils.get_R_id(this.a.getApplicationContext(), "account_username"));
                eVar.b = (CheckBox) inflate.findViewById(BaseUtils.get_R_id(this.a.getApplicationContext(), Constants.ACCOUNT_CHECK));
                eVar.c = (ImageView) inflate.findViewById(BaseUtils.get_R_id(this.a.getApplicationContext(), "account_delete"));
                eVar.c.setOnClickListener(this);
                inflate.setTag(eVar);
                return inflate;
            case 1:
                BaseUtils.D("ChooseAccountActivity", "newView 应该显示button position:" + i);
                View inflate2 = from.inflate(BaseUtils.get_R_Layout(this.a.getApplicationContext(), "gfan_accounts_header"), viewGroup, false);
                Button button = (Button) inflate2.findViewById(BaseUtils.get_R_id(this.a.getApplicationContext(), "account_header_left"));
                Button button2 = (Button) inflate2.findViewById(BaseUtils.get_R_id(this.a.getApplicationContext(), "account_header_right"));
                button.setOnClickListener(this);
                button2.setOnClickListener(this);
                return inflate2;
            default:
                return null;
        }
    }

    @Override // android.widget.Adapter
    public final int getCount() {
        if (this.b == null) {
            return 0;
        }
        return this.b.size();
    }

    @Override // android.widget.Adapter
    public final Object getItem(int i) {
        return this.b == null ? new HashMap() : this.b.get(i);
    }

    @Override // android.widget.Adapter
    public final long getItemId(int i) {
        return i;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0046, code lost:
    
        return r11;
     */
    @Override // android.widget.Adapter
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.view.View getView(int r10, android.view.View r11, android.view.ViewGroup r12) {
        /*
            Method dump skipped, instructions count: 316
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.uc.activity.c.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.d.setOnClick(view);
    }
}
