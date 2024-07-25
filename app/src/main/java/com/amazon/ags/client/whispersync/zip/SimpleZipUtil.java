package com.amazon.ags.client.whispersync.zip;

import android.text.TextUtils;
import android.util.Log;
import com.mokredit.payment.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* loaded from: classes.dex */
public class SimpleZipUtil implements ZipUtil {
    private static final String FEATURE_NAME = "STC";
    private static final int KIBI = 1024;
    private static final String TAG = "STC_" + SimpleZipUtil.class.getSimpleName();
    private FilenameFilter currentFilter;
    private boolean hasFile;
    private ZipInputStream zipInputStream;
    private ZipOutputStream zipOutputStream;

    @Override // com.amazon.ags.client.whispersync.zip.ZipUtil
    public final byte[] zipToByteArray(File baseDir, FilenameFilter filter) throws IOException {
        if (baseDir == null) {
            throw new IllegalArgumentException("baseDir cannot be null");
        }
        if (filter == null) {
            throw new IllegalArgumentException("filter cannot be null");
        }
        this.hasFile = false;
        this.currentFilter = filter;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.zipOutputStream = new ZipOutputStream(stream);
        File[] arr$ = baseDir.listFiles(filter);
        for (File child : arr$) {
            addWithFilter(child, StringUtils.EMPTY);
        }
        byte[] data = stream.toByteArray();
        try {
            this.zipOutputStream.close();
            if (!this.hasFile) {
                Log.d(TAG, "No files, cannot create zip file");
                return null;
            }
            return data;
        } catch (ZipException e) {
            Log.d(TAG, "No files found.  Returning null", e);
            return null;
        }
    }

    private void addWithFilter(File file, String inPath) throws IOException {
        Log.d(TAG, "adding file " + file.getName() + " to [" + inPath + "]");
        String path = inPath;
        if (!TextUtils.isEmpty(path)) {
            path = path + "/";
        }
        String path2 = path + file.getName();
        if (file.isDirectory()) {
            File[] arr$ = file.listFiles(this.currentFilter);
            for (File child : arr$) {
                addWithFilter(child, path2);
            }
            return;
        }
        zipFileToStream(file, path2);
        this.hasFile = true;
    }

    private void zipFileToStream(File file, String path) throws IOException {
        FileInputStream in = null;
        try {
            ZipEntry zipEntry = new ZipEntry(path);
            zipEntry.setTime(0L);
            this.zipOutputStream.putNextEntry(zipEntry);
            FileInputStream in2 = new FileInputStream(file);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = in2.read(buffer);
                    if (len <= 0) {
                        break;
                    } else {
                        this.zipOutputStream.write(buffer, 0, len);
                    }
                }
                if (in2 != null) {
                    try {
                        in2.close();
                    } finally {
                        if (this.zipOutputStream != null) {
                            this.zipOutputStream.closeEntry();
                        }
                    }
                }
            } catch (Throwable th) {
                th = th;
                in = in2;
                if (in != null) {
                    try {
                        in.close();
                    } finally {
                        if (this.zipOutputStream != null) {
                            this.zipOutputStream.closeEntry();
                        }
                    }
                }
                if (this.zipOutputStream != null) {
                    this.zipOutputStream.closeEntry();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // com.amazon.ags.client.whispersync.zip.ZipUtil
    public final void unzip(byte[] source, File destinationDir) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source cannot be null");
        }
        unzip(new ByteArrayInputStream(source), destinationDir);
    }

    @Override // com.amazon.ags.client.whispersync.zip.ZipUtil
    public final void unzip(InputStream source, File destinationDir) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source cannot be null");
        }
        if (destinationDir == null) {
            throw new IllegalArgumentException("destinationDir cannot be null");
        }
        Log.d(TAG, "Entering unzip() with destination directory [" + destinationDir.getPath() + "]");
        this.zipInputStream = new ZipInputStream(source);
        while (true) {
            try {
                ZipEntry entry = getNextEntry(this.zipInputStream);
                if (entry == null) {
                    break;
                }
                File entryFile = new File(destinationDir, entry.getName());
                if (entry.isDirectory()) {
                    Log.d(TAG, "Creating dir: " + entryFile.getName());
                    entryFile.mkdirs();
                } else {
                    Log.d(TAG, "Creating file: " + entryFile.getName());
                    unzipFile(entryFile);
                }
            } finally {
                if (this.zipInputStream != null) {
                    this.zipInputStream.close();
                }
            }
        }
    }

    private ZipEntry getNextEntry(ZipInputStream zip) {
        try {
            return zip.getNextEntry();
        } catch (EOFException e) {
            Log.d(TAG, "Ignoring EOFException");
            return null;
        } catch (IOException e2) {
            return null;
        }
    }

    private void unzipFile(File entryFile) throws IOException {
        entryFile.getParentFile().mkdirs();
        byte[] buffer = new byte[1024];
        FileOutputStream os = null;
        try {
            FileOutputStream os2 = new FileOutputStream(entryFile);
            while (true) {
                try {
                    int count = this.zipInputStream.read(buffer);
                    if (count == -1) {
                        break;
                    } else {
                        os2.write(buffer, 0, count);
                    }
                } catch (Throwable th) {
                    th = th;
                    os = os2;
                    this.zipInputStream.closeEntry();
                    if (os != null) {
                        os.close();
                    }
                    throw th;
                }
            }
            this.zipInputStream.closeEntry();
            if (os2 != null) {
                os2.close();
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
