package com.google.ads;

import android.app.Activity;
import com.google.ads.g;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.mappn.sdk.common.utils.BaseConstants;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class i implements Runnable {
    private final h a;
    private final String b;
    private final AdRequest c;
    private final HashMap<String, String> d;
    private final boolean e;
    private final WeakReference<Activity> f;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class a extends Exception {
        public a(String str) {
            super(str);
        }
    }

    private static boolean a(Map<String, String> map) {
        String remove = map.remove("gwhirl_share_location");
        if (BaseConstants.DEFAULT_UC_CNO.equals(remove)) {
            return true;
        }
        if (remove != null && !"0".equals(remove)) {
            com.google.ads.util.b.b("Received an illegal value, '" + remove + "', for the special share location parameter from mediation server (expected '0' or '1'). Will not share the location.");
        }
        return false;
    }

    public i(h hVar, Activity activity, String str, AdRequest adRequest, HashMap<String, String> hashMap) {
        this.a = hVar;
        this.b = str;
        this.f = new WeakReference<>(activity);
        this.c = adRequest;
        this.d = new HashMap<>(hashMap);
        this.e = a(this.d);
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            com.google.ads.util.b.a("Trying to instantiate: " + this.b);
            a((MediationAdapter) g.a(this.b, MediationAdapter.class));
        } catch (ClassNotFoundException e) {
            a("Cannot find adapter class '" + this.b + "'. Did you link the ad network's mediation adapter? Skipping ad network.", e, g.a.NOT_FOUND);
        } catch (Throwable th) {
            a("Error while creating adapter and loading ad from ad network. Skipping ad network.", th, g.a.EXCEPTION);
        }
    }

    private void a(String str, Throwable th, g.a aVar) {
        com.google.ads.util.b.b(str, th);
        this.a.a(false, aVar);
    }

    private <T extends NetworkExtras, U extends MediationServerParameters> void a(MediationAdapter<T, U> mediationAdapter) throws MediationServerParameters.MappingException, a, IllegalAccessException, InstantiationException {
        U u;
        Activity activity = this.f.get();
        if (activity == null) {
            throw new a("Activity became null while trying to instantiate adapter.");
        }
        this.a.a((MediationAdapter<?, ?>) mediationAdapter);
        Class<U> serverParametersType = mediationAdapter.getServerParametersType();
        if (serverParametersType != null) {
            U newInstance = serverParametersType.newInstance();
            newInstance.load(this.d);
            u = newInstance;
        } else {
            u = null;
        }
        Class<T> additionalParametersType = mediationAdapter.getAdditionalParametersType();
        NetworkExtras networkExtras = additionalParametersType != null ? (NetworkExtras) this.c.getNetworkExtras(additionalParametersType) : null;
        MediationAdRequest mediationAdRequest = new MediationAdRequest(this.c, activity, this.e);
        if (this.a.a.a()) {
            if (!(mediationAdapter instanceof MediationInterstitialAdapter)) {
                throw new a("Adapter " + this.b + " doesn't support the MediationInterstitialAdapter interface.");
            }
            ((MediationInterstitialAdapter) mediationAdapter).requestInterstitialAd(new k(this.a), activity, u, mediationAdRequest, networkExtras);
        } else {
            if (!(mediationAdapter instanceof MediationBannerAdapter)) {
                throw new a("Adapter " + this.b + " doesn't support the MediationBannerAdapter interface");
            }
            ((MediationBannerAdapter) mediationAdapter).requestBannerAd(new j(this.a), activity, u, this.a.a.b(), mediationAdRequest, networkExtras);
        }
        this.a.k();
    }
}
