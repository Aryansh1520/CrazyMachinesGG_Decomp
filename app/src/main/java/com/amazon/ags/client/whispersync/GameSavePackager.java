package com.amazon.ags.client.whispersync;

import android.content.Context;
import android.util.Log;
import com.amazon.ags.client.whispersync.zip.SimpleZipUtil;
import com.amazon.ags.client.whispersync.zip.ZipUtil;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/* loaded from: classes.dex */
public class GameSavePackager {
    private static final String FEATURE_NAME = "STC";
    private static final String TAG = "STC_" + GameSavePackager.class.getSimpleName();
    private final Context context;
    private SafeFilenameFilterWrapper filterWrapper;
    private final ZipUtil zipUtil;

    public GameSavePackager(Context context, FilenameFilter filter) {
        this.zipUtil = new SimpleZipUtil();
        this.context = context;
        this.filterWrapper = new SafeFilenameFilterWrapper();
        this.filterWrapper.setFilter(filter);
    }

    public GameSavePackager(Context context) {
        this(context, null);
    }

    public final void setFilter(FilenameFilter override) {
        this.filterWrapper.setFilter(override);
    }

    public byte[] pack() throws IOException {
        Log.d(TAG, "Entering pack()...");
        File packageDir = this.context.getFilesDir().getParentFile();
        return this.zipUtil.zipToByteArray(packageDir, this.filterWrapper);
    }

    public void unpack(byte[] data) throws IOException {
        Log.d(TAG, "Entering unpack()...");
        File packageDir = this.context.getFilesDir().getParentFile();
        this.zipUtil.unzip(data, packageDir);
    }
}
