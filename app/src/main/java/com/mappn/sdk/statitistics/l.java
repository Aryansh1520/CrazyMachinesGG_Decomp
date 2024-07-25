package com.mappn.sdk.statitistics;

import android.database.sqlite.SQLiteDatabase;

/* loaded from: classes.dex */
final class l implements k {
    public static final String[] a = {"_id", "name", "start_time", "duration", "session_id", "refer", "realtime"};

    public static final void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE activity (_id INTEGER PRIMARY KEY autoincrement,name TEXT,start_time LONG,duration INTEGER,session_id TEXT,refer TEXT,realtime LONG)");
    }
}
