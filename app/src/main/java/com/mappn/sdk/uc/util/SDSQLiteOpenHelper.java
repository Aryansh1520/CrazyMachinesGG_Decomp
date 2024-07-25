package com.mappn.sdk.uc.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;
import java.io.File;

/* loaded from: classes.dex */
public abstract class SDSQLiteOpenHelper {
    private static final String a = SDSQLiteOpenHelper.class.getSimpleName();
    private final String b;
    private final SQLiteDatabase.CursorFactory c;
    private final int d;
    private SQLiteDatabase e = null;
    private boolean f = false;

    public SDSQLiteOpenHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Version must be >= 1, was " + i);
        }
        this.b = str;
        this.c = cursorFactory;
        this.d = i;
    }

    public synchronized void close() {
        if (this.f) {
            throw new IllegalStateException("Closed during initialization");
        }
        if (this.e != null && this.e.isOpen()) {
            this.e.close();
            this.e = null;
        }
    }

    public File getDatabasePath(String str) {
        String str2 = null;
        if (Environment.getExternalStorageState().equals("mounted")) {
            str2 = Constants.DATA_FOLDER;
            File file = new File(str2);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return new File(str2 + str);
    }

    public synchronized SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase sQLiteDatabase;
        if (this.e != null && this.e.isOpen()) {
            sQLiteDatabase = this.e;
        } else {
            if (this.f) {
                throw new IllegalStateException("getReadableDatabase called recursively");
            }
            try {
                sQLiteDatabase = getWritableDatabase();
            } catch (SQLiteException e) {
                if (this.b == null) {
                    throw e;
                }
                Log.e(a, "Couldn't open " + this.b + " for writing (will try read-only):", e);
                SQLiteDatabase sQLiteDatabase2 = null;
                try {
                    this.f = true;
                    String path = getDatabasePath(this.b).getPath();
                    SQLiteDatabase openDatabase = SQLiteDatabase.openDatabase(path, this.c, 0);
                    if (openDatabase.getVersion() != this.d) {
                        throw new SQLiteException("Can't upgrade read-only database from version " + openDatabase.getVersion() + " to " + this.d + ": " + path);
                    }
                    onOpen(openDatabase);
                    Log.w(a, "Opened " + this.b + " in read-only mode");
                    this.e = openDatabase;
                    sQLiteDatabase = this.e;
                    this.f = false;
                    if (openDatabase != null && openDatabase != this.e) {
                        openDatabase.close();
                    }
                } catch (Throwable th) {
                    this.f = false;
                    if (0 != 0 && null != this.e) {
                        sQLiteDatabase2.close();
                    }
                    throw th;
                }
            }
        }
        return sQLiteDatabase;
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase sQLiteDatabase;
        SQLiteDatabase sQLiteDatabase2 = null;
        synchronized (this) {
            if (this.e != null && this.e.isOpen() && !this.e.isReadOnly()) {
                sQLiteDatabase = this.e;
            } else {
                if (this.f) {
                    throw new IllegalStateException("getWritableDatabase called recursively");
                }
                try {
                    this.f = true;
                    sQLiteDatabase = this.b == null ? SQLiteDatabase.create(null) : SQLiteDatabase.openOrCreateDatabase(getDatabasePath(this.b).getPath(), this.c);
                } catch (SQLiteException e) {
                    sQLiteDatabase = null;
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    int version = sQLiteDatabase.getVersion();
                    if (version != this.d) {
                        sQLiteDatabase.beginTransaction();
                        try {
                            if (version == 0) {
                                onCreate(sQLiteDatabase);
                            } else {
                                onUpgrade(sQLiteDatabase, version, this.d);
                            }
                            sQLiteDatabase.setVersion(this.d);
                            sQLiteDatabase.setTransactionSuccessful();
                        } finally {
                            sQLiteDatabase.endTransaction();
                        }
                    }
                    onOpen(sQLiteDatabase);
                    this.f = false;
                    if (this.e != null) {
                        try {
                            this.e.close();
                        } catch (Exception e2) {
                        }
                    }
                    this.e = sQLiteDatabase;
                } catch (SQLiteException e3) {
                    this.f = false;
                    if (sQLiteDatabase != null) {
                        sQLiteDatabase.close();
                    }
                    sQLiteDatabase = null;
                    return sQLiteDatabase;
                } catch (Throwable th2) {
                    sQLiteDatabase2 = sQLiteDatabase;
                    th = th2;
                    this.f = false;
                    if (sQLiteDatabase2 != null) {
                        sQLiteDatabase2.close();
                    }
                    throw th;
                }
            }
        }
        return sQLiteDatabase;
    }

    public abstract void onCreate(SQLiteDatabase sQLiteDatabase);

    public void onOpen(SQLiteDatabase sQLiteDatabase) {
    }

    public abstract void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);
}
