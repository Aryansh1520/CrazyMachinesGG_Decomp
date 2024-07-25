package com.amazon.ags.jni.leaderboards;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.leaderboards.GetLeaderboardsResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class GetLbsJniResponseHandler extends JniResponseHandler implements AGResponseCallback<GetLeaderboardsResponse> {
    private static String LOG_TAG = "RequestLbJniResponseHandler";

    public GetLbsJniResponseHandler(int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(GetLeaderboardsResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniRequestLeaderboards response - onFailure");
            LeaderboardsJni.getLeaderboardsResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniRequestLeaderboards response - onSuccess");
            LeaderboardsJni.getLeaderboardsResponseSuccess(result, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
