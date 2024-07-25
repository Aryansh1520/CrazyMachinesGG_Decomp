package com.mappn.sdk.common.codec.binary;

import com.mappn.sdk.common.codec.BinaryDecoder;
import com.mappn.sdk.common.codec.BinaryEncoder;
import com.mappn.sdk.common.codec.DecoderException;
import com.mappn.sdk.common.codec.EncoderException;

/* loaded from: classes.dex */
public class BinaryCodec implements BinaryDecoder, BinaryEncoder {
    private static final char[] a = new char[0];
    private static final byte[] b = new byte[0];
    private static final int[] c = {1, 2, 4, 8, 16, 32, 64, 128};

    private static boolean a(byte[] bArr) {
        return bArr == null || bArr.length == 0;
    }

    public static byte[] fromAscii(byte[] bArr) {
        if (a(bArr)) {
            return b;
        }
        byte[] bArr2 = new byte[bArr.length >> 3];
        int length = bArr.length - 1;
        int i = 0;
        while (i < bArr2.length) {
            for (int i2 = 0; i2 < c.length; i2++) {
                if (bArr[length - i2] == 49) {
                    bArr2[i] = (byte) (bArr2[i] | c[i2]);
                }
            }
            i++;
            length -= 8;
        }
        return bArr2;
    }

    public static byte[] fromAscii(char[] cArr) {
        if (cArr == null || cArr.length == 0) {
            return b;
        }
        byte[] bArr = new byte[cArr.length >> 3];
        int length = cArr.length - 1;
        int i = 0;
        while (i < bArr.length) {
            for (int i2 = 0; i2 < c.length; i2++) {
                if (cArr[length - i2] == '1') {
                    bArr[i] = (byte) (bArr[i] | c[i2]);
                }
            }
            i++;
            length -= 8;
        }
        return bArr;
    }

    public static byte[] toAsciiBytes(byte[] bArr) {
        if (a(bArr)) {
            return b;
        }
        byte[] bArr2 = new byte[bArr.length << 3];
        int length = bArr2.length - 1;
        int i = 0;
        while (i < bArr.length) {
            for (int i2 = 0; i2 < c.length; i2++) {
                if ((bArr[i] & c[i2]) == 0) {
                    bArr2[length - i2] = 48;
                } else {
                    bArr2[length - i2] = 49;
                }
            }
            i++;
            length -= 8;
        }
        return bArr2;
    }

    public static char[] toAsciiChars(byte[] bArr) {
        if (a(bArr)) {
            return a;
        }
        char[] cArr = new char[bArr.length << 3];
        int length = cArr.length - 1;
        int i = 0;
        while (i < bArr.length) {
            for (int i2 = 0; i2 < c.length; i2++) {
                if ((bArr[i] & c[i2]) == 0) {
                    cArr[length - i2] = '0';
                } else {
                    cArr[length - i2] = '1';
                }
            }
            i++;
            length -= 8;
        }
        return cArr;
    }

    public static String toAsciiString(byte[] bArr) {
        return new String(toAsciiChars(bArr));
    }

    @Override // com.mappn.sdk.common.codec.Decoder
    public Object decode(Object obj) {
        if (obj == null) {
            return b;
        }
        if (obj instanceof byte[]) {
            return fromAscii((byte[]) obj);
        }
        if (obj instanceof char[]) {
            return fromAscii((char[]) obj);
        }
        if (obj instanceof String) {
            return fromAscii(((String) obj).toCharArray());
        }
        throw new DecoderException("argument not a byte array");
    }

    @Override // com.mappn.sdk.common.codec.BinaryDecoder
    public byte[] decode(byte[] bArr) {
        return fromAscii(bArr);
    }

    @Override // com.mappn.sdk.common.codec.Encoder
    public Object encode(Object obj) {
        if (obj instanceof byte[]) {
            return toAsciiChars((byte[]) obj);
        }
        throw new EncoderException("argument not a byte array");
    }

    @Override // com.mappn.sdk.common.codec.BinaryEncoder
    public byte[] encode(byte[] bArr) {
        return toAsciiBytes(bArr);
    }

    public byte[] toByteArray(String str) {
        return str == null ? b : fromAscii(str.toCharArray());
    }
}
