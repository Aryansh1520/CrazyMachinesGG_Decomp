package com.xiaomi.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class h implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public MiBuyInfoOffline createFromParcel(Parcel parcel) {
        MiBuyInfoOffline miBuyInfoOffline = new MiBuyInfoOffline();
        miBuyInfoOffline.setCpOrderId(parcel.readString());
        miBuyInfoOffline.setProductCode(parcel.readString());
        miBuyInfoOffline.setCount(parcel.readInt());
        return miBuyInfoOffline;
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public MiBuyInfoOffline[] newArray(int i) {
        return new MiBuyInfoOffline[i];
    }
}
