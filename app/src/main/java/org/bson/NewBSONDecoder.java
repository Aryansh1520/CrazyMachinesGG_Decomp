package org.bson;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.bson.io.Bits;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class NewBSONDecoder implements BSONDecoder {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int MAX_STRING = 33554432;
    private BSONCallback _callback;
    private byte[] _data;
    private int _length;
    private int _pos = 0;

    @Override // org.bson.BSONDecoder
    public BSONObject readObject(byte[] pData) {
        this._length = pData.length;
        BasicBSONCallback c = new BasicBSONCallback();
        decode(pData, c);
        return (BSONObject) c.get();
    }

    @Override // org.bson.BSONDecoder
    public BSONObject readObject(InputStream pIn) throws IOException {
        this._length = Bits.readInt(pIn);
        if (this._data == null || this._data.length < this._length) {
            this._data = new byte[this._length];
        }
        new DataInputStream(pIn).readFully(this._data, 4, this._length - 4);
        return readObject(this._data);
    }

    @Override // org.bson.BSONDecoder
    public int decode(byte[] pData, BSONCallback pCallback) {
        this._data = pData;
        this._pos = 4;
        this._callback = pCallback;
        _decode();
        return this._length;
    }

    @Override // org.bson.BSONDecoder
    public int decode(InputStream pIn, BSONCallback pCallback) throws IOException {
        this._length = Bits.readInt(pIn);
        if (this._data == null || this._data.length < this._length) {
            this._data = new byte[this._length];
        }
        new DataInputStream(pIn).readFully(this._data, 4, this._length - 4);
        return decode(this._data, pCallback);
    }

    private final void _decode() {
        this._callback.objectStart();
        do {
        } while (decodeElement());
        this._callback.objectDone();
    }

    private final String readCstr() {
        int length = 0;
        int offset = this._pos;
        while (true) {
            byte[] bArr = this._data;
            int i = this._pos;
            this._pos = i + 1;
            if (bArr[i] == 0) {
                try {
                    return new String(this._data, offset, length, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return new String(this._data, offset, length);
                }
            }
            length++;
        }
    }

    private final String readUtf8Str() {
        int length = Bits.readInt(this._data, this._pos);
        this._pos += 4;
        if (length <= 0 || length > MAX_STRING) {
            throw new BSONException("String invalid - corruption");
        }
        try {
            String str = new String(this._data, this._pos, length - 1, "UTF-8");
            this._pos += length;
            return str;
        } catch (UnsupportedEncodingException uee) {
            throw new BSONException("What is in the db", uee);
        }
    }

    private final Object _readBasicObject() {
        this._pos += 4;
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

    private final void _binary(String pName) {
        int totalLen = Bits.readInt(this._data, this._pos);
        this._pos += 4;
        byte bType = this._data[this._pos];
        this._pos++;
        switch (bType) {
            case 0:
                byte[] data = new byte[totalLen];
                System.arraycopy(this._data, this._pos, data, 0, totalLen);
                this._pos += totalLen;
                this._callback.gotBinary(pName, bType, data);
                return;
            case 1:
            default:
                byte[] data2 = new byte[totalLen];
                System.arraycopy(this._data, this._pos, data2, 0, totalLen);
                this._pos += totalLen;
                this._callback.gotBinary(pName, bType, data2);
                return;
            case 2:
                int len = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                if (len + 4 != totalLen) {
                    throw new IllegalArgumentException("bad data size subtype 2 len: " + len + " totalLen: " + totalLen);
                }
                byte[] data3 = new byte[len];
                System.arraycopy(this._data, this._pos, data3, 0, len);
                this._pos += len;
                this._callback.gotBinary(pName, bType, data3);
                return;
            case 3:
                if (totalLen != 16) {
                    throw new IllegalArgumentException("bad data size subtype 3 len: " + totalLen + " != 16");
                }
                long part1 = Bits.readLong(this._data, this._pos);
                this._pos += 8;
                long part2 = Bits.readLong(this._data, this._pos);
                this._pos += 8;
                this._callback.gotUUID(pName, part1, part2);
                return;
        }
    }

    private final boolean decodeElement() {
        byte type = this._data[this._pos];
        this._pos++;
        if (type == 0) {
            return false;
        }
        String name = readCstr();
        switch (type) {
            case -1:
                this._callback.gotMinKey(name);
                return true;
            case 1:
                this._callback.gotDouble(name, Double.longBitsToDouble(Bits.readLong(this._data, this._pos)));
                this._pos += 8;
                return true;
            case 2:
                this._callback.gotString(name, readUtf8Str());
                return true;
            case 3:
                this._pos += 4;
                this._callback.objectStart(name);
                do {
                } while (decodeElement());
                this._callback.objectDone();
                return true;
            case 4:
                this._pos += 4;
                this._callback.arrayStart(name);
                do {
                } while (decodeElement());
                this._callback.arrayDone();
                return true;
            case 5:
                _binary(name);
                return true;
            case 6:
                this._callback.gotUndefined(name);
                return true;
            case 7:
                int p1 = Bits.readIntBE(this._data, this._pos);
                this._pos += 4;
                int p2 = Bits.readIntBE(this._data, this._pos);
                this._pos += 4;
                int p3 = Bits.readIntBE(this._data, this._pos);
                this._pos += 4;
                this._callback.gotObjectId(name, new ObjectId(p1, p2, p3));
                return true;
            case 8:
                this._callback.gotBoolean(name, this._data[this._pos] > 0);
                this._pos++;
                return true;
            case 9:
                this._callback.gotDate(name, Bits.readLong(this._data, this._pos));
                this._pos += 8;
                return true;
            case 10:
                this._callback.gotNull(name);
                return true;
            case 11:
                this._callback.gotRegex(name, readCstr(), readCstr());
                return true;
            case 12:
                this._pos += 4;
                String ns = readCstr();
                int p12 = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                int p22 = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                int p32 = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                this._callback.gotDBRef(name, ns, new ObjectId(p12, p22, p32));
                return true;
            case 13:
                this._callback.gotCode(name, readUtf8Str());
                return true;
            case 14:
                this._callback.gotSymbol(name, readUtf8Str());
                return true;
            case 15:
                this._pos += 4;
                this._callback.gotCodeWScope(name, readUtf8Str(), _readBasicObject());
                return true;
            case 16:
                this._callback.gotInt(name, Bits.readInt(this._data, this._pos));
                this._pos += 4;
                return true;
            case 17:
                int i = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                int time = Bits.readInt(this._data, this._pos);
                this._pos += 4;
                this._callback.gotTimestamp(name, time, i);
                return true;
            case 18:
                this._callback.gotLong(name, Bits.readLong(this._data, this._pos));
                this._pos += 8;
                return true;
            case Byte.MAX_VALUE:
                this._callback.gotMaxKey(name);
                return true;
            default:
                throw new UnsupportedOperationException("BSONDecoder doesn't understand type : " + ((int) type) + " name: " + name);
        }
    }
}
