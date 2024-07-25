package org.bson.util;

import com.mappn.sdk.pay.util.Constants;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.bson.util.annotations.GuardedBy;
import org.bson.util.annotations.ThreadSafe;

/* JADX INFO: Access modifiers changed from: package-private */
@ThreadSafe
/* loaded from: classes.dex */
public abstract class AbstractCopyOnWriteMap<K, V, M extends Map<K, V>> implements ConcurrentMap<K, V>, Serializable {
    private static final long serialVersionUID = 4508989182041753878L;

    @GuardedBy(Constants.RES_LOCK)
    private volatile M delegate;
    private final transient Lock lock = new ReentrantLock();
    private final View<K, V> view;

    @GuardedBy(Constants.RES_LOCK)
    abstract <N extends Map<? extends K, ? extends V>> M copy(N n);

    /* JADX INFO: Access modifiers changed from: protected */
    public <N extends Map<? extends K, ? extends V>> AbstractCopyOnWriteMap(N map, View.Type viewType) {
        this.delegate = (M) Assertions.notNull("delegate", copy((Map) Assertions.notNull("map", map)));
        this.view = ((View.Type) Assertions.notNull("viewType", viewType)).get(this);
    }

    @Override // java.util.Map
    public final void clear() {
        this.lock.lock();
        try {
            set(copy(Collections.emptyMap()));
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Map
    public final V remove(Object obj) {
        this.lock.lock();
        try {
            if (this.delegate.containsKey(obj)) {
                M copy = copy();
                try {
                    return (V) copy.remove(obj);
                } finally {
                    set(copy);
                }
            }
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean remove(Object key, Object value) {
        Lock lock;
        this.lock.lock();
        try {
            if (this.delegate.containsKey(key) && equals(value, this.delegate.get(key))) {
                M map = copy();
                map.remove(key);
                set(map);
                return true;
            }
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public boolean replace(K key, V oldValue, V newValue) {
        Lock lock;
        this.lock.lock();
        try {
            if (this.delegate.containsKey(key) && equals(oldValue, this.delegate.get(key))) {
                M map = copy();
                map.put(key, newValue);
                set(map);
                return true;
            }
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V replace(K k, V v) {
        this.lock.lock();
        try {
            if (this.delegate.containsKey(k)) {
                M copy = copy();
                try {
                    return (V) copy.put(k, v);
                } finally {
                    set(copy);
                }
            }
            return null;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Map
    public final V put(K k, V v) {
        this.lock.lock();
        try {
            M copy = copy();
            try {
                return (V) copy.put(k, v);
            } finally {
                set(copy);
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.concurrent.ConcurrentMap, java.util.Map
    public V putIfAbsent(K k, V v) {
        V v2;
        this.lock.lock();
        try {
            if (!this.delegate.containsKey(k)) {
                M copy = copy();
                try {
                    v2 = (V) copy.put(k, v);
                } finally {
                    set(copy);
                }
            } else {
                v2 = (V) this.delegate.get(k);
            }
            return v2;
        } finally {
            this.lock.unlock();
        }
    }

    @Override // java.util.Map
    public final void putAll(Map<? extends K, ? extends V> t) {
        this.lock.lock();
        try {
            M map = copy();
            map.putAll(t);
            set(map);
        } finally {
            this.lock.unlock();
        }
    }

    protected M copy() {
        this.lock.lock();
        try {
            return copy(this.delegate);
        } finally {
            this.lock.unlock();
        }
    }

    @GuardedBy(Constants.RES_LOCK)
    protected void set(M map) {
        this.delegate = map;
    }

    @Override // java.util.Map
    public final Set<Map.Entry<K, V>> entrySet() {
        return this.view.entrySet();
    }

    @Override // java.util.Map
    public final Set<K> keySet() {
        return this.view.keySet();
    }

    @Override // java.util.Map
    public final Collection<V> values() {
        return this.view.values();
    }

    @Override // java.util.Map
    public final boolean containsKey(Object key) {
        return this.delegate.containsKey(key);
    }

    @Override // java.util.Map
    public final boolean containsValue(Object value) {
        return this.delegate.containsValue(value);
    }

    @Override // java.util.Map
    public final V get(Object obj) {
        return (V) this.delegate.get(obj);
    }

    @Override // java.util.Map
    public final boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override // java.util.Map
    public final int size() {
        return this.delegate.size();
    }

    @Override // java.util.Map
    public final boolean equals(Object o) {
        return this.delegate.equals(o);
    }

    @Override // java.util.Map
    public final int hashCode() {
        return this.delegate.hashCode();
    }

    protected final M getDelegate() {
        return this.delegate;
    }

    public String toString() {
        return this.delegate.toString();
    }

    /* loaded from: classes.dex */
    private class KeySet extends CollectionView<K> implements Set<K> {
        private KeySet() {
        }

        @Override // org.bson.util.AbstractCopyOnWriteMap.CollectionView
        Collection<K> getDelegate() {
            return AbstractCopyOnWriteMap.this.delegate.keySet();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection, java.util.Set
        public void clear() {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                Map copy = AbstractCopyOnWriteMap.this.copy();
                copy.keySet().clear();
                AbstractCopyOnWriteMap.this.set(copy);
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }

        @Override // java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return AbstractCopyOnWriteMap.this.remove(o) != null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                Map copy = AbstractCopyOnWriteMap.this.copy();
                try {
                    return copy.keySet().removeAll(c);
                } finally {
                    AbstractCopyOnWriteMap.this.set(copy);
                }
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                Map copy = AbstractCopyOnWriteMap.this.copy();
                try {
                    return copy.keySet().retainAll(c);
                } finally {
                    AbstractCopyOnWriteMap.this.set(copy);
                }
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
    }

    /* loaded from: classes.dex */
    private final class Values extends CollectionView<V> {
        private Values() {
        }

        @Override // org.bson.util.AbstractCopyOnWriteMap.CollectionView
        Collection<V> getDelegate() {
            return AbstractCopyOnWriteMap.this.delegate.values();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection
        public void clear() {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                Map copy = AbstractCopyOnWriteMap.this.copy();
                copy.values().clear();
                AbstractCopyOnWriteMap.this.set(copy);
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection
        public boolean remove(Object o) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                if (contains(o)) {
                    Map copy = AbstractCopyOnWriteMap.this.copy();
                    try {
                        return copy.values().remove(o);
                    } finally {
                        AbstractCopyOnWriteMap.this.set(copy);
                    }
                }
                return false;
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection
        public boolean removeAll(Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                Map copy = AbstractCopyOnWriteMap.this.copy();
                try {
                    return copy.values().removeAll(c);
                } finally {
                    AbstractCopyOnWriteMap.this.set(copy);
                }
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection
        public boolean retainAll(Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                Map copy = AbstractCopyOnWriteMap.this.copy();
                try {
                    return copy.values().retainAll(c);
                } finally {
                    AbstractCopyOnWriteMap.this.set(copy);
                }
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
    }

    /* loaded from: classes.dex */
    private class EntrySet extends CollectionView<Map.Entry<K, V>> implements Set<Map.Entry<K, V>> {
        private EntrySet() {
        }

        @Override // org.bson.util.AbstractCopyOnWriteMap.CollectionView
        Collection<Map.Entry<K, V>> getDelegate() {
            return AbstractCopyOnWriteMap.this.delegate.entrySet();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection, java.util.Set
        public void clear() {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                Map copy = AbstractCopyOnWriteMap.this.copy();
                copy.entrySet().clear();
                AbstractCopyOnWriteMap.this.set(copy);
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                if (contains(o)) {
                    Map copy = AbstractCopyOnWriteMap.this.copy();
                    try {
                        return copy.entrySet().remove(o);
                    } finally {
                        AbstractCopyOnWriteMap.this.set(copy);
                    }
                }
                return false;
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection, java.util.Set
        public boolean removeAll(Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                Map copy = AbstractCopyOnWriteMap.this.copy();
                try {
                    return copy.entrySet().removeAll(c);
                } finally {
                    AbstractCopyOnWriteMap.this.set(copy);
                }
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> c) {
            AbstractCopyOnWriteMap.this.lock.lock();
            try {
                Map copy = AbstractCopyOnWriteMap.this.copy();
                try {
                    return copy.entrySet().retainAll(c);
                } finally {
                    AbstractCopyOnWriteMap.this.set(copy);
                }
            } finally {
                AbstractCopyOnWriteMap.this.lock.unlock();
            }
        }
    }

    /* loaded from: classes.dex */
    private static class UnmodifiableIterator<T> implements Iterator<T> {
        private final Iterator<T> delegate;

        public UnmodifiableIterator(Iterator<T> delegate) {
            this.delegate = delegate;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.delegate.hasNext();
        }

        @Override // java.util.Iterator
        public T next() {
            return this.delegate.next();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: classes.dex */
    protected static abstract class CollectionView<E> implements Collection<E> {
        abstract Collection<E> getDelegate();

        protected CollectionView() {
        }

        @Override // java.util.Collection
        public final boolean contains(Object o) {
            return getDelegate().contains(o);
        }

        @Override // java.util.Collection
        public final boolean containsAll(Collection<?> c) {
            return getDelegate().containsAll(c);
        }

        @Override // java.util.Collection, java.lang.Iterable
        public final Iterator<E> iterator() {
            return new UnmodifiableIterator(getDelegate().iterator());
        }

        @Override // java.util.Collection
        public final boolean isEmpty() {
            return getDelegate().isEmpty();
        }

        @Override // java.util.Collection
        public final int size() {
            return getDelegate().size();
        }

        @Override // java.util.Collection
        public final Object[] toArray() {
            return getDelegate().toArray();
        }

        @Override // java.util.Collection
        public final <T> T[] toArray(T[] tArr) {
            return (T[]) getDelegate().toArray(tArr);
        }

        @Override // java.util.Collection
        public int hashCode() {
            return getDelegate().hashCode();
        }

        @Override // java.util.Collection
        public boolean equals(Object obj) {
            return getDelegate().equals(obj);
        }

        public String toString() {
            return getDelegate().toString();
        }

        @Override // java.util.Collection
        public final boolean add(E o) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Collection
        public final boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }
    }

    private boolean equals(Object o1, Object o2) {
        if (o1 == null) {
            return o2 == null;
        }
        return o1.equals(o2);
    }

    /* loaded from: classes.dex */
    public static abstract class View<K, V> {

        /* loaded from: classes.dex */
        public enum Type {
            STABLE { // from class: org.bson.util.AbstractCopyOnWriteMap.View.Type.1
                @Override // org.bson.util.AbstractCopyOnWriteMap.View.Type
                <K, V, M extends Map<K, V>> View<K, V> get(AbstractCopyOnWriteMap<K, V, M> host) {
                    host.getClass();
                    return new Immutable();
                }
            },
            LIVE { // from class: org.bson.util.AbstractCopyOnWriteMap.View.Type.2
                @Override // org.bson.util.AbstractCopyOnWriteMap.View.Type
                <K, V, M extends Map<K, V>> View<K, V> get(AbstractCopyOnWriteMap<K, V, M> host) {
                    host.getClass();
                    return new Mutable(host);
                }
            };

            abstract <K, V, M extends Map<K, V>> View<K, V> get(AbstractCopyOnWriteMap<K, V, M> abstractCopyOnWriteMap);
        }

        abstract Set<Map.Entry<K, V>> entrySet();

        abstract Set<K> keySet();

        abstract Collection<V> values();

        View() {
        }
    }

    /* loaded from: classes.dex */
    final class Immutable extends View<K, V> implements Serializable {
        private static final long serialVersionUID = -4158727180429303818L;

        Immutable() {
        }

        @Override // org.bson.util.AbstractCopyOnWriteMap.View
        public Set<K> keySet() {
            return Collections.unmodifiableSet(AbstractCopyOnWriteMap.this.delegate.keySet());
        }

        @Override // org.bson.util.AbstractCopyOnWriteMap.View
        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.unmodifiableSet(AbstractCopyOnWriteMap.this.delegate.entrySet());
        }

        @Override // org.bson.util.AbstractCopyOnWriteMap.View
        public Collection<V> values() {
            return Collections.unmodifiableCollection(AbstractCopyOnWriteMap.this.delegate.values());
        }
    }

    /* loaded from: classes.dex */
    final class Mutable extends View<K, V> implements Serializable {
        private static final long serialVersionUID = 1624520291194797634L;
        private final transient AbstractCopyOnWriteMap<K, V, M>.EntrySet entrySet;
        private final transient AbstractCopyOnWriteMap<K, V, M>.KeySet keySet;
        final /* synthetic */ AbstractCopyOnWriteMap this$0;
        private final transient AbstractCopyOnWriteMap<K, V, M>.Values values;

        Mutable(AbstractCopyOnWriteMap abstractCopyOnWriteMap) {
            this.this$0 = abstractCopyOnWriteMap;
            this.keySet = new KeySet();
            this.entrySet = new EntrySet();
            this.values = new Values();
        }

        @Override // org.bson.util.AbstractCopyOnWriteMap.View
        public Set<K> keySet() {
            return this.keySet;
        }

        @Override // org.bson.util.AbstractCopyOnWriteMap.View
        public Set<Map.Entry<K, V>> entrySet() {
            return this.entrySet;
        }

        @Override // org.bson.util.AbstractCopyOnWriteMap.View
        public Collection<V> values() {
            return this.values;
        }
    }
}
