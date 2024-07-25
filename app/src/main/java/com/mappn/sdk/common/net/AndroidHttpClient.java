package com.mappn.sdk.common.net;

import android.content.Context;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public final class AndroidHttpClient implements HttpClient {
    public static long DEFAULT_SYNC_MIN_GZIP_BYTES = 256;
    private static final HttpRequestInterceptor b = new a();
    private final DefaultHttpClient c;
    private volatile d e;
    private boolean a = false;
    private RuntimeException d = new IllegalStateException("AndroidHttpClient created and never closed");

    /* loaded from: classes.dex */
    public class MySSLSocketFactory extends SSLSocketFactory {
        private SSLContext a;

        public MySSLSocketFactory(KeyStore keyStore) {
            super(keyStore);
            this.a = SSLContext.getInstance("TLS");
            this.a.init(null, new TrustManager[]{new e(this)}, null);
        }

        @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.SocketFactory
        public Socket createSocket() {
            return this.a.getSocketFactory().createSocket();
        }

        @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.LayeredSocketFactory
        public Socket createSocket(Socket socket, String str, int i, boolean z) {
            return this.a.getSocketFactory().createSocket(socket, str, i, z);
        }
    }

    private AndroidHttpClient(ClientConnectionManager clientConnectionManager, HttpParams httpParams) {
        this.c = new b(this, clientConnectionManager, httpParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00a6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static /* synthetic */ java.lang.String a(org.apache.http.client.methods.HttpUriRequest r7, boolean r8) {
        /*
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r0 = "curl "
            r2.append(r0)
            org.apache.http.Header[] r1 = r7.getAllHeaders()
            int r3 = r1.length
            r0 = 0
        L10:
            if (r0 >= r3) goto L44
            r4 = r1[r0]
            java.lang.String r5 = r4.getName()
            java.lang.String r6 = "Authorization"
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L41
            java.lang.String r5 = r4.getName()
            java.lang.String r6 = "Cookie"
            boolean r5 = r5.equals(r6)
            if (r5 != 0) goto L41
            java.lang.String r5 = "--header \""
            r2.append(r5)
            java.lang.String r4 = r4.toString()
            java.lang.String r4 = r4.trim()
            r2.append(r4)
            java.lang.String r4 = "\" "
            r2.append(r4)
        L41:
            int r0 = r0 + 1
            goto L10
        L44:
            java.net.URI r1 = r7.getURI()
            boolean r0 = r7 instanceof org.apache.http.impl.client.RequestWrapper
            if (r0 == 0) goto Lac
            r0 = r7
            org.apache.http.impl.client.RequestWrapper r0 = (org.apache.http.impl.client.RequestWrapper) r0
            org.apache.http.HttpRequest r0 = r0.getOriginal()
            boolean r3 = r0 instanceof org.apache.http.client.methods.HttpUriRequest
            if (r3 == 0) goto Lac
            org.apache.http.client.methods.HttpUriRequest r0 = (org.apache.http.client.methods.HttpUriRequest) r0
            java.net.URI r0 = r0.getURI()
        L5d:
            java.lang.String r1 = "\""
            r2.append(r1)
            r2.append(r0)
            java.lang.String r0 = "\""
            r2.append(r0)
            boolean r0 = r7 instanceof org.apache.http.HttpEntityEnclosingRequest
            if (r0 == 0) goto La1
            org.apache.http.HttpEntityEnclosingRequest r7 = (org.apache.http.HttpEntityEnclosingRequest) r7
            org.apache.http.HttpEntity r0 = r7.getEntity()
            if (r0 == 0) goto La1
            boolean r1 = r0.isRepeatable()
            if (r1 == 0) goto La1
            long r3 = r0.getContentLength()
            r5 = 1024(0x400, double:5.06E-321)
            int r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r1 >= 0) goto La6
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream
            r1.<init>()
            r0.writeTo(r1)
            java.lang.String r0 = r1.toString()
            java.lang.String r1 = " --data-ascii \""
            java.lang.StringBuilder r1 = r2.append(r1)
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r1 = "\""
            r0.append(r1)
        La1:
            java.lang.String r0 = r2.toString()
            return r0
        La6:
            java.lang.String r0 = " [TOO MUCH DATA TO INCLUDE]"
            r2.append(r0)
            goto La1
        Lac:
            r0 = r1
            goto L5d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.common.net.AndroidHttpClient.a(org.apache.http.client.methods.HttpUriRequest, boolean):java.lang.String");
    }

    public static AbstractHttpEntity getCompressedEntity(InputStream inputStream) {
        byte[] bArr = new byte[4096];
        int read = inputStream.read(bArr);
        if (read < getMinGzipSize()) {
            byte[] bArr2 = new byte[read];
            System.arraycopy(bArr, 0, bArr2, 0, read);
            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bArr2);
            inputStream.close();
            return byteArrayEntity;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        do {
            gZIPOutputStream.write(bArr, 0, read);
            read = inputStream.read(bArr);
        } while (read != -1);
        inputStream.close();
        gZIPOutputStream.close();
        ByteArrayEntity byteArrayEntity2 = new ByteArrayEntity(byteArrayOutputStream.toByteArray());
        byteArrayEntity2.setContentEncoding("gzip");
        return byteArrayEntity2;
    }

    public static AbstractHttpEntity getCompressedEntity(byte[] bArr) {
        if (bArr.length < getMinGzipSize()) {
            return new ByteArrayEntity(bArr);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gZIPOutputStream.write(bArr);
        gZIPOutputStream.close();
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(byteArrayOutputStream.toByteArray());
        byteArrayEntity.setContentEncoding("gzip");
        return byteArrayEntity;
    }

    public static long getMinGzipSize() {
        return DEFAULT_SYNC_MIN_GZIP_BYTES;
    }

    public static InputStream getUngzippedContent(HttpEntity httpEntity) {
        Header contentEncoding;
        String value;
        InputStream content = httpEntity.getContent();
        if (content == null || (contentEncoding = httpEntity.getContentEncoding()) == null || (value = contentEncoding.getValue()) == null) {
            return content;
        }
        return value.contains("gzip") ? new GZIPInputStream(content) : content;
    }

    public static void modifyRequestContentType(HttpRequest httpRequest, String str) {
        httpRequest.addHeader("Content-Type", str);
    }

    public static void modifyRequestToAcceptGzipResponse(HttpRequest httpRequest) {
        httpRequest.addHeader("Accept-Encoding", "gzip");
    }

    public static AndroidHttpClient newInstance(String str) {
        return newInstance(str, null);
    }

    public static AndroidHttpClient newInstance(String str, Context context) {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 20000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 20000);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 60);
        ConnPerRouteBean connPerRouteBean = new ConnPerRouteBean(20);
        connPerRouteBean.setMaxForRoute(new HttpRoute(new HttpHost("locahost", 80)), 20);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, connPerRouteBean);
        HttpClientParams.setRedirecting(basicHttpParams, false);
        HttpProtocolParams.setUserAgent(basicHttpParams, str);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            MySSLSocketFactory mySSLSocketFactory = new MySSLSocketFactory(keyStore);
            mySSLSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            schemeRegistry.register(new Scheme("https", mySSLSocketFactory, 443));
            return new AndroidHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (KeyManagementException e2) {
            e2.printStackTrace();
            return null;
        } catch (KeyStoreException e3) {
            e3.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e4) {
            e4.printStackTrace();
            return null;
        } catch (UnrecoverableKeyException e5) {
            e5.printStackTrace();
            return null;
        } catch (CertificateException e6) {
            e6.printStackTrace();
            return null;
        }
    }

    public final void addRequestInterceptor(HttpRequestInterceptor httpRequestInterceptor) {
        if (httpRequestInterceptor == null) {
            return;
        }
        this.c.addRequestInterceptor(httpRequestInterceptor, this.c.getRequestInterceptorCount());
    }

    public final void addResponseInterceptor(HttpResponseInterceptor httpResponseInterceptor) {
        if (httpResponseInterceptor == null) {
            return;
        }
        this.c.addResponseInterceptor(httpResponseInterceptor, this.c.getResponseInterceptorCount());
    }

    public final void close() {
        if (this.d != null) {
            getConnectionManager().shutdown();
            this.d = null;
        }
    }

    public final void disableCurlLogging() {
        this.e = null;
    }

    public final void enableCurlLogging(String str, int i) {
        if (str == null) {
            throw new NullPointerException("name");
        }
        if (i < 2 || i > 7) {
            throw new IllegalArgumentException("Level is out of range [2..7]");
        }
        this.e = new d(str, i, (byte) 0);
    }

    @Override // org.apache.http.client.HttpClient
    public final Object execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler responseHandler) {
        return this.c.execute(httpHost, httpRequest, responseHandler);
    }

    @Override // org.apache.http.client.HttpClient
    public final Object execute(HttpHost httpHost, HttpRequest httpRequest, ResponseHandler responseHandler, HttpContext httpContext) {
        return this.c.execute(httpHost, httpRequest, responseHandler, httpContext);
    }

    @Override // org.apache.http.client.HttpClient
    public final Object execute(HttpUriRequest httpUriRequest, ResponseHandler responseHandler) {
        return this.c.execute(httpUriRequest, responseHandler);
    }

    @Override // org.apache.http.client.HttpClient
    public final Object execute(HttpUriRequest httpUriRequest, ResponseHandler responseHandler, HttpContext httpContext) {
        return this.c.execute(httpUriRequest, responseHandler, httpContext);
    }

    @Override // org.apache.http.client.HttpClient
    public final HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest) {
        return this.c.execute(httpHost, httpRequest);
    }

    @Override // org.apache.http.client.HttpClient
    public final HttpResponse execute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) {
        return this.c.execute(httpHost, httpRequest, httpContext);
    }

    @Override // org.apache.http.client.HttpClient
    public final HttpResponse execute(HttpUriRequest httpUriRequest) {
        return this.c.execute(httpUriRequest);
    }

    @Override // org.apache.http.client.HttpClient
    public final HttpResponse execute(HttpUriRequest httpUriRequest, HttpContext httpContext) {
        return this.c.execute(httpUriRequest, httpContext);
    }

    protected final void finalize() {
        super.finalize();
        if (this.d != null) {
            Log.e("AndroidHttpClient", "Leak found", this.d);
            this.d = null;
        }
    }

    @Override // org.apache.http.client.HttpClient
    public final ClientConnectionManager getConnectionManager() {
        return this.c.getConnectionManager();
    }

    @Override // org.apache.http.client.HttpClient
    public final HttpParams getParams() {
        return this.c.getParams();
    }

    public final boolean isLoadOwnCookies() {
        return this.a;
    }

    public final void loadCookies(CookieStore cookieStore) {
        this.a = true;
        this.c.setCookieStore(cookieStore);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void removeRequestInterceptor(HttpRequestInterceptor httpRequestInterceptor) {
        if (httpRequestInterceptor == null) {
            return;
        }
        this.c.removeRequestInterceptorByClass(httpRequestInterceptor.getClass());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void removeResponseInterceptor(HttpResponseInterceptor httpResponseInterceptor) {
        if (httpResponseInterceptor == null) {
            return;
        }
        this.c.removeResponseInterceptorByClass(httpResponseInterceptor.getClass());
    }

    public final void useDefaultConnection() {
        this.c.getParams().removeParameter("http.route.default-proxy");
    }

    public final void useProxyConnection(HttpHost httpHost) {
        this.c.getParams().setParameter("http.route.default-proxy", httpHost);
    }
}
