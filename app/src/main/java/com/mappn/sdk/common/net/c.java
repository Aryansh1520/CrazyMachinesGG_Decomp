package com.mappn.sdk.common.net;

import android.util.Log;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
final class c implements HttpRequestInterceptor {
    private /* synthetic */ AndroidHttpClient a;

    private c(AndroidHttpClient androidHttpClient) {
        this.a = androidHttpClient;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ c(AndroidHttpClient androidHttpClient, byte b) {
        this(androidHttpClient);
    }

    @Override // org.apache.http.HttpRequestInterceptor
    public final void process(HttpRequest httpRequest, HttpContext httpContext) {
        d dVar;
        boolean isLoggable;
        dVar = this.a.e;
        if (dVar != null) {
            isLoggable = Log.isLoggable(dVar.a, dVar.b);
            if (isLoggable && (httpRequest instanceof HttpUriRequest)) {
                Log.println(dVar.b, dVar.a, AndroidHttpClient.a((HttpUriRequest) httpRequest, false));
            }
        }
    }
}
