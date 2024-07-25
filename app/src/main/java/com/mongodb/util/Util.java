package com.mongodb.util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bson.BSON;

/* loaded from: classes.dex */
public class Util {
    public static String toHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte b2 : b) {
            String s = Integer.toHexString(b2 & BSON.MINKEY);
            if (s.length() < 2) {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static String hexMD5(byte[] data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(data);
            byte[] digest = md5.digest();
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error - this implementation of Java doesn't support MD5.");
        }
    }

    public static String hexMD5(ByteBuffer buf, int offset, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = buf.get(offset + i);
        }
        return hexMD5(b);
    }
}
