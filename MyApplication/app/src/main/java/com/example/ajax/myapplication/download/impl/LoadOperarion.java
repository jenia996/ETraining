package com.example.ajax.myapplication.download.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.HttpClient;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;

import java.io.IOException;

public class LoadOperarion implements OwnAsyncTask<String, Void, Bitmap> {

    @Override
    public Bitmap doInBackground(final String s, final ProgressCallback<Void> progressCallback) throws IOException {
        return BitmapFactory.decodeStream(HttpClient.getStream(s));
    }
}
