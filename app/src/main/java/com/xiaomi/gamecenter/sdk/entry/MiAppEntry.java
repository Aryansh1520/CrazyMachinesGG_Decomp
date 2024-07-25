package com.xiaomi.gamecenter.sdk.entry;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.xiaomi.gamecenter.sdk.IServiceCallback;

/* loaded from: classes.dex */
public class MiAppEntry implements Parcelable {
    public static final Parcelable.Creator CREATOR = new b();
    private MiAccountInfo account;
    private int appId;
    private String appKey;
    private MiGameType appType;
    private IServiceCallback callback;
    private String cpMark;
    private Context ctx;
    private ScreenOrientation orientation;
    private PayMode payMode;
    private int pid;
    private String pkgLabel;
    private String pkgName;
    private int uid;
    private boolean weakAccount;

    private MiAppEntry() {
        this.cpMark = "XXX";
        this.payMode = PayMode.custom;
        this.orientation = ScreenOrientation.vertical;
        this.weakAccount = false;
    }

    public MiAppEntry(MiAppInfo miAppInfo) {
        this.cpMark = "XXX";
        this.payMode = PayMode.custom;
        this.appId = miAppInfo.getAppId();
        this.appKey = miAppInfo.getAppKey();
        this.appType = miAppInfo.getAppType();
        this.cpMark = miAppInfo.getCpMark();
        this.orientation = miAppInfo.getOrientation();
        this.weakAccount = miAppInfo.isWeakAccount();
        this.payMode = miAppInfo.getPayMode();
        this.account = miAppInfo.getAccount();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ MiAppEntry(b bVar) {
        this();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
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
        parcel.writeString(this.pkgName);
        parcel.writeString(this.pkgLabel);
        parcel.writeInt(this.pid);
        parcel.writeInt(this.uid);
        parcel.writeStrongInterface(this.callback);
    }
}
