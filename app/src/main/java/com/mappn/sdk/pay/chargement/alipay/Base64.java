package com.mappn.sdk.pay.chargement.alipay;

import com.mokredit.payment.StringUtils;
import org.bson.BSON;

/* loaded from: classes.dex */
public final class Base64 {
    private static final byte[] a = new byte[128];
    private static final char[] b = new char[64];

    static {
        int i = 0;
        for (int i2 = 0; i2 < 128; i2++) {
            a[i2] = -1;
        }
        for (int i3 = 90; i3 >= 65; i3--) {
            a[i3] = (byte) (i3 - 65);
        }
        for (int i4 = 122; i4 >= 97; i4--) {
            a[i4] = (byte) ((i4 - 97) + 26);
        }
        for (int i5 = 57; i5 >= 48; i5--) {
            a[i5] = (byte) ((i5 - 48) + 52);
        }
        a[43] = 62;
        a[47] = 63;
        for (int i6 = 0; i6 <= 25; i6++) {
            b[i6] = (char) (i6 + 65);
        }
        int i7 = 26;
        int i8 = 0;
        while (i7 <= 51) {
            b[i7] = (char) (i8 + 97);
            i7++;
            i8++;
        }
        int i9 = 52;
        while (i9 <= 61) {
            b[i9] = (char) (i + 48);
            i9++;
            i++;
        }
        b[62] = '+';
        b[63] = '/';
    }

    private static boolean a(char c) {
        return c == '=';
    }

    private static boolean b(char c) {
        return c < 128 && a[c] != -1;
    }

    public static byte[] decode(String str) {
        int i;
        int i2;
        if (str == null) {
            return null;
        }
        char[] charArray = str.toCharArray();
        if (charArray == null) {
            i = 0;
        } else {
            int length = charArray.length;
            int i3 = 0;
            i = 0;
            while (i3 < length) {
                char c = charArray[i3];
                if (c == ' ' || c == '\r' || c == '\n' || c == '\t') {
                    i2 = i;
                } else {
                    i2 = i + 1;
                    charArray[i] = charArray[i3];
                }
                i3++;
                i = i2;
            }
        }
        if (i % 4 != 0) {
            return null;
        }
        int i4 = i / 4;
        if (i4 == 0) {
            return new byte[0];
        }
        byte[] bArr = new byte[i4 * 3];
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (i7 < i4 - 1) {
            int i8 = i5 + 1;
            char c2 = charArray[i5];
            if (b(c2)) {
                int i9 = i8 + 1;
                char c3 = charArray[i8];
                if (b(c3)) {
                    int i10 = i9 + 1;
                    char c4 = charArray[i9];
                    if (b(c4)) {
                        i5 = i10 + 1;
                        char c5 = charArray[i10];
                        if (b(c5)) {
                            byte b2 = a[c2];
                            byte b3 = a[c3];
                            byte b4 = a[c4];
                            byte b5 = a[c5];
                            int i11 = i6 + 1;
                            bArr[i6] = (byte) ((b2 << 2) | (b3 >> 4));
                            int i12 = i11 + 1;
                            bArr[i11] = (byte) (((b3 & BSON.CODE_W_SCOPE) << 4) | ((b4 >> 2) & 15));
                            i6 = i12 + 1;
                            bArr[i12] = (byte) ((b4 << 6) | b5);
                            i7++;
                        }
                    }
                }
            }
            return null;
        }
        int i13 = i5 + 1;
        char c6 = charArray[i5];
        if (b(c6)) {
            int i14 = i13 + 1;
            char c7 = charArray[i13];
            if (b(c7)) {
                byte b6 = a[c6];
                byte b7 = a[c7];
                int i15 = i14 + 1;
                char c8 = charArray[i14];
                char c9 = charArray[i15];
                if (b(c8) && b(c9)) {
                    byte b8 = a[c8];
                    byte b9 = a[c9];
                    int i16 = i6 + 1;
                    bArr[i6] = (byte) ((b6 << 2) | (b7 >> 4));
                    bArr[i16] = (byte) (((b7 & BSON.CODE_W_SCOPE) << 4) | ((b8 >> 2) & 15));
                    bArr[i16 + 1] = (byte) (b9 | (b8 << 6));
                    return bArr;
                }
                if (a(c8) && a(c9)) {
                    if ((b7 & BSON.CODE_W_SCOPE) != 0) {
                        return null;
                    }
                    byte[] bArr2 = new byte[(i7 * 3) + 1];
                    System.arraycopy(bArr, 0, bArr2, 0, i7 * 3);
                    bArr2[i6] = (byte) ((b6 << 2) | (b7 >> 4));
                    return bArr2;
                }
                if (a(c8) || !a(c9)) {
                    return null;
                }
                byte b10 = a[c8];
                if ((b10 & 3) != 0) {
                    return null;
                }
                byte[] bArr3 = new byte[(i7 * 3) + 2];
                System.arraycopy(bArr, 0, bArr3, 0, i7 * 3);
                bArr3[i6] = (byte) ((b6 << 2) | (b7 >> 4));
                bArr3[i6 + 1] = (byte) (((b7 & BSON.CODE_W_SCOPE) << 4) | ((b10 >> 2) & 15));
                return bArr3;
            }
        }
        return null;
    }

