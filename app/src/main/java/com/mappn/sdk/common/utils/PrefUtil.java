package com.mappn.sdk.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class PrefUtil {
    public static final String Pref_Name = "gfan_base_sharePref";
    private static PrefUtil d;
    private Context a;
    private SharedPreferences b = null;
    private SharedPreferences.Editor c;

    public PrefUtil(Context context) {
        this.a = context;
    }

    public static PrefUtil getInstance(Context context) {
        if (d == null) {
            PrefUtil prefUtil = new PrefUtil(context);
            d = prefUtil;
            prefUtil.open();
        }
        return d;
    }

    public void close() {
        d = null;
        this.b = null;
    }

    public boolean getBoolean(String str, boolean z) {
        if (this.b != null) {
            return this.b.getBoolean(str, z);
        }
        return false;
    }

    public int getInt(String str, int i) {
        if (this.b != null) {
            return this.b.getInt(str, i);
        }
        return 0;
    }

    public String getString(String str, String str2) {
        if (this.b != null) {
            return this.b.getString(str, str2);
        }
        return null;
    }

    public void open() {
        this.b = this.a.getSharedPreferences(Pref_Name, 0);
    }

    public PrefUtil save(String str, int i) {
        if (this.b != null) {
            this.c = this.b.edit();
            this.c.putInt(str, i);
            this.c.commit();
        }
        return this;
    }

    public PrefUtil save(String str, String str2) {
        if (this.b != null) {
            this.c = this.b.edit();
            this.c.putString(str, str2);
            this.c.commit();
        }
        return this;
    }

    public PrefUtil save(String str, boolean z) {
        if (this.b != null) {
            this.c = this.b.edit();
            this.c.putBoolean(str, z);
            this.c.commit();
        }
        return this;
    }
}
