package com.mongodb.util;

import com.mokredit.payment.StringUtils;
import com.mongodb.DBObject;
import org.bson.BSONCallback;

/* loaded from: classes.dex */
public class JSON {
    public static String serialize(Object o) {
        StringBuilder buf = new StringBuilder();
        serialize(o, buf);
        return buf.toString();
    }

    public static void serialize(Object o, StringBuilder buf) {
        JSONSerializers.getLegacy().serialize(o, buf);
    }

    public static Object parse(String s) {
        return parse(s, null);
    }

    public static Object parse(String s, BSONCallback c) {
        if (s != null) {
            String s2 = s.trim();
            if (!s2.equals(StringUtils.EMPTY)) {
                JSONParser p = new JSONParser(s2, c);
                return p.parse();
            }
        }
        return (DBObject) null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void string(StringBuilder a, String s) {
        a.append("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '\\') {
                a.append("\\\\");
            } else if (c == '\"') {
                a.append("\\\"");
            } else if (c == '\n') {
                a.append("\\n");
            } else if (c == '\r') {
                a.append("\\r");
            } else if (c == '\t') {
                a.append("\\t");
            } else if (c == '\b') {
                a.append("\\b");
            } else if (c >= ' ') {
                a.append(c);
            }
        }
        a.append("\"");
    }
}
