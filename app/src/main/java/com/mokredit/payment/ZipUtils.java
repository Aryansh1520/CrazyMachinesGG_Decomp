package com.mokredit.payment;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* loaded from: classes.dex */
public class ZipUtils {
    private static void a(String str, String str2, ZipOutputStream zipOutputStream) {
        Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");
        if (zipOutputStream == null) {
            return;
        }
        File file = new File(String.valueOf(str) + str2);
        if (!file.isFile()) {
            String[] list = file.list();
            if (list.length <= 0) {
                zipOutputStream.putNextEntry(new ZipEntry(String.valueOf(str2) + File.separator));
                zipOutputStream.closeEntry();
            }
            for (String str3 : list) {
                a(str, String.valueOf(str2) + File.separator + str3, zipOutputStream);
            }
            return;
        }
        ZipEntry zipEntry = new ZipEntry(str2);
        FileInputStream fileInputStream = new FileInputStream(file);
        zipOutputStream.putNextEntry(zipEntry);
        byte[] bArr = new byte[4096];
        while (true) {
            int read = fileInputStream.read(bArr);
            if (read == -1) {
                zipOutputStream.closeEntry();
                return;
            }
            zipOutputStream.write(bArr, 0, read);
        }
    }

    public static List getFileList(String str, boolean z, boolean z2) {
        ArrayList arrayList = new ArrayList();
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(str));
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                zipInputStream.close();
                return arrayList;
            }
            String name = nextEntry.getName();
            if (nextEntry.isDirectory()) {
                File file = new File(name.substring(0, name.length() - 1));
                if (z) {
                    arrayList.add(file);
                }
            } else {
                File file2 = new File(name);
                if (z2) {
                    arrayList.add(file2);
                }
            }
        }
    }

    public static InputStream unzip(String str, String str2) {
        ZipFile zipFile = new ZipFile(str);
        return zipFile.getInputStream(zipFile.getEntry(str2));
    }

    public static void unzipToFolder(InputStream inputStream, String str) {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry == null) {
                zipInputStream.close();
                return;
            }
            String name = nextEntry.getName();
            if (nextEntry.isDirectory()) {
                new File(String.valueOf(str) + File.separator + name.substring(0, name.length() - 1)).mkdirs();
            } else {
                File file = new File(String.valueOf(str) + File.separator + name);
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = zipInputStream.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
            }
        }
    }

    public static void unzipToFolder(String str, String str2) {
        unzipToFolder(new FileInputStream(str), str2);
    }

    public static void zipFolder(String str, String str2) {
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(str2));
        File file = new File(str);
        a(String.valueOf(file.getParent()) + File.separator, file.getName(), zipOutputStream);
        zipOutputStream.finish();
        zipOutputStream.close();
    }
}
