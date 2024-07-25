package com.amazon.ags.client.whispersync;

import java.util.Date;

/* loaded from: classes.dex */
public interface GameSummary {
    String getDescription();

    String getDevice();

    String getMd5();

    Date getSaveDate();

    String getVersion();
}
