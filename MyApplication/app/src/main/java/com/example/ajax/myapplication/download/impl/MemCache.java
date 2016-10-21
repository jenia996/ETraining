package com.example.ajax.myapplication.download.impl;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by Ajax on 18.10.2016.
 */

public class MemCache {

    private static final String TAG = "MEM_CACHE";
    private Integer CACHE_SIZE = 1 << 25;
    private LruCache<String, Bitmap> memCahe = new LruCache<>(CACHE_SIZE);

    public Bitmap get(String id) {
        Bitmap result = memCahe.get(id);
        if (result == null) {
            Log.d(TAG, "Null entry");
            return null;
        }
        return result;
    }

    public void put(String id, Bitmap bitmap) {
        memCahe.put(id, bitmap);
    }

    public void clear() {
        memCahe.evictAll();
    }
}
