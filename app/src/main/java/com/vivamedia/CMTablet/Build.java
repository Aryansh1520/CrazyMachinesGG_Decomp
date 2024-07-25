package com.vivamedia.CMTablet;

/* loaded from: classes.dex */
public class Build {
    public static final Store STORE = Store.GOOGLE;
    public static final Version VERSION = Version.LITE;

    /* loaded from: classes.dex */
    public enum Store {
        NONE,
        AMAZON,
        GFAN,
        GOOGLE,
        XIAOMI;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static Store[] valuesCustom() {
            Store[] valuesCustom = values();
            int length = valuesCustom.length;
            Store[] storeArr = new Store[length];
            System.arraycopy(valuesCustom, 0, storeArr, 0, length);
            return storeArr;
        }
    }

    /* loaded from: classes.dex */
    public enum Version {
        FULL,
        LITE,
        TRIAL;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static Version[] valuesCustom() {
            Version[] valuesCustom = values();
            int length = valuesCustom.length;
            Version[] versionArr = new Version[length];
            System.arraycopy(valuesCustom, 0, versionArr, 0, length);
            return versionArr;
        }
    }
}
