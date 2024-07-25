package com.chartboost.sdk.View;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Libraries.CBConstants;
import com.chartboost.sdk.Model.CBImpression;

/* loaded from: classes.dex */
public class CBAnimationManager {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$View$CBAnimationManager$CBAnimationType;

    /* loaded from: classes.dex */
    public interface CBAnimationProtocol {
        void execute(CBImpression cBImpression);
    }

    /* loaded from: classes.dex */
    public enum CBAnimationType {
        CBAnimationTypeNone,
        CBAnimationTypePerspectiveRotate,
        CBAnimationTypeBounce,
        CBAnimationTypePerspectiveZoom,
        CBAnimationTypeSlideFromBottom,
        CBAnimationTypeSlideFromTop;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static CBAnimationType[] valuesCustom() {
            CBAnimationType[] valuesCustom = values();
            int length = valuesCustom.length;
            CBAnimationType[] cBAnimationTypeArr = new CBAnimationType[length];
            System.arraycopy(valuesCustom, 0, cBAnimationTypeArr, 0, length);
            return cBAnimationTypeArr;
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference() {
        int[] iArr = $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference;
        if (iArr == null) {
            iArr = new int[CBConstants.CBOrientation.Difference.valuesCustom().length];
            try {
                iArr[CBConstants.CBOrientation.Difference.ANGLE_0.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CBConstants.CBOrientation.Difference.ANGLE_180.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[CBConstants.CBOrientation.Difference.ANGLE_270.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[CBConstants.CBOrientation.Difference.ANGLE_90.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$chartboost$sdk$View$CBAnimationManager$CBAnimationType() {
        int[] iArr = $SWITCH_TABLE$com$chartboost$sdk$View$CBAnimationManager$CBAnimationType;
        if (iArr == null) {
            iArr = new int[CBAnimationType.valuesCustom().length];
            try {
                iArr[CBAnimationType.CBAnimationTypeBounce.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CBAnimationType.CBAnimationTypeNone.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[CBAnimationType.CBAnimationTypePerspectiveRotate.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[CBAnimationType.CBAnimationTypePerspectiveZoom.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[CBAnimationType.CBAnimationTypeSlideFromBottom.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[CBAnimationType.CBAnimationTypeSlideFromTop.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $SWITCH_TABLE$com$chartboost$sdk$View$CBAnimationManager$CBAnimationType = iArr;
        }
        return iArr;
    }

    public static void transitionInWithAnimationType(CBAnimationType type, CBImpression impression) {
        transitionInWithAnimationType(type, impression, null);
    }

    public static void transitionInWithAnimationType(CBAnimationType type, CBImpression impression, CBAnimationProtocol block) {
        transitionWithAnimationType(type, impression, block, true);
    }

    public static void transitionOutWithAnimationType(CBAnimationType type, CBImpression impression) {
        transitionOutWithAnimationType(type, impression, null);
    }

    public static void transitionOutWithAnimationType(CBAnimationType type, CBImpression impression, CBAnimationProtocol block) {
        doTransitionWithAnimationType(type, impression, block, false);
    }

    private static void transitionWithAnimationType(final CBAnimationType type, final CBImpression impression, final CBAnimationProtocol block, final Boolean isInTransition) {
        final View layer = impression.parentView.getAnimatedView();
        ViewTreeObserver viewTreeObserver = layer.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.chartboost.sdk.View.CBAnimationManager.1
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public void onGlobalLayout() {
                    layer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    CBAnimationManager.doTransitionWithAnimationType(type, impression, block, isInTransition);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void doTransitionWithAnimationType(CBAnimationType type, final CBImpression impression, final CBAnimationProtocol block, Boolean isInTransition) {
        Animation rotateAnimation;
        Animation scaleAnimation;
        Animation translateAnimation;
        Animation rotateAnimation2;
        Animation scaleAnimation2;
        Animation translateAnimation2;
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(new AlphaAnimation(1.0f, 1.0f));
        View layer = impression.parentView.getAnimatedView();
        float width = layer.getWidth();
        float height = layer.getHeight();
        float offset = (1.0f - 0.4f) / 2.0f;
        CBConstants.CBOrientation.Difference orientationDifference = Chartboost.sharedChartboost().getForcedOrientationDifference();
        switch ($SWITCH_TABLE$com$chartboost$sdk$View$CBAnimationManager$CBAnimationType()[type.ordinal()]) {
            case 2:
                switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[orientationDifference.ordinal()]) {
                    case 2:
                        if (isInTransition.booleanValue()) {
                            rotateAnimation = new CBFlipAnimation(60.0f, 0.0f, width / 2.0f, height / 2.0f, false);
                            break;
                        } else {
                            rotateAnimation = new CBFlipAnimation(0.0f, -60.0f, width / 2.0f, height / 2.0f, false);
                            break;
                        }
                    case 3:
                        if (isInTransition.booleanValue()) {
                            rotateAnimation = new CBFlipAnimation(60.0f, 0.0f, width / 2.0f, height / 2.0f, true);
                            break;
                        } else {
                            rotateAnimation = new CBFlipAnimation(0.0f, -60.0f, width / 2.0f, height / 2.0f, true);
                            break;
                        }
                    case 4:
                        if (isInTransition.booleanValue()) {
                            rotateAnimation = new CBFlipAnimation(-60.0f, 0.0f, width / 2.0f, height / 2.0f, false);
                            break;
                        } else {
                            rotateAnimation = new CBFlipAnimation(0.0f, 60.0f, width / 2.0f, height / 2.0f, false);
                            break;
                        }
                    default:
                        if (isInTransition.booleanValue()) {
                            rotateAnimation = new CBFlipAnimation(-60.0f, 0.0f, width / 2.0f, height / 2.0f, true);
                            break;
                        } else {
                            rotateAnimation = new CBFlipAnimation(0.0f, 60.0f, width / 2.0f, height / 2.0f, true);
                            break;
                        }
                }
                rotateAnimation.setDuration(600L);
                rotateAnimation.setFillAfter(true);
                animation.addAnimation(rotateAnimation);
                if (isInTransition.booleanValue()) {
                    scaleAnimation = new ScaleAnimation(0.4f, 1.0f, 0.4f, 1.0f);
                } else {
                    scaleAnimation = new ScaleAnimation(1.0f, 0.4f, 1.0f, 0.4f);
                }
                scaleAnimation.setDuration(600L);
                scaleAnimation.setFillAfter(true);
                animation.addAnimation(scaleAnimation);
                switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[orientationDifference.ordinal()]) {
                    case 2:
                        if (isInTransition.booleanValue()) {
                            translateAnimation = new TranslateAnimation(width * offset, 0.0f, (-height) * 0.4f, 0.0f);
                            break;
                        } else {
                            translateAnimation = new TranslateAnimation(0.0f, width * offset, 0.0f, height);
                            break;
                        }
                    case 3:
                        if (!isInTransition.booleanValue()) {
                            translateAnimation = new TranslateAnimation(0.0f, (-width) * 0.4f, 0.0f, height * offset);
                            break;
                        } else {
                            translateAnimation = new TranslateAnimation(width, 0.0f, height * offset, 0.0f);
                            break;
                        }
                    case 4:
                        if (!isInTransition.booleanValue()) {
                            translateAnimation = new TranslateAnimation(0.0f, width * offset, 0.0f, (-height) * 0.4f);
                            break;
                        } else {
                            translateAnimation = new TranslateAnimation(width * offset, 0.0f, height, 0.0f);
                            break;
                        }
                    default:
                        if (isInTransition.booleanValue()) {
                            translateAnimation = new TranslateAnimation((-width) * 0.4f, 0.0f, height * offset, 0.0f);
                            break;
                        } else {
                            translateAnimation = new TranslateAnimation(0.0f, width, 0.0f, height * offset);
                            break;
                        }
                }
                translateAnimation.setDuration(600L);
                translateAnimation.setFillAfter(true);
                animation.addAnimation(translateAnimation);
                break;
            case 3:
                if (isInTransition.booleanValue()) {
                    Animation scaleAnimation3 = new ScaleAnimation(0.6f, 1.1f, 0.6f, 1.1f, 1, 0.5f, 1, 0.5f);
                    scaleAnimation3.setDuration(Math.round(((float) 600) * 0.6f));
                    scaleAnimation3.setStartOffset(0L);
                    scaleAnimation3.setFillAfter(true);
                    animation.addAnimation(scaleAnimation3);
                    Animation scaleAnimation4 = new ScaleAnimation(1.0f, 0.81818175f, 1.0f, 0.81818175f, 1, 0.5f, 1, 0.5f);
                    scaleAnimation4.setDuration(Math.round(((float) 600) * 0.19999999f));
                    scaleAnimation4.setStartOffset(Math.round(((float) 600) * 0.6f));
                    scaleAnimation4.setFillAfter(true);
                    animation.addAnimation(scaleAnimation4);
                    Animation scaleAnimation5 = new ScaleAnimation(1.0f, 1.1111112f, 1.0f, 1.1111112f, 1, 0.5f, 1, 0.5f);
                    scaleAnimation5.setDuration(Math.round(((float) 600) * 0.099999964f));
                    scaleAnimation5.setStartOffset(Math.round(((float) 600) * 0.8f));
                    scaleAnimation5.setFillAfter(true);
                    animation.addAnimation(scaleAnimation5);
                    break;
                } else {
                    Animation scaleAnimation6 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, 1, 0.5f, 1, 0.5f);
                    scaleAnimation6.setDuration(600L);
                    scaleAnimation6.setStartOffset(0L);
                    scaleAnimation6.setFillAfter(true);
                    animation.addAnimation(scaleAnimation6);
                    break;
                }
            case 4:
                switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[orientationDifference.ordinal()]) {
                    case 2:
                        if (isInTransition.booleanValue()) {
                            rotateAnimation2 = new CBFlipAnimation(-60.0f, 0.0f, width / 2.0f, height / 2.0f, true);
                            break;
                        } else {
                            rotateAnimation2 = new CBFlipAnimation(0.0f, 60.0f, width / 2.0f, height / 2.0f, true);
                            break;
                        }
                    case 3:
                        if (isInTransition.booleanValue()) {
                            rotateAnimation2 = new CBFlipAnimation(60.0f, 0.0f, width / 2.0f, height / 2.0f, false);
                            break;
                        } else {
                            rotateAnimation2 = new CBFlipAnimation(0.0f, -60.0f, width / 2.0f, height / 2.0f, false);
                            break;
                        }
                    case 4:
                        if (isInTransition.booleanValue()) {
                            rotateAnimation2 = new CBFlipAnimation(60.0f, 0.0f, width / 2.0f, height / 2.0f, true);
                            break;
                        } else {
                            rotateAnimation2 = new CBFlipAnimation(0.0f, -60.0f, width / 2.0f, height / 2.0f, true);
                            break;
                        }
                    default:
                        if (isInTransition.booleanValue()) {
                            rotateAnimation2 = new CBFlipAnimation(-60.0f, 0.0f, width / 2.0f, height / 2.0f, false);
                            break;
                        } else {
                            rotateAnimation2 = new CBFlipAnimation(0.0f, 60.0f, width / 2.0f, height / 2.0f, false);
                            break;
                        }
                }
                rotateAnimation2.setDuration(600L);
                rotateAnimation2.setFillAfter(true);
                animation.addAnimation(rotateAnimation2);
                if (isInTransition.booleanValue()) {
                    scaleAnimation2 = new ScaleAnimation(0.4f, 1.0f, 0.4f, 1.0f);
                } else {
                    scaleAnimation2 = new ScaleAnimation(1.0f, 0.4f, 1.0f, 0.4f);
                }
                scaleAnimation2.setDuration(600L);
                scaleAnimation2.setFillAfter(true);
                animation.addAnimation(scaleAnimation2);
                switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[orientationDifference.ordinal()]) {
                    case 2:
                        if (!isInTransition.booleanValue()) {
                            translateAnimation2 = new TranslateAnimation(0.0f, (-width) * 0.4f, 0.0f, height * offset);
                            break;
                        } else {
                            translateAnimation2 = new TranslateAnimation(width, 0.0f, height * offset, 0.0f);
                            break;
                        }
                    case 3:
                        if (!isInTransition.booleanValue()) {
                            translateAnimation2 = new TranslateAnimation(0.0f, width * offset, 0.0f, (-height) * 0.4f);
                            break;
                        } else {
                            translateAnimation2 = new TranslateAnimation(width * offset, 0.0f, height, 0.0f);
                            break;
                        }
                    case 4:
                        if (isInTransition.booleanValue()) {
                            translateAnimation2 = new TranslateAnimation((-width) * 0.4f, 0.0f, height * offset, 0.0f);
                            break;
                        } else {
                            translateAnimation2 = new TranslateAnimation(0.0f, width, 0.0f, height * offset);
                            break;
                        }
                    default:
                        if (isInTransition.booleanValue()) {
                            translateAnimation2 = new TranslateAnimation(width * offset, 0.0f, (-height) * 0.4f, 0.0f);
                            break;
                        } else {
                            translateAnimation2 = new TranslateAnimation(0.0f, width * offset, 0.0f, height);
                            break;
                        }
                }
                translateAnimation2.setDuration(600L);
                translateAnimation2.setFillAfter(true);
                animation.addAnimation(translateAnimation2);
                break;
            case 5:
                float fromx = 0.0f;
                float tox = 0.0f;
                float fromy = 0.0f;
                float toy = 0.0f;
                switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[orientationDifference.ordinal()]) {
                    case 1:
                        fromy = isInTransition.booleanValue() ? height : 0.0f;
                        if (!isInTransition.booleanValue()) {
                            toy = height;
                            break;
                        } else {
                            toy = 0.0f;
                            break;
                        }
                    case 2:
                        fromx = isInTransition.booleanValue() ? -width : 0.0f;
                        if (!isInTransition.booleanValue()) {
                            tox = -width;
                            break;
                        } else {
                            tox = 0.0f;
                            break;
                        }
                    case 3:
                        fromy = isInTransition.booleanValue() ? -height : 0.0f;
                        if (!isInTransition.booleanValue()) {
                            toy = -height;
                            break;
                        } else {
                            toy = 0.0f;
                            break;
                        }
                    case 4:
                        fromx = isInTransition.booleanValue() ? width : 0.0f;
                        if (!isInTransition.booleanValue()) {
                            tox = width;
                            break;
                        } else {
                            tox = 0.0f;
                            break;
                        }
                }
                Animation translateAnimation3 = new TranslateAnimation(fromx, tox, fromy, toy);
                translateAnimation3.setDuration(600L);
                translateAnimation3.setFillAfter(true);
                animation.addAnimation(translateAnimation3);
                break;
            case 6:
                float fromx2 = 0.0f;
                float tox2 = 0.0f;
                float fromy2 = 0.0f;
                float toy2 = 0.0f;
                switch ($SWITCH_TABLE$com$chartboost$sdk$Libraries$CBConstants$CBOrientation$Difference()[orientationDifference.ordinal()]) {
                    case 1:
                        fromy2 = isInTransition.booleanValue() ? -height : 0.0f;
                        if (!isInTransition.booleanValue()) {
                            toy2 = -height;
                            break;
                        } else {
                            toy2 = 0.0f;
                            break;
                        }
                    case 2:
                        fromx2 = isInTransition.booleanValue() ? width : 0.0f;
                        if (!isInTransition.booleanValue()) {
                            tox2 = width;
                            break;
                        } else {
                            tox2 = 0.0f;
                            break;
                        }
                    case 3:
                        fromy2 = isInTransition.booleanValue() ? height : 0.0f;
                        if (!isInTransition.booleanValue()) {
                            toy2 = height;
                            break;
                        } else {
                            toy2 = 0.0f;
                            break;
                        }
                    case 4:
                        fromx2 = isInTransition.booleanValue() ? -width : 0.0f;
                        if (!isInTransition.booleanValue()) {
                            tox2 = -width;
                            break;
                        } else {
                            tox2 = 0.0f;
                            break;
                        }
                }
                Animation translateAnimation4 = new TranslateAnimation(fromx2, tox2, fromy2, toy2);
                translateAnimation4.setDuration(600L);
                translateAnimation4.setFillAfter(true);
                animation.addAnimation(translateAnimation4);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() { // from class: com.chartboost.sdk.View.CBAnimationManager.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(Animation animation2) {
                if (CBAnimationProtocol.this != null) {
                    CBAnimationProtocol.this.execute(impression);
                }
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(Animation animation2) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(Animation animation2) {
            }
        });
        layer.startAnimation(animation);
    }
}
