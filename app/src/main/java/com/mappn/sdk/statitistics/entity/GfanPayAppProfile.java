package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import com.mokredit.payment.StringUtils;

/* loaded from: classes.dex */
public class GfanPayAppProfile implements GfanPayMessagePackable {
    public String mAppPackageName = StringUtils.EMPTY;
    public String mAppVersionName = StringUtils.EMPTY;
    public String mAppVersionCode = StringUtils.EMPTY;
    public long mStartTime = 0;
    public String mSdkVersion = StringUtils.EMPTY;
    public String mPartnerId = StringUtils.EMPTY;
    public boolean isCracked = false;
    public long installationTime = 0;
    public long purchaseTime = 0;

    public int getPackSize() {
        return GfanPayPacker.getPackSize(9) + GfanPayPacker.getPackSize(this.mAppPackageName) + GfanPayPacker.getPackSize(this.mAppVersionName) + GfanPayPacker.getPackSize(this.mAppVersionCode) + GfanPayPacker.getPackSize(this.mStartTime) + GfanPayPacker.getPackSize(this.mSdkVersion) + GfanPayPacker.getPackSize(this.mPartnerId) + GfanPayPacker.getPackSize(this.isCracked) + GfanPayPacker.getPackSize(this.installationTime) + GfanPayPacker.getPackSize(this.purchaseTime);
    }

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(9);
        gfanPayPacker.pack(this.mAppPackageName);
        gfanPayPacker.pack(this.mAppVersionName);
        gfanPayPacker.pack(this.mAppVersionCode);
        gfanPayPacker.pack(this.mStartTime);
        gfanPayPacker.pack(this.mSdkVersion);
        gfanPayPacker.pack(this.mPartnerId);
        gfanPayPacker.pack(this.isCracked);
        gfanPayPacker.pack(this.installationTime);
        gfanPayPacker.pack(this.purchaseTime);
    }

    public final String toString() {
        return "AppProfile=@" + getClass().getName() + "\r\n        mAppPackageName" + GfanPayPrintag.STARTAG + this.mAppPackageName + ">\r\n        mAppVersionName" + GfanPayPrintag.STARTAG + this.mAppVersionName + ">\r\n        mAppVersionCode" + GfanPayPrintag.STARTAG + this.mAppVersionCode + ">\r\n        mStartTime" + GfanPayPrintag.STARTAG + this.mStartTime + ">\r\n        mSdkVersion" + GfanPayPrintag.STARTAG + this.mSdkVersion + ">\r\n        mPartnerId" + GfanPayPrintag.STARTAG + this.mPartnerId + ">\r\n        isCracked" + GfanPayPrintag.STARTAG + this.isCracked + ">\r\n        installationTime" + GfanPayPrintag.STARTAG + this.installationTime + ">\r\n        purchaseTime" + GfanPayPrintag.STARTAG + this.purchaseTime + GfanPayPrintag.ENDTAG;
    }
}
