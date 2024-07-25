package com.mongodb.util.management.jmx;

import com.mongodb.util.management.JMException;
import com.mongodb.util.management.MBeanServer;
import java.lang.management.ManagementFactory;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

/* loaded from: classes.dex */
public class JMXMBeanServer implements MBeanServer {
    private final javax.management.MBeanServer server = ManagementFactory.getPlatformMBeanServer();

    @Override // com.mongodb.util.management.MBeanServer
    public boolean isRegistered(String mBeanName) throws JMException {
        return this.server.isRegistered(createObjectName(mBeanName));
    }

    @Override // com.mongodb.util.management.MBeanServer
    public void unregisterMBean(String mBeanName) throws JMException {
        try {
            this.server.unregisterMBean(createObjectName(mBeanName));
        } catch (MBeanRegistrationException e) {
            throw new JMException(e);
        } catch (InstanceNotFoundException e2) {
            throw new JMException(e2);
        }
    }

    @Override // com.mongodb.util.management.MBeanServer
    public void registerMBean(Object mBean, String mBeanName) throws JMException {
        try {
            this.server.registerMBean(mBean, createObjectName(mBeanName));
        } catch (InstanceAlreadyExistsException e) {
            throw new JMException(e);
        } catch (MBeanRegistrationException e2) {
            throw new JMException(e2);
        } catch (NotCompliantMBeanException e3) {
            throw new JMException(e3);
        }
    }

    private ObjectName createObjectName(String mBeanName) throws JMException {
        try {
            return new ObjectName(mBeanName);
        } catch (MalformedObjectNameException e) {
            throw new JMException(e);
        }
    }
}
