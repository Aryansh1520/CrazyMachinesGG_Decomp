package com.mongodb.util;

import com.mappn.sdk.pay.util.Constants;
import org.bson.BSONCallback;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: JSON.java */
/* loaded from: classes.dex */
public class JSONParser {
    BSONCallback _callback;
    int pos;
    String s;

    public JSONParser(String s) {
        this(s, null);
    }

    public JSONParser(String s, BSONCallback callback) {
        this.pos = 0;
        this.s = s;
        this._callback = callback == null ? new JSONCallback() : callback;
    }

    public Object parse() {
        return parse(null);
    }

    protected Object parse(String name) {
        char current = get();
        switch (current) {
            case '\"':
            case '\'':
                Object value = parseString(true);
                return value;
            case '+':
            case '-':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case Constants.CUSTOM_TEXTVIEW_HEIGHT /* 53 */:
            case '6':
            case '7':
            case '8':
            case '9':
                Object value2 = parseNumber();
                return value2;
            case 'N':
                read('N');
                read('a');
                read('N');
                Object value3 = Double.valueOf(Double.NaN);
                return value3;
            case '[':
                Object value4 = parseArray(name);
                return value4;
            case 'f':
                read('f');
                read('a');
                read('l');
                read('s');
                read('e');
                return false;
            case 'n':
                read('n');
                read('u');
                read('l');
                read('l');
                return null;
            case 't':
                read('t');
                read('r');
                read('u');
                read('e');
                return true;
            case '{':
                Object value5 = parseObject(name);
                return value5;
            default:
                throw new JSONParseException(this.s, this.pos);
        }
    }

    public Object parseObject() {
        return parseObject(null);
    }

    protected Object parseObject(String name) {
        if (name != null) {
            this._callback.objectStart(name);
        } else {
            this._callback.objectStart();
        }
        read('{');
        get();
        while (get() != '}') {
            String key = parseString(false);
            read(':');
            Object value = parse(key);
            doCallback(key, value);
            char current = get();
            if (current != ',') {
                break;
            }
            read(',');
        }
        read('}');
        return this._callback.objectDone();
    }

    protected void doCallback(String name, Object value) {
        if (value == null) {
            this._callback.gotNull(name);
            return;
        }
        if (value instanceof String) {
            this._callback.gotString(name, (String) value);
            return;
        }
        if (value instanceof Boolean) {
            this._callback.gotBoolean(name, ((Boolean) value).booleanValue());
            return;
        }
        if (value instanceof Integer) {
            this._callback.gotInt(name, ((Integer) value).intValue());
        } else if (value instanceof Long) {
            this._callback.gotLong(name, ((Long) value).longValue());
        } else if (value instanceof Double) {
            this._callback.gotDouble(name, ((Double) value).doubleValue());
        }
    }

    public void read(char ch) {
        if (!check(ch)) {
            throw new JSONParseException(this.s, this.pos);
        }
        this.pos++;
    }

    public char read() {
        if (this.pos >= this.s.length()) {
            throw new IllegalStateException("string done");
        }
        String str = this.s;
        int i = this.pos;
        this.pos = i + 1;
        return str.charAt(i);
    }

    public void readHex() {
        if (this.pos < this.s.length() && ((this.s.charAt(this.pos) >= '0' && this.s.charAt(this.pos) <= '9') || ((this.s.charAt(this.pos) >= 'A' && this.s.charAt(this.pos) <= 'F') || (this.s.charAt(this.pos) >= 'a' && this.s.charAt(this.pos) <= 'f')))) {
            this.pos++;
            return;
        }
        throw new JSONParseException(this.s, this.pos);
    }

    public boolean check(char ch) {
        return get() == ch;
    }

    public void skipWS() {
        while (this.pos < this.s.length() && Character.isWhitespace(this.s.charAt(this.pos))) {
            this.pos++;
        }
    }

