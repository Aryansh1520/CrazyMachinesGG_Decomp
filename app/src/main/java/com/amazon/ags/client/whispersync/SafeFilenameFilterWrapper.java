package com.amazon.ags.client.whispersync;

import com.amazon.ags.client.whispersync.savedgame.FileBackedSummaryRepository;
import com.chartboost.sdk.Networking.CBAPIRequest;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public final class SafeFilenameFilterWrapper implements FilenameFilter {
    private static final Set<String> FORBIDDEN_DIRECTORIES = new HashSet(Arrays.asList(CBAPIRequest.CB_PARAM_CACHE, "lib", FileBackedSummaryRepository.AMAZON_GAMES_DIR_NAME));
    private FilenameFilter gameFilter = null;

    public void setFilter(FilenameFilter filter) {
        this.gameFilter = filter;
    }

    @Override // java.io.FilenameFilter
    public boolean accept(File file, String s) {
        boolean isDir = new File(file, s).isDirectory();
        if (isDir && FORBIDDEN_DIRECTORIES.contains(s)) {
            return false;
        }
        if (this.gameFilter == null) {
            return true;
        }
        return this.gameFilter.accept(file, s);
    }
}
