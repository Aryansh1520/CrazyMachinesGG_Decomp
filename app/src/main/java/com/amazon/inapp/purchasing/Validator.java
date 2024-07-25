package com.amazon.inapp.purchasing;

import java.util.Collection;

/* loaded from: classes.dex */
class Validator {
    Validator() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void validateNotEmpty(Collection<? extends Object> collection, String str) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(str + " must not be empty");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void validateNotNull(Object obj, String str) {
        if (obj == null) {
            throw new IllegalArgumentException(str + " must not be null");
        }
    }
}
