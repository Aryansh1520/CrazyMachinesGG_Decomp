package com.amazon.ags.client;

import android.content.Context;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import com.amazon.ags.api.AmazonGamesStatus;

/* loaded from: classes.dex */
public interface AmazonGamesService {
    void bind(Context context);

    AmazonGamesStatus getStatus();

    boolean isReady();

    void sendMessage(Message message) throws RemoteException;

    Parcel transact(int i, Parcel parcel) throws RemoteException;
}
