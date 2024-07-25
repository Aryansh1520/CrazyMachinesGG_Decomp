package org.bson.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/* loaded from: classes.dex */
public class StringRangeSet implements Set<String> {
    private static final String[] NUMSTRS = new String[100];
    private static final int NUMSTR_LEN = 100;
    private final int size;

    static {
        for (int i = 0; i < 100; i++) {
            NUMSTRS[i] = String.valueOf(i);
        }
    }

    public StringRangeSet(int size) {
        this.size = size;
    }

    @Override // java.util.Set, java.util.Collection
    public int size() {
        return this.size;
    }

    @Override // java.util.Set, java.util.Collection, java.lang.Iterable
    public Iterator<String> iterator() {
        return new Iterator<String>() { // from class: org.bson.util.StringRangeSet.1
            int index = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < StringRangeSet.this.size;
            }

            @Override // java.util.Iterator
            public String next() {
                if (this.index >= 100) {
                    int i = this.index;
                    this.index = i + 1;
                    return String.valueOf(i);
                }
                String[] strArr = StringRangeSet.NUMSTRS;
                int i2 = this.index;
                this.index = i2 + 1;
                return strArr[i2];
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // java.util.Set, java.util.Collection
    public boolean add(String e) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean addAll(Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean contains(Object o) {
        int t = Integer.parseInt(String.valueOf(o));
        return t >= 0 && t < this.size;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean isEmpty() {
        return false;
    }

    @Override // java.util.Set, java.util.Collection
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Set, java.util.Collection
    public Object[] toArray() {
        String[] array = new String[size()];
        for (int i = 0; i < this.size; i++) {
            if (i < 100) {
                array[i] = NUMSTRS[i];
            } else {
                array[i] = String.valueOf(i);
            }
        }
        return array;
    }

    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }
}
