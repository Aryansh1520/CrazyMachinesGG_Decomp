package com.mongodb.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class ThreadUtil {
    private static final Map<Long, FastStack<String>> _threads = Collections.synchronizedMap(new HashMap());

    public static void printStackTrace() {
        Exception e = new Exception();
        e.fillInStackTrace();
        e.printStackTrace();
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    public static void pushStatus(String what) {
        pushStatus(Thread.currentThread(), what);
    }

    public static void pushStatus(Thread t, String what) {
        getStatus(t).push(what);
    }

    public static void clearStatus() {
        clearStatus(Thread.currentThread());
    }

    public static void clearStatus(Thread t) {
        getStatus(t).clear();
    }

    public static FastStack<String> getStatus() {
        return getStatus(Thread.currentThread());
    }

    public static FastStack<String> getStatus(Thread t) {
        FastStack<String> s = _threads.get(Long.valueOf(t.getId()));
        if (s == null) {
            FastStack<String> s2 = new FastStack<>();
            _threads.put(Long.valueOf(t.getId()), s2);
            return s2;
        }
        return s;
    }
}
