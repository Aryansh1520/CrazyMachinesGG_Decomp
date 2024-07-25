package com.mappn.sdk.common.net;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class b extends DefaultHttpClient {
    private /* synthetic */ AndroidHttpClient a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b(AndroidHttpClient androidHttpClient, ClientConnectionManager clientConnectionManager, HttpParams httpParams) {
        super(clientConnectionManager, httpParams);
        this.a = androidHttpClient;
    }

    @Override // org.apache.http.impl.client.DefaultHttpClient, org.apache.http.impl.client.AbstractHttpClient
    protected final HttpContext createHttpContext() {
        BasicHttpContext basicHttpContext = new BasicHttpContext();
        basicHttpContext.setAttribute("http.authscheme-registry", getAuthSchemes());
        basicHttpContext.setAttribute("http.cookiespec-registry", getCookieSpecs());
        basicHttpContext.setAttribute("http.auth.credentials-provider", getCredentialsProvider());
        basicHttpContext.setAttribute("http.cookie-store", getCookieStore());
        return basicHttpContext;
    }

    @Override // org.apache.http.impl.client.DefaultHttpClient, org.apache.http.impl.client.AbstractHttpClient
    protected final BasicHttpProcessor createHttpProcessor() {
        HttpRequestInterceptor httpRequestInterceptor;
        BasicHttpProcessor createHttpProcessor = super.createHttpProcessor();
        httpRequestInterceptor = AndroidHttpClient.b;
        createHttpProcessor.addRequestInterceptor(httpRequestInterceptor);
        createHttpProcessor.addRequestInterceptor(new c(this.a, (byte) 0));
        return createHttpProcessor;
    }
}
