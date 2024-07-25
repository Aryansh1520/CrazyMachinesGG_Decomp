package org.bson.util;

/* loaded from: classes.dex */
public class Assertions {
    public static <T> T notNull(String name, T notNull) throws IllegalArgumentException {
        if (notNull == null) {
            throw new NullArgumentException(name);
        }
        return notNull;
    }

    public static void isTrue(String name, boolean check) throws IllegalArgumentException {
        if (!check) {
            throw new IllegalArgumentException(name);
        }
    }

    private Assertions() {
    }

    /* loaded from: classes.dex */
    static class NullArgumentException extends IllegalArgumentException {
        private static final long serialVersionUID = 6178592463723624585L;

        NullArgumentException(String name) {
            super(name + " should not be null!");
        }
    }
}
