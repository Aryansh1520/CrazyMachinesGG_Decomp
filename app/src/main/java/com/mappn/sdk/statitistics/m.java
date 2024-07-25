package com.mappn.sdk.statitistics;

import android.database.sqlite.SQLiteDatabase;

/* loaded from: classes.dex */
final class m implements k {
    static {
        String[] strArr = {"_id", "event_id", "event_label", "session_id", "occurtime", "paramap"};
    }

    public static final void a(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE app_event (_id INTEGER PRIMARY KEY autoincrement,event_id TEXT,event_label TEXT,session_id TEXT,occurtime LONG,paramap BLOB)");
    }
}
