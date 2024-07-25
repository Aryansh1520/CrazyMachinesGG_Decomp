package com.xiaomi.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class MiBuyInfoOnline implements Parcelable {
    public static final Parcelable.Creator CREATOR = new a();
    private String cpOrderId;
    private String cpUserInfo;
    private int mili;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getCpOrderId() {
        return this.cpOrderId;
    }

    public String getCpUserInfo() {
        return this.cpUserInfo;
    }

    public int getMili() {
        return this.mili;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(this.cpOrderId) && this.mili > 0 && this.mili <= 9999;
    }

    public void setCpOrderId(String str) {
        this.cpOrderId = str;
    }

    public void setCpUserInfo(String str) {
        this.cpUserInfo = str;
    }

    public void setMiBi(int i) {
        this.mili = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getCpOrderId());
        parcel.writeInt(getMili());
        parcel.writeString(getCpUserInfo());
    }
}
