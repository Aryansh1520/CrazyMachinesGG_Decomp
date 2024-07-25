package com.xiaomi.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class MiBuyInfoOffline implements Parcelable {
    public static final Parcelable.Creator CREATOR = new h();
    private int count;
    private String cpOrderId;
    private String productCode;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getCount() {
        return this.count;
    }

    public String getCpOrderId() {
        return this.cpOrderId;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(this.productCode) && !TextUtils.isEmpty(this.cpOrderId) && this.count > 0 && this.count <= 9999;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public void setCpOrderId(String str) {
        this.cpOrderId = str;
    }

    public void setProductCode(String str) {
        this.productCode = str;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getCpOrderId());
        parcel.writeString(getProductCode());
        parcel.writeInt(getCount());
    }
}
