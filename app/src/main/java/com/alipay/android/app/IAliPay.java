package com.alipay.android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.alipay.android.app.IRemoteServiceCallback;

/* loaded from: classes.dex */
public interface IAliPay extends IInterface {

    /* loaded from: classes.dex */
    public abstract class Stub extends Binder implements IAliPay {
        public Stub() {
            attachInterface(this, "com.alipay.android.app.IAliPay");
        }

        public static IAliPay asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.alipay.android.app.IAliPay");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IAliPay)) ? new a(iBinder) : (IAliPay) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.alipay.android.app.IAliPay");
                    String Pay = Pay(parcel.readString(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(Pay);
                    return true;
                case 2:
                    parcel.enforceInterface("com.alipay.android.app.IAliPay");
                    String test = test();
                    parcel2.writeNoException();
                    parcel2.writeString(test);
                    return true;
                case 3:
                    parcel.enforceInterface("com.alipay.android.app.IAliPay");
                    registerCallback(IRemoteServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.alipay.android.app.IAliPay");
                    unregisterCallback(IRemoteServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.alipay.android.app.IAliPay");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    String Pay(String str, String str2, String str3);

    void registerCallback(IRemoteServiceCallback iRemoteServiceCallback);

    String test();

    void unregisterCallback(IRemoteServiceCallback iRemoteServiceCallback);
}
