package com.mappn.sdk.statitistics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mappn.sdk.statitistics.entity.GfanPayAppEvent;
import com.mappn.sdk.statitistics.entity.GfanPayAppException;
import com.mappn.sdk.statitistics.entity.GfanPaySession;
import com.mappn.sdk.statitistics.entity.GfanPayTMessage;
import com.mokredit.payment.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class j {
    private static SQLiteDatabase a;
    private static int b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long a(long j, String str) {
        long j2 = 0;
        String[] strArr = {"Save Error", "errorTime:" + j, "data:" + str};
        GfanPayTCLog.a();
        ContentValues contentValues = new ContentValues();
        contentValues.put("error_time", Long.valueOf(j));
        try {
            GfanPayAppException gfanPayAppException = new GfanPayAppException();
            StringBuffer stringBuffer = new StringBuffer(StringUtils.EMPTY);
            long a2 = a(str, gfanPayAppException, stringBuffer);
            if (0 == a2) {
                contentValues.put("message", str.getBytes("UTF-8"));
                contentValues.put("repeat", (Integer) 1);
                contentValues.put("shorthashcode", stringBuffer.toString());
                j2 = a.insert("error_report", null, contentValues);
            } else {
                contentValues.put("repeat", Integer.valueOf(gfanPayAppException.mRepeat + 1));
                j2 = a.update("error_report", contentValues, "_id=?", new String[]{String.valueOf(a2)});
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return j2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long a(String str, long j, long j2, int i) {
        try {
            String[] strArr = {"Save Session", "sessionId:" + str, "startTime:" + j};
            GfanPayTCLog.a();
            ContentValues contentValues = new ContentValues();
            contentValues.put("session_id", str);
            contentValues.put("start_time", Long.valueOf(j));
            contentValues.put("duration", (Integer) 0);
            contentValues.put("is_launch", (Integer) 0);
            contentValues.put("interval", Long.valueOf(j2));
            contentValues.put("is_connected", Integer.valueOf(i));
            a.insert("session", null, contentValues);
            return 0L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static long a(java.lang.String r10, com.mappn.sdk.statitistics.entity.GfanPayAppException r11, java.lang.StringBuffer r12) {
        /*
            Method dump skipped, instructions count: 253
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.statitistics.j.a(java.lang.String, com.mappn.sdk.statitistics.entity.GfanPayAppException, java.lang.StringBuffer):long");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long a(String str, String str2, long j, int i, String str3, long j2) {
        try {
            String[] strArr = {"Save Activity", "sessionId:" + str, "name:" + str2, "start:" + j, "duration:0", "refer:" + str3};
            GfanPayTCLog.a();
            ContentValues contentValues = new ContentValues();
            contentValues.put("session_id", str);
            contentValues.put("name", str2);
            contentValues.put("start_time", Long.valueOf(j));
            contentValues.put("duration", (Integer) 0);
            contentValues.put("refer", str3);
            contentValues.put("realtime", Long.valueOf(j2));
            long insert = a.insert("activity", null, contentValues);
            String[] strArr2 = {"ActEvent", "Save activity: " + insert + " sessionId:" + str + " name:" + str2};
            GfanPayTCLog.a();
            return insert;
        } catch (Exception e) {
            e.printStackTrace();
            return -1L;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long a(List list) {
        long j;
        Exception e;
        Cursor cursor;
        int size = list.size();
        if (size == 0) {
            return 0L;
        }
        long j2 = 0;
        for (int i = size - 1; i >= 0; i--) {
            try {
                try {
                    Cursor rawQuery = a.rawQuery("SELECT MAX(_id) from activity where duration != 0 and session_id =?", new String[]{((GfanPaySession) list.get(i)).id});
                    try {
                        if (rawQuery.moveToFirst()) {
                            j = rawQuery.getLong(0);
                            if (j != 0) {
                                if (rawQuery == null) {
                                    return j;
                                }
                                try {
                                    rawQuery.close();
                                    return j;
                                } catch (Exception e2) {
                                    e = e2;
                                    e.printStackTrace();
                                    return j;
                                }
                            }
                            j2 = j;
                        }
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor = rawQuery;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = null;
                }
            } catch (Exception e3) {
                j = j2;
                e = e3;
            }
        }
        return j2;
    }

    private static Map a(byte[] bArr) {
        DataInputStream dataInputStream;
        ByteArrayInputStream byteArrayInputStream;
        Object readUTF;
        if (bArr == null) {
            return null;
        }
        try {
            if (bArr.length == 0) {
                return null;
            }
            try {
                HashMap hashMap = new HashMap();
                byteArrayInputStream = new ByteArrayInputStream(bArr);
                try {
                    dataInputStream = new DataInputStream(byteArrayInputStream);
                    try {
                        int readInt = dataInputStream.readInt();
                        for (int i = 0; i < readInt; i++) {
                            String readUTF2 = dataInputStream.readUTF();
                            int readInt2 = dataInputStream.readInt();
                            if (readInt2 == 66) {
                                readUTF = Double.valueOf(dataInputStream.readDouble());
                            } else {
                                if (readInt2 != 88) {
                                    GfanPayAgent.a(byteArrayInputStream);
                                    GfanPayAgent.a(dataInputStream);
                                    return null;
                                }
                                readUTF = dataInputStream.readUTF();
                            }
                            hashMap.put(readUTF2, readUTF);
                        }
                        GfanPayAgent.a(byteArrayInputStream);
                        GfanPayAgent.a(dataInputStream);
                        return hashMap;
                    } catch (Throwable th) {
                        th = th;
                        GfanPayAgent.a(byteArrayInputStream);
                        GfanPayAgent.a(dataInputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    dataInputStream = null;
                }
            } catch (Throwable th3) {
                th = th3;
                dataInputStream = null;
                byteArrayInputStream = null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void a() {
        synchronized (j.class) {
            b--;
            int max = Math.max(0, b);
            b = max;
            if (max == 0 && a != null) {
                a.close();
                a = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(long j) {
        String[] strArr = {"Delete Activity Less Than Id", "id:" + j};
        GfanPayTCLog.a();
        a.delete("activity", "_id<=? AND duration != 0 ", new String[]{String.valueOf(j)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(long j, long j2) {
        long e = (j2 - e(j)) / 1000;
        String[] strArr = {"Update Activity Duration", "id:" + j, "duration:" + e};
        GfanPayTCLog.a();
        ContentValues contentValues = new ContentValues();
        contentValues.put("duration", Long.valueOf(e));
        try {
            a.update("activity", contentValues, "_id=?", new String[]{String.valueOf(j)});
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void a(Context context) {
        synchronized (j.class) {
            if (a == null) {
                File file = new File(context.getFilesDir(), "gfagent.db");
                boolean exists = file.exists();
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                SQLiteDatabase openOrCreateDatabase = SQLiteDatabase.openOrCreateDatabase(file, (SQLiteDatabase.CursorFactory) null);
                a = openOrCreateDatabase;
                openOrCreateDatabase.setLockingEnabled(true);
                a.setMaximumSize(1000000L);
                b = 1;
                if (!exists) {
                    c();
                } else if (4 > a.getVersion()) {
                    a.execSQL("DROP TABLE IF EXISTS error_report");
                    a.execSQL("DROP TABLE IF EXISTS app_event");
                    a.execSQL("DROP TABLE IF EXISTS session");
                    a.execSQL("DROP TABLE IF EXISTS activity");
                    c();
                }
            } else {
                b++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_launch", (Integer) 1);
        a.update("session", contentValues, "session_id=?", new String[]{str});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(String str, int i) {
        String[] strArr = {"Update Session Duration", "sessionId:" + str, "duration:" + i};
        GfanPayTCLog.a();
        ContentValues contentValues = new ContentValues();
        contentValues.put("duration", Integer.valueOf(i));
        try {
            a.update("session", contentValues, "session_id=?", new String[]{str});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean a(String str, String str2, String str3, long j, Map map) {
        try {
            String[] strArr = {"Save App Event", "sessionId:" + str, "event:" + str2, "label:" + str3};
            GfanPayTCLog.a();
            ContentValues contentValues = new ContentValues();
            contentValues.put("event_id", str2);
            contentValues.put("event_label", str3);
            contentValues.put("session_id", str);
            contentValues.put("occurtime", Long.valueOf(j));
            contentValues.put("paramap", a(map));
            return a.insert("app_event", null, contentValues) != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static byte[] a(Map map) {
        DataOutputStream dataOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        if (map == null || map.size() == 0) {
            return null;
        }
        HashMap hashMap = new HashMap();
        for (String str : map.keySet()) {
            if ((map.get(str) instanceof Number) || (map.get(str) instanceof String)) {
                if (!map.get(str).toString().trim().equals(StringUtils.EMPTY)) {
                    hashMap.put(str, map.get(str));
                }
            }
        }
        int size = hashMap.size();
        if (size > 10) {
            size = 10;
        }
        try {
            try {
                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                try {
                    DataOutputStream dataOutputStream2 = new DataOutputStream(byteArrayOutputStream2);
                    try {
                        dataOutputStream2.writeInt(size);
                        int i = 0;
                        for (Map.Entry entry : hashMap.entrySet()) {
                            dataOutputStream2.writeUTF((String) entry.getKey());
                            Object value = entry.getValue();
                            if (value instanceof Number) {
                                dataOutputStream2.writeInt(66);
                                dataOutputStream2.writeDouble(((Number) value).doubleValue());
                            } else {
                                if (!(value instanceof String)) {
                                    GfanPayAgent.a(byteArrayOutputStream2);
                                    GfanPayAgent.a(dataOutputStream2);
                                    return null;
                                }
                                dataOutputStream2.writeInt(88);
                                dataOutputStream2.writeUTF((String) value);
                            }
                            int i2 = i + 1;
                            if (i2 == 10) {
                                break;
                            }
                            i = i2;
                        }
                        byte[] byteArray = byteArrayOutputStream2.toByteArray();
                        GfanPayAgent.a(byteArrayOutputStream2);
                        GfanPayAgent.a(dataOutputStream2);
                        return byteArray;
                    } catch (Throwable th) {
                        th = th;
                        dataOutputStream = dataOutputStream2;
                        byteArrayOutputStream = byteArrayOutputStream2;
                        GfanPayAgent.a(byteArrayOutputStream);
                        GfanPayAgent.a(dataOutputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    dataOutputStream = null;
                    byteArrayOutputStream = byteArrayOutputStream2;
                }
            } catch (Throwable th3) {
                th = th3;
                dataOutputStream = null;
                byteArrayOutputStream = null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long b(List list) {
        Cursor rawQuery;
        int size = list.size();
        if (size == 0) {
            return 0L;
        }
        for (int i = size - 1; i >= 0; i--) {
            try {
                Cursor cursor = null;
                try {
                    rawQuery = a.rawQuery("SELECT MAX(_id) from app_event where session_id =?", new String[]{((GfanPaySession) list.get(i)).id});
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    if (rawQuery.moveToFirst()) {
                        long j = rawQuery.getLong(0);
                        if (j != 0) {
                            if (rawQuery == null) {
                                return j;
                            }
                            rawQuery.close();
                            return j;
                        }
                    }
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = rawQuery;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0L;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00b4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List b() {
        /*
            r11 = 3
            r12 = 2
            r10 = 1
            r9 = 0
            java.util.ArrayList r13 = new java.util.ArrayList
            r13.<init>()
            android.database.sqlite.SQLiteDatabase r0 = com.mappn.sdk.statitistics.j.a     // Catch: java.lang.Throwable -> Lb0 java.lang.Exception -> Lba
            java.lang.String r1 = "session"
            java.lang.String[] r2 = com.mappn.sdk.statitistics.o.a     // Catch: java.lang.Throwable -> Lb0 java.lang.Exception -> Lba
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "_id"
            r8 = 10
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch: java.lang.Throwable -> Lb0 java.lang.Exception -> Lba
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> Lb0 java.lang.Exception -> Lba
            boolean r0 = r1.moveToFirst()     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            if (r0 == 0) goto Laa
        L25:
            boolean r0 = r1.isAfterLast()     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            if (r0 != 0) goto Laa
            com.mappn.sdk.statitistics.entity.GfanPaySession r3 = new com.mappn.sdk.statitistics.entity.GfanPaySession     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r3.<init>()     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r0 = 1
            java.lang.String r0 = r1.getString(r0)     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r3.id = r0     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r0 = 2
            long r4 = r1.getLong(r0)     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r3.start = r4     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r0 = 2
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r2 = 0
            java.lang.String r4 = "Test"
            r0[r2] = r4     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r2 = 1
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            java.lang.String r5 = "querySessions session Start: "
            r4.<init>(r5)     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            long r5 = r3.start     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r0[r2] = r4     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            com.mappn.sdk.statitistics.GfanPayTCLog.a()     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r0 = 3
            int r0 = r1.getInt(r0)     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r3.duration = r0     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r0 = 4
            int r0 = r1.getInt(r0)     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            if (r0 != 0) goto La0
            r0 = r10
            r2 = r3
        L6d:
            r2.mStatus = r0     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            int r0 = r3.mStatus     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            if (r10 != r0) goto L88
            r0 = 5
            long r4 = r1.getLong(r0)     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            int r0 = (int) r4     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r3.mLastSessionInterval = r0     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            int r0 = r3.mLastSessionInterval     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            if (r0 >= 0) goto L82
            r0 = 0
            r3.mLastSessionInterval = r0     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
        L82:
            int r0 = r3.mLastSessionInterval     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            int r0 = r0 / 1000
            r3.duration = r0     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
        L88:
            r0 = 6
            int r0 = r1.getInt(r0)     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r3.isConnected = r0     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r13.add(r3)     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            r1.moveToNext()     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            goto L25
        L96:
            r0 = move-exception
        L97:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lb8
            if (r1 == 0) goto L9f
            r1.close()
        L9f:
            return r13
        La0:
            int r0 = r3.duration     // Catch: java.lang.Exception -> L96 java.lang.Throwable -> Lb8
            if (r0 == 0) goto La7
            r0 = r11
            r2 = r3
            goto L6d
        La7:
            r0 = r12
            r2 = r3
            goto L6d
        Laa:
            if (r1 == 0) goto L9f
            r1.close()
            goto L9f
        Lb0:
            r0 = move-exception
            r1 = r9
        Lb2:
            if (r1 == 0) goto Lb7
            r1.close()
        Lb7:
            throw r0
        Lb8:
            r0 = move-exception
            goto Lb2
        Lba:
            r0 = move-exception
            r1 = r9
            goto L97
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.statitistics.j.b():java.util.List");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(long j) {
        String[] strArr = {"Delete App Event Less Than Id", "id:" + j};
        GfanPayTCLog.a();
        a.delete("app_event", "_id<=? ", new String[]{String.valueOf(j)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(String str) {
        String[] strArr = {"Delete Session By Session Id", "sessionId:" + str};
        GfanPayTCLog.a();
        a.delete("session", "session_id=?", new String[]{str});
    }

    private static void c() {
        a.setVersion(4);
        o.a(a);
        l.a(a);
        m.a(a);
        n.a(a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(long j) {
        String[] strArr = {"Delete Error Less Than Id", "id:" + j};
        GfanPayTCLog.a();
        a.delete("error_report", "_id<=?", new String[]{String.valueOf(j)});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(String str) {
        String[] strArr = {"Delete Activity By SessionID", "sessionId:" + str};
        GfanPayTCLog.a();
        a.delete("activity", "session_id=? ", new String[]{str});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List d(long j) {
        ArrayList arrayList = new ArrayList();
        Cursor cursor = null;
        try {
            try {
                cursor = a.rawQuery("SELECT error_time,message,repeat, shorthashcode from error_report where _id<=?", new String[]{String.valueOf(j)});
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        GfanPayTMessage gfanPayTMessage = new GfanPayTMessage();
                        gfanPayTMessage.mMsgType = 3;
                        GfanPayAppException gfanPayAppException = new GfanPayAppException();
                        gfanPayAppException.mErrorTime = cursor.getLong(0);
                        gfanPayAppException.data = cursor.getBlob(1);
                        gfanPayAppException.mRepeat = cursor.getInt(2);
                        gfanPayAppException.mShortHashCode = cursor.getString(3);
                        gfanPayAppException.mAppVersionCode = r.f(GfanPayAgent.getContext());
                        gfanPayTMessage.mAppException = gfanPayAppException;
                        arrayList.add(gfanPayTMessage);
                        cursor.moveToNext();
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
            }
            return arrayList;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0064  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.List d(java.lang.String r10) {
        /*
            r8 = 0
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            android.database.sqlite.SQLiteDatabase r0 = com.mappn.sdk.statitistics.j.a     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L6a
            java.lang.String r1 = "activity"
            java.lang.String[] r2 = com.mappn.sdk.statitistics.l.a     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L6a
            java.lang.String r3 = "session_id=? AND duration != 0 "
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L6a
            r5 = 0
            r4[r5] = r10     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L6a
            r5 = 0
            r6 = 0
            java.lang.String r7 = "_id"
            android.database.Cursor r1 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L6a
            boolean r0 = r1.moveToFirst()     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            if (r0 == 0) goto L5a
        L22:
            boolean r0 = r1.isAfterLast()     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            if (r0 != 0) goto L5a
            com.mappn.sdk.statitistics.entity.GfanPayActivity r0 = new com.mappn.sdk.statitistics.entity.GfanPayActivity     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r0.<init>()     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r2 = 1
            java.lang.String r2 = r1.getString(r2)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r0.name = r2     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r2 = 2
            long r2 = r1.getLong(r2)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r0.start = r2     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r2 = 3
            int r2 = r1.getInt(r2)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r0.duration = r2     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r2 = 5
            java.lang.String r2 = r1.getString(r2)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r0.refer = r2     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r9.add(r0)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            r1.moveToNext()     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> L68
            goto L22
        L50:
            r0 = move-exception
        L51:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L68
            if (r1 == 0) goto L59
            r1.close()
        L59:
            return r9
        L5a:
            if (r1 == 0) goto L59
            r1.close()
            goto L59
        L60:
            r0 = move-exception
            r1 = r8
        L62:
            if (r1 == 0) goto L67
            r1.close()
        L67:
            throw r0
        L68:
            r0 = move-exception
            goto L62
        L6a:
            r0 = move-exception
            r1 = r8
            goto L51
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.statitistics.j.d(java.lang.String):java.util.List");
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static long e(long r9) {
        /*
            r8 = 0
            android.database.sqlite.SQLiteDatabase r0 = com.mappn.sdk.statitistics.j.a     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L45
            java.lang.String r1 = "activity"
            java.lang.String[] r2 = com.mappn.sdk.statitistics.l.a     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L45
            java.lang.String r3 = "_id=?"
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L45
            r5 = 0
            java.lang.String r6 = java.lang.String.valueOf(r9)     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L45
            r4[r5] = r6     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L45
            r5 = 0
            r6 = 0
            java.lang.String r7 = "_id"
            android.database.Cursor r2 = r0.query(r1, r2, r3, r4, r5, r6, r7)     // Catch: java.lang.Exception -> L3a java.lang.Throwable -> L45
            boolean r0 = r2.moveToFirst()     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L52
            if (r0 == 0) goto L32
            boolean r0 = r2.isAfterLast()     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L52
            if (r0 != 0) goto L32
            r0 = 6
            long r0 = r2.getLong(r0)     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L52
            if (r2 == 0) goto L31
            r2.close()
        L31:
            return r0
        L32:
            if (r2 == 0) goto L37
            r2.close()
        L37:
            r0 = 0
            goto L31
        L3a:
            r0 = move-exception
            r1 = r8
        L3c:
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L4f
            if (r1 == 0) goto L37
            r1.close()
            goto L37
        L45:
            r0 = move-exception
        L46:
            if (r8 == 0) goto L4b
            r8.close()
        L4b:
            throw r0
        L4c:
            r0 = move-exception
            r8 = r2
            goto L46
        L4f:
            r0 = move-exception
            r8 = r1
            goto L46
        L52:
            r0 = move-exception
            r1 = r2
            goto L3c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mappn.sdk.statitistics.j.e(long):long");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void e(String str) {
        String[] strArr = {"Delete App Event By SessionId", "sessionId:" + str};
        GfanPayTCLog.a();
        a.delete("app_event", "session_id=? ", new String[]{str});
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static List f(String str) {
        Cursor cursor = null;
        ArrayList arrayList = new ArrayList();
        try {
            try {
                cursor = a.rawQuery("SELECT COUNT(_id), event_id, event_label, occurtime, paramap from app_event where session_id = ? group by event_id, event_label, paramap", new String[]{str});
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        GfanPayAppEvent gfanPayAppEvent = new GfanPayAppEvent();
                        gfanPayAppEvent.count = cursor.getInt(0);
                        gfanPayAppEvent.id = cursor.getString(1);
                        gfanPayAppEvent.label = cursor.getString(2);
                        gfanPayAppEvent.startTime = cursor.getLong(3);
                        gfanPayAppEvent.parameters = null;
                        try {
                            gfanPayAppEvent.parameters = a(cursor.getBlob(4));
                        } catch (Exception e) {
                        }
                        arrayList.add(gfanPayAppEvent);
                        cursor.moveToNext();
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long g(String str) {
        Cursor rawQuery;
        Cursor cursor = null;
        try {
            try {
                rawQuery = a.rawQuery("SELECT MAX(_id) from " + str, null);
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e) {
            e = e;
        }
        try {
        } catch (Exception e2) {
            e = e2;
            cursor = rawQuery;
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            return 0L;
        } catch (Throwable th2) {
            th = th2;
            cursor = rawQuery;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        if (!rawQuery.moveToFirst() || rawQuery.isAfterLast()) {
            if (rawQuery != null) {
                rawQuery.close();
            }
            return 0L;
        }
        long j = rawQuery.getLong(0);
        if (rawQuery == null) {
            return j;
        }
        rawQuery.close();
        return j;
    }
}
