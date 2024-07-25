package com.mongodb;

import com.mokredit.payment.StringUtils;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.bson.BSONObject;

/* loaded from: classes.dex */
public abstract class ReflectionDBObject implements DBObject {
    Object _id;
    JavaWrapper _wrapper;
    private static final Map<Class, JavaWrapper> _wrappers = Collections.synchronizedMap(new HashMap());
    private static final Set<String> IGNORE_FIELDS = new HashSet();

    @Override // org.bson.BSONObject
    public Object get(String key) {
        return getWrapper().get(this, key);
    }

    @Override // org.bson.BSONObject
    public Set<String> keySet() {
        return getWrapper().keySet();
    }

    @Override // org.bson.BSONObject
    @Deprecated
    public boolean containsKey(String s) {
        return containsField(s);
    }

    @Override // org.bson.BSONObject
    public boolean containsField(String s) {
        return getWrapper().containsKey(s);
    }

    @Override // org.bson.BSONObject
    public Object put(String key, Object v) {
        return getWrapper().set(this, key, v);
    }

    @Override // org.bson.BSONObject
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

    public Object get_id() {
        return this._id;
    }

    public void set_id(Object id) {
        this._id = id;
    }

    @Override // com.mongodb.DBObject
    public boolean isPartialObject() {
        return false;
    }

    @Override // org.bson.BSONObject
    public Map toMap() {
        Map m = new HashMap();
        for (String str : keySet()) {
            m.put(str, get(((Object) str) + StringUtils.EMPTY));
        }
        return m;
    }

    @Override // com.mongodb.DBObject
    public void markAsPartialObject() {
        throw new RuntimeException("ReflectionDBObjects can't be partial");
    }

    @Override // org.bson.BSONObject
    public Object removeField(String key) {
        throw new RuntimeException("can't remove from a ReflectionDBObject");
    }

    JavaWrapper getWrapper() {
        if (this._wrapper != null) {
            return this._wrapper;
        }
        this._wrapper = getWrapper(getClass());
        return this._wrapper;
    }

    /* loaded from: classes.dex */
    public static class JavaWrapper {
        final Class _class;
        final Map<String, FieldInfo> _fields = new TreeMap();
        final Set<String> _keys;
        final String _name;

        JavaWrapper(Class c) {
            this._class = c;
            this._name = c.getName();
            Method[] arr$ = c.getMethods();
            for (Method m : arr$) {
                if (m.getName().startsWith("get") || m.getName().startsWith("set")) {
                    String name = m.getName().substring(3);
                    if (name.length() != 0 && !ReflectionDBObject.IGNORE_FIELDS.contains(name)) {
                        Class type = m.getName().startsWith("get") ? m.getReturnType() : m.getParameterTypes()[0];
                        FieldInfo fi = this._fields.get(name);
                        if (fi == null) {
                            fi = new FieldInfo(name, type);
                            this._fields.put(name, fi);
                        }
                        if (m.getName().startsWith("get")) {
                            fi._getter = m;
                        } else {
                            fi._setter = m;
                        }
                    }
                }
            }
            Set<String> names = new HashSet<>(this._fields.keySet());
            for (String name2 : names) {
                if (!this._fields.get(name2).ok()) {
                    this._fields.remove(name2);
                }
            }
            this._keys = Collections.unmodifiableSet(this._fields.keySet());
        }

        public Set<String> keySet() {
            return this._keys;
        }

        @Deprecated
        public boolean containsKey(String key) {
            return this._keys.contains(key);
        }

        public Object get(ReflectionDBObject t, String name) {
            FieldInfo i = this._fields.get(name);
            if (i == null) {
                return null;
            }
            try {
                return i._getter.invoke(t, new Object[0]);
            } catch (Exception e) {
                throw new RuntimeException("could not invoke getter for [" + name + "] on [" + this._name + "]", e);
            }
        }

        public Object set(ReflectionDBObject t, String name, Object val) {
            FieldInfo i = this._fields.get(name);
            if (i == null) {
                throw new IllegalArgumentException("no field [" + name + "] on [" + this._name + "]");
            }
            try {
                return i._setter.invoke(t, val);
            } catch (Exception e) {
                throw new RuntimeException("could not invoke setter for [" + name + "] on [" + this._name + "]", e);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Class getInternalClass(String path) {
            String cur = path;
            String next = null;
            int idx = path.indexOf(".");
            if (idx >= 0) {
                cur = path.substring(0, idx);
                next = path.substring(idx + 1);
            }
            FieldInfo fi = this._fields.get(cur);
            if (fi == null) {
                return null;
            }
            if (next == null) {
                return fi._class;
            }
            JavaWrapper w = ReflectionDBObject.getWrapperIfReflectionObject(fi._class);
            if (w != null) {
                return w.getInternalClass(next);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class FieldInfo {
        final Class _class;
        Method _getter;
        final String _name;
        Method _setter;

        FieldInfo(String name, Class c) {
            this._name = name;
            this._class = c;
        }

        boolean ok() {
            return (this._getter == null || this._setter == null) ? false : true;
        }
    }

    public static JavaWrapper getWrapperIfReflectionObject(Class c) {
        if (ReflectionDBObject.class.isAssignableFrom(c)) {
            return getWrapper(c);
        }
        return null;
    }

    public static JavaWrapper getWrapper(Class c) {
        JavaWrapper w = _wrappers.get(c);
        if (w == null) {
            JavaWrapper w2 = new JavaWrapper(c);
            _wrappers.put(c, w2);
            return w2;
        }
        return w;
    }

    static {
        IGNORE_FIELDS.add("Int");
    }
}
