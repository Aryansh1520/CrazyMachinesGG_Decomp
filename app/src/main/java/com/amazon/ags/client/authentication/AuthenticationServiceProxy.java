package com.amazon.ags.client.authentication;

import android.os.Bundle;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import com.amazon.ags.client.AmazonGamesService;

/* loaded from: classes.dex */
public class AuthenticationServiceProxy implements AuthenticationService {
    private static final String FEATURE_NAME = "AGC";
    private static final String TAG = "AGC_" + AuthenticationService.class.getSimpleName();
    private AmazonGamesService amazonGamesService;

    public AuthenticationServiceProxy(AmazonGamesService amazonGamesService) {
        this.amazonGamesService = amazonGamesService;
    }

    @Override // com.amazon.ags.client.authentication.AuthenticationService
    public final boolean isAuthenticated() {
        Log.d(TAG, "Attempting to check is authenticated");
        try {
            Parcel request = Parcel.obtain();
            Bundle bundle = new Bundle();
            request.writeBundle(bundle);
            Parcel reply = this.amazonGamesService.transact(3, request);
            int replyCode = reply.readInt();
            return replyCode == 5;
        } catch (RemoteException e) {
            Log.e(TAG, "isAuthenticated(): ", e);
            return false;
        }
    }
}
