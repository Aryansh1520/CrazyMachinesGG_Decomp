package com.chartboost.sdk.Libraries;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.view.Display;
import android.view.WindowManager;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;
import com.mokredit.payment.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CBUtility {
    public static final String AUID_STATIC_EMULATOR = "ffff";
    public static final String PREFERENCES_FILE_DEFAULT = "cbPrefs";
    public static final String PREFERENCES_KEY_ID_TRACKING_DISABLED = "cbIdentityTrackingDisabled";
    public static final String PREFERENCES_KEY_SESSION_COUNT = "cbPrefSessionCount";
    private static String ANDROID_IDENTITY = null;
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    public static SharedPreferences getPreferences() {
        return Chartboost.sharedChartboost().getContext().getSharedPreferences(PREFERENCES_FILE_DEFAULT, 0);
    }

    public static String getIdentity() {
        if (isIdentityTrackingDisabledOnThisDevice()) {
            return null;
        }
        if (ANDROID_IDENTITY != null) {
            return ANDROID_IDENTITY;
        }
        ANDROID_IDENTITY = CBIdentity.hexIdentifier();
        return ANDROID_IDENTITY;
    }

    public static boolean isIdentityTrackingDisabledOnThisDevice() {
        return getPreferences().getBoolean(PREFERENCES_KEY_ID_TRACKING_DISABLED, false);
    }

    public static boolean isDebugBuild(Context cx) {
        boolean debuggable = false;
        try {
            PackageInfo pinfo = cx.getPackageManager().getPackageInfo(cx.getPackageName(), 64);
            Signature[] signatures = pinfo.signatures;
            for (Signature signature : signatures) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signature.toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) {
                    break;
                }
            }
        } catch (Exception e) {
        }
        boolean manifestDebuggable = (cx.getApplicationInfo().flags & 2) != 0;
        return debuggable | manifestDebuggable;
    }

    public static String mapToString(Map<String, String> map) {
        String encode;
        if (map == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if (!map.keySet().isEmpty()) {
            stringBuilder.append("?");
        }
        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            if (key == null) {
                encode = StringUtils.EMPTY;
            } else {
                try {
                    encode = URLEncoder.encode(key, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("This method requires UTF-8 encoding support", e);
                }
            }
            stringBuilder.append(encode);
            stringBuilder.append("=");
            stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : StringUtils.EMPTY);
        }
        return stringBuilder.toString();
    }

    public static List<String> JSONArrayToList(JSONArray arr) {
        if (arr == null) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            try {
                list.add(arr.getString(i));
            } catch (Exception e) {
            }
        }
        return list;
    }

    public static Map<String, String> JSONObjectToMap(JSONObject obj) {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        Iterator<?> itr = obj.keys();
        while (itr.hasNext()) {
            try {
                String key = itr.next();
                String value = obj.getString(key);
                map.put(key, value);
            } catch (Exception e) {
            }
        }
        return map;
    }

    public static JSONArray listToJSONArray(List<String> list) {
        if (list == null) {
            return null;
        }
        JSONArray arr = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            try {
                arr.put(list.get(i));
            } catch (Exception e) {
            }
        }
        return arr;
    }

    public static JSONObject mapToJSONObject(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        JSONObject obj = new JSONObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                obj.put(key, value);
            } catch (Exception e) {
            }
        }
        return obj;
    }

    public static float getDensity(Context cx) {
        return cx.getResources().getDisplayMetrics().density;
    }

    public static int dpToPixels(int i, Context cx) {
        return Math.round(i * getDensity(cx));
    }

    public static float dpToPixelsF(int i, Context cx) {
        return i * getDensity(cx);
    }

    public static CBConstants.CBOrientation getOrientation(Context context) {
        int orientation;
        boolean naturalOrientationIsPortrait = true;
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        int defOrientation = context.getResources().getConfiguration().orientation;
        int rotation = display.getRotation();
        if (display.getWidth() == display.getHeight()) {
            orientation = 3;
        } else if (display.getWidth() < display.getHeight()) {
            orientation = 1;
        } else {
            orientation = 2;
        }
        boolean isPortrait = true;
        if (orientation == 1) {
            isPortrait = true;
        } else if (orientation == 2) {
            isPortrait = false;
        } else if (orientation == 3) {
            if (defOrientation == 1) {
                isPortrait = true;
            } else if (defOrientation == 2) {
                isPortrait = false;
            }
        }
        if (rotation == 0 || rotation == 2) {
            naturalOrientationIsPortrait = isPortrait;
        } else if (isPortrait) {
            naturalOrientationIsPortrait = false;
        }
        if (naturalOrientationIsPortrait) {
            switch (rotation) {
                case 1:
                    return CBConstants.CBOrientation.LANDSCAPE_LEFT;
                case 2:
                    return CBConstants.CBOrientation.PORTRAIT_REVERSE;
                case 3:
                    return CBConstants.CBOrientation.LANDSCAPE_RIGHT;
                default:
                    return CBConstants.CBOrientation.PORTRAIT;
            }
        }
        switch (rotation) {
            case 1:
                return CBConstants.CBOrientation.PORTRAIT_LEFT;
            case 2:
                return CBConstants.CBOrientation.LANDSCAPE_REVERSE;
            case 3:
                return CBConstants.CBOrientation.PORTRAIT_RIGHT;
            default:
                return CBConstants.CBOrientation.LANDSCAPE;
        }
    }
}
