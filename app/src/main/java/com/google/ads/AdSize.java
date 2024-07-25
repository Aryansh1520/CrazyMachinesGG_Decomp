package com.google.ads;

import android.content.Context;
import android.util.TypedValue;
import com.mokredit.payment.StringUtils;
import com.vivamedia.CMTablet.HttpRequest;

/* loaded from: classes.dex */
public class AdSize {
    public static final int AUTO_HEIGHT = -2;
    public static final int FULL_WIDTH = -1;
    public static final int LANDSCAPE_AD_HEIGHT = 32;
    public static final int LARGE_AD_HEIGHT = 90;
    public static final int PORTRAIT_AD_HEIGHT = 50;
    private int a;
    private int b;
    private boolean c;
    private boolean d;
    private boolean e;
    private String f;
    public static final AdSize SMART_BANNER = new AdSize(-1, -2, "mb");
    public static final AdSize BANNER = new AdSize(320, 50, "mb");
    public static final AdSize IAB_MRECT = new AdSize(HttpRequest.HTTPERROR_CLIENTPROTOCOL_EXCEPTION, 250, "as");
    public static final AdSize IAB_BANNER = new AdSize(468, 60, "as");
    public static final AdSize IAB_LEADERBOARD = new AdSize(728, 90, "as");
    public static final AdSize IAB_WIDE_SKYSCRAPER = new AdSize(160, 600, "as");

    public AdSize(int width, int height) {
        this(width, height, null);
        if (a()) {
            this.e = false;
            this.f = "mb";
        } else {
            this.e = true;
        }
    }

    private AdSize(int width, int height, String type) {
        this.a = width;
        this.b = height;
        this.f = type;
        this.c = width == -1;
        this.d = height == -2;
        this.e = false;
    }

    public static AdSize createAdSize(AdSize adSize, Context context) {
        if (context == null || !adSize.a()) {
            return adSize.a() ? BANNER : adSize;
        }
        AdSize adSize2 = new AdSize(adSize.c ? a(context) : adSize.getWidth(), adSize.d ? b(context) : adSize.getHeight(), adSize.f);
        adSize2.d = adSize.d;
        adSize2.c = adSize.c;
        adSize2.e = adSize.e;
        return adSize2;
    }

    public boolean equals(Object other) {
        if (!(other instanceof AdSize)) {
            return false;
        }
        AdSize adSize = (AdSize) other;
        return this.a == adSize.a && this.b == adSize.b;
    }

    public int hashCode() {
        return (Integer.valueOf(this.a).hashCode() << 16) | (Integer.valueOf(this.b).hashCode() & 65535);
    }

    public int getWidth() {
        if (this.a < 0) {
            throw new UnsupportedOperationException("Ad size was not set before getWidth() was called.");
        }
        return this.a;
    }

    public int getHeight() {
        if (this.b < 0) {
            throw new UnsupportedOperationException("Ad size was not set before getHeight() was called.");
        }
        return this.b;
    }

    private boolean a() {
        return this.a < 0 || this.b < 0;
    }

    public boolean isFullWidth() {
        return this.c;
    }

    public boolean isAutoHeight() {
        return this.d;
    }

    public boolean isCustomAdSize() {
        return this.e;
    }

    public String toString() {
        return getWidth() + "x" + getHeight() + (this.f == null ? StringUtils.EMPTY : "_" + this.f);
    }

    public int getWidthInPixels(Context context) {
        return (int) TypedValue.applyDimension(1, this.a, context.getResources().getDisplayMetrics());
    }

    public int getHeightInPixels(Context context) {
        return (int) TypedValue.applyDimension(1, this.b, context.getResources().getDisplayMetrics());
    }

    public boolean isSizeAppropriate(int width, int height) {
        return ((double) width) <= ((double) this.a) * 1.25d && ((double) width) >= ((double) this.a) * 0.8d && ((double) height) <= ((double) this.b) * 1.25d && ((double) height) >= ((double) this.b) * 0.8d;
    }

    public AdSize findBestSize(AdSize... options) {
        double d;
        AdSize adSize;
        AdSize adSize2 = null;
        double d2 = 0.0d;
        if (options != null) {
            int length = options.length;
            int i = 0;
            while (i < length) {
                AdSize adSize3 = options[i];
                if (isSizeAppropriate(adSize3.a, adSize3.b)) {
                    d = (adSize3.a * adSize3.b) / (this.a * this.b);
                    if (d > 1.0d) {
                        d = 1.0d / d;
                    }
                    if (d > d2) {
                        adSize = adSize3;
                        i++;
                        adSize2 = adSize;
                        d2 = d;
                    }
                }
                d = d2;
                adSize = adSize2;
                i++;
                adSize2 = adSize;
                d2 = d;
            }
        }
        return adSize2;
    }

    private static int a(Context context) {
        return (int) (r0.widthPixels / context.getResources().getDisplayMetrics().density);
    }

    private static int b(Context context) {
        int i = (int) (r0.heightPixels / context.getResources().getDisplayMetrics().density);
        if (i <= 400) {
            return 32;
        }
        if (i <= 720) {
            return 50;
        }
        return 90;
    }
}
