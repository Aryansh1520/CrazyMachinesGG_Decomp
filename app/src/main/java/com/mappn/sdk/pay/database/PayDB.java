package com.mappn.sdk.pay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mappn.sdk.common.utils.BaseUtils;
import com.mappn.sdk.pay.util.DBUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.Map;

/* loaded from: classes.dex */
public class PayDB {
    private static String a = "PayDB";

    private static ContentValues a(PayVo payVo) {
        ContentValues contentValues = new ContentValues();
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ObjectOutputStream(byteArrayOutputStream).writeObject(payVo.getPayParameters());
            contentValues.put(DBUtil.PAY_PARMA, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            BaseUtils.D(a, "error occurs when get ContentValues from RuleVo", e);
        }
        return contentValues;
    }

    private static PayVo a(Cursor cursor) {
        Map map;
        PayVo payVo = new PayVo();
        payVo.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
        try {
            map = (Map) new ObjectInputStream(new ByteArrayInputStream(cursor.getBlob(cursor.getColumnIndex(DBUtil.PAY_PARMA)))).readObject();
        } catch (OptionalDataException e) {
            BaseUtils.D(a, "error occurs when get RuleVo from Cursor", e);
            map = null;
        } catch (StreamCorruptedException e2) {
            BaseUtils.D(a, "error occurs when get RuleVo from Cursor", e2);
            map = null;
        } catch (IOException e3) {
            BaseUtils.D(a, "error occurs when get RuleVo from Cursor", e3);
            map = null;
        } catch (ClassNotFoundException e4) {
            BaseUtils.D(a, "error occurs when get RuleVo from Cursor", e4);
            map = null;
        }
        payVo.setPayParameters(map);
        return payVo;
    }

    public static void delPayVo(Context context) {
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        dBInstance.delete(DBUtil.TABLE_PAY, null, null);
        dBInstance.close();
    }

    public static void insertPayVo(Context context, PayVo payVo) {
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        dBInstance.insert(DBUtil.TABLE_PAY, null, a(payVo));
        BaseUtils.D(a, "已插入数据");
        dBInstance.close();
    }

    public static PayVo queryPayVo(Context context) {
        SQLiteDatabase dBInstance = DBUtil.getDBInstance(context);
        Cursor query = dBInstance.query(DBUtil.TABLE_PAY, null, null, null, null, null, null);
        if (query != null) {
            r2 = query.moveToNext() ? a(query) : null;
            query.close();
        }
        if (r2 != null) {
            BaseUtils.D(a, "已查询数据");
        }
        dBInstance.close();
        return r2;
    }
}
