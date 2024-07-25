package com.amazon.ags.jni;

/* loaded from: classes.dex */
public abstract class JniResponseHandler {
    protected long m_CallbackPointer;
    protected int m_DeveloperTag;

    public JniResponseHandler(int developerTag, long callbackPointer) {
        this.m_DeveloperTag = developerTag;
        this.m_CallbackPointer = callbackPointer;
    }
}
