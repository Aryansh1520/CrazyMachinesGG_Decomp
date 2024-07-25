package com.mokredit.payment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/* loaded from: classes.dex */
public class MokreditSign {
    public static String getSignatureContent(Properties properties) {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList arrayList = new ArrayList(properties.keySet());
        Collections.sort(arrayList);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= arrayList.size()) {
                return stringBuffer.toString();
            }
            String str = (String) arrayList.get(i2);
            stringBuffer.append(String.valueOf(i2 == 0 ? StringUtils.EMPTY : "&") + str + "=" + properties.getProperty(str));
            i = i2 + 1;
        }
    }

    public static String sign(Map map, String str) {
        Properties properties = new Properties();
        for (String str2 : map.keySet()) {
            Object obj = map.get(str2);
            if (str2 != null && !str2.equalsIgnoreCase("sign")) {
                properties.setProperty(str2, obj.toString());
            }
        }
        String signatureContent = getSignatureContent(properties);
        if (str == null) {
            return null;
        }
        return Md5Encrypt.encrypt(String.valueOf(signatureContent) + str);
    }
}
