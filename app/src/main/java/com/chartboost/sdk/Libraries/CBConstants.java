package com.chartboost.sdk.Libraries;

/* loaded from: classes.dex */
public final class CBConstants {
    public static final boolean DEBUG = false;
    public static final String kCBAPIEndpoint = "https://www.chartboost.com";
    public static final String kCBAPIVersion = "3.1.3";
    public static final long kCBAnimationDuration = 255;
    public static final String kCBSDKUserAgent = "Chartboost-Android-SDK 3.1.3";

    /* loaded from: classes.dex */
    public enum CBOrientation {
        UNSPECIFIED,
        PORTRAIT,
        LANDSCAPE,
        PORTRAIT_REVERSE,
        LANDSCAPE_REVERSE;

        private static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation;
        public static final CBOrientation PORTRAIT_LEFT = PORTRAIT_REVERSE;
        public static final CBOrientation PORTRAIT_RIGHT = PORTRAIT;
        public static final CBOrientation LANDSCAPE_LEFT = LANDSCAPE;
        public static final CBOrientation LANDSCAPE_RIGHT = LANDSCAPE_REVERSE;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static CBOrientation[] valuesCustom() {
            CBOrientation[] valuesCustom = values();
            int length = valuesCustom.length;
            CBOrientation[] cBOrientationArr = new CBOrientation[length];
            System.arraycopy(valuesCustom, 0, cBOrientationArr, 0, length);
            return cBOrientationArr;
        }

        static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation() {
            int[] iArr = $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation;
            if (iArr == null) {
                iArr = new int[valuesCustom().length];
                try {
                    iArr[LANDSCAPE.ordinal()] = 3;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[LANDSCAPE_REVERSE.ordinal()] = 5;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[PORTRAIT.ordinal()] = 2;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[PORTRAIT_REVERSE.ordinal()] = 4;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[UNSPECIFIED.ordinal()] = 1;
                } catch (NoSuchFieldError e5) {
                }
                $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation = iArr;
            }
            return iArr;
        }

        public CBOrientation rotate90() {
            switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation()[ordinal()]) {
                case 3:
                    return PORTRAIT_LEFT;
                case 4:
                    return LANDSCAPE_RIGHT;
                case 5:
                    return PORTRAIT_RIGHT;
                default:
                    return LANDSCAPE_LEFT;
            }
        }

        public CBOrientation rotate180() {
            return rotate90().rotate90();
        }

        public CBOrientation rotate270() {
            return rotate90().rotate90().rotate90();
        }

        public boolean isPortrait() {
            return this == PORTRAIT || this == PORTRAIT_REVERSE;
        }

        public boolean isLandscape() {
            return this == LANDSCAPE || this == LANDSCAPE_REVERSE;
        }

        /* loaded from: classes.dex */
        public enum Difference {
            ANGLE_0,
            ANGLE_90,
            ANGLE_180,
            ANGLE_270;

            private static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference;

            /* renamed from: values, reason: to resolve conflict with enum method */
            public static Difference[] valuesCustom() {
                Difference[] valuesCustom = values();
                int length = valuesCustom.length;
                Difference[] differenceArr = new Difference[length];
                System.arraycopy(valuesCustom, 0, differenceArr, 0, length);
                return differenceArr;
            }

            static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference() {
                int[] iArr = $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference;
                if (iArr == null) {
                    iArr = new int[valuesCustom().length];
                    try {
                        iArr[ANGLE_0.ordinal()] = 1;
                    } catch (NoSuchFieldError e) {
                    }
                    try {
                        iArr[ANGLE_180.ordinal()] = 3;
                    } catch (NoSuchFieldError e2) {
                    }
                    try {
                        iArr[ANGLE_270.ordinal()] = 4;
                    } catch (NoSuchFieldError e3) {
                    }
                    try {
                        iArr[ANGLE_90.ordinal()] = 2;
                    } catch (NoSuchFieldError e4) {
                    }
                    $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference = iArr;
                }
                return iArr;
            }

            public int getAsInt() {
                switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[ordinal()]) {
                    case 2:
                        return 90;
                    case 3:
                        return 180;
                    case 4:
                        return 270;
                    default:
                        return 0;
                }
            }

            public boolean isOdd() {
                return this == ANGLE_90 || this == ANGLE_270;
            }

            public boolean isReverse() {
                return this == ANGLE_180 || this == ANGLE_270;
            }
        }
    }
}