    public char get() {
        skipWS();
        if (this.pos < this.s.length()) {
            return this.s.charAt(this.pos);
        }
        return (char) 65535;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x00d7 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0065 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x003a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String parseString(boolean r12) {
        /*
            r11 = this;
            r3 = 0
            r8 = 39
            boolean r8 = r11.check(r8)
            if (r8 == 0) goto L42
            r3 = 39
        Lb:
            if (r3 <= 0) goto L10
            r11.read(r3)
        L10:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            int r5 = r11.pos
        L17:
            int r8 = r11.pos
            java.lang.String r9 = r11.s
            int r9 = r9.length()
            if (r8 >= r9) goto L2d
            java.lang.String r8 = r11.s
            int r9 = r11.pos
            char r2 = r8.charAt(r9)
            if (r3 <= 0) goto L59
            if (r2 != r3) goto L61
        L2d:
            java.lang.String r8 = r11.s
            int r9 = r11.pos
            java.lang.String r8 = r8.substring(r5, r9)
            r0.append(r8)
            if (r3 <= 0) goto L3d
            r11.read(r3)
        L3d:
            java.lang.String r8 = r0.toString()
            return r8
        L42:
            r8 = 34
            boolean r8 = r11.check(r8)
            if (r8 == 0) goto L4d
            r3 = 34
            goto Lb
        L4d:
            if (r12 == 0) goto Lb
            com.mongodb.util.JSONParseException r8 = new com.mongodb.util.JSONParseException
            java.lang.String r9 = r11.s
            int r10 = r11.pos
            r8.<init>(r9, r10)
            throw r8
        L59:
            r8 = 58
            if (r2 == r8) goto L2d
            r8 = 32
            if (r2 == r8) goto L2d
        L61:
            r8 = 92
            if (r2 != r8) goto Ld7
            int r8 = r11.pos
            int r8 = r8 + 1
            r11.pos = r8
            char r7 = r11.get()
            r4 = 0
            switch(r7) {
                case 34: goto Ld1;
                case 92: goto Ld4;
                case 98: goto Lce;
                case 110: goto Lc5;
                case 114: goto Lc8;
                case 116: goto Lcb;
                case 117: goto L8e;
                default: goto L73;
            }
        L73:
            java.lang.String r8 = r11.s
            int r9 = r11.pos
            int r9 = r9 + (-1)
            java.lang.String r8 = r8.substring(r5, r9)
            r0.append(r8)
            if (r4 == 0) goto L8b
            int r8 = r11.pos
            int r8 = r8 + 1
            r11.pos = r8
            r0.append(r4)
        L8b:
            int r5 = r11.pos
            goto L17
        L8e:
            java.lang.String r8 = r11.s
            int r9 = r11.pos
            int r9 = r9 + (-1)
            java.lang.String r8 = r8.substring(r5, r9)
            r0.append(r8)
            int r8 = r11.pos
            int r8 = r8 + 1
            r11.pos = r8
            int r6 = r11.pos
            r11.readHex()
            r11.readHex()
            r11.readHex()
            r11.readHex()
            java.lang.String r8 = r11.s
            int r9 = r6 + 4
            java.lang.String r8 = r8.substring(r6, r9)
            r9 = 16
            int r1 = java.lang.Integer.parseInt(r8, r9)
            char r8 = (char) r1
            r0.append(r8)
            int r5 = r11.pos
            goto L17
        Lc5:
            r4 = 10
            goto L73
        Lc8:
            r4 = 13
            goto L73
        Lcb:
            r4 = 9
            goto L73
        Lce:
            r4 = 8
            goto L73
        Ld1:
            r4 = 34
            goto L73
        Ld4:
            r4 = 92
            goto L73
        Ld7:
            int r8 = r11.pos
            int r8 = r8 + 1
            r11.pos = r8
            goto L17
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mongodb.util.JSONParser.parseString(boolean):java.lang.String");
    }

    public Number parseNumber() {
        Number number;
        get();
        int i = this.pos;
        boolean z = false;
        if (check('-') || check('+')) {
            this.pos++;
        }
        while (this.pos < this.s.length()) {
            switch (this.s.charAt(this.pos)) {
                case '.':
                    z = true;
                    parseFraction();
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case Constants.CUSTOM_TEXTVIEW_HEIGHT /* 53 */:
                case '6':
                case '7':
                case '8':
                case '9':
                    this.pos++;
                    break;
                case 'E':
                case 'e':
                    z = true;
                    parseExponent();
                    break;
            }
        }
        try {
            if (z) {
                number = Double.valueOf(this.s.substring(i, this.pos));
            } else {
                Long valueOf = Long.valueOf(this.s.substring(i, this.pos));
                long longValue = valueOf.longValue();
                number = valueOf;
                if (longValue <= 2147483647L) {
                    long longValue2 = valueOf.longValue();
                    number = valueOf;
                    if (longValue2 >= -2147483648L) {
                        number = Integer.valueOf(valueOf.intValue());
                    }
                }
            }
            return number;
        } catch (NumberFormatException e) {
            throw new JSONParseException(this.s, i, e);
        }
    }

    public void parseFraction() {
        this.pos++;
        while (this.pos < this.s.length()) {
            switch (this.s.charAt(this.pos)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case Constants.CUSTOM_TEXTVIEW_HEIGHT /* 53 */:
                case '6':
                case '7':
                case '8':
                case '9':
                    this.pos++;
                    break;
                case 'E':
                case 'e':
                    parseExponent();
                    break;
                default:
                    return;
            }
        }
    }

    public void parseExponent() {
        this.pos++;
        if (check('-') || check('+')) {
            this.pos++;
        }
        while (this.pos < this.s.length()) {
            switch (this.s.charAt(this.pos)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case Constants.CUSTOM_TEXTVIEW_HEIGHT /* 53 */:
                case '6':
                case '7':
                case '8':
                case '9':
                    this.pos++;
                default:
                    return;
            }
        }
    }

    public Object parseArray() {
        return parseArray(null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0038, code lost:
    
        read(']');
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0041, code lost:
    
        return r8._callback.arrayDone();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected java.lang.Object parseArray(java.lang.String r9) {
        /*
            r8 = this;
            r7 = 44
            r6 = 93
            if (r9 == 0) goto L30
            org.bson.BSONCallback r5 = r8._callback
            r5.arrayStart(r9)
        Lb:
            r5 = 91
            r8.read(r5)
            r3 = 0
            char r0 = r8.get()
            r4 = r3
        L16:
            if (r0 == r6) goto L4c
            int r3 = r4 + 1
            java.lang.String r2 = java.lang.String.valueOf(r4)
            java.lang.Object r1 = r8.parse(r2)
            r8.doCallback(r2, r1)
            char r0 = r8.get()
            if (r0 != r7) goto L36
            r8.read(r7)
            r4 = r3
            goto L16
        L30:
            org.bson.BSONCallback r5 = r8._callback
            r5.arrayStart()
            goto Lb
        L36:
            if (r0 != r6) goto L42
        L38:
            r8.read(r6)
            org.bson.BSONCallback r5 = r8._callback
            java.lang.Object r5 = r5.arrayDone()
            return r5
        L42:
            com.mongodb.util.JSONParseException r5 = new com.mongodb.util.JSONParseException
            java.lang.String r6 = r8.s
            int r7 = r8.pos
            r5.<init>(r6, r7)
            throw r5
        L4c:
            r3 = r4
            goto L38
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mongodb.util.JSONParser.parseArray(java.lang.String):java.lang.Object");
    }
}
