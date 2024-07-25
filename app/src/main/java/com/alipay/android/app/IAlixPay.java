package com.alipay.android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import com.alipay.android.app.IRemoteServiceCallback;

/* loaded from: classes.dex */
public interface IAlixPay extends IInterface {

    /* loaded from: classes.dex */
    public abstract class Stub extends Binder implements IAlixPay {
        public Stub() {
            attachInterface(this, "com.alipay.android.app.IAlixPay");
        }

        public static IAlixPay asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.alipay.android.app.IAlixPay");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof IAlixPay)) ? new b(iBinder) : (IAlixPay) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            switch (i) {
                case 1:
                    parcel.enforceInterface("com.alipay.android.app.IAlixPay");
                    String Pay = Pay(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(Pay);
                    return true;
                case 2:
                    parcel.enforceInterface("com.alipay.android.app.IAlixPay");
                    String test = test();
                    parcel2.writeNoException();
                    parcel2.writeString(test);
                    return true;
                case 3:
                    parcel.enforceInterface("com.alipay.android.app.IAlixPay");
                    registerCallback(IRemoteServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface("com.alipay.android.app.IAlixPay");
                    unregisterCallback(IRemoteServiceCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString("com.alipay.android.app.IAlixPay");
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    String Pay(String str);

    void registerCallback(IRemoteServiceCallback iRemoteServiceCallback);

    String test();

    void unregisterCallback(IRemoteServiceCallback iRemoteServiceCallback);
}
