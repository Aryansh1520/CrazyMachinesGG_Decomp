package com.mongodb.io;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class ByteBufferHolder {
    static final int _bufSize = 4096;
    List<ByteBuffer> _buffers;
    final int _max;
    int _pos;

    public ByteBufferHolder() {
        this(1073741824);
    }

    public ByteBufferHolder(int max) {
        this._buffers = new ArrayList();
        this._pos = 0;
        this._max = max;
    }

    public byte get(int i) {
        if (i >= this._pos) {
            throw new RuntimeException("out of bounds");
        }
        int num = i / 4096;
        int pos = i % 4096;
        return this._buffers.get(num).get(pos);
    }

    public void get(int pos, byte[] b) {
        for (int i = 0; i < b.length; i++) {
            b[i] = get(i + pos);
        }
    }

    public void put(int i, byte val) {
        if (i >= this._pos) {
            throw new RuntimeException("out of bounds");
        }
        int num = i / 4096;
        int pos = i % 4096;
        this._buffers.get(num).put(pos, val);
    }

    public int position() {
        return this._pos;
    }

    public void position(int p) {
        this._pos = p;
        int num = this._pos / 4096;
        int pos = this._pos % 4096;
        while (this._buffers.size() <= num) {
            _addBucket();
        }
        ByteBuffer bb = this._buffers.get(num);
        bb.position(pos);
        for (int i = num + 1; i < this._buffers.size(); i++) {
            this._buffers.get(i).position(0);
        }
    }

    public int remaining() {
        return Integer.MAX_VALUE;
    }

    public void put(ByteBuffer in) {
        while (in.hasRemaining()) {
            int num = this._pos / 4096;
            if (num >= this._buffers.size()) {
                _addBucket();
            }
            ByteBuffer bb = this._buffers.get(num);
            int canRead = Math.min(bb.remaining(), in.remaining());
            int oldLimit = in.limit();
            in.limit(in.position() + canRead);
            bb.put(in);
            in.limit(oldLimit);
            this._pos += canRead;
        }
    }

    private void _addBucket() {
        if (capacity() + 4096 > this._max) {
            throw new RuntimeException("too big current:" + capacity());
        }
        this._buffers.add(ByteBuffer.allocateDirect(4096));
    }

    public int capacity() {
        return this._buffers.size() * 4096;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("{ ByteBufferHolder pos:" + this._pos + " ");
        for (ByteBuffer bb : this._buffers) {
            buf.append(bb).append(" ");
        }
        return buf.append("}").toString();
    }
}
