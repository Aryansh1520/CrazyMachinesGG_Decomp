package com.mappn.sdk.pay.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.mappn.sdk.common.utils.BaseUtils;
import java.lang.ref.SoftReference;

/* loaded from: classes.dex */
public abstract class AbstractType implements IType {
    private String a;
    private String b;
    private String c;
    private SoftReference d;
    private String e;
    private Context f;

    public AbstractType(String str, String str2, String str3, String str4, Context context) {
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.e = str4;
        this.f = context;
        this.d = new SoftReference(context.getResources().getDrawable(BaseUtils.get_R_Drawable(context, str4.contains(".") ? str4.substring(0, str4.indexOf(".")) : str4)));
    }

    @Override // com.mappn.sdk.pay.model.IType
    public String getDesc() {
        return this.c;
    }

    @Override // com.mappn.sdk.pay.model.IType
    public Drawable getIcon() {
        Drawable drawable = (Drawable) this.d.get();
        if (drawable != null) {
            return drawable;
        }
        if (this.e.contains(".")) {
            this.e = this.e.substring(0, this.e.indexOf("."));
        }
        Drawable drawable2 = this.f.getResources().getDrawable(BaseUtils.get_R_Drawable(this.f, this.e));
        this.d = new SoftReference(drawable2);
        return drawable2;
    }

    @Override // com.mappn.sdk.pay.model.IType
    public String getId() {
        return this.a;
    }

    @Override // com.mappn.sdk.pay.model.IType
    public String getName() {
        return this.b;
    }
}
