package com.amazon.ags.client.achievements;

import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.achievements.GetAchievementResponse;
import com.amazon.ags.api.achievements.GetAchievementsResponse;
import com.amazon.ags.api.achievements.LoadIconResponse;
import com.amazon.ags.api.achievements.UpdateProgressResponse;
import com.amazon.ags.client.GCResponseHandleImpl;
import com.amazon.ags.constants.IconSize;

/* loaded from: classes.dex */
public interface AchievementsService {
    void loadIcon(String str, IconSize iconSize, boolean z, GCResponseHandleImpl<LoadIconResponse> gCResponseHandleImpl);

    void requestAchievement(String str, GCResponseHandleImpl<GetAchievementResponse> gCResponseHandleImpl);

    void requestAchievements(GCResponseHandleImpl<GetAchievementsResponse> gCResponseHandleImpl);

    void resetAchievement(String str, GCResponseHandleImpl<RequestResponse> gCResponseHandleImpl);

    void resetAchievements(GCResponseHandleImpl<RequestResponse> gCResponseHandleImpl);

    void showAchievementsOverlay(GCResponseHandleImpl<RequestResponse> gCResponseHandleImpl);

    void updateProgress(String str, float f, GCResponseHandleImpl<UpdateProgressResponse> gCResponseHandleImpl);
}
