package com.amazon.ags.jni.whispersync;

import com.amazon.ags.client.whispersync.savedgame.FileBackedSummaryRepository;
import com.chartboost.sdk.Networking.CBAPIRequest;
import java.io.File;
import java.io.FilenameFilter;

/* loaded from: classes.dex */
public class SimpleExclusionFilter implements FilenameFilter {
    private String mExclusionRegex;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SimpleExclusionFilter(String exclusionRegex) {
        this.mExclusionRegex = exclusionRegex;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        boolean isDir = new File(dir, name).isDirectory();
        return !isDir ? !name.matches(this.mExclusionRegex) : (name.matches(this.mExclusionRegex) || name.equals(CBAPIRequest.CB_PARAM_CACHE) || name.equals("lib") || name.equals(FileBackedSummaryRepository.AMAZON_GAMES_DIR_NAME)) ? false : true;
    }
}
