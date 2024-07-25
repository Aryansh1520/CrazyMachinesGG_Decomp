package com.alipay.android.app;

import android.os.IBinder;
import android.os.Parcel;

/* loaded from: classes.dex */
final class a implements IAliPay {
    private IBinder a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(IBinder iBinder) {
        this.a = iBinder;
    }

    @Override // com.alipay.android.app.IAliPay
    public final String Pay(String str, String str2, String str3) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.alipay.android.app.IAliPay");
            obtain.writeString(str);
            obtain.writeString(str2);
            obtain.writeString(str3);
            this.a.transact(1, obtain, obtain2, 0);
            obtain2.readException();
            return obtain2.readString();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this.a;
    }

    @Override // com.alipay.android.app.IAliPay
    public final void registerCallback(IRemoteServiceCallback iRemoteServiceCallback) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.alipay.android.app.IAliPay");
            obtain.writeStrongBinder(iRemoteServiceCallback != null ? iRemoteServiceCallback.asBinder() : null);
            this.a.transact(3, obtain, obtain2, 0);
            obtain2.readException();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.alipay.android.app.IAliPay
    public final String test() {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.alipay.android.app.IAliPay");
            this.a.transact(2, obtain, obtain2, 0);
            obtain2.readException();
            return obtain2.readString();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }

    @Override // com.alipay.android.app.IAliPay
    public final void unregisterCallback(IRemoteServiceCallback iRemoteServiceCallback) {
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        try {
            obtain.writeInterfaceToken("com.alipay.android.app.IAliPay");
            obtain.writeStrongBinder(iRemoteServiceCallback != null ? iRemoteServiceCallback.asBinder() : null);
            this.a.transact(4, obtain, obtain2, 0);
            obtain2.readException();
        } finally {
            obtain2.recycle();
            obtain.recycle();
        }
    }
}
