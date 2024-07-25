package com.mappn.sdk.common.utils;

import java.io.ByteArrayOutputStream;
import java.util.Random;
import org.bson.BSON;

/* loaded from: classes.dex */
public class Crypter {
    private static Random k = new Random();
    private byte[] a;
    private byte[] b;
    private byte[] c;
    private int d;
    private int e;
    private int f;
    private int g;
    private byte[] h;
    private int j;
    private boolean i = true;
    private ByteArrayOutputStream l = new ByteArrayOutputStream(8);

    private static long a(byte[] bArr, int i, int i2) {
        long j = 0;
        int i3 = i + 4;
        while (i < i3) {
            j = (j << 8) | (bArr[i] & BSON.MINKEY);
            i++;
        }
        return (j >>> 32) | (4294967295L & j);
    }

    private void a() {
        this.f = 0;
        while (this.f < 8) {
            if (this.i) {
                byte[] bArr = this.a;
                int i = this.f;
                bArr[i] = (byte) (bArr[i] ^ this.b[this.f]);
            } else {
                byte[] bArr2 = this.a;
                int i2 = this.f;
                bArr2[i2] = (byte) (bArr2[i2] ^ this.c[this.e + this.f]);
            }
            this.f++;
        }
        byte[] bArr3 = this.a;
        int i3 = 16;
        long a = a(bArr3, 0, 4);
        long a2 = a(bArr3, 4, 4);
        long a3 = a(this.h, 0, 4);
        long a4 = a(this.h, 4, 4);
        long a5 = a(this.h, 8, 4);
        long a6 = a(this.h, 12, 4);
        long j = 0;
        while (true) {
            int i4 = i3 - 1;
            if (i3 <= 0) {
                break;
            }
            j = (j + 2654435769L) & 4294967295L;
            a = (a + ((((a2 << 4) + a3) ^ (a2 + j)) ^ ((a2 >>> 5) + a4))) & 4294967295L;
            a2 = (a2 + ((((a << 4) + a5) ^ (a + j)) ^ ((a >>> 5) + a6))) & 4294967295L;
            i3 = i4;
        }
        this.l.reset();
        a((int) a);
        a((int) a2);
        System.arraycopy(this.l.toByteArray(), 0, this.c, this.d, 8);
        this.f = 0;
        while (this.f < 8) {
            byte[] bArr4 = this.c;
            int i5 = this.d + this.f;
            bArr4[i5] = (byte) (bArr4[i5] ^ this.b[this.f]);
            this.f++;
        }
        System.arraycopy(this.a, 0, this.b, 0, 8);
        this.e = this.d;
        this.d += 8;
        this.f = 0;
        this.i = false;
    }

    private void a(int i) {
        this.l.write(i >>> 24);
        this.l.write(i >>> 16);
        this.l.write(i >>> 8);
        this.l.write(i);
    }

    private byte[] a(byte[] bArr, int i) {
        int i2 = 16;
        long a = a(bArr, i, 4);
        long a2 = a(bArr, i + 4, 4);
        long a3 = a(this.h, 0, 4);
        long a4 = a(this.h, 4, 4);
        long a5 = a(this.h, 8, 4);
        long a6 = a(this.h, 12, 4);
        long j = 3816266640L;
        while (true) {
            int i3 = i2 - 1;
            if (i2 <= 0) {
                this.l.reset();
                a((int) a);
                a((int) a2);
                return this.l.toByteArray();
            }
            a2 = (a2 - ((((a << 4) + a5) ^ (a + j)) ^ ((a >>> 5) + a6))) & 4294967295L;
            a = (a - ((((a2 << 4) + a3) ^ (a2 + j)) ^ ((a2 >>> 5) + a4))) & 4294967295L;
            j = (j - 2654435769L) & 4294967295L;
            i2 = i3;
        }
    }

    private boolean b(byte[] bArr, int i, int i2) {
        this.f = 0;
        while (this.f < 8) {
            if (this.j + this.f >= i2) {
                return true;
            }
            byte[] bArr2 = this.b;
            int i3 = this.f;
            bArr2[i3] = (byte) (bArr2[i3] ^ bArr[(this.d + i) + this.f]);
            this.f++;
        }
        this.b = a(this.b, 0);
        if (this.b == null) {
            return false;
        }
        this.j += 8;
        this.d += 8;
        this.f = 0;
        return true;
    }

