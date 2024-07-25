package com.mappn.sdk.uc.net;

import android.content.Context;
import com.mappn.sdk.common.codec.digest.DigestUtils;
import com.mappn.sdk.common.utils.BaseConstants;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.DESUtil;
import com.mappn.sdk.common.utils.SecurityUtil;
import com.mokredit.payment.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ApiRequestFactory {
    private static final ArrayList<Integer> a = new ArrayList<>();
    private static final ArrayList<Integer> b = new ArrayList<>();
    private static final ArrayList<Integer> c = new ArrayList<>();
    private static final ArrayList<Integer> d = new ArrayList<>();
    private static final ArrayList<Integer> e = new ArrayList<>();

    static {
        a.add(3);
        b.add(6);
        b.add(7);
        c.add(2);
        d.add(1);
        d.add(0);
        d.add(4);
        e.add(1);
        e.add(0);
        e.add(4);
        e.add(6);
        e.add(7);
    }

    private static String a(Object obj) {
        if (obj == null || !(obj instanceof HashMap)) {
            return "<request version=\"2\"></request>";
        }
        HashMap<String, Object> hashMap = (HashMap<String, Object>) obj;
        StringBuilder sb = new StringBuilder();
        sb.append("<request version=\"2\"");
        if (hashMap.containsKey("local_version")) {
            sb.append(" local_version=\"").append(hashMap.get("local_version")).append("\" ");
            hashMap.remove("local_version");
        }
        sb.append(">");
        for (String str : hashMap.keySet()) {
            sb.append("<").append(str).append(">");
            sb.append(hashMap.get(str));
            sb.append("</").append(str).append(">");
        }
        sb.append("</request>");
        return sb.toString();
    }

    private static String b(Object obj) {
        if (obj == null || !(obj instanceof HashMap)) {
            return StringUtils.EMPTY;
        }
        HashMap<String, Object> hashMap = (HashMap<String, Object>) obj;
        JSONObject jSONObject = new JSONObject();
        for (String str : hashMap.keySet()) {
            try {
                if (hashMap.get(str) instanceof List) {
                    JSONArray jSONArray = new JSONArray();
                    List<?> list = (List<?>) hashMap.get(str);
                    for (Object obj2 : list) {
                        if (obj2 instanceof HashMap) {
                            HashMap<String, Object> hashMap2 = (HashMap<String, Object>) obj2;
                            JSONObject jSONObject2 = new JSONObject();
                            for (String str2 : hashMap2.keySet()) {
                                if (hashMap2.get(str2) == null) {
                                    jSONObject2.put(str2, JSONObject.NULL);
                                } else {
                                    jSONObject2.put(str2, hashMap2.get(str2));
                                }
                                BaseUtils.D("ApiRequestFactory", "hashApp.get(key2) = " + hashMap2.get(str2));
                                BaseUtils.D("ApiRequestFactory", "jsonObj = " + jSONObject2.toString());
                            }
                            jSONArray.put(jSONObject2);
                            BaseUtils.D("ApiRequestFactory", "array = " + jSONArray.toString());
                        } else {
                            BaseUtils.D("ApiRequestFactory", "array = " + jSONArray.toString());
                            jSONArray.put(obj2);
                        }
                    }
                    if (list.size() == 0) {
                        jSONObject.put(str, JSONObject.NULL);
                    } else {
                        jSONObject.put(str, jSONArray);
                    }
                } else {
                    BaseUtils.D("ApiRequestFactory", "requestParams.get(key) = " + hashMap.get(str));
                    jSONObject.put(str, hashMap.get(str));
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
                return StringUtils.EMPTY;
            }
        }
        return jSONObject.toString();
    }

    public static HttpURLConnection createConnection(String urlStr, Context context, int i, Object obj) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        if (e.contains(Integer.valueOf(i))) {
            connection.setRequestProperty("Gfan-Agent", BaseUtils.getUCenterApiUserAgent(context));
        } else if (urlStr.startsWith(Api.API_MARKET_URL)) {
            String gHeader = BaseUtils.getGHeader(context);
            connection.setRequestProperty("G-Header", gHeader);
        }

        return connection;
    }

    public static void setRequestEntity(HttpURLConnection connection, String str, Context context, int i, Object obj) throws IOException {
        String requestBody;
        if (a.contains(Integer.valueOf(i))) {
            ArrayList<String> arrayList = new ArrayList<>();
            String jsonBody = b(obj);
            BaseUtils.D("ApiRequestFactory", "generate JSON request body is : " + jsonBody);
            String encAndBase64String = new DESUtil(context).getEncAndBase64String(jsonBody);
            String action = null;
            switch (i) {
                case 3:
                    action = "addAndroidBindInfo";
                    break;
            }
            arrayList.add("action=" + action);
            BaseUtils.D("ApiRequestFactory", "actionMethod is : " + action);
            arrayList.add("data=" + encAndBase64String);
            arrayList.add("cno=" + BaseConstants.DEFAULT_CNO);
            arrayList.add("sign=" + DigestUtils.md5Hex("action=" + action + "&data=" + encAndBase64String + "&cno=ifandroid" + BaseConstants.DEFAULT_DES_KEY));
            requestBody = String.join("&", arrayList);
        } else if (c.contains(Integer.valueOf(i))) {
            HashMap<String, String> hashMap = (HashMap<String, String>) obj;
            ArrayList<String> arrayList2 = new ArrayList<>();
            for (String str3 : hashMap.keySet()) {
                arrayList2.add(str3 + "=" + hashMap.get(str3));
            }
            requestBody = String.join("&", arrayList2);
        } else if (d.contains(Integer.valueOf(i))) {
            String xmlBody = a(obj);
            BaseUtils.D("ApiRequestFactory", "generate request body before encryption  is : " + xmlBody);
            requestBody = new String(SecurityUtil.encryptHttpBody(xmlBody));
        } else if (b.contains(Integer.valueOf(i))) {
            String jsonBody = b(obj);
            BaseUtils.D("ApiRequestFactory", "generate request body before encryption  is : " + jsonBody);
            requestBody = new String(SecurityUtil.encryptHttpBody(jsonBody));
        } else if (str.startsWith(Api.API_MARKET_URL)) {
            requestBody = a(obj);
        } else {
            requestBody = null;
        }

        if (requestBody != null) {
            OutputStream os = connection.getOutputStream();
            os.write(requestBody.getBytes("UTF-8"));
            os.flush();
            os.close();
        }
    }
}
