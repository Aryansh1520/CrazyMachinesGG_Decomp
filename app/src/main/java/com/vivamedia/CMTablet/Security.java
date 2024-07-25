package com.vivamedia.CMTablet;

import android.text.TextUtils;
import android.util.Log;
import com.mokredit.payment.StringUtils;
import com.vivamedia.CMTablet.GooglePurchaseObserver;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class Security {
    private static final String KEY_FACTORY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String TAG = "Security";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static HashSet<Long> sKnownNonces = new HashSet<>();

    /* loaded from: classes.dex */
    public static class VerifiedPurchase {
        public String developerPayload;
        public String notificationId;
        public String orderId;
        public String productId;
        public GooglePurchaseObserver.PurchaseState purchaseState;
        public long purchaseTime;

        public VerifiedPurchase(GooglePurchaseObserver.PurchaseState purchaseState, String notificationId, String productId, String orderId, long purchaseTime, String developerPayload) {
            this.purchaseState = purchaseState;
            this.notificationId = notificationId;
            this.productId = productId;
            this.orderId = orderId;
            this.purchaseTime = purchaseTime;
            this.developerPayload = developerPayload;
        }
    }

    public static long generateNonce() {
        long nonce = RANDOM.nextLong();
        sKnownNonces.add(Long.valueOf(nonce));
        return nonce;
    }

    public static void removeNonce(long nonce) {
        sKnownNonces.remove(Long.valueOf(nonce));
    }

    public static boolean isNonceKnown(long nonce) {
        return sKnownNonces.contains(Long.valueOf(nonce));
    }

    public static ArrayList<VerifiedPurchase> verifyPurchase(String signedData, String signature) {
        if (signedData == null) {
            return null;
        }
        boolean verified = false;
        if (!TextUtils.isEmpty(signature)) {
            PublicKey key = generatePublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAt4yMbCMa1o/MfImQyuLUUczT9uHj0NykL9KqpCw1GGaDiu33Qf3ytLFTv+C7U5rJYYxv0+25FJ4Sb0RTnFRD3CkH7tJe2KeYkCHNo3dARR/SRGa7uzlYJ9JoDuN4Dq7H8AcYFpXF5bPLG0/WEmyigWfEtu+SMmIXtF/Kj4B7P+SDehfBSxdHtP4+LzkI3ksk/HdWAKTMd9dl1uY8VlxIu2/QyS2IaPrRr6JgZy3X064Zp+o/hcvJQ44xiTUxwA5+eabqtKLMCSxTPhlcaywjNvXmeaOZlJOm77gacRMwu4XwOKy9jWNYGFcdKSO9khfCzjiVc49G0uL4ZVyOMyPPVQIDAQAB");
            verified = verify(key, signedData, signature);
            if (!verified) {
                return null;
            }
        }
        int numTransactions = 0;
        try {
            JSONObject jObject = new JSONObject(signedData);
            long nonce = jObject.optLong("nonce");
            JSONArray jTransactionsArray = jObject.optJSONArray("orders");
            if (jTransactionsArray != null) {
                numTransactions = jTransactionsArray.length();
            }
            if (!isNonceKnown(nonce)) {
                return null;
            }
            ArrayList<VerifiedPurchase> purchases = new ArrayList<>();
            for (int i = 0; i < numTransactions; i++) {
                try {
                    JSONObject jElement = jTransactionsArray.getJSONObject(i);
                    int response = jElement.getInt("purchaseState");
                    GooglePurchaseObserver.PurchaseState purchaseState = GooglePurchaseObserver.PurchaseState.valueOf(response);
                    String productId = jElement.getString("productId");
                    long purchaseTime = jElement.getLong("purchaseTime");
                    String orderId = jElement.optString("orderId", StringUtils.EMPTY);
                    String notifyId = null;
                    if (jElement.has("notificationId")) {
                        notifyId = jElement.getString("notificationId");
                    }
                    String developerPayload = jElement.optString("developerPayload", null);
                    if (purchaseState != GooglePurchaseObserver.PurchaseState.PURCHASED || verified) {
                        purchases.add(new VerifiedPurchase(purchaseState, notifyId, productId, orderId, purchaseTime, developerPayload));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON exception: ", e);
                    return null;
                }
            }
            removeNonce(nonce);
            return purchases;
        } catch (JSONException e2) {
            Log.e(TAG, "JSONException: " + e2);
            return null;
        }
    }

    public static PublicKey generatePublicKey(String encodedPublicKey) {
        try {
            byte[] decodedKey = Base64.decode(encodedPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } catch (Base64DecoderException e) {
            Log.e(TAG, "FBase64 decoding failed.");
            throw new IllegalArgumentException(e);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        } catch (InvalidKeySpecException e3) {
            Log.e(TAG, "Invalid key specification.");
            throw new IllegalArgumentException(e3);
        }
    }

    public static boolean verify(PublicKey publicKey, String signedData, String signature) {
        try {
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey);
            sig.update(signedData.getBytes());
            return sig.verify(Base64.decode(signature));
        } catch (Base64DecoderException e) {
            Log.e(TAG, "FBase64 decoding failed.");
            return false;
        } catch (InvalidKeyException e2) {
            Log.e(TAG, "Invalid key specification.");
            return false;
        } catch (NoSuchAlgorithmException e3) {
            Log.e(TAG, "NoSuchAlgorithmException.");
            return false;
        } catch (SignatureException e4) {
            Log.e(TAG, "Signature exception.");
            return false;
        }
    }
}
