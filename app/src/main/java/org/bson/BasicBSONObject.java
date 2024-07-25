package org.bson;

import com.mongodb.util.JSON;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.bson.types.ObjectId;

/* loaded from: classes.dex */
public class BasicBSONObject extends LinkedHashMap<String, Object> implements BSONObject {
    private static final long serialVersionUID = -4415279469780082174L;

    public BasicBSONObject() {
    }

    public BasicBSONObject(int size) {
        super(size);
    }

    public BasicBSONObject(String key, Object value) {
        put(key, value);
    }

    public BasicBSONObject(Map m) {
        super(m);
    }

    @Override // org.bson.BSONObject
    public Map toMap() {
        return new LinkedHashMap(this);
    }

    @Override // org.bson.BSONObject
    public Object removeField(String key) {
        return remove(key);
    }

    @Override // org.bson.BSONObject
    public boolean containsField(String field) {
        return super.containsKey((Object) field);
    }

    @Override // org.bson.BSONObject
    @Deprecated
    public boolean containsKey(String key) {
        return containsField(key);
    }

    @Override // org.bson.BSONObject
    public Object get(String key) {
        return super.get((Object) key);
    }

    public int getInt(String key) {
        Object o = get(key);
        if (o == null) {
            throw new NullPointerException("no value for: " + key);
        }
        return BSON.toInt(o);
    }

    public int getInt(String key, int def) {
        Object foo = get(key);
        if (foo == null) {
            return def;
        }
        int def2 = BSON.toInt(foo);
        return def2;
    }

    public long getLong(String key) {
        Object foo = get(key);
        return ((Number) foo).longValue();
    }

    public long getLong(String key, long def) {
        Object foo = get(key);
        if (foo == null) {
            return def;
        }
        long def2 = ((Number) foo).longValue();
        return def2;
    }

    public double getDouble(String key) {
        Object foo = get(key);
        return ((Number) foo).doubleValue();
    }

    public double getDouble(String key, double def) {
        Object foo = get(key);
        if (foo == null) {
            return def;
        }
        double def2 = ((Number) foo).doubleValue();
        return def2;
    }

    public String getString(String key) {
        Object foo = get(key);
        if (foo == null) {
            return null;
        }
        return foo.toString();
    }

    public String getString(String key, String def) {
        Object foo = get(key);
        if (foo == null) {
            return def;
        }
        String def2 = foo.toString();
        return def2;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean def) {
        Object foo = get(key);
        if (foo != null) {
            if (foo instanceof Number) {
                boolean def2 = ((Number) foo).intValue() > 0;
                return def2;
            }
            if (foo instanceof Boolean) {
                boolean def3 = ((Boolean) foo).booleanValue();
                return def3;
            }
            throw new IllegalArgumentException("can't coerce to bool:" + foo.getClass());
        }
        return def;
    }

    public ObjectId getObjectId(String field) {
        return (ObjectId) get(field);
    }

    public ObjectId getObjectId(String field, ObjectId def) {
        Object foo = get(field);
        return foo != null ? (ObjectId) foo : def;
    }

    public Date getDate(String field) {
        return (Date) get(field);
    }

    public Date getDate(String field, Date def) {
        Object foo = get(field);
        return foo != null ? (Date) foo : def;
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Object put(String key, Object val) {
        return super.put((BasicBSONObject) key, (String) val);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map, org.bson.BSONObject
    public void putAll(Map m) {
        for (Map.Entry entry : m.entrySet()) {
            put(entry.getKey().toString(), entry.getValue());
        }
    }

    @Override // org.bson.BSONObject
    public void putAll(BSONObject o) {
        for (String k : o.keySet()) {
            put(k, o.get(k));
        }
    }

    public BasicBSONObject append(String key, Object val) {
        put(key, val);
        return this;
    }

    @Override // java.util.AbstractMap
    public String toString() {
        return JSON.serialize(this);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object o) {
        if (!(o instanceof BSONObject)) {
            return false;
        }
        BSONObject other = (BSONObject) o;
        if (!keySet().equals(other.keySet())) {
            return false;
        }
        for (String key : keySet()) {
            Object a = get(key);
            Object b = other.get(key);
            if (a == null && b != null) {
                return false;
            }
            if (b == null) {
                if (a != null) {
                    return false;
                }
            } else if ((a instanceof Number) && (b instanceof Number)) {
                if (((Number) a).doubleValue() != ((Number) b).doubleValue()) {
                    return false;
                }
            } else if ((a instanceof Pattern) && (b instanceof Pattern)) {
                Pattern p1 = (Pattern) a;
                Pattern p2 = (Pattern) b;
                if (!p1.pattern().equals(p2.pattern()) || p1.flags() != p2.flags()) {
                    return false;
                }
            } else if (!a.equals(b)) {
                return false;
            }
        }
        return true;
    }
}
