package com.amazon.ags.jni.achievements;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class ResetAchievementsJniRespHandler extends JniResponseHandler implements AGResponseCallback<RequestResponse> {
    private static String LOG_TAG = "ResetAchievementsJniRespHandler";

    public ResetAchievementsJniRespHandler(int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(RequestResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniResetAchievement response - onFailure");
            AchievementsJni.resetAchievementsResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniResetAchievement response - onSuccess");
            AchievementsJni.resetAchievementsResponseSuccess(result, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
