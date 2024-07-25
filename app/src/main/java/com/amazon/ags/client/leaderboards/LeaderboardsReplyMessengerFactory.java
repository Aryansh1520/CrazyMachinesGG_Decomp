package com.amazon.ags.client.leaderboards;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.leaderboards.Leaderboard;
import com.amazon.ags.api.leaderboards.LeaderboardPercentileItem;
import com.amazon.ags.api.leaderboards.Score;
import com.amazon.ags.api.leaderboards.SubmitScoreResponse;
import com.amazon.ags.api.profiles.Player;
import com.amazon.ags.client.BaseReplyMessengerFactory;
import com.amazon.ags.client.RequestResponseImp;
import com.amazon.ags.client.profiles.PlayerImpl;
import com.amazon.ags.constants.LeaderboardBindingKeys;
import com.amazon.ags.constants.LeaderboardFilter;
import com.amazon.ags.constants.ScoreFormat;
import com.amazon.ags.constants.ServiceActionCode;
import com.amazon.ags.constants.SubmitScoreResultJSONKeys;
import com.amazon.ags.overlay.PopUpContent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class LeaderboardsReplyMessengerFactory<T extends RequestResponse> extends BaseReplyMessengerFactory<T> {
    private static final String FEATURE_NAME = "LB";
    private static final String TAG = "LB_" + LeaderboardsReplyMessengerFactory.class.getSimpleName();
    private static final String UNKNOWN_VALUE = "UNKNOWN";

    public LeaderboardsReplyMessengerFactory(Context context) {
        super(context);
    }

    @Override // com.amazon.ags.client.BaseReplyMessengerFactory
    protected final T processMessage(Message msg) {
        Bundle responseBundle = msg.getData();
        int responseCode = msg.arg1;
        Log.d(TAG, "Processing incoming service response message of type: " + msg.what + " and responseCode: " + responseCode);
        switch (msg.what) {
            case 7:
                if (responseCode != 17) {
                    int errorCode = msg.arg2;
                    Log.d(TAG, "The service request was a failure with code " + errorCode + ", constructing failure response");
                    return new GetScoresResponseImp(errorCode, ErrorCode.fromServiceResponseCode(errorCode));
                }
                return unbundleRequestScoresResponse(responseBundle, responseCode);
            case 8:
                if (responseCode != 17) {
                    int errorCode2 = msg.arg2;
                    Log.d(TAG, "The service request was a failure with code " + errorCode2 + ", constructing failure response");
                    return new SubmitScoreResponseImp(errorCode2, ErrorCode.fromServiceResponseCode(errorCode2));
                }
                return unbundleSubmitScoreResponse(responseBundle, responseCode);
            case 9:
                if (responseCode != 17) {
                    int errorCode3 = msg.arg2;
                    Log.d(TAG, "The service request was a failure with code " + errorCode3 + ", constructing failure response");
                    return new GetLeaderboardsResponseImp(errorCode3, ErrorCode.fromServiceResponseCode(errorCode3));
                }
                return unbundleRequestLeaderboardsResponse(responseBundle, responseCode);
            case 10:
                if (responseCode != 17) {
                    int errorCode4 = msg.arg2;
                    Log.d(TAG, "The service request was a failure with code " + errorCode4 + ", constructing failure response");
                    return new GetPlayerScoreResponseImp(errorCode4, ErrorCode.fromServiceResponseCode(errorCode4));
                }
                return unbundleRequestUserScoreResponse(responseBundle, responseCode);
            case 27:
            case 28:
                if (responseCode != 17) {
                    Log.d(TAG, "The service request was a failure with code 19, constructing failure response");
                    return new RequestResponseImp(19, ErrorCode.fromServiceResponseCode(19));
                }
                return unbundleRequestResponse(responseBundle, responseCode);
            case ServiceActionCode.REQUEST_PERCENTILES /* 31 */:
                if (responseCode != 17) {
                    Log.d(TAG, "The service request was a failure with code 19, constructing failure response");
                    return new GetLeaderboardPercentilesResponseImp(19, ErrorCode.fromServiceResponseCode(19));
                }
                return unbundleRequestPercentilesResponse(responseBundle, responseCode);
            default:
                Log.e(TAG, "Illegal value received for request type parameter: " + msg.what);
                throw new IllegalArgumentException("Received an invalid value for request type parameter");
        }
    }

    public final T unbundleRequestResponse(Bundle bundle, int replyCode) {
        return new RequestResponseImp(replyCode, ErrorCode.NONE);
    }

    protected final T unbundleSubmitScoreResponse(Bundle responseBundle, int responseCode) {
        String[] results = responseBundle.getStringArray(LeaderboardBindingKeys.LEADERBOARD_SUBMIT_RESULT_KEY);
        Map<LeaderboardFilter, Boolean> improvedInFilter = new HashMap<>();
        Map<LeaderboardFilter, Integer> rankInFilter = new HashMap<>();
        for (String result : results) {
            try {
                JSONObject resultJSON = new JSONObject(result);
                Log.v(TAG, "Parsing json element: " + resultJSON);
                String filterString = resultJSON.getString(SubmitScoreResultJSONKeys.FILTER);
                if (LeaderboardFilter.valueOf(filterString) == null) {
                    Log.w(TAG, "Invalid filter returned from service: " + filterString);
                } else {
                    LeaderboardFilter filter = LeaderboardFilter.valueOf(filterString);
                    int rank = resultJSON.getInt(SubmitScoreResultJSONKeys.PLAYER_RANK);
                    boolean isImproved = resultJSON.getBoolean(SubmitScoreResultJSONKeys.IS_IMPROVED);
                    improvedInFilter.put(filter, Boolean.valueOf(isImproved));
                    rankInFilter.put(filter, Integer.valueOf(rank));
                }
            } catch (JSONException e) {
                Log.w(TAG, "Caught JSON Exception, skipping this Submit Score Result Element");
            }
        }
        SubmitScoreResponse response = new SubmitScoreResponseImp(improvedInFilter, rankInFilter, responseCode);
        Log.d(TAG, "Created SubmitScoreResponse: " + response);
        int overlayActionCode = responseBundle.getInt(LeaderboardBindingKeys.LEADERBOARD_OVERLAY_CALLBACK_KEY);
        RemoteViews remoteViews = (RemoteViews) responseBundle.getParcelable(LeaderboardBindingKeys.LEADERBOARD_POP_UP_LAYOUT_KEY);
        String leaderboardId = responseBundle.getString(LeaderboardBindingKeys.LEADERBOARD_ID_KEY);
        if (remoteViews != null) {
            generateSubmitScorePopUp(remoteViews, overlayActionCode, leaderboardId);
        }
        return response;
    }

    protected final T unbundleRequestLeaderboardsResponse(Bundle responseBundle, int responseCode) {
        String[] names = responseBundle.getStringArray(LeaderboardBindingKeys.LEADERBOARD_GET_BOARDS_BUNDLE_NAMES_KEY);
        String[] ids = responseBundle.getStringArray(LeaderboardBindingKeys.LEADERBOARD_GET_BOARDS_BUNDLE_IDS_KEY);
        String[] displays = responseBundle.getStringArray(LeaderboardBindingKeys.LEADERBOARD_DISPLAY_TEXT_KEY);
        String[] formats = responseBundle.getStringArray(LeaderboardBindingKeys.LEADERBOARD_DATA_FORMAT_KEY);
        if (displays == null) {
            displays = new String[ids.length];
            Arrays.fill(displays, UNKNOWN_VALUE);
        }
        if (formats == null) {
            formats = new String[displays.length];
            Arrays.fill(formats, ScoreFormat.UNKNOWN.toString());
        }
        if (names.length != ids.length || ids.length != displays.length || displays.length != formats.length) {
            return new GetLeaderboardsResponseImp(19, ErrorCode.DATA_VALIDATION_ERROR);
        }
        List<Leaderboard> leaderboards = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            try {
                ScoreFormat scoreFormat = ScoreFormat.valueOf(formats[i]);
                leaderboards.add(new LeaderboardImpl(ids[i], names[i], displays[i], scoreFormat));
            } catch (IllegalArgumentException e) {
                return new GetLeaderboardsResponseImp(19, ErrorCode.DATA_VALIDATION_ERROR);
            } catch (NullPointerException e2) {
                return new GetLeaderboardsResponseImp(19, ErrorCode.DATA_VALIDATION_ERROR);
            }
        }
        return new GetLeaderboardsResponseImp(leaderboards, responseCode);
    }

    protected final T unbundleRequestScoresResponse(Bundle responseBundle, int responseCode) {
        long[] scoreValues = responseBundle.getLongArray(LeaderboardBindingKeys.LEADERBOARD_SCORES_ARRAY_KEY);
        int[] ranks = responseBundle.getIntArray(LeaderboardBindingKeys.LEADERBOARD_RANKS_ARRAY_KEY);
        String[] playerAliases = responseBundle.getStringArray(LeaderboardBindingKeys.LEADERBOARD_PLAYER_ALIASES_ARRAY_KEY);
        String displayText = responseBundle.getString(LeaderboardBindingKeys.LEADERBOARD_DISPLAY_TEXT_KEY);
        String scoreFormatString = responseBundle.getString(LeaderboardBindingKeys.LEADERBOARD_DATA_FORMAT_KEY);
        ScoreFormat scoreFormat = ScoreFormat.valueOf(scoreFormatString);
        String name = responseBundle.getString(LeaderboardBindingKeys.LEADERBOARD_NAME_KEY);
        String leaderboardId = responseBundle.getString(LeaderboardBindingKeys.LEADERBOARD_ID_KEY);
        Score[] scores = new Score[scoreValues.length];
        for (int i = 0; i < scoreValues.length; i++) {
            Player player = new PlayerImpl(playerAliases[i]);
            scores[i] = new ScoreImp(scoreValues[i], player, ranks[i], leaderboardId);
        }
        return new GetScoresResponseImp(scores, displayText, scoreFormat, name, leaderboardId, responseCode);
    }

    protected final T unbundleRequestUserScoreResponse(Bundle responseBundle, int responseCode) {
        long scoreValue = responseBundle.getLong(LeaderboardBindingKeys.LEADERBOARD_SUBMIT_SCORE_KEY);
        int rank = responseBundle.getInt(LeaderboardBindingKeys.LEADERBOARD_PLAYER_RANK_KEY);
        return new GetPlayerScoreResponseImp(scoreValue, rank, responseCode);
    }

    private final void generateSubmitScorePopUp(RemoteViews remoteViews, int overlayActionCode, String leaderboardId) {
        Log.d(TAG, "Entering generateSubmitScorePopUp with RemoteViews " + remoteViews);
        Bundle overlayDataBundle = new Bundle();
        overlayDataBundle.putString(LeaderboardBindingKeys.LEADERBOARD_ID_KEY, leaderboardId);
        PopUpContent content = new PopUpContent(overlayActionCode, remoteViews, overlayDataBundle);
        this.popUpManager.executePopUp(content);
    }

    protected final T unbundleRequestPercentilesResponse(Bundle responseBundle, int responseCode) {
        long[] scoreValues = responseBundle.getLongArray(LeaderboardBindingKeys.LEADERBOARD_SCORES_ARRAY_KEY);
        int[] percentiles = responseBundle.getIntArray(LeaderboardBindingKeys.LEADERBOARD_PERCENTILES_ARRAY_KEY);
        String[] aliases = responseBundle.getStringArray(LeaderboardBindingKeys.LEADERBOARD_PLAYER_ALIASES_ARRAY_KEY);
        String leaderboardId = responseBundle.getString(LeaderboardBindingKeys.LEADERBOARD_ID_KEY);
        String leaderboardName = responseBundle.getString(LeaderboardBindingKeys.LEADERBOARD_NAME_KEY);
        String leaderboardDisplay = responseBundle.getString(LeaderboardBindingKeys.LEADERBOARD_DISPLAY_TEXT_KEY);
        String leaderboardFormat = responseBundle.getString(LeaderboardBindingKeys.LEADERBOARD_DATA_FORMAT_KEY);
        int userIndex = responseBundle.getInt(LeaderboardBindingKeys.LEADERBOARD_USER_INDEX_KEY, -1);
        if (scoreValues == null || percentiles == null || aliases == null || TextUtils.isEmpty(leaderboardId) || TextUtils.isEmpty(leaderboardName) || TextUtils.isEmpty(leaderboardFormat)) {
            return new GetLeaderboardPercentilesResponseImp(19, ErrorCode.DATA_VALIDATION_ERROR);
        }
        if (scoreValues.length != percentiles.length || percentiles.length != aliases.length || userIndex < -1 || userIndex >= aliases.length) {
            return new GetLeaderboardPercentilesResponseImp(19, ErrorCode.DATA_VALIDATION_ERROR);
        }
        try {
            Leaderboard leaderboard = new LeaderboardImpl(leaderboardId, leaderboardName, leaderboardDisplay, ScoreFormat.valueOf(leaderboardFormat));
            List<LeaderboardPercentileItem> items = new ArrayList<>(scoreValues.length);
            int count = scoreValues.length;
            for (int i = 0; i < count; i++) {
                LeaderboardPercentileItem item = new LeaderboardPercentileItemImp(aliases[i], scoreValues[i], percentiles[i]);
                items.add(item);
            }
            return new GetLeaderboardPercentilesResponseImp(leaderboard, items, userIndex, responseCode);
        } catch (IllegalArgumentException e) {
            return new GetLeaderboardPercentilesResponseImp(19, ErrorCode.DATA_VALIDATION_ERROR);
        } catch (NullPointerException e2) {
            return new GetLeaderboardPercentilesResponseImp(19, ErrorCode.DATA_VALIDATION_ERROR);
        }
    }
}
