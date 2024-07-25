package com.xiaomi.gamecenter.sdk.ui;

import android.os.Parcel;
import android.os.Parcelable;
import com.xiaomi.gamecenter.sdk.ui.ActionTransfor;

/* loaded from: classes.dex */
final class a implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public ActionTransfor.DataAction createFromParcel(Parcel parcel) {
        ActionTransfor.DataAction dataAction = new ActionTransfor.DataAction();
        dataAction.a = parcel.readString();
        dataAction.b = ActionTransfor.ActionResult.valueOf(parcel.readString());
        dataAction.c = parcel.readBundle();
        dataAction.d = parcel.readInt();
        return dataAction;
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public ActionTransfor.DataAction[] newArray(int i) {
        return new ActionTransfor.DataAction[i];
    }
}
