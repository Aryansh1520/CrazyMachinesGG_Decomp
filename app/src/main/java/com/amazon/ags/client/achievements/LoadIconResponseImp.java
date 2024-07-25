package com.amazon.ags.client.achievements;

import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.achievements.LoadIconResponse;
import com.amazon.ags.client.RequestResponseImp;

/* loaded from: classes.dex */
public class LoadIconResponseImp extends RequestResponseImp implements LoadIconResponse {
    private final byte[] image;

    public LoadIconResponseImp(byte[] image, int responseCode) {
        super(responseCode);
        this.image = image;
    }

    public LoadIconResponseImp(int responseCode, ErrorCode errorCode) {
        super(responseCode, errorCode);
        this.image = null;
    }

    @Override // com.amazon.ags.api.achievements.LoadIconResponse
    public byte[] getImage() {
        return this.image;
    }
}
