package com.mappn.sdk.uc.net;

import android.content.Context;
import com.mappn.sdk.common.net.ApiRequestListener;
import com.mappn.sdk.uc.util.Constants;
import com.mappn.sdk.uc.util.UserUtil;
import java.util.HashMap;

/* loaded from: classes.dex */
public class Api {
    public static final int ACTION_ADD_ANDROID_BIND_INFO = 3;
    public static final int ACTION_GET_APKURL = 5;
    public static final int ACTION_GET_FRIENDS = 6;
    public static final int ACTION_LOGIN = 0;
    public static final int ACTION_MODFIY = 4;
    public static final int ACTION_REGISTER = 1;
    public static final int ACTION_SENT_MSG = 7;
    public static final int ACTION_VERIFY_TOKEN = 2;
    public static final String API_BASE_URL = "http://api.gfan.com/ifttt/";
    public static final String API_MARKET_URL = "http://api.gfan.com/market/api";
    public static final String API_UCENTER_HOST = "http://api.gfan.com/uc1/common/";
    public static final String API_UC_SERVER = "http://weibotest.gfan.com/uc_server/api/";
    public static final String API_UC_WEIBO_LOGIN = "http://weibotest.gfan.com/uc_server/weibo_login/jump.php";
    public static final String TAG = "Api";
    static final String[] a = {"http://api.gfan.com/uc1/common/login_token", "http://api.gfan.com/uc1/common/register_token", "http://api.gfan.com/uc1/common/verify_token", "http://api.gfan.com/ifttt/businessProcess.do", "http://api.gfan.com/uc1/common/resetAccount_token", "http://api.gfan.com/market/apigetDetail", "http://weibotest.gfan.com/uc_server/api/friend.api.php", "http://weibotest.gfan.com/uc_server/api/pm_send.api.php"};

    public static void getApkUrl(Context context, ApiRequestListener apiRequestListener, String str) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("local_version", 1);
        hashMap.put("packagename", str);
        new ApiAsyncTask(context, 5, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void getFriendList(Context context, ApiRequestListener apiRequestListener, Long l, int i, int i2) {
        HashMap hashMap = new HashMap(3);
        hashMap.put("uid", new StringBuilder().append(l).toString());
        hashMap.put("page", new StringBuilder().append(i).toString());
        hashMap.put("pagesize", new StringBuilder().append(i2).toString());
        new ApiAsyncTask(context, 6, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void login(Context context, ApiRequestListener apiRequestListener, String str, String str2) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("username", str);
        hashMap.put(Constants.GRANT_TYPE, str2);
        new ApiAsyncTask(context, 0, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void modfiy(Context context, ApiRequestListener apiRequestListener, Long l, String str, String str2, String str3) {
        HashMap hashMap = new HashMap(3);
        hashMap.put("uid", l);
        hashMap.put("deprecatedname", str3);
        hashMap.put("username", str);
        hashMap.put(Constants.GRANT_TYPE, str2);
        hashMap.put(Constants.KEY_USER_EMAIL, UserUtil.randomMail());
        new ApiAsyncTask(context, 4, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void register(Context context, ApiRequestListener apiRequestListener, String str, String str2, String str3) {
        register(context, apiRequestListener, str, str2, str3, 0);
    }

    public static void register(Context context, ApiRequestListener apiRequestListener, String str, String str2, String str3, int i) {
        HashMap hashMap = new HashMap(3);
        hashMap.put("username", str);
        hashMap.put(Constants.GRANT_TYPE, str2);
        hashMap.put(Constants.KEY_USER_EMAIL, str3);
        hashMap.put("rename", Integer.valueOf(i));
        new ApiAsyncTask(context, 1, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void sentMsg(Context context, ApiRequestListener apiRequestListener, Long l, String str, String str2, String str3) {
        HashMap hashMap = new HashMap(3);
        hashMap.put("fromid", new StringBuilder().append(l).toString());
        hashMap.put("toids", str);
        hashMap.put("subject", str2);
        hashMap.put("message", str3);
        new ApiAsyncTask(context, 7, apiRequestListener, hashMap).execute(new Void[0]);
    }

    public static void verifyToken(Context context, ApiRequestListener apiRequestListener, String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("token", str);
        new ApiAsyncTask(context, 2, apiRequestListener, hashMap).execute(new Void[0]);
    }
}
