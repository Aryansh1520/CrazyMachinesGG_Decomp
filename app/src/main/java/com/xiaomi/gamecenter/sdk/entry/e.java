package com.xiaomi.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class e implements Parcelable.Creator {
    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public CardBuyInfo createFromParcel(Parcel parcel) {
        CardBuyInfo cardBuyInfo = new CardBuyInfo(null);
        cardBuyInfo.setCardNumber(parcel.readString());
        cardBuyInfo.setCardPass(parcel.readString());
        cardBuyInfo.setCardType(CardType.valueOf(parcel.readString()));
        cardBuyInfo.setMoney(parcel.readInt());
        cardBuyInfo.setCpOrderId(parcel.readString());
        cardBuyInfo.setCpUserInfo(parcel.readString());
        return cardBuyInfo;
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public CardBuyInfo[] newArray(int i) {
        return new CardBuyInfo[i];
    }
}
