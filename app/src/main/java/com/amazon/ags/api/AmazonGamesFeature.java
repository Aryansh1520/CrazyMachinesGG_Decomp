package com.amazon.ags.api;

import java.util.EnumSet;

/* loaded from: classes.dex */
public enum AmazonGamesFeature {
    Leaderboards,
    Achievements,
    Whispersync;

    public static EnumSet<AmazonGamesFeature> all() {
        return EnumSet.allOf(AmazonGamesFeature.class);
    }
}
