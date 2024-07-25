package com.mappn.sdk.pay.chargement.alipay;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/* loaded from: classes.dex */
final class h implements HostnameVerifier {
    /* JADX INFO: Access modifiers changed from: package-private */
    public h(NetworkManager networkManager) {
    }

    @Override // javax.net.ssl.HostnameVerifier
    public final boolean verify(String str, SSLSession sSLSession) {
        return true;
    }
}
