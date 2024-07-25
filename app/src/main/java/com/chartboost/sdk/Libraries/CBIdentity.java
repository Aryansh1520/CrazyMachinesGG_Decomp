package com.chartboost.sdk.Libraries;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import com.chartboost.sdk.Chartboost;
import com.mokredit.payment.StringUtils;
import org.bson.BasicBSONEncoder;
import org.bson.BasicBSONObject;

/* loaded from: classes.dex */
public class CBIdentity {
    private static String cbHexHashedMACAddress() {
        if (CBUtility.isIdentityTrackingDisabledOnThisDevice()) {
            return null;
        }
        return CBCrypto.hexRepresentation(CBCrypto.sha1(getMACAddress()));
    }

    public static String hexIdentifier() {
        return CBCrypto.hexRepresentation(combinedIdentifier());
    }

    private static byte[] combinedIdentifier() {
        String cbUUID = getAndroidID();
        String cbMACID = cbHexHashedMACAddress();
        BasicBSONObject obj = new BasicBSONObject();
        obj.put("uuid", (Object) cbUUID);
        obj.put("macid", (Object) cbMACID);
        BasicBSONEncoder enc = new BasicBSONEncoder();
        return enc.encode(obj);
    }

    private static String getAndroidID() {
        if (CBUtility.isIdentityTrackingDisabledOnThisDevice()) {
            return null;
        }
        Context context = Chartboost.sharedChartboost().getContext();
        return Settings.Secure.getString(context.getContentResolver(), "android_id");
    }

    private static byte[] getMACAddress() {
        try {
            Context context = Chartboost.sharedChartboost().getContext();
            WifiManager wifiMan = (WifiManager) context.getSystemService("wifi");
            String mac = wifiMan.getConnectionInfo().getMacAddress();
            if (mac == null || mac.equals(StringUtils.EMPTY)) {
                return null;
            }
            String[] macParts = mac.split(":");
            byte[] macAddress = new byte[6];
            for (int i = 0; i < macParts.length; i++) {
                Integer hex = Integer.valueOf(Integer.parseInt(macParts[i], 16));
                macAddress[i] = hex.byteValue();
            }
            return macAddress;
        } catch (Exception e) {
            return null;
        }
    }
}
