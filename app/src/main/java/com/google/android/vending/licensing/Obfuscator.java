package com.google.android.vending.licensing;

/* loaded from: classes.dex */
public interface Obfuscator {
    String obfuscate(String str, String str2);

    String unobfuscate(String str, String str2) throws ValidationException;
}
