package com.amazon.ags.overlay;

import com.amazon.ags.api.overlay.PopUpLocation;

/* loaded from: classes.dex */
public enum PopUpPrefs {
    INSTANCE;

    private static final int FADE_IN_DURATION_MS = 500;
    private static final int FADE_OUT_DURATION_MS = 5000;
    private static final int WELCOME_BACK_FADE_IN_DURATION = 500;
    private static final int WELCOME_BACK_FADE_OUT_DURATION = 3000;
    private boolean popUpsEnabled = true;
    private PopUpLocation popUpLocation = PopUpLocation.TOP_RIGHT;

    PopUpPrefs() {
    }

    public synchronized void disable() {
        INSTANCE.popUpsEnabled = false;
    }

    public synchronized void enable() {
        INSTANCE.popUpsEnabled = true;
    }

    public synchronized void setLocation(PopUpLocation location) {
        this.popUpLocation = location;
    }

    public synchronized boolean isEnabled() {
        return this.popUpsEnabled;
    }

    public synchronized PopUpLocation getLocation() {
        return this.popUpLocation;
    }

    public int getFadeInDuration() {
        return 500;
    }

    public int getFadeOutDuration() {
        return FADE_OUT_DURATION_MS;
    }

    public int getWelcomeBackFadeInDuration() {
        return 500;
    }

    public int getWelcomeBackFadeOutDuration() {
        return WELCOME_BACK_FADE_OUT_DURATION;
    }
}
