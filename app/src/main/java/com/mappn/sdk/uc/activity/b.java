package com.mappn.sdk.uc.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import com.mappn.sdk.uc.UserDao;
import java.util.ArrayList;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class b implements DialogInterface.OnClickListener {
    private /* synthetic */ ChooseAccountActivity a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(ChooseAccountActivity chooseAccountActivity) {
        this.a = chooseAccountActivity;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public final void onClick(DialogInterface dialogInterface, int i) {
        ArrayList arrayList;
        c cVar;
        long j;
        try {
            Context applicationContext = this.a.getApplicationContext();
            j = this.a.h;
            UserDao.deleteUserByUid(applicationContext, j);
        } catch (SQLiteException e) {
        } finally {
            arrayList = this.a.f;
            arrayList.clear();
            this.a.a();
            cVar = this.a.e;
            cVar.notifyDataSetChanged();
        }
    }
}
