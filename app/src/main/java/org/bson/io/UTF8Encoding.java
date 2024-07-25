package org.bson.io;

import java.io.IOException;
import java.text.MessageFormat;
import org.bson.BSON;

/* loaded from: classes.dex */
class UTF8Encoding {
    private static final int MAX_CODE_POINT = 1114111;
    private static final int MIN_2_BYTES = 128;
    private static final int MIN_3_BYTES = 2048;
    private static final int MIN_4_BYTES = 65536;
    private char[] decoderArray = new char[1024];

    private static final void checkByte(int ch, int pos, int len) throws IOException {
        if ((ch & 192) != 128) {
            throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: byte {0} of {1} byte sequence is not 10xxxxxx: {2}", new Integer(pos), new Integer(len), new Integer(ch)));
        }
    }

    private static final void checkMinimal(int ch, int minValue) throws IOException {
        int actualLen;
        int expectedLen;
        if (ch >= minValue) {
            return;
        }
        switch (minValue) {
            case 128:
                actualLen = 2;
                break;
            case 2048:
                actualLen = 3;
                break;
            case 65536:
                actualLen = 4;
                break;
            default:
                throw new IllegalArgumentException("unexpected minValue passed to checkMinimal: " + minValue);
        }
        if (ch < 128) {
            expectedLen = 1;
        } else if (ch < 2048) {
            expectedLen = 2;
        } else if (ch < 65536) {
            expectedLen = 3;
        } else {
            throw new IllegalArgumentException("unexpected ch passed to checkMinimal: " + ch);
        }
        throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: {0} bytes used to encode a {1} byte value: {2}", new Integer(actualLen), new Integer(expectedLen), new Integer(ch)));
    }

    public synchronized String decode(byte[] data, int offset, int length) throws IOException {
        char[] cdata;
        int out;
        int out2;
        cdata = this.decoderArray;
        if (cdata.length < length) {
            cdata = new char[length];
            this.decoderArray = cdata;
        }
        int end = length + offset;
        out = 0;
        int in = offset;
        while (in < end) {
            int in2 = in + 1;
            try {
                int ch = data[in] & BSON.MINKEY;
                if (ch >= 128) {
                    if (ch < 192) {
                        throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: initial byte is {0}: {1}", "10xxxxxx", new Integer(ch)));
                    }
                    if (ch < 224) {
                        int ch2 = (ch & 31) << 6;
                        checkByte(data[in2], 2, 2);
                        int in3 = in2 + 1;
                        try {
                            ch = ch2 | (data[in2] & 63);
                            checkMinimal(ch, 128);
                            in2 = in3;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            throw new IOException("Illegal UTF-8 sequence: multibyte sequence was truncated");
                        }
                    } else if (ch < 240) {
                        int ch3 = (ch & 15) << 12;
                        checkByte(data[in2], 2, 3);
                        int in4 = in2 + 1;
                        int ch4 = ch3 | ((data[in2] & 63) << 6);
                        checkByte(data[in4], 3, 3);
                        in2 = in4 + 1;
                        ch = ch4 | (data[in4] & 63);
                        checkMinimal(ch, 2048);
                    } else if (ch < 248) {
                        int ch5 = (ch & 7) << 18;
                        checkByte(data[in2], 2, 4);
                        int in5 = in2 + 1;
                        int ch6 = ch5 | ((data[in2] & 63) << 12);
                        checkByte(data[in5], 3, 4);
                        int in6 = in5 + 1;
                        int ch7 = ch6 | ((data[in5] & 63) << 6);
                        checkByte(data[in6], 4, 4);
                        int in7 = in6 + 1;
                        ch = ch7 | (data[in6] & 63);
                        checkMinimal(ch, 65536);
                        in2 = in7;
                    } else {
                        throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: initial byte is {0}: {1}", "11111xxx", new Integer(ch)));
                    }
                }
                if (ch > MAX_CODE_POINT) {
                    throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: final value is out of range: {0}", new Integer(ch)));
                }
                if (ch > 65535) {
                    int ch8 = ch - 65536;
                    int out3 = out + 1;
                    try {
                        cdata[out] = (char) (55296 + (ch8 >> 10));
                        int out4 = out3 + 1;
                        cdata[out3] = (char) (56320 + (ch8 & 1023));
                        out2 = out4;
                    } catch (ArrayIndexOutOfBoundsException e2) {
                        throw new IOException("Illegal UTF-8 sequence: multibyte sequence was truncated");
                    }
                } else {
                    if (ch >= 55296 && ch < 57344) {
                        throw new IOException(MessageFormat.format("Illegal UTF-8 sequence: final value is a surrogate value: {0}", new Integer(ch)));
                    }
                    out2 = out + 1;
                    cdata[out] = (char) ch;
                }
                out = out2;
                in = in2;
            } catch (ArrayIndexOutOfBoundsException e3) {
            }
        }
        if (in > end) {
            throw new IOException("Illegal UTF-8 sequence: multibyte sequence was truncated");
        }
        return new String(cdata, 0, out);
    }
}
