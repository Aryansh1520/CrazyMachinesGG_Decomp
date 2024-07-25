package com.mappn.sdk.pay.chargement.alipay;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class NetworkManager {
    private int a = 30000;
    private int b = 30000;
    private Proxy c = null;
    private Context d;

    public NetworkManager(Context context) {
        this.d = context;
        HttpsURLConnection.setDefaultHostnameVerifier(new h(this));
    }

    public String SendAndWaitResponse(String str, String str2) {
        HttpURLConnection httpURLConnection;
        IOException e;
        String str3;
        detectProxy();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("requestData", str));
        try {
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(arrayList, "utf-8");
            URL url = new URL(str2);
            httpURLConnection = this.c != null ? (HttpURLConnection) url.openConnection(this.c) : (HttpURLConnection) url.openConnection();
            try {
                try {
                    httpURLConnection.setConnectTimeout(this.a);
                    httpURLConnection.setReadTimeout(this.b);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.addRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
                    httpURLConnection.connect();
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    urlEncodedFormEntity.writeTo(outputStream);
                    outputStream.flush();
                    str3 = BaseHelper.convertStreamToString(httpURLConnection.getInputStream());
                } catch (IOException e2) {
                    str3 = null;
                    e = e2;
                }
                try {
                    BaseHelper.log("NetworkManager", "response " + str3);
                    httpURLConnection.disconnect();
                } catch (IOException e3) {
                    e = e3;
                    e.printStackTrace();
                    httpURLConnection.disconnect();
                    return str3;
                }
            } catch (Throwable th) {
                th = th;
                httpURLConnection.disconnect();
                throw th;
            }
        } catch (IOException e4) {
            httpURLConnection = null;
            e = e4;
            str3 = null;
        } catch (Throwable th2) {
            th = th2;
            httpURLConnection = null;
            httpURLConnection.disconnect();
            throw th;
        }
        return str3;
    }

    public void detectProxy() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.d.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.getType() == 0) {
            String defaultHost = android.net.Proxy.getDefaultHost();
            int defaultPort = android.net.Proxy.getDefaultPort();
            if (defaultHost != null) {
                this.c = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(defaultHost, defaultPort));
            }
        }
    }

    public boolean urlDownloadToFile(Context context, String str, String str2) {
        detectProxy();
        try {
            URL url = new URL(str);
            HttpURLConnection httpURLConnection = this.c != null ? (HttpURLConnection) url.openConnection(this.c) : (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(this.a);
            httpURLConnection.setReadTimeout(this.b);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            File file = new File(str2);
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    fileOutputStream.close();
                    inputStream.close();
                    return true;
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
