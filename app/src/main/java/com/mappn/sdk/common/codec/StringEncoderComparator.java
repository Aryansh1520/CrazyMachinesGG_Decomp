package com.mappn.sdk.common.codec;

import java.util.Comparator;

/* loaded from: classes.dex */
public class StringEncoderComparator implements Comparator {
    private final StringEncoder a;

    public StringEncoderComparator() {
        this.a = null;
    }

    public StringEncoderComparator(StringEncoder stringEncoder) {
        this.a = stringEncoder;
    }

    @Override // java.util.Comparator
    public int compare(Object obj, Object obj2) {
        try {
            return ((Comparable) this.a.encode(obj)).compareTo((Comparable) this.a.encode(obj2));
        } catch (EncoderException e) {
            return 0;
        }
    }
}
