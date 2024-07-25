package com.mongodb;

import com.mongodb.DBPort;
import com.mongodb.OutMessage;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class InUseConnectionBean {
    private final long durationMS;
    private final int localPort;
    private final String namespace;
    private final int numDocuments;
    private final OutMessage.OpCode opCode;
    private final String query;
    private final String threadName;

    /* JADX INFO: Access modifiers changed from: package-private */
    public InUseConnectionBean(DBPort port, long currentNanoTime) {
        DBPort.ActiveState activeState = port.getActiveState();
        if (activeState == null) {
            this.durationMS = 0L;
            this.namespace = null;
            this.opCode = null;
            this.query = null;
            this.threadName = null;
            this.numDocuments = 0;
        } else {
            this.durationMS = TimeUnit.NANOSECONDS.toMillis(currentNanoTime - activeState.startTime);
            this.namespace = activeState.outMessage.getNamespace();
            this.opCode = activeState.outMessage.getOpCode();
            this.query = activeState.outMessage.getQuery() != null ? activeState.outMessage.getQuery().toString() : null;
            this.threadName = activeState.threadName;
            this.numDocuments = activeState.outMessage.getNumDocuments();
        }
        this.localPort = port.getLocalPort();
    }

    public String getNamespace() {
        return this.namespace;
    }

    public OutMessage.OpCode getOpCode() {
        return this.opCode;
    }

    public String getQuery() {
        return this.query;
    }

    public int getLocalPort() {
        return this.localPort;
    }

    public long getDurationMS() {
        return this.durationMS;
    }

    public String getThreadName() {
        return this.threadName;
    }

    public int getNumDocuments() {
        return this.numDocuments;
    }
}
