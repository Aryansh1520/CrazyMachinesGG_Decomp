package com.mappn.sdk.pay.payment.sms;

import android.content.Context;
import com.mappn.sdk.pay.util.Constants;
import com.mappn.sdk.pay.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class SmsInfos {
    public ArrayList smsInfos = new ArrayList();
    public String supportTel;
    public String version;

    public void add(SmsInfo smsInfo) {
        this.smsInfos.add(smsInfo);
    }

    public SmsInfo filterSmsInfo(Context context, int i) {
        String company = Utils.getCompany(context);
        String provinceCode = Utils.getProvinceCode(context);
        ArrayList arrayList = null;
        Iterator it = this.smsInfos.iterator();
        while (it.hasNext()) {
            SmsInfo smsInfo = (SmsInfo) it.next();
            if (company.equals(smsInfo.companyId)) {
                if (smsInfo.supportMultiple()) {
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        int i2 = i;
                        do {
                            if (i % i2 == 0) {
                                arrayList.add(Integer.valueOf(i2));
                            }
                            i2--;
                        } while (i2 > 0);
                    }
                    Iterator it2 = arrayList.iterator();
                    while (it2.hasNext()) {
                        if (smsInfo.money != ((Integer) it2.next()).intValue() || (provinceCode != null && smsInfo.disableAreas.contains(provinceCode))) {
                        }
                        return smsInfo;
                    }
                }
                if (i == smsInfo.money) {
                    if (provinceCode != null && smsInfo.disableAreas.contains(provinceCode)) {
                    }
                    return smsInfo;
                }
                continue;
            }
        }
        throw new SimCardNotSupportException(Constants.TEXT_PAY_SMS_ERROR_NO_COMPANY);
    }
}
