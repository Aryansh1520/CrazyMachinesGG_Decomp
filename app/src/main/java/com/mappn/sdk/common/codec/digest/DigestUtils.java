package com.mappn.sdk.common.codec.digest;

import com.mappn.sdk.common.codec.binary.Hex;
import com.mappn.sdk.common.codec.binary.StringUtils;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class DigestUtils {
    private static MessageDigest a(String str) {
        try {
            return MessageDigest.getInstance(str);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static byte[] a(MessageDigest messageDigest, InputStream inputStream) {
        byte[] bArr = new byte[1024];
        int read = inputStream.read(bArr, 0, 1024);
        while (read >= 0) {
            messageDigest.update(bArr, 0, read);
            read = inputStream.read(bArr, 0, 1024);
        }
        return messageDigest.digest();
    }

    public static byte[] md5(InputStream inputStream) {
        return a(a("MD5"), inputStream);
    }

    public static byte[] md5(String str) {
        return md5(StringUtils.getBytesUtf8(str));
    }

    public static byte[] md5(byte[] bArr) {
        return a("MD5").digest(bArr);
    }

    public static String md5Hex(InputStream inputStream) {
        return Hex.encodeHexString(md5(inputStream));
    }

    public static String md5Hex(String str) {
        return Hex.encodeHexString(md5(str));
    }

    public static String md5Hex(byte[] bArr) {
        return Hex.encodeHexString(md5(bArr));
    }

    public static byte[] sha(InputStream inputStream) {
        return a(a("SHA"), inputStream);
    }

    public static byte[] sha(String str) {
        return sha(StringUtils.getBytesUtf8(str));
    }

    public static byte[] sha(byte[] bArr) {
        return a("SHA").digest(bArr);
    }

    public static byte[] sha256(InputStream inputStream) {
        return a(a("SHA-256"), inputStream);
    }

    public static byte[] sha256(String str) {
        return sha256(StringUtils.getBytesUtf8(str));
    }

    public static byte[] sha256(byte[] bArr) {
        return a("SHA-256").digest(bArr);
    }

    public static String sha256Hex(InputStream inputStream) {
        return Hex.encodeHexString(sha256(inputStream));
    }

    public static String sha256Hex(String str) {
        return Hex.encodeHexString(sha256(str));
    }

    public static String sha256Hex(byte[] bArr) {
        return Hex.encodeHexString(sha256(bArr));
    }

    public static byte[] sha384(InputStream inputStream) {
        return a(a("SHA-384"), inputStream);
    }

    public static byte[] sha384(String str) {
        return sha384(StringUtils.getBytesUtf8(str));
    }

    public static byte[] sha384(byte[] bArr) {
        return a("SHA-384").digest(bArr);
    }

    public static String sha384Hex(InputStream inputStream) {
        return Hex.encodeHexString(sha384(inputStream));
    }

    public static String sha384Hex(String str) {
        return Hex.encodeHexString(sha384(str));
    }

    public static String sha384Hex(byte[] bArr) {
        return Hex.encodeHexString(sha384(bArr));
    }

    public static byte[] sha512(InputStream inputStream) {
        return a(a("SHA-512"), inputStream);
    }

    public static byte[] sha512(String str) {
        return sha512(StringUtils.getBytesUtf8(str));
    }

    public static byte[] sha512(byte[] bArr) {
        return a("SHA-512").digest(bArr);
    }

    public static String sha512Hex(InputStream inputStream) {
        return Hex.encodeHexString(sha512(inputStream));
    }

    public static String sha512Hex(String str) {
        return Hex.encodeHexString(sha512(str));
    }

    public static String sha512Hex(byte[] bArr) {
        return Hex.encodeHexString(sha512(bArr));
    }

    public static String shaHex(InputStream inputStream) {
        return Hex.encodeHexString(sha(inputStream));
    }

    public static String shaHex(String str) {
        return Hex.encodeHexString(sha(str));
    }

    public static String shaHex(byte[] bArr) {
        return Hex.encodeHexString(sha(bArr));
    }
}
