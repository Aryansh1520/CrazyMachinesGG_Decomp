package com.mongodb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bson.BSONObject;
import org.bson.LazyBSONCallback;
import org.bson.io.BSONByteBuffer;

/* loaded from: classes.dex */
public class LazyWriteableDBObject extends LazyDBObject {
    private final HashMap<String, Object> writeable;

    public LazyWriteableDBObject(BSONByteBuffer buff, LazyBSONCallback cbk) {
        super(buff, cbk);
        this.writeable = new HashMap<>();
    }

    public LazyWriteableDBObject(BSONByteBuffer buff, int offset, LazyBSONCallback cbk) {
        super(buff, offset, cbk);
        this.writeable = new HashMap<>();
    }

    public LazyWriteableDBObject(byte[] data, LazyBSONCallback cbk) {
        this(data, 0, cbk);
    }

    public LazyWriteableDBObject(byte[] data, int offset, LazyBSONCallback cbk) {
        super(data, offset, cbk);
        this.writeable = new HashMap<>();
    }

    @Override // org.bson.LazyBSONObject, org.bson.BSONObject
    public Object put(String key, Object v) {
        return this.writeable.put(key, v);
    }

    @Override // org.bson.LazyBSONObject, org.bson.BSONObject
    public void putAll(BSONObject o) {
        for (String key : o.keySet()) {
            put(key, o.get(key));
        }
    }

    @Override // org.bson.LazyBSONObject, org.bson.BSONObject
    public void putAll(Map m) {
        this.writeable.putAll(m);
    }

    @Override // org.bson.LazyBSONObject, org.bson.BSONObject
    public Object get(String key) {
        Object o = this.writeable.get(key);
        return o != null ? o : super.get(key);
    }

    @Override // org.bson.LazyBSONObject, org.bson.BSONObject
    public Object removeField(String key) {
        Object o = this.writeable.remove(key);
        return o != null ? o : super.removeField(key);
    }

    @Override // org.bson.LazyBSONObject, org.bson.BSONObject
    public boolean containsField(String s) {
        boolean has = this.writeable.containsKey(s);
        return has ? has : super.containsField(s);
    }

    @Override // org.bson.LazyBSONObject, org.bson.BSONObject
    public Set<String> keySet() {
        Set<String> combined = new HashSet<>();
        combined.addAll(this.writeable.keySet());
        combined.addAll(super.keySet());
        return combined;
    }

    @Override // org.bson.LazyBSONObject
    public boolean isEmpty() {
        return this.writeable.isEmpty() || super.isEmpty();
    }
}
