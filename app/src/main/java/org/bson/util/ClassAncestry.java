package org.bson.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/* loaded from: classes.dex */
class ClassAncestry {
    private static final ConcurrentMap<Class<?>, List<Class<?>>> _ancestryCache = CopyOnWriteMap.newHashMap();

    ClassAncestry() {
    }

    public static <T> List<Class<?>> getAncestry(Class<T> c) {
        ConcurrentMap<Class<?>, List<Class<?>>> cache = getClassAncestryCache();
        while (true) {
            List<Class<?>> cachedResult = cache.get(c);
            if (cachedResult != null) {
                return cachedResult;
            }
            cache.putIfAbsent(c, computeAncestry(c));
        }
    }

    private static List<Class<?>> computeAncestry(Class<?> c) {
        List<Class<?>> result = new ArrayList<>();
        result.add(Object.class);
        computeAncestry(c, result);
        Collections.reverse(result);
        return Collections.unmodifiableList(new ArrayList(result));
    }

    private static <T> void computeAncestry(Class<T> c, List<Class<?>> result) {
        if (c != null && c != Object.class) {
            Class<?>[] interfaces = c.getInterfaces();
            for (int i = interfaces.length - 1; i >= 0; i--) {
                computeAncestry(interfaces[i], result);
            }
            computeAncestry(c.getSuperclass(), result);
            if (!result.contains(c)) {
                result.add(c);
            }
        }
    }

    private static ConcurrentMap<Class<?>, List<Class<?>>> getClassAncestryCache() {
        return _ancestryCache;
    }
}
