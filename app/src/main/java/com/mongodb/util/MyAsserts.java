package com.mongodb.util;

import com.mappn.sdk.pay.chargement.ChargeActivity;
import com.mokredit.payment.StringUtils;
import java.util.Arrays;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class MyAsserts {
    private static Pattern _whiteSpace = Pattern.compile("\\s+", 40);

    /* loaded from: classes.dex */
    public static class MyAssert extends RuntimeException {
        private static final long serialVersionUID = -4415279469780082174L;
        final String _s;

        MyAssert(String s) {
            super(s);
            this._s = s;
        }

        @Override // java.lang.Throwable
        public String toString() {
            return this._s;
        }
    }

    public static void assertTrue(boolean b) {
        if (!b) {
            throw new MyAssert("false");
        }
    }

    public static void assertTrue(boolean b, String msg) {
        if (!b) {
            throw new MyAssert("false : " + msg);
        }
    }

    public static void assertFalse(boolean b) {
        if (b) {
            throw new MyAssert("true");
        }
    }

    public static void assertEquals(int a, int b) {
        if (a != b) {
            throw new MyAssert(StringUtils.EMPTY + a + " != " + b);
        }
    }

    public static void assertEquals(long a, long b) {
        if (a != b) {
            throw new MyAssert(StringUtils.EMPTY + a + " != " + b);
        }
    }

    public static void assertEquals(char a, char b) {
        if (a != b) {
            throw new MyAssert(StringUtils.EMPTY + a + " != " + b);
        }
    }

    public static void assertEquals(short a, short b) {
        if (a != b) {
            throw new MyAssert(StringUtils.EMPTY + ((int) a) + " != " + ((int) b));
        }
    }

    public static void assertEquals(byte expected, byte result) {
        if (expected != result) {
            throw new MyAssert(StringUtils.EMPTY + ((int) expected) + " != " + ((int) result));
        }
    }

    public static void assertEquals(double a, double b, double diff) {
        if (Math.abs(a - b) > diff) {
            throw new MyAssert(StringUtils.EMPTY + a + " != " + b);
        }
    }

    public static void assertEquals(String a, Object b) {
        _assertEquals(a, b == null ? null : b.toString());
    }

    public static void assertEquals(Object a, Object b) {
        _assertEquals(a, b);
    }

    public static void _assertEquals(Object a, Object b) {
        if (a == null) {
            if (b != null) {
                throw new MyAssert("left null, right not");
            }
        } else if (a.equals(b)) {
        } else {
            throw new MyAssert("[" + a + "] != [" + b + "] ");
        }
    }

    public static void assertEquals(String a, String b, String msg) {
        if (a.equals(b)) {
        } else {
            throw new MyAssert("[" + a + "] != [" + b + "] " + msg);
        }
    }

    public static void assertArrayEquals(byte[] expected, byte[] result) {
        if (Arrays.equals(expected, result)) {
        } else {
            throw new MyAssert("These arrays are different, but they might be big so not printing them here");
        }
    }

    public static void assertNotEquals(Object a, Object b) {
        if (a == null) {
            if (b == null) {
                throw new MyAssert("left null, right null");
            }
        } else if (!a.equals(b)) {
        } else {
            throw new MyAssert("[" + a + "] == [" + b + "] ");
        }
    }

    public static void assertClose(String a, Object o) {
        assertClose(a, o == null ? ChargeActivity.TYPE_CHARGE_TYPE_LIST : o.toString());
    }

    public static void assertClose(String a, String b) {
        assertClose(a, b, StringUtils.EMPTY);
    }

    public static void assertClose(String a, String b, String tag) {
        if (isClose(a, b)) {
        } else {
            throw new MyAssert(tag + "[" + a + "] != [" + b + "]");
        }
    }

    public static boolean isClose(String a, String b) {
        return _simplify(a).equalsIgnoreCase(_simplify(b));
    }

    private static String _simplify(String s) {
        return _whiteSpace.matcher(s.trim()).replaceAll(StringUtils.EMPTY);
    }

    public static void assertNull(Object foo) {
        if (foo == null) {
        } else {
            throw new MyAssert("not null [" + foo + "]");
        }
    }

    public static void assertNotNull(Object foo) {
        if (foo != null) {
        } else {
            throw new MyAssert(ChargeActivity.TYPE_CHARGE_TYPE_LIST);
        }
    }

    public static void assertLess(long lower, long higher) {
        if (lower < higher) {
        } else {
            throw new MyAssert(lower + " is higher than " + higher);
        }
    }

    public static void assertLess(double lower, double higher) {
        if (lower < higher) {
        } else {
            throw new MyAssert(lower + " is higher than " + higher);
        }
    }

    public static void assertEmptyString(String s) {
        if (!s.equals(StringUtils.EMPTY)) {
            throw new MyAssert(s);
        }
    }

    public static void fail(String errorMessage) {
        throw new MyAssert(errorMessage);
    }
}
