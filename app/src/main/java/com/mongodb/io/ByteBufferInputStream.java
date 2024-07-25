package com.mongodb.io;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import org.bson.BSON;

/* loaded from: classes.dex */
public class ByteBufferInputStream extends InputStream {
    final List<ByteBuffer> _lst;
    private int _pos;

    public ByteBufferInputStream(List<ByteBuffer> lst) {
        this(lst, false);
    }

    public ByteBufferInputStream(List<ByteBuffer> lst, boolean flip) {
        this._pos = 0;
        this._lst = lst;
        if (flip) {
            for (ByteBuffer buf : this._lst) {
                buf.flip();
            }
        }
    }

    @Override // java.io.InputStream
    public int available() {
        int avail = 0;
        for (int i = this._pos; i < this._lst.size(); i++) {
            avail += this._lst.get(i).remaining();
        }
        return avail;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // java.io.InputStream
    public void mark(int readlimit) {
        throw new RuntimeException("mark not supported");
    }

    @Override // java.io.InputStream
    public void reset() {
        throw new RuntimeException("mark not supported");
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public int read() {
        if (this._pos >= this._lst.size()) {
            return -1;
        }
        ByteBuffer buf = this._lst.get(this._pos);
        if (buf.remaining() > 0) {
            return buf.get() & BSON.MINKEY;
        }
        this._pos++;
        return read();
    }

    @Override // java.io.InputStream
    public int read(byte[] b) {
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) {
        if (this._pos >= this._lst.size()) {
            return -1;
        }
        ByteBuffer buf = this._lst.get(this._pos);
        if (buf.remaining() == 0) {
            this._pos++;
            return read(b, off, len);
        }
        int toRead = Math.min(len, buf.remaining());
        buf.get(b, off, toRead);
        if (toRead != len && this._pos + 1 < this._lst.size()) {
            this._pos++;
            return toRead + read(b, off + toRead, len - toRead);
        }
        return toRead;
    }

    @Override // java.io.InputStream
    public long skip(long n) {
        long skipped = 0;
        while (n >= 0 && this._pos < this._lst.size()) {
            ByteBuffer b = this._lst.get(this._pos);
            if (b.remaining() < n) {
                skipped += b.remaining();
                n -= b.remaining();
                b.position(b.limit());
                this._pos++;
            } else {
                long skipped2 = skipped + n;
                b.position((int) (b.position() + n));
                return skipped2;
            }
        }
        return skipped;
    }
}
