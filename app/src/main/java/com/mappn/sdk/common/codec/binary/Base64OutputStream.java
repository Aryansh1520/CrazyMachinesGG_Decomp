package com.mappn.sdk.common.codec.binary;

import java.io.FilterOutputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class Base64OutputStream extends FilterOutputStream {
    private final boolean a;
    private final Base64 b;
    private final byte[] c;

    public Base64OutputStream(OutputStream outputStream) {
        this(outputStream, true);
    }

    public Base64OutputStream(OutputStream outputStream, boolean z) {
        super(outputStream);
        this.c = new byte[1];
        this.a = z;
        this.b = new Base64();
    }

    public Base64OutputStream(OutputStream outputStream, boolean z, int i, byte[] bArr) {
        super(outputStream);
        this.c = new byte[1];
        this.a = z;
        this.b = new Base64(i, bArr);
    }

    private void a(boolean z) {
        byte[] bArr;
        int a;
        int b = this.b.b();
        if (b > 0 && (a = this.b.a((bArr = new byte[b]), 0, b)) > 0) {
            this.out.write(bArr, 0, a);
        }
        if (z) {
            this.out.flush();
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.a) {
            this.b.c(this.c, 0, -1);
        } else {
            this.b.d(this.c, 0, -1);
        }
        flush();
        this.out.close();
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() {
        a(true);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) {
        this.c[0] = (byte) i;
        write(this.c, 0, 1);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        if (i < 0 || i2 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i > bArr.length || i + i2 > bArr.length) {
            throw new IndexOutOfBoundsException();
        }
        if (i2 > 0) {
            if (this.a) {
                this.b.c(bArr, i, i2);
            } else {
                this.b.d(bArr, i, i2);
            }
            a(false);
        }
    }
}
