package com.mappn.sdk.common.codec.binary;

import com.mappn.sdk.common.codec.BinaryDecoder;
import com.mappn.sdk.common.codec.BinaryEncoder;
import com.mappn.sdk.common.codec.DecoderException;
import com.mappn.sdk.common.codec.EncoderException;
import java.io.UnsupportedEncodingException;
import org.bson.BSON;

/* loaded from: classes.dex */
public class Hex implements BinaryDecoder, BinaryEncoder {
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private final String c;

    public Hex() {
        this.c = "UTF-8";
    }

    public Hex(String str) {
        this.c = str;
    }

    public static byte[] decodeHex(char[] cArr) {
        int i = 0;
        int length = cArr.length;
        if ((length & 1) != 0) {
            throw new DecoderException("Odd number of characters.");
        }
        byte[] bArr = new byte[length >> 1];
        int i2 = 0;
        while (i < length) {
            int digit = toDigit(cArr[i], i) << 4;
            int i3 = i + 1;
            int digit2 = digit | toDigit(cArr[i3], i3);
            i = i3 + 1;
            bArr[i2] = (byte) digit2;
            i2++;
        }
        return bArr;
    }

    public static char[] encodeHex(byte[] bArr) {
        return encodeHex(bArr, true);
    }

    public static char[] encodeHex(byte[] bArr, boolean z) {
        return encodeHex(bArr, z ? a : b);
    }

    protected static char[] encodeHex(byte[] bArr, char[] cArr) {
        int i = 0;
        int length = bArr.length;
        char[] cArr2 = new char[length << 1];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr2[i] = cArr[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr2[i3] = cArr[bArr[i2] & BSON.CODE_W_SCOPE];
        }
        return cArr2;
    }

    public static String encodeHexString(byte[] bArr) {
        return new String(encodeHex(bArr));
    }

    protected static int toDigit(char c, int i) {
        int digit = Character.digit(c, 16);
        if (digit == -1) {
            throw new DecoderException("Illegal hexadecimal charcter " + c + " at index " + i);
        }
        return digit;
    }

    @Override // com.mappn.sdk.common.codec.Decoder
    public Object decode(Object obj) {
        try {
            return decodeHex(obj instanceof String ? ((String) obj).toCharArray() : (char[]) obj);
        } catch (ClassCastException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    @Override // com.mappn.sdk.common.codec.BinaryDecoder
    public byte[] decode(byte[] bArr) {
        try {
            return decodeHex(new String(bArr, getCharsetName()).toCharArray());
        } catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    @Override // com.mappn.sdk.common.codec.Encoder
    public Object encode(Object obj) {
        try {
            return encodeHex(obj instanceof String ? ((String) obj).getBytes(getCharsetName()) : (byte[]) obj);
        } catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage(), e);
        } catch (ClassCastException e2) {
            throw new EncoderException(e2.getMessage(), e2);
        }
    }

    @Override // com.mappn.sdk.common.codec.BinaryEncoder
    public byte[] encode(byte[] bArr) {
        return StringUtils.getBytesUnchecked(encodeHexString(bArr), getCharsetName());
    }

    public String getCharsetName() {
        return this.c;
    }

    public final String toString() {
        return super.toString() + "[charsetName=" + this.c + "]";
    }
}
