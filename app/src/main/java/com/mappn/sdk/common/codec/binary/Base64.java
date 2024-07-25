package com.mappn.sdk.common.codec.binary;

import com.mappn.sdk.common.codec.BinaryDecoder;
import com.mappn.sdk.common.codec.BinaryEncoder;
import com.mappn.sdk.common.codec.DecoderException;
import com.mappn.sdk.common.codec.EncoderException;
import java.math.BigInteger;
import org.bson.BSON;

/* loaded from: classes.dex */
public class Base64 implements BinaryDecoder, BinaryEncoder {
    private static byte[] a = {BSON.CODE, 10};
    private static final byte[] b = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] c = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] d = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, BSON.REGEX, BSON.REF, BSON.CODE, BSON.SYMBOL, BSON.CODE_W_SCOPE, BSON.NUMBER_INT, BSON.TIMESTAMP, BSON.NUMBER_LONG, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    private final byte[] e;
    private final int f;
    private final byte[] g;
    private final int h;
    private final int i;
    private byte[] j;
    private int k;
    private int l;
    private int m;
    private int n;
    private boolean o;
    private int p;

    public Base64() {
        this(false);
    }

    public Base64(int i) {
        this(i, a);
    }

    public Base64(int i, byte[] bArr) {
        this(i, bArr, false);
    }

    public Base64(int i, byte[] bArr, boolean z) {
        if (bArr == null) {
            bArr = a;
            i = 0;
        }
        this.f = i > 0 ? (i / 4) << 2 : 0;
        this.g = new byte[bArr.length];
        System.arraycopy(bArr, 0, this.g, 0, bArr.length);
        if (i > 0) {
            this.i = bArr.length + 4;
        } else {
            this.i = 4;
        }
        this.h = this.i - 1;
        if (a(bArr)) {
            throw new IllegalArgumentException("lineSeperator must not contain base64 characters: [" + StringUtils.newStringUtf8(bArr) + "]");
        }
        this.e = z ? c : b;
    }

    public Base64(boolean z) {
        this(76, a, z);
    }

    private static long a(byte[] bArr, int i, byte[] bArr2) {
        int i2 = (i / 4) << 2;
        long length = (bArr.length << 2) / 3;
        long j = length % 4;
        if (j != 0) {
            length += 4 - j;
        }
        if (i2 > 0) {
            boolean z = length % ((long) i2) == 0;
            length += (length / i2) * bArr2.length;
            if (!z) {
                return length + bArr2.length;
            }
        }
        return length;
    }

    private static boolean a(byte[] bArr) {
        for (byte b2 : bArr) {
            if (isBase64(b2)) {
                return true;
            }
        }
        return false;
    }

    private void c() {
        if (this.j == null) {
            this.j = new byte[8192];
            this.k = 0;
            this.l = 0;
        } else {
            byte[] bArr = new byte[this.j.length << 1];
            System.arraycopy(this.j, 0, bArr, 0, this.j.length);
            this.j = bArr;
        }
    }

    private void d() {
        this.j = null;
        this.k = 0;
        this.l = 0;
        this.m = 0;
        this.n = 0;
        this.o = false;
    }

    public static byte[] decodeBase64(String str) {
        return new Base64().decode(str);
    }

    public static byte[] decodeBase64(byte[] bArr) {
        return new Base64().decode(bArr);
    }

    public static BigInteger decodeInteger(byte[] bArr) {
        return new BigInteger(1, decodeBase64(bArr));
    }

    public static byte[] encodeBase64(byte[] bArr) {
        return encodeBase64(bArr, false);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z) {
        return encodeBase64(bArr, z, false);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z, boolean z2) {
        return encodeBase64(bArr, z, z2, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] bArr, boolean z, boolean z2, int i) {
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        long a2 = a(bArr, 76, a);
        if (a2 > i) {
            throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + a2 + ") than the specified maxium size of " + i);
        }
        return (z ? new Base64(z2) : new Base64(0, a, z2)).encode(bArr);
    }

    public static byte[] encodeBase64Chunked(byte[] bArr) {
        return encodeBase64(bArr, true);
    }

    public static String encodeBase64String(byte[] bArr) {
        return StringUtils.newStringUtf8(encodeBase64(bArr, true));
    }

    public static byte[] encodeBase64URLSafe(byte[] bArr) {
        return encodeBase64(bArr, false, true);
    }

    public static String encodeBase64URLSafeString(byte[] bArr) {
        return StringUtils.newStringUtf8(encodeBase64(bArr, false, true));
    }

    public static byte[] encodeInteger(BigInteger bigInteger) {
        int i;
        byte[] bArr;
        if (bigInteger == null) {
            throw new NullPointerException("encodeInteger called with null parameter");
        }
        int bitLength = ((bigInteger.bitLength() + 7) >> 3) << 3;
        byte[] byteArray = bigInteger.toByteArray();
        if (bigInteger.bitLength() % 8 == 0 || (bigInteger.bitLength() / 8) + 1 != bitLength / 8) {
            int length = byteArray.length;
            if (bigInteger.bitLength() % 8 == 0) {
                i = 1;
                length--;
            } else {
                i = 0;
            }
            int i2 = (bitLength / 8) - length;
            byte[] bArr2 = new byte[bitLength / 8];
            System.arraycopy(byteArray, i, bArr2, i2, length);
            bArr = bArr2;
        } else {
            bArr = byteArray;
        }
        return encodeBase64(bArr, false);
    }

    public static boolean isArrayByteBase64(byte[] bArr) {
        boolean z;
        for (int i = 0; i < bArr.length; i++) {
            if (!isBase64(bArr[i])) {
                switch (bArr[i]) {
                    case 9:
                    case 10:
                    case 13:
                    case 32:
                        z = true;
                        break;
                    default:
                        z = false;
                        break;
                }
                if (!z) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isBase64(byte b2) {
        return b2 == 61 || (b2 >= 0 && b2 < d.length && d[b2] != -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int a(byte[] bArr, int i, int i2) {
        if (this.j == null) {
            return this.o ? -1 : 0;
        }
        int min = Math.min(b(), i2);
        if (this.j != bArr) {
            System.arraycopy(this.j, this.l, bArr, i, min);
            this.l += min;
            if (this.l < this.k) {
                return min;
            }
        }
        this.j = null;
        return min;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean a() {
        return this.j != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int b() {
        if (this.j != null) {
            return this.k - this.l;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void b(byte[] bArr, int i, int i2) {
        if (bArr == null || bArr.length != i2) {
            return;
        }
        this.j = bArr;
        this.k = i;
        this.l = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void c(byte[] bArr, int i, int i2) {
        if (this.o) {
            return;
        }
        if (i2 >= 0) {
            int i3 = 0;
            while (i3 < i2) {
                if (this.j == null || this.j.length - this.k < this.i) {
                    c();
                }
                int i4 = this.n + 1;
                this.n = i4;
                this.n = i4 % 3;
                int i5 = i + 1;
                int i6 = bArr[i];
                if (i6 < 0) {
                    i6 += 256;
                }
                this.p = i6 + (this.p << 8);
                if (this.n == 0) {
                    byte[] bArr2 = this.j;
                    int i7 = this.k;
                    this.k = i7 + 1;
                    bArr2[i7] = this.e[(this.p >> 18) & 63];
                    byte[] bArr3 = this.j;
                    int i8 = this.k;
                    this.k = i8 + 1;
                    bArr3[i8] = this.e[(this.p >> 12) & 63];
                    byte[] bArr4 = this.j;
                    int i9 = this.k;
                    this.k = i9 + 1;
                    bArr4[i9] = this.e[(this.p >> 6) & 63];
                    byte[] bArr5 = this.j;
                    int i10 = this.k;
                    this.k = i10 + 1;
                    bArr5[i10] = this.e[this.p & 63];
                    this.m += 4;
                    if (this.f > 0 && this.f <= this.m) {
                        System.arraycopy(this.g, 0, this.j, this.k, this.g.length);
                        this.k += this.g.length;
                        this.m = 0;
                    }
                }
                i3++;
                i = i5;
            }
            return;
        }
        this.o = true;
        if (this.j == null || this.j.length - this.k < this.i) {
            c();
        }
        switch (this.n) {
            case 1:
                byte[] bArr6 = this.j;
                int i11 = this.k;
                this.k = i11 + 1;
                bArr6[i11] = this.e[(this.p >> 2) & 63];
                byte[] bArr7 = this.j;
                int i12 = this.k;
                this.k = i12 + 1;
                bArr7[i12] = this.e[(this.p << 4) & 63];
                if (this.e == b) {
                    byte[] bArr8 = this.j;
                    int i13 = this.k;
                    this.k = i13 + 1;
                    bArr8[i13] = 61;
                    byte[] bArr9 = this.j;
                    int i14 = this.k;
                    this.k = i14 + 1;
                    bArr9[i14] = 61;
                    break;
                }
                break;
            case 2:
                byte[] bArr10 = this.j;
                int i15 = this.k;
                this.k = i15 + 1;
                bArr10[i15] = this.e[(this.p >> 10) & 63];
                byte[] bArr11 = this.j;
                int i16 = this.k;
                this.k = i16 + 1;
                bArr11[i16] = this.e[(this.p >> 4) & 63];
                byte[] bArr12 = this.j;
                int i17 = this.k;
                this.k = i17 + 1;
                bArr12[i17] = this.e[(this.p << 2) & 63];
                if (this.e == b) {
                    byte[] bArr13 = this.j;
                    int i18 = this.k;
                    this.k = i18 + 1;
                    bArr13[i18] = 61;
                    break;
                }
                break;
        }
        if (this.f <= 0 || this.k <= 0) {
            return;
        }
        System.arraycopy(this.g, 0, this.j, this.k, this.g.length);
        this.k += this.g.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void d(byte[] bArr, int i, int i2) {
        byte b2;
        if (this.o) {
            return;
        }
        if (i2 < 0) {
            this.o = true;
        }
        int i3 = 0;
        while (true) {
            if (i3 >= i2) {
                break;
            }
            if (this.j == null || this.j.length - this.k < this.h) {
                c();
            }
            int i4 = i + 1;
            byte b3 = bArr[i];
            if (b3 == 61) {
                this.o = true;
                break;
            }
            if (b3 >= 0 && b3 < d.length && (b2 = d[b3]) >= 0) {
                int i5 = this.n + 1;
                this.n = i5;
                this.n = i5 % 4;
                this.p = b2 + (this.p << 6);
                if (this.n == 0) {
                    byte[] bArr2 = this.j;
                    int i6 = this.k;
                    this.k = i6 + 1;
                    bArr2[i6] = (byte) (this.p >> 16);
                    byte[] bArr3 = this.j;
                    int i7 = this.k;
                    this.k = i7 + 1;
                    bArr3[i7] = (byte) (this.p >> 8);
                    byte[] bArr4 = this.j;
                    int i8 = this.k;
                    this.k = i8 + 1;
                    bArr4[i8] = (byte) this.p;
                }
            }
            i3++;
            i = i4;
        }
        if (!this.o || this.n == 0) {
            return;
        }
        this.p <<= 6;
        switch (this.n) {
            case 2:
                this.p <<= 6;
                byte[] bArr5 = this.j;
                int i9 = this.k;
                this.k = i9 + 1;
                bArr5[i9] = (byte) (this.p >> 16);
                return;
            case 3:
                byte[] bArr6 = this.j;
                int i10 = this.k;
                this.k = i10 + 1;
                bArr6[i10] = (byte) (this.p >> 16);
                byte[] bArr7 = this.j;
                int i11 = this.k;
                this.k = i11 + 1;
                bArr7[i11] = (byte) (this.p >> 8);
                return;
            default:
                return;
        }
    }

    @Override // com.mappn.sdk.common.codec.Decoder
    public Object decode(Object obj) {
        if (obj instanceof byte[]) {
            return decode((byte[]) obj);
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }
        throw new DecoderException("Parameter supplied to Base64 decode is not a byte[] or a String");
    }

    public byte[] decode(String str) {
        return decode(StringUtils.getBytesUtf8(str));
    }

    @Override // com.mappn.sdk.common.codec.BinaryDecoder
    public byte[] decode(byte[] bArr) {
        d();
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        byte[] bArr2 = new byte[(bArr.length * 3) / 4];
        b(bArr2, 0, bArr2.length);
        d(bArr, 0, bArr.length);
        d(bArr, 0, -1);
        byte[] bArr3 = new byte[this.k];
        a(bArr3, 0, bArr3.length);
        return bArr3;
    }

    @Override // com.mappn.sdk.common.codec.Encoder
    public Object encode(Object obj) {
        if (obj instanceof byte[]) {
            return encode((byte[]) obj);
        }
        throw new EncoderException("Parameter supplied to Base64 encode is not a byte[]");
    }

    @Override // com.mappn.sdk.common.codec.BinaryEncoder
    public byte[] encode(byte[] bArr) {
        d();
        if (bArr == null || bArr.length == 0) {
            return bArr;
        }
        byte[] bArr2 = new byte[(int) a(bArr, this.f, this.g)];
        b(bArr2, 0, bArr2.length);
        c(bArr, 0, bArr.length);
        c(bArr, 0, -1);
        if (this.j != bArr2) {
            a(bArr2, 0, bArr2.length);
        }
        if (!isUrlSafe() || this.k >= bArr2.length) {
            return bArr2;
        }
        byte[] bArr3 = new byte[this.k];
        System.arraycopy(bArr2, 0, bArr3, 0, this.k);
        return bArr3;
    }

    public String encodeToString(byte[] bArr) {
        return StringUtils.newStringUtf8(encode(bArr));
    }

    public boolean isUrlSafe() {
        return this.e == c;
    }
}
