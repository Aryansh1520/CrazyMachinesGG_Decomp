package com.amazon.ags.jni.leaderboards;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.leaderboards.GetScoresResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class GetScoresJniResponseHandler extends JniResponseHandler implements AGResponseCallback<GetScoresResponse> {
    private static String LOG_TAG = "RequestScoresJniResponseHandler";

    public GetScoresJniResponseHandler(int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(GetScoresResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniRequestScores response - onFailure");
            LeaderboardsJni.getScoresResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniRequestScores response - onSuccess");
            LeaderboardsJni.getScoresResponseSuccess(result, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
