package com.chartboost.sdk.Networking;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;
import com.chartboost.sdk.Libraries.CBUtility;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes.dex */
public class CBAPIConnection {
    public static final int DEFAULT_CONNECT_TIMEOUT = 30000;
    public static final int DEFAULT_READ_TIMEOUT = 30000;
    public static final int MIN_TIMEOUT = 10000;
    private static final String kCBQueuedRequestPrefix = "CBQueuedRequests-";
    public SparseArray<CBAPIActiveConnection> activeConnections;
    public Object data;
    public CBAPIConnectionDelegate delegate;
    public String endpoint;
    public String identifier;
    private static int counter = 0;
    private static HttpClient sharedHttpClient = null;
    public int requestIdMax = 1;
    public String loadingMessage = "Loading...";

    /* loaded from: classes.dex */
    public interface CBAPIConnectionDelegate {
        void didFailToReceiveAPIResponseForRequest(CBAPIRequest cBAPIRequest);

        void didReceiveAPIResponse(JSONObject jSONObject, CBAPIRequest cBAPIRequest);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class CBAPIActiveConnection implements Serializable {
        private static final long serialVersionUID = 1;
        public JSONObject data;
        public CBAPIRequest request;
        public Integer tag;

        public CBAPIActiveConnection(CBAPIRequest _request, JSONObject _data) {
            this.request = _request;
            this.data = _data;
        }
    }

    public CBAPIConnection(String _endpoint, CBAPIConnectionDelegate _delegate, String _identifier) {
        this.endpoint = _endpoint == null ? CBConstants.kCBAPIEndpoint : _endpoint;
        this.delegate = _delegate;
        this.identifier = _identifier;
        this.activeConnections = new SparseArray<>();
    }

    public void sendRequest(CBAPIRequest request) {
        if (!CBReachability.reachabilityForInternetConnection()) {
            didFailForRequest(request);
            return;
        }
        int requestId = this.requestIdMax;
        this.requestIdMax = requestId + 1;
        CBAPIActiveConnection activeConnection = new CBAPIActiveConnection(request, null);
        activeConnection.tag = Integer.valueOf(requestId);
        this.activeConnections.put(requestId, activeConnection);
        AsyncURLConnection urlConnection = new AsyncURLConnection();
        urlConnection.execute(activeConnection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void didFailForRequest(CBAPIRequest request) {
        JSONArray array;
        if (request.ensureDelivery && this.identifier != null) {
            SharedPreferences prefs = CBUtility.getPreferences();
            String key = kCBQueuedRequestPrefix + this.identifier;
            try {
                JSONObject archivedRequest = request.serialize();
                if (archivedRequest != null) {
                    String queue = prefs.getString(key, null);
                    if (queue != null) {
                        try {
                            JSONTokener tokener = new JSONTokener(queue);
                            array = new JSONArray(tokener);
                        } catch (Exception e) {
                            Log.w(Chartboost.TAG, "Failure reading saved request list");
                            array = new JSONArray();
                        }
                    } else {
                        array = new JSONArray();
                    }
                    array.put(archivedRequest);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString(key, array.toString());
                    edit.commit();
                    return;
                }
                return;
            } catch (Exception e2) {
                Log.w(Chartboost.TAG, "Unable to save failed request");
                return;
            }
        }
        if (this.delegate != null) {
            this.delegate.didFailToReceiveAPIResponseForRequest(request);
        }
    }

    public void retryDeliveries() {
        SharedPreferences prefs;
        String key;
        String queue;
        if (CBReachability.reachabilityForInternetConnection() && (queue = (prefs = CBUtility.getPreferences()).getString((key = kCBQueuedRequestPrefix + this.identifier), null)) != null) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(key, null);
            edit.commit();
            try {
                JSONTokener tokener = new JSONTokener(queue);
                JSONArray array = new JSONArray(tokener);
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        CBAPIRequest request = CBAPIRequest.deserialize(obj);
                        if (request != null) {
                            sendRequest(request);
                        }
                    } catch (Exception e) {
                        Log.w(Chartboost.TAG, "Retrying request failed");
                    }
                }
            } catch (Exception e2) {
                Log.w(Chartboost.TAG, "Retrying request list failed");
            }
        }
    }

