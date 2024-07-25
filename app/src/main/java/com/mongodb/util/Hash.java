package com.mongodb.util;

/* loaded from: classes.dex */
public final class Hash {
    static final long _longHashConstant = 4095;

    public static final int hashBackward(String s) {
        int hash = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            hash = (hash * 31) + s.charAt(i);
        }
        return hash;
    }

    public static final long hashBackwardLong(String s) {
        long hash = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            hash = (63 * hash) + s.charAt(i);
        }
        return hash;
    }

    public static final long longHash(String s) {
        return longHash(s, 0, s.length());
    }

    public static final long longHash(String s, int start, int end) {
        long hash = 0;
        while (start < end) {
            hash = (_longHashConstant * hash) + s.charAt(start);
            start++;
        }
        return hash;
    }

    public static final long longLowerHash(String s) {
        return longLowerHash(s, 0, s.length());
    }

    public static final long longLowerHash(String s, int start, int end) {
        long hash = 0;
        while (start < end) {
            hash = (_longHashConstant * hash) + Character.toLowerCase(s.charAt(start));
            start++;
        }
        return hash;
    }

    public static final long longLowerHash(String s, int start, int end, long hash) {
        while (start < end) {
            hash = (_longHashConstant * hash) + Character.toLowerCase(s.charAt(start));
            start++;
        }
        return hash;
    }

    public static final long longLowerHashAppend(long hash, char c) {
        return (_longHashConstant * hash) + Character.toLowerCase(c);
    }

    public static final long longHashAppend(long hash, char c) {
        return (_longHashConstant * hash) + c;
    }

    public static final int lowerCaseHash(String s) {
        int h = 0;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            h = (h * 31) + Character.toLowerCase(s.charAt(i));
        }
        return h;
    }

    public static final int lowerCaseHash(String s, int start, int end) {
        int h = 0;
        int len = s.length();
        for (int i = start; i < len && i < end; i++) {
            h = (h * 31) + Character.toLowerCase(s.charAt(i));
        }
        return h;
    }

    public static final int hashCode(CharSequence s, int start, int end) {
        int h = 0;
        int len = s.length();
        for (int i = start; i < len && i < end; i++) {
            h = (h * 31) + s.charAt(i);
        }
        return h;
    }

    public static final int nospaceLowerHash(String s, int start, int end) {
        int h = 0;
        int len = s.length();
        for (int i = start; i < len && i < end; i++) {
            char c = s.charAt(i);
            if (!Character.isWhitespace(c)) {
                h = (h * 31) + Character.toLowerCase(c);
            }
        }
        return h;
    }

    public static final int lowerCaseSpaceTrimHash(String s) {
        int h = 0;
        int len = s.length();
        while (len > 1 && Character.isWhitespace(s.charAt(len - 1))) {
            len--;
        }
        boolean lastWasSpace = true;
        for (int i = 0; i < len; i++) {
            boolean isSpace = Character.isWhitespace(s.charAt(i));
            if (!isSpace || !lastWasSpace) {
                lastWasSpace = isSpace;
                h = (h * 31) + Character.toLowerCase(s.charAt(i));
            }
        }
        return h;
    }

    public static final int lowerCaseSpaceTrimHash(String s, int start, int end) {
        int h = 0;
        int len = s.length();
        while (len > 1 && Character.isWhitespace(s.charAt(len - 1))) {
            len--;
        }
        boolean lastWasSpace = true;
        for (int i = start; i < len && i < end; i++) {
            boolean isSpace = Character.isWhitespace(s.charAt(i));
            if (!isSpace || !lastWasSpace) {
                lastWasSpace = isSpace;
                h = (h * 31) + Character.toLowerCase(s.charAt(i));
            }
        }
        return h;
    }

    public static final int hashCode(String... strings) {
        int h = 0;
        for (String s : strings) {
            int len = s.length();
            for (int i = 0; i < len; i++) {
                h = (h * 31) + s.charAt(i);
            }
        }
        return h;
    }
}
