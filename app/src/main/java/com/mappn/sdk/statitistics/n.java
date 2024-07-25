package com.mappn.sdk.statitistics;

import android.database.sqlite.SQLiteDatabase;

/* loaded from: classes.dex */
final class n implements k {
    public static final String[] a = {"_id", "error_time", "message", "repeat", "shorthashcode"};

    public static final void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE error_report (_id INTEGER PRIMARY KEY autoincrement,error_time LONG,message BLOB,repeat INTERGER,shorthashcode TEXT)");
    }
}
