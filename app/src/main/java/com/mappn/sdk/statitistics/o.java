package com.mappn.sdk.statitistics;

import android.database.sqlite.SQLiteDatabase;

/* loaded from: classes.dex */
final class o implements k {
    public static final String[] a = {"_id", "session_id", "start_time", "duration", "is_launch", "interval", "is_connected"};

    public static final void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE session (_id INTEGER PRIMARY KEY autoincrement,session_id TEXT,start_time LONG,duration INTEGER,is_launch INTEGER,interval LONG, is_connected INTEGER)");
    }
}
