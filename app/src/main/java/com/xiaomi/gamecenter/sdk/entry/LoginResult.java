package com.xiaomi.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class LoginResult implements Parcelable {
    public static final Parcelable.Creator CREATOR = new c();
    private MiAccountInfo account;
    private int errcode;

    public LoginResult() {
    }

    public LoginResult(int i, MiAccountInfo miAccountInfo) {
        this.errcode = i;
        this.account = miAccountInfo;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public MiAccountInfo getAccount() {
        return this.account;
    }

    public int getErrcode() {
        return this.errcode;
    }

    public void setAccount(MiAccountInfo miAccountInfo) {
        this.account = miAccountInfo;
    }

    public void setErrcode(int i) {
        this.errcode = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.errcode);
        parcel.writeParcelable(this.account, 0);
    }
}
