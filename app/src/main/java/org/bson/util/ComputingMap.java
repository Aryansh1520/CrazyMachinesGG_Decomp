package org.bson.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes.dex */
final class ComputingMap<K, V> implements Map<K, V>, Function<K, V> {
    private final Function<K, V> function;
    private final ConcurrentMap<K, V> map;

    public static <K, V> Map<K, V> create(Function<K, V> function) {
        return new ComputingMap(CopyOnWriteMap.newHashMap(), function);
    }

    ComputingMap(ConcurrentMap<K, V> map, Function<K, V> function) {
        this.map = (ConcurrentMap) Assertions.notNull("map", map);
        this.function = (Function) Assertions.notNull("function", function);
    }

    @Override // java.util.Map
    public V get(Object key) {
        while (true) {
            V v = this.map.get(key);
            if (v == null) {
                V value = this.function.apply(key);
                if (value != null) {
                    this.map.putIfAbsent(key, value);
                } else {
                    return null;
                }
            } else {
                return v;
            }
        }
    }

    @Override // org.bson.util.Function
    public V apply(K k) {
        return get(k);
    }

    @Override // java.util.Map
    public V putIfAbsent(K key, V value) {
        return this.map.putIfAbsent(key, value);
    }

    @Override // java.util.Map
    public boolean remove(Object key, Object value) {
        return this.map.remove(key, value);
    }

    @Override // java.util.Map
    public boolean replace(K key, V oldValue, V newValue) {
        return this.map.replace(key, oldValue, newValue);
    }

    @Override // java.util.Map
    public V replace(K key, V value) {
        return this.map.replace(key, value);
    }

    @Override // java.util.Map
    public int size() {
        return this.map.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        return this.map.put(key, value);
    }

    @Override // java.util.Map
    public V remove(Object key) {
        return this.map.remove(key);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> m) {
        this.map.putAll(m);
    }

    @Override // java.util.Map
    public void clear() {
        this.map.clear();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return this.map.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    @Override // java.util.Map
    public boolean equals(Object o) {
        return this.map.equals(o);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.map.hashCode();
    }
}
