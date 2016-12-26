package com.example.ajax.myapplication.utils;

import android.util.LruCache;


public class RequestCache {

    private static RequestCache mInstance;
    private final LruCache<String, String> memCache;

    private RequestCache() {
        final Integer CACHE_SIZE = Math.min((int) Runtime.getRuntime().maxMemory() / 6, 1 << 25);
        this.memCache = new LruCache<>(CACHE_SIZE);
    }

    public static RequestCache getInstance() {
        if (mInstance == null) {
            mInstance = new RequestCache();
        }
        return mInstance;
    }

    public String get(final String id) {
        return memCache.get(id);
    }

    public void put(final String request, final String response) {
        memCache.put(request, response);
    }

    public void clear() {
        memCache.evictAll();
    }
}
