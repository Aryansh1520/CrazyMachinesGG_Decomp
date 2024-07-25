package com.amazon.ags.overlay;

import android.os.Bundle;
import android.widget.RemoteViews;

/* loaded from: classes.dex */
public class PopUpContent {
    private final int overlayActionCode;
    private final Bundle overlayData;
    private final RemoteViews remoteViews;

    public PopUpContent(int overlayActionCode, RemoteViews remoteViews, Bundle overlayData) {
        this.overlayActionCode = overlayActionCode;
        this.remoteViews = remoteViews;
        this.overlayData = overlayData;
    }

    public final int getOverlayActionCode() {
        return this.overlayActionCode;
    }

    public final RemoteViews getRemoteViews() {
        return this.remoteViews;
    }

    public final Bundle getOverlayData() {
        return this.overlayData;
    }

    public String toString() {
        return "PopUpContent -\n + overlayActionCode: " + this.overlayActionCode + "\n + remoteViews: " + this.remoteViews + "\n + overlayData: " + this.overlayData;
    }
}
