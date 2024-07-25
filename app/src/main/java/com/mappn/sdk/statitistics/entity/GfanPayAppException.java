package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import com.mokredit.payment.StringUtils;

/* loaded from: classes.dex */
public class GfanPayAppException implements GfanPayMessagePackable {
    public long mErrorTime = 0;
    public int mRepeat = 1;
    public String mAppVersionCode = StringUtils.EMPTY;
    public byte[] data = new byte[0];
    public String mShortHashCode = StringUtils.EMPTY;

    public int getPackSize() {
        return GfanPayPacker.getPackSize(5) + GfanPayPacker.getPackSize(this.mErrorTime) + GfanPayPacker.getPackSize(this.mRepeat) + GfanPayPacker.getPackSize(this.mAppVersionCode) + GfanPayPacker.getPackSize(this.data) + GfanPayPacker.getPackSize(this.mShortHashCode);
    }

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(5);
        gfanPayPacker.pack(this.mErrorTime);
        gfanPayPacker.pack(this.mRepeat);
        gfanPayPacker.pack(this.mAppVersionCode);
        gfanPayPacker.pack(this.data);
        gfanPayPacker.pack(this.mShortHashCode);
    }

    public final String toString() {
        try {
            return "AppException=@" + getClass().getName() + "\r\n            mErrorTime--<" + this.mErrorTime + ">\r\n            mRepeat--<" + this.mRepeat + ">\r\n            mAppVersionCode--<" + this.mAppVersionCode + ">\r\n            data--<" + new String(this.data, "utf-8") + ">\r\n            mShortHashCode--<" + this.mShortHashCode + GfanPayPrintag.ENDTAG;
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }
}
