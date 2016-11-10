package com.example.ajax.myapplication.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.example.HttpClient;
import com.example.ajax.myapplication.download.OnResultCallback;
import com.example.ajax.myapplication.download.OwnAsyncTask;
import com.example.ajax.myapplication.download.ProgressCallback;
import com.example.ajax.myapplication.download.impl.Loader;
import com.example.ajax.myapplication.utils.ContextHolder;

import java.io.IOException;
import java.lang.ref.WeakReference;

class KnightOfTheBrushImpl implements KnightOfTheBrush {
    private static final String TAG = "KnightOfTheBrush";
    private final MemCache memCache;
    private final Object mDiskCacheLock = new Object();
    private DiskCache mDiskCache;
    private Loader loader = new Loader();
    private BitmapOperation bitmapOperation;

    KnightOfTheBrushImpl() {
        memCache = new MemCache();
        mDiskCache = DiskCache.getInstance();
        mDiskCache.requestInit(ContextHolder.get());
        bitmapOperation = new BitmapOperation();
    }

    @Override
    public void drawBitmap(ImageView imageView, final String imageUrl) {
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
            public void onError(Exception e) {
                super.onError(e);
            }

            @Override
            public void onSucess(Bitmap bitmap) {
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

        public ImageData(String url, int width, int height) {
            this.url = url;
            this.width = width;
            this.height = height;
        }
    }

    private static class BitmapResultCallback implements OnResultCallback<Bitmap, Void> {

        private static final String TAG = BitmapResultCallback.class.getSimpleName();
        private final WeakReference<ImageView> imageView;
        private String value;


        BitmapResultCallback(ImageView imageView, String value) {
            this.imageView = new WeakReference<>(imageView);
            imageView.setTag(value);
            this.value = value;
        }

        @Override
        public void onSucess(Bitmap bitmap) {
            ImageView imageView = this.imageView.get();
            if (imageView != null) {
                Object tag = imageView.getTag();
                if (tag != null && tag.equals(value)) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, e.getMessage());
        }

        @Override
        public void onProgressChange(Void aVoid) {

        }
    }

    private static class BitmapOperation implements OwnAsyncTask<ImageData, Void, Bitmap> {

        static Bitmap decode(byte[] bytes, int reqWidth, int reqHeight) {

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }

        private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

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
        public Bitmap doInBackground(ImageData imageData, ProgressCallback<Void> progressCallback) throws IOException {
            byte[] image = HttpClient.getByteArray(imageData.url);
            return decode(image, imageData.width, imageData.height);
        }
    }

}
