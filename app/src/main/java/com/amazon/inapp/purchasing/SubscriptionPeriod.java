package com.amazon.inapp.purchasing;

import java.util.Date;

/* loaded from: classes.dex */
public final class SubscriptionPeriod {
    private static final String TO_STRING_FORMAT = "(%s, startDate: \"%s\", endDate: \"%s\")";
    final Date _endDate;
    final Date _startDate;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SubscriptionPeriod(Date date, Date date2) {
        this._startDate = date;
        this._endDate = date2;
    }

    public Date getEndDate() {
        return this._endDate;
    }

    public Date getStartDate() {
        return this._startDate;
    }

    public String toString() {
        return String.format(TO_STRING_FORMAT, super.toString(), this._startDate, this._endDate);
    }
}
