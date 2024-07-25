package com.amazon.ags.client.whispersync;

import android.os.Bundle;
import com.amazon.ags.client.whispersync.savedgame.JsonSummaryMarshaller;
import com.amazon.ags.client.whispersync.savedgame.SummaryMarshaller;
import com.amazon.ags.constants.WhisperSyncBindingKeys;

/* loaded from: classes.dex */
public class SummaryBundleParser {
    private static SummaryMarshaller marshaller = new JsonSummaryMarshaller();

    protected SummaryBundleParser() {
        throw new UnsupportedOperationException();
    }

    static GameSummary parse(Bundle bundle) {
        String cloudSummaryJson = bundle.getString(WhisperSyncBindingKeys.WS_LATEST_CLOUD_GAME_SUMMARY_KEY);
        if (cloudSummaryJson == null) {
            return null;
        }
        GameSummary cloudSummary = marshaller.unmarshal(cloudSummaryJson);
        return cloudSummary;
    }
}
