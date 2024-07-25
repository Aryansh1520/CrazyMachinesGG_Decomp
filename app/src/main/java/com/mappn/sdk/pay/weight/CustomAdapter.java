package com.mappn.sdk.pay.weight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.model.IType;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.Utils;

/* loaded from: classes.dex */
public class CustomAdapter extends BaseAdapter {
    private static Drawable c;
    private Context a;
    private IType[] b;

    public CustomAdapter(Context context, IType[] iTypeArr) {
        this.a = context;
        this.b = iTypeArr;
        if (c == null) {
            c = context.getResources().getDrawable(BaseUtils.get_R_Drawable(context, Utils.isHdpi() ? Constants.RES_LIST_ITEM_MORE_ICON_HDPI : Constants.RES_LIST_ITEM_MORE_ICON));
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.b == null) {
            return 0;
        }
        return this.b.length;
    }

    @Override // android.widget.Adapter
    public IType getItem(int i) {
        if (this.b == null) {
            return null;
        }
        return this.b[i];
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(this.a).inflate(BaseUtils.get_R_Layout(this.a, "gfan_charge_list_item"), viewGroup, false);
        CustomTextView customTextView = (CustomTextView) inflate.findViewById(BaseUtils.get_R_id(this.a, "tv_item"));
        customTextView.setTextColor(-13487566);
        customTextView.setText(Html.fromHtml(this.b[i].getDesc()));
        customTextView.setCompoundDrawablePadding(6);
        customTextView.setCompoundDrawablesWithIntrinsicBounds(this.b[i].getIcon(), (Drawable) null, c, (Drawable) null);
        customTextView.setPadding(3, 2, 3, 2);
        customTextView.setGravity(16);
        return inflate;
    }
}
