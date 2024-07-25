package com.mappn.sdk.statitistics.util;

import com.mappn.sdk.statitistics.entity.GfanPayActivity;
import com.mappn.sdk.statitistics.entity.GfanPayAppEvent;
import com.mappn.sdk.statitistics.entity.GfanPayEventPackage;
import com.mappn.sdk.statitistics.entity.GfanPayPrintag;
import com.mappn.sdk.statitistics.entity.GfanPayTMessage;
import java.util.Iterator;

/* loaded from: classes.dex */
public class GfanPayDataInfoLog {
    private static String a(GfanPayTMessage gfanPayTMessage) {
        StringBuffer stringBuffer = new StringBuffer();
        switch (gfanPayTMessage.mMsgType) {
            case 1:
                stringBuffer.append("                  [Event]----<type:init>\r\n");
                stringBuffer.append("                             <mCpuDiscription:" + gfanPayTMessage.mInitProfile.mCpuDiscription + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mCpuCoreNum:" + gfanPayTMessage.mInitProfile.mCpuCoreNum + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mCpuFrequency:" + gfanPayTMessage.mInitProfile.mCpuFrequency + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mCpuImplementor:" + gfanPayTMessage.mInitProfile.mCpuImplementor + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mGpuVendor:" + gfanPayTMessage.mInitProfile.mGpuVendor + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mGpuRenderer:" + gfanPayTMessage.mInitProfile.mGpuRenderer + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mMemoryTotal:" + gfanPayTMessage.mInitProfile.mMemoryTotal + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mMemoryFree:" + gfanPayTMessage.mInitProfile.mMemoryFree + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mMobileStorageTotal:" + gfanPayTMessage.mInitProfile.mMobileStorageTotal + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mMobileStorageFree:" + gfanPayTMessage.mInitProfile.mMobileStorageFree + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mSDCardStorageTotal:" + gfanPayTMessage.mInitProfile.mSDCardStorageTotal + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mSDCardStorageFree:" + gfanPayTMessage.mInitProfile.mSDCardStorageFree + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mBatteryCapacity:" + gfanPayTMessage.mInitProfile.mBatteryCapacity + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mDisplaMetricWidth:" + gfanPayTMessage.mInitProfile.mDisplaMetricWidth + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mDisplaMetricHeight:" + gfanPayTMessage.mInitProfile.mDisplaMetricHeight + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mDisplayMetricDensity:" + gfanPayTMessage.mInitProfile.mDisplayMetricDensity + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mRomInfo:" + gfanPayTMessage.mInitProfile.mRomInfo + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mBaseBand:" + gfanPayTMessage.mInitProfile.mBaseBand + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mIMEI:" + gfanPayTMessage.mInitProfile.mIMEI + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mMACAddress:" + gfanPayTMessage.mInitProfile.mMACAddress + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mApnName:" + gfanPayTMessage.mInitProfile.mApnName + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mApn_mcc:" + gfanPayTMessage.mInitProfile.mApn_mcc + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mApn_mnc:" + gfanPayTMessage.mInitProfile.mApn_mnc + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mApn_proxy:" + gfanPayTMessage.mInitProfile.mApn_proxy + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mIMSI:" + gfanPayTMessage.mInitProfile.mIMSI + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mUpid:" + gfanPayTMessage.mInitProfile.mUpid + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mSimId:" + gfanPayTMessage.mInitProfile.mSimId + GfanPayPrintag.ENDTAG);
                break;
            case 2:
                switch (gfanPayTMessage.session.mStatus) {
                    case 1:
                        stringBuffer.append("                  [Session]----<type:launch>\r\n");
                        stringBuffer.append("                             <session id:" + gfanPayTMessage.session.id + GfanPayPrintag.ENDTAG);
                        stringBuffer.append("                             <start:" + gfanPayTMessage.session.start + GfanPayPrintag.ENDTAG);
                        stringBuffer.append("                             <mStatus:" + gfanPayTMessage.session.mStatus + GfanPayPrintag.ENDTAG);
                        stringBuffer.append("                             <duration:" + gfanPayTMessage.session.duration + GfanPayPrintag.ENDTAG);
                        break;
                    case 2:
                        stringBuffer.append("                  [Session]----<type:continue>\r\n");
                        stringBuffer.append("                             <session id:" + gfanPayTMessage.session.id + GfanPayPrintag.ENDTAG);
                        stringBuffer.append("                             <start:" + gfanPayTMessage.session.start + GfanPayPrintag.ENDTAG);
                        stringBuffer.append("                             <mStatus:" + gfanPayTMessage.session.mStatus + GfanPayPrintag.ENDTAG);
                        stringBuffer.append("                             <duration:" + gfanPayTMessage.session.duration + GfanPayPrintag.ENDTAG);
                        break;
                    case 3:
                        stringBuffer.append("                  [Session]----<type:terminate>\r\n");
                        stringBuffer.append("                             <session id:" + gfanPayTMessage.session.id + GfanPayPrintag.ENDTAG);
                        stringBuffer.append("                             <start:" + gfanPayTMessage.session.start + GfanPayPrintag.ENDTAG);
                        stringBuffer.append("                             <mStatus:" + gfanPayTMessage.session.mStatus + GfanPayPrintag.ENDTAG);
                        stringBuffer.append("                             <duration:" + gfanPayTMessage.session.duration + GfanPayPrintag.ENDTAG);
                        break;
                }
                for (GfanPayActivity gfanPayActivity : gfanPayTMessage.session.activities) {
                    StringBuffer stringBuffer2 = new StringBuffer();
                    stringBuffer2.append("                             [Activity]----<name:" + gfanPayActivity.name + GfanPayPrintag.ENDTAG);
                    stringBuffer2.append("                                           <start:" + gfanPayActivity.start + GfanPayPrintag.ENDTAG);
                    stringBuffer2.append("                                           <duration:" + gfanPayActivity.duration + GfanPayPrintag.ENDTAG);
                    stringBuffer2.append("                                           <refer:" + gfanPayActivity.refer + GfanPayPrintag.ENDTAG);
                    stringBuffer.append(stringBuffer2.toString());
                }
                for (GfanPayAppEvent gfanPayAppEvent : gfanPayTMessage.session.appEvents) {
                    StringBuffer stringBuffer3 = new StringBuffer();
                    stringBuffer3.append("                             [AppEvent]----<event id:" + gfanPayAppEvent.id + GfanPayPrintag.ENDTAG);
                    stringBuffer3.append("                                           <label:" + gfanPayAppEvent.label + GfanPayPrintag.ENDTAG);
                    stringBuffer3.append("                                           <count:" + gfanPayAppEvent.count + GfanPayPrintag.ENDTAG);
                    stringBuffer.append(stringBuffer3.toString());
                }
                break;
            case 3:
                stringBuffer.append("                  [Event]----<type:app_exception>\r\n");
                stringBuffer.append("                             <mErrorTime:" + gfanPayTMessage.mAppException.mErrorTime + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mRepeat:" + gfanPayTMessage.mAppException.mRepeat + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <mAppVersionCode:" + gfanPayTMessage.mAppException.mAppVersionCode + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <data:" + gfanPayTMessage.mAppException.data.toString() + GfanPayPrintag.ENDTAG);
                stringBuffer.append("                             <shorthashcode:" + gfanPayTMessage.mAppException.mShortHashCode + GfanPayPrintag.ENDTAG);
                break;
            default:
                stringBuffer.append("                  [Event]----<type:other>\r\n");
                break;
        }
        return stringBuffer.toString();
    }

    public static String getCollectorServletLog(GfanPayEventPackage gfanPayEventPackage) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\r\n[EventPackage]----<devId:" + gfanPayEventPackage.mDeviceId + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                  <developerAppkey:" + gfanPayEventPackage.mDeveploperAppkey + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                  <mPartnerId:>" + gfanPayEventPackage.mAppProfile.mPartnerId + GfanPayPrintag.ENDTAG);
        stringBuffer.append("\r\n[AppProfile]-----------------------\r\n");
        stringBuffer.append("                             <mStartTime:" + gfanPayEventPackage.mAppProfile.mStartTime + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mAppPackageName:" + gfanPayEventPackage.mAppProfile.mAppPackageName + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mAppVersionName:" + gfanPayEventPackage.mAppProfile.mAppVersionName + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mAppVersionCode:" + gfanPayEventPackage.mAppProfile.mAppVersionCode + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mSdkVersion:" + gfanPayEventPackage.mAppProfile.mSdkVersion + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mPartnerId:" + gfanPayEventPackage.mAppProfile.mPartnerId + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <isCracked:" + gfanPayEventPackage.mAppProfile.isCracked + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mPartnerId:" + gfanPayEventPackage.mAppProfile.mPartnerId + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <installationTime:" + gfanPayEventPackage.mAppProfile.installationTime + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <purchaseTime:" + gfanPayEventPackage.mAppProfile.purchaseTime + GfanPayPrintag.ENDTAG);
        stringBuffer.append("\r\n[DeviceProfile]-----------------------\r\n");
        stringBuffer.append("                             <mobile:" + gfanPayEventPackage.mDeviceProfile.mMobileModel + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <os:" + gfanPayEventPackage.mDeviceProfile.mOsSdkVersion + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <gis:" + gfanPayEventPackage.mDeviceProfile.mGis + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <cpu:" + gfanPayEventPackage.mDeviceProfile.mCpuABI + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <pixel:" + gfanPayEventPackage.mDeviceProfile.mPixelMetric + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <country:" + gfanPayEventPackage.mDeviceProfile.mCountry + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mCarrier:" + gfanPayEventPackage.mDeviceProfile.mCarrier + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <language:" + gfanPayEventPackage.mDeviceProfile.mLanguage + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <timezone:" + gfanPayEventPackage.mDeviceProfile.mTimezone + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <osVersion:" + gfanPayEventPackage.mDeviceProfile.mOsVersion + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mChannel:" + gfanPayEventPackage.mDeviceProfile.mChannel + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <m2G_3G:" + gfanPayEventPackage.mDeviceProfile.m2G_3G + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mSimOperator:" + gfanPayEventPackage.mDeviceProfile.mSimOperator + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <mNetworkOperator:" + gfanPayEventPackage.mDeviceProfile.mNetworkOperator + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <hostName:" + gfanPayEventPackage.mDeviceProfile.hostName + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <deviceName:" + gfanPayEventPackage.mDeviceProfile.deviceName + GfanPayPrintag.ENDTAG);
        stringBuffer.append("                             <kernBootTime:" + gfanPayEventPackage.mDeviceProfile.kernBootTime + GfanPayPrintag.ENDTAG);
        Iterator it = gfanPayEventPackage.mTMessages.iterator();
        while (it.hasNext()) {
            stringBuffer.append(a((GfanPayTMessage) it.next()));
        }
        return stringBuffer.toString();
    }
}
