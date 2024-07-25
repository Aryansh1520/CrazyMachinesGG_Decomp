package com.amazon.ags.jni.leaderboards;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.leaderboards.GetLeaderboardPercentilesResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class GetPercentilesJniResponseHandler extends JniResponseHandler implements AGResponseCallback<GetLeaderboardPercentilesResponse> {
    private static String LOG_TAG = "GetPercentilesJniResponseHandler";

    public GetPercentilesJniResponseHandler(int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(GetLeaderboardPercentilesResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniGetPercentiles response - onFailure");
            LeaderboardsJni.getPercentilesResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniGetPercentiles response - onSuccess");
            LeaderboardsJni.getPercentilesResponseSuccess(result, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
