package org.bson.types;

import java.io.Serializable;

/* loaded from: classes.dex */
public class MinKey implements Serializable {
    private static final long serialVersionUID = 4075901136671855684L;

    public boolean equals(Object o) {
        return o instanceof MinKey;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "MinKey";
    }
}
