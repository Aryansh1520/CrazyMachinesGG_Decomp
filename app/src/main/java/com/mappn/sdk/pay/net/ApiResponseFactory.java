package com.mappn.sdk.pay.net;

import android.text.TextUtils;
import com.mappn.sdk.common.utils.BaseConstants;
import com.mappn.sdk.pay.chargement.phonecard.CardsVerification;
import com.mappn.sdk.pay.chargement.phonecard.CardsVerifications;
import com.mappn.sdk.pay.model.TypeFactory;
import com.mappn.sdk.pay.payment.sms.SmsInfo;
import com.mappn.sdk.pay.payment.sms.SmsInfoFactory;
import com.mappn.sdk.pay.payment.sms.SmsInfos;
import com.mappn.sdk.pay.util.Utils;
import com.mappn.sdk.uc.util.Constants;
import com.mokredit.payment.StringUtils;
import java.io.StringReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes.dex */
public class ApiResponseFactory {
    private static String a(XmlPullParser xmlPullParser, String str) {
        String attributeValue = xmlPullParser.getAttributeValue(StringUtils.EMPTY, str);
        return attributeValue == null ? StringUtils.EMPTY : attributeValue.trim();
    }

    private static void a(XmlPullParser xmlPullParser) {
        String name = xmlPullParser.getName();
        while (xmlPullParser.next() > 0) {
            if (xmlPullParser.getEventType() == 3 && xmlPullParser.getName().equals(name)) {
                return;
            }
        }
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:9:0x0042. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0122  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.Object getResponse(android.content.Context r7, int r8, org.apache.http.HttpResponse r9, java.lang.String r10) {
        /*
            Method dump skipped, instructions count: 362
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.pay.net.ApiResponseFactory.getResponse(android.content.Context, int, org.apache.http.HttpResponse, java.lang.String):java.lang.Object");
    }

    public static ArrayList parseAccount(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new StringReader(str));
        ArrayList arrayList = new ArrayList(3);
        newPullParser.nextTag();
        newPullParser.require(2, StringUtils.EMPTY, "response");
        while (newPullParser.nextTag() == 2) {
            String name = newPullParser.getName();
            newPullParser.require(2, StringUtils.EMPTY, name);
            if ("name".equals(name)) {
                arrayList.add(newPullParser.nextText());
            } else if ("uid".equals(name)) {
                arrayList.add(newPullParser.nextText());
            } else if (Constants.KEY_USER_EMAIL.equals(name)) {
                arrayList.add(newPullParser.nextText());
            } else if ("token".equals(name)) {
                arrayList.add(newPullParser.nextText());
            } else {
                a(newPullParser);
            }
            newPullParser.require(3, StringUtils.EMPTY, name);
        }
        newPullParser.require(3, StringUtils.EMPTY, "response");
        return arrayList;
    }

    public static ArrayList parseAlipayOrder(String str) {
        JSONObject jSONObject = new JSONObject(str);
        if (1 != jSONObject.getInt(Constants.RESULT_CODE)) {
            return null;
        }
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(jSONObject.getString("alipayParam"));
        arrayList.add(jSONObject.getString("orderNo"));
        return arrayList;
    }

    public static int parseAlipayResult(String str) {
        return new JSONObject(str).getInt(Constants.RESULT_CODE);
    }

    public static String parseAppname(String str) {
        if (TextUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new StringReader(str));
        newPullParser.nextTag();
        newPullParser.require(2, StringUtils.EMPTY, "response");
        while (newPullParser.nextTag() == 2) {
            String name = newPullParser.getName();
            newPullParser.require(2, StringUtils.EMPTY, name);
            if ("appname".equals(name)) {
                return newPullParser.nextText();
            }
            a(newPullParser);
            newPullParser.require(3, StringUtils.EMPTY, name);
        }
        newPullParser.require(3, StringUtils.EMPTY, "response");
        return StringUtils.EMPTY;
    }

    public static String[] parseChargeChannel(String str) {
        JSONObject jSONObject = new JSONObject(str);
        if (1 != jSONObject.getInt(Constants.RESULT_CODE)) {
            return null;
        }
        JSONArray jSONArray = jSONObject.getJSONArray("channels");
        String[] strArr = new String[jSONArray.length()];
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            switch (jSONArray.getInt(i)) {
                case 1:
                    strArr[i] = "alipay";
                    continue;
                case 2:
                    strArr[i] = TypeFactory.TYPE_CHARGE_G;
                    continue;
                case 3:
                    strArr[i] = TypeFactory.TYPE_CHARGE_PHONECARD;
                    continue;
                case 4:
                    strArr[i] = TypeFactory.TYPE_CHARGE_NETBANK;
                    break;
            }
            strArr[i] = "mo9";
        }
        return strArr;
    }

    public static int parseChargeG(String str) {
        return new JSONObject(str).getInt(Constants.RESULT_CODE);
    }

    public static String parseChargePhoneCard(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new StringReader(str));
        String str2 = StringUtils.EMPTY;
        newPullParser.nextTag();
        newPullParser.require(2, StringUtils.EMPTY, "result");
        while (newPullParser.nextTag() == 2) {
            String name = newPullParser.getName();
            newPullParser.require(2, StringUtils.EMPTY, name);
            if ("pay_result".equals(name)) {
                str2 = a(newPullParser, "order_id");
                newPullParser.nextTag();
            } else {
                a(newPullParser);
            }
            newPullParser.require(3, StringUtils.EMPTY, name);
        }
        newPullParser.require(3, StringUtils.EMPTY, "result");
        return str2;
    }

    public static ArrayList parseJifengquanAndGBalance(String str) {
        JSONObject jSONObject = new JSONObject(str);
        if (1 != jSONObject.getInt(Constants.RESULT_CODE)) {
            return null;
        }
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(jSONObject.getInt("gVolume")));
        arrayList.add(Integer.valueOf(jSONObject.getInt("gMoney")));
        return arrayList;
    }

    public static ArrayList parseMO9Order(String str) {
        JSONObject jSONObject = new JSONObject(str);
        if (1 != jSONObject.getInt(Constants.RESULT_CODE)) {
            return null;
        }
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(jSONObject.getString("mo9Param"));
        arrayList.add(jSONObject.getString("orderNo"));
        return arrayList;
    }

    public static int parseMO9Result(String str) {
        return new JSONObject(str).getInt(Constants.RESULT_CODE);
    }

    public static String parsePayChannel(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new StringReader(str));
        newPullParser.nextTag();
        newPullParser.require(2, StringUtils.EMPTY, "response");
        while (newPullParser.nextTag() == 2) {
            String name = newPullParser.getName();
            newPullParser.require(2, StringUtils.EMPTY, name);
            if ("channels".equals(name)) {
                return newPullParser.nextText().replace(BaseConstants.DEFAULT_UC_CNO, TypeFactory.TYPE_PAY_JIFENGQUAN).replace("2", "sms");
            }
            a(newPullParser);
            newPullParser.require(3, StringUtils.EMPTY, name);
        }
        newPullParser.require(3, StringUtils.EMPTY, "response");
        return null;
    }

    public static String parsePayOrder(String str) {
        if (TextUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        }
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new StringReader(str));
        newPullParser.nextTag();
        newPullParser.require(2, StringUtils.EMPTY, "order_id");
        return newPullParser.nextText();
    }

    public static int parsePhoneCardChargeResult(String str) {
        int i = -1;
        if (!TextUtils.isEmpty(str)) {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new StringReader(str));
            newPullParser.nextTag();
            newPullParser.require(2, StringUtils.EMPTY, "result");
            while (newPullParser.nextTag() == 2) {
                String name = newPullParser.getName();
                newPullParser.require(2, StringUtils.EMPTY, name);
                if ("pay_result".equals(name)) {
                    i = Utils.getInt(a(newPullParser, "status"));
                    newPullParser.nextTag();
                } else {
                    a(newPullParser);
                }
                newPullParser.require(3, StringUtils.EMPTY, name);
            }
            newPullParser.require(3, StringUtils.EMPTY, "result");
        }
        return i;
    }

    public static SmsInfos parseSmsInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new StringReader(str));
        newPullParser.nextTag();
        newPullParser.require(2, StringUtils.EMPTY, "response");
        SmsInfos smsInfos = new SmsInfos();
        smsInfos.version = a(newPullParser, "updatetime");
        smsInfos.supportTel = a(newPullParser, "spcustom");
        while (newPullParser.nextTag() == 2) {
            String name = newPullParser.getName();
            newPullParser.require(2, StringUtils.EMPTY, name);
            if ("sp".equals(name)) {
                SmsInfo factory = SmsInfoFactory.factory(a(newPullParser, "spname"));
                factory.companyId = a(newPullParser, "companyid");
                factory.receiverNumber = a(newPullParser, "sendcode");
                factory.merId = a(newPullParser, "merId");
                factory.goodsId = a(newPullParser, "sendsms");
                factory.money = (int) Utils.getDouble(a(newPullParser, "money"));
                factory.disableAreas = a(newPullParser, "disablearea");
                smsInfos.add(factory);
                newPullParser.nextTag();
            } else {
                a(newPullParser);
            }
            newPullParser.require(3, StringUtils.EMPTY, name);
        }
        newPullParser.require(3, StringUtils.EMPTY, "response");
        return smsInfos;
    }

    public static CardsVerifications parseSyncCardInfoResult(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new StringReader(str));
        CardsVerifications cardsVerifications = new CardsVerifications();
        newPullParser.nextTag();
        newPullParser.require(2, StringUtils.EMPTY, "result");
        cardsVerifications.version = Utils.getInt(a(newPullParser, "remote_version"));
        while (newPullParser.nextTag() == 2) {
            String name = newPullParser.getName();
            newPullParser.require(2, StringUtils.EMPTY, name);
            if ("card".equals(name)) {
                CardsVerification cardsVerification = new CardsVerification();
                cardsVerification.name = a(newPullParser, "name");
                cardsVerification.pay_type = a(newPullParser, "pay_type");
                cardsVerification.accountNum = Utils.getInt(a(newPullParser, "account_len"));
                cardsVerification.passwordNum = Utils.getInt(a(newPullParser, "password_len"));
                cardsVerification.credit = a(newPullParser, "credit");
                cardsVerifications.cards.add(cardsVerification);
                newPullParser.nextTag();
            } else {
                a(newPullParser);
            }
            newPullParser.require(3, StringUtils.EMPTY, name);
        }
        newPullParser.require(3, StringUtils.EMPTY, "result");
        return cardsVerifications;
    }

    public static String parseUserProfile(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
        newPullParser.setInput(new StringReader(str));
        newPullParser.nextTag();
        newPullParser.require(2, StringUtils.EMPTY, "response");
        while (newPullParser.nextTag() == 2) {
            String name = newPullParser.getName();
            newPullParser.require(2, StringUtils.EMPTY, name);
            if ("credit".equals(name)) {
                return newPullParser.nextText();
            }
            a(newPullParser);
            newPullParser.require(3, StringUtils.EMPTY, name);
        }
        newPullParser.require(3, StringUtils.EMPTY, "response");
        return null;
    }
}
