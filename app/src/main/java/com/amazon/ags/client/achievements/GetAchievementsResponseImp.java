package com.amazon.ags.client.achievements;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.achievements.Achievement;
import com.amazon.ags.api.achievements.GetAchievementsResponse;
import com.amazon.ags.client.RequestResponseImp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class GetAchievementsResponseImp extends RequestResponseImp implements GetAchievementsResponse {
    private List<Achievement> achievements;
    private Map<String, Achievement> achievementsMap;

    public GetAchievementsResponseImp(List<Achievement> achievements, int responseCode) {
        super(responseCode);
        this.achievements = null;
        this.achievementsMap = null;
        this.achievements = achievements;
    }

    public GetAchievementsResponseImp(int responseCode, ErrorCode errorCode) {
        super(responseCode, errorCode);
        this.achievements = null;
        this.achievementsMap = null;
    }

    @Override // com.amazon.ags.api.achievements.GetAchievementsResponse
    public final List<Achievement> getAchievementsList() {
        return this.achievements;
    }

    @Override // com.amazon.ags.api.achievements.GetAchievementsResponse
    public final Map<String, Achievement> getAchievementsMap() {
        if (this.achievements == null) {
            return null;
        }
        if (this.achievementsMap == null) {
            this.achievementsMap = new HashMap();
            for (Achievement a : this.achievements) {
                this.achievementsMap.put(a.getId(), a);
            }
        }
        return this.achievementsMap;
    }

    @Override // com.amazon.ags.api.achievements.GetAchievementsResponse
    public final int getNumVisibleAchievements() {
        if (this.achievements != null) {
            return this.achievements.size();
        }
        return 0;
    }

    @Override // com.amazon.ags.client.RequestResponseImp
    public final int getEventType() {
        return 17;
    }

    @Override // com.amazon.ags.client.RequestResponseImp, com.amazon.ags.api.RequestResponse
    public final String toString() {
        String text = super.toString();
        return text + "\n Number of AchievementsClient Returned: " + getNumVisibleAchievements();
    }
}
