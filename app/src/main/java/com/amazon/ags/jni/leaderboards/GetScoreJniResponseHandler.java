package com.amazon.ags.jni.leaderboards;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.leaderboards.GetPlayerScoreResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class GetScoreJniResponseHandler extends JniResponseHandler implements AGResponseCallback<GetPlayerScoreResponse> {
    private static String LOG_TAG = "RequestScoreJniResponseHandler";
    private String m_LeaderboardId;

    public GetScoreJniResponseHandler(String leaderboardId, int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
        this.m_LeaderboardId = leaderboardId;
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(GetPlayerScoreResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniRequestScore response - onFailure");
            LeaderboardsJni.getPlayerScoreResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniRequestScore response - onSuccess");
            LeaderboardsJni.getPlayerScoreResponseSuccess(result, this.m_LeaderboardId, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
