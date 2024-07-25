package com.xiaomi.gamecenter.sdk.entry;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class CardBuyInfo implements Parcelable {
    public static final Parcelable.Creator CREATOR = new e();
    private String cardNumber;
    private String cardPass;
    private CardType cardType;
    private String cpOrderId;
    private String cpUserInfo;
    private int money;

    private CardBuyInfo() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ CardBuyInfo(e eVar) {
        this();
    }

    public CardBuyInfo(String str, String str2, String str3, String str4, CardType cardType, int i) {
        this.cardNumber = str;
        this.cardPass = str2;
        this.cpOrderId = str3;
        this.cardType = cardType;
        this.money = i;
        this.cpUserInfo = str4;
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

    public String getCpOrderId() {
        return this.cpOrderId;
    }

    public String getCpUserInfo() {
        return this.cpUserInfo;
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

    public void setCpOrderId(String str) {
        this.cpOrderId = str;
    }

    public void setCpUserInfo(String str) {
        this.cpUserInfo = str;
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
        parcel.writeString(getCpOrderId());
        parcel.writeString(getCpUserInfo());
    }
}
