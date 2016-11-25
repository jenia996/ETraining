package com.example.ajax.myapplication.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemCache {

    private final LruCache<String, Bitmap> memCahe;

    public MemCache() {
        final Integer CACHE_SIZE = Math.min((int) Runtime.getRuntime().maxMemory() / 4, 1 << 25);
        this.memCahe = new LruCache<>(CACHE_SIZE);
    }

    public Bitmap get(final String id) {
        return memCahe.get(id);
    }

    public void put(final String id, final Bitmap bitmap) {
        memCahe.put(id, bitmap);
    }

    public void clear() {
        memCahe.evictAll();
    }
}
