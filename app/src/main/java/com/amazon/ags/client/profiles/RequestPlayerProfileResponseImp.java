package com.amazon.ags.client.profiles;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.profiles.Player;
import com.amazon.ags.api.profiles.RequestPlayerProfileResponse;
import com.amazon.ags.client.RequestResponseImp;

/* loaded from: classes.dex */
public class RequestPlayerProfileResponseImp extends RequestResponseImp implements RequestPlayerProfileResponse {
    private final Player player;

    public RequestPlayerProfileResponseImp(Player player, int responseCode) {
        super(responseCode);
        this.player = player;
    }

    public RequestPlayerProfileResponseImp(int responseCode, ErrorCode errorCode) {
        super(responseCode, errorCode);
        this.player = null;
    }

    @Override // com.amazon.ags.client.RequestResponseImp
    public int getEventType() {
        return 18;
    }

    @Override // com.amazon.ags.api.profiles.RequestPlayerProfileResponse
    public Player getPlayer() {
        return this.player;
    }
}
