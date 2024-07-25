package com.mappn.sdk.pay.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* loaded from: classes.dex */
public class DBUtil extends SQLiteOpenHelper {
    public static final String DB_NAME = "gfanPay.db";
    public static final String PAY_ID = "_id";
    public static final String PAY_ORDERID = "orderId";
    public static final String PAY_PARMA = "payParameters";
    public static final String TABLE_PAY = "pay";
    public static SQLiteDatabase mDb;
    public static DBUtil mInstance;

    public DBUtil(Context context) {
        super(context, DB_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public static SQLiteDatabase getDBInstance(Context context) {
        if (mDb != null) {
            return mDb;
        }
        if (mInstance == null) {
            mInstance = new DBUtil(context);
        }
        if (mInstance == null) {
            return null;
        }
        return mInstance.getWritableDatabase();
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE pay (_id INTEGER PRIMARY KEY AUTOINCREMENT ,payParameters BLOBorderId TEXT);");
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i != 1) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS pay");
            onCreate(sQLiteDatabase);
        }
    }
}
