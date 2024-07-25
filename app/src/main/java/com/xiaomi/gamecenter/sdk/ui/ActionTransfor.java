package com.xiaomi.gamecenter.sdk.ui;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.UUID;

/* loaded from: classes.dex */
public class ActionTransfor {

    /* loaded from: classes.dex */
    public enum ActionResult {
        ACTION_FAIL,
        ACTION_OK,
        ACTION_CANCEL,
        ACTION_NONE
    }

    /* loaded from: classes.dex */
    public class DataAction implements Parcelable {
        public static final Parcelable.Creator CREATOR = new a();
        public ActionResult b;
        public Bundle c = new Bundle();
        public String a = UUID.randomUUID().toString();
        public int d = 0;

        public DataAction() {
            this.b = ActionResult.ACTION_NONE;
            this.b = ActionResult.ACTION_NONE;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.a);
            parcel.writeString(this.b.toString());
            parcel.writeBundle(this.c);
            parcel.writeInt(this.d);
        }
    }
}
