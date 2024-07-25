package com.google.ads.util;

import com.mappn.sdk.common.codec.CharEncoding;
import java.io.UnsupportedEncodingException;
import org.bson.BSON;

/* loaded from: classes.dex */
public class c {
    static final /* synthetic */ boolean a;

    /* loaded from: classes.dex */
    public static abstract class a {
        public byte[] a;
        public int b;
    }

    static {
        a = !c.class.desiredAssertionStatus();
    }

    public static byte[] a(String str) {
        return a(str.getBytes(), 0);
    }

    public static byte[] a(byte[] bArr, int i) {
        return a(bArr, 0, bArr.length, i);
    }

    public static byte[] a(byte[] bArr, int i, int i2, int i3) {
        b bVar = new b(i3, new byte[(i2 * 3) / 4]);
        if (!bVar.a(bArr, i, i2, true)) {
            throw new IllegalArgumentException("bad base-64");
        }
        if (bVar.b == bVar.a.length) {
            return bVar.a;
        }
        byte[] bArr2 = new byte[bVar.b];
        System.arraycopy(bVar.a, 0, bArr2, 0, bVar.b);
        return bArr2;
    }

    /* loaded from: classes.dex */
    public static class b extends a {
        private static final int[] c = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private static final int[] d = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int e;
        private int f;
        private final int[] g;

