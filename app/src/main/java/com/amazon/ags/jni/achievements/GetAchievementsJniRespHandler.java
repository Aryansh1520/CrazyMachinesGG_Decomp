package com.amazon.ags.jni.achievements;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.achievements.GetAchievementsResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class GetAchievementsJniRespHandler extends JniResponseHandler implements AGResponseCallback<GetAchievementsResponse> {
    private static String LOG_TAG = "ReqAchievementsJniRespHandler";

    public GetAchievementsJniRespHandler(int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(GetAchievementsResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniRequestAchievements response - onFailure");
            AchievementsJni.getAchievementsResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniRequestAchievements response - onSuccess");
            AchievementsJni.getAchievementsResponseSuccess(result, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
