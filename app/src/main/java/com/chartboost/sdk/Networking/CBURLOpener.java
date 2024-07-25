package com.chartboost.sdk.Networking;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/* loaded from: classes.dex */
public class CBURLOpener {
    public CBUrlOpenerDelegate delegate;

    /* loaded from: classes.dex */
    public interface CBUrlOpenerDelegate {
        void urlWillOpen(String str);
    }

    public static CBURLOpener openerWithDelegate(CBUrlOpenerDelegate _delegate) {
        CBURLOpener opener = new CBURLOpener();
        opener.delegate = _delegate;
        return opener;
    }

    public void open(String url, Context context) {
        try {
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            if (scheme != null) {
                if (!scheme.equals("http") && !scheme.equals("https")) {
                    doOpenUrl(url, context);
                    return;
                }
                HttpURLConnection urlConnection = null;
                try {
                    try {
                        URL httpUrl = new URL(url);
                        urlConnection = (HttpURLConnection) httpUrl.openConnection();
                        urlConnection.setInstanceFollowRedirects(false);
                        String secondURL = urlConnection.getHeaderField("Location");
                        if (secondURL == null) {
                            secondURL = url;
                        }
                        doOpenUrl(secondURL, context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        } catch (URISyntaxException e2) {
        }
    }

    private void doOpenUrl(String url, Context context) {
        if (this.delegate != null) {
            this.delegate.urlWillOpen(url);
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
