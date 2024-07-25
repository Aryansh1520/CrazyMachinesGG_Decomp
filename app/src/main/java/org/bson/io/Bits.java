package org.bson.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.bson.BSON;

/* loaded from: classes.dex */
public class Bits {
    public static void readFully(InputStream in, byte[] b) throws IOException {
        readFully(in, b, b.length);
    }

    public static void readFully(InputStream in, byte[] b, int length) throws IOException {
        readFully(in, b, 0, length);
    }

    public static void readFully(InputStream in, byte[] b, int startOffset, int length) throws IOException {
        if (b.length - startOffset > length) {
            throw new IllegalArgumentException("Buffer is too small");
        }
        int offset = startOffset;
        int toRead = length;
        while (toRead > 0) {
            int bytesRead = in.read(b, offset, toRead);
            if (bytesRead < 0) {
                throw new EOFException();
            }
            toRead -= bytesRead;
            offset += bytesRead;
        }
    }

    public static int readInt(InputStream in) throws IOException {
        return readInt(in, new byte[4]);
    }

    public static int readInt(InputStream in, byte[] data) throws IOException {
        readFully(in, data, 4);
        return readInt(data);
    }

    public static int readInt(byte[] data) {
        return readInt(data, 0);
    }

    public static int readInt(byte[] data, int offset) {
        int x = 0 | ((data[offset + 0] & BSON.MINKEY) << 0);
        return x | ((data[offset + 1] & BSON.MINKEY) << 8) | ((data[offset + 2] & BSON.MINKEY) << 16) | ((data[offset + 3] & BSON.MINKEY) << 24);
    }

    public static int readIntBE(byte[] data, int offset) {
        int x = 0 | ((data[offset + 0] & BSON.MINKEY) << 24);
        return x | ((data[offset + 1] & BSON.MINKEY) << 16) | ((data[offset + 2] & BSON.MINKEY) << 8) | ((data[offset + 3] & BSON.MINKEY) << 0);
    }

    public static long readLong(InputStream in) throws IOException {
        return readLong(in, new byte[8]);
    }

    public static long readLong(InputStream in, byte[] data) throws IOException {
        readFully(in, data, 8);
        return readLong(data);
    }

    public static long readLong(byte[] data) {
        return readLong(data, 0);
    }

    public static long readLong(byte[] data, int offset) {
        long x = 0 | ((data[offset + 0] & 255) << 0);
        return x | ((data[offset + 1] & 255) << 8) | ((data[offset + 2] & 255) << 16) | ((data[offset + 3] & 255) << 24) | ((data[offset + 4] & 255) << 32) | ((data[offset + 5] & 255) << 40) | ((data[offset + 6] & 255) << 48) | ((data[offset + 7] & 255) << 56);
    }
}
