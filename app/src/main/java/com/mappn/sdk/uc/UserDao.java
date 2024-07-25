package com.mappn.sdk.uc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import com.mappn.sdk.uc.util.DBUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class UserDao implements BaseColumns {
    public static void deleteUserByUid(Context context, long j) {
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        if (dBInstance == null) {
            return;
        }
        dBInstance.delete(DBUtil.TABLE_UC, "uid=?", new String[]{new StringBuilder().append(j).toString()});
    }

    public static UserVo fromCursor2User(Cursor cursor) {
        UserVo userVo = new UserVo();
        userVo.setId(cursor.getInt(cursor.getColumnIndex("_id")));
        userVo.setUserName(cursor.getString(cursor.getColumnIndex("name")));
        userVo.setUid(cursor.getLong(cursor.getColumnIndex("uid")));
        userVo.setPassword(cursor.getString(cursor.getColumnIndex(DBUtil.UC_PASS)));
        userVo.setToken(cursor.getString(cursor.getColumnIndex("token")));
        return userVo;
    }

    public static ContentValues getContentValuesByUser(UserVo userVo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", userVo.getUserName());
        contentValues.put("uid", Long.valueOf(userVo.getUid()));
        contentValues.put(DBUtil.UC_PASS, userVo.getPassword());
        contentValues.put("token", userVo.getToken());
        return contentValues;
    }

    public static UserVo getUserByUid(Context context, long j) {
        Cursor query;
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        if (dBInstance != null && (query = dBInstance.query(DBUtil.TABLE_UC, null, "uid=?", new String[]{new StringBuilder().append(j).toString()}, null, null, null)) != null) {
            r2 = query.moveToNext() ? fromCursor2User(query) : null;
            query.close();
        }
        return r2;
    }

    public static UserVo getUserByUserName(Context context, String str) {
        Cursor query;
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        if (dBInstance != null && (query = dBInstance.query(DBUtil.TABLE_UC, null, "name=?", new String[]{str}, null, null, null)) != null) {
            r2 = query.moveToNext() ? fromCursor2User(query) : null;
            query.close();
        }
        return r2;
    }

    public static void insertUser(Context context, UserVo userVo) {
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        if (dBInstance == null) {
            return;
        }
        dBInstance.insert(DBUtil.TABLE_UC, null, getContentValuesByUser(userVo));
    }

    public static List queryAllUsers(Context context) {
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        if (dBInstance == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Cursor query = dBInstance.query(DBUtil.TABLE_UC, null, null, null, null, null, null);
        if (query != null) {
            while (query.moveToNext()) {
                arrayList.add(fromCursor2User(query));
            }
            query.close();
        }
        return arrayList;
    }

    public static long queryUsersNum(Context context) {
        long j;
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        if (dBInstance == null) {
            return 0L;
        }
        Cursor query = dBInstance.query(DBUtil.TABLE_UC, new String[]{" count (*) "}, null, null, null, null, null);
        if (query == null || !query.moveToNext()) {
            j = 0;
        } else {
            j = query.getLong(0);
            query.close();
        }
        return j;
    }

    public static void updateToken(Context context, String str, String str2) {
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        if (dBInstance == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("token", str);
        dBInstance.update(DBUtil.TABLE_UC, contentValues, "name=?", new String[]{str2});
    }
}
