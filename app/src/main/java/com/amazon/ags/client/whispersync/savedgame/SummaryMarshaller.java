package com.amazon.ags.client.whispersync.savedgame;

import com.amazon.ags.client.whispersync.GameSummary;

/* loaded from: classes.dex */
public interface SummaryMarshaller {
    String marshal(GameSummary gameSummary);

    GameSummary unmarshal(String str);

    GameSummary[] unmarshalArray(String str);
}
