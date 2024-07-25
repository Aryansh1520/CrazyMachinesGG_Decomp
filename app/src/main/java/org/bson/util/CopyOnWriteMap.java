package org.bson.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bson.util.AbstractCopyOnWriteMap;
import org.bson.util.annotations.GuardedBy;
import org.bson.util.annotations.ThreadSafe;

@ThreadSafe
/* loaded from: classes.dex */
abstract class CopyOnWriteMap<K, V> extends AbstractCopyOnWriteMap<K, V, Map<K, V>> {
    private static final long serialVersionUID = 7935514534647505917L;

    @Override // org.bson.util.AbstractCopyOnWriteMap
    @GuardedBy("internal-lock")
    protected abstract <N extends Map<? extends K, ? extends V>> Map<K, V> copy(N n);

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    /* loaded from: classes.dex */
    public static class Builder<K, V> {
        private AbstractCopyOnWriteMap.View.Type viewType = AbstractCopyOnWriteMap.View.Type.STABLE;
        private final Map<K, V> initialValues = new HashMap();

        Builder() {
        }

        public Builder<K, V> stableViews() {
            this.viewType = AbstractCopyOnWriteMap.View.Type.STABLE;
            return this;
        }

        public Builder<K, V> addAll(Map<? extends K, ? extends V> values) {
            this.initialValues.putAll(values);
            return this;
        }

        public Builder<K, V> liveViews() {
            this.viewType = AbstractCopyOnWriteMap.View.Type.LIVE;
            return this;
        }

        public CopyOnWriteMap<K, V> newHashMap() {
            return new Hash(this.initialValues, this.viewType);
        }

        public CopyOnWriteMap<K, V> newLinkedMap() {
            return new Linked(this.initialValues, this.viewType);
        }
    }

    public static <K, V> CopyOnWriteMap<K, V> newHashMap() {
        Builder<K, V> builder = builder();
        return builder.newHashMap();
    }

    public static <K, V> CopyOnWriteMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
        Builder<K, V> builder = builder();
        return builder.addAll(map).newHashMap();
    }

    public static <K, V> CopyOnWriteMap<K, V> newLinkedMap() {
        Builder<K, V> builder = builder();
        return builder.newLinkedMap();
    }

    public static <K, V> CopyOnWriteMap<K, V> newLinkedMap(Map<? extends K, ? extends V> map) {
        Builder<K, V> builder = builder();
        return builder.addAll(map).newLinkedMap();
    }

    @Deprecated
    protected CopyOnWriteMap(Map<? extends K, ? extends V> map) {
        this(map, AbstractCopyOnWriteMap.View.Type.LIVE);
    }

    @Deprecated
    protected CopyOnWriteMap() {
        this(Collections.emptyMap(), AbstractCopyOnWriteMap.View.Type.LIVE);
    }

    protected CopyOnWriteMap(Map<? extends K, ? extends V> map, AbstractCopyOnWriteMap.View.Type viewType) {
        super(map, viewType);
    }

    protected CopyOnWriteMap(AbstractCopyOnWriteMap.View.Type viewType) {
        super(Collections.emptyMap(), viewType);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Hash<K, V> extends CopyOnWriteMap<K, V> {
        private static final long serialVersionUID = 5221824943734164497L;

        Hash(Map<? extends K, ? extends V> map, AbstractCopyOnWriteMap.View.Type viewType) {
            super(map, viewType);
        }

        @Override // org.bson.util.CopyOnWriteMap, org.bson.util.AbstractCopyOnWriteMap
        public <N extends Map<? extends K, ? extends V>> Map<K, V> copy(N map) {
            return new HashMap(map);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Linked<K, V> extends CopyOnWriteMap<K, V> {
        private static final long serialVersionUID = -8659999465009072124L;

        Linked(Map<? extends K, ? extends V> map, AbstractCopyOnWriteMap.View.Type viewType) {
            super(map, viewType);
        }

        @Override // org.bson.util.CopyOnWriteMap, org.bson.util.AbstractCopyOnWriteMap
        public <N extends Map<? extends K, ? extends V>> Map<K, V> copy(N map) {
            return new LinkedHashMap(map);
        }
    }
}
