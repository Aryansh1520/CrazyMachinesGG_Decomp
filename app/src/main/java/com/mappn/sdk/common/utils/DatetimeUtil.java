package com.mappn.sdk.common.utils;

import com.mappn.sdk.pay.util.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* loaded from: classes.dex */
public class DatetimeUtil {
    public static long getLongFromString(String str) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str).getTime();
        } catch (ParseException e) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(str).getTime();
            } catch (Exception e2) {
                return 0L;
            }
        }
    }

    public static Long getMonthAgoTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(2, -1);
        return Long.valueOf(calendar.getTimeInMillis());
    }

    public static Long getTodayTimeInMillis(int i, int i2, int i3) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, i);
        calendar.set(13, i3);
        calendar.set(12, i2);
        calendar.set(14, 0);
        return Long.valueOf(calendar.getTimeInMillis());
    }

    public static Long getTomorrowTimeInMillis(int i, int i2, int i3) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(6, 1);
        calendar.set(11, i);
        calendar.set(13, i3);
        calendar.set(12, i2);
        calendar.set(14, 0);
        return Long.valueOf(calendar.getTimeInMillis());
    }

    public static Long getWeekAgoTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(3, -1);
        return Long.valueOf(calendar.getTimeInMillis());
    }

    public static Long getYestodayTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(6, -1);
        calendar.set(11, 0);
        calendar.set(13, 0);
        calendar.set(12, 0);
        calendar.set(14, 0);
        return Long.valueOf(calendar.getTimeInMillis());
    }

    public static int subtraction(Date date, Date date2) {
        if (date2 == null) {
            date2 = new Date();
        }
        if (date == null) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date2);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date);
        return ((((int) (calendar2.getTimeInMillis() - calendar.getTimeInMillis())) / Constants.PAYMENT_MAX) / 60) / 60;
    }
}
