package com.amazon.ags.jni.leaderboards;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class SubmitScoreJniResponseHandler extends JniResponseHandler implements AGResponseCallback<SubmitScoreResponse> {
    private static String LOG_TAG = "SubmitScoreJniResponseHandler";

    public SubmitScoreJniResponseHandler(int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(SubmitScoreResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniSubmitScores response - onFailure");
            LeaderboardsJni.submitScoreResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniSubmitScores response - onSuccess");
            LeaderboardsJni.submitScoreResponseSuccess(result, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
