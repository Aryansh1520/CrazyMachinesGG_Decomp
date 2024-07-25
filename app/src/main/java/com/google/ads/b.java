package com.google.ads;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/* loaded from: classes.dex */
public class b {
    private static b c = null;
    private BigInteger b = BigInteger.ONE;
    private final BigInteger a = d();

    public static synchronized b a() {
        b bVar;
        synchronized (b.class) {
            if (c == null) {
                c = new b();
            }
            bVar = c;
        }
        return bVar;
    }

    public synchronized BigInteger b() {
        return this.a;
    }

    public synchronized BigInteger c() {
        BigInteger bigInteger;
        bigInteger = this.b;
        this.b = this.b.add(BigInteger.ONE);
        return bigInteger;
    }

    private b() {
    }

    private static BigInteger d() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            UUID randomUUID = UUID.randomUUID();
            messageDigest.update(a(randomUUID.getLeastSignificantBits()));
            messageDigest.update(a(randomUUID.getMostSignificantBits()));
            byte[] bArr = new byte[9];
            bArr[0] = 0;
            System.arraycopy(messageDigest.digest(), 0, bArr, 1, 8);
            return new BigInteger(bArr);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot find MD5 message digest algorithm.");
        }
    }

    private static byte[] a(long j) {
        return BigInteger.valueOf(j).toByteArray();
    }
}
