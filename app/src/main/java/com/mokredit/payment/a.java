package com.mokredit.payment;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.client.RedirectHandler;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
final class a implements RedirectHandler {
    @Override // org.apache.http.client.RedirectHandler
    public final URI getLocationURI(HttpResponse httpResponse, HttpContext httpContext) {
        return null;
    }

    @Override // org.apache.http.client.RedirectHandler
    public final boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext) {
        return false;
    }
}
