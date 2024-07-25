package com.mappn.sdk.pay;

import android.content.Context;
import android.os.AsyncTask;
import com.mappn.sdk.common.utils.BaseConstants;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.database.PayDB;
import com.mappn.sdk.pay.database.PayVo;
import com.mappn.sdk.pay.model.Order;
import com.mappn.sdk.pay.net.Api;
import java.util.HashMap;

/* loaded from: classes.dex */
public class ConfirmPayTask extends AsyncTask {
    private static String c = BaseConstants.DEFAULT_UC_CNO;
    private Context a;
    private GfanConfirmOrderCallback b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfirmPayTask(Context context, GfanConfirmOrderCallback gfanConfirmOrderCallback) {
        this.a = context;
        this.b = gfanConfirmOrderCallback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Object doInBackground(Void... voidArr) {
        PayVo queryPayVo = PayDB.queryPayVo(this.a);
        if (queryPayVo == null) {
            this.b.onNotExist();
            return null;
        }
        Api.confirmPayResult(this.a, new a(this), ((Order) ((HashMap) queryPayVo.getPayParameters()).get("com.mappn.sdk.order")).getOrderID(), BaseUtils.getAppKey(this.a));
        return null;
    }
}
