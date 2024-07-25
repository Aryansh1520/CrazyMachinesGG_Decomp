package com.mongodb.util;

/* loaded from: classes.dex */
public class Base64Codec {
    private static final byte PAD = 61;
    private static final int SixBitMask = 63;
    private static int BYTES_PER_UNENCODED_BLOCK = 3;
    private static int BYTES_PER_ENCODED_BLOCK = 4;
    private static final byte[] EncodeTable = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};

    public String encode(byte[] bArr) {
        int modulus = 0;
        int bitWorkArea = 0;
        int numEncodedBytes = (BYTES_PER_ENCODED_BLOCK * (bArr.length / BYTES_PER_UNENCODED_BLOCK)) + (bArr.length % BYTES_PER_UNENCODED_BLOCK == 0 ? 0 : 4);
        byte[] buffer = new byte[numEncodedBytes];
        int pos = 0;
        for (int i : bArr) {
            modulus = (modulus + 1) % BYTES_PER_UNENCODED_BLOCK;
            if (i < 0) {
                i += 256;
            }
            bitWorkArea = (bitWorkArea << 8) + i;
            if (modulus == 0) {
                int pos2 = pos + 1;
                buffer[pos] = EncodeTable[(bitWorkArea >> 18) & SixBitMask];
                int pos3 = pos2 + 1;
                buffer[pos2] = EncodeTable[(bitWorkArea >> 12) & SixBitMask];
                int pos4 = pos3 + 1;
                buffer[pos3] = EncodeTable[(bitWorkArea >> 6) & SixBitMask];
                pos = pos4 + 1;
                buffer[pos4] = EncodeTable[bitWorkArea & SixBitMask];
            }
        }
        switch (modulus) {
            case 1:
                int pos5 = pos + 1;
                buffer[pos] = EncodeTable[(bitWorkArea >> 2) & SixBitMask];
                int pos6 = pos5 + 1;
                buffer[pos5] = EncodeTable[(bitWorkArea << 4) & SixBitMask];
                int pos7 = pos6 + 1;
                buffer[pos6] = PAD;
                int i2 = pos7 + 1;
                buffer[pos7] = PAD;
                break;
            case 2:
                int pos8 = pos + 1;
                buffer[pos] = EncodeTable[(bitWorkArea >> 10) & SixBitMask];
                int pos9 = pos8 + 1;
                buffer[pos8] = EncodeTable[(bitWorkArea >> 4) & SixBitMask];
                int pos10 = pos9 + 1;
                buffer[pos9] = EncodeTable[(bitWorkArea << 2) & SixBitMask];
                int i3 = pos10 + 1;
                buffer[pos10] = PAD;
                break;
        }
        return new String(buffer);
    }
}
