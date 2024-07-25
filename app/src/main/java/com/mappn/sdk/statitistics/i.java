package com.mappn.sdk.statitistics;

import android.content.Context;
import android.net.Proxy;
import android.text.TextUtils;

import androidx.privacysandbox.tools.core.model.Type;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
public class i {
    String a;
    String b;
    long c;
    String d;
    Map<String, Object> f = null;
    long e = System.currentTimeMillis();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long a(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 64).firstInstallTime;
        } catch (Exception e) {
            return -1L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(String str, String str2, byte[] bArr) {
        HttpURLConnection connection = null;
        try {
            // Create URL object
            URL url = new URL("http://gfanpay.tenddata.com" + str);

            // Create connection object
            connection = (HttpURLConnection) url.openConnection();

            // Handle proxy settings
            if (!TextUtils.isEmpty(Proxy.getDefaultHost())) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Proxy.getDefaultHost(), Proxy.getDefaultPort()));
                connection = (HttpURLConnection) url.openConnection();
            }

            // Set request method
            if ("POST".equals(str2)) {
                connection.setRequestMethod("POST");
            } else if ("PUT".equals(str2)) {
                connection.setRequestMethod("PUT");
            } else {
                throw new IllegalArgumentException("Unsupported HTTP method: " + str2);
            }

            // Set request headers
            String language = Locale.getDefault().getLanguage();
            if (!TextUtils.isEmpty(language)) {
                connection.setRequestProperty("Accept-Language", language);
            }
            connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
            connection.setRequestProperty("Content-Type", "application/unpack_chinar");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);

            // Send the request
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                os.write(bArr);
            }

            // Check response code
            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
