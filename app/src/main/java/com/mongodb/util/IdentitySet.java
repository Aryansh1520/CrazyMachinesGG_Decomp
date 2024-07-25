package com.mongodb.util;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;

/* loaded from: classes.dex */
public class IdentitySet<T> implements Iterable<T> {
    final IdentityHashMap<T, String> _map = new IdentityHashMap<>();

    public IdentitySet() {
    }

    public IdentitySet(Iterable<T> copy) {
        for (T t : copy) {
            add(t);
        }
    }

    public boolean add(T t) {
        return this._map.put(t, "a") == null;
    }

    public boolean contains(T t) {
        return this._map.containsKey(t);
    }

    public void remove(T t) {
        this._map.remove(t);
    }

    public void removeall(Iterable<T> coll) {
        for (T t : coll) {
            this._map.remove(t);
        }
    }

    public void clear() {
        this._map.clear();
    }

    public int size() {
        return this._map.size();
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return this._map.keySet().iterator();
    }

    public void addAll(Collection<T> c) {
        for (T t : c) {
            add(t);
        }
    }

    public void addAll(IdentitySet<T> c) {
        Iterator i$ = c.iterator();
        while (i$.hasNext()) {
            T t = i$.next();
            add(t);
        }
    }

    public void removeAll(Iterable<T> prev) {
        for (T t : prev) {
            remove(t);
        }
    }
}
