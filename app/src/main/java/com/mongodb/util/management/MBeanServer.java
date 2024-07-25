package com.mongodb.util.management;

/* loaded from: classes.dex */
public interface MBeanServer {
    boolean isRegistered(String str) throws JMException;

    void registerMBean(Object obj, String str) throws JMException;

    void unregisterMBean(String str) throws JMException;
}
