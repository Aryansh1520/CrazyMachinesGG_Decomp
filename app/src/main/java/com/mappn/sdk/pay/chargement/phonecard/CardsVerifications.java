package com.mappn.sdk.pay.chargement.phonecard;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class CardsVerifications {
    public ArrayList cards = new ArrayList();
    public int version;

    public String[] getCardNames() {
        if (this.cards == null || this.cards.size() <= 0) {
            return null;
        }
        int size = this.cards.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = ((CardsVerification) this.cards.get(i)).name;
        }
        return strArr;
    }
}
