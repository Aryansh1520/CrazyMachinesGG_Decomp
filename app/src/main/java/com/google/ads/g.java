package com.google.ads;

import com.mappn.sdk.common.utils.BaseConstants;

/* loaded from: classes.dex */
public final class g {

    /* loaded from: classes.dex */
    public enum a {
        AD,
        NO_FILL,
        ERROR,
        TIMEOUT,
        NOT_FOUND,
        EXCEPTION
    }

    public static String a(String str, String str2, Boolean bool, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        String replaceAll = str.replaceAll("@gw_adlocid@", str2).replaceAll("@gw_qdata@", str6).replaceAll("@gw_sdkver@", "afma-sdk-a-v6.1.0").replaceAll("@gw_sessid@", str7).replaceAll("@gw_seqnum@", str8).replaceAll("@gw_devid@", str3);
        if (str5 != null) {
            replaceAll = replaceAll.replaceAll("@gw_adnetid@", str5);
        }
        if (str4 != null) {
            replaceAll = replaceAll.replaceAll("@gw_allocid@", str4);
        }
        if (str9 != null) {
            replaceAll = replaceAll.replaceAll("@gw_adt@", str9);
        }
        if (str10 != null) {
            replaceAll = replaceAll.replaceAll("@gw_aec@", str10);
        }
        if (bool != null) {
            return replaceAll.replaceAll("@gw_adnetrefresh@", bool.booleanValue() ? BaseConstants.DEFAULT_UC_CNO : "0");
        }
        return replaceAll;
    }

    public static <T> T a(String str, Class<T> cls) throws ClassNotFoundException, ClassCastException, IllegalAccessException, InstantiationException, LinkageError, ExceptionInInitializerError {
        return cls.cast(Class.forName(str).newInstance());
    }
}