    public static void longInfo(String tag, String str) {
        if (str.length() > 200) {
            Log.i(tag, str.substring(0, 200));
            longInfo(tag, str.substring(200));
        } else {
            Log.i(tag, str);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public class AsyncURLConnection extends AsyncTask<CBAPIActiveConnection, Void, CBAPIActiveConnection> {
        protected AsyncURLConnection() {
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public CBAPIActiveConnection doInBackground(CBAPIActiveConnection... connections) {
            int localCounter;
            CBAPIActiveConnection activeConnection = connections[0];
            CBAPIRequest request = activeConnection.request;
            String urlString = String.valueOf(CBAPIConnection.this.endpoint) + request.uri();
            HttpPost httpRequest = new HttpPost(urlString);
            httpRequest.setHeader("Content-Type", "application/json; charset=UTF-8");
            httpRequest.setHeader("Accept", "application/json; charset=UTF-8");
            httpRequest.setHeader("X-Chartboost-Client", CBConstants.kCBSDKUserAgent);
            httpRequest.setHeader("X-Chartboost-API", CBConstants.kCBAPIVersion);
            Map<String, String> headers = request.getHeaders();
            if (headers != null) {
                for (String key : headers.keySet()) {
                    httpRequest.setHeader(key, headers.get(key));
                }
            }
            synchronized (CBAPIConnection.this) {
                localCounter = CBAPIConnection.counter + 1;
                CBAPIConnection.counter = localCounter;
            }
            HttpResponse response = null;
            try {
                if (request.body != null) {
                    StringEntity se = new StringEntity(request.body.toString());
                    se.setContentType(new BasicHeader("Content-Type", "application/json"));
                    httpRequest.setEntity(se);
                } else {
                    Log.i("HTTP Request Body " + localCounter + " " + request.action, "<empty>");
                }
                HttpResponse response2 = CBAPIConnection.getSharedHttpClient().execute(httpRequest);
                int status = response2.getStatusLine().getStatusCode();
                if (status < 300 && status >= 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response2.getEntity().getContent(), "UTF-8"), 2048);
                    StringBuilder builder = new StringBuilder();
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        builder.append(line).append("\n");
                    }
                    reader.close();
                    JSONTokener tokener = new JSONTokener(builder.toString());
                    JSONObject json = new JSONObject(tokener);
                    request.jsonResult = json;
                    response2.getEntity().consumeContent();
                } else {
                    Log.w(Chartboost.TAG, "Request failed. Response body: " + response2);
                    response2.getEntity().consumeContent();
                }
            } catch (Exception e) {
                Log.e(Chartboost.TAG, "Exception on http request: " + e.getLocalizedMessage());
                if (0 != 0) {
                    try {
                        if (response.getEntity() != null) {
                            response.getEntity().consumeContent();
                        }
                    } catch (Exception e2) {
                    }
                }
            }
            return activeConnection;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(CBAPIActiveConnection activeConnection) {
            CBAPIConnection.this.activeConnections.remove(activeConnection.tag.intValue());
            if (activeConnection.request.jsonResult == null) {
                CBAPIConnection.this.didFailForRequest(activeConnection.request);
            } else if (CBAPIConnection.this.delegate != null) {
                CBAPIConnection.this.delegate.didReceiveAPIResponse(activeConnection.request.jsonResult, activeConnection.request);
            }
        }
    }

    public static HttpClient getSharedHttpClient() {
        if (sharedHttpClient != null) {
            return sharedHttpClient;
        }
        try {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory sf = new CBSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            HttpConnectionParams.setConnectionTimeout(params, Chartboost.sharedChartboost().timeout);
            HttpConnectionParams.setSoTimeout(params, Chartboost.sharedChartboost().timeout);
            sharedHttpClient = new DefaultHttpClient(ccm, params);
            return sharedHttpClient;
        } catch (Exception e) {
            try {
                HttpClient client = new DefaultHttpClient();
                ClientConnectionManager mgr = client.getConnectionManager();
                HttpParams params2 = client.getParams();
                sharedHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params2, mgr.getSchemeRegistry()), params2);
                return sharedHttpClient;
            } catch (Exception e2) {
                sharedHttpClient = new DefaultHttpClient();
                return sharedHttpClient;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class CBSSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext;

        public CBSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);
            this.sslContext = SSLContext.getInstance("TLS");
            TrustManager tm = new X509TrustManager() { // from class: com.chartboost.sdk.Networking.CBAPIConnection.CBSSLSocketFactory.1
                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override // javax.net.ssl.X509TrustManager
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            this.sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.LayeredSocketFactory
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return this.sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override // org.apache.http.conn.ssl.SSLSocketFactory, org.apache.http.conn.scheme.SocketFactory
        public Socket createSocket() throws IOException {
            return this.sslContext.getSocketFactory().createSocket();
        }
    }
}
