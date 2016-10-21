package com.example.ajax.myapplication.download.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.HttpClient;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;

/**
 * Created by Ajax on 19.10.2016.
 */

public class LoadOperarion implements OwnAsyncTask<String, Void, Bitmap> {

    private static final String TAG = "Load_Operation";
    private MemCache memCache;

    public LoadOperarion(MemCache memCache) {
        this.memCache = memCache;
    }

    @Override
    public Bitmap doInBackground(String s, ProgressCallback<Void> progressCallback) {
        final Bitmap bitmap = memCache.get(s);
        if (bitmap != null) {
            Log.d(TAG, "From_Memory");
            return bitmap;
        } else {
            Log.d(TAG, "From_Web");
            return BitmapFactory.decodeStream(HttpClient.getStream(s));
        }
    }
}
