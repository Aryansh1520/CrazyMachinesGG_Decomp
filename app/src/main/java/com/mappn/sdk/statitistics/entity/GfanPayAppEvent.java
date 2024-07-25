package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import com.mokredit.payment.StringUtils;
import java.util.Map;

/* loaded from: classes.dex */
public class GfanPayAppEvent implements GfanPayMessagePackable {
    public Map parameters;
    public long startTime;
    public String id = StringUtils.EMPTY;
    public String label = StringUtils.EMPTY;
    public int count = 0;

    public int getPackSize() {
        return GfanPayPacker.getPackSize(3) + GfanPayPacker.getPackSize(this.id) + GfanPayPacker.getPackSize(this.label) + GfanPayPacker.getPackSize(this.count);
    }

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(5);
        gfanPayPacker.pack(this.id);
        gfanPayPacker.pack(this.label);
        gfanPayPacker.pack(this.count);
        gfanPayPacker.pack(this.startTime);
        gfanPayPacker.pack(this.parameters);
    }

    public final String toString() {
        return "AppEvent=@" + getClass().getName() + "\r\n                id" + GfanPayPrintag.STARTAG + this.id + ">\r\n                label" + GfanPayPrintag.STARTAG + this.label + ">\r\n                count" + GfanPayPrintag.STARTAG + this.count + ">\r\n                startTime" + GfanPayPrintag.STARTAG + this.startTime + ">\r\n                parameters" + GfanPayPrintag.STARTAG + this.parameters + GfanPayPrintag.ENDTAG;
    }
}
