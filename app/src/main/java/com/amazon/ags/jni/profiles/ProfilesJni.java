package com.amazon.ags.jni.profiles;

import com.amazon.ags.api.profiles.RequestPlayerProfileResponse;

/* loaded from: classes.dex */
public class ProfilesJni {
    public static native void getLocalPlayerProfileResponseFailure(long j, int i, int i2);

    public static native void getLocalPlayerProfileResponseSuccess(RequestPlayerProfileResponse requestPlayerProfileResponse, long j, int i);
}
