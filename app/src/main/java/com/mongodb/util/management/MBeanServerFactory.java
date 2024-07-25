package com.mongodb.util.management;

import com.mongodb.util.management.jmx.JMXMBeanServer;

/* loaded from: classes.dex */
public class MBeanServerFactory {
    private static final MBeanServer mBeanServer;

    static {
        MBeanServer tmp;
        try {
            tmp = new JMXMBeanServer();
        } catch (Throwable th) {
            tmp = new NullMBeanServer();
        }
        mBeanServer = tmp;
    }

    public static MBeanServer getMBeanServer() {
        return mBeanServer;
    }
}
