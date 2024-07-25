package com.mappn.sdk.uc.util;

/* loaded from: classes.dex */
public class StringUtil {
    public static byte[] hexString2Bytes(String str) {
        try {
            byte[] bArr = new byte[str.length() / 2];
            byte[] bytes = str.getBytes();
            for (int i = 0; i < bytes.length / 2; i++) {
                bArr[i] = uniteBytes(bytes[i << 1], bytes[(i << 1) + 1]);
            }
            return bArr;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte uniteBytes(byte b, byte b2) {
        return (byte) (((byte) (Byte.decode("0x" + new String(new byte[]{b})).byteValue() << 4)) ^ Byte.decode("0x" + new String(new byte[]{b2})).byteValue());
    }
}
