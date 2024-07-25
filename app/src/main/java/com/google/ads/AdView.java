package com.google.ads;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.ads.util.AdUtil;
import com.mappn.sdk.pay.util.Constants;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class AdView extends RelativeLayout implements Ad {
    private m a;
    private com.google.ads.internal.d b;

    public AdView(Activity activity, AdSize adSize, String adUnitId) {
        super(activity.getApplicationContext());
        try {
            a(activity, adSize, (AttributeSet) null);
            b(activity, adSize, null);
            a(activity, adSize, adUnitId);
        } catch (com.google.ads.internal.b e) {
            a(activity, e.c("Could not initialize AdView"), adSize, null);
            e.a("Could not initialize AdView");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AdView(Activity activity, AdSize[] adSizes, String adUnitId) {
        this(activity, new AdSize(0, 0), adUnitId);
        a(adSizes);
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        a(context, attrs);
    }

    public AdView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    void a(Context context, String str, int i, AdSize adSize, AttributeSet attributeSet) {
        if (adSize == null) {
            adSize = AdSize.BANNER;
        }
        AdSize createAdSize = AdSize.createAdSize(adSize, context.getApplicationContext());
        if (getChildCount() == 0) {
            TextView textView = attributeSet == null ? new TextView(context) : new TextView(context, attributeSet);
            textView.setGravity(17);
            textView.setText(str);
            textView.setTextColor(i);
            textView.setBackgroundColor(-16777216);
            LinearLayout linearLayout = attributeSet == null ? new LinearLayout(context) : new LinearLayout(context, attributeSet);
            linearLayout.setGravity(17);
            LinearLayout linearLayout2 = attributeSet == null ? new LinearLayout(context) : new LinearLayout(context, attributeSet);
            linearLayout2.setGravity(17);
            linearLayout2.setBackgroundColor(i);
            int a = a(context, createAdSize.getWidth());
            int a2 = a(context, createAdSize.getHeight());
            linearLayout.addView(textView, a - 2, a2 - 2);
            linearLayout2.addView(linearLayout);
            addView(linearLayout2, a, a2);
        }
    }

    private int a(Context context, int i) {
        return (int) TypedValue.applyDimension(1, i, context.getResources().getDisplayMetrics());
    }

    private boolean a(Context context, AdSize adSize, AttributeSet attributeSet) {
        if (AdUtil.c(context)) {
            return true;
        }
        a(context, "You must have AdActivity declared in AndroidManifest.xml with configChanges.", adSize, attributeSet);
        return false;
    }

    private boolean b(Context context, AdSize adSize, AttributeSet attributeSet) {
        if (AdUtil.b(context)) {
            return true;
        }
        a(context, "You must have INTERNET and ACCESS_NETWORK_STATE permissions in AndroidManifest.xml.", adSize, attributeSet);
        return false;
    }

    public void destroy() {
        this.b.b();
    }

    private void a(Context context, String str, AdSize adSize, AttributeSet attributeSet) {
        com.google.ads.util.b.b(str);
        a(context, str, -65536, adSize, attributeSet);
    }

    private AdSize[] a(String str) {
        AdSize adSize;
        String[] split = str.split(Constants.TERM);
        AdSize[] adSizeArr = new AdSize[split.length];
        for (int i = 0; i < split.length; i++) {
            String trim = split[i].trim();
            if ("BANNER".equals(trim)) {
                adSize = AdSize.BANNER;
            } else {
                adSize = "SMART_BANNER".equals(trim) ? AdSize.SMART_BANNER : "IAB_MRECT".equals(trim) ? AdSize.IAB_MRECT : "IAB_BANNER".equals(trim) ? AdSize.IAB_BANNER : "IAB_LEADERBOARD".equals(trim) ? AdSize.IAB_LEADERBOARD : "IAB_WIDE_SKYSCRAPER".equals(trim) ? AdSize.IAB_WIDE_SKYSCRAPER : null;
            }
            if (adSize == null) {
                return null;
            }
            adSizeArr[i] = adSize;
        }
        return adSizeArr;
    }

    private void a(Context context, AttributeSet attributeSet) {
        com.google.ads.internal.b bVar;
        AdSize[] adSizeArr;
        if (attributeSet != null) {
            try {
                String a = a("adSize", context, attributeSet, false, true);
                AdSize[] a2 = a(a);
                if (a2 != null) {
                    try {
                        if (a2.length != 0) {
                            if (!a("adUnitId", attributeSet)) {
                                throw new com.google.ads.internal.b("Required XML attribute \"adUnitId\" missing", true);
                            }
                            if (isInEditMode()) {
                                a(context, "Ads by Google", -1, a2[0], attributeSet);
                                return;
                            }
                            String a3 = a("adUnitId", context, attributeSet, true, true);
                            boolean attributeBooleanValue = attributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/lib/com.google.ads", "loadAdOnCreate", false);
                            if (context instanceof Activity) {
                                Activity activity = (Activity) context;
                                a(activity, a2[0], attributeSet);
                                b(activity, a2[0], attributeSet);
                                if (a2.length == 1) {
                                    a(activity, a2[0], a3);
                                } else {
                                    a(activity, new AdSize(0, 0), a3);
                                    a(a2);
                                }
                                if (attributeBooleanValue) {
                                    Set<String> b = b("testDevices", context, attributeSet, false, false);
                                    if (b.contains("TEST_EMULATOR")) {
                                        b.remove("TEST_EMULATOR");
                                        b.add(AdRequest.TEST_EMULATOR);
                                    }
                                    loadAd(new AdRequest().setTestDevices(b).setKeywords(b("keywords", context, attributeSet, false, false)));
                                    return;
                                }
                                return;
                            }
                            throw new com.google.ads.internal.b("AdView was initialized with a Context that wasn't an Activity.", true);
                        }
                    } catch (com.google.ads.internal.b e) {
                        bVar = e;
                        adSizeArr = a2;
                        a(context, bVar.c("Could not initialize AdView"), (adSizeArr == null || adSizeArr.length <= 0) ? AdSize.BANNER : adSizeArr[0], attributeSet);
                        bVar.a("Could not initialize AdView");
                        if (!isInEditMode()) {
                            bVar.b("Could not initialize AdView");
                            return;
                        }
                        return;
                    }
                }
                throw new com.google.ads.internal.b("Attribute \"adSize\" invalid: " + a, true);
            } catch (com.google.ads.internal.b e2) {
                bVar = e2;
                adSizeArr = null;
            }
        }
    }

    private String a(String str, Context context, AttributeSet attributeSet, boolean z, boolean z2) throws com.google.ads.internal.b {
        String str2;
        String attributeValue = attributeSet.getAttributeValue("http://schemas.android.com/apk/lib/com.google.ads", str);
        if (attributeValue != null && attributeValue.startsWith("@string/") && z) {
            String substring = attributeValue.substring("@string/".length());
            String packageName = context.getPackageName();
            TypedValue typedValue = new TypedValue();
            try {
                getResources().getValue(packageName + ":string/" + substring, typedValue, true);
                if (typedValue.string != null) {
                    str2 = typedValue.string.toString();
                } else {
                    throw new com.google.ads.internal.b("Resource " + str + " was not a string: " + typedValue, true);
                }
            } catch (Resources.NotFoundException e) {
                throw new com.google.ads.internal.b("Could not find resource for " + str + ": " + attributeValue, true, e);
            }
        } else {
            str2 = attributeValue;
        }
        if (z2 && str2 == null) {
            throw new com.google.ads.internal.b("Required XML attribute \"" + str + "\" missing", true);
        }
        return str2;
    }

    private Set<String> b(String str, Context context, AttributeSet attributeSet, boolean z, boolean z2) throws com.google.ads.internal.b {
        String a = a(str, context, attributeSet, z, z2);
        HashSet hashSet = new HashSet();
        if (a != null) {
            String[] split = a.split(Constants.TERM);
            for (String str2 : split) {
                String trim = str2.trim();
                if (trim.length() != 0) {
                    hashSet.add(trim);
                }
            }
        }
        return hashSet;
    }

    private boolean a(String str, AttributeSet attributeSet) {
        return attributeSet.getAttributeValue("http://schemas.android.com/apk/lib/com.google.ads", str) != null;
    }

    private void a(Activity activity, AdSize adSize, String str) throws com.google.ads.internal.b {
        FrameLayout frameLayout = new FrameLayout(activity);
        frameLayout.setFocusable(false);
        this.a = m.a(this, str, activity, frameLayout, adSize);
        this.b = new com.google.ads.internal.d(this.a, false);
        setGravity(17);
        try {
            ViewGroup a = com.google.ads.internal.j.a(activity, this.b);
            if (a != null) {
                a.addView(frameLayout, -2, -2);
                addView(a, -2, -2);
            } else {
                addView(frameLayout, -2, -2);
            }
        } catch (VerifyError e) {
            com.google.ads.util.b.a("Gestures disabled: Not supported on this version of Android.", e);
            addView(frameLayout, -2, -2);
        }
    }

    @Override // com.google.ads.Ad
    public boolean isReady() {
        if (this.b == null) {
            return false;
        }
        return this.b.r();
    }

    public boolean isRefreshing() {
        if (this.b == null) {
            return false;
        }
        return this.b.s();
    }

    @Override // com.google.ads.Ad
    public void loadAd(AdRequest adRequest) {
        if (this.b != null) {
            if (isRefreshing()) {
                this.b.e();
            }
            this.b.a(adRequest);
        }
    }

    @Override // com.google.ads.Ad
    public void setAdListener(AdListener adListener) {
        this.a.k.a(adListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setAppEventListener(AppEventListener appEventListener) {
        this.a.l.a(appEventListener);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSupportedAdSizes(AdSize... adSizes) {
        if (this.a.j.a() == null) {
            com.google.ads.util.b.b("Error: Tried to set supported ad sizes on a single-size AdView.");
        } else {
            a(adSizes);
        }
    }

    private void a(AdSize... adSizeArr) {
        AdSize[] adSizeArr2 = new AdSize[adSizeArr.length];
        for (int i = 0; i < adSizeArr.length; i++) {
            adSizeArr2[i] = AdSize.createAdSize(adSizeArr[i], getContext());
        }
        this.a.j.a(adSizeArr2);
    }

    @Override // com.google.ads.Ad
    public void stopLoading() {
        if (this.b != null) {
            this.b.A();
        }
    }
}