    public static String encode(byte[] bArr) {
        int i = 0;
        if (bArr == null) {
            return null;
        }
        int length = bArr.length << 3;
        if (length == 0) {
            return StringUtils.EMPTY;
        }
        int i2 = length % 24;
        int i3 = length / 24;
        char[] cArr = new char[(i2 != 0 ? i3 + 1 : i3) << 2];
        int i4 = 0;
        int i5 = 0;
        while (i4 < i3) {
            int i6 = i + 1;
            byte b2 = bArr[i];
            int i7 = i6 + 1;
            byte b3 = bArr[i6];
            int i8 = i7 + 1;
            byte b4 = bArr[i7];
            byte b5 = (byte) (b3 & BSON.CODE_W_SCOPE);
            byte b6 = (byte) (b2 & 3);
            byte b7 = (b2 & Byte.MIN_VALUE) == 0 ? (byte) (b2 >> 2) : (byte) ((b2 >> 2) ^ 192);
            byte b8 = (b3 & Byte.MIN_VALUE) == 0 ? (byte) (b3 >> 4) : (byte) ((b3 >> 4) ^ 240);
            int i9 = (b4 & Byte.MIN_VALUE) == 0 ? b4 >> 6 : (b4 >> 6) ^ 252;
            int i10 = i5 + 1;
            cArr[i5] = b[b7];
            int i11 = i10 + 1;
            cArr[i10] = b[b8 | (b6 << 4)];
            int i12 = i11 + 1;
            cArr[i11] = b[((byte) i9) | (b5 << 2)];
            cArr[i12] = b[b4 & 63];
            i4++;
            i5 = i12 + 1;
            i = i8;
        }
        if (i2 == 8) {
            byte b9 = bArr[i];
            byte b10 = (byte) (b9 & 3);
            int i13 = i5 + 1;
            cArr[i5] = b[(b9 & Byte.MIN_VALUE) == 0 ? (byte) (b9 >> 2) : (byte) ((b9 >> 2) ^ 192)];
            int i14 = i13 + 1;
            cArr[i13] = b[b10 << 4];
            cArr[i14] = '=';
            cArr[i14 + 1] = '=';
        } else if (i2 == 16) {
            byte b11 = bArr[i];
            byte b12 = bArr[i + 1];
            byte b13 = (byte) (b12 & BSON.CODE_W_SCOPE);
            byte b14 = (byte) (b11 & 3);
            byte b15 = (b11 & Byte.MIN_VALUE) == 0 ? (byte) (b11 >> 2) : (byte) ((b11 >> 2) ^ 192);
            byte b16 = (b12 & Byte.MIN_VALUE) == 0 ? (byte) (b12 >> 4) : (byte) ((b12 >> 4) ^ 240);
            int i15 = i5 + 1;
            cArr[i5] = b[b15];
            int i16 = i15 + 1;
            cArr[i15] = b[b16 | (b14 << 4)];
            cArr[i16] = b[b13 << 2];
            cArr[i16 + 1] = '=';
        }
        return new String(cArr);
    }
}
