package org.bson;

import com.mokredit.payment.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.bson.io.Bits;
import org.bson.io.PoolOutputBuffer;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class BasicBSONDecoder implements BSONDecoder {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int MAX_STRING = 33554432;
    static final String[] ONE_BYTE_STRINGS = new String[128];
    protected BSONCallback _callback;
    protected BSONInput _in;
    protected int _len;
    protected int _pos;
    private byte[] _random = new byte[1024];
    private byte[] _inputBuffer = new byte[1024];
    private PoolOutputBuffer _stringBuffer = new PoolOutputBuffer();

    @Override // org.bson.BSONDecoder
    public BSONObject readObject(byte[] b) {
        try {
            return readObject(new ByteArrayInputStream(b));
        } catch (IOException ioe) {
            throw new BSONException("should be impossible", ioe);
        }
    }

    @Override // org.bson.BSONDecoder
    public BSONObject readObject(InputStream in) throws IOException {
        BasicBSONCallback c = new BasicBSONCallback();
        decode(in, c);
        return (BSONObject) c.get();
    }

    @Override // org.bson.BSONDecoder
    public int decode(byte[] b, BSONCallback callback) {
        try {
            return _decode(new BSONInput(new ByteArrayInputStream(b)), callback);
        } catch (IOException ioe) {
            throw new BSONException("should be impossible", ioe);
        }
    }

    @Override // org.bson.BSONDecoder
    public int decode(InputStream in, BSONCallback callback) throws IOException {
        return _decode(new BSONInput(in), callback);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int _decode(BSONInput bSONInput, BSONCallback bSONCallback) throws IOException {
        if (this._in != null || this._callback != null) {
            throw new IllegalStateException("not ready");
        }
        this._in = bSONInput;
        this._callback = bSONCallback;
        if (bSONInput.numRead() != 0) {
            throw new IllegalArgumentException("i'm confused");
        }
        try {
            int readInt = this._in.readInt();
            this._in.setMax(readInt);
            this._callback.objectStart();
            do {
            } while (decodeElement());
            this._callback.objectDone();
            if (this._in.numRead() != readInt) {
                throw new IllegalArgumentException("bad data.  lengths don't match read:" + this._in.numRead() + " != len:" + readInt);
            }
            return readInt;
        } finally {
            this._in = null;
            this._callback = null;
        }
    }

    int decode(boolean first) throws IOException {
        int start = this._in.numRead();
        int len = this._in.readInt();
        if (first) {
            this._in.setMax(len);
        }
        this._callback.objectStart();
        do {
        } while (decodeElement());
        this._callback.objectDone();
        int read = this._in.numRead() - start;
        if (read != len) {
        }
        return len;
    }

    boolean decodeElement() throws IOException {
        byte type = this._in.read();
        if (type == 0) {
            return false;
        }
        String name = this._in.readCStr();
        switch (type) {
            case -1:
                this._callback.gotMinKey(name);
                break;
            case 1:
                this._callback.gotDouble(name, this._in.readDouble());
                break;
            case 2:
                this._callback.gotString(name, this._in.readUTF8String());
                break;
            case 3:
                this._in.readInt();
                this._callback.objectStart(name);
                do {
                } while (decodeElement());
                this._callback.objectDone();
                break;
            case 4:
                this._in.readInt();
                this._callback.arrayStart(name);
                do {
                } while (decodeElement());
                this._callback.arrayDone();
                break;
            case 5:
                _binary(name);
                break;
            case 6:
                this._callback.gotUndefined(name);
                break;
            case 7:
                this._callback.gotObjectId(name, new ObjectId(this._in.readIntBE(), this._in.readIntBE(), this._in.readIntBE()));
                break;
            case 8:
                this._callback.gotBoolean(name, this._in.read() > 0);
                break;
            case 9:
                this._callback.gotDate(name, this._in.readLong());
                break;
            case 10:
                this._callback.gotNull(name);
                break;
            case 11:
                this._callback.gotRegex(name, this._in.readCStr(), this._in.readCStr());
                break;
            case 12:
                this._in.readInt();
                String ns = this._in.readCStr();
                ObjectId theOID = new ObjectId(this._in.readInt(), this._in.readInt(), this._in.readInt());
                this._callback.gotDBRef(name, ns, theOID);
                break;
            case 13:
                this._callback.gotCode(name, this._in.readUTF8String());
                break;
            case 14:
                this._callback.gotSymbol(name, this._in.readUTF8String());
                break;
            case 15:
                this._in.readInt();
                this._callback.gotCodeWScope(name, this._in.readUTF8String(), _readBasicObject());
                break;
            case 16:
                this._callback.gotInt(name, this._in.readInt());
                break;
            case 17:
                int i = this._in.readInt();
                int time = this._in.readInt();
                this._callback.gotTimestamp(name, time, i);
                break;
            case 18:
                this._callback.gotLong(name, this._in.readLong());
                break;
            case Byte.MAX_VALUE:
                this._callback.gotMaxKey(name);
                break;
            default:
                throw new UnsupportedOperationException("BSONDecoder doesn't understand type : " + ((int) type) + " name: " + name);
        }
        return true;
    }

    protected void _binary(String name) throws IOException {
        int totalLen = this._in.readInt();
        byte bType = this._in.read();
        switch (bType) {
            case 0:
                byte[] data = new byte[totalLen];
                this._in.fill(data);
                this._callback.gotBinary(name, bType, data);
                return;
            case 1:
            default:
                byte[] data2 = new byte[totalLen];
                this._in.fill(data2);
                this._callback.gotBinary(name, bType, data2);
                return;
            case 2:
                int len = this._in.readInt();
                if (len + 4 != totalLen) {
                    throw new IllegalArgumentException("bad data size subtype 2 len: " + len + " totalLen: " + totalLen);
                }
                byte[] data3 = new byte[len];
                this._in.fill(data3);
                this._callback.gotBinary(name, bType, data3);
                return;
            case 3:
                if (totalLen != 16) {
                    throw new IllegalArgumentException("bad data size subtype 3 len: " + totalLen + " != 16");
                }
                long part1 = this._in.readLong();
                long part2 = this._in.readLong();
                this._callback.gotUUID(name, part1, part2);
                return;
        }
    }

    Object _readBasicObject() throws IOException {
        this._in.readInt();
        BSONCallback save = this._callback;
        BSONCallback _basic = this._callback.createBSONCallback();
        this._callback = _basic;
        _basic.reset();
        _basic.objectStart(false);
        do {
        } while (decodeElement());
        this._callback = save;
        return _basic.get();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class BSONInput {
        final InputStream _raw;
        int _max = 4;
        int _read = 0;

        public BSONInput(InputStream in) {
            this._raw = in;
            BasicBSONDecoder.this._pos = 0;
            BasicBSONDecoder.this._len = 0;
        }

        protected int _need(int num) throws IOException {
            if (BasicBSONDecoder.this._len - BasicBSONDecoder.this._pos < num) {
                if (num >= BasicBSONDecoder.this._inputBuffer.length) {
                    throw new IllegalArgumentException("you can't need that much");
                }
                int remaining = BasicBSONDecoder.this._len - BasicBSONDecoder.this._pos;
                if (BasicBSONDecoder.this._pos > 0) {
                    System.arraycopy(BasicBSONDecoder.this._inputBuffer, BasicBSONDecoder.this._pos, BasicBSONDecoder.this._inputBuffer, 0, remaining);
                    BasicBSONDecoder.this._pos = 0;
                    BasicBSONDecoder.this._len = remaining;
                }
                int maxToRead = Math.min((this._max - this._read) - remaining, BasicBSONDecoder.this._inputBuffer.length - BasicBSONDecoder.this._len);
                while (maxToRead > 0) {
                    int x = this._raw.read(BasicBSONDecoder.this._inputBuffer, BasicBSONDecoder.this._len, maxToRead);
                    if (x <= 0) {
                        throw new IOException("unexpected EOF");
                    }
                    maxToRead -= x;
                    BasicBSONDecoder.this._len += x;
                }
                int ret = BasicBSONDecoder.this._pos;
                BasicBSONDecoder.this._pos += num;
                this._read += num;
                return ret;
            }
            int ret2 = BasicBSONDecoder.this._pos;
            BasicBSONDecoder.this._pos += num;
            this._read += num;
            return ret2;
        }

        public int readInt() throws IOException {
            return Bits.readInt(BasicBSONDecoder.this._inputBuffer, _need(4));
        }

        public int readIntBE() throws IOException {
            return Bits.readIntBE(BasicBSONDecoder.this._inputBuffer, _need(4));
        }

        public long readLong() throws IOException {
            return Bits.readLong(BasicBSONDecoder.this._inputBuffer, _need(8));
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }

        public byte read() throws IOException {
            if (BasicBSONDecoder.this._pos >= BasicBSONDecoder.this._len) {
                return BasicBSONDecoder.this._inputBuffer[_need(1)];
            }
            this._read++;
            byte[] bArr = BasicBSONDecoder.this._inputBuffer;
            BasicBSONDecoder basicBSONDecoder = BasicBSONDecoder.this;
            int i = basicBSONDecoder._pos;
            basicBSONDecoder._pos = i + 1;
            return bArr[i];
        }

        public void fill(byte[] b) throws IOException {
            fill(b, b.length);
        }

        public void fill(byte[] b, int len) throws IOException {
            int have = BasicBSONDecoder.this._len - BasicBSONDecoder.this._pos;
            int tocopy = Math.min(len, have);
            System.arraycopy(BasicBSONDecoder.this._inputBuffer, BasicBSONDecoder.this._pos, b, 0, tocopy);
            BasicBSONDecoder.this._pos += tocopy;
            this._read += tocopy;
            int len2 = len - tocopy;
            int off = tocopy;
            while (len2 > 0) {
                int x = this._raw.read(b, off, len2);
                if (x <= 0) {
                    throw new IOException("unexpected EOF");
                }
                this._read += x;
                off += x;
                len2 -= x;
            }
        }

        protected boolean _isAscii(byte b) {
            return b >= 0 && b <= Byte.MAX_VALUE;
        }

        public String readCStr() throws IOException {
            String out;
            BasicBSONDecoder.this._random[0] = read();
            if (BasicBSONDecoder.this._random[0] != 0) {
                BasicBSONDecoder.this._random[1] = read();
                if (BasicBSONDecoder.this._random[1] == 0) {
                    String out2 = BasicBSONDecoder.ONE_BYTE_STRINGS[BasicBSONDecoder.this._random[0]];
                    if (out2 == null) {
                        return new String(BasicBSONDecoder.this._random, 0, 1, "UTF-8");
                    }
                    return out2;
                }
                BasicBSONDecoder.this._stringBuffer.reset();
                BasicBSONDecoder.this._stringBuffer.write(BasicBSONDecoder.this._random, 0, 2);
                boolean isAscii = _isAscii(BasicBSONDecoder.this._random[0]) && _isAscii(BasicBSONDecoder.this._random[1]);
                while (true) {
                    byte b = read();
                    if (b == 0) {
                        break;
                    }
                    BasicBSONDecoder.this._stringBuffer.write(b);
                    isAscii = isAscii && _isAscii(b);
                }
                if (isAscii) {
                    out = BasicBSONDecoder.this._stringBuffer.asAscii();
                } else {
                    try {
                        out = BasicBSONDecoder.this._stringBuffer.asString("UTF-8");
                    } catch (UnsupportedOperationException e) {
                        throw new BSONException("impossible", e);
                    }
                }
                BasicBSONDecoder.this._stringBuffer.reset();
                return out;
            }
            return StringUtils.EMPTY;
        }

        public String readUTF8String() throws IOException {
            byte[] b;
            int size = readInt();
            if (size > 0 && size <= BasicBSONDecoder.MAX_STRING) {
                if (size >= BasicBSONDecoder.this._inputBuffer.length / 2) {
                    if (size < BasicBSONDecoder.this._random.length) {
                        b = BasicBSONDecoder.this._random;
                    } else {
                        b = new byte[size];
                    }
                    fill(b, size);
                    try {
                        return new String(b, 0, size - 1, "UTF-8");
                    } catch (UnsupportedEncodingException uee) {
                        throw new BSONException("impossible", uee);
                    }
                }
                if (size == 1) {
                    read();
                    return StringUtils.EMPTY;
                }
                return new String(BasicBSONDecoder.this._inputBuffer, _need(size), size - 1, "UTF-8");
            }
            throw new BSONException("bad string size: " + size);
        }

        public int numRead() {
            return this._read;
        }

        public int getPos() {
            return BasicBSONDecoder.this._pos;
        }

        public int getMax() {
            return this._max;
        }

        public void setMax(int _max) {
            this._max = _max;
        }
    }

    private static final boolean _isAscii(byte b) {
        return b >= 0 && b <= Byte.MAX_VALUE;
    }

    static {
        _fillRange((byte) 48, (byte) 57);
        _fillRange((byte) 97, (byte) 122);
        _fillRange((byte) 65, (byte) 90);
    }

    static void _fillRange(byte min, byte max) {
        while (min < max) {
            String s = StringUtils.EMPTY + ((char) min);
            ONE_BYTE_STRINGS[min] = s;
            min = (byte) (min + 1);
        }
    }
}
