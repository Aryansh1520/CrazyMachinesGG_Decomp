package com.mappn.sdk.common.codec.binary;

import java.io.FilterInputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class Base64InputStream extends FilterInputStream {
    private final boolean a;
    private final Base64 b;
    private final byte[] c;

    public Base64InputStream(InputStream inputStream) {
        this(inputStream, false);
    }

    public Base64InputStream(InputStream inputStream, boolean z) {
        super(inputStream);
        this.c = new byte[1];
        this.a = z;
        this.b = new Base64();
    }

    public Base64InputStream(InputStream inputStream, boolean z, int i, byte[] bArr) {
        super(inputStream);
        this.c = new byte[1];
        this.a = z;
        this.b = new Base64(i, bArr);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() {
        int read = read(this.c, 0, 1);
        while (read == 0) {
            read = read(this.c, 0, 1);
        }
        if (read > 0) {
            return this.c[0] < 0 ? this.c[0] + 256 : this.c[0];
        }
        return -1;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i < 0 || i2 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i > bArr.length || i + i2 > bArr.length) {
            throw new IndexOutOfBoundsException();
        }
        if (i2 == 0) {
            return 0;
        }
        if (!this.b.a()) {
            byte[] bArr2 = new byte[this.a ? 4096 : 8192];
            int read = this.in.read(bArr2);
            if (read > 0 && bArr.length == i2) {
                this.b.b(bArr, i, i2);
            }
            if (this.a) {
                this.b.c(bArr2, 0, read);
            } else {
                this.b.d(bArr2, 0, read);
            }
        }
        return this.b.a(bArr, i, i2);
    }
}
