package com.xiaomi.gamecenter.sdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.widget.RemoteViews;
import com.xiaomi.gamecenter.sdk.IServiceCallback;
import com.xiaomi.gamecenter.sdk.entry.CardBuyInfo;
import com.xiaomi.gamecenter.sdk.entry.LoginResult;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOffline;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOnline;

/* loaded from: classes.dex */
public interface IGameCenterSDK extends IInterface {

    /* loaded from: classes.dex */
    public abstract class Stub extends Binder implements IGameCenterSDK {
        private static final String DESCRIPTOR = "com.xiaomi.gamecenter.sdk.IGameCenterSDK";
        static final int TRANSACTION_ConnService = 1;
        static final int TRANSACTION_getRemoteViews = 11;
        static final int TRANSACTION_miCardPay = 6;
        static final int TRANSACTION_miGetAccountInfo = 7;
        static final int TRANSACTION_miLogin = 2;
        static final int TRANSACTION_miLogout = 3;
        static final int TRANSACTION_miUniPayOffline = 4;
        static final int TRANSACTION_miUniPayOnline = 5;
        static final int TRANSACTION_openAppReport = 10;
        static final int TRANSACTION_registCallback = 8;
        static final int TRANSACTION_unregistCallBack = 9;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGameCenterSDK asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IGameCenterSDK)) ? new m(iBinder) : (IGameCenterSDK) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean ConnService = ConnService(parcel.readInt() != 0 ? (MiAppInfo) MiAppInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(ConnService ? 1 : 0);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    LoginResult miLogin = miLogin();
                    parcel2.writeNoException();
                    if (miLogin == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    miLogin.writeToParcel(parcel2, 1);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    miLogout();
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    int miUniPayOffline = miUniPayOffline(parcel.readInt() != 0 ? (MiBuyInfoOffline) MiBuyInfoOffline.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(miUniPayOffline);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    int miUniPayOnline = miUniPayOnline(parcel.readInt() != 0 ? (MiBuyInfoOnline) MiBuyInfoOnline.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(miUniPayOnline);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    int miCardPay = miCardPay(parcel.readInt() != 0 ? (CardBuyInfo) CardBuyInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(miCardPay);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    MiAccountInfo miGetAccountInfo = miGetAccountInfo();
                    parcel2.writeNoException();
                    if (miGetAccountInfo == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    miGetAccountInfo.writeToParcel(parcel2, 1);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    registCallback(IServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregistCallBack(IServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    openAppReport(parcel.readInt() != 0 ? (MiAppInfo) MiAppInfo.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    RemoteViews remoteViews = getRemoteViews();
                    parcel2.writeNoException();
                    if (remoteViews == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    remoteViews.writeToParcel(parcel2, 1);
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    boolean ConnService(MiAppInfo miAppInfo);

    RemoteViews getRemoteViews();

    int miCardPay(CardBuyInfo cardBuyInfo);

    MiAccountInfo miGetAccountInfo();

    LoginResult miLogin();

    void miLogout();

    int miUniPayOffline(MiBuyInfoOffline miBuyInfoOffline);

    int miUniPayOnline(MiBuyInfoOnline miBuyInfoOnline);

    void openAppReport(MiAppInfo miAppInfo);

    void registCallback(IServiceCallback iServiceCallback);

    void unregistCallBack(IServiceCallback iServiceCallback);
}
