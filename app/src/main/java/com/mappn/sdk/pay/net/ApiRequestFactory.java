package com.mappn.sdk.pay.net;

import android.content.Context;
import com.mappn.sdk.common.codec.digest.DigestUtils;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.common.utils.SecurityUtil;
import com.mappn.sdk.pay.util.DBUtil;
import com.mokredit.payment.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ApiRequestFactory {
    private static ArrayList a = new ArrayList();
    private static ArrayList b = new ArrayList();

    static {
        a.add(13);
        a.add(14);
        a.add(15);
        a.add(16);
        a.add(17);
        a.add(21);
        a.add(22);
        b.add(0);
        b.add(1);
        b.add(2);
        b.add(3);
        b.add(4);
        b.add(5);
        b.add(6);
        b.add(7);
        b.add(8);
        b.add(9);
        b.add(10);
        b.add(11);
        b.add(12);
        b.add(18);
    }

    private static String a(Object obj) {
        if (obj == null || !(obj instanceof HashMap)) {
            return StringUtils.EMPTY;
        }
        HashMap hashMap = (HashMap) obj;
        JSONObject jSONObject = new JSONObject();
        for (String str : hashMap.keySet()) {
            try {
                jSONObject.put(str, hashMap.get(str));
            } catch (JSONException e) {
                e.printStackTrace();
                return StringUtils.EMPTY;
            }
        }
        return jSONObject.toString();
    }

    private static UrlEncodedFormEntity a(Context context, int i, Object obj) {
        String str;
        String str2;
        String str3;
        String a2 = a(obj);
        BaseUtils.D("ApiRequestFactory", "generate request body before encryption  is : " + a2);
        try {
            str = new String(SecurityUtil.encryptHttpChargeAlipayBody(a2), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            BaseUtils.E("ApiRequestFactory", "getEncryptJsonRequest e1 :" + e);
            str = StringUtils.EMPTY;
        }
        switch (i) {
            case 13:
                str2 = "addProductGMoneyOrder";
                str3 = "06";
                break;
            case 14:
            case 18:
            case 19:
            case 20:
            default:
                str2 = null;
                str3 = null;
                break;
            case 15:
                str2 = "addProductAlipayOrder";
                str3 = "05";
                break;
            case 16:
                str2 = "queryAlipayOrderIsSuccess";
                str3 = "05";
                break;
            case 17:
                str2 = "getRechargeChannels";
                str3 = "06";
                break;
            case 21:
                str2 = "addProductMo9Order";
                str3 = "05";
                break;
            case 22:
                str2 = "queryMo9OrderIsSuccess";
                str3 = "05";
                break;
        }
        ArrayList arrayList = new ArrayList(4);
        arrayList.add(new BasicNameValuePair("action", str2));
        arrayList.add(new BasicNameValuePair("data", str));
        arrayList.add(new BasicNameValuePair("cno", str3));
        String str4 = "action=" + str2 + "&data=" + str + "&cno=" + str3 + SecurityUtil.KEY_HTTP_CHARGE_ALIPAY_AND_G;
        BaseUtils.D("ApiRequestFactory", str4);
        arrayList.add(new BasicNameValuePair("sign", DigestUtils.md5Hex(str4)));
        BaseUtils.D("ApiRequestFactory", arrayList.toString());
        try {
            return new UrlEncodedFormEntity(arrayList, "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            BaseUtils.E("ApiRequestFactory", "getEncryptJsonRequest e :" + e2);
            return null;
        }
    }

    public static HttpUriRequest getRequest(String str, int i, HttpEntity httpEntity, Context context) {
        HttpPost httpPost = new HttpPost(str);
        if (i <= 9 || i == 18) {
            httpPost.setHeader("Gfan-Agent", BaseUtils.getUCenterApiUserAgent(context));
        }
        httpPost.setEntity(httpEntity);
        return httpPost;
    }

    public static HttpEntity getRequestEntity(Context context, int i, Object obj) {
        StringEntity stringEntity;
        byte[] bArr = null;
        if (a.contains(Integer.valueOf(i))) {
            return a(context, i, obj);
        }
        if (!b.contains(Integer.valueOf(i))) {
            return null;
        }
        if (i == 11 || i == 12) {
            try {
                stringEntity = new StringEntity(getXmlRequestBody(obj, context), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                BaseUtils.E("ApiRequestFactory", "getRequestEntity e :" + e);
                stringEntity = null;
            }
            stringEntity.setContentType("text/xml; charset=UTF-8");
            return stringEntity;
        }
        String xmlRequestBody = getXmlRequestBody(obj, context);
        BaseUtils.D("ApiRequestFactory", "generate request body before encryption  is : " + xmlRequestBody);
        if (i <= 9 || i == 18) {
            bArr = SecurityUtil.encryptHttpBody(xmlRequestBody);
        } else if (i == 10) {
            bArr = SecurityUtil.encryptHttpChargePhoneCardBody(xmlRequestBody);
        }
        return new ByteArrayEntity(bArr);
    }

    public static String getXmlRequestBody(Object obj, Context context) {
        if (obj == null || !(obj instanceof HashMap)) {
            return "<request version=\"2\"></request>";
        }
        HashMap hashMap = (HashMap) obj;
        StringBuilder sb = new StringBuilder();
        sb.append("<request");
        if (hashMap.containsKey("local_version")) {
            sb.append(" local_version=\"" + hashMap.get("local_version") + "\" ");
            hashMap.remove("local_version");
        }
        sb.append(">");
        for (String str : hashMap.keySet()) {
            sb.append("<").append(str).append(">");
            sb.append(hashMap.get(str));
            sb.append("</").append(str).append(">");
        }
        sb.append("</request>");
        String sb2 = sb.toString();
        BaseUtils.D(DBUtil.TABLE_PAY, sb2);
        return sb2;
    }
}
