package org.bson.util;

import com.mappn.sdk.pay.util.Constants;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/* loaded from: classes.dex */
public abstract class SimplePool<T> {
    final int _max;
    private Queue<T> _stored;

    protected abstract T createNew();

    public SimplePool(int max) {
        this._stored = new ConcurrentLinkedQueue();
        this._max = max;
    }

    public SimplePool() {
        this._stored = new ConcurrentLinkedQueue();
        this._max = Constants.PAYMENT_MAX;
    }

    protected boolean ok(T t) {
        return true;
    }

    public T get() {
        T t = this._stored.poll();
        return t != null ? t : createNew();
    }

    public void done(T t) {
        if (ok(t) && this._stored.size() <= this._max) {
            this._stored.add(t);
        }
    }
}
