package com.xiaomi.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class MiAccountInfo implements Parcelable {
    public static final Parcelable.Creator CREATOR = new g();
    private String nikename;
    private String sessionId;
    private long uid;

    public MiAccountInfo() {
    }

    public MiAccountInfo(long j, String str, String str2) {
        this.uid = j;
        this.sessionId = str;
        this.nikename = str2;
    }

    public static Parcelable.Creator getCreator() {
        return CREATOR;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getNikename() {
        return this.nikename;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public long getUid() {
        return this.uid;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.uid);
        parcel.writeString(this.sessionId);
        parcel.writeString(this.nikename);
    }
}
