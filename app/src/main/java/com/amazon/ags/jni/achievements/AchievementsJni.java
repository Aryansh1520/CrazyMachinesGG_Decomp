package com.amazon.ags.jni.achievements;

import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.achievements.GetAchievementResponse;
import com.amazon.ags.api.achievements.GetAchievementsResponse;
import com.amazon.ags.api.achievements.UpdateProgressResponse;

/* loaded from: classes.dex */
public class AchievementsJni {
    public static native void getAchievementResponseFailure(long j, int i, int i2);

    public static native void getAchievementResponseSuccess(GetAchievementResponse getAchievementResponse, long j, int i);

    public static native void getAchievementsResponseFailure(long j, int i, int i2);

    public static native void getAchievementsResponseSuccess(GetAchievementsResponse getAchievementsResponse, long j, int i);

    public static native void resetAchievementResponseFailure(long j, int i, int i2);

    public static native void resetAchievementResponseSuccess(RequestResponse requestResponse, long j, int i);

    public static native void resetAchievementsResponseFailure(long j, int i, int i2);

    public static native void resetAchievementsResponseSuccess(RequestResponse requestResponse, long j, int i);

    public static native void updateProgressResponseFailure(long j, int i, int i2);

    public static native void updateProgressResponseSuccess(UpdateProgressResponse updateProgressResponse, String str, long j, int i);
}
