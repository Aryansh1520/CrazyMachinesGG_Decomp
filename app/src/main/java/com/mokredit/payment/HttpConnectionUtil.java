package com.mokredit.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;

/* loaded from: classes.dex */
public class HttpConnectionUtil {
    private static HttpClient a;

    static {
        a = null;
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(basicHttpParams, "UTF-8");
        HttpProtocolParams.setUseExpectContinue(basicHttpParams, true);
        HttpProtocolParams.setUserAgent(basicHttpParams, "mokredit mobile");
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
        a = defaultHttpClient;
        defaultHttpClient.setRedirectHandler(new a());
    }

    public static String getResponse(String str) {
        HttpResponse execute;
        int statusCode;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            execute = a.execute(new HttpPost(str));
            statusCode = execute.getStatusLine().getStatusCode();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        if (statusCode == 301 || statusCode == 302 || statusCode == 303 || statusCode == 307) {
            throw new Exception("Redirect:" + execute.getLastHeader("Location").getValue());
        }
        HttpEntity entity = execute.getEntity();
        if (entity != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuffer.append(String.valueOf(readLine) + "\n");
            }
        }
        return stringBuffer.toString();
    }
}
