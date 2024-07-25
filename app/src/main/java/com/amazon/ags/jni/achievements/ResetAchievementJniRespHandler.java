package com.amazon.ags.jni.achievements;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class ResetAchievementJniRespHandler extends JniResponseHandler implements AGResponseCallback<RequestResponse> {
    private static String LOG_TAG = "ResetAchievementJniRespHandler";

    public ResetAchievementJniRespHandler(int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(RequestResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniResetAchievement response - onFailure");
            AchievementsJni.resetAchievementResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniResetAchievement response - onSuccess");
            AchievementsJni.resetAchievementResponseSuccess(result, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
