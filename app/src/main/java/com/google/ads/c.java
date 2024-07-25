package com.google.ads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class c {
    private static final Map<String, AdSize> a = Collections.unmodifiableMap(new HashMap<String, AdSize>() { // from class: com.google.ads.c.1
        {
            put("banner", AdSize.BANNER);
            put("mrec", AdSize.IAB_MRECT);
            put("fullbanner", AdSize.IAB_BANNER);
            put("leaderboard", AdSize.IAB_LEADERBOARD);
            put("skyscraper", AdSize.IAB_WIDE_SKYSCRAPER);
        }
    });
    private final String b;
    private final String c;
    private final List<a> d;
    private final Integer e;
    private final Integer f;
    private final List<String> g;
    private final List<String> h;
    private final List<String> i;

    public static c a(String str) throws JSONException {
        List<String> list;
        List<String> list2;
        List<String> list3;
        Integer num;
        Integer num2;
        JSONObject jSONObject = new JSONObject(str);
        String string = jSONObject.getString("qdata");
        String string2 = jSONObject.has("ad_type") ? jSONObject.getString("ad_type") : null;
        JSONArray jSONArray = jSONObject.getJSONArray("ad_networks");
        ArrayList arrayList = new ArrayList(jSONArray.length());
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(a(jSONArray.getJSONObject(i)));
        }
        JSONObject optJSONObject = jSONObject.optJSONObject("settings");
        if (optJSONObject != null) {
            num2 = optJSONObject.has("refresh") ? Integer.valueOf(optJSONObject.getInt("refresh")) : null;
            Integer valueOf = optJSONObject.has("ad_network_timeout_millis") ? Integer.valueOf(optJSONObject.getInt("ad_network_timeout_millis")) : null;
            list2 = a(optJSONObject, "imp_urls");
            list3 = a(optJSONObject, "click_urls");
            list = a(optJSONObject, "nofill_urls");
            num = valueOf;
        } else {
            list = null;
            list2 = null;
            list3 = null;
            num = null;
            num2 = null;
        }
        return new c(string, string2, arrayList, num2, num, list2, list3, list);
    }

    public boolean a() {
        return this.f != null;
    }

    public int b() {
        return this.f.intValue();
    }

    public String c() {
        return this.b;
    }

    public boolean d() {
        return this.e != null;
    }

    public int e() {
        return this.e.intValue();
    }

    public List<a> f() {
        return this.d;
    }

    public List<String> g() {
        return this.g;
    }

    public List<String> h() {
        return this.h;
    }

    public List<String> i() {
        return this.i;
    }

    private static a a(JSONObject jSONObject) throws JSONException {
        String string = jSONObject.getString("id");
        String optString = jSONObject.optString("allocation_id", null);
        JSONArray jSONArray = jSONObject.getJSONArray("adapters");
        ArrayList arrayList = new ArrayList(jSONArray.length());
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(jSONArray.getString(i));
        }
        JSONObject optJSONObject = jSONObject.optJSONObject("data");
        HashMap hashMap = new HashMap(0);
        if (optJSONObject != null) {
            HashMap hashMap2 = new HashMap(optJSONObject.length());
            Iterator<String> keys = optJSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                hashMap2.put(next, optJSONObject.getString(next));
            }
            hashMap = hashMap2;
        }
        return new a(optString, string, arrayList, hashMap);
    }

    public com.google.ads.internal.h j() {
        if (this.c == null) {
            return null;
        }
        if ("interstitial".equals(this.c)) {
            return com.google.ads.internal.h.a;
        }
        AdSize adSize = a.get(this.c);
        if (adSize != null) {
            return com.google.ads.internal.h.a(adSize);
        }
        return null;
    }

    private static List<String> a(JSONObject jSONObject, String str) throws JSONException {
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList(optJSONArray.length());
        for (int i = 0; i < optJSONArray.length(); i++) {
            arrayList.add(optJSONArray.getString(i));
        }
        return arrayList;
    }

    private c(String str, String str2, List<a> list, Integer num, Integer num2, List<String> list2, List<String> list3, List<String> list4) {
        com.google.ads.util.a.a(str);
        this.b = str;
        this.c = str2;
        this.d = list;
        this.e = num;
        this.f = num2;
        this.g = list2;
        this.h = list3;
        this.i = list4;
    }
}
