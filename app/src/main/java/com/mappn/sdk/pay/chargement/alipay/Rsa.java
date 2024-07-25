package com.mappn.sdk.pay.chargement.alipay;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/* loaded from: classes.dex */
public class Rsa {
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static boolean doCheck(String str, String str2, String str3) {
        try {
            PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(str3)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(generatePublic);
            signature.update(str.getBytes("utf-8"));
            return signature.verify(Base64.decode(str2));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String encrypt(String str, String str2) {
        try {
            PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(str2)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, generatePublic);
            return new String(Base64.encode(cipher.doFinal(str.getBytes("UTF-8"))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String sign(String str, String str2) {
        try {
            PrivateKey generatePrivate = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(str2)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(generatePrivate);
            signature.update(str.getBytes("utf-8"));
            return Base64.encode(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
