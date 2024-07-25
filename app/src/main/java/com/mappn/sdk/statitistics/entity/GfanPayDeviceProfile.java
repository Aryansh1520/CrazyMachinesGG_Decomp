package com.mappn.sdk.statitistics.entity;

import com.mappn.sdk.statitistics.msgpack.GfanPayPacker;
import com.mokredit.payment.StringUtils;

/* loaded from: classes.dex */
public class GfanPayDeviceProfile implements GfanPayMessagePackable {
    public String mMobileModel = StringUtils.EMPTY;
    public String mOsSdkVersion = StringUtils.EMPTY;
    public GfanPayGis mGis = new GfanPayGis();
    public String mCpuABI = StringUtils.EMPTY;
    public String mPixelMetric = StringUtils.EMPTY;
    public String mCountry = StringUtils.EMPTY;
    public String mCarrier = StringUtils.EMPTY;
    public String mLanguage = StringUtils.EMPTY;
    public int mTimezone = 8;
    public String mOsVersion = StringUtils.EMPTY;
    public int mChannel = -1;
    public String m2G_3G = StringUtils.EMPTY;
    public boolean isJailBroken = false;
    public String mSimOperator = StringUtils.EMPTY;
    public String mNetworkOperator = StringUtils.EMPTY;
    public String hostName = StringUtils.EMPTY;
    public String deviceName = StringUtils.EMPTY;
    public long kernBootTime = 0;

    public int getPackSize() {
        return GfanPayPacker.getPackSize(18) + GfanPayPacker.getPackSize(this.mMobileModel) + GfanPayPacker.getPackSize(this.mOsSdkVersion) + this.mGis.getPackSize() + GfanPayPacker.getPackSize(this.mCpuABI) + GfanPayPacker.getPackSize(this.mPixelMetric) + GfanPayPacker.getPackSize(this.mCountry) + GfanPayPacker.getPackSize(this.mCarrier) + GfanPayPacker.getPackSize(this.mLanguage) + GfanPayPacker.getPackSize(this.mTimezone) + GfanPayPacker.getPackSize(this.mOsVersion) + GfanPayPacker.getPackSize(this.mChannel) + GfanPayPacker.getPackSize(this.m2G_3G) + GfanPayPacker.getPackSize(this.isJailBroken) + GfanPayPacker.getPackSize(this.mSimOperator) + GfanPayPacker.getPackSize(this.mNetworkOperator) + GfanPayPacker.getPackSize(this.hostName) + GfanPayPacker.getPackSize(this.deviceName) + GfanPayPacker.getPackSize(this.kernBootTime);
    }

    @Override // com.mappn.sdk.statitistics.entity.GfanPayMessagePackable
    public void messagePack(GfanPayPacker gfanPayPacker) {
        gfanPayPacker.packArray(18);
        gfanPayPacker.pack(this.mMobileModel);
        gfanPayPacker.pack(this.mOsSdkVersion);
        gfanPayPacker.pack(this.mGis);
        gfanPayPacker.pack(this.mCpuABI);
        gfanPayPacker.pack(this.mPixelMetric);
        gfanPayPacker.pack(this.mCountry);
        gfanPayPacker.pack(this.mCarrier);
        gfanPayPacker.pack(this.mLanguage);
        gfanPayPacker.pack(this.mTimezone);
        gfanPayPacker.pack(this.mOsVersion);
        gfanPayPacker.pack(this.mChannel);
        gfanPayPacker.pack(this.m2G_3G);
        gfanPayPacker.pack(this.isJailBroken);
        gfanPayPacker.pack(this.mSimOperator);
        gfanPayPacker.pack(this.mNetworkOperator);
        gfanPayPacker.pack(this.hostName);
        gfanPayPacker.pack(this.deviceName);
        gfanPayPacker.pack(this.kernBootTime);
    }

    public final String toString() {
        return "DeviceProfile=@" + getClass().getName() + "\r\n        mMobileModel" + GfanPayPrintag.STARTAG + this.mMobileModel + ">\r\n        mOsSdkVersion" + GfanPayPrintag.STARTAG + this.mOsSdkVersion + ">\r\n    " + GfanPayPrintag.SPACE + this.mGis + "        mCpuABI--<" + this.mCpuABI + ">\r\n        mPixelMetric" + GfanPayPrintag.STARTAG + this.mPixelMetric + ">\r\n        mCountry" + GfanPayPrintag.STARTAG + this.mCountry + ">\r\n        mCarrier" + GfanPayPrintag.STARTAG + this.mCarrier + ">\r\n        mLanguage" + GfanPayPrintag.STARTAG + this.mLanguage + ">\r\n        mTimezone" + GfanPayPrintag.STARTAG + this.mTimezone + ">\r\n        mOsVersion" + GfanPayPrintag.STARTAG + this.mOsVersion + ">\r\n        mChannel" + GfanPayPrintag.STARTAG + this.mChannel + ">\r\n        m2G_3G" + GfanPayPrintag.STARTAG + this.m2G_3G + ">\r\n        isJailBroken" + GfanPayPrintag.STARTAG + this.isJailBroken + ">\r\n        mSimOperator" + GfanPayPrintag.STARTAG + this.mSimOperator + ">\r\n        mNetworkOperator" + GfanPayPrintag.STARTAG + this.mNetworkOperator + ">\r\n    hostName--<" + this.hostName + ">\r\n        deviceName" + GfanPayPrintag.STARTAG + this.deviceName + ">\r\n        kernBootTime" + GfanPayPrintag.STARTAG + this.kernBootTime + ">\r\n    ";
    }
}
