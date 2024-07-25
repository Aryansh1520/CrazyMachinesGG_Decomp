package com.mappn.sdk.uc;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import com.mappn.sdk.common.utils.DESUtil;
import com.mappn.sdk.uc.activity.LoginActivity;
import com.mappn.sdk.uc.activity.ModfiyActivity;
import com.mappn.sdk.uc.activity.RegisterActivity;
import com.mappn.sdk.uc.util.PrefUtil;
import com.mappn.sdk.uc.util.UserUtil;

/* loaded from: classes.dex */
public class GfanUCenter {
    public static final int RETURN_TYPE_LOGIN = 0;
    public static final int RETURN_TYPE_MODFIY = 2;
    public static final int RETURN_TYPE_REGIST = 1;
    public static GfanUCCallback gfanUCCallback;

    public static void chooseAccount(Activity activity, GfanUCCallback gfanUCCallback2) {
        PrefUtil.logout(activity.getApplicationContext());
        login(activity, gfanUCCallback2);
    }

    public static Boolean isOneKey(Activity activity) {
        Long valueOf = Long.valueOf(PrefUtil.getUid(activity.getApplicationContext()));
        if (valueOf.longValue() != -1) {
            try {
                UserVo userByUid = UserDao.getUserByUid(activity.getApplicationContext(), valueOf.longValue());
                if (userByUid != null) {
                    if (UserUtil.getOnekeyName(activity.getApplicationContext()).equals(new DESUtil(activity.getApplicationContext()).getDesAndBase64String(userByUid.getUserName()))) {
                        return true;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static void login(Activity activity, GfanUCCallback gfanUCCallback2) {
        gfanUCCallback = gfanUCCallback2;
        try {
            UserDao.queryUsersNum(activity.getApplicationContext());
        } catch (SQLiteException e) {
        } finally {
            activity.startActivity(new Intent(activity.getApplicationContext(), (Class<?>) LoginActivity.class));
        }
    }

    public static void logout(Activity activity) {
        PrefUtil.logout(activity.getApplicationContext());
    }

    public static void modfiy(Activity activity, GfanUCCallback gfanUCCallback2) {
        gfanUCCallback = gfanUCCallback2;
        activity.startActivity(new Intent(activity.getApplicationContext(), (Class<?>) ModfiyActivity.class));
    }

    public static void regist(Activity activity, GfanUCCallback gfanUCCallback2) {
        gfanUCCallback = gfanUCCallback2;
        activity.startActivity(new Intent(activity.getApplicationContext(), (Class<?>) RegisterActivity.class));
    }
}
