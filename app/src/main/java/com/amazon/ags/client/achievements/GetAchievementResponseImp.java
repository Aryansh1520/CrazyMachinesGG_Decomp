package com.amazon.ags.client.achievements;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.achievements.Achievement;
import com.amazon.ags.api.achievements.GetAchievementResponse;
import com.amazon.ags.client.RequestResponseImp;

/* loaded from: classes.dex */
public class GetAchievementResponseImp extends RequestResponseImp implements GetAchievementResponse {
    private Achievement achievement;

    public GetAchievementResponseImp(Achievement achievement, int responseCode) {
        super(responseCode);
        this.achievement = null;
        this.achievement = achievement;
    }

    public GetAchievementResponseImp(int responseCode, ErrorCode errorCode) {
        super(responseCode, errorCode);
        this.achievement = null;
    }

    @Override // com.amazon.ags.api.achievements.GetAchievementResponse
    public final Achievement getAchievement() {
        return this.achievement;
    }

    @Override // com.amazon.ags.client.RequestResponseImp
    public final int getEventType() {
        return 12;
    }

    @Override // com.amazon.ags.client.RequestResponseImp, com.amazon.ags.api.RequestResponse
    public final String toString() {
        String text = super.toString() + "\n Achievement - " + this.achievement;
        return text;
    }
}
