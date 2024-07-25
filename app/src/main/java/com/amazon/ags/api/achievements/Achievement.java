package com.amazon.ags.api.achievements;

import java.util.Date;

/* loaded from: classes.dex */
public interface Achievement {
    Date getDateUnlocked();

    String getDescription();

    String getId();

    int getPointValue();

    int getPosition();

    float getProgress();

    String getTitle();

    boolean isHidden();

    boolean isUnlocked();
}
