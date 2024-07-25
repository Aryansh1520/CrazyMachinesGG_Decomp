package com.chartboost.sdk.Networking;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;
import com.chartboost.sdk.Libraries.CBCrypto;
import com.chartboost.sdk.Libraries.CBUtility;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CBAPIRequest {
    public static final String CB_PARAM_AD_ID = "ad_id";
    private static final String CB_PARAM_APP = "app";
    public static final String CB_PARAM_CACHE = "cache";
    private static final String CB_PARAM_COUNTRY = "country";
    private static final String CB_PARAM_DEVICE_TYPE = "device_type";
    private static final String CB_PARAM_HEIGHT = "h";
    private static final String CB_PARAM_IDENTITY = "identity";
    private static final String CB_PARAM_LANGUAGE = "language";
    private static final String CB_PARAM_MODEL = "model";
    private static final String CB_PARAM_OS = "os";
    private static final String CB_PARAM_SCALE = "scale";
    private static final String CB_PARAM_SDK = "sdk";
    private static final String CB_PARAM_TIMESTAMP = "timestamp";
    private static final String CB_PARAM_VERSION = "bundle";
    private static final String CB_PARAM_WIDTH = "w";
    private static final String kCBAppHeaderKey = "X-Chartboost-App";
    private static final String kCBSignatureHeaderKey = "X-Chartboost-Signature";
    public String action;
    public JSONObject body;
    public CBAPIResponseBlock contextBlock;
    private Object contextInfoObject;
    public String controller;
    public boolean ensureDelivery;
    public Map<String, String> headers;
    public List<String> params;
    public Map<String, String> query;
    private int displayWidth = 0;
    private int displayHeight = 0;
    public JSONObject jsonResult = null;
    public String method = "GET";

    /* loaded from: classes.dex */
    public interface CBAPIResponseBlock {
        void execute(JSONObject jSONObject);
    }

    public CBAPIRequest(String controller, String action) {
        this.controller = controller;
        this.action = action;
    }

    public void appendBodyArgument(String key, String value) {
        if (this.body == null) {
            this.body = new JSONObject();
            this.method = "POST";
        }
        try {
            this.body.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void appendHeaderArgument(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap();
        }
        this.headers.put(key, value);
    }

    public void appendQueryArgument(String key, String value) {
        if (this.query == null) {
            this.query = new HashMap();
        }
        this.query.put(key, value);
    }

    public void measureContainer() {
        int statusBarOffset = 0;
        Context context = Chartboost.sharedChartboost().getContext();
        if (Build.VERSION.SDK_INT <= 8) {
            Rect rectangle = new Rect();
            Window window = ((Activity) context).getWindow();
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            statusBarOffset = rectangle.top;
        }
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        this.displayWidth = display.getWidth();
        this.displayHeight = display.getHeight() - statusBarOffset;
    }

    public void appendDeviceInfoParams() {
        Context context = Chartboost.sharedChartboost().getContext();
        appendBodyArgument(CB_PARAM_APP, Chartboost.sharedChartboost().appId);
        if (Build.PRODUCT.equals(CB_PARAM_SDK)) {
            appendBodyArgument(CB_PARAM_MODEL, "Android Simulator");
            appendBodyArgument(CB_PARAM_IDENTITY, CBUtility.AUID_STATIC_EMULATOR);
        } else {
            appendBodyArgument(CB_PARAM_MODEL, Build.MODEL);
            appendBodyArgument(CB_PARAM_IDENTITY, CBUtility.getIdentity());
        }
        appendBodyArgument(CB_PARAM_DEVICE_TYPE, String.valueOf(Build.MANUFACTURER) + " " + Build.MODEL);
        appendBodyArgument(CB_PARAM_OS, "Android " + Build.VERSION.RELEASE);
        appendBodyArgument(CB_PARAM_COUNTRY, Locale.getDefault().getCountry());
        appendBodyArgument(CB_PARAM_LANGUAGE, Locale.getDefault().getDisplayLanguage(Locale.US));
        appendBodyArgument(CB_PARAM_SDK, CBConstants.kCBAPIVersion);
        appendBodyArgument(CB_PARAM_TIMESTAMP, new StringBuilder(String.valueOf(new Date().getTime())).toString());
        measureContainer();
        appendBodyArgument(CB_PARAM_WIDTH, new StringBuilder().append(this.displayWidth).toString());
        appendBodyArgument(CB_PARAM_HEIGHT, new StringBuilder().append(this.displayHeight).toString());
        appendBodyArgument(CB_PARAM_SCALE, new StringBuilder().append(context.getResources().getDisplayMetrics().density).toString());
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 128);
            String version = manager.versionName;
            appendBodyArgument(CB_PARAM_VERSION, version);
        } catch (Exception e) {
        }
    }

    public void sign(String appId, String appSignature) {
        String description = String.valueOf(this.method) + " " + uri() + "\n" + appSignature + "\n" + serializedBody();
        String signature = CBCrypto.hexRepresentation(CBCrypto.sha1(description.getBytes()));
        appendHeaderArgument(kCBAppHeaderKey, appId);
        appendHeaderArgument(kCBSignatureHeaderKey, signature);
    }

    public String uri() {
        String uri = "/" + this.controller + "/" + this.action + CBUtility.mapToString(this.query);
        return uri;
    }

    public String serializedBody() {
        return this.body.toString();
    }

    public String getController() {
        return this.controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public JSONObject getBody() {
        return this.body;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public Map<String, String> getQuery() {
        return this.query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public List<String> getParams() {
        return this.params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public Object getContextInfoObject() {
        return this.contextInfoObject;
    }

    public void setContextInfoObject(Object contextInfoObject) {
        this.contextInfoObject = contextInfoObject;
    }

    public static CBAPIRequest deserialize(JSONObject obj) {
        try {
            CBAPIRequest request = new CBAPIRequest(obj.getString("controller"), obj.getString("action"));
            request.params = CBUtility.JSONArrayToList(obj.optJSONArray("params"));
            request.query = CBUtility.JSONObjectToMap(obj.optJSONObject("query"));
            request.body = obj.optJSONObject("body");
            request.ensureDelivery = obj.getBoolean("ensureDelivery");
            request.headers = CBUtility.JSONObjectToMap(obj.optJSONObject("headers"));
            return request;
        } catch (Exception e) {
            Log.w(Chartboost.TAG, "Unable to deserialize failed request");
            return null;
        }
    }

    public JSONObject serialize() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("controller", this.controller);
            obj.put("action", this.action);
            obj.put("params", CBUtility.listToJSONArray(this.params));
            obj.put("query", CBUtility.mapToJSONObject(this.query));
            obj.put("body", this.body);
            obj.put("ensureDelivery", this.ensureDelivery);
            obj.put("headers", CBUtility.mapToJSONObject(this.headers));
            return obj;
        } catch (Exception e) {
            Log.w(Chartboost.TAG, "Unable to serialize failed request");
            return null;
        }
    }
}
