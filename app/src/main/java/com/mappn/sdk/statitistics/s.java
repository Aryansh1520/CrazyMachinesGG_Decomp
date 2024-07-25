package com.mappn.sdk.statitistics;

import android.os.Handler;
import android.os.HandlerThread;
import com.mappn.sdk.statitistics.entity.GfanPayEventPackage;
import com.mappn.sdk.statitistics.entity.GfanPaySession;
import com.mappn.sdk.statitistics.entity.GfanPayTMessage;
import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class s implements Runnable {
    private static HandlerThread d;
    private int b = 0;
    private static s a = null;
    private static Handler c = null;
    private static int e = 0;
    private static boolean f = false;
    private static int g = 20480;

    static {
        HandlerThread handlerThread = new HandlerThread("ProcessingThread");
        d = handlerThread;
        handlerThread.start();
    }

    private s() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized s a() {
        s sVar;
        synchronized (s.class) {
            d();
            sVar = a;
        }
        return sVar;
    }

    private static void a(GfanPayEventPackage gfanPayEventPackage) {
        new String[1][0] = "Send Success, Clear Data";
        GfanPayTCLog.a();
        j.a(GfanPayAgent.getContext());
        List<GfanPayTMessage> list = gfanPayEventPackage.mTMessages;
        j.a(gfanPayEventPackage.mMaxActivityId);
        j.b(gfanPayEventPackage.mMaxAppEventId);
        j.c(gfanPayEventPackage.mMaxErrorId);
        for (GfanPayTMessage gfanPayTMessage : list) {
            switch (gfanPayTMessage.mMsgType) {
                case 1:
                    GfanPayAgent.a(GfanPayAgent.getContext(), false);
                    break;
                case 2:
                    GfanPaySession gfanPaySession = gfanPayTMessage.session;
                    if (gfanPaySession.mStatus == 1) {
                        j.a(gfanPaySession.id);
                        break;
                    } else if (gfanPaySession.mStatus == 3) {
                        j.b(gfanPaySession.id);
                        j.c(gfanPaySession.id);
                        j.e(gfanPaySession.id);
                        break;
                    } else {
                        break;
                    }
            }
        }
        j.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized Handler b() {
        Handler handler;
        synchronized (s.class) {
            d();
            handler = c;
        }
        return handler;
    }

    private static synchronized void d() {
        synchronized (s.class) {
            if (a == null) {
                a = new s();
                new Thread(a).start();
                c = new t(d.getLooper());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final synchronized void c() {
        if (!GfanPayNetworkWatcher.b()) {
            new String[1][0] = "network is disabled.";
            GfanPayTCLog.a();
        } else if (GfanPayAgent.c == 0 && GfanPayAgent.b && !GfanPayNetworkWatcher.a()) {
            new String[1][0] = "wifi is not connected.";
            GfanPayTCLog.a();
        } else if (this.b <= 1) {
            this.b++;
            String[] strArr = {"TCPostData thread will notify...", "onlyhere"};
            GfanPayTCLog.a();
            notify();
        }
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        boolean a2;
        try {
            synchronized (this) {
                while (true) {
                    if (this.b == 0 || !GfanPayNetworkWatcher.b()) {
                        try {
                            String[] strArr = {"TCPostData thread will wait...", "onlyhere"};
                            GfanPayTCLog.a();
                            wait();
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                    }
                    try {
                        String[] strArr2 = {"Test", "[HTTPThread]pre send"};
                        GfanPayTCLog.a();
                        this.b--;
                        if (GfanPayAgent.getContext() == null) {
                            new String[1][0] = "TCAgent.getContext is null...";
                            GfanPayTCLog.a();
                            a2 = false;
                        } else {
                            new String[1][0] = "Send Data Now...";
                            GfanPayTCLog.a();
                            GfanPayEventPackage gfanPayEventPackage = new GfanPayEventPackage();
                            gfanPayEventPackage.mDeviceId = r.a(GfanPayAgent.getContext());
                            gfanPayEventPackage.mDeveploperAppkey = GfanPayAgent.getAppId();
                            gfanPayEventPackage.mAppProfile = GfanPayAgent.a();
                            gfanPayEventPackage.mDeviceProfile = GfanPayAgent.b();
                            int packSizeNoSub = gfanPayEventPackage.getPackSizeNoSub() + 3 + 0;
                            if (GfanPayAgent.a(GfanPayAgent.getContext())) {
                                new String[1][0] = "Prepare init Device profile";
                                GfanPayTCLog.a();
                                GfanPayTMessage gfanPayTMessage = new GfanPayTMessage();
                                gfanPayTMessage.mMsgType = 1;
                                gfanPayTMessage.mInitProfile = r.k(GfanPayAgent.getContext());
                                gfanPayEventPackage.mTMessages.add(gfanPayTMessage);
                                packSizeNoSub += gfanPayTMessage.mInitProfile.getPackSize() + GfanPayPacker.getPackSize(gfanPayTMessage.mMsgType);
                                z = true;
                            } else {
                                z = false;
                            }
                            j.a(GfanPayAgent.getContext());
                            gfanPayEventPackage.mMaxErrorId = j.g("error_report");
                            List b = j.b();
                            ArrayList arrayList = new ArrayList();
                            Iterator it = b.iterator();
                            int i = packSizeNoSub;
                            int i2 = 0;
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                GfanPaySession gfanPaySession = (GfanPaySession) it.next();
                                i2++;
                                String str = gfanPaySession.id;
                                long j = gfanPayEventPackage.mMaxActivityId;
                                gfanPaySession.activities = j.d(str);
                                String str2 = gfanPaySession.id;
                                long j2 = gfanPayEventPackage.mMaxAppEventId;
                                gfanPaySession.appEvents = j.f(str2);
                                GfanPayTMessage gfanPayTMessage2 = new GfanPayTMessage();
                                gfanPayTMessage2.mMsgType = 2;
                                gfanPayTMessage2.session = gfanPaySession;
                                int packSize = gfanPaySession.getPackSize();
                                if (packSize + i > g && i2 != 1) {
                                    f = true;
                                    break;
                                }
                                i += packSize;
                                arrayList.add(gfanPaySession);
                                if (gfanPaySession.mStatus != 2 || gfanPaySession.activities.size() != 0 || gfanPaySession.appEvents.size() != 0) {
                                    gfanPayEventPackage.mTMessages.add(gfanPayTMessage2);
                                }
                            }
                            new String[1][0] = "************ " + arrayList.size() + " Session*****************";
                            GfanPayTCLog.a();
                            gfanPayEventPackage.mMaxActivityId = j.a(arrayList);
                            gfanPayEventPackage.mMaxAppEventId = j.b(arrayList);
                            if (gfanPayEventPackage.mMaxErrorId > 0) {
                                Iterator it2 = j.d(gfanPayEventPackage.mMaxErrorId).iterator();
                                while (it2.hasNext()) {
                                    gfanPayEventPackage.mTMessages.add((GfanPayTMessage) it2.next());
                                }
                            }
                            j.a();
                            if (!z && gfanPayEventPackage.mTMessages.size() == 0) {
                                gfanPayEventPackage = null;
                            }
                            String[] strArr3 = {"Test", "[HTTPThread]2 send ep: " + gfanPayEventPackage};
                            GfanPayTCLog.a();
                            if (gfanPayEventPackage == null) {
                                a2 = true;
                            } else if (e > 1) {
                                f = false;
                                boolean hasMessages = b().hasMessages(5);
                                String[] strArr4 = new String[2];
                                strArr4[0] = "Test";
                                strArr4[1] = "[HTTPThread]MessageQueue" + (hasMessages ? " has " : "no") + "message in queue";
                                GfanPayTCLog.a();
                                b().removeMessages(5);
                                GfanPayAgent.delaySendSession(300000L);
                                e = 0;
                                a2 = false;
                            } else {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                new GfanPayPacker(byteArrayOutputStream).pack(gfanPayEventPackage);
                                new String[1][0] = "send package byte size : " + byteArrayOutputStream.toByteArray().length;
                                GfanPayTCLog.a();
                                a2 = i.a("/g/d", "POST", byteArrayOutputStream.toByteArray());
                                if (a2) {
                                    e = 0;
                                    a(gfanPayEventPackage);
                                    GfanPayAgent.a(GfanPayAgent.getContext(), System.currentTimeMillis());
                                } else {
                                    e++;
                                    f = true;
                                }
                            }
                        }
                        if (f) {
                            String[] strArr5 = {"Test", "[HTTPThread]after send:" + a2};
                            GfanPayTCLog.a();
                            GfanPayAgent.delaySendSession(0L);
                            f = false;
                        }
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        } catch (Throwable th) {
        }
    }
}
