package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import com.mokredit.payment.StringUtils;

/* loaded from: classes.dex */
public class GfanPayActivity implements GfanPayMessagePackable {
    public String name = StringUtils.EMPTY;
    public long start = 0;
    public int duration = 0;
    public String refer = StringUtils.EMPTY;

    public int getPackSize() {
        return GfanPayPacker.getPackSize(4) + GfanPayPacker.getPackSize(this.name) + GfanPayPacker.getPackSize(this.start) + GfanPayPacker.getPackSize(this.duration) + GfanPayPacker.getPackSize(this.refer);
    }

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(4);
        gfanPayPacker.pack(this.name);
        gfanPayPacker.pack(this.start);
        gfanPayPacker.pack(this.duration);
        gfanPayPacker.pack(this.refer);
    }

    public final String toString() {
        return "Activity=@" + getClass().getName() + "\r\n                name" + GfanPayPrintag.STARTAG + this.name + ">\r\n                start" + GfanPayPrintag.STARTAG + this.start + ">\r\n                duration" + GfanPayPrintag.STARTAG + this.duration + ">\r\n                refer" + GfanPayPrintag.STARTAG + this.refer + GfanPayPrintag.ENDTAG;
    }
}
