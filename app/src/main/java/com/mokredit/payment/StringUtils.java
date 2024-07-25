package com.mokredit.payment;

import java.util.Locale;

/* loaded from: classes.dex */
public class StringUtils {
    public static final String EMPTY = "";
    public static final int INDEX_NOT_FOUND = -1;

    private static int a(String str, String str2, int i, boolean z) {
        int i2 = 0;
        if (str == null || str2 == null || i <= 0) {
            return -1;
        }
        if (str2.length() == 0) {
            if (z) {
                return str.length();
            }
            return 0;
        }
        int length = z ? str.length() : -1;
        do {
            length = z ? str.lastIndexOf(str2, length - 1) : str.indexOf(str2, length + 1);
            if (length < 0) {
                return length;
            }
            i2++;
        } while (i2 < i);
        return length;
    }

    private static String a(int i, char c) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + i);
        }
        char[] cArr = new char[i];
        for (int i2 = 0; i2 < cArr.length; i2++) {
            cArr[i2] = c;
        }
        return new String(cArr);
    }

    private static String a(String str, String[] strArr, String[] strArr2, boolean z, int i) {
        int i2;
        int length;
        String str2 = str;
        while (str2 != null && str2.length() != 0 && strArr != null && strArr.length != 0 && strArr2 != null && strArr2.length != 0) {
            if (i < 0) {
                throw new IllegalStateException("TimeToLive of " + i + " is less than 0: " + str2);
            }
            int length2 = strArr.length;
            int length3 = strArr2.length;
            if (length2 != length3) {
                throw new IllegalArgumentException("Search and Replace array lengths don't match: " + length2 + " vs " + length3);
            }
            boolean[] zArr = new boolean[length2];
            int i3 = -1;
            int i4 = -1;
            for (int i5 = 0; i5 < length2; i5++) {
                if (!zArr[i5] && strArr[i5] != null && strArr[i5].length() != 0 && strArr2[i5] != null) {
                    int indexOf = str2.indexOf(strArr[i5]);
                    if (indexOf == -1) {
                        zArr[i5] = true;
                    } else if (i3 == -1 || indexOf < i3) {
                        i4 = i5;
                        i3 = indexOf;
                    }
                }
            }
            if (i3 == -1) {
                return str2;
            }
            int i6 = 0;
            int i7 = 0;
            for (int i8 = 0; i8 < strArr.length; i8++) {
                if (strArr[i8] != null && strArr2[i8] != null && (length = strArr2[i8].length() - strArr[i8].length()) > 0) {
                    i7 += length * 3;
                }
            }
            StringBuffer stringBuffer = new StringBuffer(Math.min(i7, str2.length() / 5) + str2.length());
            while (true) {
                int i9 = i4;
                i2 = i6;
                if (i3 == -1) {
                    break;
                }
                while (i2 < i3) {
                    stringBuffer.append(str2.charAt(i2));
                    i2++;
                }
                stringBuffer.append(strArr2[i9]);
                i6 = i3 + strArr[i9].length();
                i3 = -1;
                i4 = -1;
                for (int i10 = 0; i10 < length2; i10++) {
                    if (!zArr[i10] && strArr[i10] != null && strArr[i10].length() != 0 && strArr2[i10] != null) {
                        int indexOf2 = str2.indexOf(strArr[i10], i6);
                        if (indexOf2 == -1) {
                            zArr[i10] = true;
                        } else if (i3 == -1 || indexOf2 < i3) {
                            i4 = i10;
                            i3 = indexOf2;
                        }
                    }
                }
            }
            int length4 = str2.length();
            while (i2 < length4) {
                stringBuffer.append(str2.charAt(i2));
                i2++;
            }
            String stringBuffer2 = stringBuffer.toString();
            if (!z) {
                return stringBuffer2;
            }
            i--;
            str2 = stringBuffer2;
        }
        return str2;
    }

    private static boolean a(String str, String str2, boolean z) {
        if (str == null || str2 == null) {
            return str == null && str2 == null;
        }
        if (str2.length() <= str.length()) {
            return str.regionMatches(z, str.length() - str2.length(), str2, 0, str2.length());
        }
        return false;
    }

    public static String capitalise(String str) {
        return capitalize(str);
    }

    public static String capitalize(String str) {
        int length;
        return (str == null || (length = str.length()) == 0) ? str : new StringBuffer(length).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    public static String center(String str, int i) {
        return center(str, i, ' ');
    }

    public static String center(String str, int i, char c) {
        int length;
        int length2;
        return (str == null || i <= 0 || (length2 = i - (length = str.length())) <= 0) ? str : rightPad(leftPad(str, length + (length2 / 2), c), i, c);
    }

    public static String center(String str, int i, String str2) {
        if (str == null || i <= 0) {
            return str;
        }
        if (isEmpty(str2)) {
            str2 = " ";
        }
        int length = str.length();
        int i2 = i - length;
        return i2 > 0 ? rightPad(leftPad(str, length + (i2 / 2), str2), i, str2) : str;
    }

    public static String chomp(String str, String str2) {
        return (isEmpty(str) || str2 == null || !str.endsWith(str2)) ? str : str.substring(0, str.length() - str2.length());
    }

    public static String chompLast(String str) {
        return chompLast(str, "\n");
    }

    public static String chompLast(String str, String str2) {
        return (str.length() != 0 && str2.equals(str.substring(str.length() - str2.length()))) ? str.substring(0, str.length() - str2.length()) : str;
    }

    public static String clean(String str) {
        return str == null ? EMPTY : str.trim();
    }

    public static boolean contains(String str, char c) {
        return !isEmpty(str) && str.indexOf(c) >= 0;
    }

    public static boolean contains(String str, String str2) {
        return (str == null || str2 == null || str.indexOf(str2) < 0) ? false : true;
    }

    public static boolean containsAny(String str, String str2) {
        if (str2 == null) {
            return false;
        }
        return containsAny(str, str2.toCharArray());
    }

    public static boolean containsAny(String str, char[] cArr) {
        if (str == null || str.length() == 0 || cArr == null || cArr.length == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            for (char c : cArr) {
                if (c == charAt) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containsIgnoreCase(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        int length = str2.length();
        int length2 = str.length() - length;
        for (int i = 0; i <= length2; i++) {
            if (str.regionMatches(true, i, str2, 0, length)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsNone(String str, String str2) {
        if (str == null || str2 == null) {
            return true;
        }
        return containsNone(str, str2.toCharArray());
    }

    public static boolean containsNone(String str, char[] cArr) {
        if (str == null || cArr == null) {
            return true;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            for (char c : cArr) {
                if (c == charAt) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int countMatches(String str, String str2) {
        int i = 0;
        if (isEmpty(str) || isEmpty(str2)) {
            return 0;
        }
        int i2 = 0;
        while (true) {
            int indexOf = str.indexOf(str2, i);
            if (indexOf == -1) {
                return i2;
            }
            i2++;
            i = indexOf + str2.length();
        }
    }

    public static String defaultIfEmpty(String str, String str2) {
        return isEmpty(str) ? str2 : str;
    }

    public static String defaultString(String str) {
        return str == null ? EMPTY : str;
    }

    public static String defaultString(String str, String str2) {
        return str == null ? str2 : str;
    }

    public static boolean endsWith(String str, String str2) {
        return a(str, str2, false);
    }

    public static boolean endsWithIgnoreCase(String str, String str2) {
        return a(str, str2, true);
    }

    public static boolean equals(String str, String str2) {
        return str == null ? str2 == null : str.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str, String str2) {
        return str == null ? str2 == null : str.equalsIgnoreCase(str2);
    }

    public static String getChomp(String str, String str2) {
        int lastIndexOf = str.lastIndexOf(str2);
        return lastIndexOf == str.length() - str2.length() ? str2 : lastIndexOf != -1 ? str.substring(lastIndexOf) : EMPTY;
    }

    public static String getPrechomp(String str, String str2) {
        int indexOf = str.indexOf(str2);
        return indexOf == -1 ? EMPTY : str.substring(0, indexOf + str2.length());
    }

    public static int indexOf(String str, char c) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.indexOf(c);
    }

    public static int indexOf(String str, char c, int i) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.indexOf(c, i);
    }

    public static int indexOf(String str, String str2) {
        if (str == null || str2 == null) {
            return -1;
        }
        return str.indexOf(str2);
    }

    public static int indexOf(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return -1;
        }
        return (str2.length() != 0 || i < str.length()) ? str.indexOf(str2, i) : str.length();
    }

    public static int indexOfAny(String str, String[] strArr) {
        int i;
        if (str == null || strArr == null) {
            return -1;
        }
        int length = strArr.length;
        int i2 = 0;
        int i3 = Integer.MAX_VALUE;
        while (i2 < length) {
            String str2 = strArr[i2];
            if (str2 == null || (i = str.indexOf(str2)) == -1 || i >= i3) {
                i = i3;
            }
            i2++;
            i3 = i;
        }
        if (i3 == Integer.MAX_VALUE) {
            return -1;
        }
        return i3;
    }

    public static int indexOfAnyBut(String str, String str2) {
        if (isEmpty(str) || isEmpty(str2)) {
            return -1;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str2.indexOf(str.charAt(i)) < 0) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOfIgnoreCase(String str, String str2) {
        return indexOfIgnoreCase(str, str2, 0);
    }

    public static int indexOfIgnoreCase(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return -1;
        }
        if (i < 0) {
            i = 0;
        }
        int length = (str.length() - str2.length()) + 1;
        if (i > length) {
            return -1;
        }
        if (str2.length() == 0) {
            return i;
        }
        for (int i2 = i; i2 < length; i2++) {
            if (str.regionMatches(true, i2, str2, 0, str2.length())) {
                return i2;
            }
        }
        return -1;
    }

    public static boolean isAllLowerCase(String str) {
        if (str == null || isEmpty(str)) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLowerCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllUpperCase(String str) {
        if (str == null || isEmpty(str)) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isUpperCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlpha(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetter(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaSpace(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetter(str.charAt(i)) && str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumeric(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetterOrDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphanumericSpace(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLetterOrDigit(str.charAt(i)) && str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isBlank(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumericSpace(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static boolean isWhitespace(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String join(Object[] objArr) {
        return join(objArr, (String) null);
    }

    public static String join(Object[] objArr, char c) {
        if (objArr == null) {
            return null;
        }
        return join(objArr, c, 0, objArr.length);
    }

    public static String join(Object[] objArr, char c, int i, int i2) {
        if (objArr == null) {
            return null;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY;
        }
        StringBuffer stringBuffer = new StringBuffer(((objArr[i] == null ? 16 : objArr[i].toString().length()) + 1) * i3);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                stringBuffer.append(c);
            }
            if (objArr[i4] != null) {
                stringBuffer.append(objArr[i4]);
            }
        }
        return stringBuffer.toString();
    }

    public static String join(Object[] objArr, String str) {
        if (objArr == null) {
            return null;
        }
        return join(objArr, str, 0, objArr.length);
    }

    public static String join(Object[] objArr, String str, int i, int i2) {
        if (objArr == null) {
            return null;
        }
        if (str == null) {
            str = EMPTY;
        }
        int i3 = i2 - i;
        if (i3 <= 0) {
            return EMPTY;
        }
        StringBuffer stringBuffer = new StringBuffer(((objArr[i] == null ? 16 : objArr[i].toString().length()) + str.length()) * i3);
        for (int i4 = i; i4 < i2; i4++) {
            if (i4 > i) {
                stringBuffer.append(str);
            }
            if (objArr[i4] != null) {
                stringBuffer.append(objArr[i4]);
            }
        }
        return stringBuffer.toString();
    }

    public static int lastIndexOf(String str, char c) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(c);
    }

    public static int lastIndexOf(String str, char c, int i) {
        if (isEmpty(str)) {
            return -1;
        }
        return str.lastIndexOf(c, i);
    }

    public static int lastIndexOf(String str, String str2) {
        if (str == null || str2 == null) {
            return -1;
        }
        return str.lastIndexOf(str2);
    }

    public static int lastIndexOf(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return -1;
        }
        return str.lastIndexOf(str2, i);
    }

    public static int lastIndexOfAny(String str, String[] strArr) {
        int i;
        int i2 = -1;
        if (str != null && strArr != null) {
            int length = strArr.length;
            int i3 = 0;
            while (i3 < length) {
                String str2 = strArr[i3];
                if (str2 == null || (i = str.lastIndexOf(str2)) <= i2) {
                    i = i2;
                }
                i3++;
                i2 = i;
            }
        }
        return i2;
    }

    public static int lastIndexOfIgnoreCase(String str, String str2) {
        if (str == null || str2 == null) {
            return -1;
        }
        return lastIndexOfIgnoreCase(str, str2, str.length());
    }

    public static int lastIndexOfIgnoreCase(String str, String str2, int i) {
        if (str == null || str2 == null) {
            return -1;
        }
        int length = i > str.length() - str2.length() ? str.length() - str2.length() : i;
        if (length < 0) {
            return -1;
        }
        if (str2.length() == 0) {
            return length;
        }
        while (length >= 0) {
            if (str.regionMatches(true, length, str2, 0, str2.length())) {
                return length;
            }
            length--;
        }
        return -1;
    }

    public static int lastOrdinalIndexOf(String str, String str2, int i) {
        return a(str, str2, i, true);
    }

    public static String left(String str, int i) {
        if (str == null) {
            return null;
        }
        return i < 0 ? EMPTY : str.length() > i ? str.substring(0, i) : str;
    }

    public static String leftPad(String str, int i) {
        return leftPad(str, i, ' ');
    }

    public static String leftPad(String str, int i, char c) {
        if (str == null) {
            return null;
        }
        int length = i - str.length();
        return length > 0 ? length > 8192 ? leftPad(str, i, String.valueOf(c)) : a(length, c).concat(str) : str;
    }

    public static String leftPad(String str, int i, String str2) {
        if (str == null) {
            return null;
        }
        if (isEmpty(str2)) {
            str2 = " ";
        }
        int length = str2.length();
        int length2 = i - str.length();
        if (length2 <= 0) {
            return str;
        }
        if (length == 1 && length2 <= 8192) {
            return leftPad(str, i, str2.charAt(0));
        }
        if (length2 == length) {
            return str2.concat(str);
        }
        if (length2 < length) {
            return str2.substring(0, length2).concat(str);
        }
        char[] cArr = new char[length2];
        char[] charArray = str2.toCharArray();
        for (int i2 = 0; i2 < length2; i2++) {
            cArr[i2] = charArray[i2 % length];
        }
        return new String(cArr).concat(str);
    }

    public static int length(String str) {
        if (str == null) {
            return 0;
        }
        return str.length();
    }

    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }

    public static String lowerCase(String str, Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase(locale);
    }

    public static String mid(String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (i2 < 0 || i > str.length()) {
            return EMPTY;
        }
        if (i < 0) {
            i = 0;
        }
        return str.length() <= i + i2 ? str.substring(i) : str.substring(i, i + i2);
    }

    public static int ordinalIndexOf(String str, String str2, int i) {
        return a(str, str2, i, false);
    }

    public static String overlay(String str, String str2, int i, int i2) {
        if (str == null) {
            return null;
        }
        if (str2 == null) {
            str2 = EMPTY;
        }
        int length = str.length();
        int i3 = i < 0 ? 0 : i;
        if (i3 > length) {
            i3 = length;
        }
        int i4 = i2 < 0 ? 0 : i2;
        if (i4 > length) {
            i4 = length;
        }
        if (i3 <= i4) {
            int i5 = i4;
            i4 = i3;
            i3 = i5;
        }
        return new StringBuffer(((length + i4) - i3) + str2.length() + 1).append(str.substring(0, i4)).append(str2).append(str.substring(i3)).toString();
    }

    public static String overlayString(String str, String str2, int i, int i2) {
        return new StringBuffer((((str2.length() + i) + str.length()) - i2) + 1).append(str.substring(0, i)).append(str2).append(str.substring(i2)).toString();
    }

    public static String prechomp(String str, String str2) {
        int indexOf = str.indexOf(str2);
        return indexOf == -1 ? str : str.substring(indexOf + str2.length());
    }

    public static String remove(String str, char c) {
        if (isEmpty(str) || str.indexOf(c) == -1) {
            return str;
        }
        char[] charArray = str.toCharArray();
        int i = 0;
        for (int i2 = 0; i2 < charArray.length; i2++) {
            if (charArray[i2] != c) {
                charArray[i] = charArray[i2];
                i++;
            }
        }
        return new String(charArray, 0, i);
    }

    public static String remove(String str, String str2) {
        return (isEmpty(str) || isEmpty(str2)) ? str : replace(str, str2, EMPTY, -1);
    }

    public static String removeEnd(String str, String str2) {
        return (isEmpty(str) || isEmpty(str2) || !str.endsWith(str2)) ? str : str.substring(0, str.length() - str2.length());
    }

    public static String removeEndIgnoreCase(String str, String str2) {
        return (isEmpty(str) || isEmpty(str2) || !endsWithIgnoreCase(str, str2)) ? str : str.substring(0, str.length() - str2.length());
    }

    public static String repeat(String str, int i) {
        if (str == null) {
            return null;
        }
        if (i <= 0) {
            return EMPTY;
        }
        int length = str.length();
        if (i == 1 || length == 0) {
            return str;
        }
        if (length == 1 && i <= 8192) {
            return a(i, str.charAt(0));
        }
        int i2 = length * i;
        switch (length) {
            case 1:
                char charAt = str.charAt(0);
                char[] cArr = new char[i2];
                for (int i3 = i - 1; i3 >= 0; i3--) {
                    cArr[i3] = charAt;
                }
                return new String(cArr);
            case 2:
                char charAt2 = str.charAt(0);
                char charAt3 = str.charAt(1);
                char[] cArr2 = new char[i2];
                for (int i4 = (i << 1) - 2; i4 >= 0; i4 = (i4 - 1) - 1) {
                    cArr2[i4] = charAt2;
                    cArr2[i4 + 1] = charAt3;
                }
                return new String(cArr2);
            default:
                StringBuffer stringBuffer = new StringBuffer(i2);
                for (int i5 = 0; i5 < i; i5++) {
                    stringBuffer.append(str);
                }
                return stringBuffer.toString();
        }
    }

    public static String repeat(String str, String str2, int i) {
        return (str == null || str2 == null) ? repeat(str, i) : removeEnd(repeat(String.valueOf(str) + str2, i), str2);
    }

    public static String replace(String str, String str2, String str3) {
        return replace(str, str2, str3, -1);
    }

    public static String replace(String str, String str2, String str3, int i) {
        int indexOf;
        int i2 = 64;
        if (isEmpty(str) || isEmpty(str2) || str3 == null || i == 0 || (indexOf = str.indexOf(str2, 0)) == -1) {
            return str;
        }
        int length = str2.length();
        int length2 = str3.length() - length;
        if (length2 < 0) {
            length2 = 0;
        }
        if (i < 0) {
            i2 = 16;
        } else if (i <= 64) {
            i2 = i;
        }
        StringBuffer stringBuffer = new StringBuffer((i2 * length2) + str.length());
        int i3 = 0;
        while (indexOf != -1) {
            stringBuffer.append(str.substring(i3, indexOf)).append(str3);
            i3 = indexOf + length;
            i--;
            if (i == 0) {
                break;
            }
            indexOf = str.indexOf(str2, i3);
        }
        stringBuffer.append(str.substring(i3));
        return stringBuffer.toString();
    }

    public static String replaceChars(String str, char c, char c2) {
        if (str == null) {
            return null;
        }
        return str.replace(c, c2);
    }

    public static String replaceChars(String str, String str2, String str3) {
        boolean z = false;
        if (isEmpty(str) || isEmpty(str2)) {
            return str;
        }
        if (str3 == null) {
            str3 = EMPTY;
        }
        int length = str3.length();
        int length2 = str.length();
        StringBuffer stringBuffer = new StringBuffer(length2);
        for (int i = 0; i < length2; i++) {
            char charAt = str.charAt(i);
            int indexOf = str2.indexOf(charAt);
            if (indexOf >= 0) {
                z = true;
                if (indexOf < length) {
                    stringBuffer.append(str3.charAt(indexOf));
                }
            } else {
                stringBuffer.append(charAt);
            }
        }
        return z ? stringBuffer.toString() : str;
    }

    public static String replaceEach(String str, String[] strArr, String[] strArr2) {
        return a(str, strArr, strArr2, false, 0);
    }

    public static String replaceEachRepeatedly(String str, String[] strArr, String[] strArr2) {
        return a(str, strArr, strArr2, true, strArr == null ? 0 : strArr.length);
    }

    public static String replaceOnce(String str, String str2, String str3) {
        return replace(str, str2, str3, 1);
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuffer(str).reverse().toString();
    }

    public static String right(String str, int i) {
        if (str == null) {
            return null;
        }
        return i < 0 ? EMPTY : str.length() > i ? str.substring(str.length() - i) : str;
    }

    public static String rightPad(String str, int i) {
        return rightPad(str, i, ' ');
    }

    public static String rightPad(String str, int i, char c) {
        if (str == null) {
            return null;
        }
        int length = i - str.length();
        return length > 0 ? length > 8192 ? rightPad(str, i, String.valueOf(c)) : str.concat(a(length, c)) : str;
    }

    public static String rightPad(String str, int i, String str2) {
        if (str == null) {
            return null;
        }
        if (isEmpty(str2)) {
            str2 = " ";
        }
        int length = str2.length();
        int length2 = i - str.length();
        if (length2 <= 0) {
            return str;
        }
        if (length == 1 && length2 <= 8192) {
            return rightPad(str, i, str2.charAt(0));
        }
        if (length2 == length) {
            return str.concat(str2);
        }
        if (length2 < length) {
            return str.concat(str2.substring(0, length2));
        }
        char[] cArr = new char[length2];
        char[] charArray = str2.toCharArray();
        for (int i2 = 0; i2 < length2; i2++) {
            cArr[i2] = charArray[i2 % length];
        }
        return str.concat(new String(cArr));
    }

    public static String strip(String str) {
        return strip(str, null);
    }

    public static String strip(String str, String str2) {
        return isEmpty(str) ? str : stripEnd(stripStart(str, str2), str2);
    }

    public static String[] stripAll(String[] strArr) {
        return stripAll(strArr, null);
    }

    public static String[] stripAll(String[] strArr, String str) {
        int length;
        if (strArr == null || (length = strArr.length) == 0) {
            return strArr;
        }
        String[] strArr2 = new String[length];
        for (int i = 0; i < length; i++) {
            strArr2[i] = strip(strArr[i], str);
        }
        return strArr2;
    }

    public static String stripEnd(String str, String str2) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        if (str2 == null) {
            while (length != 0 && Character.isWhitespace(str.charAt(length - 1))) {
                length--;
            }
        } else {
            if (str2.length() == 0) {
                return str;
            }
            while (length != 0 && str2.indexOf(str.charAt(length - 1)) != -1) {
                length--;
            }
        }
        return str.substring(0, length);
    }

    public static String stripStart(String str, String str2) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        int i = 0;
        if (str2 == null) {
            while (i != length && Character.isWhitespace(str.charAt(i))) {
                i++;
            }
        } else {
            if (str2.length() == 0) {
                return str;
            }
            while (i != length && str2.indexOf(str.charAt(i)) != -1) {
                i++;
            }
        }
        return str.substring(i);
    }

    public static String stripToEmpty(String str) {
        return str == null ? EMPTY : strip(str, null);
    }

    public static String stripToNull(String str) {
        if (str == null) {
            return null;
        }
        String strip = strip(str, null);
        if (strip.length() != 0) {
            return strip;
        }
        return null;
    }

    public static String substring(String str, int i) {
        if (str == null) {
            return null;
        }
        int length = i < 0 ? str.length() + i : i;
        if (length < 0) {
            length = 0;
        }
        return length > str.length() ? EMPTY : str.substring(length);
    }

    public static String substring(String str, int i, int i2) {
        if (str == null) {
            return null;
        }
        int length = i2 < 0 ? str.length() + i2 : i2;
        if (i < 0) {
            i += str.length();
        }
        if (length > str.length()) {
            length = str.length();
        }
        if (i > length) {
            return EMPTY;
        }
        if (i < 0) {
            i = 0;
        }
        return str.substring(i, length >= 0 ? length : 0);
    }

    public static String substringAfter(String str, String str2) {
        int indexOf;
        return isEmpty(str) ? str : (str2 == null || (indexOf = str.indexOf(str2)) == -1) ? EMPTY : str.substring(indexOf + str2.length());
    }

    public static String substringAfterLast(String str, String str2) {
        int lastIndexOf;
        return isEmpty(str) ? str : (isEmpty(str2) || (lastIndexOf = str.lastIndexOf(str2)) == -1 || lastIndexOf == str.length() - str2.length()) ? EMPTY : str.substring(lastIndexOf + str2.length());
    }

    public static String substringBefore(String str, String str2) {
        if (isEmpty(str) || str2 == null) {
            return str;
        }
        if (str2.length() == 0) {
            return EMPTY;
        }
        int indexOf = str.indexOf(str2);
        return indexOf != -1 ? str.substring(0, indexOf) : str;
    }

    public static String substringBeforeLast(String str, String str2) {
        int lastIndexOf;
        return (isEmpty(str) || isEmpty(str2) || (lastIndexOf = str.lastIndexOf(str2)) == -1) ? str : str.substring(0, lastIndexOf);
    }

    public static String substringBetween(String str, String str2) {
        return substringBetween(str, str2, str2);
    }

    public static String substringBetween(String str, String str2, String str3) {
        int indexOf;
        int indexOf2;
        if (str == null || str2 == null || str3 == null || (indexOf = str.indexOf(str2)) == -1 || (indexOf2 = str.indexOf(str3, str2.length() + indexOf)) == -1) {
            return null;
        }
        return str.substring(str2.length() + indexOf, indexOf2);
    }

    public static String swapCase(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(length);
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            if (Character.isUpperCase(charAt)) {
                charAt = Character.toLowerCase(charAt);
            } else if (Character.isTitleCase(charAt)) {
                charAt = Character.toLowerCase(charAt);
            } else if (Character.isLowerCase(charAt)) {
                charAt = Character.toUpperCase(charAt);
            }
            stringBuffer.append(charAt);
        }
        return stringBuffer.toString();
    }

    public static String trim(String str) {
        if (str == null) {
            return null;
        }
        return str.trim();
    }

    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }

    public static String trimToNull(String str) {
        String trim = trim(str);
        if (isEmpty(trim)) {
            return null;
        }
        return trim;
    }

    public static String uncapitalise(String str) {
        return uncapitalize(str);
    }

    public static String uncapitalize(String str) {
        int length;
        return (str == null || (length = str.length()) == 0) ? str : new StringBuffer(length).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
    }

    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase();
    }

    public static String upperCase(String str, Locale locale) {
        if (str == null) {
            return null;
        }
        return str.toUpperCase(locale);
    }
}
