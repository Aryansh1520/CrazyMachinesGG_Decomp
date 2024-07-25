package com.xiaomi.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class CardBuyInfoOffline implements Parcelable {
    public static final Parcelable.Creator CREATOR = new d();
    private String cardNumber;
    private String cardPass;
    private CardType cardType;
    private MiBuyInfoOffline infoOffline;
    private int money;

    private CardBuyInfoOffline() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ CardBuyInfoOffline(d dVar) {
        this();
    }

    public CardBuyInfoOffline(String str, String str2, CardType cardType, int i, MiBuyInfoOffline miBuyInfoOffline) {
        this.cardNumber = str;
        this.cardPass = str2;
        this.cardType = cardType;
        this.money = i;
        this.infoOffline = miBuyInfoOffline;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getCardPass() {
        return this.cardPass;
    }

    public CardType getCardType() {
        return this.cardType;
    }

    public MiBuyInfoOffline getInfoOffline() {
        return this.infoOffline;
    }

    public int getMoney() {
        return this.money;
    }

    public void setCardNumber(String str) {
        this.cardNumber = str;
    }

    public void setCardPass(String str) {
        this.cardPass = str;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setInfoOffline(MiBuyInfoOffline miBuyInfoOffline) {
        this.infoOffline = miBuyInfoOffline;
    }

    public void setMoney(int i) {
        this.money = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getCardNumber());
        parcel.writeString(getCardPass());
        parcel.writeString(getCardType().toString());
        parcel.writeInt(getMoney());
        parcel.writeParcelable(getInfoOffline(), i);
    }
}
