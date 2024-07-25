package com.mappn.sdk.uc.activity;

import android.view.View;
import com.mappn.sdk.uc.util.Constants;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes.dex */
final class d implements View.OnClickListener {
    private /* synthetic */ int a;
    private /* synthetic */ c b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d(c cVar, int i) {
        this.b = cVar;
        this.a = i;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        ArrayList arrayList;
        ArrayList arrayList2;
        ChooseAccountActivity chooseAccountActivity = this.b.a;
        arrayList = this.b.a.f;
        chooseAccountActivity.h = ((Long) ((HashMap) arrayList.get(this.a)).get(Constants.ACCOUNT_UID)).longValue();
        ChooseAccountActivity chooseAccountActivity2 = this.b.a;
        arrayList2 = this.b.a.f;
        chooseAccountActivity2.j = (String) ((HashMap) arrayList2.get(this.a)).get(Constants.ACCOUNT_NAME);
        this.b.a.a(3);
    }
}
