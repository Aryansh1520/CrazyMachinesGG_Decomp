package com.mappn.sdk.statitistics.msgpack;

import com.mappn.sdk.statitistics.entity.GfanPayMessagePackable;
import com.mokredit.payment.StringUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class GfanPayPacker {
    protected byte[] castBytes = new byte[9];
    protected OutputStream out;

    public GfanPayPacker(OutputStream outputStream) {
        this.out = outputStream;
    }

    public static int getPackSize(double d) {
        return 9;
    }

    public static int getPackSize(float f) {
        return 5;
    }

    public static int getPackSize(int i) {
        if (i < 16) {
            return 1;
        }
        return i < 65536 ? 3 : 5;
    }

    public static int getPackSize(long j) {
        if (j < -32) {
            return j < -32768 ? j < -2147483648L ? 9 : 5 : j < -128 ? 3 : 2;
        }
        if (j < 128) {
            return 1;
        }
        return j < 65536 ? j < 256 ? 2 : 3 : j < 4294967296L ? 5 : 9;
    }

    public static int getPackSize(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            return bytes.length + getPackSize(bytes.length);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getPackSize(boolean z) {
        return 1;
    }

    public static int getPackSize(byte[] bArr) {
        if (bArr == null) {
            return 1;
        }
        return getPackSize(bArr.length) + bArr.length;
    }

    public GfanPayPacker pack(byte b) {
        return packByte(b);
    }

    public GfanPayPacker pack(double d) {
        return packDouble(d);
    }

    public GfanPayPacker pack(float f) {
        return packFloat(f);
    }

    public GfanPayPacker pack(int i) {
        return packInt(i);
    }

    public GfanPayPacker pack(long j) {
        return packLong(j);
    }

    public GfanPayPacker pack(GfanPayMessagePackable gfanPayMessagePackable) {
        if (gfanPayMessagePackable == null) {
            return packNil();
        }
        gfanPayMessagePackable.messagePack(this);
        return this;
    }

    public GfanPayPacker pack(Boolean bool) {
        return bool == null ? packNil() : bool.booleanValue() ? packTrue() : packFalse();
    }

    public GfanPayPacker pack(Byte b) {
        return b == null ? packNil() : packByte(b.byteValue());
    }

    public GfanPayPacker pack(Double d) {
        return d == null ? packNil() : packDouble(d.doubleValue());
    }

    public GfanPayPacker pack(Float f) {
        return f == null ? packNil() : packFloat(f.floatValue());
    }

    public GfanPayPacker pack(Integer num) {
        return num == null ? packNil() : packInt(num.intValue());
    }

    public GfanPayPacker pack(Long l) {
        return l == null ? packNil() : packLong(l.longValue());
    }

    public GfanPayPacker pack(Short sh) {
        return sh == null ? packNil() : packShort(sh.shortValue());
    }

    public GfanPayPacker pack(String str) {
        return str == null ? packString(StringUtils.EMPTY) : packString(str);
    }

    public GfanPayPacker pack(BigInteger bigInteger) {
        return bigInteger == null ? packNil() : packBigInteger(bigInteger);
    }

    public GfanPayPacker pack(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            return packNil();
        }
        packRaw(byteBuffer.remaining());
        return packRawBody(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
    }

    public GfanPayPacker pack(List list) {
        if (list == null) {
            return packNil();
        }
        packArray(list.size());
        Iterator it = list.iterator();
        while (it.hasNext()) {
            pack((String) it.next());
        }
        return this;
    }

    public GfanPayPacker pack(Map map) {
        if (map == null) {
            return packNil();
        }
        HashMap hashMap = new HashMap();
        for (String str : map.keySet()) {
            Object obj = map.get(str);
            if (obj instanceof String) {
                hashMap.put(str, obj.toString());
            } else if (obj instanceof Number) {
                hashMap.put(str, Double.valueOf(((Number) obj).doubleValue()));
            }
        }
        packMap(hashMap.size());
        for (Map.Entry entry : hashMap.entrySet()) {
            pack((String) entry.getKey());
            Object value = entry.getValue();
            if (value instanceof Number) {
                pack(((Number) value).doubleValue());
            } else if (value instanceof String) {
                pack(value.toString());
            }
        }
        return this;
    }

    public GfanPayPacker pack(short s) {
        return packShort(s);
    }

    public GfanPayPacker pack(boolean z) {
        return z ? packTrue() : packFalse();
    }

    public GfanPayPacker pack(byte[] bArr) {
        if (bArr == null) {
            return packNil();
        }
        packRaw(bArr.length);
        return packRawBody(bArr);
    }

    public GfanPayPacker packArray(int i) {
        if (i < 16) {
            this.out.write((byte) (i | 144));
        } else if (i < 65536) {
            this.castBytes[0] = -36;
            this.castBytes[1] = (byte) (i >> 8);
            this.castBytes[2] = (byte) i;
            this.out.write(this.castBytes, 0, 3);
        } else {
            this.castBytes[0] = -35;
            this.castBytes[1] = i >> 24;
            this.castBytes[2] = (byte) (i >> 16);
            this.castBytes[3] = (byte) (i >> 8);
            this.castBytes[4] = (byte) i;
            this.out.write(this.castBytes, 0, 5);
        }
        return this;
    }

    public GfanPayPacker packBigInteger(BigInteger bigInteger) {
        if (bigInteger.bitLength() <= 63) {
            return packLong(bigInteger.longValue());
        }
        if (bigInteger.bitLength() > 64 || bigInteger.signum() < 0) {
            throw new IOException("can't pack BigInteger larger than 0xffffffffffffffff");
        }
        this.castBytes[0] = -49;
        byte[] byteArray = bigInteger.toByteArray();
        this.castBytes[1] = byteArray[byteArray.length - 8];
        this.castBytes[2] = byteArray[byteArray.length - 7];
        this.castBytes[3] = byteArray[byteArray.length - 6];
        this.castBytes[4] = byteArray[byteArray.length - 5];
        this.castBytes[5] = byteArray[byteArray.length - 4];
        this.castBytes[6] = byteArray[byteArray.length - 3];
        this.castBytes[7] = byteArray[byteArray.length - 2];
        this.castBytes[8] = byteArray[byteArray.length - 1];
        this.out.write(this.castBytes, 0, 9);
        return this;
    }

    public GfanPayPacker packBoolean(boolean z) {
        return z ? packTrue() : packFalse();
    }

    public GfanPayPacker packByte(byte b) {
        if (b < -32) {
            this.castBytes[0] = -48;
            this.castBytes[1] = b;
            this.out.write(this.castBytes, 0, 2);
        } else {
            this.out.write(b);
        }
        return this;
    }

    public GfanPayPacker packByteArray(byte[] bArr) {
        packRaw(bArr.length);
        return packRawBody(bArr, 0, bArr.length);
    }

    public GfanPayPacker packByteArray(byte[] bArr, int i, int i2) {
        packRaw(i2);
        return packRawBody(bArr, i, i2);
    }

    public GfanPayPacker packByteBuffer(ByteBuffer byteBuffer) {
        packRaw(byteBuffer.remaining());
        return packRawBody(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining());
    }

    public GfanPayPacker packDouble(double d) {
        this.castBytes[0] = -53;
        long doubleToRawLongBits = Double.doubleToRawLongBits(d);
        this.castBytes[1] = (byte) (doubleToRawLongBits >> 56);
        this.castBytes[2] = (byte) (doubleToRawLongBits >> 48);
        this.castBytes[3] = (byte) (doubleToRawLongBits >> 40);
        this.castBytes[4] = (byte) (doubleToRawLongBits >> 32);
        this.castBytes[5] = (byte) (doubleToRawLongBits >> 24);
        this.castBytes[6] = (byte) (doubleToRawLongBits >> 16);
        this.castBytes[7] = (byte) (doubleToRawLongBits >> 8);
        this.castBytes[8] = (byte) doubleToRawLongBits;
        this.out.write(this.castBytes, 0, 9);
        return this;
    }

    public GfanPayPacker packFalse() {
        this.out.write(-62);
        return this;
    }

    public GfanPayPacker packFloat(float f) {
        this.castBytes[0] = -54;
        int floatToRawIntBits = Float.floatToRawIntBits(f);
        this.castBytes[1] = floatToRawIntBits >> 24;
        this.castBytes[2] = (byte) (floatToRawIntBits >> 16);
        this.castBytes[3] = (byte) (floatToRawIntBits >> 8);
        this.castBytes[4] = (byte) floatToRawIntBits;
        this.out.write(this.castBytes, 0, 5);
        return this;
    }

    public GfanPayPacker packInt(int i) {
        if (i < -32) {
            if (i < -32768) {
                this.castBytes[0] = -46;
                this.castBytes[1] = i >> 24;
                this.castBytes[2] = (byte) (i >> 16);
                this.castBytes[3] = (byte) (i >> 8);
                this.castBytes[4] = (byte) i;
                this.out.write(this.castBytes, 0, 5);
            } else if (i < -128) {
                this.castBytes[0] = -47;
                this.castBytes[1] = (byte) (i >> 8);
                this.castBytes[2] = (byte) i;
                this.out.write(this.castBytes, 0, 3);
            } else {
                this.castBytes[0] = -48;
                this.castBytes[1] = (byte) i;
                this.out.write(this.castBytes, 0, 2);
            }
        } else if (i < 128) {
            this.out.write((byte) i);
        } else if (i < 256) {
            this.castBytes[0] = -52;
            this.castBytes[1] = (byte) i;
            this.out.write(this.castBytes, 0, 2);
        } else if (i < 65536) {
            this.castBytes[0] = -51;
            this.castBytes[1] = (byte) (i >> 8);
            this.castBytes[2] = (byte) i;
            this.out.write(this.castBytes, 0, 3);
        } else {
            this.castBytes[0] = -50;
            this.castBytes[1] = i >> 24;
            this.castBytes[2] = (byte) (i >> 16);
            this.castBytes[3] = (byte) (i >> 8);
            this.castBytes[4] = (byte) i;
            this.out.write(this.castBytes, 0, 5);
        }
        return this;
    }

    public GfanPayPacker packLong(long j) {
        if (j < -32) {
            if (j < -32768) {
                if (j < -2147483648L) {
                    this.castBytes[0] = -45;
                    this.castBytes[1] = (byte) (j >> 56);
                    this.castBytes[2] = (byte) (j >> 48);
                    this.castBytes[3] = (byte) (j >> 40);
                    this.castBytes[4] = (byte) (j >> 32);
                    this.castBytes[5] = (byte) (j >> 24);
                    this.castBytes[6] = (byte) (j >> 16);
                    this.castBytes[7] = (byte) (j >> 8);
                    this.castBytes[8] = (byte) j;
                    this.out.write(this.castBytes, 0, 9);
                } else {
                    this.castBytes[0] = -46;
                    this.castBytes[1] = (byte) (j >> 24);
                    this.castBytes[2] = (byte) (j >> 16);
                    this.castBytes[3] = (byte) (j >> 8);
                    this.castBytes[4] = (byte) j;
                    this.out.write(this.castBytes, 0, 5);
                }
            } else if (j < -128) {
                this.castBytes[0] = -47;
                this.castBytes[1] = (byte) (j >> 8);
                this.castBytes[2] = (byte) j;
                this.out.write(this.castBytes, 0, 3);
            } else {
                this.castBytes[0] = -48;
                this.castBytes[1] = (byte) j;
                this.out.write(this.castBytes, 0, 2);
            }
        } else if (j < 128) {
            this.out.write((byte) j);
        } else if (j < 65536) {
            if (j < 256) {
                this.castBytes[0] = -52;
                this.castBytes[1] = (byte) j;
                this.out.write(this.castBytes, 0, 2);
            } else {
                this.castBytes[0] = -51;
                this.castBytes[1] = (byte) ((65280 & j) >> 8);
                this.castBytes[2] = (byte) (255 & j);
                this.out.write(this.castBytes, 0, 3);
            }
        } else if (j < 4294967296L) {
            this.castBytes[0] = -50;
            this.castBytes[1] = (byte) (((-16777216) & j) >> 24);
            this.castBytes[2] = (byte) ((16711680 & j) >> 16);
            this.castBytes[3] = (byte) ((65280 & j) >> 8);
            this.castBytes[4] = (byte) (255 & j);
            this.out.write(this.castBytes, 0, 5);
        } else {
            this.castBytes[0] = -49;
            this.castBytes[1] = (byte) (j >> 56);
            this.castBytes[2] = (byte) (j >> 48);
            this.castBytes[3] = (byte) (j >> 40);
            this.castBytes[4] = (byte) (j >> 32);
            this.castBytes[5] = (byte) (j >> 24);
            this.castBytes[6] = (byte) (j >> 16);
            this.castBytes[7] = (byte) (j >> 8);
            this.castBytes[8] = (byte) j;
            this.out.write(this.castBytes, 0, 9);
        }
        return this;
    }

    public GfanPayPacker packMap(int i) {
        if (i < 16) {
            this.out.write((byte) (i | 128));
        } else if (i < 65536) {
            this.castBytes[0] = -34;
            this.castBytes[1] = (byte) (i >> 8);
            this.castBytes[2] = (byte) i;
            this.out.write(this.castBytes, 0, 3);
        } else {
            this.castBytes[0] = -33;
            this.castBytes[1] = i >> 24;
            this.castBytes[2] = (byte) (i >> 16);
            this.castBytes[3] = (byte) (i >> 8);
            this.castBytes[4] = (byte) i;
            this.out.write(this.castBytes, 0, 5);
        }
        return this;
    }

    public GfanPayPacker packNil() {
        this.out.write(-64);
        return this;
    }

    public GfanPayPacker packRaw(int i) {
        if (i < 32) {
            this.out.write((byte) (i | 160));
        } else if (i < 65536) {
            this.castBytes[0] = -38;
            this.castBytes[1] = (byte) (i >> 8);
            this.castBytes[2] = (byte) i;
            this.out.write(this.castBytes, 0, 3);
        } else {
            this.castBytes[0] = -37;
            this.castBytes[1] = i >> 24;
            this.castBytes[2] = (byte) (i >> 16);
            this.castBytes[3] = (byte) (i >> 8);
            this.castBytes[4] = (byte) i;
            this.out.write(this.castBytes, 0, 5);
        }
        return this;
    }

    public GfanPayPacker packRawBody(byte[] bArr) {
        this.out.write(bArr);
        return this;
    }

    public GfanPayPacker packRawBody(byte[] bArr, int i, int i2) {
        this.out.write(bArr, i, i2);
        return this;
    }

    public GfanPayPacker packShort(short s) {
        if (s < -32) {
            if (s < -128) {
                this.castBytes[0] = -47;
                this.castBytes[1] = (byte) (s >> 8);
                this.castBytes[2] = (byte) s;
                this.out.write(this.castBytes, 0, 3);
            } else {
                this.castBytes[0] = -48;
                this.castBytes[1] = (byte) s;
                this.out.write(this.castBytes, 0, 2);
            }
        } else if (s < 128) {
            this.out.write((byte) s);
        } else if (s < 256) {
            this.castBytes[0] = -52;
            this.castBytes[1] = (byte) s;
            this.out.write(this.castBytes, 0, 2);
        } else {
            this.castBytes[0] = -51;
            this.castBytes[1] = (byte) (s >> 8);
            this.castBytes[2] = (byte) s;
            this.out.write(this.castBytes, 0, 3);
        }
        return this;
    }

    public GfanPayPacker packString(String str) {
        byte[] bytes = str.getBytes("UTF-8");
        packRaw(bytes.length);
        return packRawBody(bytes);
    }

    public GfanPayPacker packTrue() {
        this.out.write(-61);
        return this;
    }
}
