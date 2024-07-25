package com.mokredit.payment;

import android.app.Activity;
import android.os.Bundle;

/* loaded from: classes.dex */
public class MktPayment extends Activity {
    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().clearFlags(1024);
        setContentView(new MktLineLyt(this));
    }
}
