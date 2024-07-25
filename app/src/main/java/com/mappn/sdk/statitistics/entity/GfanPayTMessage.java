package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import java.io.IOException;

/* loaded from: classes.dex */
public class GfanPayTMessage implements GfanPayMessagePackable {
    public GfanPayAppException mAppException;
    public GfanPayInitProfile mInitProfile;
    public int mMsgType = -1;
    public GfanPaySession session;

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(2);
        gfanPayPacker.pack(this.mMsgType);
        switch (this.mMsgType) {
            case 1:
                gfanPayPacker.pack(this.mInitProfile);
                return;
            case 2:
                gfanPayPacker.pack(this.session);
                return;
            case 3:
                gfanPayPacker.pack(this.mAppException);
                return;
            default:
                throw new IOException("unknown TMessageType");
        }
    }

    public final String toString() {
        StringBuilder append = new StringBuilder("TMessage=@").append(getClass().getName()).append("\r\n    ").append("    mMsgType").append(GfanPayPrintag.STARTAG).append(this.mMsgType).append(GfanPayPrintag.ENDTAG);
        switch (this.mMsgType) {
            case 1:
                append.append("        ").append(this.mInitProfile);
                break;
            case 2:
                append.append("        ").append(this.session);
                break;
            case 3:
                append.append("        ").append(this.mAppException);
                break;
        }
        return append.toString();
    }
}
