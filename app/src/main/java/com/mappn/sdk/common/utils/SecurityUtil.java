package com.mappn.sdk.common.utils;

import com.mappn.sdk.common.codec.binary.Base64;

/* loaded from: classes.dex */
public class SecurityUtil {
    private static final byte[] a = "sdk_mappn_201008".getBytes();
    private static final byte[] b = "MAPPN-ANDY-XIAN-".getBytes();
    public static final String KEY_HTTP_CHARGE_ALIPAY_AND_G = "6R4ya0Nee7aLgl4k";
    public static final byte[] SECRET_KEY_HTTP_CHARGE_ALIPAY = KEY_HTTP_CHARGE_ALIPAY_AND_G.getBytes();

    public static String decryptHttpChargeAlipayBody(byte[] bArr) {
        return BaseUtils.getUTF8String(new Crypter().decrypt(Base64.decodeBase64(bArr), SECRET_KEY_HTTP_CHARGE_ALIPAY));
    }

    public static byte[] encryptHttpBody(String str) {
        return Base64.encodeBase64(new Crypter().encrypt(BaseUtils.getUTF8Bytes(str), a));
    }

    public static byte[] encryptHttpChargeAlipayBody(String str) {
        return Base64.encodeBase64(new Crypter().encrypt(BaseUtils.getUTF8Bytes(str), SECRET_KEY_HTTP_CHARGE_ALIPAY));
    }

    public static byte[] encryptHttpChargePhoneCardBody(String str) {
        return new Crypter().encrypt(BaseUtils.getUTF8Bytes(str), b);
    }
}
