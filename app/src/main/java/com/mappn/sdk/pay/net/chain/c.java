package com.mappn.sdk.pay.net.chain;

import java.io.File;
import java.io.FilenameFilter;

/* loaded from: classes.dex */
final class c implements FilenameFilter {
    @Override // java.io.FilenameFilter
    public final boolean accept(File file, String str) {
        return str.endsWith(".smspayment");
    }
}
