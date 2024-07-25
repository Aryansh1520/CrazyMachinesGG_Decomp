package com.mappn.sdk.pay.net.chain;

import android.content.Context;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.pay.chargement.alipay.BaseHelper;
import com.mappn.sdk.pay.net.Api;
import com.mappn.sdk.pay.net.ApiResponseFactory;
import com.mappn.sdk.pay.net.chain.Handler;
import com.mappn.sdk.pay.payment.sms.SmsInfos;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.PrefUtil;
import com.mappn.sdk.pay.util.Utils;
import java.io.IOException;

/* loaded from: classes.dex */
public class SyncSmsInfoHandler extends Handler implements ApiRequestListener {
    private static int b;
    private Handler.OnFinishListener a;
    private final Integer c;

    public SyncSmsInfoHandler(Context context) {
        super(context);
        this.c = 0;
    }

    public SyncSmsInfoHandler(Context context, Handler.OnFinishListener onFinishListener) {
        super(context);
        this.c = 0;
        this.a = onFinishListener;
    }

    public static void init() {
        b = 0;
    }

    @Override // com.mappn.sdk.pay.net.chain.Handler
    public void handleRequest() {
        switch (b) {
            case 0:
                b = 1;
                Api.syncSmsInfo(this.mContext, this);
                return;
            case 1:
                new Thread(new h(this)).start();
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
        b = 2;
        if (Utils.getSmsInfos() == null) {
            try {
                String smsInfo = PrefUtil.getSmsInfo(this.mContext);
                if (smsInfo == null) {
                    smsInfo = BaseHelper.convertStreamToString(this.mContext.getResources().getAssets().open(Constants.TEXT_PAY_SMS_INFO_DEFAULT_FILE_NAME));
                }
                SmsInfos parseSmsInfo = ApiResponseFactory.parseSmsInfo(smsInfo);
                if (smsInfo == null) {
                    PrefUtil.setSmsInfoVersion(this.mContext, parseSmsInfo.version);
                }
                Utils.setSmsInfo(parseSmsInfo);
            } catch (IOException e) {
                b = 0;
                e.printStackTrace();
            } catch (Exception e2) {
                b = 0;
                e2.printStackTrace();
            }
        }
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
        b = 2;
        PrefUtil.setSmsInfoVersion(this.mContext, ((SmsInfos) obj).version);
        Utils.setSmsInfo((SmsInfos) obj);
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
