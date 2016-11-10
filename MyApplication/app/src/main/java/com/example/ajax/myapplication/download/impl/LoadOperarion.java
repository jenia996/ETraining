package com.example.ajax.myapplication.download.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.HttpClient;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.loader.MemCache;

import java.io.IOException;

/**
 * Created by Ajax on 19.10.2016.
 */

public class LoadOperarion implements OwnAsyncTask<String, Void, Bitmap> {

    private static final String TAG = "Load_Operation";

    public LoadOperarion(MemCache memCache) {
    }

    @Override
    public Bitmap doInBackground(String s, ProgressCallback<Void> progressCallback) throws IOException {
        return BitmapFactory.decodeStream(HttpClient.getStream(s));
    }
}
