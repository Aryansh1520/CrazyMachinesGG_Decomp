package com.xiaomi.gamecenter.sdk;

import android.os.IBinder;
import android.os.Parcel;
import android.widget.RemoteViews;
import com.xiaomi.gamecenter.sdk.entry.CardBuyInfo;
import com.xiaomi.gamecenter.sdk.entry.LoginResult;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOffline;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOnline;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class m implements IGameCenterSDK {
    private IBinder a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(IBinder iBinder) {
        this.a = iBinder;
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public boolean ConnService(MiAppInfo miAppInfo) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            if (miAppInfo != null) {
                obtain.writeInt(1);
                miAppInfo.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            this.a.transact(1, obtain, obtain2, 0);
            obtain2.readException();
            return obtain2.readInt() != 0;
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this.a;
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public RemoteViews getRemoteViews() {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            this.a.transact(11, obtain, obtain2, 0);
            obtain2.readException();
            return obtain2.readInt() != 0 ? (RemoteViews) RemoteViews.CREATOR.createFromParcel(obtain2) : null;
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public int miCardPay(CardBuyInfo cardBuyInfo) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            if (cardBuyInfo != null) {
                obtain.writeInt(1);
                cardBuyInfo.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            this.a.transact(6, obtain, obtain2, 0);
            obtain2.readException();
            return obtain2.readInt();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public MiAccountInfo miGetAccountInfo() {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            this.a.transact(7, obtain, obtain2, 0);
            obtain2.readException();
            return obtain2.readInt() != 0 ? (MiAccountInfo) MiAccountInfo.CREATOR.createFromParcel(obtain2) : null;
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public LoginResult miLogin() {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            this.a.transact(2, obtain, obtain2, 0);
            obtain2.readException();
            return obtain2.readInt() != 0 ? (LoginResult) LoginResult.CREATOR.createFromParcel(obtain2) : null;
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public void miLogout() {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            this.a.transact(3, obtain, obtain2, 0);
            obtain2.readException();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public int miUniPayOffline(MiBuyInfoOffline miBuyInfoOffline) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            if (miBuyInfoOffline != null) {
                obtain.writeInt(1);
                miBuyInfoOffline.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            this.a.transact(4, obtain, obtain2, 0);
            obtain2.readException();
            return obtain2.readInt();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public int miUniPayOnline(MiBuyInfoOnline miBuyInfoOnline) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            if (miBuyInfoOnline != null) {
                obtain.writeInt(1);
                miBuyInfoOnline.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            this.a.transact(5, obtain, obtain2, 0);
            obtain2.readException();
            return obtain2.readInt();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public void openAppReport(MiAppInfo miAppInfo) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            if (miAppInfo != null) {
                obtain.writeInt(1);
                miAppInfo.writeToParcel(obtain, 0);
            } else {
                obtain.writeInt(0);
            }
            this.a.transact(10, obtain, obtain2, 0);
            obtain2.readException();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public void registCallback(IServiceCallback iServiceCallback) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            obtain.writeStrongBinder(iServiceCallback != null ? iServiceCallback.asBinder() : null);
            this.a.transact(8, obtain, obtain2, 0);
            obtain2.readException();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.xiaomi.gamecenter.sdk.IGameCenterSDK
    public void unregistCallBack(IServiceCallback iServiceCallback) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.xiaomi.gamecenter.sdk.IGameCenterSDK");
            obtain.writeStrongBinder(iServiceCallback != null ? iServiceCallback.asBinder() : null);
            this.a.transact(9, obtain, obtain2, 0);
            obtain2.readException();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }
}
