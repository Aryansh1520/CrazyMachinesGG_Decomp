package com.amazon.ags.api.achievements;

import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.constants.IconSize;

/* loaded from: classes.dex */
public interface AchievementsClient {
    AGResponseHandle<GetAchievementResponse> getAchievement(String str, Object... objArr);

    AGResponseHandle<GetAchievementsResponse> getAchievements(Object... objArr);

    AGResponseHandle<LoadIconResponse> loadIcon(String str, IconSize iconSize, boolean z, Object... objArr);

    AGResponseHandle<RequestResponse> resetAchievement(String str, Object... objArr);

    AGResponseHandle<RequestResponse> resetAchievements(Object... objArr);

    AGResponseHandle<RequestResponse> showAchievementsOverlay(Object... objArr);

    AGResponseHandle<UpdateProgressResponse> updateProgress(String str, float f, Object... objArr);
}
