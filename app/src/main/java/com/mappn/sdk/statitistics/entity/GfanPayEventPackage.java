package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import com.mokredit.payment.StringUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
public class GfanPayEventPackage implements GfanPayMessagePackable {
    public String mDeviceId = StringUtils.EMPTY;
    public String mDeveploperAppkey = StringUtils.EMPTY;
    public GfanPayAppProfile mAppProfile = new GfanPayAppProfile();
    public GfanPayDeviceProfile mDeviceProfile = new GfanPayDeviceProfile();
    public List mTMessages = new ArrayList();
    public long mMaxActivityId = 0;
    public long mMaxAppEventId = 0;
    public long mMaxErrorId = 0;

    public int getPackSizeNoSub() {
        return GfanPayPacker.getPackSize(5) + GfanPayPacker.getPackSize(this.mDeviceId) + GfanPayPacker.getPackSize(this.mDeveploperAppkey) + this.mAppProfile.getPackSize() + this.mDeviceProfile.getPackSize();
    }

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(5);
        gfanPayPacker.pack(this.mDeviceId);
        gfanPayPacker.pack(this.mDeveploperAppkey);
        gfanPayPacker.pack(this.mAppProfile);
        gfanPayPacker.pack(this.mDeviceProfile);
        gfanPayPacker.packArray(this.mTMessages.size());
        Iterator it = this.mTMessages.iterator();
        while (it.hasNext()) {
            gfanPayPacker.pack((GfanPayTMessage) it.next());
        }
    }

    public final String toString() {
        return "EventPackage=@" + getClass().getName() + "\r\n    mDeviceId--<" + this.mDeviceId + ">\r\n    mDeveploperAppkey--<" + this.mDeveploperAppkey + ">\r\n    " + this.mAppProfile + GfanPayPrintag.SPACE + this.mDeviceProfile + "    List<TMessage>" + GfanPayPrintag.LISTSTARTAG + this.mTMessages.size() + "]\r\n    " + this.mTMessages;
    }
}
