package com.chartboost.sdk.Libraries;

import android.graphics.Bitmap;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class CBBitmapMemoryCache {
    private static final String TAG = "MemoryCache";
    private Map<String, Bitmap> cache = Collections.synchronizedMap(new LinkedHashMap(10, 1.5f, true));
    private long size = 0;
    private long limit = 1000000;

    public CBBitmapMemoryCache() {
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit) {
        this.limit = new_limit;
    }

    public Bitmap get(String id) {
        if (this.cache.containsKey(id)) {
            return this.cache.get(id);
        }
        return null;
    }

    public void put(String id, Bitmap bitmap) {
        try {
            if (this.cache.containsKey(id)) {
                this.size -= getSizeInBytes(this.cache.get(id));
            }
            this.cache.put(id, bitmap);
            this.size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void checkSize() {
        if (this.size > this.limit) {
            Iterator<Map.Entry<String, Bitmap>> iter = this.cache.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Bitmap> entry = iter.next();
                this.size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (this.size <= this.limit) {
                    return;
                }
            }
        }
    }

    public void clear() {
        this.cache.clear();
    }

    long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return 0L;
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
