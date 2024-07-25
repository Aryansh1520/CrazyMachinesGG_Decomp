package com.mappn.sdk.uc.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/* loaded from: classes.dex */
public class DBUtil extends SDSQLiteOpenHelper {
    public static final String TABLE_UC = "uc";
    public static final String UC_COL_ID = "_id";
    public static final String UC_NAME = "name";
    public static final String UC_PASS = "pass";
    public static final String UC_TOKEN = "token";
    public static final String UC_UID = "uid";
    public static DBUtil mInstance;

    private DBUtil(Context context) {
        super(context, "uc.db", null, 1);
    }

    public static SQLiteDatabase getDBInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBUtil(context);
        }
        return mInstance.getWritableDatabase();
    }

    @Override // com.mappn.sdk.uc.util.SDSQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE uc (_id INTEGER PRIMARY KEY AUTOINCREMENT ,name TEXT,token TEXT,uid LONG,pass TEXT);");
    }

    @Override // com.mappn.sdk.uc.util.SDSQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i != 1) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS uc");
            onCreate(sQLiteDatabase);
        }
    }
}
