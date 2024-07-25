package com.mongodb.util;

import java.util.ArrayList;
import java.util.Collection;

/* loaded from: classes.dex */
public class UniqueList<T> extends ArrayList<T> {
    private static final long serialVersionUID = -4415279469780082174L;

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(T t) {
        if (contains(t)) {
            return false;
        }
        return super.add(t);
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean addAll(Collection<? extends T> c) {
        boolean added = false;
        for (T t : c) {
            added = added || add(t);
        }
        return added;
    }
}
