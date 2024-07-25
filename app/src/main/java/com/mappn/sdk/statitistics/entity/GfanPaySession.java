package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import com.mokredit.payment.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class GfanPaySession implements GfanPayMessagePackable {
    public static final int CONTINUE = 2;
    public static final int LAUNCH = 1;
    public static final int TERMINATE = 3;
    public String id = StringUtils.EMPTY;
    public long start = 0;
    public int mStatus = 0;
    public int duration = 0;
    public List activities = new ArrayList();
    public List appEvents = new ArrayList();
    public int mLastSessionInterval = 0;
    public int isConnected = 0;

    public int getPackSize() {
        int i;
        int packSize = GfanPayPacker.getPackSize(7) + GfanPayPacker.getPackSize(this.id) + GfanPayPacker.getPackSize(this.start) + GfanPayPacker.getPackSize(this.mStatus) + GfanPayPacker.getPackSize(this.duration) + GfanPayPacker.getPackSize(this.isConnected) + GfanPayPacker.getPackSize(this.activities.size());
        Iterator it = this.activities.iterator();
        while (true) {
            i = packSize;
            if (!it.hasNext()) {
                break;
            }
            packSize = ((GfanPayActivity) it.next()).getPackSize() + i;
        }
        int packSize2 = GfanPayPacker.getPackSize(this.appEvents.size()) + i;
        Iterator it2 = this.appEvents.iterator();
        while (true) {
            int i2 = packSize2;
            if (!it2.hasNext()) {
                return i2;
            }
            packSize2 = ((GfanPayAppEvent) it2.next()).getPackSize() + i2;
        }
    }

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(7);
        gfanPayPacker.pack(this.id);
        gfanPayPacker.pack(this.start);
        gfanPayPacker.pack(this.mStatus);
        gfanPayPacker.pack(this.duration);
        gfanPayPacker.packArray(this.activities.size());
        Iterator it = this.activities.iterator();
        while (it.hasNext()) {
            gfanPayPacker.pack((GfanPayActivity) it.next());
        }
        gfanPayPacker.packArray(this.appEvents.size());
        Iterator it2 = this.appEvents.iterator();
        while (it2.hasNext()) {
            gfanPayPacker.pack((GfanPayAppEvent) it2.next());
        }
        gfanPayPacker.pack(this.isConnected);
    }

    public final String toString() {
        return "Session=@" + getClass().getName() + "\r\n            id--<" + this.id + ">\r\n            start--<" + this.start + ">\r\n            mStatus--<" + this.mStatus + ">\r\n            duration--<" + this.duration + ">\r\n            List<Activity>--[" + this.activities.size() + "]\r\n            " + this.activities + "\r\n            List<AppEvent>--[" + this.appEvents.size() + "]\r\n            " + this.appEvents + "\r\n            isConnected--<" + this.isConnected + GfanPayPrintag.ENDTAG;
    }
}
