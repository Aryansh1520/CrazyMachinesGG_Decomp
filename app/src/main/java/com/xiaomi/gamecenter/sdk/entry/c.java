package com.xiaomi.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class c implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public LoginResult createFromParcel(Parcel parcel) {
        LoginResult loginResult = new LoginResult();
        loginResult.errcode = parcel.readInt();
        loginResult.account = (MiAccountInfo) parcel.readParcelable(LoginResult.class.getClassLoader());
        return loginResult;
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public LoginResult[] newArray(int i) {
        return new LoginResult[i];
    }
}
