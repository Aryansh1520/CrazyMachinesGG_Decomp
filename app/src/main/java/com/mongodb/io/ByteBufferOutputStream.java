package com.mongodb.io;

import com.mongodb.io.ByteBufferFactory;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ByteBufferOutputStream extends OutputStream {
    static final ByteBufferFactory _defaultFactory = new ByteBufferFactory.SimpleHeapByteBufferFactory(4096);
    final ByteBufferFactory _factory;
    final List<ByteBuffer> _lst;

    public ByteBufferOutputStream() {
        this(_defaultFactory);
    }

    public ByteBufferOutputStream(int size) {
        this(new ByteBufferFactory.SimpleHeapByteBufferFactory(size));
    }

    public ByteBufferOutputStream(ByteBufferFactory factory) {
        this._lst = new ArrayList();
        this._factory = factory;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() {
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) {
        ByteBuffer cur = _need(1);
        int toWrite = Math.min(len, cur.remaining());
        cur.put(b, off, toWrite);
        if (toWrite != len) {
            write(b, off + toWrite, len - toWrite);
        }
    }

    @Override // java.io.OutputStream
    public void write(int b) {
        _need(1).put((byte) b);
    }

    public List<ByteBuffer> getBuffers() {
        return this._lst;
    }

    public List<ByteBuffer> getBuffers(boolean flip) {
        if (flip) {
            for (ByteBuffer buf : this._lst) {
                buf.flip();
            }
        }
        return this._lst;
    }

    private ByteBuffer _need(int space) {
        if (this._lst.size() == 0) {
            this._lst.add(this._factory.get());
            return this._lst.get(0);
        }
        ByteBuffer cur = this._lst.get(this._lst.size() - 1);
        if (space <= cur.remaining()) {
            return cur;
        }
        this._lst.add(this._factory.get());
        return this._lst.get(this._lst.size() - 1);
    }
}
