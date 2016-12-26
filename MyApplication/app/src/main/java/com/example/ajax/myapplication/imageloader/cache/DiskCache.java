package com.example.ajax.myapplication.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.ajax.myapplication.imageloader.BitmapProcessor;
import com.example.ajax.myapplication.utils.HashHelper;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DiskCache {

    private static final int DISK_CACHE_INDEX = 0;
    private static final String DISK_CACHE_SUBDIR = "covers";
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 100; // 100MB
    private static boolean mDiskCacheStarting = true;
    private static DiskCache diskCache;
    private final Object mDiskCacheLock = new Object();
    private DiskLruCache mDiskLruCache;
    private Context context;
    private BitmapProcessor bitmapProcessor;

    private DiskCache() {
    }

    public static DiskCache getInstance() {
        if (diskCache == null) {
            diskCache = new DiskCache();
        }

        return diskCache;
    }

    public void requestInit(final Context context) {
        this.context = context;

        bitmapProcessor = new BitmapProcessor();

        new DiskCacheTask(this).execute(DiskCacheTask.INIT);
    }

    public void addBitmapToDiskCache(final String key, final Bitmap value) {
        if (key == null || value == null) {
            return;
        }
        final String cacheKey = HashHelper.sha256(key);
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null) {
                OutputStream out = null;

                try {
                    final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(cacheKey);

                    if (snapshot == null) {
                        final DiskLruCache.Editor editor = mDiskLruCache.edit(cacheKey);

                        if (editor != null) {
                            out = editor.newOutputStream(DISK_CACHE_INDEX);

                            value.compress(Bitmap.CompressFormat.JPEG, 100, out);

                            editor.commit();
                            out.close();
                        }
                    } else {
                        snapshot.getInputStream(DISK_CACHE_INDEX).close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public Bitmap getBitmapFromDiskCache(final String key) {
        Bitmap bitmap = null;

        final String cacheKey = HashHelper.sha256(key);

        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (mDiskLruCache != null) {
                InputStream inputStream = null;

                try {
                    final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(cacheKey);

                    if (snapshot != null) {

                        inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);

                        if (inputStream != null) {
                            final FileDescriptor fd = ((FileInputStream) inputStream).getFD();

                            bitmap = bitmapProcessor.decodeSampledBitmapFromDescriptor(fd);
                        }
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return bitmap;
    }

    public void requestFlush() {
        new DiskCacheTask(this).execute(DiskCacheTask.FLUSH);
    }

    public void requestClose() {
        new DiskCacheTask(this).execute(DiskCacheTask.CLOSE);
    }

    public void requestTearDown() {
        new DiskCacheTask(this).execute(DiskCacheTask.TEAR_DOWN);
    }

    public final void tearDown() {
        synchronized (mDiskCacheLock) {
            mDiskCacheStarting = true;

            if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
                try {
                    mDiskLruCache.delete();

                } catch (final IOException e) {
                    e.printStackTrace();
                }

                mDiskLruCache = null;
            }
        }
    }

    private void init() throws IOException {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
                final File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);

                if (!cacheDir.exists()) {
                    cacheDir.mkdir();
                }

                if (cacheDir.getUsableSpace() > DISK_CACHE_SIZE) {
                    mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE);
                } else {
                    //TODO don't use disk cache
                }
            }

            mDiskCacheStarting = false; // Finished initialization
            mDiskCacheLock.notifyAll(); // Wake any waiting threads
        }
    }

    private File getDiskCacheDir(final Context context, final String diskCacheSubdir) {

        final String cachePath = (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                !Environment.isExternalStorageRemovable()) && context.getExternalCacheDir() != null ? context.getExternalCacheDir().getPath() : context
                .getCacheDir().getPath();

        return new File(cachePath + File.separator + diskCacheSubdir);

    }

    private void flush() {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null) {
                try {
                    mDiskLruCache.flush();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void close() {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null) {
                if (!mDiskLruCache.isClosed()) {
                    try {
                        mDiskLruCache.close();
                        mDiskLruCache = null;
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class DiskCacheTask extends AsyncTask<Integer, Void, Void> {

        static final int INIT = 1;
        static final int FLUSH = 2;
        static final int CLOSE = 3;
        static final int TEAR_DOWN = 4;
        private final DiskCache diskCache;

        DiskCacheTask(final DiskCache diskCache) {
            this.diskCache = diskCache;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            switch (params[0]) {
                case INIT:
                    try {
                        diskCache.init();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case FLUSH:
                    diskCache.flush();
                    break;
                case CLOSE:
                    diskCache.close();
                    break;
                case TEAR_DOWN:
                    diskCache.tearDown();
                    break;
            }

            return null;
        }
    }
}
