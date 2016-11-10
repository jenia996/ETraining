package com.example.ajax.myapplication.loader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Ajax on 18.10.2016.
 */

public class MemCache {

    private Integer CACHE_SIZE;
    private LruCache<String, Bitmap> memCahe;

    public MemCache() {
        this.CACHE_SIZE = Math.min((int) Runtime.getRuntime().maxMemory() / 4, 1 << 25);
        this.memCahe = new LruCache<>(CACHE_SIZE);
    }

    public MemCache(Integer CACHE_SIZE) {
        this.CACHE_SIZE = CACHE_SIZE;
    }

    public Bitmap get(String id) {
        return memCahe.get(id);
    }

    public void put(String id, Bitmap bitmap) {
        memCahe.put(id, bitmap);
    }

    public void clear() {
        memCahe.evictAll();
    }
}
