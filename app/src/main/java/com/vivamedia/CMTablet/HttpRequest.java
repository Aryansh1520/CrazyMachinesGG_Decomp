package com.vivamedia.CMTablet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

/* loaded from: classes.dex */
public class HttpRequest {
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final int HTTPERROR_CLIENTPROTOCOL_EXCEPTION = 300;
    public static final int HTTPERROR_IO_BINDEXCEPTION = 407;
    public static final int HTTPERROR_IO_CONNECTEXCEPTION = 401;
    public static final int HTTPERROR_IO_CONNECTIONCLOSEDEXCEPTION = 404;
    public static final int HTTPERROR_IO_CONNECTTIMEOUTEXCEPTION = 402;
    public static final int HTTPERROR_IO_EXCEPTION = 400;
    public static final int HTTPERROR_IO_HTTPRESPONSEEXCEPTION = 403;
    public static final int HTTPERROR_IO_MALFORMEDURLEXCEPTION = 405;
    public static final int HTTPERROR_IO_SOCKETTIMEOUTEXCEPTION = 408;
    public static final int HTTPERROR_IO_UNKNOWNHOSTEXCEPTION = 406;
    public static final int HTTPERROR_UPDATING_REQUESTS = 500;
    public static final int HTTP_OK = 1;
    public static final int HTTP_WORKING = 0;
    private byte[] response;
    private int state = 0;
    private int ID = -1;
    private Thread thread = null;
    private int mime = 0;

    public int getID() {
        return this.ID;
    }

    public int getMime() {
        return this.mime;
    }

    public byte[] getResponse() {
        return this.response;
    }

    public int getState() {
        return this.state;
    }

    public boolean isError() {
        return this.state > 1;
    }

    public boolean isOK() {
        return this.state == 1;
    }

