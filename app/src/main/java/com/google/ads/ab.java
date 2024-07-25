package com.google.ads;

import android.content.Context;
import com.google.ads.util.AdUtil;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: classes.dex */
public class ab implements Runnable {
    private Context a;
    private String b;

    public ab(String str, Context context) {
        this.b = str;
        this.a = context;
    }

    protected HttpURLConnection a(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            com.google.ads.util.b.a("Pinging URL: " + this.b);
            HttpURLConnection a = a(new URL(this.b));
            try {
                AdUtil.a(a, this.a);
                a.setInstanceFollowRedirects(true);
                a.connect();
                int responseCode = a.getResponseCode();
                if (responseCode < 200 || responseCode >= 300) {
                    com.google.ads.util.b.e("Did not receive 2XX (got " + responseCode + ") from pinging URL: " + this.b);
                }
            } finally {
                a.disconnect();
            }
        } catch (IOException e) {
            com.google.ads.util.b.d("Unable to ping the URL: " + this.b, e);
        }
    }
}
