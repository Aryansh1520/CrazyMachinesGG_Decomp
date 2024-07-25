package com.amazon.ags.client.achievements;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import com.amazon.ags.api.ErrorCode;
import com.amazon.ags.api.RequestResponse;
import com.amazon.ags.api.achievements.Achievement;
import com.amazon.ags.client.BaseReplyMessengerFactory;
import com.amazon.ags.client.RequestResponseImp;
import com.amazon.ags.constants.AchievementsBindingKeys;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* loaded from: classes.dex */
public class AchievementsReplyMessengerFactory<T extends RequestResponse> extends BaseReplyMessengerFactory<T> {
    private static final String FEATURE_NAME = "AC";
    private static final String TAG = "AC_" + AchievementsReplyMessengerFactory.class.getSimpleName();

    public AchievementsReplyMessengerFactory(Context context) {
        super(context);
    }

    @Override // com.amazon.ags.client.BaseReplyMessengerFactory
    protected final T processMessage(Message msg) {
        Bundle responseBundle = msg.getData();
        int responseCode = msg.arg1;
        Log.d(TAG, "Processing incoming service response message of type: " + msg.what + " and responseCode: " + responseCode);
        switch (msg.what) {
            case 12:
                if (responseCode != 17) {
                    int errorCode = msg.arg2;
                    Log.d(TAG, "The service request was a failure with code " + errorCode + ", constructing failure response");
                    return new GetAchievementResponseImp(errorCode, ErrorCode.fromServiceResponseCode(errorCode));
                }
                return unbundleRequestAchievementResponse(responseBundle, responseCode);
            case 13:
                if (responseCode != 17) {
                    int errorCode2 = msg.arg2;
                    Log.d(TAG, "The service request was a failure with code " + errorCode2 + ", constructing failure response");
                    return new UpdateProgressResponseImp(errorCode2, ErrorCode.fromServiceResponseCode(errorCode2));
                }
                return unbundleUpdateProgressResponse(responseBundle, responseCode);
            case 14:
            case 15:
            case 26:
                if (responseCode != 17) {
                    int errorCode3 = msg.arg2;
                    Log.d(TAG, "The service request was a failure with code " + errorCode3 + ", constructing failure response");
                    return new RequestResponseImp(errorCode3, ErrorCode.fromServiceResponseCode(errorCode3));
                }
                return unbundleRequestResponse(responseBundle, responseCode);
            case 16:
                if (responseCode != 17) {
                    int errorCode4 = msg.arg2;
                    Log.d(TAG, "The service request was a failure with code " + errorCode4 + ", constructing failure response");
                    return new LoadIconResponseImp(errorCode4, ErrorCode.fromServiceResponseCode(errorCode4));
                }
                return unbundleLoadIconResponse(responseBundle, responseCode);
            case 17:
                if (responseCode != 17) {
                    int errorCode5 = msg.arg2;
                    Log.d(TAG, "The service request was a failure with code " + errorCode5 + ", constructing failure response");
                    return new GetAchievementsResponseImp(errorCode5, ErrorCode.fromServiceResponseCode(errorCode5));
                }
                return unbundleRequestAchievementsResponse(responseBundle, responseCode);
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            default:
                Log.e(TAG, "Illegal value received for request type parameter: " + msg.what);
                throw new IllegalArgumentException("Received an invalid value for request type parameter");
        }
    }

