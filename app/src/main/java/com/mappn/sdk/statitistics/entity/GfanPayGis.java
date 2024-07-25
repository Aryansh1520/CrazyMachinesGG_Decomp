package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;

/* loaded from: classes.dex */
public class GfanPayGis implements GfanPayMessagePackable {
    public double lng = 0.0d;
    public double lat = 0.0d;

    public int getPackSize() {
        return GfanPayPacker.getPackSize(2) + GfanPayPacker.getPackSize(this.lng) + GfanPayPacker.getPackSize(this.lat);
    }

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(2);
        gfanPayPacker.pack(this.lng);
        gfanPayPacker.pack(this.lat);
    }

    public final String toString() {
        return "Gis=@" + getClass().getName() + "\r\n            lng--<" + this.lng + ">\r\n            lat--<" + this.lat + GfanPayPrintag.ENDTAG;
    }
}
