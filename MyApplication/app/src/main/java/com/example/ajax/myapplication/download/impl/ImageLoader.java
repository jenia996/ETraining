package com.example.ajax.myapplication.download.impl;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.example.ajax.myapplication.download.OnResultCallback;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    private static final String TAG = "IMAGE_LOADER";
    private final ExecutorService executorService;
    private final Handler handler;
    private Map<ImageView, String> imageViewMap = Collections.synchronizedMap(new
            WeakHashMap<ImageView, String>());
    private MemCache memCache = new MemCache();
    private Loader loader;
    public ImageLoader() {
        handler = new Handler(Looper.getMainLooper());
        this.executorService = Executors.newCachedThreadPool();
        loader = new Loader();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        Log.d(TAG, "Destroyed");
    }

    public void displayImage(final String path, final ImageView imageView) {
        imageViewMap.put(imageView, path);
        final Bitmap bitmap = memCache.get(path);
        if (bitmap != null) {
            Log.d(TAG, "Memory");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                }
            });
        } else {
            Log.d(TAG, "Load");
            loader.execute(new LoadOperarion(memCache), path, new OnResultCallback<Bitmap, Void>() {
                @Override
                public void onProgressChange(Void aVoid) {

                }

                @Override
                public void onSucess(final Bitmap bitmap) {
                    memCache.put(path, bitmap);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }


}
