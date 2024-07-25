package com.mappn.sdk.pay.net.chain;

import android.content.Context;
import android.text.TextUtils;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.net.chain.Handler;
import com.mappn.sdk.pay.util.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/* loaded from: classes.dex */
public class PostSmsPaymentHandler extends Handler implements ApiRequestListener {
    private static int b;
    private Handler.OnFinishListener a;
    private final Integer c;
    private File[] d;
    private int e;

    public PostSmsPaymentHandler(Context context) {
        super(context);
        this.c = 0;
        File file = new File(context.getFilesDir().getAbsolutePath() + "/");
        file.mkdir();
        this.d = file.listFiles(new c());
        this.e = 0;
    }

    public PostSmsPaymentHandler(Context context, Handler.OnFinishListener onFinishListener) {
        super(context);
        this.c = 0;
        this.a = onFinishListener;
    }

    private static String a(File file) {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                bufferedReader.close();
                return sb.toString();
            }
            sb.append(readLine);
            sb.append(System.getProperty("line.separator"));
        }
    }

    public static void init() {
        b = 0;
    }

    @Override // com.mappn.sdk.pay.net.chain.Handler
    public void handleRequest() {
        int i;
        int i2 = 1;
        switch (b) {
            case 0:
                b = 1;
                if (this.d == null || this.e >= this.d.length) {
                    return;
                }
                try {
                    String[] split = a(this.d[this.e]).split(Constants.TERM);
                    try {
                        i2 = Integer.parseInt(split[2]);
                    } catch (NumberFormatException e) {
                    }
                    try {
                        i = TextUtils.isEmpty(split[5]) ? -1 : Integer.parseInt(split[5]);
                    } catch (NumberFormatException e2) {
                        i = -1;
                    }
                    Api.postSmsPayment(this.mContext, this, split[0], split[1], i2, split[3], split[4], i);
                    return;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return;
                }
            case 1:
                new Thread(new a(this)).start();
                return;
            case 2:
                if (getSuccessor() != null) {
                    getSuccessor().handleRequest();
                    return;
                } else {
                    if (this.a != null) {
                        this.a.onFinish();
                        return;
                    }
                    return;
                }
            default:
                return;
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onError(int i, int i2) {
        int i3 = this.e + 1;
        this.e = i3;
        if (i3 < this.d.length) {
            handleRequest();
            return;
        }
        b = 0;
        synchronized (this.c) {
            this.c.notifyAll();
        }
        if (getSuccessor() != null) {
            getSuccessor().handleRequest();
        } else if (this.a != null) {
            this.a.onFinish();
        }
    }

    @Override // com.mappn.sdk.common.net.ApiRequestListener
    public void onSuccess(int i, Object obj) {
        this.d[this.e].delete();
        int i2 = this.e + 1;
        this.e = i2;
        if (i2 < this.d.length) {
            handleRequest();
            return;
        }
        b = 2;
        synchronized (this.c) {
            this.c.notifyAll();
        }
        if (getSuccessor() != null) {
            getSuccessor().handleRequest();
        } else if (this.a != null) {
            this.a.onFinish();
        }
    }
}
