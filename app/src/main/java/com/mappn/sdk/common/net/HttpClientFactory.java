package com.mappn.sdk.common.net;

import java.util.WeakHashMap;
import org.apache.http.params.HttpProtocolParams;

/* loaded from: classes.dex */
public class HttpClientFactory {
    private static HttpClientFactory a;
    private WeakHashMap b;

    private HttpClientFactory() {
        synchronized (this) {
            this.b = new WeakHashMap(2);
        }
    }

    public static HttpClientFactory get() {
        if (a == null) {
            a = new HttpClientFactory();
        }
        return a;
    }

    public synchronized void close() {
        AndroidHttpClient androidHttpClient;
        if (this.b.containsKey("sdk") && (androidHttpClient = (AndroidHttpClient) this.b.get("sdk")) != null) {
            androidHttpClient.close();
        }
        this.b.clear();
        a = null;
    }

    public synchronized AndroidHttpClient getSDKHttpClient(String str) {
        AndroidHttpClient newInstance;
        if (!this.b.containsKey("sdk") || (newInstance = (AndroidHttpClient) this.b.get("sdk")) == null) {
            newInstance = AndroidHttpClient.newInstance(str);
            this.b.put("sdk", newInstance);
        }
        return newInstance;
    }

    public void updateUserAgent(String str) {
        AndroidHttpClient androidHttpClient = (AndroidHttpClient) this.b.get("sdk");
        if (androidHttpClient != null) {
            HttpProtocolParams.setUserAgent(androidHttpClient.getParams(), str);
        }
    }
}
