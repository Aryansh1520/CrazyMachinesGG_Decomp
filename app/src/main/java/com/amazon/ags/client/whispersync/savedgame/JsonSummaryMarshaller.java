package com.amazon.ags.client.whispersync.savedgame;

import android.util.Log;
import com.amazon.ags.client.whispersync.GameSummary;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class JsonSummaryMarshaller implements SummaryMarshaller {
    private static final String DESCRIPTION_KEY = "Description";
    private static final String DEVICE_ID_KEY = "DeviceId";
    private static final String FEATURE_NAME = "STC";
    private static final String MD5_KEY = "MD5";
    private static final String SAVE_TIME_KEY = "SaveTime";
    private static final String TAG = "STC_" + JsonSummaryMarshaller.class.getSimpleName();
    private static final String VERSION_KEY = "Version";

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryMarshaller
    public final String marshal(GameSummary summary) {
        if (summary == null) {
            return null;
        }
        JSONObject json = new JSONObject();
        try {
            json.put(VERSION_KEY, summary.getVersion());
            json.put(MD5_KEY, summary.getMd5());
            json.put(SAVE_TIME_KEY, summary.getSaveDate().getTime());
            json.put(DESCRIPTION_KEY, summary.getDescription());
            json.put(DEVICE_ID_KEY, summary.getDevice());
            return json.toString();
        } catch (Exception e) {
            Log.e(TAG, "Unable to compose JSON object for Summary:" + summary.toString(), e);
            return null;
        }
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryMarshaller
    public final GameSummary unmarshal(String jsonSummary) {
        if (jsonSummary == null) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(jsonSummary);
            return makeSummary(json);
        } catch (Exception e) {
            Log.e(TAG, "Invalid JSON doc for Saved Game Summary:" + jsonSummary, e);
            return null;
        }
    }

    @Override // com.amazon.ags.client.whispersync.savedgame.SummaryMarshaller
    public final GameSummary[] unmarshalArray(String jsonSummaries) {
        try {
            JSONArray summaryArray = new JSONArray(jsonSummaries);
            GameSummary[] summaries = new SimpleGameSummary[summaryArray.length()];
            for (int i = 0; i < summaryArray.length(); i++) {
                JSONObject summary = summaryArray.getJSONObject(i);
                summaries[i] = makeSummary(summary);
            }
            return summaries;
        } catch (JSONException e) {
            Log.e(TAG, "Invalid JSON doc for Summaries List");
            return new SimpleGameSummary[0];
        }
    }

    private GameSummary makeSummary(JSONObject json) throws JSONException {
        String version = json.getString(VERSION_KEY);
        String md5 = json.getString(MD5_KEY);
        Date saveTime = new Date(json.getLong(SAVE_TIME_KEY));
        String description = json.optString(DESCRIPTION_KEY);
        String device = json.optString(DEVICE_ID_KEY);
        return new SimpleGameSummary(version, md5, saveTime, description, device);
    }
}
