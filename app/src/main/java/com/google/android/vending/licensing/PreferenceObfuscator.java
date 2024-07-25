package com.google.android.vending.licensing;

import android.content.SharedPreferences;
import android.util.Log;

/* loaded from: classes.dex */
public class PreferenceObfuscator {
    private static final String TAG = "PreferenceObfuscator";
    private SharedPreferences.Editor mEditor = null;
    private final Obfuscator mObfuscator;
    private final SharedPreferences mPreferences;

    public PreferenceObfuscator(SharedPreferences sp, Obfuscator o) {
        this.mPreferences = sp;
        this.mObfuscator = o;
    }

    public void putString(String key, String value) {
        if (this.mEditor == null) {
            this.mEditor = this.mPreferences.edit();
        }
        String obfuscatedValue = this.mObfuscator.obfuscate(value, key);
        this.mEditor.putString(key, obfuscatedValue);
    }

    public String getString(String key, String defValue) {
        String value = this.mPreferences.getString(key, null);
        if (value != null) {
            try {
                String result = this.mObfuscator.unobfuscate(value, key);
                return result;
            } catch (ValidationException e) {
                Log.w(TAG, "Validation error while reading preference: " + key);
                return defValue;
            }
        }
        return defValue;
    }

    public void commit() {
        if (this.mEditor != null) {
            this.mEditor.commit();
            this.mEditor = null;
        }
    }
}
