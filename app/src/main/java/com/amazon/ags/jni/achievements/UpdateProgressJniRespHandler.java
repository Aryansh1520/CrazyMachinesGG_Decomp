package com.amazon.ags.jni.achievements;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.achievements.UpdateProgressResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class UpdateProgressJniRespHandler extends JniResponseHandler implements AGResponseCallback<UpdateProgressResponse> {
    private static String LOG_TAG = "UpdateProgressJniRespHandler";
    private String m_AchievementId;

    public UpdateProgressJniRespHandler(String achievementId, int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
        this.m_AchievementId = achievementId;
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(UpdateProgressResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniUpdateProgress response - onFailure");
            AchievementsJni.updateProgressResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniUpdateProgress response - onSuccess");
            AchievementsJni.updateProgressResponseSuccess(result, this.m_AchievementId, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
