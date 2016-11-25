package com.example.ajax.myapplication.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.HttpClient;
import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.imageloader.cache.DiskCache;
import com.example.ajax.myapplication.imageloader.cache.MemCache;
import com.example.ajax.myapplication.utils.ContextHolder;

import java.io.IOException;
import java.lang.ref.WeakReference;

class KnightOfTheBrushImpl implements KnightOfTheBrush {

    private final MemCache memCache;
    private final DiskCache mDiskCache;
    private final Loader loader = new Loader();
    private final BitmapOperation bitmapOperation;

    KnightOfTheBrushImpl() {
        memCache = new MemCache();
        mDiskCache = DiskCache.getInstance();
        mDiskCache.requestInit(ContextHolder.get());
        bitmapOperation = new BitmapOperation();
    }

    public void clearCache() {
        memCache.clear();
    }

    @Override
    public void drawBitmap(final ImageView imageView, final String imageUrl) {
        synchronized (memCache) {
            final Bitmap bitmap = memCache.get(imageUrl);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                return;
            }
        }
        final Bitmap bitmap = mDiskCache.getBitmapFromDiskCache(imageUrl);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }

        loader.execute(bitmapOperation, new ImageData(imageUrl, imageView.getLayoutParams().width, imageView
                .getLayoutParams().height), new BitmapResultCallback(imageView, imageUrl) {

            @Override
            public void onError(final Exception e) {
                super.onError(e);
            }

            @Override
            public void onSucess(final Bitmap bitmap) {
                if (bitmap != null) {
                    mDiskCache.addBitmapToDiskCache(imageUrl, bitmap);
                }
                synchronized (memCache) {
                    if (bitmap != null) {
                        memCache.put(imageUrl, bitmap);
                    }
                }

                super.onSucess(bitmap);
            }
        });

    }

    private static class ImageData {

        String url;
        int width;
        int height;

        ImageData(final String url, final int width, final int height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }
    }

    private static class BitmapResultCallback implements OnResultCallback<Bitmap, Void> {

        private static final String TAG = BitmapResultCallback.class.getSimpleName();
        private final WeakReference<ImageView> imageView;
        private final String value;

        BitmapResultCallback(final ImageView imageView, final String value) {
            this.imageView = new WeakReference<>(imageView);
            imageView.setTag(value);
            this.value = value;
        }

        @Override
        public void onSucess(final Bitmap bitmap) {
            final ImageView imageView = this.imageView.get();
            if (imageView != null) {
                final Object tag = imageView.getTag();
                if (tag != null && tag.equals(value)) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        @Override
        public void onError(final Exception e) {
            Log.d(TAG, e.getMessage());
        }

        @Override
        public void onProgressChange(final Void aVoid) {

        }
    }

    private static class BitmapOperation implements OwnAsyncTask<ImageData, Void, Bitmap> {

        static Bitmap decode(final byte[] bytes, final int reqWidth, final int reqHeight) {

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }

        private static int calculateInSampleSize(final BitmapFactory.Options options, final int reqWidth, final int reqHeight) {

            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        @Override
        public Bitmap doInBackground(final ImageData imageData, final ProgressCallback<Void> progressCallback) throws IOException {
            final byte[] image = HttpClient.getByteArray(imageData.url);
            return decode(image, imageData.width, imageData.height);
        }
    }

}
