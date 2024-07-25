package com.chartboost.sdk.Libraries;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

/* loaded from: classes.dex */
public class CBCrypto {
    public static byte[] sha1(byte[] input) {
        if (input == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(input);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String hexRepresentation(byte[] data) {
        if (data == null) {
            return null;
        }
        BigInteger bi = new BigInteger(1, data);
        return String.format(Locale.US, "%0" + (data.length << 1) + "x", bi);
    }
}
