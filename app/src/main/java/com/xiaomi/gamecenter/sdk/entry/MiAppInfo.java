package com.xiaomi.gamecenter.sdk.entry;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class MiAppInfo implements Parcelable {
    public static final Parcelable.Creator CREATOR = new f();
    private MiAccountInfo account;
    private int appId;
    private String appKey;
    private MiGameType appType;
    private Context ctx;
    private String cpMark = "XXX";
    private PayMode payMode = PayMode.custom;
    private ScreenOrientation orientation = ScreenOrientation.vertical;
    private boolean weakAccount = false;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MiAccountInfo getAccount() {
        return this.account;
    }

    public int getAppId() {
        return this.appId;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public MiGameType getAppType() {
        return this.appType;
    }

    public String getCpMark() {
        return this.cpMark;
    }

    public Context getCtx() {
        return this.ctx;
    }

    public ScreenOrientation getOrientation() {
        return this.orientation;
    }

    public PayMode getPayMode() {
        return this.payMode;
    }

    public boolean isWeakAccount() {
        return this.weakAccount;
    }

    public void setAccount(MiAccountInfo miAccountInfo) {
        this.account = miAccountInfo;
    }

    public void setAppId(int i) {
        this.appId = i;
    }

    public void setAppKey(String str) {
        this.appKey = str;
    }

    public void setAppType(MiGameType miGameType) {
        this.appType = miGameType;
    }

    public void setCpMark(String str) {
        this.cpMark = str;
    }

    public void setCtx(Context context) {
        this.ctx = context;
    }

    public void setOrientation(ScreenOrientation screenOrientation) {
        this.orientation = screenOrientation;
    }

    public void setPayMode(PayMode payMode) {
        this.payMode = payMode;
    }

    public void setWeakAccount(boolean z) {
        this.weakAccount = z;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.appId);
        parcel.writeString(this.appKey);
        parcel.writeString(this.appType.toString());
        parcel.writeString(this.cpMark);
        parcel.writeString(this.orientation.toString());
        parcel.writeString(Boolean.toString(this.weakAccount));
        parcel.writeString(this.payMode.toString());
        parcel.writeParcelable(this.account, 0);
    }
}
