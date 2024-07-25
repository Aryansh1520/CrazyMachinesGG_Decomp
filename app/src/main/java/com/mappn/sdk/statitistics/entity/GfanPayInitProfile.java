package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import com.mokredit.payment.StringUtils;

/* loaded from: classes.dex */
public class GfanPayInitProfile implements GfanPayMessagePackable {
    public String mCpuDiscription = StringUtils.EMPTY;
    public int mCpuCoreNum = 0;
    public float mCpuFrequency = 0.0f;
    public String mCpuImplementor = StringUtils.EMPTY;
    public String mGpuVendor = StringUtils.EMPTY;
    public String mGpuRenderer = StringUtils.EMPTY;
    public int mMemoryTotal = 0;
    public int mMemoryFree = 0;
    public int mMobileStorageTotal = 0;
    public int mMobileStorageFree = 0;
    public int mSDCardStorageTotal = 0;
    public int mSDCardStorageFree = 0;
    public int mBatteryCapacity = 0;
    public float mDisplaMetricWidth = 0.0f;
    public float mDisplaMetricHeight = 0.0f;
    public int mDisplayMetricDensity = 0;
    public String mRomInfo = StringUtils.EMPTY;
    public String mBaseBand = StringUtils.EMPTY;
    public String mIMEI = StringUtils.EMPTY;
    public String mMACAddress = StringUtils.EMPTY;
    public String mApnName = StringUtils.EMPTY;
    public String mApn_mcc = StringUtils.EMPTY;
    public String mApn_mnc = StringUtils.EMPTY;
    public boolean mApn_proxy = false;
    public String mIMSI = StringUtils.EMPTY;
    public String mUpid = StringUtils.EMPTY;
    public String mSimId = StringUtils.EMPTY;

    public int getPackSize() {
        return GfanPayPacker.getPackSize(27) + GfanPayPacker.getPackSize(this.mCpuDiscription) + GfanPayPacker.getPackSize(this.mCpuCoreNum) + GfanPayPacker.getPackSize(this.mCpuFrequency) + GfanPayPacker.getPackSize(this.mCpuImplementor) + GfanPayPacker.getPackSize(this.mGpuVendor) + GfanPayPacker.getPackSize(this.mGpuRenderer) + GfanPayPacker.getPackSize(this.mMemoryTotal) + GfanPayPacker.getPackSize(this.mMemoryFree) + GfanPayPacker.getPackSize(this.mMobileStorageTotal) + GfanPayPacker.getPackSize(this.mMobileStorageFree) + GfanPayPacker.getPackSize(this.mSDCardStorageTotal) + GfanPayPacker.getPackSize(this.mSDCardStorageFree) + GfanPayPacker.getPackSize(this.mBatteryCapacity) + GfanPayPacker.getPackSize(this.mDisplaMetricWidth) + GfanPayPacker.getPackSize(this.mDisplaMetricHeight) + GfanPayPacker.getPackSize(this.mDisplayMetricDensity) + GfanPayPacker.getPackSize(this.mRomInfo) + GfanPayPacker.getPackSize(this.mBaseBand) + GfanPayPacker.getPackSize(this.mIMEI) + GfanPayPacker.getPackSize(this.mMACAddress) + GfanPayPacker.getPackSize(this.mApnName) + GfanPayPacker.getPackSize(this.mApn_mcc) + GfanPayPacker.getPackSize(this.mApn_mnc) + GfanPayPacker.getPackSize(this.mApn_proxy) + GfanPayPacker.getPackSize(this.mIMSI) + GfanPayPacker.getPackSize(this.mUpid) + GfanPayPacker.getPackSize(this.mSimId);
    }

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(27);
        gfanPayPacker.pack(this.mCpuDiscription);
        gfanPayPacker.pack(this.mCpuCoreNum);
        gfanPayPacker.pack(this.mCpuFrequency);
        gfanPayPacker.pack(this.mCpuImplementor);
        gfanPayPacker.pack(this.mGpuVendor);
        gfanPayPacker.pack(this.mGpuRenderer);
        gfanPayPacker.pack(this.mMemoryTotal);
        gfanPayPacker.pack(this.mMemoryFree);
        gfanPayPacker.pack(this.mMobileStorageTotal);
        gfanPayPacker.pack(this.mMobileStorageFree);
        gfanPayPacker.pack(this.mSDCardStorageTotal);
        gfanPayPacker.pack(this.mSDCardStorageFree);
        gfanPayPacker.pack(this.mBatteryCapacity);
        gfanPayPacker.pack(this.mDisplaMetricWidth);
        gfanPayPacker.pack(this.mDisplaMetricHeight);
        gfanPayPacker.pack(this.mDisplayMetricDensity);
        gfanPayPacker.pack(this.mRomInfo);
        gfanPayPacker.pack(this.mBaseBand);
        gfanPayPacker.pack(this.mIMEI);
        gfanPayPacker.pack(this.mMACAddress);
        gfanPayPacker.pack(this.mApnName);
        gfanPayPacker.pack(this.mApn_mcc);
        gfanPayPacker.pack(this.mApn_mnc);
        gfanPayPacker.pack(this.mApn_proxy);
        gfanPayPacker.pack(this.mIMSI);
        gfanPayPacker.pack(this.mUpid);
        gfanPayPacker.pack(this.mSimId);
    }

    public final String toString() {
        return "InitProfile=@" + getClass().getName() + "\r\n            mCpuDiscription--<" + this.mCpuDiscription + ">\r\n            mCpuCoreNum--<" + this.mCpuCoreNum + ">\r\n            mCpuFrequency--<" + this.mCpuFrequency + ">\r\n            mCpuImplementor--<" + this.mCpuImplementor + ">\r\n            mGpuVendor--<" + this.mGpuVendor + ">\r\n            mGpuRenderer--<" + this.mGpuRenderer + ">\r\n            mMemoryTotal--<" + this.mMemoryTotal + ">\r\n            mMemoryFree--<" + this.mMemoryFree + ">\r\n            mMobileStorageTotal--<" + this.mMobileStorageTotal + ">\r\n            mMobileStorageFree--<" + this.mMobileStorageFree + ">\r\n            mSDCardStorageTotal--<" + this.mSDCardStorageTotal + ">\r\n            mSDCardStorageFree--<" + this.mSDCardStorageFree + ">\r\n            mBatteryCapacity--<" + this.mBatteryCapacity + ">\r\n            mDisplaMetricWidth--<" + this.mDisplaMetricWidth + ">\r\n            mDisplaMetricHeight--<" + this.mDisplaMetricHeight + ">\r\n            mDisplayMetricDensity--<" + this.mDisplayMetricDensity + ">\r\n            mRomInfo--<" + this.mRomInfo + ">\r\n            mBaseBand--<" + this.mBaseBand + ">\r\n            mIMEI--<" + this.mIMEI + ">\r\n            mMACAddress--<" + this.mMACAddress + ">\r\n            mApnName--<" + this.mApnName + ">\r\n            mApn_mcc--<" + this.mApn_mcc + ">\r\n            mApn_mnc--<" + this.mApn_mnc + ">\r\n            mApn_proxy--<" + String.valueOf(this.mApn_proxy) + ">\r\n            mIMSI--<" + this.mIMSI + ">\r\n            upid--<" + this.mUpid + ">\r\n            mSimId--<" + this.mSimId + GfanPayPrintag.ENDTAG;
    }
}
