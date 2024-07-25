package org.bson.io;

import android.support.v4.view.MotionEventCompat;
import com.mongodb.util.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public abstract class OutputBuffer extends OutputStream {
    public abstract int getPosition();

    public abstract int pipe(OutputStream outputStream) throws IOException;

    public abstract void seekEnd();

    public abstract void seekStart();

    public abstract void setPosition(int i);

    public abstract int size();

    @Override // java.io.OutputStream
    public abstract void write(int i);

    @Override // java.io.OutputStream
    public abstract void write(byte[] bArr);

    @Override // java.io.OutputStream
    public abstract void write(byte[] bArr, int i, int i2);

    public byte[] toByteArray() {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream(size());
            pipe(bout);
            return bout.toByteArray();
        } catch (IOException ioe) {
            throw new RuntimeException("should be impossible", ioe);
        }
    }

    public String asString() {
        return new String(toByteArray());
    }

    public String asString(String encoding) throws UnsupportedEncodingException {
        return new String(toByteArray(), encoding);
    }

    public String hex() {
        final StringBuilder buf = new StringBuilder();
        try {
            pipe(new OutputStream() { // from class: org.bson.io.OutputBuffer.1
                @Override // java.io.OutputStream
                public void write(int b) {
                    String s = Integer.toHexString(b & MotionEventCompat.ACTION_MASK);
                    if (s.length() < 2) {
                        buf.append("0");
                    }
                    buf.append(s);
                }
            });
            return buf.toString();
        } catch (IOException e) {
            throw new RuntimeException("impossible");
        }
    }

    public String md5() {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            try {
                pipe(new OutputStream() { // from class: org.bson.io.OutputBuffer.2
                    @Override // java.io.OutputStream
                    public void write(byte[] b, int off, int len) {
                        md5.update(b, off, len);
                    }

                    @Override // java.io.OutputStream
                    public void write(int b) {
                        md5.update((byte) (b & MotionEventCompat.ACTION_MASK));
                    }
                });
                return Util.toHex(md5.digest());
            } catch (IOException e) {
                throw new RuntimeException("impossible");
            }
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException("Error - this implementation of Java doesn't support MD5.");
        }
    }

    public void writeInt(int x) {
        write(x >> 0);
        write(x >> 8);
        write(x >> 16);
        write(x >> 24);
    }

    public void writeIntBE(int x) {
        write(x >> 24);
        write(x >> 16);
        write(x >> 8);
        write(x);
    }

    public void writeInt(int pos, int x) {
        int save = getPosition();
        setPosition(pos);
        writeInt(x);
        setPosition(save);
    }

    public void writeLong(long x) {
        write((byte) ((x >> 0) & 255));
        write((byte) ((x >> 8) & 255));
        write((byte) ((x >> 16) & 255));
        write((byte) ((x >> 24) & 255));
        write((byte) ((x >> 32) & 255));
        write((byte) ((x >> 40) & 255));
        write((byte) ((x >> 48) & 255));
        write((byte) ((x >> 56) & 255));
    }

    public void writeDouble(double x) {
        writeLong(Double.doubleToRawLongBits(x));
    }

    public String toString() {
        return getClass().getName() + " size: " + size() + " pos: " + getPosition();
    }
}