        public b(int i, byte[] bArr) {
            this.a = bArr;
            this.g = (i & 8) == 0 ? c : d;
            this.e = 0;
            this.f = 0;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x005d, code lost:
        
            if (r14 != false) goto L57;
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x005f, code lost:
        
            r10.e = r3;
            r10.f = r2;
            r10.b = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:?, code lost:
        
            return true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0105, code lost:
        
            switch(r3) {
                case 0: goto L58;
                case 1: goto L59;
                case 2: goto L60;
                case 3: goto L61;
                case 4: goto L62;
                default: goto L58;
            };
         */
        /* JADX WARN: Code restructure failed: missing block: B:21:0x0108, code lost:
        
            r10.e = r3;
            r10.b = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:?, code lost:
        
            return true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x010f, code lost:
        
            r10.e = 6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:?, code lost:
        
            return false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:25:0x0115, code lost:
        
            r5[r0] = (byte) (r2 >> 4);
            r0 = r0 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x011e, code lost:
        
            r1 = r0 + 1;
            r5[r0] = (byte) (r2 >> 10);
            r0 = r1 + 1;
            r5[r1] = (byte) (r2 >> 2);
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x012d, code lost:
        
            r10.e = 6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:28:?, code lost:
        
            return false;
         */
        /* JADX WARN: Failed to find 'out' block for switch in B:31:0x006f. Please report as an issue. */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean a(byte[] r11, int r12, int r13, boolean r14) {
            /*
                Method dump skipped, instructions count: 340
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.ads.util.c.b.a(byte[], int, int, boolean):boolean");
        }
    }

    public static String b(byte[] bArr, int i) {
        try {
            return new String(c(bArr, i), CharEncoding.US_ASCII);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] c(byte[] bArr, int i) {
        return b(bArr, 0, bArr.length, i);
    }

    public static byte[] b(byte[] bArr, int i, int i2, int i3) {
        C0004c c0004c = new C0004c(i3, null);
        int i4 = (i2 / 3) * 4;
        if (c0004c.d) {
            if (i2 % 3 > 0) {
                i4 += 4;
            }
        } else {
            switch (i2 % 3) {
                case 1:
                    i4 += 2;
                    break;
                case 2:
                    i4 += 3;
                    break;
            }
        }
        if (c0004c.e && i2 > 0) {
            i4 += (c0004c.f ? 2 : 1) * (((i2 - 1) / 57) + 1);
        }
        c0004c.a = new byte[i4];
        c0004c.a(bArr, i, i2, true);
        if (a || c0004c.b == i4) {
            return c0004c.a;
        }
        throw new AssertionError();
    }

    /* renamed from: com.google.ads.util.c$c, reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0004c extends a {
        static final /* synthetic */ boolean g;
        private static final byte[] h;
        private static final byte[] i;
        public int c;
        public final boolean d;
        public final boolean e;
        public final boolean f;
        private final byte[] j;
        private int k;
        private final byte[] l;

        static {
            g = !c.class.desiredAssertionStatus();
            h = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
            i = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
        }

        public C0004c(int i2, byte[] bArr) {
            this.a = bArr;
            this.d = (i2 & 1) == 0;
            this.e = (i2 & 2) == 0;
            this.f = (i2 & 4) != 0;
            this.l = (i2 & 8) == 0 ? h : i;
            this.j = new byte[2];
            this.c = 0;
            this.k = this.e ? 19 : -1;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public boolean a(byte[] bArr, int i2, int i3, boolean z) {
            int i4;
            int i5;
            byte b;
            int i6;
            byte b2;
            int i7;
            byte b3;
            int i8;
            int i9;
            int i10;
            int i11;
            byte[] bArr2 = this.l;
            byte[] bArr3 = this.a;
            int i12 = 0;
            int i13 = this.k;
            int i14 = i3 + i2;
            int i15 = -1;
            switch (this.c) {
                case 0:
                    i4 = i2;
                    break;
                case 1:
                    if (i2 + 2 <= i14) {
                        int i16 = i2 + 1;
                        i15 = ((this.j[0] & BSON.MINKEY) << 16) | ((bArr[i2] & BSON.MINKEY) << 8) | (bArr[i16] & BSON.MINKEY);
                        this.c = 0;
                        i4 = i16 + 1;
                        break;
                    }
                    i4 = i2;
                    break;
                case 2:
                    if (i2 + 1 <= i14) {
                        i4 = i2 + 1;
                        i15 = ((this.j[0] & BSON.MINKEY) << 16) | ((this.j[1] & BSON.MINKEY) << 8) | (bArr[i2] & BSON.MINKEY);
                        this.c = 0;
                        break;
                    }
                    i4 = i2;
                    break;
                default:
                    i4 = i2;
                    break;
            }
            if (i15 != -1) {
                bArr3[0] = bArr2[(i15 >> 18) & 63];
                bArr3[1] = bArr2[(i15 >> 12) & 63];
                bArr3[2] = bArr2[(i15 >> 6) & 63];
                i12 = 4;
                bArr3[3] = bArr2[i15 & 63];
                i13--;
                if (i13 == 0) {
                    if (!this.f) {
                        i11 = 4;
                    } else {
                        i11 = 5;
                        bArr3[4] = BSON.CODE;
                    }
                    i12 = i11 + 1;
                    bArr3[i11] = 10;
                    i13 = 19;
                }
            }
            while (true) {
                int i17 = i13;
                int i18 = i12;
                if (i4 + 3 <= i14) {
                    int i19 = ((bArr[i4] & BSON.MINKEY) << 16) | ((bArr[i4 + 1] & BSON.MINKEY) << 8) | (bArr[i4 + 2] & BSON.MINKEY);
                    bArr3[i18] = bArr2[(i19 >> 18) & 63];
                    bArr3[i18 + 1] = bArr2[(i19 >> 12) & 63];
                    bArr3[i18 + 2] = bArr2[(i19 >> 6) & 63];
                    bArr3[i18 + 3] = bArr2[i19 & 63];
                    i4 += 3;
                    i12 = i18 + 4;
                    i13 = i17 - 1;
                    if (i13 == 0) {
                        if (this.f) {
                            i10 = i12 + 1;
                            bArr3[i12] = BSON.CODE;
                        } else {
                            i10 = i12;
                        }
                        i12 = i10 + 1;
                        bArr3[i10] = 10;
                        i13 = 19;
                    }
                } else {
                    if (z) {
                        if (i4 - this.c == i14 - 1) {
                            if (this.c > 0) {
                                i9 = 1;
                                b3 = this.j[0];
                                i8 = i4;
                            } else {
                                b3 = bArr[i4];
                                i8 = i4 + 1;
                                i9 = 0;
                            }
                            int i20 = (b3 & BSON.MINKEY) << 4;
                            this.c -= i9;
                            int i21 = i18 + 1;
                            bArr3[i18] = bArr2[(i20 >> 6) & 63];
                            int i22 = i21 + 1;
                            bArr3[i21] = bArr2[i20 & 63];
                            if (this.d) {
                                int i23 = i22 + 1;
                                bArr3[i22] = 61;
                                i22 = i23 + 1;
                                bArr3[i23] = 61;
                            }
                            if (this.e) {
                                if (this.f) {
                                    bArr3[i22] = BSON.CODE;
                                    i22++;
                                }
                                bArr3[i22] = 10;
                                i22++;
                            }
                            i4 = i8;
                            i18 = i22;
                        } else if (i4 - this.c == i14 - 2) {
                            if (this.c > 1) {
                                i6 = 1;
                                b = this.j[0];
                            } else {
                                b = bArr[i4];
                                i4++;
                                i6 = 0;
                            }
                            int i24 = (b & BSON.MINKEY) << 10;
                            if (this.c > 0) {
                                b2 = this.j[i6];
                                i6++;
                            } else {
                                b2 = bArr[i4];
                                i4++;
                            }
                            int i25 = ((b2 & BSON.MINKEY) << 2) | i24;
                            this.c -= i6;
                            int i26 = i18 + 1;
                            bArr3[i18] = bArr2[(i25 >> 12) & 63];
                            int i27 = i26 + 1;
                            bArr3[i26] = bArr2[(i25 >> 6) & 63];
                            int i28 = i27 + 1;
                            bArr3[i27] = bArr2[i25 & 63];
                            if (this.d) {
                                i7 = i28 + 1;
                                bArr3[i28] = 61;
                            } else {
                                i7 = i28;
                            }
                            if (this.e) {
                                if (this.f) {
                                    bArr3[i7] = BSON.CODE;
                                    i7++;
                                }
                                bArr3[i7] = 10;
                                i7++;
                            }
                            i18 = i7;
                        } else if (this.e && i18 > 0 && i17 != 19) {
                            if (this.f) {
                                i5 = i18 + 1;
                                bArr3[i18] = BSON.CODE;
                            } else {
                                i5 = i18;
                            }
                            i18 = i5 + 1;
                            bArr3[i5] = 10;
                        }
                        if (!g && this.c != 0) {
                            throw new AssertionError();
                        }
                        if (!g && i4 != i14) {
                            throw new AssertionError();
                        }
                    } else if (i4 == i14 - 1) {
                        byte[] bArr4 = this.j;
                        int i29 = this.c;
                        this.c = i29 + 1;
                        bArr4[i29] = bArr[i4];
                    } else if (i4 == i14 - 2) {
                        byte[] bArr5 = this.j;
                        int i30 = this.c;
                        this.c = i30 + 1;
                        bArr5[i30] = bArr[i4];
                        byte[] bArr6 = this.j;
                        int i31 = this.c;
                        this.c = i31 + 1;
                        bArr6[i31] = bArr[i4 + 1];
                    }
                    this.b = i18;
                    this.k = i17;
                    return true;
                }
            }
        }
    }

    private c() {
    }
}
