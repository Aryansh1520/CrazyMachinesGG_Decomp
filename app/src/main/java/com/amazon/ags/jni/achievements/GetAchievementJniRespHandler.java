package com.amazon.ags.jni.achievements;

import android.util.Log;
import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.achievements.GetAchievementResponse;
import com.amazon.ags.jni.JniResponseHandler;

/* loaded from: classes.dex */
public class GetAchievementJniRespHandler extends JniResponseHandler implements AGResponseCallback<GetAchievementResponse> {
    private static String LOG_TAG = "ReqAchievementJniRespHandler";

    public GetAchievementJniRespHandler(int developerTag, long callbackPointer) {
        super(developerTag, callbackPointer);
    }

    @Override // com.amazon.ags.api.AGResponseCallback
    public void onComplete(GetAchievementResponse result) {
        if (result.isError()) {
            Log.d(LOG_TAG, "jniRequestAchievement response - onFailure");
            AchievementsJni.getAchievementResponseFailure(this.m_CallbackPointer, result.getError().ordinal(), this.m_DeveloperTag);
        } else {
            Log.d(LOG_TAG, "jniRequestAchievement response - onSuccess");
            AchievementsJni.getAchievementResponseSuccess(result, this.m_CallbackPointer, this.m_DeveloperTag);
        }
    }
}
