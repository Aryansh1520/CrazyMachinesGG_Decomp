package org.bson.io;

import android.support.v4.view.MotionEventCompat;
import com.mappn.sdk.pay.util.Constants;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.bson.util.SimplePool;

/* loaded from: classes.dex */
public class PoolOutputBuffer extends OutputBuffer {
    public static final int BUF_SIZE = 16384;
    private static final String DEFAULT_ENCODING_1 = "UTF-8";
    private static final String DEFAULT_ENCODING_2 = "UTF8";
    private static SimplePool<byte[]> _extra = new SimplePool<byte[]>(640) { // from class: org.bson.io.PoolOutputBuffer.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.bson.util.SimplePool
        public byte[] createNew() {
            return new byte[16384];
        }
    };
    final byte[] _mine = new byte[16384];
    final char[] _chars = new char[16384];
    final List<byte[]> _fromPool = new ArrayList();
    final UTF8Encoding _encoding = new UTF8Encoding();
    private final Position _cur = new Position();
    private final Position _end = new Position();

    public PoolOutputBuffer() {
        reset();
    }

    public void reset() {
        this._cur.reset();
        this._end.reset();
        for (int i = 0; i < this._fromPool.size(); i++) {
            _extra.done(this._fromPool.get(i));
        }
        this._fromPool.clear();
    }

    @Override // org.bson.io.OutputBuffer
    public int getPosition() {
        return this._cur.pos();
    }

    @Override // org.bson.io.OutputBuffer
    public void setPosition(int position) {
        this._cur.reset(position);
    }

    @Override // org.bson.io.OutputBuffer
    public void seekEnd() {
        this._cur.reset(this._end);
    }

    @Override // org.bson.io.OutputBuffer
    public void seekStart() {
        this._cur.reset();
    }

    @Override // org.bson.io.OutputBuffer
    public int size() {
        return this._end.pos();
    }

    @Override // org.bson.io.OutputBuffer, java.io.OutputStream
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    @Override // org.bson.io.OutputBuffer, java.io.OutputStream
    public void write(byte[] b, int off, int len) {
        while (len > 0) {
            byte[] bs = _cur();
            int space = Math.min(bs.length - this._cur.y, len);
            System.arraycopy(b, off, bs, this._cur.y, space);
            this._cur.inc(space);
            len -= space;
            off += space;
            _afterWrite();
        }
    }

    @Override // org.bson.io.OutputBuffer, java.io.OutputStream
    public void write(int b) {
        byte[] bs = _cur();
        bs[this._cur.getAndInc()] = (byte) (b & MotionEventCompat.ACTION_MASK);
        _afterWrite();
    }

    void _afterWrite() {
        if (this._cur.pos() < this._end.pos()) {
            if (this._cur.y == 16384) {
                this._cur.nextBuffer();
            }
        } else {
            this._end.reset(this._cur);
            if (this._end.y >= 16384) {
                this._fromPool.add(_extra.get());
                this._end.nextBuffer();
                this._cur.reset(this._end);
            }
        }
    }

    byte[] _cur() {
        return _get(this._cur.x);
    }

    byte[] _get(int z) {
        return z < 0 ? this._mine : this._fromPool.get(z);
    }

    @Override // org.bson.io.OutputBuffer
    public int pipe(OutputStream out) throws IOException {
        if (out == null) {
            throw new NullPointerException("out is null");
        }
        int total = 0;
        for (int i = -1; i < this._fromPool.size(); i++) {
            byte[] b = _get(i);
            int amt = this._end.len(i);
            out.write(b, 0, amt);
            total += amt;
        }
        return total;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Position {
        int x;
        int y;

        Position() {
            reset();
        }

        void reset() {
            this.x = -1;
            this.y = 0;
        }

        void reset(Position other) {
            this.x = other.x;
            this.y = other.y;
        }

        void reset(int pos) {
            this.x = (pos / 16384) - 1;
            this.y = pos % 16384;
        }

        int pos() {
            return ((this.x + 1) * 16384) + this.y;
        }

        int getAndInc() {
            int i = this.y;
            this.y = i + 1;
            return i;
        }

        void inc(int amt) {
            this.y += amt;
            if (this.y > 16384) {
                throw new IllegalArgumentException("something is wrong");
            }
        }

        void nextBuffer() {
            if (this.y != 16384) {
                throw new IllegalArgumentException("broken");
            }
            this.x++;
            this.y = 0;
        }

        int len(int which) {
            if (which < this.x) {
                return 16384;
            }
            return this.y;
        }

        public String toString() {
            return this.x + Constants.TERM + this.y;
        }
    }

    public String asAscii() {
        if (this._fromPool.size() > 0) {
            return super.asString();
        }
        int m = size();
        char[] c = m < this._chars.length ? this._chars : new char[m];
        for (int i = 0; i < m; i++) {
            c[i] = (char) this._mine[i];
        }
        return new String(c, 0, m);
    }

    @Override // org.bson.io.OutputBuffer
    public String asString(String encoding) throws UnsupportedEncodingException {
        if (this._fromPool.size() > 0) {
            return super.asString(encoding);
        }
        if (encoding.equals("UTF-8") || encoding.equals(DEFAULT_ENCODING_2)) {
            try {
                return this._encoding.decode(this._mine, 0, size());
            } catch (IOException e) {
            }
        }
        return new String(this._mine, 0, size(), encoding);
    }
}
