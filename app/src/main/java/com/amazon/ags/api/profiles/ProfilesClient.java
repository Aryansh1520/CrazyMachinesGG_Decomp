package com.amazon.ags.api.profiles;

import com.amazon.ags.api.AGResponseHandle;

/* loaded from: classes.dex */
public interface ProfilesClient {
    AGResponseHandle<RequestPlayerProfileResponse> getLocalPlayerProfile(Object... objArr);
}
