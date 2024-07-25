package com.amazon.ags.client.profiles;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.profiles.Player;
import com.amazon.ags.client.BaseReplyMessengerFactory;
import com.amazon.ags.constants.ProfilesBindingKeys;

/* loaded from: classes.dex */
public class ProfilesReplyMessengerFactory<T extends RequestResponse> extends BaseReplyMessengerFactory<T> {
    private static final String FEATURE_NAME = "PROFILES";
    private static final String TAG = "PROFILES_" + ProfilesReplyMessengerFactory.class.getSimpleName();

    public ProfilesReplyMessengerFactory(Context context) {
        super(context);
    }

    @Override // com.amazon.ags.client.BaseReplyMessengerFactory
    protected T processMessage(Message msg) {
        Bundle responseBundle = msg.getData();
        int responseCode = msg.arg1;
        Log.d(TAG, "Processing incoming service response message of type: " + msg.what + " and responseCode: " + responseCode);
        switch (msg.what) {
            case 18:
                if (responseCode == 17) {
                    return unbundleRequestPlayerProfileResponse(responseBundle, responseCode);
                }
                int errorCode = msg.arg2;
                Log.d(TAG, "The service request was a failure with code " + errorCode + ", constructing failure response");
                return new RequestPlayerProfileResponseImp(errorCode, ErrorCode.fromServiceResponseCode(errorCode));
            default:
                Log.e(TAG, "Illegal value received for request type parameter: " + msg.what);
                throw new IllegalArgumentException("Received an invalid value for request type parameter");
        }
    }

    public T unbundleRequestPlayerProfileResponse(Bundle bundle, int replyCode) {
        String alias = bundle.getString(ProfilesBindingKeys.ALIAS_KEY);
        Player player = new PlayerImpl(alias);
        return new RequestPlayerProfileResponseImp(player, replyCode);
    }
}
