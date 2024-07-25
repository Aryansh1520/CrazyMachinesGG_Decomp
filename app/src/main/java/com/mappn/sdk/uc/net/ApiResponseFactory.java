package com.mappn.sdk.uc.net;

import android.content.Context;

import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.uc.User;
import com.mappn.sdk.uc.UserVo;
import com.mappn.sdk.uc.util.Constants;
import com.mappn.sdk.uc.util.XmlElement;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ApiResponseFactory {
    private static UserVo a(XmlElement xmlElement) {
        if (xmlElement == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        userVo.setUid(Long.parseLong(xmlElement.getChild("uid", 0).getText()));
        userVo.setUserName(xmlElement.getChild("name", 0).getText());
        userVo.setToken(xmlElement.getChild("token", 0).getText());
        return userVo;
    }

    private static HashMap a(String str) {
        HashMap hashMap;
        JSONException e;
        BaseUtils.D("ApiResponseFactory", "parseVerifyToken body: " + str);
        if (str == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            hashMap = new HashMap();
            try {
                hashMap.put(Constants.RESULT_CODE, Integer.valueOf(jSONObject.getInt(Constants.RESULT_CODE)));
                return hashMap;
            } catch (JSONException e2) {
                e = e2;
                BaseUtils.D("ApiResponseFactory", "have json exception when parse parseVerifyToken", e);
                return hashMap;
            }
        } catch (JSONException e3) {
            hashMap = null;
            e = e3;
        }
    }

    private static List a(JSONObject jSONObject) {
        ArrayList arrayList = new ArrayList();
        try {
            JSONArray jSONArray = jSONObject.getJSONArray("friend");
            int length = jSONArray.length();
            for (int i = 0; i < length; i++) {
                User parseJsonFriend = parseJsonFriend(jSONArray.getJSONObject(i));
                if (parseJsonFriend != null) {
                    arrayList.add(parseJsonFriend);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private static String b(XmlElement xmlElement) {
        if (xmlElement == null) {
            return null;
        }
        try {
            return xmlElement.getChild("product", 0).getAttribute("p_id");
        } catch (Exception e) {
            return null;
        }
    }

    private static HashMap b(String str) {
        HashMap hashMap;
        JSONException e;
        JSONObject jSONObject;
        if (str == null) {
            return null;
        }
        try {
            jSONObject = new JSONObject(str);
            hashMap = new HashMap();
        } catch (JSONException e2) {
            hashMap = null;
            e = e2;
        }
        try {
            hashMap.put(Constants.RESULT_CODE, Integer.valueOf(jSONObject.getInt(Constants.RESULT_CODE)));
            return hashMap;
        } catch (JSONException e3) {
            e = e3;
            BaseUtils.D("ApiResponseFactory", "have json exception when parse parseAddAndroidBindInfo", e);
            return hashMap;
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(7:1|(3:70|(1:72)|38)(2:9|(3:11|(1:13)(2:51|52)|14)(3:56|(4:61|62|(1:64)(1:67)|65)(1:58)|60))|15|16|17|38|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00f7, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00f8, code lost:
    
        com.mappn.sdk.common.utils.BaseUtils.D("ApiResponseFactory", r3 + " has IOException", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00da, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00db, code lost:
    
        r2 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00dc, code lost:
    
        com.mappn.sdk.common.utils.BaseUtils.D("ApiResponseFactory", r2 + " has XmlPullParserException", r0);
        r3 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x012c, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x012d, code lost:
    
        r3 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x012f, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0072, code lost:
    
        if (android.text.TextUtils.isEmpty(r0) != false) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x005d, code lost:
    
        if (android.text.TextUtils.isEmpty(r0) == false) goto L32;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0112  */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v21 */
    /* JADX WARN: Type inference failed for: r2v24 */
    /* JADX WARN: Type inference failed for: r2v26 */
    /* JADX WARN: Type inference failed for: r2v27 */
    /* JADX WARN: Type inference failed for: r2v28 */
    /* JADX WARN: Type inference failed for: r2v29 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r4v0, types: [java.lang.StringBuilder] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.Object getResponse(Context r6, int r7, HttpURLConnection r8, String r9) {
        /*
            Method dump skipped, instructions count: 340
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.uc.net.ApiResponseFactory.getResponse(android.content.Context, int, org.apache.http.HttpResponse, java.lang.String):java.lang.Object");
    }

    public static User parseJsonFriend(JSONObject jSONObject) {
        User user = new User();
        try {
            user.setUid(jSONObject.getLong("uid"));
            user.setUserName(jSONObject.getString("username"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