    public final T unbundleRequestAchievementsResponse(Bundle bundle, int replyCode) {
        Date dateUnlocked;
        String[] id = bundle.getStringArray(AchievementsBindingKeys.ACHIEVEMENT_ID_KEY);
        String[] title = bundle.getStringArray(AchievementsBindingKeys.ACHIEVEMENT_TITLE_KEY);
        String[] description = bundle.getStringArray(AchievementsBindingKeys.ACHIEVEMENT_DESCRIPTION_KEY);
        int[] pointValue = bundle.getIntArray(AchievementsBindingKeys.ACHIEVEMENT_POINTS_KEY);
        boolean[] hidden = bundle.getBooleanArray(AchievementsBindingKeys.ACHIEVEMENT_HIDDEN_KEY);
        boolean[] unlocked = bundle.getBooleanArray(AchievementsBindingKeys.ACHIEVEMENT_UNLOCKED_KEY);
        float[] progress = bundle.getFloatArray(AchievementsBindingKeys.ACHIEVEMENT_UPDATE_PERCENT_KEY);
        int[] position = bundle.getIntArray(AchievementsBindingKeys.ACHIEVEMENT_POSITION_KEY);
        long[] dateUnlockedLong = bundle.getLongArray(AchievementsBindingKeys.ACHIEVEMENT_DATE_UNLOCKED_KEY);
        List<Achievement> achievements = new ArrayList<>(id.length);
        for (int i = 0; i < id.length; i++) {
            if (!unlocked[i] || dateUnlockedLong == null) {
                dateUnlocked = null;
            } else {
                dateUnlocked = new Date(dateUnlockedLong[i]);
            }
            Achievement a = new AchievementImpl(id[i], title[i], description[i], pointValue[i], hidden[i], unlocked[i], progress[i], position[i], dateUnlocked);
            achievements.add(a);
        }
        return new GetAchievementsResponseImp(achievements, replyCode);
    }

    public final T unbundleRequestAchievementResponse(Bundle bundle, int replyCode) {
        Date dateUnlocked;
        String id = bundle.getString(AchievementsBindingKeys.ACHIEVEMENT_ID_KEY);
        String title = bundle.getString(AchievementsBindingKeys.ACHIEVEMENT_TITLE_KEY);
        String description = bundle.getString(AchievementsBindingKeys.ACHIEVEMENT_DESCRIPTION_KEY);
        int pointValue = bundle.getInt(AchievementsBindingKeys.ACHIEVEMENT_POINTS_KEY);
        boolean hidden = bundle.getBoolean(AchievementsBindingKeys.ACHIEVEMENT_HIDDEN_KEY);
        boolean unlocked = bundle.getBoolean(AchievementsBindingKeys.ACHIEVEMENT_UNLOCKED_KEY);
        float progress = bundle.getFloat(AchievementsBindingKeys.ACHIEVEMENT_UPDATE_PERCENT_KEY);
        int position = bundle.getInt(AchievementsBindingKeys.ACHIEVEMENT_POSITION_KEY);
        long dateUnlockedLong = bundle.getLong(AchievementsBindingKeys.ACHIEVEMENT_DATE_UNLOCKED_KEY, 0L);
        if (!unlocked || dateUnlockedLong == 0) {
            dateUnlocked = null;
        } else {
            dateUnlocked = new Date(dateUnlockedLong);
        }
        Achievement achievement = new AchievementImpl(id, title, description, pointValue, hidden, unlocked, progress, position, dateUnlocked);
        return new GetAchievementResponseImp(achievement, replyCode);
    }

    public final T unbundleRequestResponse(Bundle bundle, int replyCode) {
        return new RequestResponseImp(replyCode, ErrorCode.NONE);
    }

    public final T unbundleUpdateProgressResponse(Bundle bundle, int replyCode) {
        boolean isNewlyUnlocked = bundle.getBoolean(AchievementsBindingKeys.ACHIEVEMENT_NEWLY_UNLOCKED_KEY);
        UpdateProgressResponseImp response = new UpdateProgressResponseImp(isNewlyUnlocked, replyCode);
        int overlayActionCode = bundle.getInt(AchievementsBindingKeys.ACHIEVEMENT_OVERLAY_CALLBACK_KEY);
        RemoteViews remoteViews = (RemoteViews) bundle.getParcelable(AchievementsBindingKeys.ACHIEVEMENT_POP_UP_LAYOUT_KEY);
        if (remoteViews != null) {
            this.popUpManager.executePopUp(remoteViews, overlayActionCode);
        }
        return response;
    }

    public final T unbundleLoadIconResponse(Bundle bundle, int replyCode) {
        Log.d(TAG, "Entering unbundleLoadIconResponse...");
        byte[] image = bundle.getByteArray(AchievementsBindingKeys.ACHIEVEMENT_ICON_IMAGE_KEY);
        Log.d(TAG, "icon byte array in bundle: " + image);
        return new LoadIconResponseImp(image, replyCode);
    }
}
