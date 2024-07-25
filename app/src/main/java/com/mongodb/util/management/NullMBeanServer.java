package com.mongodb.util.management;

/* loaded from: classes.dex */
public class NullMBeanServer implements MBeanServer {
    @Override // com.mongodb.util.management.MBeanServer
    public boolean isRegistered(String mBeanName) {
        return false;
    }

    @Override // com.mongodb.util.management.MBeanServer
    public void unregisterMBean(String mBeanName) {
    }

    @Override // com.mongodb.util.management.MBeanServer
    public void registerMBean(Object mBean, String mBeanName) {
    }
}
