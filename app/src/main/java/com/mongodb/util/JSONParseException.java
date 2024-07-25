package com.mongodb.util;

/* loaded from: classes.dex */
public class JSONParseException extends RuntimeException {
    private static final long serialVersionUID = -4415279469780082174L;
    int pos;
    String s;

    @Override // java.lang.Throwable
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append(this.s);
        sb.append("\n");
        for (int i = 0; i < this.pos; i++) {
            sb.append(" ");
        }
        sb.append("^");
        return sb.toString();
    }

    public JSONParseException(String s, int pos) {
        this.s = s;
        this.pos = pos;
    }

    public JSONParseException(String s, int pos, Throwable cause) {
        super(cause);
        this.s = s;
        this.pos = pos;
    }
}
