package com.chartboost.sdk.Libraries;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.Networking.CBAPIConnection;
import com.chartboost.sdk.Networking.CBAPIRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

/* loaded from: classes.dex */
public class CBWebImageCache {
    private static final String TAG = "CBWebImageCache";
    private static CBWebImageCache sharedCache = null;
    private FileCache fileCache = new FileCache(Chartboost.sharedChartboost().getContext());
    private CBBitmapMemoryCache memoryCache = new CBBitmapMemoryCache();

    /* loaded from: classes.dex */
    public interface CBWebImageProtocol {
        void execute(Bitmap bitmap, Bundle bundle);
    }

    public static synchronized CBWebImageCache sharedCache() {
        CBWebImageCache cBWebImageCache;
        synchronized (CBWebImageCache.class) {
            if (sharedCache == null) {
                sharedCache = new CBWebImageCache();
            }
            cBWebImageCache = sharedCache;
        }
        return cBWebImageCache;
    }

    private CBWebImageCache() {
    }

    public void clearCache() {
        this.fileCache.clear();
        this.memoryCache.clear();
    }

    public void loadImageWithURL(String urlString, String hexSHA1Checksum, CBWebImageProtocol delegate, ImageView targetImageView, Bundle bundle) {
        Bitmap cachedBitmap = null;
        try {
            cachedBitmap = this.memoryCache.get(hexSHA1Checksum);
            if (cachedBitmap == null && (cachedBitmap = readCachedBitmapFromDisk(hexSHA1Checksum)) != null) {
                this.memoryCache.put(hexSHA1Checksum, cachedBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cachedBitmap != null) {
            if (targetImageView != null) {
                targetImageView.setImageBitmap(cachedBitmap);
            }
            if (delegate != null) {
                delegate.execute(cachedBitmap, bundle);
                return;
            }
            return;
        }
        new BitmapDownloaderTask(targetImageView, delegate, hexSHA1Checksum, bundle).execute(urlString);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private Bundle bundle;
        private String checksum;
        private CBWebImageProtocol delegate;
        private final WeakReference<ImageView> imageViewReference;
        private String url;

        public BitmapDownloaderTask(ImageView imageView, CBWebImageProtocol _delegate, String _checksum, Bundle _bundle) {
            this.imageViewReference = new WeakReference<>(imageView);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(this);
            if (imageView != null) {
                imageView.setImageDrawable(downloadedDrawable);
            }
            this.checksum = _checksum;
            this.delegate = _delegate;
            this.bundle = _bundle;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(String... params) {
            this.url = params[0];
            return CBWebImageCache.this.downloadBitmap(this.url, this.checksum);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            if (!isCancelled()) {
                if (bitmap != null) {
                    try {
                        CBWebImageCache.this.writeBitmapToDisk(this.checksum, bitmap);
                        CBWebImageCache.this.memoryCache.put(this.checksum, bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (this.imageViewReference != null) {
                    ImageView imageView = this.imageViewReference.get();
                    BitmapDownloaderTask bitmapDownloaderTask = CBWebImageCache.getBitmapDownloaderTask(imageView);
                    if (this == bitmapDownloaderTask) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
                if (this.delegate != null) {
                    this.delegate.execute(bitmap, this.bundle);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class DownloadedDrawable extends BitmapDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            this.bitmapDownloaderTaskReference = new WeakReference<>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return this.bitmapDownloaderTaskReference.get();
        }
    }

    Bitmap downloadBitmap(String url, String checksum) {
        HttpResponse response;
        int statusCode;
        Bitmap cachedBitmap = null;
        try {
            cachedBitmap = readCachedBitmapFromDisk(checksum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cachedBitmap == null) {
            HttpClient client = CBAPIConnection.getSharedHttpClient();
            HttpGet getRequest = new HttpGet(url);
            try {
                response = client.execute(getRequest);
                statusCode = response.getStatusLine().getStatusCode();
            } catch (IOException e2) {
                getRequest.abort();
                Log.w(TAG, "I/O error while retrieving bitmap from " + url, e2);
            } catch (IllegalStateException e3) {
                getRequest.abort();
                Log.w(TAG, "Incorrect URL: " + url);
            } catch (Exception e4) {
                getRequest.abort();
                Log.w(TAG, "Error while retrieving bitmap from " + url, e4);
            }
            if (statusCode != 200) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    Bitmap cachedBitmap2 = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
                    return cachedBitmap2;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
            return null;
        }
        return cachedBitmap;
    }

    protected Bitmap readCachedBitmapFromDisk(String checksum) throws IOException {
        File file = this.fileCache.getFile(String.valueOf(checksum) + ".png");
        boolean fileExists = file.exists();
        if (!fileExists) {
            return null;
        }
        BufferedInputStream istream = new BufferedInputStream(new FileInputStream(file));
        long fileSize = file.length();
        if (fileSize > 2147483647L) {
            throw new IOException("Cannot read files larger than 2147483647 bytes");
        }
        int imageDataLength = (int) fileSize;
        byte[] imageData = new byte[imageDataLength];
        istream.read(imageData, 0, imageDataLength);
        istream.close();
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }

    protected void writeBitmapToDisk(String checksum, Bitmap image) throws IOException {
        File file = this.fileCache.getFile(String.valueOf(checksum) + ".png");
        FileOutputStream out = new FileOutputStream(file);
        image.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.close();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0;
            while (totalBytesSkipped < n) {
                long bytesSkipped = this.in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0) {
                    int b = read();
                    if (b < 0) {
                        break;
                    }
                    bytesSkipped = 1;
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class FileCache {
        private File cacheDir;

        public FileCache(Context cx) {
            this.cacheDir = null;
            try {
                int res = cx.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
                boolean hasPermission = res == 0;
                if (hasPermission && Environment.getExternalStorageState().equals("mounted")) {
                    this.cacheDir = cx.getExternalFilesDir(CBAPIRequest.CB_PARAM_CACHE);
                }
                if (this.cacheDir != null && !this.cacheDir.exists()) {
                    this.cacheDir.mkdirs();
                }
            } catch (Exception e) {
                this.cacheDir = null;
            }
            if (this.cacheDir == null) {
                this.cacheDir = cx.getCacheDir();
                if (!this.cacheDir.exists()) {
                    this.cacheDir.mkdirs();
                }
            }
        }

        public File getFile(String url) {
            File f = new File(this.cacheDir, url);
            return f;
        }

        public void clear() {
            try {
                File[] files = this.cacheDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        f.delete();
                    }
                }
            } catch (Exception e) {
            }
        }
    }
}
