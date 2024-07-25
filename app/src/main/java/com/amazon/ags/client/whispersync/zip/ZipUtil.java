package com.amazon.ags.client.whispersync.zip;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public interface ZipUtil {
    void unzip(InputStream inputStream, File file) throws IOException;

    void unzip(byte[] bArr, File file) throws IOException;

    byte[] zipToByteArray(File file, FilenameFilter filenameFilter) throws IOException;
}