    public byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0;
        while (true) {
            int n = input.read(buffer);
            if (-1 != n) {
                output.write(buffer, 0, n);
                count += n;
            } else {
                return count;
            }
        }
    }

    public void startDataPost(final byte[] byPost, final byte[] pData, final int nDataSize, int nID) {
        this.ID = nID;
        this.thread = new Thread(new Runnable() { // from class: com.vivamedia.CMTablet.HttpRequest.1
            @Override // java.lang.Runnable
            public void run() {
                BasicHttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setSoTimeout(httpParameters, 600000);
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParameters);
                String strGet = new String(byPost);
                String strURL = String.valueOf("http://cm2mobile.viva-media.com/server.php") + "?" + strGet;
                HttpPost httppost = new HttpPost(strURL);
                try {
                    ByteArrayInputStream content = new ByteArrayInputStream(pData);
                    BasicHttpEntity entity = new BasicHttpEntity();
                    entity.setContent(content);
                    entity.setContentLength(nDataSize);
                    httppost.addHeader("Content-Type", "application/octet-stream");
                    httppost.setEntity(entity);
                    HttpResponse httpResponse = defaultHttpClient.execute(httppost);
                    HttpRequest.this.mime = -1;
                    String strResType = new StringBuilder().append(httpResponse.getEntity().getContentType()).toString();
                    if (strResType.equals("Content-Type: text/plain")) {
                        HttpRequest.this.mime = 1;
                    } else if (strResType.equals("Content-Type: text/html")) {
                        HttpRequest.this.mime = 2;
                    } else if (strResType.equals("Content-Type: application/octet-stream")) {
                        HttpRequest.this.mime = 3;
                    }
                    HttpRequest.this.response = HttpRequest.this.toByteArray(httpResponse.getEntity().getContent());
                    HttpRequest.this.state = 1;
                } catch (BindException e) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_BINDEXCEPTION;
                    e.printStackTrace();
                } catch (ConnectException e2) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_CONNECTEXCEPTION;
                    e2.printStackTrace();
                } catch (MalformedURLException e3) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_MALFORMEDURLEXCEPTION;
                    e3.printStackTrace();
                } catch (SocketTimeoutException e4) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_SOCKETTIMEOUTEXCEPTION;
                    e4.printStackTrace();
                } catch (UnknownHostException e5) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_UNKNOWNHOSTEXCEPTION;
                    e5.printStackTrace();
                } catch (ConnectionClosedException e6) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_CONNECTIONCLOSEDEXCEPTION;
                    e6.printStackTrace();
                } catch (HttpResponseException e7) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_HTTPRESPONSEEXCEPTION;
                    e7.printStackTrace();
                } catch (ClientProtocolException e8) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_CLIENTPROTOCOL_EXCEPTION;
                    e8.printStackTrace();
                } catch (ConnectTimeoutException e9) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_CONNECTTIMEOUTEXCEPTION;
                    e9.printStackTrace();
                } catch (IOException e10) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_EXCEPTION;
                    e10.printStackTrace();
                }
            }
        });
        if (this.thread != null) {
            this.thread.start();
        }
    }

    public void startCommandPost(final byte[] byPost, int nID) {
        this.ID = nID;
        this.thread = new Thread(new Runnable() { // from class: com.vivamedia.CMTablet.HttpRequest.2
            @Override // java.lang.Runnable
            public void run() {
                BasicHttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setSoTimeout(httpParameters, 600000);
                DefaultHttpClient defaultHttpClient = new DefaultHttpClient(httpParameters);
                HttpPost httppost = new HttpPost("http://cm2mobile.viva-media.com/server.php");
                try {
                    ByteArrayInputStream content = new ByteArrayInputStream(byPost);
                    BasicHttpEntity entity = new BasicHttpEntity();
                    entity.setContent(content);
                    entity.setContentLength(byPost.length);
                    httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
                    httppost.setEntity(entity);
                    HttpResponse httpResponse = defaultHttpClient.execute(httppost);
                    HttpRequest.this.mime = -1;
                    String strResType = new StringBuilder().append(httpResponse.getEntity().getContentType()).toString();
                    if (strResType.equals("Content-Type: text/plain")) {
                        HttpRequest.this.mime = 1;
                    } else if (strResType.equals("Content-Type: text/html")) {
                        HttpRequest.this.mime = 2;
                    } else if (strResType.equals("Content-Type: application/octet-stream")) {
                        HttpRequest.this.mime = 3;
                    }
                    HttpRequest.this.response = HttpRequest.this.toByteArray(httpResponse.getEntity().getContent());
                    HttpRequest.this.state = 1;
                } catch (BindException e) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_BINDEXCEPTION;
                    e.printStackTrace();
                } catch (ConnectException e2) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_CONNECTEXCEPTION;
                    e2.printStackTrace();
                } catch (MalformedURLException e3) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_MALFORMEDURLEXCEPTION;
                    e3.printStackTrace();
                } catch (SocketTimeoutException e4) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_SOCKETTIMEOUTEXCEPTION;
                    e4.printStackTrace();
                } catch (UnknownHostException e5) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_UNKNOWNHOSTEXCEPTION;
                    e5.printStackTrace();
                } catch (ConnectionClosedException e6) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_CONNECTIONCLOSEDEXCEPTION;
                    e6.printStackTrace();
                } catch (HttpResponseException e7) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_HTTPRESPONSEEXCEPTION;
                    e7.printStackTrace();
                } catch (ClientProtocolException e8) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_CLIENTPROTOCOL_EXCEPTION;
                    e8.printStackTrace();
                } catch (ConnectTimeoutException e9) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_CONNECTTIMEOUTEXCEPTION;
                    e9.printStackTrace();
                } catch (IOException e10) {
                    HttpRequest.this.state = HttpRequest.HTTPERROR_IO_EXCEPTION;
                    e10.printStackTrace();
                }
            }
        });
        if (this.thread != null) {
            this.thread.start();
        }
    }

    public void stop() {
        if (this.thread != null) {
            this.thread.interrupt();
        }
    }
}
