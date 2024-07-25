package com.mokredit.payment;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;
import org.bson.BSON;

/* loaded from: classes.dex */
public class Md5Encrypt {
    private static Md5Encrypt a;

    static {
        Logger.getLogger(Md5Encrypt.class.getName());
        a = new Md5Encrypt();
    }

    private Md5Encrypt() {
    }

    public static String bytesToHexString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length);
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & BSON.MINKEY);
            if (hexString.length() < 2) {
                stringBuffer.append(0);
            }
            stringBuffer.append(hexString.toUpperCase());
        }
        return stringBuffer.toString();
    }

    public static String encrypt(String str) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            return bytesToHexString(messageDigest.digest());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static Md5Encrypt getInstance() {
        return a;
    }

    public static byte[] hexToByte(String str) {
        byte[] bArr = new byte[8];
        byte[] bytes = str.getBytes();
        for (int i = 0; i < 8; i++) {
            bArr[i] = unitBytes(bytes[i << 1], bytes[(i << 1) + 1]);
        }
        return bArr;
    }

    public static void main(String[] strArr) {
        System.out.println(encrypt("liyi@camel4u.comada11112111111111212127.00CNYGGACJDJFOFKPBOOC687EBF887E15515F911E915FAD9EC154"));
    }

    public static String sign(Map map, String str) {
        ArrayList arrayList = new ArrayList(map.keySet());
        Collections.sort(arrayList);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayList.size(); i++) {
            String str2 = (String) arrayList.get(i);
            String str3 = (String) map.get(str2);
            if (str2 != null && !str2.equalsIgnoreCase("sign") && !str2.equalsIgnoreCase("sign_type")) {
                stringBuffer.append(String.valueOf(str2) + "=" + str3 + "&");
            }
        }
        return encrypt(String.valueOf(stringBuffer.toString().substring(0, stringBuffer.lastIndexOf("&"))) + str);
    }

    public static byte unitBytes(byte b, byte b2) {
        return (byte) (((byte) (Byte.decode("0x" + new String(new byte[]{b})).byteValue() << 4)) ^ Byte.decode("0x" + new String(new byte[]{b2})).byteValue());
    }
}
