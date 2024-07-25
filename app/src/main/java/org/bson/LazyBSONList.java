package org.bson;

import com.mokredit.payment.StringUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.bson.LazyBSONObject;
import org.bson.io.BSONByteBuffer;

/* loaded from: classes.dex */
public class LazyBSONList extends LazyBSONObject implements List {
    public LazyBSONList(byte[] data, LazyBSONCallback callback) {
        super(data, callback);
    }

    public LazyBSONList(byte[] data, int offset, LazyBSONCallback callback) {
        super(data, offset, callback);
    }

    public LazyBSONList(BSONByteBuffer buffer, LazyBSONCallback callback) {
        super(buffer, callback);
    }

    public LazyBSONList(BSONByteBuffer buffer, int offset, LazyBSONCallback callback) {
        super(buffer, offset, callback);
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object arg0) {
        return indexOf(arg0) > -1;
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection arg0) {
        for (Object obj : arg0) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.List
    public Object get(int pos) {
        return get(StringUtils.EMPTY + pos);
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator iterator() {
        return new LazyBSONListIterator();
    }

    @Override // java.util.List
    public int indexOf(Object arg0) {
        int pos = 0;
        Iterator it = iterator();
        while (it.hasNext()) {
            Object curr = it.next();
            if (!arg0.equals(curr)) {
                pos++;
            } else {
                return pos;
            }
        }
        return -1;
    }

    @Override // java.util.List
    public int lastIndexOf(Object arg0) {
        int pos = 0;
        int lastFound = -1;
        Iterator it = iterator();
        while (it.hasNext()) {
            Object curr = it.next();
            if (arg0.equals(curr)) {
                lastFound = pos;
            }
            pos++;
        }
        return lastFound;
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        return getElements().size();
    }

    /* loaded from: classes.dex */
    public class LazyBSONListIterator implements Iterator {
        List<LazyBSONObject.ElementRecord> elements;
        int pos = 0;

        public LazyBSONListIterator() {
            this.elements = LazyBSONList.this.getElements();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.pos < this.elements.size();
        }

        @Override // java.util.Iterator
        public Object next() {
            LazyBSONList lazyBSONList = LazyBSONList.this;
            List<LazyBSONObject.ElementRecord> list = this.elements;
            int i = this.pos;
            this.pos = i + 1;
            return lazyBSONList.getElementValue(list.get(i));
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException("Read Only");
        }
    }

    @Override // java.util.List
    public ListIterator listIterator(int arg0) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override // java.util.List
    public ListIterator listIterator() {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(Object arg0) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List
    public void add(int arg0, Object arg1) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection arg0) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List
    public boolean addAll(int arg0, Collection arg1) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object arg0) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List
    public Object remove(int arg0) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection arg0) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection arg0) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List
    public Object set(int arg0, Object arg1) {
        throw new UnsupportedOperationException("Read Only");
    }

    @Override // java.util.List
    public List subList(int arg0, int arg1) {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not Supported");
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray(Object[] arg0) {
        throw new UnsupportedOperationException("Not Supported");
    }
}
