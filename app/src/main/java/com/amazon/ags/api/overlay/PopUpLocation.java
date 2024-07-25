package com.amazon.ags.api.overlay;

/* loaded from: classes.dex */
public enum PopUpLocation {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    TOP_CENTER,
    BOTTOM_CENTER;

    public static final PopUpLocation DEFAULT_POPUP_LOCATION = TOP_RIGHT;

    public static PopUpLocation getLocationFromString(String str, PopUpLocation defaultLocation) {
        try {
            return valueOf(str);
        } catch (IllegalArgumentException e) {
            return defaultLocation;
        } catch (NullPointerException e2) {
            return defaultLocation;
        }
    }
}
