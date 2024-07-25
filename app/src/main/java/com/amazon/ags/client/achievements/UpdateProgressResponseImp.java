package com.amazon.ags.client.achievements;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.achievements.UpdateProgressResponse;
import com.amazon.ags.client.RequestResponseImp;

/* loaded from: classes.dex */
public class UpdateProgressResponseImp extends RequestResponseImp implements UpdateProgressResponse {
    private final boolean isNewlyUnlocked;

    public UpdateProgressResponseImp(boolean isNewlyUnlocked, int responseCode) {
        super(responseCode);
        this.isNewlyUnlocked = isNewlyUnlocked;
    }

    public UpdateProgressResponseImp(int responseCode, ErrorCode errorCode) {
        super(responseCode, errorCode);
        this.isNewlyUnlocked = false;
    }

    @Override // com.amazon.ags.api.achievements.UpdateProgressResponse
    public boolean isNewlyUnlocked() {
        return this.isNewlyUnlocked;
    }
}