    public byte[] decrypt(byte[] bArr, int i, int i2, byte[] bArr2) {
        if (bArr2 == null) {
            return null;
        }
        this.e = 0;
        this.d = 0;
        this.h = bArr2;
        byte[] bArr3 = new byte[i + 8];
        if (i2 % 8 != 0 || i2 < 16) {
            return null;
        }
        this.b = a(bArr, i);
        this.f = this.b[0] & 7;
        int i3 = (i2 - this.f) - 10;
        if (i3 < 0) {
            return null;
        }
        for (int i4 = i; i4 < bArr3.length; i4++) {
            bArr3[i4] = 0;
        }
        this.c = new byte[i3];
        this.e = 0;
        this.d = 8;
        this.j = 8;
        this.f++;
        this.g = 1;
        byte[] bArr4 = bArr3;
        while (this.g <= 2) {
            if (this.f < 8) {
                this.f++;
                this.g++;
            }
            if (this.f == 8) {
                if (!b(bArr, i, i2)) {
                    return null;
                }
                bArr4 = bArr;
            }
        }
        int i5 = i3;
        byte[] bArr5 = bArr4;
        int i6 = 0;
        byte[] bArr6 = bArr5;
        while (i5 != 0) {
            if (this.f < 8) {
                this.c[i6] = (byte) (bArr6[(this.e + i) + this.f] ^ this.b[this.f]);
                i6++;
                i5--;
                this.f++;
            }
            if (this.f == 8) {
                this.e = this.d - 8;
                if (!b(bArr, i, i2)) {
                    return null;
                }
                bArr6 = bArr;
            }
        }
        this.g = 1;
        byte[] bArr7 = bArr6;
        while (this.g < 8) {
            if (this.f < 8) {
                if ((bArr7[(this.e + i) + this.f] ^ this.b[this.f]) != 0) {
                    return null;
                }
                this.f++;
            }
            if (this.f == 8) {
                this.e = this.d;
                if (!b(bArr, i, i2)) {
                    return null;
                }
                bArr7 = bArr;
            }
            this.g++;
        }
        return this.c;
    }

    public byte[] decrypt(byte[] bArr, byte[] bArr2) {
        return decrypt(bArr, 0, bArr.length, bArr2);
    }

    public byte[] encrypt(byte[] bArr, int i, int i2, byte[] bArr2) {
        int i3;
        int i4;
        if (bArr2 == null) {
            return bArr;
        }
        this.a = new byte[8];
        this.b = new byte[8];
        this.f = 1;
        this.g = 0;
        this.e = 0;
        this.d = 0;
        this.h = bArr2;
        this.i = true;
        this.f = (i2 + 10) % 8;
        if (this.f != 0) {
            this.f = 8 - this.f;
        }
        this.c = new byte[this.f + i2 + 10];
        this.a[0] = (byte) ((k.nextInt() & 248) | this.f);
        for (int i5 = 1; i5 <= this.f; i5++) {
            this.a[i5] = (byte) k.nextInt();
        }
        this.f++;
        for (int i6 = 0; i6 < 8; i6++) {
            this.b[i6] = 0;
        }
        this.g = 1;
        while (this.g <= 2) {
            if (this.f < 8) {
                byte[] bArr3 = this.a;
                int i7 = this.f;
                this.f = i7 + 1;
                bArr3[i7] = (byte) k.nextInt();
                this.g++;
            }
            if (this.f == 8) {
                a();
            }
        }
        int i8 = i;
        int i9 = i2;
        while (i9 > 0) {
            if (this.f < 8) {
                byte[] bArr4 = this.a;
                int i10 = this.f;
                this.f = i10 + 1;
                i3 = i8 + 1;
                bArr4[i10] = bArr[i8];
                i4 = i9 - 1;
            } else {
                i3 = i8;
                i4 = i9;
            }
            if (this.f == 8) {
                a();
                i9 = i4;
                i8 = i3;
            } else {
                i9 = i4;
                i8 = i3;
            }
        }
        this.g = 1;
        while (this.g <= 7) {
            if (this.f < 8) {
                byte[] bArr5 = this.a;
                int i11 = this.f;
                this.f = i11 + 1;
                bArr5[i11] = 0;
                this.g++;
            }
            if (this.f == 8) {
                a();
            }
        }
        return this.c;
    }

    public byte[] encrypt(byte[] bArr, byte[] bArr2) {
        return encrypt(bArr, 0, bArr.length, bArr2);
    }
}
