package com.amazon.ags.api.achievements;

import com.amazon.ags.api.RequestResponse;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public interface GetAchievementsResponse extends RequestResponse {
    List<Achievement> getAchievementsList();

    Map<String, Achievement> getAchievementsMap();

    int getNumVisibleAchievements();
}
