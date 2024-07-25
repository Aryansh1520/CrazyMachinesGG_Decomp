package com.amazon.ags.client.profiles;

import com.amazon.ags.api.profiles.Player;

/* loaded from: classes.dex */
public class PlayerImpl implements Player {
    private final String playerAlias;

    public PlayerImpl(String playerAlias) {
        this.playerAlias = playerAlias;
    }

    @Override // com.amazon.ags.api.profiles.Player
    public final String getAlias() {
        return this.playerAlias;
    }

    public final String toString() {
        return " Alias: " + this.playerAlias;
    }
}
