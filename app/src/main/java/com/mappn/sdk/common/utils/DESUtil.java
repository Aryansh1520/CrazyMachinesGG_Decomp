package com.mappn.sdk.common.utils;

import android.content.Context;
import com.mappn.sdk.common.codec.binary.Base64;
import com.mokredit.payment.StringUtils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/* loaded from: classes.dex */
public class DESUtil {
    private Cipher a;
    private Cipher b;

    public DESUtil(Context context) {
        SecretKey key = getKey(BaseConstants.DEFAULT_DES_KEY);
        try {
            this.a = Cipher.getInstance("DES/ECB/PKCS5Padding");
            this.b = Cipher.getInstance("DES/ECB/PKCS5Padding");
            this.a.init(1, key);
            this.b.init(2, key);
        } catch (InvalidKeyException e) {
            BaseUtils.E("DESUtil", e.getMessage());
        } catch (NoSuchAlgorithmException e2) {
            BaseUtils.E("DESUtil", e2.getMessage());
        } catch (NoSuchPaddingException e3) {
            BaseUtils.E("DESUtil", e3.getMessage());
        }
    }

    public static void generateKeyToFile(String str) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
            keyGenerator.init(secureRandom);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(str));
            objectOutputStream.writeObject(keyGenerator.generateKey());
            objectOutputStream.close();
        } catch (Exception e) {
            BaseUtils.E("DESUtil", e.getMessage());
        }
    }

    public static SecretKey getKeyFromFile(String str) {
        SecretKey secretKey;
        Exception e;
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(str));
            secretKey = (SecretKey) objectInputStream.readObject();
        } catch (Exception e2) {
            secretKey = null;
            e = e2;
        }
        try {
            objectInputStream.close();
        } catch (Exception e3) {
            e = e3;
            BaseUtils.E("DESUtil", "getKeyFromFile", e);
            return secretKey;
        }
        return secretKey;
    }

    public void decrypt(InputStream inputStream, OutputStream outputStream) {
        try {
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, this.b);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = cipherInputStream.read(bArr);
                if (read < 0) {
                    return;
                } else {
                    outputStream.write(bArr, 0, read);
                }
            }
        } catch (IOException e) {
            BaseUtils.E("DESUtil", "decrypt", e);
        }
    }

    public void encrypt(InputStream inputStream, OutputStream outputStream) {
        try {
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, this.a);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read < 0) {
                    return;
                } else {
                    cipherOutputStream.write(bArr, 0, read);
                }
            }
        } catch (IOException e) {
            BaseUtils.E("DESUtil", "encrypt", e);
        }
    }

    public String getDesAndBase64String(String str) {
        try {
            return new String(getDesCode(new Base64().decode(str.getBytes("UTF-8"))), "UTF-8");
        } catch (Exception e) {
            BaseUtils.E("DESUtil", "getDesAndBase64String", e);
            return StringUtils.EMPTY;
        }
    }

    public byte[] getDesCode(byte[] bArr) {
        try {
            return this.b.doFinal(bArr);
        } catch (Exception e) {
            BaseUtils.E("DESUtil", "getDesCode", e);
            return null;
        }
    }

    public String getEncAndBase64String(String str) {
        if (str == null) {
            str = StringUtils.EMPTY;
        }
        try {
            byte[] bArr = new byte[r0.length - 2];
            System.arraycopy(new Base64().encode(getEncCode(str.getBytes("UTF-8"))), 0, bArr, 0, r0.length - 2);
            return new String(bArr, "UTF-8");
        } catch (Exception e) {
            BaseUtils.E("DESUtil", "getEncAndBase64String", e);
            return StringUtils.EMPTY;
        }
    }

    public byte[] getEncCode(byte[] bArr) {
        try {
            return this.a.doFinal(bArr);
        } catch (Exception e) {
            BaseUtils.E("DESUtil", "getEncCode", e);
            return null;
        }
    }

    public SecretKey getKey(String str) {
        try {
            return SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(str.getBytes("UTF-8")));
        } catch (Exception e) {
            BaseUtils.E("DESUtil", "getKey", e);
            return null;
        }
    }
}
