package com.mongodb.util;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
/* loaded from: classes.dex */
public abstract class ThreadPool<T> {
    private final AtomicInteger _inProgress;
    final int _maxThreads;
    private final ThreadPool<T>.MyThreadGroup _myThreadGroup;
    final String _name;
    private final BlockingQueue<T> _queue;
    private final List<ThreadPool<T>.MyThread> _threads;

    public abstract void handle(T t) throws Exception;

    public abstract void handleError(T t, Exception exc);

    public ThreadPool(String name, int numThreads) {
        this(name, numThreads, Integer.MAX_VALUE);
    }

    public ThreadPool(String name, int numThreads, int maxQueueSize) {
        this._inProgress = new AtomicInteger(0);
        this._threads = new Vector();
        this._name = name;
        this._maxThreads = numThreads;
        this._queue = new LinkedBlockingQueue(maxQueueSize);
        this._myThreadGroup = new MyThreadGroup();
        this._threads.add(new MyThread());
    }

    public int queueSize() {
        return this._queue.size();
    }

    public boolean offer(T t) {
        if ((this._queue.size() > 0 || this._inProgress.get() == this._threads.size()) && this._threads.size() < this._maxThreads) {
            this._threads.add(new MyThread());
        }
        return this._queue.offer(t);
    }

    public int inProgress() {
        return this._inProgress.get();
    }

    public int numThreads() {
        return this._threads.size();
    }

    /* loaded from: classes.dex */
    class MyThreadGroup extends ThreadGroup {
        MyThreadGroup() {
            super("ThreadPool.MyThreadGroup:" + ThreadPool.this._name);
        }

        @Override // java.lang.ThreadGroup, java.lang.Thread.UncaughtExceptionHandler
        public void uncaughtException(Thread t, Throwable e) {
            for (int i = 0; i < ThreadPool.this._threads.size(); i++) {
                if (ThreadPool.this._threads.get(i) == t) {
                    ThreadPool.this._threads.remove(i);
                    return;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    class MyThread extends Thread {
        MyThread() {
            super(ThreadPool.this._myThreadGroup, "ThreadPool.MyThread:" + ThreadPool.this._name + ":" + ThreadPool.this._threads.size());
            setDaemon(true);
            start();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                Object obj = null;
                try {
                    obj = ThreadPool.this._queue.take();
                } catch (InterruptedException e) {
                }
                if (obj != null) {
                    try {
                        try {
                            ThreadPool.this._inProgress.incrementAndGet();
                            ThreadPool.this.handle(obj);
                            ThreadPool.this._inProgress.decrementAndGet();
                        } catch (Throwable th) {
                            ThreadPool.this._inProgress.decrementAndGet();
                            throw th;
                        }
                    } catch (Exception e2) {
                        ThreadPool.this.handleError(obj, e2);
                        ThreadPool.this._inProgress.decrementAndGet();
                    }
                }
            }
        }
    }
}
