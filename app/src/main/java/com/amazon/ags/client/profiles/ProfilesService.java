package com.amazon.ags.client.profiles;

import com.amazon.ags.api.profiles.RequestPlayerProfileResponse;
import com.amazon.ags.client.GCResponseHandleImpl;

/* loaded from: classes.dex */
public interface ProfilesService {
    void requestLocalPlayerProfile(GCResponseHandleImpl<RequestPlayerProfileResponse> gCResponseHandleImpl);
}
