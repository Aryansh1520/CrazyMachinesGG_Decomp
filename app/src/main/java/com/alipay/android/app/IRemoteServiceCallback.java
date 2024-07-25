package com.alipay.android.app;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;

/* loaded from: classes.dex */
public interface IRemoteServiceCallback extends IInterface {

    /* loaded from: classes.dex */
    public abstract class Stub extends Binder implements IRemoteServiceCallback {
        public Stub() {
            attachInterface(this, "com.alipay.android.app.IRemoteServiceCallback");
        }

        public static IRemoteServiceCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.alipay.android.app.IRemoteServiceCallback");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IRemoteServiceCallback)) ? new c(iBinder) : (IRemoteServiceCallback) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.alipay.android.app.IRemoteServiceCallback");
                    startActivity(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.alipay.android.app.IRemoteServiceCallback");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void startActivity(String str, String str2, int i, Bundle bundle);
}
